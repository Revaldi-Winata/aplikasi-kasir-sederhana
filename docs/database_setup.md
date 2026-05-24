```sql
-- Buat database jika belum ada
CREATE DATABASE IF NOT EXISTS tokoberkahjaya;
USE tokoberkahjaya;

-- Hapus tabel jika sudah ada (sesuai urutan foreign key untuk menghindari error constraint)
DROP TABLE IF EXISTS tb_penjualan;
DROP TABLE IF EXISTS tb_barang;
DROP TABLE IF EXISTS tb_kategori;
DROP TABLE IF EXISTS tb_customer;
DROP TABLE IF EXISTS tb_user;

-- 1. Tabel tb_user
CREATE TABLE tb_user (
    id_user INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    nama_lengkap VARCHAR(100) NOT NULL,
    level ENUM('Admin', 'Petugas') NOT NULL
);

-- 2. Tabel tb_customer
CREATE TABLE tb_customer (
    id_customer VARCHAR(10) PRIMARY KEY,
    nama_customer VARCHAR(100) NOT NULL,
    alamat TEXT,
    telepon VARCHAR(15)
);

-- 3. Tabel tb_kategori
CREATE TABLE tb_kategori (
    id_kategori INT AUTO_INCREMENT PRIMARY KEY,
    nama_kategori VARCHAR(50) NOT NULL
);

-- 4. Tabel tb_barang
CREATE TABLE tb_barang (
    id_barang VARCHAR(10) PRIMARY KEY,
    id_kategori INT,
    nama_barang VARCHAR(100) NOT NULL,
    satuan VARCHAR(20),
    harga_jual DOUBLE NOT NULL,
    stok INT NOT NULL,
    FOREIGN KEY (id_kategori) REFERENCES tb_kategori(id_kategori) ON DELETE SET NULL ON UPDATE CASCADE
);

-- 5. Tabel tb_penjualan
CREATE TABLE tb_penjualan (
    id_jual INT AUTO_INCREMENT PRIMARY KEY,
    tgl_transaksi DATE NOT NULL,
    id_customer VARCHAR(10),
    id_barang VARCHAR(10),
    jumlah_beli INT NOT NULL,
    total_bayar DOUBLE NOT NULL,
    id_user INT,
    FOREIGN KEY (id_customer) REFERENCES tb_customer(id_customer) ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (id_barang) REFERENCES tb_barang(id_barang) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (id_user) REFERENCES tb_user(id_user) ON DELETE SET NULL ON UPDATE CASCADE
);

-- ==============================================================
-- Insert Data Awal (Dummy Data) untuk Keperluan Testing
-- ==============================================================

-- Dummy User
INSERT INTO tb_user (username, password, nama_lengkap, level) VALUES 
('admin', 'admin123', 'Administrator Utama', 'Admin'),
('kasir1', 'kasir123', 'Budi Kasir', 'Petugas');

-- Dummy Kategori
INSERT INTO tb_kategori (nama_kategori) VALUES 
('Makanan Ringan'), ('Minuman'), ('Sembako');

-- Dummy Barang
INSERT INTO tb_barang (id_barang, id_kategori, nama_barang, satuan, harga_jual, stok) VALUES 
('BRG001', 1, 'Indomie Goreng', 'Bungkus', 3500, 100),
('BRG002', 2, 'Aqua 600ml', 'Botol', 4000, 50),
('BRG003', 3, 'Beras Maknyuss 5kg', 'Karung', 65000, 20);

-- Dummy Customer
INSERT INTO tb_customer (id_customer, nama_customer, alamat, telepon) VALUES 
('CUS001', 'Andi Sucipto', 'Jl. Merdeka No 1, Jakarta', '081234567890'),
('CUS002', 'Siti Aminah', 'Jl. Sudirman No 2, Bandung', '089876543210');
```
