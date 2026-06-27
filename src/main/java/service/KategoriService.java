/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import database.Koneksi;
import model.Kategori;
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
public class KategoriService {

    public List<Kategori> getAllKategori() {
        List<Kategori> list = new ArrayList<>();
        String sql = "SELECT * FROM tb_kategori ORDER BY id_kategori";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Kategori k = new Kategori();
                k.setIdKategori(rs.getInt("id_kategori"));
                k.setNamaKategori(rs.getString("nama_kategori"));
                list.add(k);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Kategori> searchKategori(String keyword) {
        List<Kategori> list = new ArrayList<>();
        String sql = "SELECT * FROM tb_kategori WHERE nama_kategori LIKE ? ORDER BY id_kategori";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Kategori k = new Kategori();
                    k.setIdKategori(rs.getInt("id_kategori"));
                    k.setNamaKategori(rs.getString("nama_kategori"));
                    list.add(k);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean tambahKategori(Kategori k) {
        String sql = "INSERT INTO tb_kategori (nama_kategori) VALUES (?)";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, k.getNamaKategori());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateKategori(Kategori k) {
        String sql = "UPDATE tb_kategori SET nama_kategori=? WHERE id_kategori=?";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, k.getNamaKategori());
            ps.setInt(2, k.getIdKategori());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean hapusKategori(int idKategori) {
        String sql = "DELETE FROM tb_kategori WHERE id_kategori=?";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idKategori);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getNextAutoIncrement() {
        String sql = "SELECT AUTO_INCREMENT FROM information_schema.tables WHERE table_name = 'tb_kategori' AND table_schema = DATABASE()";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int nextId = rs.getInt(1);
                // Jika database baru kosong dan AUTO_INCREMENT mengembalikan 0 atau null, fallback ke 1
                return String.valueOf(nextId > 0 ? nextId : 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "1";
    }
}
