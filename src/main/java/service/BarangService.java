package service;

import database.Koneksi;
import model.Barang;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// [Service] Class ini adalah "Jembatan" antara tampilan form Barang (UI) dengan Database MySQL (tb_barang).
// Semua logika SQL (SELECT, INSERT, UPDATE, DELETE) untuk barang ada di sini.
public class BarangService {

    // [Logika Read] Fungsi ini dipanggil untuk mengambil SEMUA data barang dari database.
    // Dipanggil oleh: BarangForm.loadData() (jika tanpa filter) atau form lain yang butuh daftar barang.
    public List<Barang> getAllBarang() {
        List<Barang> list = new ArrayList<>(); // Membuat wadah (list) kosong
        String sql = "SELECT * FROM tb_barang ORDER BY id_barang"; // Perintah SQL
        
        try (Connection conn = Koneksi.getKoneksi(); // Buka jalur ke database
             PreparedStatement ps = conn.prepareStatement(sql); // Siapkan wadah eksekusi SQL
             ResultSet rs = ps.executeQuery()) { // Jalankan SQL dan simpan hasilnya di 'rs'
            
            // Loop membaca setiap baris dari hasil tabel database
            while (rs.next()) {
                Barang b = new Barang(); // Buat "kardus" objek Barang kosong
                // Isi kardus dengan data dari tiap kolom
                b.setIdBarang(rs.getString("id_barang"));
                b.setIdKategori(rs.getInt("id_kategori"));
                b.setNamaBarang(rs.getString("nama_barang"));
                b.setSatuan(rs.getString("satuan"));
                b.setHargaBeli(rs.getDouble("harga_beli"));
                b.setHargaJual(rs.getDouble("harga_jual"));
                b.setStok(rs.getInt("stok"));
                
                list.add(b); // Masukkan barang yang sudah dibungkus ke dalam list
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Jika error, cetak penyebabnya ke console (untuk debugging)
        }
        return list; // Kembalikan list penuh ke pemanggil (misal UI form)
    }

    // [Logika Search & Filter] Dipanggil ketika user mengetik di kotak pencarian atau memilih dropdown kategori.
    public List<Barang> searchBarang(String keyword, String filterKategori) {
        List<Barang> list = new ArrayList<>();
        // Query SQL dengan kondisi LIKE (mencari huruf yang mirip)
        String sql = "SELECT b.* FROM tb_barang b LEFT JOIN tb_kategori k ON b.id_kategori = k.id_kategori WHERE (b.nama_barang LIKE ? OR b.id_barang LIKE ?) ";
        
        // Jika ada filter kategori, tambahkan rumus "AND kategori = X" ke perintah SQL
        if (filterKategori != null && !filterKategori.isEmpty() && !filterKategori.equals("Semua Kategori")) {
            sql += " AND k.nama_kategori = ? ";
        }
        sql += " ORDER BY b.id_barang";

        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            // Ganti tanda '?' di atas dengan teks asli dari keyword
            // Tanda % berarti "huruf apapun di depan/belakang" (wildcard)
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            
            if (filterKategori != null && !filterKategori.isEmpty() && !filterKategori.equals("Semua Kategori")) {
                ps.setString(3, filterKategori);
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Barang b = new Barang();
                    b.setIdBarang(rs.getString("id_barang"));
                    b.setIdKategori(rs.getInt("id_kategori"));
                    b.setNamaBarang(rs.getString("nama_barang"));
                    b.setSatuan(rs.getString("satuan"));
                    b.setHargaBeli(rs.getDouble("harga_beli"));
                    b.setHargaJual(rs.getDouble("harga_jual"));
                    b.setStok(rs.getInt("stok"));
                    list.add(b);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // [Logika Create] Dipanggil ketika user mengklik tombol "Simpan" (Baru) di UI.
    public boolean tambahBarang(Barang b) {
        // Query untuk menyimpan baris baru ke tabel. Tanda ? akan diisi otomatis di bawah.
        String sql = "INSERT INTO tb_barang (id_barang, id_kategori, nama_barang, satuan, harga_beli, harga_jual, stok) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            // Mengisi tanda ? satu per satu dari data object Barang 'b'
            ps.setString(1, b.getIdBarang());
            ps.setInt(2, b.getIdKategori());
            ps.setString(3, b.getNamaBarang());
            ps.setString(4, b.getSatuan());
            ps.setDouble(5, b.getHargaBeli());
            ps.setDouble(6, b.getHargaJual());
            ps.setInt(7, b.getStok());
            
            // executeUpdate() mengembalikan jumlah baris yang berhasil disimpan (harus > 0 agar sukses)
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // [Logika Update] Dipanggil ketika user menyeleksi tabel (klik) lalu mengubah nilainya dan klik "Simpan".
    public boolean updateBarang(Barang b) {
        // Query UPDATE mengubah data lama dengan data baru, dicari berdasarkan id_barang-nya
        String sql = "UPDATE tb_barang SET id_kategori=?, nama_barang=?, satuan=?, harga_beli=?, harga_jual=?, stok=? WHERE id_barang=?";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            // Urutannya disesuaikan dengan tanda ? di atas (WHERE id_barang=? itu yang ke-7)
            ps.setInt(1, b.getIdKategori());
            ps.setString(2, b.getNamaBarang());
            ps.setString(3, b.getSatuan());
            ps.setDouble(4, b.getHargaBeli());
            ps.setDouble(5, b.getHargaJual());
            ps.setInt(6, b.getStok());
            ps.setString(7, b.getIdBarang());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // [Logika Delete] Dipanggil saat user mengklik tombol "Hapus" pada UI.
    public boolean hapusBarang(String idBarang) {
        String sql = "DELETE FROM tb_barang WHERE id_barang=?";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idBarang);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // [Logika Helper] Mencari 1 barang saja berdasarkan ID. Dipanggil oleh modul keranjang di PenjualanForm.
    public Barang getBarangById(String idBarang) {
        String sql = "SELECT * FROM tb_barang WHERE id_barang=?";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idBarang);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Barang b = new Barang();
                    b.setIdBarang(rs.getString("id_barang"));
                    b.setIdKategori(rs.getInt("id_kategori"));
                    b.setNamaBarang(rs.getString("nama_barang"));
                    b.setSatuan(rs.getString("satuan"));
                    b.setHargaBeli(rs.getDouble("harga_beli"));
                    b.setHargaJual(rs.getDouble("harga_jual"));
                    b.setStok(rs.getInt("stok"));
                    return b;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // [Logika Auto Increment Custom] Membuat ID otomatis (contoh: BRG001, BRG002)
    // Dipanggil oleh UI saat form di-clear (tombol Tambah / Clear).
    public String generateId() {
        String sql = "SELECT id_barang FROM tb_barang";
        int max = 0;
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String idStr = rs.getString(1); // Mengambil tulisan 'BRG005'
                String numStr = idStr.replaceAll("\\D+", ""); // Extract hanya angkanya saja: '005'
                if (!numStr.isEmpty()) {
                    int num = Integer.parseInt(numStr);
                    if (num > max) max = num; // Mencari angka terbesar saat ini
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Tambahkan angka terbesar dengan 1, lalu bungkus dalam format 3 digit (00X)
        return "BRG" + String.format("%03d", max + 1);
    }
}
