# Arsitektur Database - Toko Berkah Jaya

Sistem menggunakan database relational **MySQL** dengan 5 tabel utama yang saling berelasi.

## 1. Skema Tabel (Tables Schema)

### A. `tb_customer`
Menyimpan data identitas pelanggan tetap.
- **id_customer** (VARCHAR(10), Primary Key)
- **nama_customer** (VARCHAR(100), NOT NULL)
- **alamat** (TEXT)
- **telepon** (VARCHAR(15))

### B. `tb_kategori`
Kategori pengelompokan barang.
- **id_kategori** (INT, Primary Key, AUTO_INCREMENT)
- **nama_kategori** (VARCHAR(50), NOT NULL)

### C. `tb_barang`
Data master barang/produk.
- **id_barang** (VARCHAR(10), Primary Key)
- **id_kategori** (INT, Foreign Key referencing `tb_kategori(id_kategori)`)
- **nama_barang** (VARCHAR(100), NOT NULL)
- **satuan** (VARCHAR(20))
- **harga_jual** (DOUBLE, NOT NULL)
- **stok** (INT, NOT NULL)

### D. `tb_user`
Data pengguna/staf kasir yang mengoperasikan sistem.
- **id_user** (INT, Primary Key, AUTO_INCREMENT)
- **username** (VARCHAR(50), UNIQUE, NOT NULL)
- **password** (VARCHAR(255), NOT NULL)
- **nama_lengkap** (VARCHAR(100), NOT NULL)
- **level** (ENUM('Admin', 'Petugas'), NOT NULL)

### E. `tb_penjualan`
Mencatat transaksi penjualan barang.
- **id_jual** (INT, Primary Key, AUTO_INCREMENT)
- **tgl_transaksi** (DATE, NOT NULL)
- **id_customer** (VARCHAR(10), Foreign Key referencing `tb_customer(id_customer)`)
- **id_barang** (VARCHAR(10), Foreign Key referencing `tb_barang(id_barang)`)
- **jumlah_beli** (INT, NOT NULL)
- **total_bayar** (DOUBLE, NOT NULL)
- **id_user** (INT, Foreign Key referencing `tb_user(id_user)`)

---

## 2. Struktur Relasi (Entity Relationship Diagram - ERD)

```mermaid
erDiagram
    tb_customer ||--o{ tb_penjualan : "melakukan"
    tb_user ||--o{ tb_penjualan : "melayani"
    tb_kategori ||--o{ tb_barang : "mengelompokkan"
    tb_barang ||--o{ tb_penjualan : "dijual"

    tb_customer {
        varchar id_customer PK
        varchar nama_customer
        text alamat
        varchar telepon
    }

    tb_kategori {
        int id_kategori PK
        varchar nama_kategori
    }

    tb_barang {
        varchar id_barang PK
        int id_kategori FK
        varchar nama_barang
        varchar satuan
        double harga_jual
        int stok
    }

    tb_user {
        int id_user PK
        varchar username
        varchar password
        varchar nama_lengkap
        enum level
    }

    tb_penjualan {
        int id_jual PK
        date tgl_transaksi
        varchar id_customer FK
        varchar id_barang FK
        int jumlah_beli
        double total_bayar
        int id_user FK
    }
```
