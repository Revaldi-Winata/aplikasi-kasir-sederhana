package ui;

import model.User;
import service.UserService;
import util.ThemeUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class UserForm extends JPanel {
    private JTextField txtIdUser, txtUsername, txtNamaLengkap;
    private JTextField txtPassword;
    private JComboBox<String> cbLevel;
    private JButton btnSimpan, btnUbah, btnHapus, btnClear;
    private JTable table;
    private DefaultTableModel tableModel;

    private UserService userService;

    public UserForm() {
        userService = new UserService();

        setLayout(new BorderLayout(10, 10));
        setBackground(ThemeUtil.BG_SOFT);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        initComponents();
        loadData();
        clearForm(); // To set "(Otomatis)"
    }

    private void initComponents() {
        // Title
        JLabel lblTitle = new JLabel("Manajemen Data User");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(ThemeUtil.TEXT_PRIMARY);
        lblTitle.setBorder(new EmptyBorder(0, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        // Input Panel
        JPanel panelTop = new JPanel(new BorderLayout(10, 10));
        panelTop.setOpaque(false);

        // Form Panel
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // ID User (Hidden or Readonly)
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblId = new JLabel("ID User:");
        lblId.setFont(ThemeUtil.FONT_REGULAR);
        lblId.setForeground(ThemeUtil.TEXT_SECONDARY);
        panelForm.add(lblId, gbc);
        txtIdUser = new JTextField(20);
        ThemeUtil.styleTextField(txtIdUser);
        txtIdUser.setEditable(false);
        txtIdUser.setBackground(ThemeUtil.HOVER_BG);
        gbc.gridx = 1;
        panelForm.add(txtIdUser, gbc);

        // Nama Lengkap
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblNama = new JLabel("Nama Lengkap:");
        lblNama.setFont(ThemeUtil.FONT_REGULAR);
        lblNama.setForeground(ThemeUtil.TEXT_SECONDARY);
        panelForm.add(lblNama, gbc);
        txtNamaLengkap = new JTextField(20);
        ThemeUtil.styleTextField(txtNamaLengkap);
        gbc.gridx = 1;
        panelForm.add(txtNamaLengkap, gbc);

        // Username
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblUser = new JLabel("Username:");
        lblUser.setFont(ThemeUtil.FONT_REGULAR);
        lblUser.setForeground(ThemeUtil.TEXT_SECONDARY);
        panelForm.add(lblUser, gbc);
        txtUsername = new JTextField(20);
        ThemeUtil.styleTextField(txtUsername);
        gbc.gridx = 1;
        panelForm.add(txtUsername, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(ThemeUtil.FONT_REGULAR);
        lblPass.setForeground(ThemeUtil.TEXT_SECONDARY);
        panelForm.add(lblPass, gbc);
        
        txtPassword = new JTextField(20);
        ThemeUtil.styleTextField(txtPassword);
        
        gbc.gridx = 1;
        panelForm.add(txtPassword, gbc);

        // Level
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel lblLevel = new JLabel("Level Akses:");
        lblLevel.setFont(ThemeUtil.FONT_REGULAR);
        lblLevel.setForeground(ThemeUtil.TEXT_SECONDARY);
        panelForm.add(lblLevel, gbc);
        cbLevel = new JComboBox<>(new String[]{"Admin", "Kasir"});
        cbLevel.setFont(ThemeUtil.FONT_REGULAR);
        cbLevel.setBackground(ThemeUtil.BG_WHITE);
        gbc.gridx = 1;
        panelForm.add(cbLevel, gbc);

        // Buttons
        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelBtn.setOpaque(false);
        
        btnSimpan = new JButton("Simpan Data");
        btnUbah = new JButton("Simpan Perubahan");
        btnHapus = new JButton("Hapus Data");
        btnClear = new JButton("Bersihkan Form");

        ThemeUtil.styleButton(btnSimpan, new Color(16, 185, 129)); // Emerald 500
        ThemeUtil.styleButton(btnUbah, new Color(59, 130, 246)); // Blue 500
        ThemeUtil.styleButton(btnHapus, new Color(239, 68, 68)); // Red 500
        ThemeUtil.styleButton(btnClear, new Color(100, 116, 139)); // Slate 500

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
        wrapInput.add(panelForm);

        panelTop.add(wrapInput, BorderLayout.NORTH);
        panelTop.add(panelBtn, BorderLayout.CENTER);

        JPanel wrapTop = new JPanel(new BorderLayout());
        wrapTop.setOpaque(false);
        wrapTop.add(panelTop, BorderLayout.WEST);

        // Table
        tableModel = new DefaultTableModel(new String[]{"ID User", "Nama Lengkap", "Username", "Password", "Level"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        ThemeUtil.styleTable(table, scrollPane);
        
        // Atur lebar kolom ID agar kecil
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(0).setMaxWidth(100);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setOpaque(false);
        centerPanel.add(wrapTop, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Event Listeners
        btnSimpan.addActionListener(e -> simpan());
        btnUbah.addActionListener(e -> ubah());
        btnHapus.addActionListener(e -> hapus());
        btnClear.addActionListener(e -> clearForm());

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    txtIdUser.setText(table.getValueAt(row, 0).toString());
                    txtNamaLengkap.setText(table.getValueAt(row, 1).toString());
                    txtUsername.setText(table.getValueAt(row, 2).toString());
                    // Password disembunyikan di tabel, biarkan form teks kosong
                    // Atau bisa fetch ulang dari DB kalau perlu. Di sini kita biarkan kosong agar aman, 
                    // namun karena di tabel kita akan load data asli (atau bintang-bintang), kita handle saat ubah.
                    // Untuk kemudahan, kita ambil data asli dan tampilkan ke password field.
                    // Tapi pastikan password asli dimasukkan ke tabel atau sembunyikan.
                    txtPassword.setText(table.getValueAt(row, 3).toString());
                    cbLevel.setSelectedItem(table.getValueAt(row, 4).toString());
                    
                    btnSimpan.setEnabled(false);
                    btnUbah.setEnabled(true);
                    btnHapus.setEnabled(true);
                }
            }
        });
        
        // Deselect table if clicked outside
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clearForm();
            }
        });
        panelForm.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clearForm();
            }
        });
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<User> list = userService.getAllUser();
        for (User u : list) {
            tableModel.addRow(new Object[]{
                u.getIdUser(),
                u.getNamaLengkap(),
                u.getUsername(),
                u.getPassword(), // In production, never show passwords!
                u.getLevel()
            });
        }
    }

    private void clearForm() {
        txtIdUser.setText(userService.getNextAutoIncrement());
        txtNamaLengkap.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
        cbLevel.setSelectedIndex(0);
        table.clearSelection();
        
        btnSimpan.setEnabled(true);
        btnUbah.setEnabled(false);
        btnHapus.setEnabled(false);
    }

    private void simpan() {
        String nama = txtNamaLengkap.getText().trim();
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();
        String level = cbLevel.getSelectedItem().toString();

        if (nama.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            return;
        }

        User u = new User();
        u.setNamaLengkap(nama);
        u.setUsername(username);
        u.setPassword(password);
        u.setLevel(level);

        if (userService.tambahUser(u)) {
            JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");
            clearForm();
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data!");
        }
    }

    private void ubah() {
        if (txtIdUser.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin diubah!");
            return;
        }

        int id = Integer.parseInt(txtIdUser.getText());
        String nama = txtNamaLengkap.getText().trim();
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();
        String level = cbLevel.getSelectedItem().toString();

        if (nama.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            return;
        }

        User u = new User();
        u.setIdUser(id);
        u.setNamaLengkap(nama);
        u.setUsername(username);
        u.setPassword(password);
        u.setLevel(level);

        if (userService.updateUser(u)) {
            JOptionPane.showMessageDialog(this, "Data berhasil diubah!");
            clearForm();
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal mengubah data!");
        }
    }

    private void hapus() {
        if (txtIdUser.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus user ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(txtIdUser.getText());
            if (userService.hapusUser(id)) {
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
                clearForm();
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus data!");
            }
        }
    }
}
