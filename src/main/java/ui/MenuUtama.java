package ui;

import model.User;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import util.ThemeUtil;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class MenuUtama extends JFrame {

    private User loggedInUser;
    private JPanel mainContent;
    private CardLayout cardLayout;
    
    // Panels
    private JPanel welcomePanel;
    private KategoriForm kategoriForm;
    private BarangForm barangForm;
    private CustomerForm customerForm;
    private PenjualanForm penjualanForm;
    private ProfilForm profilForm;
    private UserForm userForm;
    private JLabel lblUserInfo;
    
    // Untuk tracking tombol aktif
    private java.util.List<JButton> sidebarButtons = new ArrayList<>();

    public MenuUtama(User user) {
        this.loggedInUser = user;

        setTitle("Toko Berkah Jaya - Dashboard");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
    }

    private void initComponents() {
        Container mainContainer = getContentPane();
        mainContainer.setLayout(new BorderLayout());

        // ==================== TOPBAR (NORTH) ====================
        // Desain Topbar putih bersih ala Modern SaaS
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setPreferredSize(new Dimension(getWidth(), 70));
        topBar.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 1, 0, new Color(226, 232, 240)), // Subtle bottom border
            new EmptyBorder(10, 25, 10, 25)
        ));

        // Judul Aplikasi (Tanpa Emoji)
        JLabel lblTitle = new JLabel("Toko Berkah Jaya");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26)); // Dibuat lebih besar
        lblTitle.setForeground(ThemeUtil.TEXT_PRIMARY); // Slate-900

        // Area Kanan (User Info & Logout)
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 5));
        userPanel.setOpaque(false);
        
        lblUserInfo = new JLabel("Halo, " + loggedInUser.getNamaLengkap() + " (" + loggedInUser.getLevel() + ")");
        lblUserInfo.setForeground(new Color(71, 85, 105));
        lblUserInfo.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        JButton btnLogout = new JButton("Logout");
        ThemeUtil.styleButton(btnLogout, new Color(239, 68, 68)); // Red-500
        btnLogout.addActionListener(e -> {
            new LoginForm().setVisible(true);
            dispose();
        });

        userPanel.add(lblUserInfo);
        userPanel.add(btnLogout);

        topBar.add(lblTitle, BorderLayout.WEST);
        topBar.add(userPanel, BorderLayout.EAST);


        // ==================== SIDEBAR (WEST) ====================
        // Desain Sidebar Gelap (Slate-900) ala Modern Admin Dashboard
        JPanel sideBar = new JPanel();
        sideBar.setLayout(new BoxLayout(sideBar, BoxLayout.Y_AXIS));
        sideBar.setBackground(new Color(15, 23, 42)); 
        sideBar.setPreferredSize(new Dimension(260, getHeight()));
        sideBar.setBorder(new MatteBorder(0, 0, 0, 1, new Color(30, 41, 59)));

        // Spacing atas sidebar
        sideBar.add(Box.createRigidArea(new Dimension(0, 30)));

        JLabel lblMenuMaster = new JLabel("   MAIN MENU");
        lblMenuMaster.setForeground(new Color(148, 163, 184)); // Slate-400
        lblMenuMaster.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblMenuMaster.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblMenuMaster.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JButton btnKategori = createSidebarButton("Kelola Kategori", "KATEGORI");
        JButton btnBarang = createSidebarButton("Kelola Barang", "BARANG");
        JButton btnCustomer = createSidebarButton("Kelola Customer", "CUSTOMER");

        JLabel lblMenuTransaksi = new JLabel("   TRANSAKSI");
        lblMenuTransaksi.setForeground(new Color(148, 163, 184));
        lblMenuTransaksi.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblMenuTransaksi.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblMenuTransaksi.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JButton btnPenjualan = createSidebarButton("Kasir / Penjualan", "PENJUALAN");

        // Add components to Sidebar
        sideBar.add(lblMenuMaster);
        sideBar.add(Box.createRigidArea(new Dimension(0, 10)));
        sideBar.add(btnKategori);
        sideBar.add(btnBarang);
        sideBar.add(btnCustomer);
        
        sideBar.add(Box.createRigidArea(new Dimension(0, 30)));
        
        sideBar.add(lblMenuTransaksi);
        sideBar.add(Box.createRigidArea(new Dimension(0, 10)));
        sideBar.add(btnPenjualan);
        
        JLabel lblMenuPengaturan = new JLabel("   PENGATURAN");
        lblMenuPengaturan.setForeground(new Color(148, 163, 184));
        lblMenuPengaturan.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblMenuPengaturan.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblMenuPengaturan.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JButton btnKelolaUser = createSidebarButton("Kelola User", "USER");
        JButton btnProfil = createSidebarButton("Profil Saya", "PROFIL");
        
        sideBar.add(Box.createRigidArea(new Dimension(0, 30)));
        sideBar.add(lblMenuPengaturan);
        sideBar.add(Box.createRigidArea(new Dimension(0, 10)));
        sideBar.add(btnKelolaUser);
        sideBar.add(btnProfil);


        // ==================== MAIN CONTENT (CENTER / CARDLAYOUT) ====================
        cardLayout = new CardLayout();
        mainContent = new JPanel(cardLayout);
        mainContent.setBackground(new Color(248, 250, 252)); // Slate-50 background (sangat soft gray)
        
        // 1. Welcome Panel
        welcomePanel = new JPanel(new GridBagLayout());
        welcomePanel.setBackground(new Color(248, 250, 252));
        
        JPanel welcomeBox = new JPanel(new GridLayout(2, 1, 0, 10));
        welcomeBox.setOpaque(false);
        JLabel lblWelcomeTitle = new JLabel("Selamat Datang di Toko Berkah Jaya", SwingConstants.CENTER);
        lblWelcomeTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblWelcomeTitle.setForeground(new Color(15, 23, 42));
        
        JLabel lblWelcomeSub = new JLabel("Silakan pilih menu di samping kiri untuk memulai aktivitas Anda.", SwingConstants.CENTER);
        lblWelcomeSub.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblWelcomeSub.setForeground(new Color(100, 116, 139));
        
        welcomeBox.add(lblWelcomeTitle);
        welcomeBox.add(lblWelcomeSub);
        welcomePanel.add(welcomeBox);

        // 2. Instantiate Forms
        kategoriForm = new KategoriForm();
        barangForm = new BarangForm();
        customerForm = new CustomerForm();
        penjualanForm = new PenjualanForm(loggedInUser);
        userForm = new UserForm();
        profilForm = new ProfilForm(loggedInUser, () -> {
            lblUserInfo.setText("Halo, " + loggedInUser.getNamaLengkap() + " (" + loggedInUser.getLevel() + ")");
        });

        // 3. Add to CardLayout
        mainContent.add(welcomePanel, "WELCOME");
        mainContent.add(kategoriForm, "KATEGORI");
        mainContent.add(barangForm, "BARANG");
        mainContent.add(customerForm, "CUSTOMER");
        mainContent.add(penjualanForm, "PENJUALAN");
        mainContent.add(userForm, "USER");
        mainContent.add(profilForm, "PROFIL");

        // Tampilkan welcome secara default
        cardLayout.show(mainContent, "WELCOME");

        // ==================== ACTIONS / ROUTING ====================
        btnKategori.addActionListener(e -> { setActiveButton(btnKategori); cardLayout.show(mainContent, "KATEGORI"); });
        btnBarang.addActionListener(e -> {
            setActiveButton(btnBarang);
            barangForm.loadComboKategori();
            barangForm.loadData();
            cardLayout.show(mainContent, "BARANG");
        });
        btnCustomer.addActionListener(e -> { setActiveButton(btnCustomer); cardLayout.show(mainContent, "CUSTOMER"); });
        btnPenjualan.addActionListener(e -> {
            setActiveButton(btnPenjualan);
            penjualanForm.loadCombo();
            penjualanForm.loadData();
            cardLayout.show(mainContent, "PENJUALAN");
        });
        btnKelolaUser.addActionListener(e -> { setActiveButton(btnKelolaUser); cardLayout.show(mainContent, "USER"); });
        btnProfil.addActionListener(e -> { setActiveButton(btnProfil); cardLayout.show(mainContent, "PROFIL"); });

        // Role-based Access Control (Fail-safe: Hide if NOT Admin)
        if (!"Admin".equalsIgnoreCase(loggedInUser.getLevel())) {
            // Sembunyikan menu sepenuhnya
            btnKategori.setVisible(false);
            btnBarang.setVisible(false);
            btnKelolaUser.setVisible(false);
            sideBar.remove(btnKategori);
            sideBar.remove(btnBarang);
            sideBar.remove(btnKelolaUser);
        }

        // ==================== ASSEMBLE ====================
        mainContainer.add(topBar, BorderLayout.NORTH);
        mainContainer.add(sideBar, BorderLayout.WEST);
        mainContainer.add(mainContent, BorderLayout.CENTER);
    }

    private JButton createSidebarButton(String text, String cardName) {
        JButton btn = new JButton("   " + text);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45)); // Supaya melar memenuhi lebar sidebar
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setBackground(new Color(15, 23, 42)); // Slate-900
        btn.setForeground(new Color(241, 245, 249)); // Slate-100
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0)); // Padding kiri ditingkatkan
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Simpan button untuk tracking
        sidebarButtons.add(btn);
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                if(btn.isEnabled() && !btn.getName().equals("ACTIVE")) {
                    btn.setBackground(new Color(30, 41, 59)); // Slate-800 on hover
                }
            }
            public void mouseExited(MouseEvent evt) {
                if(btn.isEnabled() && !btn.getName().equals("ACTIVE")) {
                    btn.setBackground(new Color(15, 23, 42)); // Revert
                }
            }
        });
        
        // Default penanda
        btn.setName("");
        return btn;
    }
    
    // Method untuk mengatur highlight menu yang sedang aktif
    private void setActiveButton(JButton activeBtn) {
        for(JButton btn : sidebarButtons) {
            btn.setName(""); // Clear status
            if(btn.isEnabled()) {
                btn.setBackground(new Color(15, 23, 42)); // Reset background
                btn.setForeground(new Color(241, 245, 249)); // Reset text
            }
        }
        activeBtn.setName("ACTIVE");
        activeBtn.setBackground(new Color(56, 189, 248).brighter().darker()); // Highlight warna biru soft
        activeBtn.setForeground(Color.WHITE);
    }
}
