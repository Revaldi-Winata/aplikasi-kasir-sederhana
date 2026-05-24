<div align="center">
  <h1>🛒 Toko Berkah Jaya - Point of Sales (POS)</h1>
  <p>Aplikasi Desktop Kasir & Manajemen Inventaris Modern berbasis Java Swing</p>
  
  [![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.java.com/)
  [![MySQL](https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
  [![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)](https://maven.apache.org/)
</div>

<br>

## 📖 Tentang Proyek

**Toko Berkah Jaya** adalah sistem *Point of Sales* (POS) dan manajemen inventaris yang dikembangkan sebagai proyek untuk mata kuliah **Pemrograman II**. Aplikasi ini dirancang untuk mempermudah operasional toko, mulai dari pendataan barang, kategori, hingga manajemen pelanggan dan karyawan (kasir). 

Aplikasi ini mengedepankan **User Experience (UX)** dengan antarmuka yang bersih (*clean*), rapi (*pixel-perfect alignment*), dan interaktif.

## ✨ Fitur Utama

- 🔐 **Sistem Multi-Role Otentikasi**:
  - **Admin**: Akses penuh ke seluruh fitur (Manajemen Kategori, Barang, Customer, User).
  - **Kasir**: Akses terbatas pada operasional kasir, dashboard, dan pengaturan profil mandiri.
- 📦 **Manajemen Inventaris**: CRUD (Create, Read, Update, Delete) data Barang beserta Kategori, Harga, Satuan, dan Stok.
- 👥 **Manajemen Relasi (CRM)**: Pendataan Customer/Pelanggan secara terstruktur.
- ⚙️ **Pengaturan Karyawan**: Manajemen User (Admin/Kasir) untuk mengontrol siapa saja yang bisa masuk ke dalam sistem.
- 👤 **Profil Saya**: Fitur bagi pengguna yang sedang login untuk memperbarui data diri secara mandiri tanpa harus ke Admin.
- 🎨 **Modern & Consistent UI**: Desain form dengan rata kiri (*left-aligned*) yang presisi di semua halaman, warna *soft*, dan state tombol yang dinamis (mengunci tombol simpan/ubah secara otomatis untuk mencegah error).
- ⚡ **Smart ID Generation**: ID data di-generate otomatis dari *auto-increment* database dan ditampilkan langsung ke pengguna secara *real-time* saat pengisian form.

## 🛠️ Teknologi yang Digunakan

- **Bahasa Pemrograman**: Java (JDK 17 atau lebih baru disarankan)
- **GUI Framework**: Java Swing & AWT
- **Database**: MySQL
- **Build Tool / Dependency**: Apache Maven
- **Driver Database**: MySQL Connector/J

---

## 📸 Pratinjau Aplikasi (Screenshots)

| Halaman Login | Halaman Utama (Beranda) |
|:---:|:---:|
| ![Login](https://i.postimg.cc/PfQWmXSn/image.png) | ![Beranda](https://i.postimg.cc/3rty8dLG/image.png) |

| Manajemen Kategori | Manajemen Barang |
|:---:|:---:|
| ![Kategori](https://i.postimg.cc/Hsfkn5bQ/image.png) | ![Barang](https://i.postimg.cc/WzcPjmB2/image.png) |

| Manajemen Customer | Kasir (Transaksi Penjualan) |
|:---:|:---:|
| ![Customer](https://i.postimg.cc/RZDy4Qf9/image.png) | ![Penjualan](https://i.postimg.cc/HsWhMP4N/image.png) |

| Manajemen User | Profil Saya |
|:---:|:---:|
| ![User](https://i.postimg.cc/0NSWxgPn/image.png) | ![Profil](https://i.postimg.cc/rsfggMCb/image.png) |

---

## 🚀 Panduan Instalasi & Setup

Berikut adalah panduan lengkap untuk menginstal dan menjalankan aplikasi ini, bahkan di komputer (device) yang baru pertama kali digunakan.

### 1. Persiapan Kebutuhan Sistem (Prerequisites)
Pastikan komputer Anda sudah terinstal perangkat lunak berikut:
1. **Java Development Kit (JDK)**: Minimal versi 8, disarankan versi 17+. ([Download JDK](https://adoptium.net/))
2. **XAMPP** (atau aplikasi sejenis seperti WAMP/Laragon) untuk menjalankan server database MySQL lokal. ([Download XAMPP](https://www.apachefriends.org/download.html))
3. **IDE Java** (Pilihan): NetBeans (Disarankan), IntelliJ IDEA, atau Eclipse.

### 2. Setup Database
Agar aplikasi dapat menyimpan data, Anda harus menyiapkan database MySQL:
1. Buka aplikasi **XAMPP Control Panel** dan klik tombol **Start** pada modul **Apache** dan **MySQL**.
2. Buka browser dan akses [http://localhost/phpmyadmin](http://localhost/phpmyadmin).
3. Buat database baru:
   - Klik **Baru (New)** di panel sebelah kiri.
   - Masukkan nama basis data: `tokoberkahjaya`.
   - Klik **Buat (Create)**.
4. Temukan file `tokoberkahjaya.sql` yang terletak di dalam folder `database/` di dalam *repository* ini.
5. Di phpMyAdmin, klik tab **Import**, pilih file tersebut, lalu klik **Go** untuk meng-import struktur tabel (dan data awal) secara otomatis.

### 3. Clone Repository
Buka Terminal / Command Prompt dan jalankan perintah berikut untuk mengunduh kode (atau klik tombol **Download ZIP** di GitHub):
```bash
git clone https://github.com/username-anda/TokoBerkahJaya.git
```

### 4. Konfigurasi Koneksi Database di Kode
Jika Anda menggunakan *password* pada root MySQL lokal Anda, Anda harus mengubahnya di dalam kode:
1. Buka folder *project* di IDE Anda (misal: NetBeans).
2. Cari file `Koneksi.java` di dalam *package* `database` (`src/main/java/database/Koneksi.java`).
3. Sesuaikan dengan kredensial MySQL Anda:
   ```java
   private static final String URL = "jdbc:mysql://localhost:3306/tokoberkahjaya";
   private static final String USER = "root";       // Username MySQL (Default XAMPP: root)
   private static final String PASSWORD = "";       // Password MySQL (Default XAMPP: kosong)
   ```

### 5. Compile & Run (Menjalankan Aplikasi)
- **Menggunakan NetBeans**:
  - Klik kanan pada *project* **TokoBerkahJaya** di tab *Projects*.
  - Pilih **Clean and Build** untuk mengunduh *dependencies* Maven dan meng-compile kode.
  - Cari file kelas yang memiliki method `main` (misalnya `LoginForm.java` atau `Main.java` di package UI), klik kanan lalu pilih **Run File** (atau `Shift+F6`).

---

## 📦 Panduan Distribusi (Membuat file .JAR untuk User)

Jika Anda ingin memberikan aplikasi ini ke komputer kasir atau pengguna lain **tanpa perlu menginstal NetBeans**, Anda dapat membungkusnya menjadi file `.jar`.

1. Di NetBeans, pastikan Anda sudah melakukan **Clean and Build**.
2. Buka folder project Anda melalui File Explorer (`TokoBerkahJaya\target`).
3. Anda akan menemukan file berekstensi `.jar` (misalnya `TokoBerkahJaya-1.0-SNAPSHOT.jar` atau file berakhiran `-jar-with-dependencies.jar`).
4. **Cara Penggunaan di Komputer Tujuan**:
   - Pindahkan file `.jar` tersebut ke komputer tujuan.
   - **SYARAT**: Komputer tujuan **WAJIB** terinstal Java (JRE) dan XAMPP (dengan database `tokoberkahjaya` yang sudah ter-import dan menyala).
   - Klik ganda (double-click) pada file `.jar` tersebut, atau jalankan via CMD:
     ```bash
     java -jar NamaFileAplikasi.jar
     ```

---



<div align="center">
  <p>Dibuat dengan ❤️ untuk proyek Pemrograman II</p>
</div>
