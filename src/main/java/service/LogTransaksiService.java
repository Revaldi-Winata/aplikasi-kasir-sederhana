package service;

import database.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogTransaksiService {
    
    public List<Map<String, Object>> getTransactionsLog(int limit, int offset) {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT p.no_faktur, p.tgl_transaksi AS tanggal, p.total_bayar AS total, p.total_bayar AS bayar, 0 AS kembali, u.nama_lengkap AS kasir " +
                     "FROM tb_penjualan p " +
                     "JOIN tb_user u ON p.id_user = u.id_user " +
                     "ORDER BY p.tgl_transaksi DESC LIMIT ? OFFSET ?";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, limit);
            pstmt.setInt(2, offset);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("no_faktur", rs.getString("no_faktur"));
                    map.put("tanggal", rs.getTimestamp("tanggal"));
                    map.put("total", rs.getDouble("total"));
                    map.put("bayar", rs.getDouble("bayar"));
                    map.put("kembali", rs.getDouble("kembali"));
                    map.put("kasir", rs.getString("kasir"));
                    list.add(map);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public int getTotalTransactionsCount() {
        String sql = "SELECT COUNT(*) AS total FROM tb_penjualan";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public Map<String, Object> getTransactionHeader(String noFaktur) {
        String sql = "SELECT p.no_faktur, p.tgl_transaksi AS tanggal, p.total_bayar AS total, p.total_bayar AS bayar, 0 AS kembali, u.nama_lengkap AS kasir " +
                     "FROM tb_penjualan p " +
                     "JOIN tb_user u ON p.id_user = u.id_user " +
                     "WHERE p.no_faktur = ?";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, noFaktur);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("no_faktur", rs.getString("no_faktur"));
                    map.put("tanggal", rs.getTimestamp("tanggal"));
                    map.put("total", rs.getDouble("total"));
                    map.put("bayar", rs.getDouble("bayar"));
                    map.put("kembali", rs.getDouble("kembali"));
                    map.put("kasir", rs.getString("kasir"));
                    return map;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Map<String, Object>> getTransactionDetails(String noFaktur) {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT d.id_barang, b.nama_barang, d.harga_satuan AS harga_jual, d.jumlah_beli AS jumlah, d.subtotal " +
                     "FROM tb_detail_penjualan d " +
                     "JOIN tb_barang b ON d.id_barang = b.id_barang " +
                     "JOIN tb_penjualan p ON d.id_jual = p.id_jual " +
                     "WHERE p.no_faktur = ?";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, noFaktur);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("nama_barang", rs.getString("nama_barang"));
                    map.put("harga_jual", rs.getDouble("harga_jual"));
                    map.put("jumlah", rs.getInt("jumlah"));
                    map.put("subtotal", rs.getDouble("subtotal"));
                    list.add(map);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
