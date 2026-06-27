<div align="center">
  <img src="https://i.postimg.cc/L8ZVLW7C/logo.png" alt="Logo Toko Berkah Jaya" width="150"/>
  <p><i>*Note: The logo of this application was created using ChatGPT.</i></p>
  <h1>Simple Cashier Application (Toko Berkah Jaya POS)</h1>
  <p>A desktop point-of-sale and inventory management application built with Java Swing.</p>
  
  [![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.java.com/)
  [![MySQL](https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
  [![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)](https://maven.apache.org/)

  <br>
  <p><b><a href="README.md">Baca dalam Bahasa Indonesia</a></b></p>
</div>

<br>

Toko Berkah Jaya handles daily store operations. The system tracks inventory, categorizes products, registers customers, and processes sales. It also includes user management with role-based access control for administrators and cashiers.

## Key features

- **Role-based access**: Administrators have full system access. Cashiers can only access the point of sale, dashboard, and their own profile.
- **Inventory tracking**: Add, update, and remove products. The system tracks stock levels and product categories.
- **Customer database**: Keep records of loyal customers.
- **Point of sale**: Process transactions, calculate totals with automatic invoice number generation, print physical receipts or PDFs, and display dynamic invoice previews.
- **Sales Reports & Excel Export**: Interactive sales reporting module supporting filters by date range, product categories, and customer names. Equipped with manual row selection and instant data export to Microsoft Excel (`.xlsx`) via Apache POI.
- **Transaction logs**: Track stock levels and transaction activities in real-time.
- **User profiles**: Staff can update their own account details (username, password, name) independently.

## Tech stack

- **Language**: Java (JDK 11+ / compatible with JDK 17)
- **GUI framework**: Java Swing (FlatLaf Look and Feel)
- **Database**: MySQL (RDBMS)
- **Build tool**: Apache Maven
- **External Libraries**:
  - Apache POI (Excel report export `.xlsx`)
  - OpenPDF / PDFBox (PDF invoice generation)
  - MySQL Connector/J (MySQL JDBC driver)

## Prerequisites

- Java Development Kit (JDK) 17 or higher
- MySQL server (via XAMPP, WAMP, or standalone)
- An IDE like NetBeans or IntelliJ IDEA (for development mode)

## Development Setup

Follow these steps if you want to inspect the source code, modify features, or run the application from an IDE.

### 1. Clone the repository

```bash
git clone https://github.com/username/TokoBerkahJaya.git
cd TokoBerkahJaya
```

### 2. Set up the database

1. Start your local MySQL server.
2. Create a new database named `tokoberkahjaya`.
3. Import the initial schema and data. You can do this via phpMyAdmin or the command line:

```bash
mysql -u root -p tokoberkahjaya < database/tokoberkahjaya.sql
```

### 3. Configure the connection

The application connects to MySQL using the credentials defined in `src/main/java/database/Koneksi.java`. If your local MySQL setup requires a password, update these lines:

```java
private static final String URL = "jdbc:mysql://localhost:3306/tokoberkahjaya";
private static final String USER = "root";
private static final String PASSWORD = ""; // Add your password here
```

### 4. Build and run

If you use NetBeans, open the project, right-click it, and select **Clean and Build**. Then run the project.

If you use the command line with Maven:

```bash
mvn clean package
java -cp target/classes;target/dependency/* ui.LoginForm
```

## Production Deployment

Follow these steps if you are preparing to distribute the application and install it on a store's cashier computer. The target computer does not need the source code or an IDE.

### 1. Build the executable JAR

On your development machine, run the following Maven command to bundle the application and all its dependencies into a single portable JAR file:

```bash
mvn clean package
```

This generates `TokoBerkahJaya-1.0-SNAPSHOT-jar-with-dependencies.jar` in the `target/` directory.

### 2. Prepare the cashier computer

Move the JAR file to the target computer using a flash drive (e.g., place it in `D:\StoreApp\`). Make sure the cashier computer has:
- **Java Runtime Environment (JRE) 17** or newer.
- **XAMPP** (as the local MySQL server).

### 3. Setup the production database

1. On the cashier computer, start XAMPP (Apache and MySQL).
2. Access `http://localhost/phpmyadmin` and create the `tokoberkahjaya` database.
3. Import the `tokoberkahjaya.sql` file.
4. If you are starting completely fresh for production, you may want to truncate the transaction history tables (`tb_penjualan` and `tb_detail_penjualan`), but keep the master product data if needed.
5. For security, set a root password on MySQL and ensure you update the password in `Koneksi.java` (before building the JAR).

### 4. Create an application shortcut

To make it easy for the cashier to open the app:
1. Right-click the JAR file, select **Send to > Desktop (create shortcut)**.
2. On the desktop, rename the shortcut to "Toko Berkah Jaya".
3. *(Optional)* Right-click the shortcut > **Properties**, and change the icon to a custom store logo for a professional look.
4. You can set the target to `javaw -jar "D:\StoreApp\TokoBerkahJaya-1.0-SNAPSHOT-jar-with-dependencies.jar"` so the background command prompt window does not appear.

### 5. Secure your accounts

When logging into the cashier application for the first time, use the initial Administrator account (from the database dump). Immediately create a new account for the employees/cashiers, then change or delete the default admin account to prevent unauthorized access.

## Architecture

The application follows a standard Model-View-Service pattern.

### Directory structure

```
├── database/            # SQL dumps for database setup
├── invoices/            # Generated PDF receipts are saved here
├── src/main/java/
│   ├── database/        # Database connection configuration
│   ├── model/           # Data transfer objects (User, Barang, Penjualan)
│   ├── service/         # Business logic and database queries
│   ├── ui/              # Swing forms and panels
│   │   └── components/  # Reusable custom UI elements
│   └── util/            # Formatting helpers and PDF generator
└── pom.xml              # Maven dependencies and build configuration
```

### Data flow

1. The user interacts with a Swing form in the `ui` package.
2. The form collects data and passes it to the corresponding class in the `service` package.
3. The service class requests a connection from `database.Koneksi`.
4. The service class executes SQL queries against the MySQL database.
5. Data returns as `ResultSet` objects, gets mapped to `model` classes, and goes back to the UI for visual display.

## Screenshots

| Login screen | Main dashboard |
|:---:|:---:|
| ![Login](https://i.postimg.cc/0y88wgrV/image.png) | ![Beranda](https://i.postimg.cc/1zXsvpGd/image.png) |

| Category management | Product management |
|:---:|:---:|
| ![Kategori](https://i.postimg.cc/gjFpcQfZ/image.png) | ![Barang](https://i.postimg.cc/PJWjCKj0/image.png) |

| Customer management | Point of sale |
|:---:|:---:|
| ![Customer](https://i.postimg.cc/bwNhvQFK/image.png) | ![Penjualan](https://i.postimg.cc/1z81GySH/image.png) |

| Transaction logs | User management |
|:---:|:---:|
| ![Log](https://i.postimg.cc/VvBxf696/image.png) | ![User](https://i.postimg.cc/TY0zBKYD/image.png) |

| My profile | |
|:---:|:---:|
| ![Profil](https://i.postimg.cc/P5qgHdP9/image.png) | |

## Credits

This application was developed by:
- **Revaldi Winata**
- Informatics Engineering Study Program, Faculty of Computer Science, Pamulang University.
