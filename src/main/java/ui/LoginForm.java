package ui;

import model.User;
import service.UserService;
import ui.components.Toast;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import util.ThemeUtil;
import util.ValidationUtil;
import ui.components.RoundedPanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// [Tampilan] Class LoginForm adalah jendela pertama yang terbuka saat aplikasi dijalankan
// Class ini mewarisi JFrame, yang berarti ini adalah form/window utama.
public class LoginForm extends JFrame {

    // [Deklarasi UI] Komponen-komponen yang akan digunakan di dalam form ini
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    
    // [Deklarasi Logika] UserService digunakan untuk menghubungkan form ini dengan database
    private UserService userService;

    public LoginForm() {
        // [Inisialisasi Mekanisme] Membuat object dari UserService agar siap dipakai untuk cek login
        userService = new UserService();

        // [Konfigurasi Tampilan] Pengaturan dasar jendela aplikasi
        setTitle("Login - Toko Berkah Jaya");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Aplikasi akan tertutup jika form disilang
        setLocationRelativeTo(null); // Membuat posisi jendela otomatis di tengah layar
        getContentPane().setBackground(ThemeUtil.BG_SOFT); // Menerapkan warna latar dari ThemeUtil

        // [Tampilan] Mengatur ikon jendela (pojok kiri atas)
        ImageIcon logoIcon = ThemeUtil.getScaledImage("/images/logo.png", 64, 64);
        if (logoIcon != null) {
            setIconImage(logoIcon.getImage());
        }

        // [Mekanisme] Memanggil fungsi yang menyusun letak dan bentuk tombol, teks, dll
        initComponents();
    }

    private void initComponents() {
        // [Mekanisme Layout] Container adalah kanvas utama dari jendela ini.
        // GridBagLayout digunakan agar kotak form login (loginCard) bisa persis di tengah
        Container container = getContentPane();
        container.setLayout(new GridBagLayout());

        // [Tampilan] Membuat kotak putih dengan sudut melengkung sebagai latar form
        RoundedPanel loginCard = ThemeUtil.createCardPanel();
        loginCard.setPreferredSize(new Dimension(400, 480));
        loginCard.setLayout(new BoxLayout(loginCard, BoxLayout.Y_AXIS)); // Layout menyusun dari atas ke bawah

        // --- Logo & Judul ---
        JLabel lblLogo = new JLabel();
        ImageIcon bigLogo = ThemeUtil.getScaledImage("/images/logo.png", 80, 80);
        if (bigLogo != null) lblLogo.setIcon(bigLogo);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitle = new JLabel("Toko Berkah Jaya");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(ThemeUtil.OCEAN_BLUE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblSub = new JLabel("Silakan Login ke Akun Anda");
        lblSub.setFont(ThemeUtil.FONT_REGULAR);
        lblSub.setForeground(ThemeUtil.TEXT_SECONDARY);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Bagian Input Username ---
        // [UI] Membuat panel khusus untuk menampung teks "Username" dan kotak ketikannya
        JPanel panelUser = new JPanel(new BorderLayout(5, 5));
        panelUser.setOpaque(false);
        panelUser.setMaximumSize(new Dimension(300, 60));
        
        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(ThemeUtil.FONT_REGULAR);
        lblUser.setForeground(ThemeUtil.TEXT_SECONDARY);
        
        // [UI] Membuat kotak input untuk username
        txtUsername = new JTextField();
        ThemeUtil.styleTextField(txtUsername);
        ValidationUtil.addRequiredValidation(txtUsername); // [Logika UI] Mencegah kotak kosong
        
        panelUser.add(lblUser, BorderLayout.NORTH);
        panelUser.add(txtUsername, BorderLayout.CENTER);

        // --- Bagian Input Password ---
        // [UI] Mirip dengan Username, tetapi menggunakan JPasswordField agar tulisan menjadi titik-titik (*)
        JPanel panelPass = new JPanel(new BorderLayout(5, 5));
        panelPass.setOpaque(false);
        panelPass.setMaximumSize(new Dimension(300, 60));
        
        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(ThemeUtil.FONT_REGULAR);
        lblPass.setForeground(ThemeUtil.TEXT_SECONDARY);
        
        txtPassword = new JPasswordField();
        ThemeUtil.stylePasswordField(txtPassword);
        ValidationUtil.addRequiredValidation(txtPassword);
        
        panelPass.add(lblPass, BorderLayout.NORTH);
        panelPass.add(txtPassword, BorderLayout.CENTER);

        // --- Tombol Login ---
        // [UI] Deklarasi tombol "LOGIN"
        btnLogin = new JButton("LOGIN");
        ThemeUtil.styleButton(btnLogin, ThemeUtil.OCEAN_BLUE); // Mempercantik tombol
        btnLogin.setMaximumSize(new Dimension(300, 45));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Merakit (Assemble) Komponen ke Card ---
        // Box.createRigidArea digunakan untuk memberikan jarak/spasi vertikal antar komponen
        loginCard.add(Box.createRigidArea(new Dimension(0, 10)));
        loginCard.add(lblLogo);
        loginCard.add(Box.createRigidArea(new Dimension(0, 10)));
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

        // Memasukkan card form ke jendela utama
        container.add(loginCard);

        // ==========================================
        // [Mekanisme Tombol & Keyboard] (EVENT LISTENERS)
        // ==========================================

        // 1. [Event] Ketika btnLogin ditekan oleh mouse/dijari, jalankan fungsi doLogin()
        btnLogin.addActionListener(e -> doLogin());
        
        // 2. [Event] Ketika di kotak Username kita tekan 'Enter', kursor otomatis pindah ke Password
        txtUsername.addActionListener(e -> txtPassword.requestFocus());
        
        // 3. [Event] Ketika di kotak Password kita tekan 'Enter', otomatis memicu klik tombol Login
        txtPassword.addActionListener(e -> btnLogin.doClick());
        
        // 4. Menjadikan tombol btnLogin sebagai tombol bawaan jendela (Bisa di-trigger kapan saja dengan Enter)
        getRootPane().setDefaultButton(btnLogin);
    }

    // [Logika Inti] Metode yang memproses data ketika tombol Login ditekan
    private void doLogin() {
        // [Logika] Mengambil tulisan dari JTextField username dan password
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        // [Logika] Validasi awal: cegah jika user belum mengisi sama sekali
        if (username.isEmpty() || password.isEmpty()) {
            Toast.showError(this, "Username dan Password tidak boleh kosong!");
            return; // Hentikan proses, jangan lanjut ke database
        }

        // [Mekanisme Database] Memanggil UserService.login()
        // Fungsi ini pergi ke database (SELECT * FROM tb_user), mencocokkan password, dan melempar hasilnya.
        // Jika cocok, mengembalikan object 'User'. Jika salah, mengembalikan nilai null.
        User user = userService.login(username, password);
        
        if (user != null) {
            // [Aksi Sukses] Menampilkan pop-up toast berhasil
            Toast.showSuccess(this, "Login Berhasil! Selamat Datang " + user.getNamaLengkap());
            
            // [Navigasi Layar] Buka form MenuUtama sambil mengirimkan data User yang sedang login
            SwingUtilities.invokeLater(() -> {
                MenuUtama menu = new MenuUtama(user);
                menu.setVisible(true);
                dispose(); // Tutup form LoginForm ini karena sudah tidak dipakai
            });
        } else {
            // [Aksi Gagal] Jika hasil dari database null (salah password / tidak ada)
            Toast.showError(this, "Username atau Password salah!");
        }
    }
}
