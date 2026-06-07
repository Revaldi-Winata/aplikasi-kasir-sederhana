package ui;

import model.User;
import service.UserService;
import ui.components.Toast;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import util.ThemeUtil;
import ui.components.RoundedPanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginForm extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private UserService userService;

    public LoginForm() {
        userService = new UserService();

        setTitle("Login - Toko Berkah Jaya");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(ThemeUtil.BG_SOFT);

        initComponents();
    }

    private void initComponents() {
        Container container = getContentPane();
        container.setLayout(new GridBagLayout());

        RoundedPanel loginCard = ThemeUtil.createCardPanel();
        loginCard.setPreferredSize(new Dimension(400, 350));
        loginCard.setLayout(new BoxLayout(loginCard, BoxLayout.Y_AXIS));

        // Judul
        JLabel lblTitle = new JLabel("Toko Berkah Jaya");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(ThemeUtil.OCEAN_BLUE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblSub = new JLabel("Silakan Login ke Akun Anda");
        lblSub.setFont(ThemeUtil.FONT_REGULAR);
        lblSub.setForeground(ThemeUtil.TEXT_SECONDARY);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Username
        JPanel panelUser = new JPanel(new BorderLayout(5, 5));
        panelUser.setOpaque(false);
        panelUser.setMaximumSize(new Dimension(300, 60));
        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(ThemeUtil.FONT_REGULAR);
        lblUser.setForeground(ThemeUtil.TEXT_SECONDARY);
        txtUsername = new JTextField();
        ThemeUtil.styleTextField(txtUsername);
        panelUser.add(lblUser, BorderLayout.NORTH);
        panelUser.add(txtUsername, BorderLayout.CENTER);

        // Password
        JPanel panelPass = new JPanel(new BorderLayout(5, 5));
        panelPass.setOpaque(false);
        panelPass.setMaximumSize(new Dimension(300, 60));
        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(ThemeUtil.FONT_REGULAR);
        lblPass.setForeground(ThemeUtil.TEXT_SECONDARY);
        txtPassword = new JPasswordField();
        ThemeUtil.stylePasswordField(txtPassword);
        panelPass.add(lblPass, BorderLayout.NORTH);
        panelPass.add(txtPassword, BorderLayout.CENTER);

        // Login Button
        btnLogin = new JButton("LOGIN");
        ThemeUtil.styleButton(btnLogin, ThemeUtil.OCEAN_BLUE);
        btnLogin.setMaximumSize(new Dimension(300, 45));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Assemble
        loginCard.add(Box.createRigidArea(new Dimension(0, 20)));
        loginCard.add(lblTitle);
        loginCard.add(Box.createRigidArea(new Dimension(0, 5)));
        loginCard.add(lblSub);
        loginCard.add(Box.createRigidArea(new Dimension(0, 30)));
        loginCard.add(panelUser);
        loginCard.add(Box.createRigidArea(new Dimension(0, 15)));
        loginCard.add(panelPass);
        loginCard.add(Box.createRigidArea(new Dimension(0, 30)));
        loginCard.add(btnLogin);
        loginCard.add(Box.createRigidArea(new Dimension(0, 20)));

        container.add(loginCard);

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doLogin();
            }
        });
        
        txtPassword.addActionListener(e -> doLogin());
    }

    private void doLogin() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            Toast.showError(this, "Username dan Password tidak boleh kosong!");
            return;
        }

        User user = userService.login(username, password);
        if (user != null) {
            Toast.showSuccess(this, "Login Berhasil! Selamat Datang " + user.getNamaLengkap());
            SwingUtilities.invokeLater(() -> {
                MenuUtama menu = new MenuUtama(user);
                menu.setVisible(true);
                dispose();
            });
        } else {
            Toast.showError(this, "Username atau Password salah!");
        }
    }
}
