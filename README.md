<div align="center">
  <img src="https://i.postimg.cc/L8ZVLW7C/logo.png" alt="Logo Toko Berkah Jaya" width="150"/>
  <p><i>*Catatan: Logo aplikasi ini dibuat menggunakan bantuan ChatGPT.</i></p>
  <h1>Aplikasi Kasir Sederhana (Toko Berkah Jaya POS)</h1>
  <p>Aplikasi point-of-sale dan manajemen inventaris desktop yang dibangun dengan Java Swing.</p>
  
  [![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.java.com/)
  [![MySQL](https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
  [![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)](https://maven.apache.org/)

  <br>
  <p><b><a href="README_EN.md">Read in English</a></b></p>
</div>

<br>

Toko Berkah Jaya menangani operasional toko harian. Sistem ini melacak inventaris, mengkategorikan produk, mendata pelanggan, dan memproses penjualan. Aplikasi ini juga mencakup manajemen pengguna dengan kontrol akses berbasis peran untuk administrator dan kasir.

## Fitur utama

- **Akses berbasis peran**: Administrator memiliki akses sistem penuh. Kasir hanya dapat mengakses area penjualan, dasbor, dan profil mereka sendiri.
- **Pelacakan inventaris**: Tambah, perbarui, dan hapus produk. Sistem melacak tingkat stok dan kategori produk.
- **Database pelanggan**: Simpan catatan informasi pelanggan tetap.
- **Point of sale**: Proses transaksi, hitung total dengan pembuatan nomor faktur otomatis, dan cetak setruk sebagai file PDF.
- **Log transaksi**: Pantau riwayat keseluruhan penjualan yang pernah terjadi.
- **Profil pengguna**: Staf dapat memperbarui kata sandi dan nama pengguna mereka sendiri tanpa harus merepotkan administrator.

## Tech stack

- **Bahasa**: Java (JDK 17+)
- **GUI framework**: Java Swing dan AWT
- **Database**: MySQL
- **Build tool**: Apache Maven
- **Pembuatan PDF**: Apache PDFBox

## Persyaratan sistem

- Java Development Kit (JDK) 17 atau lebih baru
- Server MySQL (melalui XAMPP, WAMP, atau mandiri)
- IDE seperti NetBeans atau IntelliJ IDEA (untuk mode pengembangan)

## Instalasi untuk Pengembangan (Development Setup)

Gunakan langkah ini jika Anda ingin melihat kode sumber, mengubah fitur, atau menjalankan aplikasi dari IDE.

### 1. Clone repositori

```bash
git clone https://github.com/username/TokoBerkahJaya.git
cd TokoBerkahJaya
```

### 2. Siapkan database

1. Jalankan server MySQL Anda melalui XAMPP atau layanan lokal lainnya.
2. Buat database baru bernama `tokoberkahjaya`.
3. Impor skema dan data awal. Anda dapat melakukan ini melalui antarmuka phpMyAdmin atau langsung dari baris perintah:

```bash
mysql -u root -p tokoberkahjaya < database/tokoberkahjaya.sql
```

### 3. Konfigurasi koneksi

Aplikasi terhubung ke MySQL menggunakan kredensial yang ditentukan di `src/main/java/database/Koneksi.java`. Jika pengaturan MySQL lokal Anda menggunakan kata sandi, perbarui baris ini:

```java
private static final String URL = "jdbc:mysql://localhost:3306/tokoberkahjaya";
private static final String USER = "root";
private static final String PASSWORD = ""; // Tambahkan kata sandi Anda di sini
```

### 4. Build dan jalankan

Buka proyek di NetBeans, klik kanan nama proyek, dan pilih **Clean and Build**. Setelah proses selesai, jalankan (Run) proyek.

Sebagai alternatif, jika Anda menggunakan terminal dan Maven:

```bash
mvn clean package
java -cp target/classes;target/dependency/* ui.LoginForm
```

## Instalasi untuk Penggunaan Nyata (Production Deployment)

Ikuti langkah ini jika Anda bersiap untuk mendistribusikan aplikasi dan memasangnya langsung di komputer toko (kasir). Komputer target tidak memerlukan *source code* atau aplikasi IDE.

### 1. Bangun file eksekutabel (JAR)

Di komputer pengembangan Anda (komputer yang ada kode sumbernya), jalankan perintah Maven berikut untuk membungkus aplikasi dan semua dependensinya ke dalam satu file JAR portabel:

```bash
mvn clean package
```

Ini akan menghasilkan file `TokoBerkahJaya-1.0-SNAPSHOT-jar-with-dependencies.jar` di dalam direktori `target/`. 

### 2. Persiapan komputer kasir

Pindahkan file JAR tersebut ke komputer kasir menggunakan flashdisk atau media lainnya (misalnya ke `D:\AplikasiToko\`). Pastikan komputer kasir telah diinstal:
- **Java Runtime Environment (JRE) 17** atau yang lebih baru.
- **XAMPP** (sebagai server lokal MySQL).

### 3. Setup database produksi

1. Di komputer kasir, jalankan XAMPP (Apache dan MySQL).
2. Akses `http://localhost/phpmyadmin` dan buat database `tokoberkahjaya`.
3. Impor file `tokoberkahjaya.sql`. 
4. Jika Anda benar-benar memulai dari nol untuk produksi, hapus data (*truncate*) pada tabel riwayat transaksi (`tb_penjualan` dan `tb_detail_penjualan`), namun Anda dapat membiarkan data master barang jika diperlukan.
5. Demi keamanan, atur kata sandi *root* pada MySQL, lalu pastikan Anda juga mengubah *password* pada `Koneksi.java` (sebelum di-build jadi JAR).

### 4. Buat shortcut aplikasi

Untuk memudahkan kasir membuka aplikasi:
1. Klik kanan pada file JAR, pilih **Send to > Desktop (create shortcut)**.
2. Di desktop, ganti nama *shortcut* menjadi "Toko Berkah Jaya".
3. *(Opsional)* Klik kanan *shortcut* > **Properties**, Anda dapat mengubah ikon dengan ikon khusus agar terlihat lebih profesional.
4. Anda dapat mengatur targetnya menjadi `javaw -jar "D:\AplikasiToko\TokoBerkahJaya-1.0-SNAPSHOT-jar-with-dependencies.jar"` agar jendela *command prompt* hitam di latar belakang tidak ikut muncul.

### 5. Pengamanan akun

Saat pertama kali masuk ke aplikasi kasir, gunakan akun Administrator awal (yang ada di *database dump*). Segera buat akun baru untuk karyawan/kasir, lalu ubah atau hapus akun *default* demi menghindari penyalahgunaan wewenang.

## Arsitektur

Aplikasi mengikuti pola standar Model-View-Service.

### Struktur direktori

```
├── database/            # Dump SQL untuk pengaturan database
├── invoices/            # Setruk PDF yang dihasilkan disimpan di sini
├── src/main/java/
│   ├── database/        # Konfigurasi koneksi database
│   ├── model/           # Objek transfer data (User, Barang, Penjualan)
│   ├── service/         # Logika bisnis dan query database
│   ├── ui/              # Form dan panel Swing
│   │   └── components/  # Elemen UI kustom yang dapat digunakan kembali
│   └── util/            # Pembantu pemformatan dan pembuatan PDF
└── pom.xml              # Dependensi Maven dan konfigurasi build
```

### Alur data

1. Pengguna berinteraksi dengan form Swing di paket `ui`.
2. Form mengumpulkan data dan meneruskannya ke kelas yang sesuai di paket `service`.
3. Kelas service meminta koneksi dari `database.Koneksi`.
4. Kelas service mengeksekusi query SQL terhadap database MySQL.
5. Data kembali sebagai objek `ResultSet`, dipetakan ke kelas `model`, dan kembali ke UI untuk ditampilkan secara visual.

## Tangkapan layar

| Layar login | Dasbor utama |
|:---:|:---:|
| ![Login](https://i.postimg.cc/0y88wgrV/image.png) | ![Beranda](https://i.postimg.cc/1zXsvpGd/image.png) |

| Manajemen kategori | Manajemen barang |
|:---:|:---:|
| ![Kategori](https://i.postimg.cc/gjFpcQfZ/image.png) | ![Barang](https://i.postimg.cc/PJWjCKj0/image.png) |

| Manajemen pelanggan | Point of sale (Kasir) |
|:---:|:---:|
| ![Customer](https://i.postimg.cc/bwNhvQFK/image.png) | ![Penjualan](https://i.postimg.cc/1z81GySH/image.png) |

| Log transaksi | Manajemen pengguna |
|:---:|:---:|
| ![Log](https://i.postimg.cc/VvBxf696/image.png) | ![User](https://i.postimg.cc/TY0zBKYD/image.png) |

| Profil saya | |
|:---:|:---:|
| ![Profil](https://i.postimg.cc/P5qgHdP9/image.png) | |

## Kredit

Aplikasi ini dikembangkan oleh:
- **Revaldi Winata**
- Program Studi Teknik Informatika, Fakultas Ilmu Komputer, Universitas Pamulang.
