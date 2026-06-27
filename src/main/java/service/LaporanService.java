package service;

import database.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LaporanService {

    public List<Object[]> getLaporanRingkasan(Date tglMulai, Date tglAkhir, String idCustomer, String idKategori) {
        List<Object[]> data = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT DISTINCT p.no_faktur, p.tgl_transaksi, c.nama_customer, p.total_bayar, u.nama_lengkap " +
            "FROM tb_penjualan p " +
            "LEFT JOIN tb_customer c ON p.id_customer = c.id_customer " +
            "LEFT JOIN tb_user u ON p.id_user = u.id_user " +
            "LEFT JOIN tb_detail_penjualan dp ON p.no_faktur = dp.no_faktur " +
            "LEFT JOIN tb_barang b ON dp.id_barang = b.id_barang " +
            "WHERE 1=1 "
        );

        if (tglMulai != null) sql.append("AND p.tgl_transaksi >= ? ");
        if (tglAkhir != null) sql.append("AND p.tgl_transaksi <= ? ");
        if (idCustomer != null && !idCustomer.isEmpty() && !idCustomer.equals("Semua Customer")) sql.append("AND p.id_customer = ? ");
        if (idKategori != null && !idKategori.isEmpty() && !idKategori.equals("Semua Kategori")) sql.append("AND b.id_kategori = ? ");
        
        sql.append("ORDER BY p.tgl_transaksi DESC, p.no_faktur DESC");

        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement pst = conn.prepareStatement(sql.toString())) {
            
            int paramIndex = 1;
            if (tglMulai != null) pst.setDate(paramIndex++, new java.sql.Date(tglMulai.getTime()));
            if (tglAkhir != null) pst.setDate(paramIndex++, new java.sql.Date(tglAkhir.getTime()));
            if (idCustomer != null && !idCustomer.isEmpty() && !idCustomer.equals("Semua Customer")) pst.setString(paramIndex++, idCustomer);
            if (idKategori != null && !idKategori.isEmpty() && !idKategori.equals("Semua Kategori")) pst.setString(paramIndex++, idKategori);
            
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String faktur = rs.getString("no_faktur");
                Date tanggal = rs.getDate("tgl_transaksi");
                String customer = rs.getString("nama_customer");
                if (customer == null) customer = "Umum";
                double total = rs.getDouble("total_bayar");
                String kasir = rs.getString("nama_lengkap");
                
                data.add(new Object[]{faktur, tanggal, customer, kasir, total});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public List<Object[]> getLaporanDetail(Date tglMulai, Date tglAkhir, String idCustomer, String idKategori) {
        List<Object[]> data = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT p.no_faktur, p.tgl_transaksi, c.nama_customer, b.nama_barang, k.nama_kategori, dp.harga_satuan, dp.jumlah_beli, dp.subtotal " +
            "FROM tb_detail_penjualan dp " +
            "JOIN tb_penjualan p ON dp.no_faktur = p.no_faktur " +
            "JOIN tb_barang b ON dp.id_barang = b.id_barang " +
            "JOIN tb_kategori k ON b.id_kategori = k.id_kategori " +
            "LEFT JOIN tb_customer c ON p.id_customer = c.id_customer " +
            "WHERE 1=1 "
        );

        if (tglMulai != null) sql.append("AND p.tgl_transaksi >= ? ");
        if (tglAkhir != null) sql.append("AND p.tgl_transaksi <= ? ");
        if (idCustomer != null && !idCustomer.isEmpty() && !idCustomer.equals("Semua Customer")) sql.append("AND p.id_customer = ? ");
        if (idKategori != null && !idKategori.isEmpty() && !idKategori.equals("Semua Kategori")) sql.append("AND b.id_kategori = ? ");
        
        sql.append("ORDER BY p.tgl_transaksi DESC, p.no_faktur DESC");

        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement pst = conn.prepareStatement(sql.toString())) {
            
            int paramIndex = 1;
            if (tglMulai != null) pst.setDate(paramIndex++, new java.sql.Date(tglMulai.getTime()));
            if (tglAkhir != null) pst.setDate(paramIndex++, new java.sql.Date(tglAkhir.getTime()));
            if (idCustomer != null && !idCustomer.isEmpty() && !idCustomer.equals("Semua Customer")) pst.setString(paramIndex++, idCustomer);
            if (idKategori != null && !idKategori.isEmpty() && !idKategori.equals("Semua Kategori")) pst.setString(paramIndex++, idKategori);
            
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String faktur = rs.getString("no_faktur");
                Date tanggal = rs.getDate("tgl_transaksi");
                String customer = rs.getString("nama_customer");
                if (customer == null) customer = "Umum";
                String barang = rs.getString("nama_barang");
                String kategori = rs.getString("nama_kategori");
                double harga = rs.getDouble("harga_satuan");
                int qty = rs.getInt("jumlah_beli");
                double subtotal = rs.getDouble("subtotal");
                
                data.add(new Object[]{faktur, tanggal, customer, barang, kategori, harga, qty, subtotal});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
