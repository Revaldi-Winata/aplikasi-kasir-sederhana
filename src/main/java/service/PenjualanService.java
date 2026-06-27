/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import database.Koneksi;
import model.DetailPenjualan;
import model.Penjualan;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Service layer untuk transaksi penjualan (Header-Detail pattern).
 *
 * @author Revaldi
 */
public class PenjualanService {

    // ==========================================
    // [Logika Inti] SIMPAN TRANSAKSI (ATOMIC)
    // ==========================================
    // Fungsi ini dipanggil saat kasir menekan tombol "Simpan Transaksi" di UI PenjualanForm.
    // Menggunakan teknik "Database Transaction" agar jika di tengah jalan mati lampu/error, data tidak jadi tersimpan (konsisten).
    //
    // Alur Kerjanya:
    // 1. Matikan mode simpan-otomatis (AutoCommit = false)
    // 2. Buat Nomor Struk baru (misal: FK-20231024-0001)
    // 3. Simpan data global (Tanggal, Kasir, Customer, Total Belanja) ke tb_penjualan (tabel INDUK)
    // 4. Ambil ID Penjualan yang baru saja terbentuk dari database
    // 5. Looping untuk setiap barang yang ada di dalam keranjang:
    //    a. Kurangi sisa stok barang di tb_barang. Jika stok ternyata kurang, BATALKAN SEMUA (Rollback)!
    //    b. Simpan rincian barang tersebut ke tb_detail_penjualan (tabel ANAK)
    // 6. Jika semua langkah di atas aman, BARU SIMPAN PERMANEN ke Database (Commit).
    public boolean simpanTransaksi(Penjualan p) throws Exception {
        Connection conn = null;
        try {
            conn = Koneksi.getKoneksi();
            conn.setAutoCommit(false);

            // Generate no_faktur
            String noFaktur = generateNoFaktur(conn);
            p.setNoFaktur(noFaktur);

            // 1. [Simpan Induk] Masukkan nota penjualan (header) ke tb_penjualan
            String sqlHeader = "INSERT INTO tb_penjualan (no_faktur, tgl_transaksi, id_customer, total_bayar, id_user) VALUES (?,?,?,?,?)";
            int idJual;
            try (PreparedStatement psHeader = conn.prepareStatement(sqlHeader, Statement.RETURN_GENERATED_KEYS)) {
                psHeader.setString(1, noFaktur);
                psHeader.setTimestamp(2, new java.sql.Timestamp(p.getTglTransaksi().getTime())); // Simpan tanggal & jam saat ini
                psHeader.setString(3, p.getIdCustomer());
                psHeader.setDouble(4, p.getTotalBayar());
                psHeader.setInt(5, p.getIdUser());
                psHeader.executeUpdate(); // Jalankan SQL

                // [Mekanisme Lanjut] Ambil "id_jual" (Auto Increment) yang baru saja tercipta di MySQL
                try (ResultSet generatedKeys = psHeader.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        idJual = generatedKeys.getInt(1); // Simpan ID ini untuk tabel detail nanti
                        p.setIdJual(idJual);
                    } else {
                        throw new Exception("Gagal mendapatkan ID transaksi!"); // Jika gagal, paksa error
                    }
                }
            }

            // 2. [Simpan Anak] Looping satu per satu barang yang ada di dalam keranjang
            String sqlStok = "UPDATE tb_barang SET stok = stok - ? WHERE id_barang = ? AND stok >= ?";
            String sqlDetail = "INSERT INTO tb_detail_penjualan (id_jual, id_barang, harga_beli, harga_satuan, jumlah_beli, subtotal) VALUES (?,?,?,?,?,?)";

            for (DetailPenjualan detail : p.getDetails()) {
                // 2a. [Keamanan Data] Kurangi stok barang, tapi pastikan sisa stok (di database) cukup dengan pesanan!
                try (PreparedStatement psStok = conn.prepareStatement(sqlStok)) {
                    psStok.setInt(1, detail.getJumlahBeli()); // Stok akan dikurangi sebanyak yg dibeli
                    psStok.setString(2, detail.getIdBarang());
                    psStok.setInt(3, detail.getJumlahBeli()); // Syarat: sisa stok aslinya HARUS >= qty yg dibeli
                    
                    int affectedRows = psStok.executeUpdate(); // Jika stok kurang, hasilnya 0 (gagal eksekusi)
                    if (affectedRows == 0) {
                        conn.rollback(); // [BATAL!] Batalkan seluruh penyimpanan transaksi, termasuk nota induknya!
                        String namaBarang = detail.getNamaBarang() != null ? detail.getNamaBarang() : detail.getIdBarang();
                        throw new Exception("Stok tidak mencukupi untuk barang: " + namaBarang); // Lempar pesan error ke layar
                    }
                }

                // 2b. [Catat Rincian] Jika stok aman, catat barang ini ke dalam tabel detail_penjualan
                try (PreparedStatement psDetail = conn.prepareStatement(sqlDetail)) {
                    psDetail.setInt(1, idJual); // Masukkan id_jual (dari tabel induk) untuk mengaitkannya
                    psDetail.setString(2, detail.getIdBarang());
                    psDetail.setDouble(3, detail.getHargaBeli());
                    psDetail.setDouble(4, detail.getHargaSatuan());
                    psDetail.setInt(5, detail.getJumlahBeli());
                    psDetail.setDouble(6, detail.getSubtotal());
                    psDetail.executeUpdate(); // Jalankan SQL
                }
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Mengambil semua header transaksi penjualan untuk ditampilkan di JTable riwayat.
     */
    public List<Penjualan> getAllPenjualan() {
        List<Penjualan> list = new ArrayList<>();
        String sql = "SELECT p.*, c.nama_customer "
                   + "FROM tb_penjualan p "
                   + "LEFT JOIN tb_customer c ON p.id_customer = c.id_customer "
                   + "ORDER BY p.id_jual DESC";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Penjualan p = new Penjualan();
                p.setIdJual(rs.getInt("id_jual"));
                p.setNoFaktur(rs.getString("no_faktur"));
                p.setTglTransaksi(rs.getDate("tgl_transaksi"));
                p.setIdCustomer(rs.getString("id_customer"));
                p.setTotalBayar(rs.getDouble("total_bayar"));
                p.setIdUser(rs.getInt("id_user"));
                p.setNamaCustomer(rs.getString("nama_customer"));
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Mengambil detail item untuk satu transaksi berdasarkan id_jual.
     */
    public List<DetailPenjualan> getDetailByIdJual(int idJual) {
        List<DetailPenjualan> list = new ArrayList<>();
        String sql = "SELECT d.*, b.nama_barang "
                   + "FROM tb_detail_penjualan d "
                   + "LEFT JOIN tb_barang b ON d.id_barang = b.id_barang "
                   + "WHERE d.id_jual = ? "
                   + "ORDER BY d.id_detail";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idJual);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetailPenjualan d = new DetailPenjualan();
                    d.setIdDetail(rs.getInt("id_detail"));
                    d.setIdJual(rs.getInt("id_jual"));
                    d.setIdBarang(rs.getString("id_barang"));
                    d.setHargaSatuan(rs.getDouble("harga_satuan"));
                    d.setJumlahBeli(rs.getInt("jumlah_beli"));
                    d.setSubtotal(rs.getDouble("subtotal"));
                    d.setNamaBarang(rs.getString("nama_barang"));
                    list.add(d);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public String getPreviewNoFaktur() {
        try (Connection conn = database.Koneksi.getKoneksi()) {
            return generateNoFaktur(conn);
        } catch (SQLException e) {
            e.printStackTrace();
            return "FK-ERROR";
        }
    }

    // [Logika Helper] Generate nomor faktur dinamis dengan format: FK-yyyyMMdd-XXXX (cth: FK-20231024-0001)
    // Angka XXXX di akhir (Sequence) akan di-reset otomatis mulai dari 0001 setiap berganti hari baru.
    private String generateNoFaktur(Connection conn) throws SQLException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String today = sdf.format(new java.util.Date());
        String prefix = "FK-" + today + "-";

        String sql = "SELECT no_faktur FROM tb_penjualan WHERE no_faktur LIKE ? ORDER BY no_faktur DESC LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, prefix + "%");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String lastFaktur = rs.getString("no_faktur");
                    int lastSeq = Integer.parseInt(lastFaktur.substring(lastFaktur.lastIndexOf('-') + 1));
                    return prefix + String.format("%04d", lastSeq + 1);
                }
            }
        }
        return prefix + "0001";
    }
}
