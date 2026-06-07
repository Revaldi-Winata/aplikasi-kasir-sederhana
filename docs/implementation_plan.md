# Rencana Implementasi - Toko Berkah Jaya (Updated)

Rencana kerja bertahap untuk menyelesaikan implementasi sistem Toko Berkah Jaya berdasarkan hasil evaluasi Multi-Agent Brainstorming (Opsi B - Programmatic GUI).

## Fase 1: Setup Database & Koneksi
- [ ] Eksekusi script SQL dari `database_setup.md` di phpMyAdmin (XAMPP).
- [ ] Uji coba class `Koneksi.java` untuk memastikan koneksi ke `localhost:3306` database `tokoberkahjaya` berhasil.

## Fase 2: Pemetaan Objek Model (POJO)
Membuat kelas-kelas Java biasa (`New Java Class...`) di package `model` dengan variabel instansi, constructor, getter, dan setter:
- [ ] `model.Kategori`
- [ ] `model.Barang`
- [ ] `model.Customer`
- [ ] `model.User`
- [ ] `model.Penjualan`

## Fase 3: Implementasi Layanan Database (Service Layer / DAO)
Membuat kelas-kelas di package `service` dengan JDBC queries (menggunakan `PreparedStatement` dan `try-with-resources`):
- [ ] **`KategoriService`**: CRUD tabel `tb_kategori`
- [ ] **`BarangService`**: CRUD tabel `tb_barang`
- [ ] **`CustomerService`**: CRUD tabel `tb_customer`
- [ ] **`UserService`**: Login authentication & CRUD tabel `tb_user`
- [ ] **`PenjualanService`**:
  - Implementasi metode `simpanTransaksi()` yang menerapkan **Database Transaction**.
  - Menggunakan `connection.setAutoCommit(false)`.
  - Melakukan `INSERT INTO tb_penjualan`.
  - Melakukan `UPDATE tb_barang SET stok = stok - ? WHERE id_barang = ? AND stok >= ?`.
  - Jika update return `0` (stok kurang), lakukan `connection.rollback()`.
  - Jika sukses, lakukan `connection.commit()`.

## Fase 4: Desain UI (Pure Programmatic Swing)
Mendesain tampilan GUI secara manual (Opsi B) di package `ui`.
- [ ] **`LoginForm`**: Autentikasi user.
- [ ] **`MenuUtama`**: Frame induk dengan navigasi (misal `JMenuBar`).
- [ ] **Form Data Master** (`KategoriForm`, `BarangForm`, `CustomerForm`): Form entri dengan `JTable` untuk melihat data.
- [ ] **`PenjualanForm`** (Modul Transaksi Kasir Utama):
  - Menggunakan `BorderLayout` dan `GridBagLayout` untuk menata komponen input dan tabel riwayat.
  - Memuat data customer dan barang ke `JComboBox`.
  - Event `ItemListener` pada `JComboBox` barang untuk update kolom harga secara *real-time*.
  - Event `DocumentListener` pada kolom Jumlah Beli untuk kalkulasi total bayar.
  - Pemanggilan `PenjualanService.simpanTransaksi()` dengan penanganan error (`try-catch`).
  - Menampilkan `JOptionPane` untuk notifikasi sukses atau gagal (misal stok kurang).

## Fase 5: Pengujian
- [ ] Pengujian skenario stok kosong/kurang (Race Condition / Business Validation).
- [ ] Pengujian kelancaran update data master dan transaksi.
