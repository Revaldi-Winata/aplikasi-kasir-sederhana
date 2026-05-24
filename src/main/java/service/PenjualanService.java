/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import database.Koneksi;
import model.Penjualan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Revaldi
 */
public class PenjualanService {

    public boolean simpanTransaksi(Penjualan p) throws Exception {
        Connection conn = null;
        try {
            conn = Koneksi.getKoneksi();
            conn.setAutoCommit(false);

            // Update stok with guard: stok >= jumlah_beli
            String sqlStok = "UPDATE tb_barang SET stok = stok - ? WHERE id_barang = ? AND stok >= ?";
            try (PreparedStatement psStok = conn.prepareStatement(sqlStok)) {
                psStok.setInt(1, p.getJumlahBeli());
                psStok.setString(2, p.getIdBarang());
                psStok.setInt(3, p.getJumlahBeli());
                int affectedRows = psStok.executeUpdate();
                if (affectedRows == 0) {
                    conn.rollback();
                    throw new Exception("Stok tidak mencukupi!");
                }
            }

            // Insert penjualan record
            String sqlInsert = "INSERT INTO tb_penjualan (tgl_transaksi, id_customer, id_barang, jumlah_beli, total_bayar, id_user) VALUES (?,?,?,?,?,?)";
            try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert)) {
                psInsert.setDate(1, new java.sql.Date(p.getTglTransaksi().getTime()));
                psInsert.setString(2, p.getIdCustomer());
                psInsert.setString(3, p.getIdBarang());
                psInsert.setInt(4, p.getJumlahBeli());
                psInsert.setDouble(5, p.getTotalBayar());
                psInsert.setInt(6, p.getIdUser());
                psInsert.executeUpdate();
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

    public List<Penjualan> getAllPenjualan() {
        List<Penjualan> list = new ArrayList<>();
        String sql = "SELECT p.*, c.nama_customer, b.nama_barang "
                   + "FROM tb_penjualan p "
                   + "LEFT JOIN tb_customer c ON p.id_customer = c.id_customer "
                   + "LEFT JOIN tb_barang b ON p.id_barang = b.id_barang "
                   + "ORDER BY p.id_jual DESC";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Penjualan p = new Penjualan();
                p.setIdJual(rs.getInt("id_jual"));
                p.setTglTransaksi(rs.getDate("tgl_transaksi"));
                p.setIdCustomer(rs.getString("id_customer"));
                p.setIdBarang(rs.getString("id_barang"));
                p.setJumlahBeli(rs.getInt("jumlah_beli"));
                p.setTotalBayar(rs.getDouble("total_bayar"));
                p.setIdUser(rs.getInt("id_user"));
                p.setNamaCustomer(rs.getString("nama_customer"));
                p.setNamaBarang(rs.getString("nama_barang"));
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
