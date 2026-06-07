package ui;

import model.User;
import service.UserService;
import ui.components.Toast;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import util.ThemeUtil;
import ui.components.RoundedPanel;
import java.awt.*;
import java.awt.event.ActionListener;

public class ProfilForm extends JPanel {

    private JTextField txtUsername, txtNamaLengkap;
    private JPasswordField txtPasswordLama, txtPasswordBaru, txtKonfirmasiPassword;
    private JButton btnSimpan;
    private UserService service;
    private User currentUser;
    private Runnable onUpdate;

    public ProfilForm(User user, Runnable onUpdate) {
        this.currentUser = user;
        this.onUpdate = onUpdate;
        service = new UserService();
        setLayout(new BorderLayout(20, 20));
        setBackground(ThemeUtil.BG_SOFT);
        setBorder(new EmptyBorder(20, 30, 20, 30));
        initComponents();
        loadData();
    }

    private void initComponents() {


        RoundedPanel panelTop = ThemeUtil.createCardPanel();
        panelTop.setLayout(new BorderLayout(10, 10));

        JPanel panelInput = new JPanel(new GridBagLayout());
        panelInput.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        addLabel(panelInput, "Username:", gbc);
        txtUsername = new JTextField(20);
        ThemeUtil.styleTextField(txtUsername);
        txtUsername.setEditable(false);
        gbc.gridx = 1; panelInput.add(txtUsername, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        addLabel(panelInput, "Nama Lengkap:", gbc);
        txtNamaLengkap = new JTextField(20);
        ThemeUtil.styleTextField(txtNamaLengkap);
        gbc.gridx = 1; panelInput.add(txtNamaLengkap, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        addLabel(panelInput, "Password Lama:", gbc);
        txtPasswordLama = new JPasswordField(20);
        ThemeUtil.stylePasswordField(txtPasswordLama);
        gbc.gridx = 1; panelInput.add(txtPasswordLama, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        addLabel(panelInput, "Password Baru:", gbc);
        txtPasswordBaru = new JPasswordField(20);
        ThemeUtil.stylePasswordField(txtPasswordBaru);
        gbc.gridx = 1; panelInput.add(txtPasswordBaru, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        addLabel(panelInput, "Konfirmasi Password:", gbc);
        txtKonfirmasiPassword = new JPasswordField(20);
        ThemeUtil.stylePasswordField(txtKonfirmasiPassword);
        gbc.gridx = 1; panelInput.add(txtKonfirmasiPassword, gbc);

        gbc.gridx = 1; gbc.gridy = 5;
        JCheckBox chkShowPassword = new JCheckBox("Tampilkan Password");
        chkShowPassword.setOpaque(false);
        chkShowPassword.setFont(ThemeUtil.FONT_REGULAR);
        chkShowPassword.setForeground(ThemeUtil.TEXT_SECONDARY);
        chkShowPassword.addItemListener(e -> {
            char echoChar = chkShowPassword.isSelected() ? (char) 0 : '\u2022';
            txtPasswordLama.setEchoChar(echoChar);
            txtPasswordBaru.setEchoChar(echoChar);
            txtKonfirmasiPassword.setEchoChar(echoChar);
        });
        panelInput.add(chkShowPassword, gbc);

        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelBtn.setOpaque(false);
        btnSimpan = new JButton("Update Profil"); 
        ThemeUtil.styleButton(btnSimpan, ThemeUtil.OCEAN_BLUE);

        panelBtn.add(btnSimpan);

        panelTop.add(panelInput, BorderLayout.WEST);
        panelTop.add(panelBtn, BorderLayout.SOUTH);
        
        // Wrap with a flow layout so it doesn't stretch vertically too much
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(panelTop, BorderLayout.NORTH);

        add(wrapper, BorderLayout.CENTER);

        // Events
        btnSimpan.addActionListener(e -> updateProfil());

        ActionListener enterSubmit = e -> btnSimpan.doClick();
        txtNamaLengkap.addActionListener(enterSubmit);
        txtPasswordLama.addActionListener(enterSubmit);
        txtPasswordBaru.addActionListener(enterSubmit);
        txtKonfirmasiPassword.addActionListener(enterSubmit);
    }

    private void addLabel(JPanel p, String text, GridBagConstraints gbc) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(ThemeUtil.FONT_REGULAR);
        lbl.setForeground(ThemeUtil.TEXT_SECONDARY);
        p.add(lbl, gbc);
    }

    private void loadData() {
        if (currentUser != null) {
            txtUsername.setText(currentUser.getUsername());
            txtNamaLengkap.setText(currentUser.getNamaLengkap());
        }
    }

    private void updateProfil() {
        if (currentUser == null) return;

        String namaBaru = txtNamaLengkap.getText().trim();
        String passLama = new String(txtPasswordLama.getPassword());
        String passBaru = new String(txtPasswordBaru.getPassword());
        String confPass = new String(txtKonfirmasiPassword.getPassword());

        if (namaBaru.isEmpty()) {
            Toast.showError((JFrame) SwingUtilities.getWindowAncestor(this), "Nama Lengkap tidak boleh kosong");
            return;
        }

        String passwordToSave = currentUser.getPassword();

        // Jika ingin ubah password
        if (!passLama.isEmpty() || !passBaru.isEmpty() || !confPass.isEmpty()) {
            if (!passLama.equals(currentUser.getPassword())) {
                Toast.showError((JFrame) SwingUtilities.getWindowAncestor(this), "Password Lama salah!");
                return;
            }
            if (passBaru.length() < 6) {
                Toast.showError((JFrame) SwingUtilities.getWindowAncestor(this), "Password Baru minimal 6 karakter!");
                return;
            }
            if (!passBaru.equals(confPass)) {
                Toast.showError((JFrame) SwingUtilities.getWindowAncestor(this), "Konfirmasi Password tidak cocok!");
                return;
            }
            passwordToSave = passBaru;
        }

        currentUser.setNamaLengkap(namaBaru);
        currentUser.setPassword(passwordToSave);

        if (service.updateUser(currentUser)) {
            Toast.showSuccess((JFrame) SwingUtilities.getWindowAncestor(this), "Profil berhasil diupdate!");
            txtPasswordLama.setText("");
            txtPasswordBaru.setText("");
            txtKonfirmasiPassword.setText("");
            if (onUpdate != null) {
                onUpdate.run();
            }
        } else {
            Toast.showError((JFrame) SwingUtilities.getWindowAncestor(this), "Gagal mengupdate profil!");
        }
    }
}
