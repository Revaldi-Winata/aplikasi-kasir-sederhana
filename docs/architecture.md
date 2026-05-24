# Arsitektur Aplikasi - Toko Berkah Jaya (Updated)

Sistem Toko Berkah Jaya dirancang menggunakan bahasa pemrograman **Java** dengan arsitektur **3-Tier / MVC (Model-View-Controller)** yang ketat dan aman, disesuaikan dengan pendekatan **Programmatic GUI (Opsi B)** di **NetBeans IDE**.

## 1. Komponen Arsitektur

1. **`database`**:
   - `Koneksi.java`: Menyediakan koneksi ke MySQL. Memiliki fitur manajemen *connection pool* sederhana jika diperlukan, menggunakan pola desain Singleton.

2. **`model`**:
   - Kelas POJO (Plain Old Java Object) representasi tabel database. Terdiri dari: `Barang`, `Customer`, `Kategori`, `Penjualan`, `User`.

3. **`service`** (Controller/DAO Layer - *Business Logic*):
   - Menangani operasi database melalui **PreparedStatement** (mencegah SQL Injection).
   - Menerapkan **Database Transaction** (`setAutoCommit(false)`, `commit`, `rollback`) untuk operasi yang melibatkan lebih dari satu tabel.
   - Menerapkan blok `try-with-resources` untuk mencegah *resource leak* (memori penuh karena koneksi tidak ditutup).

4. **`ui`** (View Layer - *Programmatic Swing*):
   - Murni kode Java tanpa `.form` file.
   - Menggunakan `BorderLayout` dan `GridBagLayout` untuk responsivitas form.
   - Memisahkan thread UI (Event Dispatch Thread / EDT) dari thread pemrosesan data, jika memungkinkan, agar antarmuka tidak *freeze* saat query database lambat.
   - Terdapat event listener (`ItemListener`, `DocumentListener`) untuk merespons perubahan input seketika (seperti kalkulasi total bayar).

5. **`util`**:
   - `Formatter.java`: Utilitas sanitasi input, format mata uang, penanganan *error parsing* agar aplikasi tidak *crash* (`NumberFormatException`).

---

## 2. Aliran Data Transaksi Penjualan Terproteksi (Protected Data Flow)

```mermaid
sequenceDiagram
    autonumber
    actor Kasir
    participant UI as PenjualanForm
    participant Service as PenjualanService
    participant DB as MySQL Database

    Kasir->>UI: Pilih Customer & Barang
    UI->>UI: Tampilkan harga jual (Real-time)
    Kasir->>UI: Input Jumlah Beli
    UI->>UI: Validasi Input Numerik & Kalkulasi Total
    Kasir->>UI: Klik "Simpan"
    
    UI->>Service: simpanTransaksi(Penjualan p)
    activate Service
    Service->>DB: connection.setAutoCommit(false)
    Service->>DB: INSERT INTO tb_penjualan (...)
    
    Note over Service,DB: Race Condition Prevention
    Service->>DB: UPDATE tb_barang SET stok = stok - ? WHERE id_barang = ? AND stok >= ?
    
    alt Jika update affected_rows == 0
        DB-->>Service: Gagal (Stok Kurang/Habis)
        Service->>DB: connection.rollback()
        Service-->>UI: Exception("Stok tidak mencukupi saat ini!")
        UI->>Kasir: JOptionPane (Peringatan Stok Habis)
    else Jika update berhasil
        DB-->>Service: OK
        Service->>DB: connection.commit()
        Service-->>UI: Transaksi Sukses
        UI->>UI: Reset Form & Refresh JTable
        UI->>Kasir: JOptionPane (Transaksi Berhasil)
    end
    deactivate Service
```
