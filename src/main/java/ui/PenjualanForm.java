package ui;

import model.Barang;
import model.Customer;
import model.Penjualan;
import model.User;
import service.BarangService;
import service.CustomerService;
import service.PenjualanService;
import util.Formatter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import util.ThemeUtil;
import java.awt.*;
import java.sql.Date;

public class PenjualanForm extends JPanel {

    private JComboBox<Customer> cbCustomer;
    private JComboBox<Barang> cbBarang;
    private JTextField txtHarga, txtJumlah, txtTotal;
    private JButton btnSimpan, btnRefresh;
    private JTable table;
    private DefaultTableModel tableModel;

    private PenjualanService penjualanService;
    private BarangService barangService;
    private CustomerService customerService;
    private User loggedInUser;

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
        loadData();
    }

    private void initComponents() {
        // Title
        JLabel lblTitle = new JLabel("Transaksi Kasir / Penjualan");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(ThemeUtil.TEXT_PRIMARY);
        lblTitle.setBorder(new EmptyBorder(0, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        // Input Panel
        JPanel panelTop = new JPanel(new BorderLayout(10, 10));
        panelTop.setOpaque(false);

        JPanel panelInput = new JPanel(new GridBagLayout());
        panelInput.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblCustomer = new JLabel("Customer:");
        lblCustomer.setFont(ThemeUtil.FONT_REGULAR);
        lblCustomer.setForeground(ThemeUtil.TEXT_SECONDARY);
        panelInput.add(lblCustomer, gbc);
        cbCustomer = new JComboBox<>();
        ThemeUtil.styleComboBox(cbCustomer);
        gbc.gridx = 1;
        panelInput.add(cbCustomer, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblBarang = new JLabel("Barang:");
        lblBarang.setFont(ThemeUtil.FONT_REGULAR);
        lblBarang.setForeground(ThemeUtil.TEXT_SECONDARY);
        panelInput.add(lblBarang, gbc);
        cbBarang = new JComboBox<>();
        ThemeUtil.styleComboBox(cbBarang);
        gbc.gridx = 1;
        panelInput.add(cbBarang, gbc);

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

        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblJumlah = new JLabel("Jumlah Beli:");
        lblJumlah.setFont(ThemeUtil.FONT_REGULAR);
        lblJumlah.setForeground(ThemeUtil.TEXT_SECONDARY);
        panelInput.add(lblJumlah, gbc);
        txtJumlah = new JTextField(20);
        ThemeUtil.styleTextField(txtJumlah);
        gbc.gridx = 1;
        panelInput.add(txtJumlah, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        JLabel lblTotal = new JLabel("Total Bayar:");
        lblTotal.setFont(ThemeUtil.FONT_REGULAR);
        lblTotal.setForeground(ThemeUtil.TEXT_SECONDARY);
        panelInput.add(lblTotal, gbc);
        txtTotal = new JTextField(20);
        ThemeUtil.styleTextField(txtTotal);
        txtTotal.setEditable(false);
        gbc.gridx = 1;
        panelInput.add(txtTotal, gbc);

        // Button Panel
        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelBtn.setOpaque(false);
        btnSimpan = new JButton("Simpan Transaksi");
        ThemeUtil.styleButton(btnSimpan, new Color(16, 185, 129));
        
        btnRefresh = new JButton("Refresh Data");
        ThemeUtil.styleButton(btnRefresh, new Color(245, 158, 11));
        
        panelBtn.add(btnSimpan);
        panelBtn.add(btnRefresh);

        panelTop.add(panelInput, BorderLayout.NORTH);
        panelTop.add(panelBtn, BorderLayout.CENTER);

        JPanel wrapTop = new JPanel(new BorderLayout());
        wrapTop.setOpaque(false);
        wrapTop.add(panelTop, BorderLayout.WEST);

        // Table
        tableModel = new DefaultTableModel(new String[]{"ID Transaksi", "Tanggal", "Customer", "Barang", "Jumlah", "Total"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        
        JScrollPane scrollPane = new JScrollPane(table);
        ThemeUtil.styleTable(table, scrollPane);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setOpaque(false);
        centerPanel.add(wrapTop, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Events
        cbBarang.addActionListener(e -> {
            Barang b = (Barang) cbBarang.getSelectedItem();
            if (b != null) {
                txtHarga.setText(String.valueOf(b.getHargaJual()));
                hitungTotal();
            }
        });

        txtJumlah.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { hitungTotal(); }
            public void removeUpdate(DocumentEvent e) { hitungTotal(); }
            public void changedUpdate(DocumentEvent e) { hitungTotal(); }
        });
        
        btnSimpan.addActionListener(e -> simpanTransaksi());
        btnRefresh.addActionListener(e -> { loadCombo(); loadData(); });
    }

    private void hitungTotal() {
        double harga = Formatter.parseDoubleSafe(txtHarga.getText());
        int jumlah = Formatter.parseIntSafe(txtJumlah.getText());
        txtTotal.setText(String.valueOf(harga * jumlah));
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

    public void loadData() {
        tableModel.setRowCount(0);
        for (Penjualan p : penjualanService.getAllPenjualan()) {
            tableModel.addRow(new Object[]{
                p.getIdJual(), p.getTglTransaksi(), p.getNamaCustomer(), 
                p.getNamaBarang(), p.getJumlahBeli(), Formatter.formatRupiah(p.getTotalBayar())
            });
        }
    }

    private void simpanTransaksi() {
        Customer c = (Customer) cbCustomer.getSelectedItem();
        Barang b = (Barang) cbBarang.getSelectedItem();
        int jumlah = Formatter.parseIntSafe(txtJumlah.getText());
        double total = Formatter.parseDoubleSafe(txtTotal.getText());

        if (c == null || b == null || jumlah <= 0) {
            JOptionPane.showMessageDialog(this, "Data transaksi tidak valid! Periksa jumlah beli.");
            return;
        }

        Penjualan p = new Penjualan(0, new Date(System.currentTimeMillis()), c.getIdCustomer(), b.getIdBarang(), jumlah, total, loggedInUser.getIdUser());

        try {
            if (penjualanService.simpanTransaksi(p)) {
                JOptionPane.showMessageDialog(this, "Transaksi Berhasil!");
                txtJumlah.setText("");
                loadData();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Transaksi Gagal: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
