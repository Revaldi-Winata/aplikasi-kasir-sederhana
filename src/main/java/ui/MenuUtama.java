package ui;

import model.User;
import ui.components.Toast;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import util.ThemeUtil;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class MenuUtama extends JFrame {

    private User loggedInUser;
    private JPanel mainContent;
    private CardLayout cardLayout;
    
    // Panels
    private DashboardForm dashboardForm;
    private KategoriForm kategoriForm;
    private BarangForm barangForm;
    private CustomerForm customerForm;
    private PenjualanForm penjualanForm;
    private LogTransaksiForm logTransaksiForm;
    private ProfilForm profilForm;
    private UserForm userForm;
    
    private JLabel lblTopTitle;
    private JButton btnUserDropdown;
    private java.util.List<JButton> sidebarButtons = new ArrayList<>();

    public MenuUtama(User user) {
        this.loggedInUser = user;

        setTitle("Toko Berkah Jaya - Dashboard");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        ImageIcon logoIcon = ThemeUtil.getScaledImage("/images/logo.png", 64, 64);
        if (logoIcon != null) {
            setIconImage(logoIcon.getImage());
        }

        initComponents();
    }

    private void initComponents() {
        Container mainContainer = getContentPane();
        mainContainer.setLayout(new BorderLayout());

        // ==================== SIDEBAR (WEST) ====================
        JPanel sideBar = new JPanel();
        sideBar.setLayout(new BoxLayout(sideBar, BoxLayout.Y_AXIS));
        sideBar.setBackground(ThemeUtil.OCEAN_BLUE); 
        sideBar.setPreferredSize(new Dimension(260, getHeight()));

        // --- Wrapper for Logo & Title ---
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        logoPanel.setOpaque(false);
        logoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        logoPanel.setPreferredSize(new Dimension(260, 70));
        logoPanel.setBorder(new MatteBorder(0, 0, 1, 0, new Color(255, 255, 255, 60)));
        
        JLabel lblSidebarLogo = new JLabel();
        ImageIcon logoIconSmall = ThemeUtil.getScaledImage("/images/logo.png", 40, 40);
        if (logoIconSmall != null) {
            lblSidebarLogo.setIcon(logoIconSmall);
        }

        JLabel lblSidebarTitle = new JLabel("Toko Berkah Jaya");
        lblSidebarTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblSidebarTitle.setForeground(Color.WHITE);

        logoPanel.add(lblSidebarLogo);
        logoPanel.add(lblSidebarTitle);

        sideBar.add(logoPanel);
        sideBar.add(Box.createRigidArea(new Dimension(0, 10)));

        // --- Sidebar Menus ---
        JLabel lblMenuMaster = createMenuHeader("MAIN MENU");
        JButton btnDashboard = createSidebarButton("Dashboard");
        JButton btnKategori = createSidebarButton("Kelola Kategori");
        JButton btnBarang = createSidebarButton("Kelola Barang");
        JButton btnCustomer = createSidebarButton("Kelola Customer");

        JLabel lblMenuTransaksi = createMenuHeader("TRANSAKSI");
        JButton btnPenjualan = createSidebarButton("Kasir / Penjualan");
        JButton btnLogTransaksi = createSidebarButton("Log Transaksi");

        sideBar.add(btnDashboard);
        sideBar.add(Box.createRigidArea(new Dimension(0, 20)));
        sideBar.add(lblMenuMaster);
        sideBar.add(Box.createRigidArea(new Dimension(0, 10)));
        sideBar.add(btnKategori);
        sideBar.add(btnBarang);
        sideBar.add(btnCustomer);
        
        sideBar.add(Box.createRigidArea(new Dimension(0, 30)));
        
        sideBar.add(lblMenuTransaksi);
        sideBar.add(Box.createRigidArea(new Dimension(0, 10)));
        sideBar.add(btnPenjualan);
        sideBar.add(btnLogTransaksi);
        
        JLabel lblMenuPengaturan = createMenuHeader("PENGATURAN");
        JButton btnKelolaUser = createSidebarButton("Kelola User");
        
        sideBar.add(Box.createRigidArea(new Dimension(0, 30)));
        sideBar.add(lblMenuPengaturan);
        sideBar.add(Box.createRigidArea(new Dimension(0, 10)));
        sideBar.add(btnKelolaUser);

        // ==================== CONTENT WRAPPER (CENTER) ====================
        JPanel contentWrapper = new JPanel(new BorderLayout());

        // ==================== TOPBAR (NORTH of wrapper) ====================
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(ThemeUtil.BG_WHITE);
        topBar.setPreferredSize(new Dimension(getWidth(), 70));
        topBar.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 1, 0, ThemeUtil.BORDER_COLOR),
            new EmptyBorder(10, 25, 10, 25)
        ));

        // Topbar Title (Left)
        lblTopTitle = new JLabel("Ringkasan Dashboard");
        lblTopTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTopTitle.setForeground(ThemeUtil.OCEAN_BLUE);
        topBar.add(lblTopTitle, BorderLayout.WEST);

        // Topbar User Dropdown (Right)
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        userPanel.setOpaque(false);
        
        btnUserDropdown = new JButton("Halo, " + loggedInUser.getNamaLengkap() + " (" + loggedInUser.getLevel() + ") \u25BC");
        btnUserDropdown.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnUserDropdown.setForeground(ThemeUtil.TEXT_PRIMARY);
        btnUserDropdown.setContentAreaFilled(false);
        btnUserDropdown.setBorderPainted(false);
        btnUserDropdown.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPopupMenu userMenu = new JPopupMenu();
        userMenu.setBackground(Color.WHITE);
        userMenu.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeUtil.BORDER_COLOR, 1),
            new EmptyBorder(5, 0, 5, 0)
        ));
        
        JMenuItem itemProfil = createDropdownItem("Profil Saya", ThemeUtil.TEXT_PRIMARY);
        itemProfil.addActionListener(e -> { 
            setActiveButton(null); 
            lblTopTitle.setText("Profil Pengguna");
            cardLayout.show(mainContent, "PROFIL"); 
        });

        JMenuItem itemTentang = createDropdownItem("Tentang Aplikasi", ThemeUtil.TEXT_PRIMARY);
        itemTentang.addActionListener(e -> showAboutDialog());

        JMenuItem itemLogout = createDropdownItem("Logout", ThemeUtil.ERROR_COLOR);
        itemLogout.addActionListener(e -> { 
            new LoginForm().setVisible(true); 
            dispose(); 
        });

        userMenu.add(itemProfil);
        userMenu.add(itemTentang);
        
        JSeparator sep = new JSeparator();
        sep.setForeground(ThemeUtil.BORDER_COLOR);
        userMenu.add(sep);
        
        userMenu.add(itemLogout);

        btnUserDropdown.addActionListener(e -> {
            // Position popup precisely below the text/arrow, ignoring the button's right margin
            int rightPadding = btnUserDropdown.getInsets().right;
            int x = btnUserDropdown.getWidth() - userMenu.getPreferredSize().width - rightPadding;
            int y = btnUserDropdown.getHeight() + 5;
            userMenu.show(btnUserDropdown, x, y);
        });

        userPanel.add(btnUserDropdown);
        topBar.add(userPanel, BorderLayout.EAST);
        contentWrapper.add(topBar, BorderLayout.NORTH);

        // ==================== MAIN CONTENT (CENTER of wrapper) ====================
        cardLayout = new CardLayout();
        mainContent = new JPanel(cardLayout) {
            @Override
            public Dimension getPreferredSize() {
                Dimension pref = super.getPreferredSize();
                Container parent = getParent();
                if (parent instanceof JViewport) {
                    Dimension viewSize = ((JViewport) parent).getSize();
                    pref.width = Math.max(pref.width, viewSize.width);
                    pref.height = Math.max(pref.height, viewSize.height);
                }
                return pref;
            }
        };
        mainContent.setBackground(ThemeUtil.BG_SOFT); 
        
        dashboardForm = new DashboardForm(() -> btnLogTransaksi.doClick());
        kategoriForm = new KategoriForm();
        barangForm = new BarangForm();
        customerForm = new CustomerForm();
        penjualanForm = new PenjualanForm(loggedInUser);
        logTransaksiForm = new LogTransaksiForm();
        userForm = new UserForm();
        profilForm = new ProfilForm(loggedInUser, () -> {
            btnUserDropdown.setText("Halo, " + loggedInUser.getNamaLengkap() + " (" + loggedInUser.getLevel() + ") \u25BC");
        });

        mainContent.add(dashboardForm, "DASHBOARD");
        mainContent.add(kategoriForm, "KATEGORI");
        mainContent.add(barangForm, "BARANG");
        mainContent.add(customerForm, "CUSTOMER");
        mainContent.add(penjualanForm, "PENJUALAN");
        mainContent.add(logTransaksiForm, "LOG");
        mainContent.add(userForm, "USER");
        mainContent.add(profilForm, "PROFIL");

        JScrollPane mainScroll = new JScrollPane(mainContent);
        mainScroll.setBorder(BorderFactory.createEmptyBorder());
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);
        contentWrapper.add(mainScroll, BorderLayout.CENTER);

        cardLayout.show(mainContent, "DASHBOARD");
        setActiveButton(btnDashboard);

        // ==================== ACTIONS / ROUTING ====================
        btnDashboard.addActionListener(e -> { 
            setActiveButton(btnDashboard); 
            lblTopTitle.setText("Ringkasan Dashboard");
            dashboardForm.loadData(); 
            cardLayout.show(mainContent, "DASHBOARD"); 
        });
        btnKategori.addActionListener(e -> { 
            setActiveButton(btnKategori); 
            lblTopTitle.setText("Manajemen Kategori");
            cardLayout.show(mainContent, "KATEGORI"); 
        });
        btnBarang.addActionListener(e -> {
            setActiveButton(btnBarang);
            lblTopTitle.setText("Daftar Barang");
            barangForm.loadComboKategori();
            barangForm.loadData();
            cardLayout.show(mainContent, "BARANG");
        });
        btnCustomer.addActionListener(e -> { 
            setActiveButton(btnCustomer); 
            lblTopTitle.setText("Daftar Customer");
            cardLayout.show(mainContent, "CUSTOMER"); 
        });
        btnPenjualan.addActionListener(e -> {
            setActiveButton(btnPenjualan);
            lblTopTitle.setText("Formulir Penjualan");
            penjualanForm.loadCombo();
            penjualanForm.loadData();
            cardLayout.show(mainContent, "PENJUALAN");
        });
        btnLogTransaksi.addActionListener(e -> {
            setActiveButton(btnLogTransaksi);
            lblTopTitle.setText("Log Transaksi");
            logTransaksiForm.loadData();
            cardLayout.show(mainContent, "LOG");
        });
        btnKelolaUser.addActionListener(e -> { 
            setActiveButton(btnKelolaUser); 
            lblTopTitle.setText("Manajemen User");
            cardLayout.show(mainContent, "USER"); 
        });

        if (!"Admin".equalsIgnoreCase(loggedInUser.getLevel())) {
            btnKategori.setVisible(false);
            btnBarang.setVisible(false);
            btnKelolaUser.setVisible(false);
        }

        // ==================== ASSEMBLE ====================
        mainContainer.add(sideBar, BorderLayout.WEST);
        mainContainer.add(contentWrapper, BorderLayout.CENTER);
    }

    private JLabel createMenuHeader(String text) {
        JLabel lbl = new JLabel("   " + text);
        lbl.setForeground(new Color(207, 250, 254)); // Cyan-100
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        return lbl;
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

    private JMenuItem createDropdownItem(String text, Color fgColor) {
        JMenuItem item = new JMenuItem(text);
        item.setFont(new Font("Segoe UI", Font.BOLD, 14));
        item.setForeground(fgColor);
        item.setBackground(Color.WHITE);
        item.setOpaque(true);
        item.setBorder(new EmptyBorder(8, 20, 8, 40));
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return item;
    }
    
    private void setActiveButton(JButton activeBtn) {
        for(JButton btn : sidebarButtons) {
            btn.setName(""); 
            if(btn.isEnabled()) {
                btn.setBackground(ThemeUtil.OCEAN_BLUE); 
                btn.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
            }
        }
        if (activeBtn != null) {
            activeBtn.setName("ACTIVE");
            activeBtn.setBackground(ThemeUtil.OCEAN_BLUE_DARK); 
            activeBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, ThemeUtil.SKY_BLUE),
                BorderFactory.createEmptyBorder(0, 16, 0, 0)
            ));
        }
    }

    private void showAboutDialog() {
        JDialog dialog = new JDialog(this, "Tentang Aplikasi", true);
        dialog.setSize(350, 330);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(Color.WHITE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblLogo = new JLabel();
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        ImageIcon logoIcon = ThemeUtil.getScaledImage("/images/logo.png", 80, 80);
        if (logoIcon != null) {
            lblLogo.setIcon(logoIcon);
        }

        JLabel lblTitle = new JLabel("Toko Berkah Jaya");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setForeground(ThemeUtil.OCEAN_BLUE);

        JLabel lblVersion = new JLabel("Versi 1.0.0");
        lblVersion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblVersion.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblVersion.setForeground(ThemeUtil.TEXT_SECONDARY);

        JLabel lblDeveloper = new JLabel("Dikembangkan oleh: Revaldi Winata");
        lblDeveloper.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDeveloper.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblDeveloper.setForeground(ThemeUtil.TEXT_PRIMARY);

        JLabel lblUniv = new JLabel("Universitas Pamulang");
        lblUniv.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblUniv.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblUniv.setForeground(ThemeUtil.TEXT_SECONDARY);

        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(lblLogo);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(lblTitle);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 0)));
        mainPanel.add(lblVersion);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(lblDeveloper);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 0)));
        mainPanel.add(lblUniv);
        mainPanel.add(Box.createVerticalGlue());

        JButton btnClose = new JButton("Tutup");
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnClose.setBackground(ThemeUtil.OCEAN_BLUE);
        btnClose.setForeground(Color.WHITE);
        btnClose.setFocusPainted(false);
        btnClose.setBorderPainted(false);
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClose.addActionListener(e -> dialog.dispose());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        bottomPanel.add(btnClose);

        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.add(bottomPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}
