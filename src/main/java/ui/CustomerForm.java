package ui;

import model.Customer;
import service.CustomerService;
import ui.components.Toast;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import util.ThemeUtil;
import ui.components.RoundedPanel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class CustomerForm extends JPanel {

    private JTextField txtId, txtNama, txtAlamat, txtTelepon;
    private JButton btnSimpan, btnUbah, btnHapus, btnClear;
    private JTable table;
    private DefaultTableModel tableModel;
    private CustomerService service;

    public CustomerForm() {
        service = new CustomerService();
        setLayout(new BorderLayout(20, 20));
        setBackground(ThemeUtil.BG_SOFT);
        setBorder(new EmptyBorder(20, 30, 20, 30));
        initComponents();
        loadData();
        clear();
    }

    private void initComponents() {
        JLabel lblTitle = new JLabel("Manajemen Customer");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(ThemeUtil.OCEAN_BLUE);
        add(lblTitle, BorderLayout.NORTH);

        RoundedPanel panelTop = ThemeUtil.createCardPanel();
        panelTop.setLayout(new BorderLayout(10, 10));

        JPanel panelInput = new JPanel(new GridBagLayout());
        panelInput.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; addLabel(panelInput, "ID Customer:", gbc);
        txtId = new JTextField(20); ThemeUtil.styleTextField(txtId); txtId.setEditable(false);
        gbc.gridx = 1; panelInput.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1; addLabel(panelInput, "Nama Customer:", gbc);
        txtNama = new JTextField(20); ThemeUtil.styleTextField(txtNama);
        gbc.gridx = 1; panelInput.add(txtNama, gbc);

        gbc.gridx = 0; gbc.gridy = 2; addLabel(panelInput, "Alamat:", gbc);
        txtAlamat = new JTextField(20); ThemeUtil.styleTextField(txtAlamat);
        gbc.gridx = 1; panelInput.add(txtAlamat, gbc);

        gbc.gridx = 0; gbc.gridy = 3; addLabel(panelInput, "No Telepon:", gbc);
        txtTelepon = new JTextField(20); ThemeUtil.styleTextField(txtTelepon);
        gbc.gridx = 1; panelInput.add(txtTelepon, gbc);

        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelBtn.setOpaque(false);
        btnSimpan = new JButton("Simpan"); ThemeUtil.styleButton(btnSimpan, ThemeUtil.SUCCESS_COLOR);
        btnUbah = new JButton("Ubah"); ThemeUtil.styleButton(btnUbah, ThemeUtil.OCEAN_BLUE);
        btnHapus = new JButton("Hapus"); ThemeUtil.styleButton(btnHapus, ThemeUtil.ERROR_COLOR);
        btnClear = new JButton("Clear"); ThemeUtil.styleButton(btnClear, ThemeUtil.TEXT_SECONDARY);

        panelBtn.add(btnSimpan); panelBtn.add(btnUbah); panelBtn.add(btnHapus); panelBtn.add(btnClear);

        panelTop.add(panelInput, BorderLayout.CENTER);
        panelTop.add(panelBtn, BorderLayout.SOUTH);
        
        add(panelTop, BorderLayout.NORTH);

        RoundedPanel panelBottom = ThemeUtil.createCardPanel();
        panelBottom.setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"ID Customer", "Nama Customer", "Alamat", "No Telepon"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        ThemeUtil.styleTable(table, scrollPane);

        panelBottom.add(scrollPane, BorderLayout.CENTER);
        add(panelBottom, BorderLayout.CENTER);

        // Events
        btnSimpan.addActionListener(e -> simpanData());
        btnUbah.addActionListener(e -> ubahData());
        btnHapus.addActionListener(e -> hapusData());
        btnClear.addActionListener(e -> clear());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtId.setText(tableModel.getValueAt(row, 0).toString());
                    txtId.setEditable(false);
                    txtNama.setText(tableModel.getValueAt(row, 1).toString());
                    txtAlamat.setText(tableModel.getValueAt(row, 2).toString());
                    txtTelepon.setText(tableModel.getValueAt(row, 3).toString());
                }
            }
        });
    }

    private void addLabel(JPanel p, String text, GridBagConstraints gbc) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(ThemeUtil.FONT_REGULAR);
        lbl.setForeground(ThemeUtil.TEXT_SECONDARY);
        p.add(lbl, gbc);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<Customer> list = service.getAllCustomer();
        for (Customer c : list) {
            tableModel.addRow(new Object[]{c.getIdCustomer(), c.getNamaCustomer(), c.getAlamat(), c.getTelepon()});
        }
    }

    private void clear() {
        txtId.setText(service.generateId());
        txtId.setEditable(false);
        txtNama.setText("");
        txtAlamat.setText("");
        txtTelepon.setText("");
        table.clearSelection();
    }

    private void simpanData() {
        if (txtNama.getText().trim().isEmpty()) {
            Toast.showError((JFrame) SwingUtilities.getWindowAncestor(this), "Nama Customer tidak boleh kosong!");
            return;
        }

        Customer c = new Customer();
        c.setIdCustomer(txtId.getText());
        c.setNamaCustomer(txtNama.getText().trim());
        c.setAlamat(txtAlamat.getText().trim());
        c.setTelepon(txtTelepon.getText().trim());
        
        if (service.tambahCustomer(c)) {
            Toast.showSuccess((JFrame) SwingUtilities.getWindowAncestor(this), "Data berhasil disimpan");
            loadData();
            clear();
        } else {
            Toast.showError((JFrame) SwingUtilities.getWindowAncestor(this), "Gagal menyimpan data!");
        }
    }

    private void ubahData() {
        if (table.getSelectedRow() < 0) {
            Toast.showError((JFrame) SwingUtilities.getWindowAncestor(this), "Pilih data yang akan diubah!");
            return;
        }

        Customer c = new Customer();
        c.setIdCustomer(txtId.getText());
        c.setNamaCustomer(txtNama.getText().trim());
        c.setAlamat(txtAlamat.getText().trim());
        c.setTelepon(txtTelepon.getText().trim());
        
        if (service.updateCustomer(c)) {
            Toast.showSuccess((JFrame) SwingUtilities.getWindowAncestor(this), "Data berhasil diubah");
            loadData();
            clear();
        } else {
            Toast.showError((JFrame) SwingUtilities.getWindowAncestor(this), "Gagal mengubah data!");
        }
    }

    private void hapusData() {
        if (table.getSelectedRow() < 0) {
            Toast.showError((JFrame) SwingUtilities.getWindowAncestor(this), "Pilih data yang akan dihapus!");
            return;
        }

        if (service.hapusCustomer(txtId.getText())) {
            Toast.showSuccess((JFrame) SwingUtilities.getWindowAncestor(this), "Data berhasil dihapus");
            loadData();
            clear();
        } else {
            Toast.showError((JFrame) SwingUtilities.getWindowAncestor(this), "Gagal menghapus data!");
        }
    }
}
