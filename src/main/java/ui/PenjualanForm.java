package ui;

import model.Barang;
import model.Customer;
import model.Penjualan;
import model.DetailPenjualan;
import model.User;
import service.BarangService;
import service.CustomerService;
import service.PenjualanService;
import ui.components.Toast;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import util.ThemeUtil;
import ui.components.RoundedPanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PenjualanForm extends JPanel {

    private JTextField txtIdTrans, txtTanggal, txtQty, txtTotal, txtBayar, txtKembali;
    private JComboBox<String> cbCustomer, cbBarang;
    private JButton btnTambah, btnSimpan, btnClear;
    private JTable tableKeranjang;
    private DefaultTableModel modelKeranjang;

    private BarangService barangService;
    private CustomerService customerService;
    private PenjualanService penjualanService;

    private List<Customer> listCustomer;
    private List<Barang> listBarang;
    private List<DetailPenjualan> keranjang;
    private User currentUser;

    private double grandTotal = 0;

    public PenjualanForm(User user) {
        this.currentUser = user;
        barangService = new BarangService();
        customerService = new CustomerService();
        penjualanService = new PenjualanService();
        keranjang = new ArrayList<>();

        setLayout(new BorderLayout(20, 20));
        setBackground(ThemeUtil.BG_SOFT);
        setBorder(new EmptyBorder(20, 30, 20, 30));

        initComponents();
        loadCombo();
        clearForm();
    }

    private void initComponents() {


        // --- Panel Kiri: Input Keranjang ---
        RoundedPanel panelInput = ThemeUtil.createCardPanel();
        panelInput.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        addLabel(panelInput, "No Faktur:", gbc);
        txtIdTrans = new JTextField(15);
        ThemeUtil.styleTextField(txtIdTrans);
        txtIdTrans.setEditable(false);
        gbc.gridx = 1; panelInput.add(txtIdTrans, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        addLabel(panelInput, "Tanggal:", gbc);
        txtTanggal = new JTextField(15);
        ThemeUtil.styleTextField(txtTanggal);
        txtTanggal.setEditable(false);
        txtTanggal.setText(new java.text.SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        gbc.gridx = 1; panelInput.add(txtTanggal, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        addLabel(panelInput, "Customer:", gbc);
        cbCustomer = new JComboBox<>();
        ThemeUtil.styleComboBox(cbCustomer);
        gbc.gridx = 1; panelInput.add(cbCustomer, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        addLabel(panelInput, "Barang:", gbc);
        cbBarang = new JComboBox<>();
        ThemeUtil.styleComboBox(cbBarang);
        gbc.gridx = 1; panelInput.add(cbBarang, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        addLabel(panelInput, "Qty:", gbc);
        txtQty = new JTextField(15);
        ThemeUtil.styleTextField(txtQty);
        gbc.gridx = 1; panelInput.add(txtQty, gbc);

        btnTambah = new JButton("Tambah ke Keranjang");
        ThemeUtil.styleButton(btnTambah, ThemeUtil.OCEAN_BLUE);
        gbc.gridx = 1; gbc.gridy = 5;
        gbc.insets = new Insets(15, 5, 5, 15);
        panelInput.add(btnTambah, gbc);

        // --- Panel Tengah: Tabel Keranjang ---
        RoundedPanel panelTabel = ThemeUtil.createCardPanel();
        panelTabel.setLayout(new BorderLayout());
        modelKeranjang = new DefaultTableModel(new String[]{"ID Barang", "Nama Barang", "Harga", "Qty", "Subtotal"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableKeranjang = new JTable(modelKeranjang);
        JScrollPane scrollTabel = new JScrollPane(tableKeranjang);
        ThemeUtil.styleTable(tableKeranjang, scrollTabel);
        panelTabel.add(scrollTabel, BorderLayout.CENTER);

        // --- Panel Kanan: Pembayaran ---
        RoundedPanel panelBayar = ThemeUtil.createCardPanel();
        panelBayar.setLayout(new GridBagLayout());
        GridBagConstraints gbcB = new GridBagConstraints();
        gbcB.insets = new Insets(5, 5, 5, 15);
        gbcB.fill = GridBagConstraints.HORIZONTAL;

        gbcB.gridx = 0; gbcB.gridy = 0;
        addLabel(panelBayar, "Total:", gbcB);
        txtTotal = new JTextField(15);
        ThemeUtil.styleTextField(txtTotal);
        txtTotal.setEditable(false);
        txtTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbcB.gridx = 1; panelBayar.add(txtTotal, gbcB);

        gbcB.gridx = 0; gbcB.gridy = 1;
        addLabel(panelBayar, "Bayar:", gbcB);
        txtBayar = new JTextField(15);
        ThemeUtil.styleTextField(txtBayar);
        txtBayar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbcB.gridx = 1; panelBayar.add(txtBayar, gbcB);

        gbcB.gridx = 0; gbcB.gridy = 2;
        addLabel(panelBayar, "Kembali:", gbcB);
        txtKembali = new JTextField(15);
        ThemeUtil.styleTextField(txtKembali);
        txtKembali.setEditable(false);
        txtKembali.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbcB.gridx = 1; panelBayar.add(txtKembali, gbcB);

        JPanel panelBtnSave = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBtnSave.setOpaque(false);
        btnSimpan = new JButton("Simpan Transaksi");
        ThemeUtil.styleButton(btnSimpan, ThemeUtil.SUCCESS_COLOR);
        btnClear = new JButton("Clear Keranjang");
        ThemeUtil.styleButton(btnClear, ThemeUtil.ERROR_COLOR);
        panelBtnSave.add(btnClear);
        panelBtnSave.add(btnSimpan);

        gbcB.gridx = 0; gbcB.gridy = 3;
        gbcB.gridwidth = 2;
        gbcB.insets = new Insets(20, 5, 5, 15);
        panelBayar.add(panelBtnSave, gbcB);

        // Layout Kombinasi
        JPanel panelUtama = new JPanel(new BorderLayout(15, 15));
        panelUtama.setOpaque(false);
        
        JPanel panelAtas = new JPanel(new BorderLayout(15, 15));
        panelAtas.setOpaque(false);
        panelAtas.add(panelInput, BorderLayout.WEST);
        panelAtas.add(panelBayar, BorderLayout.CENTER);

        panelUtama.add(panelAtas, BorderLayout.NORTH);
        panelUtama.add(panelTabel, BorderLayout.CENTER);

        add(panelUtama, BorderLayout.CENTER);

        // Events
        btnTambah.addActionListener(e -> tambahKeKeranjang());
        btnClear.addActionListener(e -> clearForm());

        txtQty.addActionListener(e -> btnTambah.doClick());
        txtBayar.addActionListener(e -> {
            hitungKembali();
            btnSimpan.doClick();
        });
        btnSimpan.addActionListener(e -> simpanTransaksi());

        txtBayar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { hitungKembali(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { hitungKembali(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { hitungKembali(); }
        });
    }

    private void addLabel(JPanel p, String text, GridBagConstraints gbc) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(ThemeUtil.FONT_REGULAR);
        lbl.setForeground(ThemeUtil.TEXT_SECONDARY);
        p.add(lbl, gbc);
    }

    public void loadCombo() {
        cbCustomer.removeAllItems();
        listCustomer = customerService.getAllCustomer();
        for (Customer c : listCustomer) {
            cbCustomer.addItem(c.getNamaCustomer() + " (" + c.getIdCustomer() + ")");
        }

        cbBarang.removeAllItems();
        listBarang = barangService.getAllBarang();
        for (Barang b : listBarang) {
            cbBarang.addItem(b.getNamaBarang() + " - " + util.Formatter.formatRupiah(b.getHargaJual()) + " (Stok: " + b.getStok() + ")");
        }
    }
    
    public void loadData() {
        refreshTabel();
    }

    private void clearForm() {
        txtIdTrans.setText(penjualanService.getPreviewNoFaktur()); 
        txtQty.setText("");
        txtTotal.setText("0");
        txtBayar.setText("");
        txtKembali.setText("0");
        keranjang.clear();
        refreshTabel();
    }

    private void refreshTabel() {
        modelKeranjang.setRowCount(0);
        grandTotal = 0;
        for (DetailPenjualan detail : keranjang) {
            modelKeranjang.addRow(new Object[]{
                    detail.getIdBarang(),
                    getNamaBarang(detail.getIdBarang()),
                    util.Formatter.formatRupiah(detail.getHargaSatuan()),
                    detail.getJumlahBeli(),
                    util.Formatter.formatRupiah(detail.getSubtotal())
            });
            grandTotal += detail.getSubtotal();
        }
        txtTotal.setText(util.Formatter.formatRupiah(grandTotal));
        txtBayar.setText("");
        txtKembali.setText("");
    }

    private String getNamaBarang(String id) {
        for (Barang b : listBarang) {
            if (b.getIdBarang().equals(id)) return b.getNamaBarang();
        }
        return "-";
    }

    private void tambahKeKeranjang() {
        if (cbBarang.getSelectedIndex() < 0) return;
        try {
            int qty = Integer.parseInt(txtQty.getText().trim());
            if (qty <= 0) {
                Toast.showError((JFrame) SwingUtilities.getWindowAncestor(this), "Qty harus lebih besar dari 0");
                return;
            }

            Barang b = listBarang.get(cbBarang.getSelectedIndex());
            if (b.getStok() < qty) {
                Toast.showError((JFrame) SwingUtilities.getWindowAncestor(this), "Stok tidak mencukupi!");
                return;
            }

            // Cek jika sudah ada di keranjang
            boolean found = false;
            for (DetailPenjualan pd : keranjang) {
                if (pd.getIdBarang().equals(b.getIdBarang())) {
                    pd.setJumlahBeli(pd.getJumlahBeli() + qty);
                    pd.setSubtotal(pd.getJumlahBeli() * pd.getHargaSatuan());
                    found = true;
                    break;
                }
            }

            if (!found) {
                DetailPenjualan pd = new DetailPenjualan();
                // pd.setIdJual(...) will be set by the service
                pd.setIdBarang(b.getIdBarang());
                pd.setHargaSatuan(b.getHargaJual());
                pd.setJumlahBeli(qty);
                pd.setSubtotal(b.getHargaJual() * qty);
                keranjang.add(pd);
            }

            txtQty.setText("");
            refreshTabel();

        } catch (NumberFormatException e) {
            Toast.showError((JFrame) SwingUtilities.getWindowAncestor(this), "Qty harus angka!");
        }
    }

    private void hitungKembali() {
        try {
            double bayar = Double.parseDouble(txtBayar.getText().trim());
            double kembali = bayar - grandTotal;
            txtKembali.setText(util.Formatter.formatRupiah(kembali));
        } catch (Exception e) {
            txtKembali.setText("Error");
        }
    }

    private void simpanTransaksi() {
        if (keranjang.isEmpty()) {
            Toast.showError((JFrame) SwingUtilities.getWindowAncestor(this), "Keranjang masih kosong!");
            return;
        }
        if (cbCustomer.getSelectedIndex() < 0) {
            Toast.showError((JFrame) SwingUtilities.getWindowAncestor(this), "Pilih customer!");
            return;
        }

        try {
            double bayar = Double.parseDouble(txtBayar.getText().trim());
            if (bayar < grandTotal) {
                Toast.showError((JFrame) SwingUtilities.getWindowAncestor(this), "Uang bayar kurang!");
                return;
            }

            Customer c = listCustomer.get(cbCustomer.getSelectedIndex());

            Penjualan p = new Penjualan();
            p.setNoFaktur(txtIdTrans.getText());
            p.setTglTransaksi(new java.sql.Date(new Date().getTime()));
            p.setIdCustomer(c.getIdCustomer());
            p.setIdUser(currentUser != null ? currentUser.getIdUser() : 1);
            p.setTotalBayar(grandTotal);
            p.setDetails(keranjang);

            if (penjualanService.simpanTransaksi(p)) {
                Toast.showSuccess((JFrame) SwingUtilities.getWindowAncestor(this), "Transaksi Berhasil Disimpan!");
                
                int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda ingin melihat pratinjau dan menyimpan faktur?", "Cetak Faktur", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    service.LogTransaksiService logService = new service.LogTransaksiService();
                    java.util.Map<String, Object> header = logService.getTransactionHeader(p.getNoFaktur());
                    java.util.List<java.util.Map<String, Object>> details = logService.getTransactionDetails(p.getNoFaktur());
                    if (header != null && !details.isEmpty()) {
                        try {
                            util.ReceiptPrinter printer = new util.ReceiptPrinter(header, details);
                            java.awt.image.BufferedImage img = util.PdfGenerator.createReceiptImage(printer, details.size());
                            java.io.File pdfFile = util.PdfGenerator.saveAsPdf(img, p.getNoFaktur());
                            
                            InvoicePreviewDialog dialog = new InvoicePreviewDialog((JFrame) SwingUtilities.getWindowAncestor(this), img, pdfFile, printer);
                            dialog.setVisible(true);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(this, "Gagal membuat PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            ex.printStackTrace();
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Data transaksi untuk cetak tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                
                clearForm();
                loadCombo(); // refresh stok combo
            } else {
                Toast.showError((JFrame) SwingUtilities.getWindowAncestor(this), "Gagal menyimpan transaksi!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.showError((JFrame) SwingUtilities.getWindowAncestor(this), "Error: " + e.getMessage());
        }
    }
}
