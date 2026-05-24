package ui;

import model.User;
import service.UserService;
import util.ThemeUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ProfilForm extends JPanel {
    private JTextField txtUsername, txtNamaLengkap, txtLevel;
    private JPasswordField txtPassword;
    private JCheckBox chkShowPassword;
    private JButton btnSimpan;

    private User currentUser;
    private UserService userService;
    private Runnable onProfileUpdated;

    public ProfilForm(User user, Runnable onProfileUpdated) {
        this.currentUser = user;
        this.userService = new UserService();
        this.onProfileUpdated = onProfileUpdated;

        setLayout(new BorderLayout(10, 10));
        setBackground(ThemeUtil.BG_SOFT);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        initComponents();
        loadData();
    }

    private void initComponents() {
        // Title
        JLabel lblTitle = new JLabel("Manajemen Profil Saya");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(ThemeUtil.TEXT_PRIMARY);
        lblTitle.setBorder(new EmptyBorder(0, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        JPanel panelInput = new JPanel(new GridBagLayout());
        panelInput.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Level (Read Only)
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblLevel = new JLabel("Level Akses:");
        lblLevel.setFont(ThemeUtil.FONT_REGULAR);
        lblLevel.setForeground(ThemeUtil.TEXT_SECONDARY);
        panelInput.add(lblLevel, gbc);
        txtLevel = new JTextField(20);
        ThemeUtil.styleTextField(txtLevel);
        txtLevel.setEditable(false);
        gbc.gridx = 1;
        panelInput.add(txtLevel, gbc);

        // Username
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblUser = new JLabel("Username:");
        lblUser.setFont(ThemeUtil.FONT_REGULAR);
        lblUser.setForeground(ThemeUtil.TEXT_SECONDARY);
        panelInput.add(lblUser, gbc);
        txtUsername = new JTextField(20);
        ThemeUtil.styleTextField(txtUsername);
        gbc.gridx = 1;
        panelInput.add(txtUsername, gbc);

        // Nama Lengkap
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblNama = new JLabel("Nama Lengkap:");
        lblNama.setFont(ThemeUtil.FONT_REGULAR);
        lblNama.setForeground(ThemeUtil.TEXT_SECONDARY);
        panelInput.add(lblNama, gbc);
        txtNamaLengkap = new JTextField(20);
        ThemeUtil.styleTextField(txtNamaLengkap);
        gbc.gridx = 1;
        panelInput.add(txtNamaLengkap, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(ThemeUtil.FONT_REGULAR);
        lblPass.setForeground(ThemeUtil.TEXT_SECONDARY);
        panelInput.add(lblPass, gbc);
        
        JPanel passPanel = new JPanel(new BorderLayout(5, 0));
        passPanel.setOpaque(false);
        txtPassword = new JPasswordField(20);
        ThemeUtil.styleTextField(txtPassword);
        passPanel.add(txtPassword, BorderLayout.CENTER);
        
        chkShowPassword = new JCheckBox("Tampilkan");
        chkShowPassword.setOpaque(false);
        chkShowPassword.setFont(ThemeUtil.FONT_REGULAR);
        chkShowPassword.setForeground(ThemeUtil.TEXT_SECONDARY);
        chkShowPassword.addActionListener(e -> {
            if (chkShowPassword.isSelected()) {
                txtPassword.setEchoChar((char) 0);
            } else {
                txtPassword.setEchoChar('•');
            }
        });
        passPanel.add(chkShowPassword, BorderLayout.EAST);
        
        gbc.gridx = 1;
        panelInput.add(passPanel, gbc);

        // Button
        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelBtn.setOpaque(false);
        panelBtn.setBorder(new EmptyBorder(15, 0, 0, 0));
        btnSimpan = new JButton("Simpan Perubahan");
        ThemeUtil.styleButton(btnSimpan, new Color(59, 130, 246));
        btnSimpan.addActionListener(e -> simpan());
        panelBtn.add(btnSimpan);

        gbc.gridx = 1; gbc.gridy = 4;
        panelInput.add(panelBtn, gbc);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);

        JPanel wrapTop = new JPanel(new BorderLayout());
        wrapTop.setOpaque(false);
        wrapTop.add(panelInput, BorderLayout.WEST);
        
        centerPanel.add(wrapTop, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void loadData() {
        txtLevel.setText(currentUser.getLevel());
        txtUsername.setText(currentUser.getUsername());
        txtNamaLengkap.setText(currentUser.getNamaLengkap());
        txtPassword.setText(currentUser.getPassword());
    }

    private void simpan() {
        String username = txtUsername.getText().trim();
        String nama = txtNamaLengkap.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if(username.isEmpty() || nama.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            return;
        }

        currentUser.setUsername(username);
        currentUser.setNamaLengkap(nama);
        currentUser.setPassword(password);

        if(userService.updateUser(currentUser)) {
            JOptionPane.showMessageDialog(this, "Profil berhasil diperbarui!");
            if(onProfileUpdated != null) {
                onProfileUpdated.run();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Gagal memperbarui profil!");
        }
    }
}
