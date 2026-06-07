package database;

import java.sql.Connection;

public class TestKoneksi {
    public static void main(String[] args) {
        System.out.println("Mencoba menghubungkan ke MySQL (Toko Berkah Jaya)...");
        try (Connection conn = Koneksi.getKoneksi()) {
            if (conn != null) {
                System.out.println("============================================");
                System.out.println("KONEKSI BERHASIL!");
                System.out.println("Aplikasi sudah terhubung dengan database.");
                System.out.println("============================================");
            }
        } catch (Exception e) {
            System.err.println("============================================");
            System.err.println("KONEKSI GAGAL!");
            System.err.println("Pastikan modul MySQL di XAMPP sudah berstatus 'Start'.");
            System.err.println("Error detail: " + e.getMessage());
            System.err.println("============================================");
        }
    }
}
