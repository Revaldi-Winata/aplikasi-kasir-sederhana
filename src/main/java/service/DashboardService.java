package service;

import database.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DashboardService {

    public int getTotalBarang() {
        int total = 0;
        String query = "SELECT COUNT(*) FROM tb_barang";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    public int getTotalKategori() {
        int total = 0;
        String query = "SELECT COUNT(*) FROM tb_kategori";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    public int getTotalCustomer() {
        int total = 0;
        String query = "SELECT COUNT(*) FROM tb_customer";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    public double getTotalPendapatan() {
        double total = 0;
        String query = "SELECT SUM(total_bayar) FROM tb_penjualan";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                total = rs.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    public java.util.List<java.util.Map<String, Object>> getRecentTransactions(int limit) {
        java.util.List<java.util.Map<String, Object>> list = new java.util.ArrayList<>();
        String query = "SELECT p.tgl_transaksi, p.no_faktur, c.nama_customer, p.total_bayar " +
                       "FROM tb_penjualan p " +
                       "LEFT JOIN tb_customer c ON p.id_customer = c.id_customer " +
                       "ORDER BY p.tgl_transaksi DESC, p.id_jual DESC LIMIT ?";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    java.util.Map<String, Object> map = new java.util.HashMap<>();
                    map.put("tanggal", rs.getDate("tgl_transaksi"));
                    map.put("faktur", rs.getString("no_faktur"));
                    map.put("customer", rs.getString("nama_customer") != null ? rs.getString("nama_customer") : "Umum");
                    map.put("total", rs.getDouble("total_bayar"));
                    list.add(map);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public java.util.List<java.util.Map<String, Object>> getTopSellingProducts(String periodFilter) {
        java.util.List<java.util.Map<String, Object>> list = new java.util.ArrayList<>();
        String dateCondition = getDateCondition(periodFilter);
        
        String query = "SELECT b.nama_barang, SUM(d.jumlah_beli) as total_terjual " +
                       "FROM tb_detail_penjualan d " +
                       "JOIN tb_penjualan p ON d.id_jual = p.id_jual " +
                       "JOIN tb_barang b ON d.id_barang = b.id_barang " +
                       "WHERE " + dateCondition + " " +
                       "GROUP BY d.id_barang, b.nama_barang " +
                       "ORDER BY total_terjual DESC LIMIT 10";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                java.util.Map<String, Object> map = new java.util.HashMap<>();
                map.put("barang", rs.getString("nama_barang"));
                map.put("terjual", rs.getInt("total_terjual"));
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public java.util.List<java.util.Map<String, Object>> getTopCustomers(String periodFilter) {
        java.util.List<java.util.Map<String, Object>> list = new java.util.ArrayList<>();
        String dateCondition = getDateCondition(periodFilter);
        
        String query = "SELECT c.nama_customer, COUNT(p.id_jual) as freq, SUM(p.total_bayar) as total_belanja " +
                       "FROM tb_penjualan p " +
                       "JOIN tb_customer c ON p.id_customer = c.id_customer " +
                       "WHERE " + dateCondition + " " +
                       "GROUP BY p.id_customer, c.nama_customer " +
                       "ORDER BY freq DESC LIMIT 10";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                java.util.Map<String, Object> map = new java.util.HashMap<>();
                map.put("customer", rs.getString("nama_customer"));
                map.put("freq", rs.getInt("freq"));
                map.put("total", rs.getDouble("total_belanja"));
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private String getDateCondition(String periodFilter) {
        if ("Hari ini".equalsIgnoreCase(periodFilter)) {
            return "DATE(p.tgl_transaksi) = CURDATE()";
        } else if ("7 Hari Terakhir".equalsIgnoreCase(periodFilter)) {
            return "p.tgl_transaksi >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)";
        } else if ("Sebulan Terakhir".equalsIgnoreCase(periodFilter)) {
            return "p.tgl_transaksi >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH)";
        }
        return "1=1"; // default no filter
    }
}
