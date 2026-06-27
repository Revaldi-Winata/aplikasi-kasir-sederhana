import database.Koneksi;
import java.sql.Connection;
import java.sql.Statement;

public class RunAlter {
    public static void main(String[] args) {
        try (Connection conn = Koneksi.getKoneksi();
             Statement stmt = conn.createStatement()) {
            
            System.out.println("Adding harga_beli to tb_barang...");
            try {
                stmt.executeUpdate("ALTER TABLE tb_barang ADD COLUMN harga_beli DOUBLE NOT NULL DEFAULT 0 AFTER satuan;");
                System.out.println("Success.");
            } catch (Exception e) {
                System.out.println("Failed or already exists: " + e.getMessage());
            }

            System.out.println("Adding harga_beli to tb_detail_penjualan...");
            try {
                stmt.executeUpdate("ALTER TABLE tb_detail_penjualan ADD COLUMN harga_beli DOUBLE NOT NULL DEFAULT 0 AFTER id_barang;");
                System.out.println("Success.");
            } catch (Exception e) {
                System.out.println("Failed or already exists: " + e.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
