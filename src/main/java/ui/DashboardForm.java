package ui;

import service.DashboardService;
import ui.components.RoundedPanel;
import util.ThemeUtil;
import util.Formatter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class DashboardForm extends JPanel {

    private DashboardService service;

    private JLabel lblTotalKategori, lblTotalBarang, lblTotalCustomer, lblTotalPendapatan, lblTotalKeuntungan;
    
    private DefaultTableModel tblModelRiwayat, tblModelTopBarang, tblModelTopCustomer;
    private JComboBox<String> cbFilterPeriode;

    private Runnable onLihatSemuaClick;

    public DashboardForm(Runnable onLihatSemuaClick) {
        this.onLihatSemuaClick = onLihatSemuaClick;
        service = new DashboardService();
        setLayout(new BorderLayout(20, 20));
        setBackground(ThemeUtil.BG_SOFT);
        setBorder(new EmptyBorder(10, 20, 10, 20));
        initComponents();
        loadData();
    }

    private void initComponents() {
        // TOP: Cards
        JPanel pnlTop = new JPanel(new BorderLayout(0, 10));
        pnlTop.setOpaque(false);


        JPanel cardsPanel = new JPanel(new GridLayout(1, 5, 20, 20));
        cardsPanel.setOpaque(false);
        cardsPanel.setPreferredSize(new Dimension(0, 120));

        lblTotalPendapatan = createLabel("Rp 0", ThemeUtil.TEXT_PRIMARY);
        lblTotalKeuntungan = createLabel("Rp 0", ThemeUtil.TEXT_PRIMARY);
        lblTotalBarang = createLabel("0", ThemeUtil.TEXT_PRIMARY);
        lblTotalCustomer = createLabel("0", ThemeUtil.TEXT_PRIMARY);
        lblTotalKategori = createLabel("0", ThemeUtil.TEXT_PRIMARY);

        cardsPanel.add(createSummaryCard("Pendapatan", ThemeUtil.SKY_BLUE, lblTotalPendapatan, "circle-dollar-sign"));
        cardsPanel.add(createSummaryCard("Keuntungan", ThemeUtil.SUCCESS_COLOR, lblTotalKeuntungan, "circle-dollar-sign"));
        cardsPanel.add(createSummaryCard("Total Barang", ThemeUtil.OCEAN_BLUE, lblTotalBarang, "box"));
        cardsPanel.add(createSummaryCard("Total Customer", ThemeUtil.WARNING_COLOR, lblTotalCustomer, "users"));
        cardsPanel.add(createSummaryCard("Total Kategori", ThemeUtil.ERROR_COLOR, lblTotalKategori, "tags"));
        
        pnlTop.add(cardsPanel, BorderLayout.CENTER);
        add(pnlTop, BorderLayout.NORTH);

        // CENTER: Tables
        JPanel pnlCenter = new JPanel(new BorderLayout(0, 15));
        pnlCenter.setOpaque(false);

        // Riwayat Table
        JPanel pnlRiwayat = new JPanel(new BorderLayout(5, 5));
        pnlRiwayat.setOpaque(false);
        pnlRiwayat.setPreferredSize(new Dimension(0, 200));
        JPanel pnlRiwayatHeader = new JPanel(new BorderLayout());
        pnlRiwayatHeader.setOpaque(false);
        JLabel lblRiwayat = new JLabel("10 Riwayat Transaksi Terakhir");
        lblRiwayat.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblRiwayat.setForeground(ThemeUtil.TEXT_PRIMARY);
        pnlRiwayatHeader.add(lblRiwayat, BorderLayout.WEST);

        JButton btnLihatSemua = new JButton("Lihat semua >");
        btnLihatSemua.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLihatSemua.setForeground(ThemeUtil.OCEAN_BLUE);
        btnLihatSemua.setContentAreaFilled(false);
        btnLihatSemua.setBorderPainted(false);
        btnLihatSemua.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLihatSemua.addActionListener(e -> {
            if (onLihatSemuaClick != null) onLihatSemuaClick.run();
        });
        pnlRiwayatHeader.add(btnLihatSemua, BorderLayout.EAST);

        pnlRiwayat.add(pnlRiwayatHeader, BorderLayout.NORTH);
        
        tblModelRiwayat = new DefaultTableModel(new String[]{"Tanggal", "No Faktur", "Customer", "Total Bayar"}, 0);
        JTable tblRiwayat = new JTable(tblModelRiwayat);
        JScrollPane spRiwayat = new JScrollPane(tblRiwayat);
        ThemeUtil.styleTable(tblRiwayat, spRiwayat);
        pnlRiwayat.add(spRiwayat, BorderLayout.CENTER);

        // Top Statistics Panel
        JPanel pnlStats = new JPanel(new BorderLayout(5, 5));
        pnlStats.setOpaque(false);
        
        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlFilter.setOpaque(false);
        JLabel lblStats = new JLabel("Statistik Top 10 Berdasarkan:  ");
        lblStats.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblStats.setForeground(ThemeUtil.TEXT_PRIMARY);
        
        cbFilterPeriode = new JComboBox<>(new String[]{"Hari ini", "7 Hari Terakhir", "Sebulan Terakhir", "Semua Waktu"});
        cbFilterPeriode.setFont(ThemeUtil.FONT_REGULAR);
        cbFilterPeriode.addActionListener(e -> loadTopData());
        
        pnlFilter.add(lblStats);
        pnlFilter.add(cbFilterPeriode);
        pnlStats.add(pnlFilter, BorderLayout.NORTH);

        JPanel pnlTopTables = new JPanel(new GridLayout(1, 2, 15, 0));
        pnlTopTables.setOpaque(false);
        
        // Top Barang Table
        JPanel pnlTopBarang = new JPanel(new BorderLayout(5, 5));
        pnlTopBarang.setOpaque(false);
        JLabel lblTopBarang = new JLabel("Top Barang Terjual");
        lblTopBarang.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTopBarang.setForeground(ThemeUtil.TEXT_SECONDARY);
        pnlTopBarang.add(lblTopBarang, BorderLayout.NORTH);
        
        tblModelTopBarang = new DefaultTableModel(new String[]{"Nama Barang", "Total Terjual"}, 0);
        JTable tblTopBarang = new JTable(tblModelTopBarang);
        JScrollPane spTopBarang = new JScrollPane(tblTopBarang);
        ThemeUtil.styleTable(tblTopBarang, spTopBarang);
        pnlTopBarang.add(spTopBarang, BorderLayout.CENTER);

        // Top Customer Table
        JPanel pnlTopCustomer = new JPanel(new BorderLayout(5, 5));
        pnlTopCustomer.setOpaque(false);
        JLabel lblTopCustomer = new JLabel("Top Customer Sering Beli");
        lblTopCustomer.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTopCustomer.setForeground(ThemeUtil.TEXT_SECONDARY);
        pnlTopCustomer.add(lblTopCustomer, BorderLayout.NORTH);
        
        tblModelTopCustomer = new DefaultTableModel(new String[]{"Nama Customer", "Frekuensi Beli"}, 0);
        JTable tblTopCustomer = new JTable(tblModelTopCustomer);
        JScrollPane spTopCustomer = new JScrollPane(tblTopCustomer);
        ThemeUtil.styleTable(tblTopCustomer, spTopCustomer);
        pnlTopCustomer.add(spTopCustomer, BorderLayout.CENTER);

        pnlTopTables.add(pnlTopBarang);
        pnlTopTables.add(pnlTopCustomer);
        pnlStats.add(pnlTopTables, BorderLayout.CENTER);

        pnlCenter.add(pnlRiwayat, BorderLayout.NORTH);
        pnlCenter.add(pnlStats, BorderLayout.CENTER);

        add(pnlCenter, BorderLayout.CENTER);
    }

    private JLabel createLabel(String text, Color color) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lbl.setForeground(color);
        return lbl;
    }

    private RoundedPanel createSummaryCard(String title, Color accentColor, JLabel valueLabel, String iconName) {
        RoundedPanel card = ThemeUtil.createCardPanel();
        card.setLayout(new BorderLayout(10, 10));
        
        JLabel lblHeader = new JLabel(title, SwingConstants.LEFT);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblHeader.setForeground(ThemeUtil.TEXT_SECONDARY);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(lblHeader, BorderLayout.CENTER);
        
        if (iconName != null) {
            try {
                com.formdev.flatlaf.extras.FlatSVGIcon icon = new com.formdev.flatlaf.extras.FlatSVGIcon("icons/" + iconName + ".svg", 24, 24);
                icon.setColorFilter(new com.formdev.flatlaf.extras.FlatSVGIcon.ColorFilter(color -> accentColor));
                JLabel lblIcon = new JLabel(icon);
                topPanel.add(lblIcon, BorderLayout.EAST);
            } catch (Exception e) {}
        }
        
        valueLabel.setHorizontalAlignment(SwingConstants.LEFT);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 26)); // Slightly smaller for better fit
        valueLabel.setForeground(ThemeUtil.TEXT_PRIMARY); 
        
        card.add(topPanel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 4, 0, accentColor),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        return card;
    }

    public void loadData() {
        int kategori = service.getTotalKategori();
        int barang = service.getTotalBarang();
        int customer = service.getTotalCustomer();
        double pendapatan = service.getTotalPendapatan();
        double keuntungan = service.getTotalKeuntungan();

        lblTotalKategori.setText(String.valueOf(kategori));
        lblTotalBarang.setText(String.valueOf(barang));
        lblTotalCustomer.setText(String.valueOf(customer));
        lblTotalPendapatan.setText(Formatter.formatRupiah(pendapatan));
        lblTotalKeuntungan.setText(Formatter.formatRupiah(keuntungan));

        loadRiwayatData();
        loadTopData();
    }

    private void loadRiwayatData() {
        tblModelRiwayat.setRowCount(0);
        List<Map<String, Object>> recent = service.getRecentTransactions(10);
        for (Map<String, Object> row : recent) {
            tblModelRiwayat.addRow(new Object[]{
                row.get("tanggal"),
                row.get("faktur"),
                row.get("customer"),
                Formatter.formatRupiah((Double) row.get("total"))
            });
        }
    }

    private void loadTopData() {
        String filter = (String) cbFilterPeriode.getSelectedItem();
        
        // Load Top Barang
        tblModelTopBarang.setRowCount(0);
        List<Map<String, Object>> topBarang = service.getTopSellingProducts(filter);
        for (Map<String, Object> row : topBarang) {
            tblModelTopBarang.addRow(new Object[]{
                row.get("barang"),
                row.get("terjual")
            });
        }

        // Load Top Customer
        tblModelTopCustomer.setRowCount(0);
        List<Map<String, Object>> topCustomer = service.getTopCustomers(filter);
        for (Map<String, Object> row : topCustomer) {
            tblModelTopCustomer.addRow(new Object[]{
                row.get("customer"),
                row.get("freq") + " kali"
            });
        }
    }
}
