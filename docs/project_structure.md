# Struktur Proyek - Toko Berkah Jaya (NetBeans IDE)

Berikut adalah struktur *package* dan *class* yang harus dibuat di NetBeans IDE untuk proyek `TokoBerkahJaya`. Sesuai dengan pendekatan "Opsi B", file antarmuka (GUI) dibuat sebagai **Java Class** biasa (Pure Programmatic Java Code), bukan JFrame Form bawaan NetBeans GUI Builder.

## Root Package: `com.mycompany.tokoberkahjaya`
- `TokoBerkahJaya.java` -> **Java Main Class** (Titik masuk / Entry point aplikasi)

## Package: `database`
- `Koneksi.java` -> **Java Class** (Menangani koneksi JDBC ke MySQL dengan pola Singleton)

## Package: `model` (POJO - Plain Old Java Object)
- `Barang.java` -> **Java Class** (Mapping tabel `tb_barang`)
- `Customer.java` -> **Java Class** (Mapping tabel `tb_customer`)
- `Kategori.java` -> **Java Class** (Mapping tabel `tb_kategori`)
- `Penjualan.java` -> **Java Class** (Mapping tabel `tb_penjualan`)
- `User.java` -> **Java Class** (Mapping tabel `tb_user`)

## Package: `service` (Data Access Object / Logika Bisnis)
- `BarangService.java` -> **Java Class** (CRUD dan validasi stok barang)
- `CustomerService.java` -> **Java Class** (CRUD pelanggan)
- `KategoriService.java` -> **Java Class** (CRUD kategori)
- `PenjualanService.java` -> **Java Class** (Transaksi penjualan, `setAutoCommit(false)`, `commit`, `rollback`)
- `UserService.java` -> **Java Class** (Proses login dan manajemen pengguna)

## Package: `ui` (Antarmuka Pengguna - Programmatic GUI)
- `BarangForm.java` -> **Java Class** (Turunan `JPanel` atau `JFrame`)
- `CustomerForm.java` -> **Java Class** (Turunan `JPanel` atau `JFrame`)
- `KategoriForm.java` -> **Java Class** (Turunan `JPanel` atau `JFrame`)
- `LoginForm.java` -> **Java Class** (Turunan `JFrame` untuk login awal)
- `MenuUtama.java` -> **Java Class** (Turunan `JFrame` utama dengan `JMenuBar` atau `JDesktopPane`)
- `PenjualanForm.java` -> **Java Class** (Turunan `JPanel` atau `JFrame` untuk form kasir)

## Package: `util` (Kelas Bantuan)
- `Formatter.java` -> **Java Class** (Fungsi utilitas seperti format Rupiah `Rp`, sanitasi input angka)

---
**Catatan untuk Pembuatan di NetBeans:**
Pilih **New -> Java Class...** untuk semua file di atas. Hindari memilih `JFrame Form...` atau `JPanel Form...` karena kita akan menulis kode tata letak (layouting) menggunakan `GridBagLayout`, `BorderLayout`, dll. secara manual.
