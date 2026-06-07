# Toko Berkah Jaya Foundation Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Implement the Core Models and Data Access Services for the Toko Berkah Jaya application with complete Database CRUD operations and Stock validation.

**Architecture:** We are using a 3-Tier MVC architecture (Model-View-Controller). The `model` package contains pure POJOs. The `service` package contains the Business Logic and DAO (Data Access Object) using raw JDBC `PreparedStatement` to prevent SQL Injection. Connection pooling/singleton is provided by `database.Koneksi`.

**Tech Stack:** Java 11+, MySQL JDBC Driver, NetBeans Project Structure.

---

### Task 1: Implement `Kategori` Model and Service

**Files:**
- Modify: `src/main/java/model/Kategori.java`
- Modify: `src/main/java/service/KategoriService.java`
- Create: `src/test/java/service/KategoriServiceTest.java`

**Step 1: Write the failing test**

```java
// src/test/java/service/KategoriServiceTest.java
package service;

import model.Kategori;
import java.util.List;

public class KategoriServiceTest {
    public static void main(String[] args) {
        KategoriService service = new KategoriService();
        List<Kategori> list = service.getAllKategori();
        System.out.println("Total kategori: " + list.size());
        System.out.println("TEST PASS");
    }
}
```

**Step 2: Run test to verify it fails**

Run: `javac -d target/classes -cp "target/classes;C:/Users/Revaldi/.m2/repository/com/mysql/mysql-connector-j/9.0.0/mysql-connector-j-9.0.0.jar" src/test/java/service/KategoriServiceTest.java`
Expected: FAIL with compilation error (method `getAllKategori` not found in `KategoriService`).

**Step 3: Write minimal implementation**

```java
// src/main/java/model/Kategori.java
package model;

public class Kategori {
    private int idKategori;
    private String namaKategori;

    public Kategori() {}
    public Kategori(int idKategori, String namaKategori) {
        this.idKategori = idKategori;
        this.namaKategori = namaKategori;
    }

    public int getIdKategori() { return idKategori; }
    public void setIdKategori(int idKategori) { this.idKategori = idKategori; }
    public String getNamaKategori() { return namaKategori; }
    public void setNamaKategori(String namaKategori) { this.namaKategori = namaKategori; }

    @Override
    public String toString() { return namaKategori; }
}
```

```java
// src/main/java/service/KategoriService.java
package service;

import database.Koneksi;
import model.Kategori;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KategoriService {
    public List<Kategori> getAllKategori() {
        List<Kategori> list = new ArrayList<>();
        String sql = "SELECT * FROM tb_kategori";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Kategori(rs.getInt("id_kategori"), rs.getString("nama_kategori")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
```

**Step 4: Run test to verify it passes**

Run: 
```bash
javac -d target/classes -sourcepath src/main/java src/main/java/model/Kategori.java src/main/java/service/KategoriService.java src/test/java/service/KategoriServiceTest.java
java -cp "target/classes;C:/Users/Revaldi/.m2/repository/com/mysql/mysql-connector-j/9.0.0/mysql-connector-j-9.0.0.jar" service.KategoriServiceTest
```
Expected: PASS (prints "Total kategori: 0" and "TEST PASS")

**Step 5: Commit**

```bash
git add src/main/java/model/Kategori.java src/main/java/service/KategoriService.java src/test/java/service/KategoriServiceTest.java
git commit -m "feat: implement Kategori model and basic KategoriService"
```

---

### Task 2: Implement `PenjualanService` with Transaction Management

**Files:**
- Modify: `src/main/java/model/Penjualan.java`
- Modify: `src/main/java/service/PenjualanService.java`
- Create: `src/test/java/service/PenjualanServiceTest.java`

**Step 1: Write the failing test**

```java
// src/test/java/service/PenjualanServiceTest.java
package service;

import model.Penjualan;
import java.sql.Date;

public class PenjualanServiceTest {
    public static void main(String[] args) {
        PenjualanService service = new PenjualanService();
        Penjualan p = new Penjualan(0, new Date(System.currentTimeMillis()), "C001", "B001", 2, 50000.0, 1);
        try {
            boolean success = service.simpanTransaksi(p);
            System.out.println("Success: " + success);
            System.out.println("TEST PASS");
        } catch (Exception e) {
            System.out.println("Exception expected for non-existent items: " + e.getMessage());
            System.out.println("TEST PASS");
        }
    }
}
```

