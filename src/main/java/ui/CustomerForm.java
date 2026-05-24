package ui;

import model.Customer;
import service.CustomerService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import util.ThemeUtil;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class CustomerForm extends JPanel {

    private JTextField txtId, txtNama, txtTelepon;
    private JTextArea txtAlamat;
    private JButton btnSimpan, btnUbah, btnHapus, btnClear;
    private JTable table;
    private DefaultTableModel tableModel;
    private CustomerService service;

    public CustomerForm() {
        service = new CustomerService();
        setLayout(new BorderLayout(10, 10));
        setBackground(ThemeUtil.BG_SOFT);
        setBorder(new EmptyBorder(30, 30, 30, 30));
        initComponents();
        loadData();
        clear(); // Untuk men-generate ID awal
    }

    private void initComponents() {
        // Title
        JLabel lblTitle = new JLabel("Manajemen Data Customer");
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
        JLabel lblId = new JLabel("ID Customer:");
        lblId.setFont(ThemeUtil.FONT_REGULAR);
        lblId.setForeground(ThemeUtil.TEXT_SECONDARY);
        panelInput.add(lblId, gbc);
        txtId = new JTextField(20);
        ThemeUtil.styleTextField(txtId);
        txtId.setEditable(false);
        gbc.gridx = 1;
        panelInput.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblNama = new JLabel("Nama Customer:");
        lblNama.setFont(ThemeUtil.FONT_REGULAR);
        lblNama.setForeground(ThemeUtil.TEXT_SECONDARY);
        panelInput.add(lblNama, gbc);
        txtNama = new JTextField(20);
        ThemeUtil.styleTextField(txtNama);
        gbc.gridx = 1;
        panelInput.add(txtNama, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblTelepon = new JLabel("Telepon:");
        lblTelepon.setFont(ThemeUtil.FONT_REGULAR);
        lblTelepon.setForeground(ThemeUtil.TEXT_SECONDARY);
        panelInput.add(lblTelepon, gbc);
        txtTelepon = new JTextField(20);
        ThemeUtil.styleTextField(txtTelepon);
        gbc.gridx = 1;
        panelInput.add(txtTelepon, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblAlamat = new JLabel("Alamat:");
        lblAlamat.setFont(ThemeUtil.FONT_REGULAR);
        lblAlamat.setForeground(ThemeUtil.TEXT_SECONDARY);
        panelInput.add(lblAlamat, gbc);
        txtAlamat = new JTextArea(3, 20);
        txtAlamat.setFont(ThemeUtil.FONT_REGULAR);
        txtAlamat.setForeground(ThemeUtil.TEXT_PRIMARY);
        txtAlamat.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeUtil.BORDER_COLOR, 1, true),
            new EmptyBorder(8, 12, 8, 12)
        ));
        gbc.gridx = 1;
        panelInput.add(new JScrollPane(txtAlamat), gbc);

        // Button Panel
        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelBtn.setOpaque(false);
        btnSimpan = new JButton("Simpan Data");
        ThemeUtil.styleButton(btnSimpan, new Color(16, 185, 129));
        
        btnUbah = new JButton("Simpan Perubahan");
        ThemeUtil.styleButton(btnUbah, new Color(59, 130, 246));
        
        btnHapus = new JButton("Hapus Data");
        ThemeUtil.styleButton(btnHapus, new Color(239, 68, 68));
        
        btnClear = new JButton("Bersihkan Form");
        ThemeUtil.styleButton(btnClear, new Color(100, 116, 139));

        panelBtn.add(btnSimpan);
        panelBtn.add(btnUbah);
        panelBtn.add(btnHapus);
        panelBtn.add(btnClear);
        
        // Initial state for buttons (Option B UX)
        btnUbah.setEnabled(false);
        btnHapus.setEnabled(false);
        btnSimpan.setEnabled(true);

        JPanel wrapInput = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrapInput.setOpaque(false);
        wrapInput.add(panelInput);

        panelTop.add(wrapInput, BorderLayout.NORTH);
        panelTop.add(panelBtn, BorderLayout.CENTER);

        JPanel wrapTop = new JPanel(new BorderLayout());
        wrapTop.setOpaque(false);
        wrapTop.add(panelTop, BorderLayout.WEST);

        // Table
        tableModel = new DefaultTableModel(new String[]{"ID Customer", "Nama", "Telepon", "Alamat"}, 0) {
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

        // Actions
        btnSimpan.addActionListener(e -> simpan());
        btnUbah.addActionListener(e -> ubah());
        btnHapus.addActionListener(e -> hapus());
        btnClear.addActionListener(e -> clear());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                int row = table.getSelectedRow();
                if(row != -1) {
                    txtId.setText(table.getValueAt(row, 0).toString());
                    txtNama.setText(table.getValueAt(row, 1).toString());
                    txtTelepon.setText(table.getValueAt(row, 2).toString());
                    txtAlamat.setText(table.getValueAt(row, 3).toString());
                    
                    // Option B UX: Lock Simpan, Unlock Ubah & Hapus
                    btnSimpan.setEnabled(false);
                    btnUbah.setEnabled(true);
                    btnHapus.setEnabled(true);
                }
            }
        });
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<Customer> list = service.getAllCustomer();
        for (Customer c : list) {
            tableModel.addRow(new Object[]{c.getIdCustomer(), c.getNamaCustomer(), c.getTelepon(), c.getAlamat()});
        }
    }

    private void clear() {
        txtId.setText(service.generateId());
        txtNama.setText("");
        txtTelepon.setText("");
        txtAlamat.setText("");
        txtNama.requestFocus();
        table.clearSelection();
        
        // Option B UX: Reset buttons
        btnSimpan.setEnabled(true);
        btnUbah.setEnabled(false);
        btnHapus.setEnabled(false);
    }

    private void simpan() {
        Customer c = new Customer(txtId.getText(), txtNama.getText(), txtAlamat.getText(), txtTelepon.getText());
        if (service.tambahCustomer(c)) {
            JOptionPane.showMessageDialog(this, "Data tersimpan!");
            clear();
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data!");
        }
    }

    private void ubah() {
        if(txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih data di tabel terlebih dahulu!");
            return;
        }

        int selectedRow = table.getSelectedRow();
        if(selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris data di tabel terlebih dahulu!");
            return;
        }

        String oldNama = table.getValueAt(selectedRow, 1).toString();
        String oldTelp = table.getValueAt(selectedRow, 2).toString();
        String oldAlamat = table.getValueAt(selectedRow, 3).toString();

        Customer c = new Customer(txtId.getText(), txtNama.getText(), txtAlamat.getText(), txtTelepon.getText());
        if (service.updateCustomer(c)) {
            String msg = "Data Customer berhasil diperbarui!\n\n"
                       + "Nama: " + oldNama + " -> " + c.getNamaCustomer() + "\n"
                       + "Telepon: " + oldTelp + " -> " + c.getTelepon() + "\n"
                       + "Alamat: " + oldAlamat + " -> " + c.getAlamat();
            JOptionPane.showMessageDialog(this, msg, "Informasi Perubahan", JOptionPane.INFORMATION_MESSAGE);
            clear();
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal mengubah data!");
        }
    }

    private void hapus() {
        if(txtId.getText().isEmpty()) return;
        if (service.hapusCustomer(txtId.getText())) {
            JOptionPane.showMessageDialog(this, "Data dihapus!");
            clear();
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menghapus data!");
        }
    }
}
