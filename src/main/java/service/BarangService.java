/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import database.Koneksi;
import model.Barang;
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
public class BarangService {

    public List<Barang> getAllBarang() {
        List<Barang> list = new ArrayList<>();
        String sql = "SELECT * FROM tb_barang ORDER BY id_barang";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Barang b = new Barang();
                b.setIdBarang(rs.getString("id_barang"));
                b.setIdKategori(rs.getInt("id_kategori"));
                b.setNamaBarang(rs.getString("nama_barang"));
                b.setSatuan(rs.getString("satuan"));
                b.setHargaJual(rs.getDouble("harga_jual"));
                b.setStok(rs.getInt("stok"));
                list.add(b);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean tambahBarang(Barang b) {
        String sql = "INSERT INTO tb_barang (id_barang, id_kategori, nama_barang, satuan, harga_jual, stok) VALUES (?,?,?,?,?,?)";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, b.getIdBarang());
            ps.setInt(2, b.getIdKategori());
            ps.setString(3, b.getNamaBarang());
            ps.setString(4, b.getSatuan());
            ps.setDouble(5, b.getHargaJual());
            ps.setInt(6, b.getStok());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateBarang(Barang b) {
        String sql = "UPDATE tb_barang SET id_kategori=?, nama_barang=?, satuan=?, harga_jual=?, stok=? WHERE id_barang=?";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, b.getIdKategori());
            ps.setString(2, b.getNamaBarang());
            ps.setString(3, b.getSatuan());
            ps.setDouble(4, b.getHargaJual());
            ps.setInt(5, b.getStok());
            ps.setString(6, b.getIdBarang());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

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

    public String generateId() {
        String sql = "SELECT id_barang FROM tb_barang";
        int max = 0;
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String idStr = rs.getString(1);
                String numStr = idStr.replaceAll("\\D+", ""); // Extract only numbers
                if (!numStr.isEmpty()) {
                    int num = Integer.parseInt(numStr);
                    if (num > max) max = num;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "BRG" + String.format("%03d", max + 1);
    }
}
