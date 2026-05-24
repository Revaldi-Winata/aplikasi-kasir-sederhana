# Persyaratan Sistem - Toko Berkah Jaya

Dokumen ini menjelaskan kebutuhan fungsional dan bisnis untuk aplikasi desktop kasir Toko Berkah Jaya.

## 1. Kebutuhan Fungsional (Functional Requirements)

### A. Manajemen Data Master (CRUD)
Sistem memiliki modul pengelolaan data master untuk entitas berikut:
1. **Data Barang**:
   - Mengelola informasi produk yang dijual.
   - Atribut: ID Barang, Nama, Kategori, Satuan, Harga Jual, dan Stok.
2. **Data Customer**:
   - Mengelola identitas pelanggan tetap.
   - Atribut: ID Customer, Nama, Alamat, dan No. Telepon.

### B. Modul Transaksi Penjualan
Modul utama untuk memproses transaksi kasir dengan ketentuan:
1. **Form Transaksi**:
   - Memilih **Customer** dari daftar yang sudah ada via dropdown (`JComboBox`).
   - Memilih **Barang** yang akan dibeli dari dropdown (`JComboBox`).
   - Secara otomatis menampilkan harga jual barang di input field (`JTextField`) saat barang dipilih.
   - Menginput **Jumlah Beli**.
   - Menghitung **Total Harga** secara otomatis (`Harga Jual * Jumlah Beli`).
2. **Logika Pengurangan Stok**:
   - Ketika transaksi disimpan (`INSERT INTO tb_penjualan`), sistem harus secara otomatis mengurangi stok barang terkait (`tb_barang`) menggunakan query:
     ```sql
     UPDATE tb_barang SET stok = stok - ? WHERE id_barang = ?
     ```

### C. Laporan Penjualan
- Menampilkan riwayat transaksi penjualan dalam bentuk tabel (`JTable`).
- Informasi yang dirangkum meliputi:
  - Nama Pembeli (Customer)
  - Nama Barang yang dibeli
  - Jumlah Beli
  - Total Pendapatan/Total Bayar

---

## 2. Validasi Bisnis (Business Rules & Validation)

1. **Pencegahan Stok Minus**:
   - Sistem **wajib mencegah** transaksi jika stok barang bernilai `0` atau kurang dari jumlah yang diminta oleh pembeli.
   - Tampilkan dialog peringatan menggunakan `JOptionPane` jika stok tidak mencukupi.
2. **Validasi Input**:
   - Menggunakan `JComboBox` untuk ID Customer dan ID Barang agar data yang masuk valid (tidak asal ketik).
   - Penggunaan `ActionListener` pada `JComboBox` Barang agar update harga bersifat real-time/instan.
