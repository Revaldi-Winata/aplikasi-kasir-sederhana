# 🛒 Toko Berkah Jaya - Point of Sales (POS) System

Toko Berkah Jaya adalah aplikasi desktop berbasis **Java Swing** yang dirancang untuk memanajemen inventaris dan operasional toko secara efisien. Aplikasi ini mendukung sistem otentikasi dengan pembagian hak akses (Admin dan Kasir), pengelolaan data master (Barang, Kategori, Customer, User), serta manajemen profil pengguna.

## ✨ Fitur Utama

- **Sistem Otentikasi & Otorisasi**: Login aman dengan pembatasan hak akses berbasis *role*.
  - `Admin`: Memiliki akses penuh ke seluruh menu (Data Kategori, Data Barang, Data Customer, Data User).
  - `Kasir`: Akses dibatasi pada fitur kasir, dashboard, dan pengaturan profil.
- **Manajemen Kategori**: Fitur CRUD (Create, Read, Update, Delete) untuk kategori produk.
- **Manajemen Barang**: CRUD data barang lengkap dengan relasi ke kategori, informasi stok, satuan, dan harga jual.
- **Manajemen Customer**: CRUD data pelanggan (Nama, Telepon, Alamat).
- **Manajemen User**: CRUD data kredensial karyawan beserta penetapan hak akses (Admin/Kasir).
- **Profil Pengguna**: Antarmuka bagi pengguna untuk memperbarui data diri secara mandiri tanpa memerlukan intervensi admin.
- **Modern User Interface (UI)**: Desain GUI yang interaktif, bersih (*clean*), presisi (*pixel-perfect alignment*), dan memberikan *User Experience* (UX) yang sangat baik.
- **Smart Data Entry**: Penggunaan ID yang di-generate otomatis dari database (*auto-increment*) dan keamanan form dari kesalahan *input*.

## 💻 Tech Stack

- **Bahasa Pemrograman**: Java 
- **GUI Framework**: Java Swing (AWT/Swing)
- **Database**: MySQL (via JDBC Driver)
- **Build Tool**: Maven

## 🗄️ Struktur Database

Database yang digunakan bernama `toko_berkah_jaya_db`. Berikut adalah struktur tabel utama:
- `tb_user`: Menyimpan kredensial sistem (ID User, Nama Lengkap, Username, Password, Role).
- `tb_kategori`: Menyimpan daftar klasifikasi barang (ID Kategori, Nama Kategori).
- `tb_barang`: Menyimpan detail entitas barang (ID Barang, ID Kategori, Nama Barang, Satuan, Harga, Stok).
- `tb_customer`: Menyimpan kontak pelanggan (ID Customer, Nama, Telepon, Alamat).

## 🚀 Panduan Instalasi dan Penggunaan

1. **Persiapan Database**
   - Pastikan layanan **MySQL** pada **XAMPP** atau server sejenis telah berjalan.
   - Buat database baru di `phpMyAdmin` dengan nama `toko_berkah_jaya_db`.
   - Lakukan *import* berkas skema SQL (jika tersedia) atau biarkan aplikasi men-generate tabel sesuai kebutuhan arsitektur.

2. **Konfigurasi Database di Kode**
   - Buka utilitas database di dalam kode (`util.DatabaseUtil` / `util.DBConnection`).
   - Pastikan parameter koneksi telah sesuai:
   ```java
   String url = "jdbc:mysql://localhost:3306/toko_berkah_jaya_db";
   String user = "root";
   String password = "";
   ```

3. **Menjalankan Aplikasi**
   - Buka direktori proyek menggunakan IDE Java seperti **NetBeans**.
   - Jalankan proses kompilasi melalui Maven: `Clean and Build`.
   - Buka `LoginForm.java` dan jalankan (*Run File* atau tekan **Shift+F6**).
   - Gunakan akun `Admin` untuk masuk pertama kali dan mengatur data master.

---
*Dokumentasi ini di-generate secara otomatis sebagai bagian dari pengembangan sistem.*
