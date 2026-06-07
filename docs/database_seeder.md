# Database Seeder (Toko Berkah Jaya)

Silakan eksekusi (Copy-Paste) perintah SQL di bawah ini pada menu **SQL** di dalam phpMyAdmin (pastikan Anda sedang berada di dalam database `tokoberkahjaya`). Seeder ini akan membuatkan 2 akun *default* untuk Anda coba:

```sql
-- Memasukkan 2 akun awal (Admin & Petugas)
INSERT INTO `tb_user` (`username`, `password`, `nama_lengkap`, `level`) VALUES
('admin', 'admin123', 'Budi Santoso (Admin)', 'Admin'),
('kasir', 'kasir123', 'Siti Aminah (Petugas)', 'Petugas');
```

### Akun Login yang bisa Anda gunakan:

**1. Login sebagai Admin (Akses Penuh):**
* **Username:** `admin`
* **Password:** `admin123`

**2. Login sebagai Petugas (Akses Terbatas ke Transaksi Kasir & Customer saja):**
* **Username:** `kasir`
* **Password:** `kasir123`

*(Password yang disimpan tidak di-hash (Plain Text) karena ini masih dalam tahap pembelajaran/project akademik sesuai struktur yang telah dibuat).*
