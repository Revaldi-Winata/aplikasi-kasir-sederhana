package ui;

import model.User;
import ui.components.Toast;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import util.ThemeUtil;
import ui.components.RoundedPanel;
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
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(ThemeUtil.BG_WHITE);
        topBar.setPreferredSize(new Dimension(getWidth(), 70));
        topBar.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 1, 0, ThemeUtil.BORDER_COLOR),
            new EmptyBorder(10, 25, 10, 25)
        ));

        JLabel lblTitle = new JLabel("Toko Berkah Jaya");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(ThemeUtil.OCEAN_BLUE);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 5));
        userPanel.setOpaque(false);
        
        lblUserInfo = new JLabel("Halo, " + loggedInUser.getNamaLengkap() + " (" + loggedInUser.getLevel() + ")");
        lblUserInfo.setForeground(ThemeUtil.TEXT_SECONDARY);
        lblUserInfo.setFont(ThemeUtil.FONT_REGULAR);

        JButton btnLogout = new JButton("Logout");
        ThemeUtil.styleButton(btnLogout, ThemeUtil.ERROR_COLOR);
        btnLogout.addActionListener(e -> {
            new LoginForm().setVisible(true);
            dispose();
        });

        userPanel.add(lblUserInfo);
        userPanel.add(btnLogout);

        topBar.add(lblTitle, BorderLayout.WEST);
        topBar.add(userPanel, BorderLayout.EAST);


        // ==================== SIDEBAR (WEST) ====================
        JPanel sideBar = new JPanel();
        sideBar.setLayout(new BoxLayout(sideBar, BoxLayout.Y_AXIS));
        sideBar.setBackground(ThemeUtil.OCEAN_BLUE); 
        sideBar.setPreferredSize(new Dimension(260, getHeight()));

        sideBar.add(Box.createRigidArea(new Dimension(0, 30)));

        JLabel lblMenuMaster = new JLabel("   MAIN MENU");
        lblMenuMaster.setForeground(new Color(207, 250, 254)); // Cyan-100
        lblMenuMaster.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblMenuMaster.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblMenuMaster.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JButton btnKategori = createSidebarButton("Kelola Kategori");
        JButton btnBarang = createSidebarButton("Kelola Barang");
        JButton btnCustomer = createSidebarButton("Kelola Customer");

        JLabel lblMenuTransaksi = new JLabel("   TRANSAKSI");
        lblMenuTransaksi.setForeground(new Color(207, 250, 254));
        lblMenuTransaksi.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblMenuTransaksi.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblMenuTransaksi.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JButton btnPenjualan = createSidebarButton("Kasir / Penjualan");

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
        lblMenuPengaturan.setForeground(new Color(207, 250, 254));
        lblMenuPengaturan.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblMenuPengaturan.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblMenuPengaturan.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JButton btnKelolaUser = createSidebarButton("Kelola User");
        JButton btnProfil = createSidebarButton("Profil Saya");
        
        sideBar.add(Box.createRigidArea(new Dimension(0, 30)));
        sideBar.add(lblMenuPengaturan);
        sideBar.add(Box.createRigidArea(new Dimension(0, 10)));
        sideBar.add(btnKelolaUser);
        sideBar.add(btnProfil);


        // ==================== MAIN CONTENT (CENTER / CARDLAYOUT) ====================
        cardLayout = new CardLayout();
        mainContent = new JPanel(cardLayout);
        mainContent.setBackground(ThemeUtil.BG_SOFT); 
        
        welcomePanel = new JPanel(new GridBagLayout());
        welcomePanel.setBackground(ThemeUtil.BG_SOFT);
        
        RoundedPanel welcomeBox = ThemeUtil.createCardPanel(); // Rounded card
        welcomeBox.setLayout(new GridLayout(2, 1, 0, 10));
        
        JLabel lblWelcomeTitle = new JLabel("Selamat Datang di Toko Berkah Jaya", SwingConstants.CENTER);
        lblWelcomeTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblWelcomeTitle.setForeground(ThemeUtil.OCEAN_BLUE);
        
        JLabel lblWelcomeSub = new JLabel("Silakan pilih menu di samping kiri untuk memulai aktivitas Anda.", SwingConstants.CENTER);
        lblWelcomeSub.setFont(ThemeUtil.FONT_REGULAR);
        lblWelcomeSub.setForeground(ThemeUtil.TEXT_SECONDARY);
        
        welcomeBox.add(lblWelcomeTitle);
        welcomeBox.add(lblWelcomeSub);
        welcomePanel.add(welcomeBox);

        kategoriForm = new KategoriForm();
        barangForm = new BarangForm();
        customerForm = new CustomerForm();
        penjualanForm = new PenjualanForm(loggedInUser);
        userForm = new UserForm();
        profilForm = new ProfilForm(loggedInUser, () -> {
            lblUserInfo.setText("Halo, " + loggedInUser.getNamaLengkap() + " (" + loggedInUser.getLevel() + ")");
        });

        mainContent.add(welcomePanel, "WELCOME");
        mainContent.add(kategoriForm, "KATEGORI");
        mainContent.add(barangForm, "BARANG");
        mainContent.add(customerForm, "CUSTOMER");
        mainContent.add(penjualanForm, "PENJUALAN");
        mainContent.add(userForm, "USER");
        mainContent.add(profilForm, "PROFIL");

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

        if (!"Admin".equalsIgnoreCase(loggedInUser.getLevel())) {
            btnKategori.setVisible(false);
            btnBarang.setVisible(false);
            btnKelolaUser.setVisible(false);
        }

        // ==================== ASSEMBLE ====================
        mainContainer.add(topBar, BorderLayout.NORTH);
        mainContainer.add(sideBar, BorderLayout.WEST);
        mainContainer.add(mainContent, BorderLayout.CENTER);
    }

    private JButton createSidebarButton(String text) {
        JButton btn = new JButton("   " + text);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45)); 
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setBackground(ThemeUtil.OCEAN_BLUE); 
        btn.setForeground(Color.WHITE); 
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0)); 
        btn.setFont(ThemeUtil.FONT_REGULAR);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        sidebarButtons.add(btn);
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                if(btn.isEnabled() && !btn.getName().equals("ACTIVE")) {
                    btn.setBackground(ThemeUtil.OCEAN_BLUE_DARK);
                }
            }
            public void mouseExited(MouseEvent evt) {
                if(btn.isEnabled() && !btn.getName().equals("ACTIVE")) {
                    btn.setBackground(ThemeUtil.OCEAN_BLUE); 
                }
            }
        });
        
        btn.setName("");
        return btn;
    }
    
    private void setActiveButton(JButton activeBtn) {
        for(JButton btn : sidebarButtons) {
            btn.setName(""); 
            if(btn.isEnabled()) {
                btn.setBackground(ThemeUtil.OCEAN_BLUE); 
                btn.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
            }
        }
        activeBtn.setName("ACTIVE");
        activeBtn.setBackground(ThemeUtil.OCEAN_BLUE_DARK); 
        activeBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 4, 0, 0, ThemeUtil.SKY_BLUE),
            BorderFactory.createEmptyBorder(0, 16, 0, 0)
        ));
    }
}