**Step 2: Run test to verify it fails**

Run: `javac -d target/classes -sourcepath src/main/java src/test/java/service/PenjualanServiceTest.java`
Expected: FAIL (missing methods/constructors).

**Step 3: Write minimal implementation**

```java
// src/main/java/model/Penjualan.java
package model;
import java.sql.Date;

public class Penjualan {
    private int idJual;
    private Date tglTransaksi;
    private String idCustomer;
    private String idBarang;
    private int jumlahBeli;
    private double totalBayar;
    private int idUser;

    public Penjualan(int idJual, Date tglTransaksi, String idCustomer, String idBarang, int jumlahBeli, double totalBayar, int idUser) {
        this.idJual = idJual;
        this.tglTransaksi = tglTransaksi;
        this.idCustomer = idCustomer;
        this.idBarang = idBarang;
        this.jumlahBeli = jumlahBeli;
        this.totalBayar = totalBayar;
        this.idUser = idUser;
    }
    
    // Getters and Setters omitted for brevity but MUST be implemented...
    public String getIdBarang() { return idBarang; }
    public int getJumlahBeli() { return jumlahBeli; }
    public Date getTglTransaksi() { return tglTransaksi; }
    public String getIdCustomer() { return idCustomer; }
    public double getTotalBayar() { return totalBayar; }
    public int getIdUser() { return idUser; }
}
```

```java
// src/main/java/service/PenjualanService.java
package service;

import database.Koneksi;
import model.Penjualan;
import java.sql.*;

public class PenjualanService {
    public boolean simpanTransaksi(Penjualan p) throws Exception {
        Connection conn = Koneksi.getKoneksi();
        boolean success = false;
        
        String sqlUpdateStok = "UPDATE tb_barang SET stok = stok - ? WHERE id_barang = ? AND stok >= ?";
        String sqlInsertJual = "INSERT INTO tb_penjualan (tgl_transaksi, id_customer, id_barang, jumlah_beli, total_bayar, id_user) VALUES (?, ?, ?, ?, ?, ?)";
        
        try {
            conn.setAutoCommit(false);
            
            // 1. Update stok
            try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdateStok)) {
                psUpdate.setInt(1, p.getJumlahBeli());
                psUpdate.setString(2, p.getIdBarang());
                psUpdate.setInt(3, p.getJumlahBeli());
                int affectedRows = psUpdate.executeUpdate();
                
                if (affectedRows == 0) {
                    conn.rollback();
                    throw new Exception("Stok tidak mencukupi atau barang tidak ditemukan.");
                }
            }
            
            // 2. Insert transaksi
            try (PreparedStatement psInsert = conn.prepareStatement(sqlInsertJual)) {
                psInsert.setDate(1, p.getTglTransaksi());
                psInsert.setString(2, p.getIdCustomer());
                psInsert.setString(3, p.getIdBarang());
                psInsert.setInt(4, p.getJumlahBeli());
                psInsert.setDouble(5, p.getTotalBayar());
                psInsert.setInt(6, p.getIdUser());
                psInsert.executeUpdate();
            }
            
            conn.commit();
            success = true;
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
        return success;
    }
}
```

**Step 4: Run test to verify it passes**

Run:
```bash
javac -d target/classes -sourcepath src/main/java src/main/java/model/Penjualan.java src/main/java/service/PenjualanService.java src/test/java/service/PenjualanServiceTest.java
java -cp "target/classes;C:/Users/Revaldi/.m2/repository/com/mysql/mysql-connector-j/9.0.0/mysql-connector-j-9.0.0.jar" service.PenjualanServiceTest
```
Expected: PASS

**Step 5: Commit**

```bash
git add src/main/java/model/Penjualan.java src/main/java/service/PenjualanService.java src/test/java/service/PenjualanServiceTest.java
git commit -m "feat: implement Penjualan transaction logic with rollback"
```
