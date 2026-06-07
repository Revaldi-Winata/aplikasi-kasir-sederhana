package ui;

import model.User;
import service.UserService;
import ui.components.Toast;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import util.ThemeUtil;
import ui.components.RoundedPanel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class UserForm extends JPanel {

    private JTextField txtId, txtUsername, txtNamaLengkap;
    private JPasswordField txtPassword;
    private JComboBox<String> cbRole;
    private JButton btnSimpan, btnUbah, btnHapus, btnClear;
    private JTable table;
    private DefaultTableModel tableModel;
    private UserService service;

    public UserForm() {
        service = new UserService();
        setLayout(new BorderLayout(20, 20));
        setBackground(ThemeUtil.BG_SOFT);
        setBorder(new EmptyBorder(20, 30, 20, 30));
        initComponents();
        loadData();
        clear();
    }

    private void initComponents() {


        RoundedPanel panelTop = ThemeUtil.createCardPanel();
        panelTop.setLayout(new BorderLayout(10, 10));

        JPanel panelInput = new JPanel(new GridBagLayout());
        panelInput.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        addLabel(panelInput, "ID User:", gbc);
        txtId = new JTextField(20);
        ThemeUtil.styleTextField(txtId);
        txtId.setEditable(false);
        gbc.gridx = 1; panelInput.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        addLabel(panelInput, "Username:", gbc);
        txtUsername = new JTextField(20);
        ThemeUtil.styleTextField(txtUsername);
        gbc.gridx = 1; panelInput.add(txtUsername, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        addLabel(panelInput, "Password:", gbc);
        txtPassword = new JPasswordField(20);
        ThemeUtil.stylePasswordField(txtPassword);
        gbc.gridx = 1; panelInput.add(txtPassword, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        addLabel(panelInput, "Nama Lengkap:", gbc);
        txtNamaLengkap = new JTextField(20);
        ThemeUtil.styleTextField(txtNamaLengkap);
        gbc.gridx = 1; panelInput.add(txtNamaLengkap, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        addLabel(panelInput, "Role/Level:", gbc);
        cbRole = new JComboBox<>(new String[]{"Admin", "Kasir"});
        ThemeUtil.styleComboBox(cbRole);
        gbc.gridx = 1; panelInput.add(cbRole, gbc);

        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelBtn.setOpaque(false);
        btnSimpan = new JButton("Simpan"); ThemeUtil.styleButton(btnSimpan, ThemeUtil.SUCCESS_COLOR);
        btnUbah = new JButton("Ubah"); ThemeUtil.styleButton(btnUbah, ThemeUtil.OCEAN_BLUE);
        btnHapus = new JButton("Hapus"); ThemeUtil.styleButton(btnHapus, ThemeUtil.ERROR_COLOR);
        btnClear = new JButton("Clear"); ThemeUtil.styleButton(btnClear, ThemeUtil.TEXT_SECONDARY);

        panelBtn.add(btnSimpan); panelBtn.add(btnUbah); panelBtn.add(btnHapus); panelBtn.add(btnClear);

        panelTop.add(panelInput, BorderLayout.WEST);
        panelTop.add(panelBtn, BorderLayout.SOUTH);
        
        add(panelTop, BorderLayout.NORTH);

        RoundedPanel panelBottom = ThemeUtil.createCardPanel();
        panelBottom.setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"ID User", "Username", "Nama Lengkap", "Role"}, 0) {
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

        ActionListener enterSubmit = e -> {
            if (table.getSelectedRow() >= 0) btnUbah.doClick();
            else btnSimpan.doClick();
        };
        txtUsername.addActionListener(enterSubmit);
        txtPassword.addActionListener(enterSubmit);
        txtNamaLengkap.addActionListener(enterSubmit);

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtId.setText(tableModel.getValueAt(row, 0).toString());
                    txtUsername.setText(tableModel.getValueAt(row, 1).toString());
                    txtPassword.setText(""); // Jangan tampilkan password
                    txtNamaLengkap.setText(tableModel.getValueAt(row, 2).toString());
                    cbRole.setSelectedItem(tableModel.getValueAt(row, 3).toString());
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
        List<User> list = service.getAllUser();
        for (User u : list) {
            tableModel.addRow(new Object[]{u.getIdUser(), u.getUsername(), u.getNamaLengkap(), u.getLevel()});
        }
    }

    private void clear() {
        txtId.setText(service.getNextAutoIncrement());
        txtUsername.setText("");
        txtPassword.setText("");
        txtNamaLengkap.setText("");
        cbRole.setSelectedIndex(0);
        table.clearSelection();
    }

    private void simpanData() {
        if (txtUsername.getText().trim().isEmpty() || new String(txtPassword.getPassword()).trim().isEmpty()) {
            Toast.showError((JFrame) SwingUtilities.getWindowAncestor(this), "Username dan Password tidak boleh kosong");
            return;
        }

        String passwordInput = new String(txtPassword.getPassword());

        User u = new User();
        u.setUsername(txtUsername.getText().trim());
        u.setPassword(passwordInput);
        u.setNamaLengkap(txtNamaLengkap.getText().trim());
        u.setLevel(cbRole.getSelectedItem().toString());
        
        if (service.tambahUser(u)) {
            Toast.showSuccess((JFrame) SwingUtilities.getWindowAncestor(this), "User disimpan");
            loadData();
            clear();
        } else {
            Toast.showError((JFrame) SwingUtilities.getWindowAncestor(this), "Gagal menyimpan user");
        }
    }

    private void ubahData() {
        if (table.getSelectedRow() < 0) {
            Toast.showError((JFrame) SwingUtilities.getWindowAncestor(this), "Pilih data!");
            return;
        }

        String passwordInput = new String(txtPassword.getPassword());
        User u = new User();
        try {
            u.setIdUser(Integer.parseInt(txtId.getText()));
        } catch (NumberFormatException e) {
            Toast.showError((JFrame) SwingUtilities.getWindowAncestor(this), "ID User tidak valid");
            return;
        }
        
        u.setUsername(txtUsername.getText().trim());
        u.setNamaLengkap(txtNamaLengkap.getText().trim());
        u.setLevel(cbRole.getSelectedItem().toString());

        if (!passwordInput.isEmpty()) {
            u.setPassword(passwordInput);
        } else {
            // retain old password
            List<User> users = service.getAllUser();
            for(User old : users) {
                if(old.getIdUser() == u.getIdUser()) {
                    u.setPassword(old.getPassword());
                    break;
                }
            }
        }

        if (service.updateUser(u)) {
            Toast.showSuccess((JFrame) SwingUtilities.getWindowAncestor(this), "User diubah");
            loadData();
            clear();
        } else {
            Toast.showError((JFrame) SwingUtilities.getWindowAncestor(this), "Gagal mengubah user");
        }
    }

    private void hapusData() {
        if (table.getSelectedRow() < 0) {
            Toast.showError((JFrame) SwingUtilities.getWindowAncestor(this), "Pilih data!");
            return;
        }
        
        int idUser;
        try {
            idUser = Integer.parseInt(txtId.getText());
        } catch (NumberFormatException e) {
            Toast.showError((JFrame) SwingUtilities.getWindowAncestor(this), "ID User tidak valid");
            return;
        }

        if (service.hapusUser(idUser)) {
            Toast.showSuccess((JFrame) SwingUtilities.getWindowAncestor(this), "User dihapus");
            loadData();
            clear();
        } else {
            Toast.showError((JFrame) SwingUtilities.getWindowAncestor(this), "Gagal menghapus user");
        }
    }
}
