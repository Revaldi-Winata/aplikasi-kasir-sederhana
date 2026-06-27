/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import database.Koneksi;
import model.Customer;
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
public class CustomerService {

    public List<Customer> getAllCustomer() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM tb_customer ORDER BY id_customer";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Customer c = new Customer();
                c.setIdCustomer(rs.getString("id_customer"));
                c.setNamaCustomer(rs.getString("nama_customer"));
                c.setAlamat(rs.getString("alamat"));
                c.setTelepon(rs.getString("telepon"));
                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Customer> searchCustomer(String keyword) {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM tb_customer WHERE nama_customer LIKE ? OR id_customer LIKE ? OR alamat LIKE ? ORDER BY id_customer";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            ps.setString(3, kw);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Customer c = new Customer();
                    c.setIdCustomer(rs.getString("id_customer"));
                    c.setNamaCustomer(rs.getString("nama_customer"));
                    c.setAlamat(rs.getString("alamat"));
                    c.setTelepon(rs.getString("telepon"));
                    list.add(c);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean tambahCustomer(Customer c) {
        String sql = "INSERT INTO tb_customer (id_customer, nama_customer, alamat, telepon) VALUES (?,?,?,?)";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getIdCustomer());
            ps.setString(2, c.getNamaCustomer());
            ps.setString(3, c.getAlamat());
            ps.setString(4, c.getTelepon());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateCustomer(Customer c) {
        String sql = "UPDATE tb_customer SET nama_customer=?, alamat=?, telepon=? WHERE id_customer=?";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNamaCustomer());
            ps.setString(2, c.getAlamat());
            ps.setString(3, c.getTelepon());
            ps.setString(4, c.getIdCustomer());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean hapusCustomer(String idCustomer) {
        String sql = "DELETE FROM tb_customer WHERE id_customer=?";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idCustomer);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String generateId() {
        String sql = "SELECT id_customer FROM tb_customer";
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
        return "CUS" + String.format("%03d", max + 1);
    }
}
