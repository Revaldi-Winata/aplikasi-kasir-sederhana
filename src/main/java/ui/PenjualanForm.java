package ui;

import model.Barang;
import model.Customer;
import model.DetailPenjualan;
import model.Penjualan;
import model.User;
import service.BarangService;
import service.CustomerService;
import service.PenjualanService;
import util.Formatter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import util.ThemeUtil;
import java.awt.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class PenjualanForm extends JPanel {

    private JComboBox<Customer> cbCustomer;
    private JComboBox<Barang> cbBarang;
    private JTextField txtHarga, txtJumlah, txtSubtotal;
    private JLabel lblGrandTotal;
    private JButton btnTambahKeranjang, btnHapusItem, btnSimpan, btnRefresh;
    private JTable tableKeranjang, tableRiwayat;
    private DefaultTableModel modelKeranjang, modelRiwayat;

    private PenjualanService penjualanService;
    private BarangService barangService;
    private CustomerService customerService;
    private User loggedInUser;

    // In-memory cart
    private List<DetailPenjualan> keranjang = new ArrayList<>();

    public PenjualanForm(User user) {
        this.loggedInUser = user;
        penjualanService = new PenjualanService();
        barangService = new BarangService();
        customerService = new CustomerService();

        setLayout(new BorderLayout(10, 10));
        setBackground(ThemeUtil.BG_SOFT);
        setBorder(new EmptyBorder(30, 30, 30, 30));
        initComponents();
        loadCombo();
        loadRiwayat();
    }

    private void initComponents() {
        // Title
        JLabel lblTitle = new JLabel("Transaksi Kasir / Penjualan");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(ThemeUtil.TEXT_PRIMARY);
        lblTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        add(lblTitle, BorderLayout.NORTH);

        // ========== TOP SECTION: Input + Keranjang ==========
        JPanel panelTop = new JPanel(new BorderLayout(15, 10));
        panelTop.setOpaque(false);

        // --- Left: Input Form ---
        JPanel panelInput = new JPanel(new GridBagLayout());
        panelInput.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Customer
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblCustomer = new JLabel("Customer:");
        lblCustomer.setFont(ThemeUtil.FONT_REGULAR);
        lblCustomer.setForeground(ThemeUtil.TEXT_SECONDARY);
        panelInput.add(lblCustomer, gbc);
        cbCustomer = new JComboBox<>();
        ThemeUtil.styleComboBox(cbCustomer);
        gbc.gridx = 1;
        panelInput.add(cbCustomer, gbc);

        // Barang
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblBarang = new JLabel("Barang:");
        lblBarang.setFont(ThemeUtil.FONT_REGULAR);
        lblBarang.setForeground(ThemeUtil.TEXT_SECONDARY);
        panelInput.add(lblBarang, gbc);
        cbBarang = new JComboBox<>();
        ThemeUtil.styleComboBox(cbBarang);
        gbc.gridx = 1;
        panelInput.add(cbBarang, gbc);

        // Harga Satuan
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblHarga = new JLabel("Harga Satuan:");
        lblHarga.setFont(ThemeUtil.FONT_REGULAR);
        lblHarga.setForeground(ThemeUtil.TEXT_SECONDARY);
        panelInput.add(lblHarga, gbc);
        txtHarga = new JTextField(20);
        ThemeUtil.styleTextField(txtHarga);
        txtHarga.setEditable(false);
        gbc.gridx = 1;
        panelInput.add(txtHarga, gbc);

        // Jumlah Beli
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblJumlah = new JLabel("Jumlah Beli:");
        lblJumlah.setFont(ThemeUtil.FONT_REGULAR);
        lblJumlah.setForeground(ThemeUtil.TEXT_SECONDARY);
        panelInput.add(lblJumlah, gbc);
        txtJumlah = new JTextField(20);
        ThemeUtil.styleTextField(txtJumlah);
        gbc.gridx = 1;
        panelInput.add(txtJumlah, gbc);

        // Subtotal
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel lblSubtotal = new JLabel("Subtotal:");
        lblSubtotal.setFont(ThemeUtil.FONT_REGULAR);
        lblSubtotal.setForeground(ThemeUtil.TEXT_SECONDARY);
        panelInput.add(lblSubtotal, gbc);
        txtSubtotal = new JTextField(20);
        ThemeUtil.styleTextField(txtSubtotal);
        txtSubtotal.setEditable(false);
        gbc.gridx = 1;
        panelInput.add(txtSubtotal, gbc);

        // Button: Tambah ke Keranjang
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        JPanel panelBtnInput = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelBtnInput.setOpaque(false);
        btnTambahKeranjang = new JButton("+ Tambah ke Keranjang");
        ThemeUtil.styleButton(btnTambahKeranjang, new Color(59, 130, 246));
        panelBtnInput.add(btnTambahKeranjang);
        panelInput.add(panelBtnInput, gbc);
        gbc.gridwidth = 1;

        JPanel wrapInput = new JPanel(new BorderLayout());
        wrapInput.setOpaque(false);
        wrapInput.add(panelInput, BorderLayout.NORTH);

        // --- Right: Keranjang Table ---
        JPanel panelKeranjang = new JPanel(new BorderLayout(0, 5));
        panelKeranjang.setOpaque(false);
        TitledBorder keranjangBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Keranjang Belanja");
        keranjangBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 13));
        keranjangBorder.setTitleColor(ThemeUtil.TEXT_PRIMARY);
        panelKeranjang.setBorder(keranjangBorder);

        modelKeranjang = new DefaultTableModel(new String[]{"Barang", "Harga Satuan", "Jumlah", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableKeranjang = new JTable(modelKeranjang);
        JScrollPane scrollKeranjang = new JScrollPane(tableKeranjang);
        ThemeUtil.styleTable(tableKeranjang, scrollKeranjang);
        scrollKeranjang.setPreferredSize(new Dimension(400, 160));
        panelKeranjang.add(scrollKeranjang, BorderLayout.CENTER);

        // Keranjang footer: grand total + buttons
        JPanel panelKeranjangFooter = new JPanel(new BorderLayout(10, 0));
        panelKeranjangFooter.setOpaque(false);

        lblGrandTotal = new JLabel("Grand Total: Rp 0");
        lblGrandTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblGrandTotal.setForeground(new Color(16, 185, 129));
        panelKeranjangFooter.add(lblGrandTotal, BorderLayout.WEST);

        JPanel panelBtnKeranjang = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBtnKeranjang.setOpaque(false);

        btnHapusItem = new JButton("Hapus Item");
        ThemeUtil.styleButton(btnHapusItem, new Color(239, 68, 68));
        btnHapusItem.setEnabled(false);

        btnSimpan = new JButton("Simpan Transaksi");
        ThemeUtil.styleButton(btnSimpan, new Color(16, 185, 129));
        btnSimpan.setEnabled(false);

        btnRefresh = new JButton("Refresh");
        ThemeUtil.styleButton(btnRefresh, new Color(245, 158, 11));

        panelBtnKeranjang.add(btnHapusItem);
        panelBtnKeranjang.add(btnSimpan);
        panelBtnKeranjang.add(btnRefresh);
        panelKeranjangFooter.add(panelBtnKeranjang, BorderLayout.EAST);

        panelKeranjang.add(panelKeranjangFooter, BorderLayout.SOUTH);

        panelTop.add(wrapInput, BorderLayout.WEST);
        panelTop.add(panelKeranjang, BorderLayout.CENTER);

        // ========== BOTTOM SECTION: Riwayat ==========
        JPanel panelRiwayat = new JPanel(new BorderLayout(0, 5));
        panelRiwayat.setOpaque(false);
        TitledBorder riwayatBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Riwayat Transaksi");
        riwayatBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 13));
        riwayatBorder.setTitleColor(ThemeUtil.TEXT_PRIMARY);
        panelRiwayat.setBorder(riwayatBorder);

        modelRiwayat = new DefaultTableModel(new String[]{"No Faktur", "Tanggal", "Customer", "Total Bayar"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableRiwayat = new JTable(modelRiwayat);
        JScrollPane scrollRiwayat = new JScrollPane(tableRiwayat);
        ThemeUtil.styleTable(tableRiwayat, scrollRiwayat);
        panelRiwayat.add(scrollRiwayat, BorderLayout.CENTER);

        // Main layout
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelTop, panelRiwayat);
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerSize(5);
        splitPane.setBorder(null);
        splitPane.setOpaque(false);
        add(splitPane, BorderLayout.CENTER);

        // ========== EVENTS ==========
        cbBarang.addActionListener(e -> {
            Barang b = (Barang) cbBarang.getSelectedItem();
            if (b != null) {
                txtHarga.setText(String.valueOf(b.getHargaJual()));
                hitungSubtotal();
            }
        });

        txtJumlah.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { hitungSubtotal(); }
            public void removeUpdate(DocumentEvent e) { hitungSubtotal(); }
            public void changedUpdate(DocumentEvent e) { hitungSubtotal(); }
        });

        btnTambahKeranjang.addActionListener(e -> tambahKeKeranjang());
        btnHapusItem.addActionListener(e -> hapusDariKeranjang());
        btnSimpan.addActionListener(e -> simpanTransaksi());
        btnRefresh.addActionListener(e -> { loadCombo(); loadRiwayat(); });

        tableKeranjang.getSelectionModel().addListSelectionListener(e -> {
            btnHapusItem.setEnabled(tableKeranjang.getSelectedRow() >= 0);
        });
    }

    private void hitungSubtotal() {
        double harga = Formatter.parseDoubleSafe(txtHarga.getText());
        int jumlah = Formatter.parseIntSafe(txtJumlah.getText());
        txtSubtotal.setText(String.valueOf(harga * jumlah));
    }

    private void tambahKeKeranjang() {
        Barang b = (Barang) cbBarang.getSelectedItem();
        int jumlah = Formatter.parseIntSafe(txtJumlah.getText());

        if (b == null) {
            JOptionPane.showMessageDialog(this, "Pilih barang terlebih dahulu!");
            return;
        }
        if (jumlah <= 0) {
            JOptionPane.showMessageDialog(this, "Jumlah beli harus lebih dari 0!");
            return;
        }
        if (jumlah > b.getStok()) {
            JOptionPane.showMessageDialog(this,
                    "Stok tidak mencukupi! Sisa stok: " + b.getStok(),
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Check if barang already in cart — accumulate
        boolean found = false;
        for (DetailPenjualan d : keranjang) {
            if (d.getIdBarang().equals(b.getIdBarang())) {
                int newQty = d.getJumlahBeli() + jumlah;
                if (newQty > b.getStok()) {
                    JOptionPane.showMessageDialog(this,
                            "Total jumlah melebihi stok! Sisa stok: " + b.getStok() + ", sudah di keranjang: " + d.getJumlahBeli(),
                            "Peringatan", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                d.setJumlahBeli(newQty);
                d.setSubtotal(d.getHargaSatuan() * newQty);
                found = true;
                break;
            }
        }

        if (!found) {
            DetailPenjualan detail = new DetailPenjualan();
            detail.setIdBarang(b.getIdBarang());
            detail.setNamaBarang(b.getNamaBarang());
            detail.setHargaSatuan(b.getHargaJual());
            detail.setJumlahBeli(jumlah);
            detail.setSubtotal(b.getHargaJual() * jumlah);
            keranjang.add(detail);
        }

        refreshKeranjangTable();
        txtJumlah.setText("");
        txtSubtotal.setText("");
    }

    private void hapusDariKeranjang() {
        int selectedRow = tableKeranjang.getSelectedRow();
        if (selectedRow >= 0 && selectedRow < keranjang.size()) {
            keranjang.remove(selectedRow);
            refreshKeranjangTable();
        }
    }

    private void refreshKeranjangTable() {
        modelKeranjang.setRowCount(0);
        double grandTotal = 0;
        for (DetailPenjualan d : keranjang) {
            modelKeranjang.addRow(new Object[]{
                d.getNamaBarang(),
                Formatter.formatRupiah(d.getHargaSatuan()),
                d.getJumlahBeli(),
                Formatter.formatRupiah(d.getSubtotal())
            });
            grandTotal += d.getSubtotal();
        }
        lblGrandTotal.setText("Grand Total: " + Formatter.formatRupiah(grandTotal));
        btnSimpan.setEnabled(!keranjang.isEmpty());
    }

    public void loadCombo() {
        cbCustomer.removeAllItems();
        for (Customer c : customerService.getAllCustomer()) {
            cbCustomer.addItem(c);
        }

        cbBarang.removeAllItems();
        for (Barang b : barangService.getAllBarang()) {
            cbBarang.addItem(b);
        }
    }

    public void loadRiwayat() {
        modelRiwayat.setRowCount(0);
        for (Penjualan p : penjualanService.getAllPenjualan()) {
            modelRiwayat.addRow(new Object[]{
                p.getNoFaktur(),
                p.getTglTransaksi(),
                p.getNamaCustomer(),
                Formatter.formatRupiah(p.getTotalBayar())
            });
        }
    }

    // Keep backward compatibility for MenuUtama refresh calls
    public void loadData() {
        loadRiwayat();
    }

    private void simpanTransaksi() {
        Customer c = (Customer) cbCustomer.getSelectedItem();
        if (c == null) {
            JOptionPane.showMessageDialog(this, "Pilih customer terlebih dahulu!");
            return;
        }
        if (keranjang.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Keranjang masih kosong! Tambahkan barang terlebih dahulu.");
            return;
        }

        // Calculate grand total from cart
        double grandTotal = 0;
        for (DetailPenjualan d : keranjang) {
            grandTotal += d.getSubtotal();
        }

        Penjualan p = new Penjualan(
            0, null,
            new Date(System.currentTimeMillis()),
            c.getIdCustomer(),
            grandTotal,
            loggedInUser.getIdUser()
        );
        p.setDetails(new ArrayList<>(keranjang));

        try {
            if (penjualanService.simpanTransaksi(p)) {
                JOptionPane.showMessageDialog(this,
                        "Transaksi Berhasil!\nNo. Faktur: " + p.getNoFaktur(),
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
                keranjang.clear();
                refreshKeranjangTable();
                loadCombo(); // Refresh stok di combo
                loadRiwayat();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Transaksi Gagal: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
