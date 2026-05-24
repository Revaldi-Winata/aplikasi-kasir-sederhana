/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;

/**
 *
 * @author Revaldi
 */
public class Penjualan {

    private int idJual;
    private Date tglTransaksi;
    private String idCustomer;
    private String idBarang;
    private int jumlahBeli;
    private double totalBayar;
    private int idUser;

    // Display-only fields (not persisted, used for JTable display)
    private String namaCustomer;
    private String namaBarang;

    public Penjualan() {
    }

    public Penjualan(int idJual, Date tglTransaksi, String idCustomer, String idBarang, int jumlahBeli, double totalBayar, int idUser) {
        this.idJual = idJual;
        this.tglTransaksi = tglTransaksi;
        this.idCustomer = idCustomer;
        this.idBarang = idBarang;
        this.jumlahBeli = jumlahBeli;
        this.totalBayar = totalBayar;
        this.idUser = idUser;
    }

    public int getIdJual() {
        return idJual;
    }

    public void setIdJual(int idJual) {
        this.idJual = idJual;
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

    public String getIdBarang() {
        return idBarang;
    }

    public void setIdBarang(String idBarang) {
        this.idBarang = idBarang;
    }

    public int getJumlahBeli() {
        return jumlahBeli;
    }

    public void setJumlahBeli(int jumlahBeli) {
        this.jumlahBeli = jumlahBeli;
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

    public String getNamaCustomer() {
        return namaCustomer;
    }

    public void setNamaCustomer(String namaCustomer) {
        this.namaCustomer = namaCustomer;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    @Override
    public String toString() {
        return "Transaksi #" + idJual;
    }
}
