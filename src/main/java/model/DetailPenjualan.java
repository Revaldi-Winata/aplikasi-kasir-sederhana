package model;

/**
 * Model untuk detail item dalam satu transaksi penjualan.
 * Setiap record merepresentasikan 1 jenis barang yang dibeli.
 *
 * @author Revaldi
 */
public class DetailPenjualan {

    private int idDetail;
    private int idJual;
    private String idBarang;
    private double hargaSatuan;
    private int jumlahBeli;
    private double subtotal;

    // Display-only field (not persisted, used for JTable display)
    private String namaBarang;

    public DetailPenjualan() {
    }

    public DetailPenjualan(int idDetail, int idJual, String idBarang, double hargaSatuan, int jumlahBeli, double subtotal) {
        this.idDetail = idDetail;
        this.idJual = idJual;
        this.idBarang = idBarang;
        this.hargaSatuan = hargaSatuan;
        this.jumlahBeli = jumlahBeli;
        this.subtotal = subtotal;
    }

    public int getIdDetail() {
        return idDetail;
    }

    public void setIdDetail(int idDetail) {
        this.idDetail = idDetail;
    }

    public int getIdJual() {
        return idJual;
    }

    public void setIdJual(int idJual) {
        this.idJual = idJual;
    }

    public String getIdBarang() {
        return idBarang;
    }

    public void setIdBarang(String idBarang) {
        this.idBarang = idBarang;
    }

    public double getHargaSatuan() {
        return hargaSatuan;
    }

    public void setHargaSatuan(double hargaSatuan) {
        this.hargaSatuan = hargaSatuan;
    }

    public int getJumlahBeli() {
        return jumlahBeli;
    }

    public void setJumlahBeli(int jumlahBeli) {
        this.jumlahBeli = jumlahBeli;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    @Override
    public String toString() {
        return namaBarang != null ? namaBarang : ("Barang#" + idBarang);
    }
}
