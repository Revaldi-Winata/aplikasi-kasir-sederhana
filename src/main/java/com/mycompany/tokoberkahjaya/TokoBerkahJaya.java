package com.mycompany.tokoberkahjaya;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import ui.LoginForm;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

// [Main Class] Titik awal (entry point) aplikasi dijalankan
public class TokoBerkahJaya {
    public static void main(String[] args) {
        // [Konfigurasi UI] Menggunakan FlatLaf agar tampilan modern
        try {
            // Mengatur font default seluruh aplikasi menjadi Segoe UI ukuran 14
            UIManager.put("defaultFont", new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
            
            // Membuat sudut membulat (rounded) sebesar 8px pada tombol
            UIManager.put("Button.arc", 8);
            // Membuat sudut membulat 8px pada komponen umum (panel, dll)
            UIManager.put("Component.arc", 8);
            // Membuat sudut membulat 8px pada bar proses
            UIManager.put("ProgressBar.arc", 8);
            // Membuat sudut membulat 8px pada kotak input teks
            UIManager.put("TextComponent.arc", 8);
            
            // Mengatur jarak (padding dalam) tombol agar proporsional
            UIManager.put("Button.margin", new java.awt.Insets(4, 14, 4, 14));
            
            // Memperbesar ukuran kotak input (teks) agar lebih mudah diklik
            UIManager.put("TextComponent.margin", new java.awt.Insets(8, 12, 8, 12));
            // Menyesuaikan padding dropdown (ComboBox) agar tingginya pas
            UIManager.put("ComboBox.padding", new java.awt.Insets(4, 12, 4, 12));
            
            // Menerapkan tema FlatLaf dengan gaya terang ala desain MacOS
            FlatMacLightLaf.setup();
        } catch (Exception ex) {
            // [Error Handling] Jika tema gagal dimuat, tampilkan peringatan di konsol
            System.err.println("Failed to initialize FlatLaf");
        }

        // [Navigasi Awal] Menjalankan UI secara aman di antrean memori Java (EDT)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Membuat lalu menampilkan layar LoginForm pertama kali saat aplikasi dibuka
                new LoginForm().setVisible(true);
            }
        });
    }
}