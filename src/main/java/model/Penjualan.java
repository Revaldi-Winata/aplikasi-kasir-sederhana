/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Model untuk header transaksi penjualan.
 * Satu Penjualan memiliki banyak DetailPenjualan (1-to-many).
 *
 * @author Revaldi
 */
public class Penjualan {

    private int idJual;
    private String noFaktur;
    private Date tglTransaksi;
    private String idCustomer;
    private double totalBayar;
    private int idUser;

    // Detail items for this transaction
    private List<DetailPenjualan> details = new ArrayList<>();

    // Display-only field (not persisted, used for JTable display)
    private String namaCustomer;

    public Penjualan() {
    }

    public Penjualan(int idJual, String noFaktur, Date tglTransaksi, String idCustomer, double totalBayar, int idUser) {
        this.idJual = idJual;
        this.noFaktur = noFaktur;
        this.tglTransaksi = tglTransaksi;
        this.idCustomer = idCustomer;
        this.totalBayar = totalBayar;
        this.idUser = idUser;
    }

    public int getIdJual() {
        return idJual;
    }

    public void setIdJual(int idJual) {
        this.idJual = idJual;
    }

    public String getNoFaktur() {
        return noFaktur;
    }

    public void setNoFaktur(String noFaktur) {
        this.noFaktur = noFaktur;
    }

    public Date getTglTransaksi() {
        return tglTransaksi;
    }

    public void setTglTransaksi(Date tglTransaksi) {
        this.tglTransaksi = tglTransaksi;
    }

    public String getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(String idCustomer) {
        this.idCustomer = idCustomer;
    }

    public double getTotalBayar() {
        return totalBayar;
    }

    public void setTotalBayar(double totalBayar) {
        this.totalBayar = totalBayar;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public List<DetailPenjualan> getDetails() {
        return details;
    }

    public void setDetails(List<DetailPenjualan> details) {
        this.details = details;
    }

    public String getNamaCustomer() {
        return namaCustomer;
    }

    public void setNamaCustomer(String namaCustomer) {
        this.namaCustomer = namaCustomer;
    }

    @Override
    public String toString() {
        return "Transaksi #" + idJual + " (" + noFaktur + ")";
    }
}
