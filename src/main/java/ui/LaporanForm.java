package ui;

import service.LaporanService;
import service.KategoriService;
import service.CustomerService;
import ui.components.RoundedPanel;
import util.ThemeUtil;
import util.Formatter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class LaporanForm extends JPanel {

    private LaporanService laporanService;
    private KategoriService kategoriService;
    private CustomerService customerService;

    private DefaultTableModel tblModel;
    private JTable tblLaporan;

    private JTextField txtTglMulai, txtTglAkhir;
    private JComboBox<String> cbKategori, cbCustomer;
    private JLabel lblTotalPendapatan, lblTotalTransaksi;

    public LaporanForm() {
        laporanService = new LaporanService();
        kategoriService = new KategoriService();
        customerService = new CustomerService();

        setLayout(new BorderLayout(20, 20));
        setBackground(ThemeUtil.BG_SOFT);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        initComponents();
        loadDropdowns();
        loadData();
    }

    private void initComponents() {
        // --- HEADER & FILTER PANEL ---
        RoundedPanel pnlHeader = ThemeUtil.createCardPanel();
        pnlHeader.setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = new JLabel("Laporan Penjualan");
        lblTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 22));
        lblTitle.setForeground(ThemeUtil.TEXT_PRIMARY);
        pnlHeader.add(lblTitle, BorderLayout.NORTH);

        JPanel pnlFilter = new JPanel(new java.awt.GridLayout(2, 1, 0, 10));
        pnlFilter.setOpaque(false);

        // Baris 1: Tanggal
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        pnlTop.setOpaque(false);
        pnlTop.add(new JLabel("Mulai (YYYY-MM-DD):"));
        txtTglMulai = new JTextField(10);
        ThemeUtil.styleTextField(txtTglMulai);
        pnlTop.add(txtTglMulai);
        pnlTop.add(new JLabel("Akhir (YYYY-MM-DD):"));
        txtTglAkhir = new JTextField(10);
        ThemeUtil.styleTextField(txtTglAkhir);
        pnlTop.add(txtTglAkhir);
        pnlFilter.add(pnlTop);

        // Baris 2: Dropdown & Button
        JPanel pnlBot = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        pnlBot.setOpaque(false);
        pnlBot.add(new JLabel("Kategori:"));
        cbKategori = new JComboBox<>();
        cbKategori.setPreferredSize(new java.awt.Dimension(180, 30));
        pnlBot.add(cbKategori);
        
        pnlBot.add(new JLabel("Customer:"));
        cbCustomer = new JComboBox<>();
        cbCustomer.setPreferredSize(new java.awt.Dimension(180, 30));
        pnlBot.add(cbCustomer);

        JButton btnFilter = new JButton("Terapkan Filter");
        ThemeUtil.styleButton(btnFilter, ThemeUtil.OCEAN_BLUE);
        btnFilter.addActionListener(e -> loadData());
        pnlBot.add(btnFilter);
        pnlFilter.add(pnlBot);

        pnlHeader.add(pnlFilter, BorderLayout.CENTER);
        add(pnlHeader, BorderLayout.NORTH);

        // --- TABLE PANEL ---
        RoundedPanel pnlTable = ThemeUtil.createCardPanel();
        pnlTable.setLayout(new BorderLayout(10, 10));

        String[] cols = {"Pilih", "No Faktur", "Tanggal", "Customer", "Kasir", "Total Bayar"};
        tblModel = new DefaultTableModel(cols, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Boolean.class : super.getColumnClass(columnIndex);
            }
            @Override
            public boolean isCellEditable(int row, int column) { return column == 0; }
        };
        tblLaporan = new JTable(tblModel);
        JScrollPane sp = new JScrollPane(tblLaporan);
        ThemeUtil.styleTable(tblLaporan, sp);
        pnlTable.add(sp, BorderLayout.CENTER);

        // --- FOOTER (SUMMARY & EXPORT) ---
        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setOpaque(false);
        pnlFooter.setBorder(new EmptyBorder(10, 0, 0, 0));

        JPanel pnlSummary = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        pnlSummary.setOpaque(false);
        lblTotalTransaksi = new JLabel("Total Transaksi: 0");
        lblTotalTransaksi.setFont(ThemeUtil.FONT_BOLD);
        lblTotalPendapatan = new JLabel("Total Pendapatan: Rp0");
        lblTotalPendapatan.setFont(ThemeUtil.FONT_BOLD);
        lblTotalPendapatan.setForeground(ThemeUtil.SUCCESS_COLOR);
        pnlSummary.add(lblTotalTransaksi);
        pnlSummary.add(lblTotalPendapatan);

        JPanel pnlAction = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlAction.setOpaque(false);

        JButton btnExportExcel = new JButton("Export ke Excel");
        ThemeUtil.styleButton(btnExportExcel, new java.awt.Color(33, 115, 70)); // Excel Green
        btnExportExcel.addActionListener(e -> exportToExcel());
        pnlAction.add(btnExportExcel);

        pnlFooter.add(pnlSummary, BorderLayout.WEST);
        pnlFooter.add(pnlAction, BorderLayout.EAST);

        pnlTable.add(pnlFooter, BorderLayout.SOUTH);
        add(pnlTable, BorderLayout.CENTER);
    }

    private void loadDropdowns() {
        cbKategori.addItem("Semua Kategori");
        java.util.List<model.Kategori> kats = kategoriService.getAllKategori();
        for (model.Kategori k : kats) {
            cbKategori.addItem(k.getIdKategori() + " - " + k.getNamaKategori());
        }
        util.AutoCompletion.enable(cbKategori);

        cbCustomer.addItem("Semua Customer");
        java.util.List<model.Customer> custs = customerService.getAllCustomer();
        for (model.Customer c : custs) {
            cbCustomer.addItem(c.getIdCustomer() + " - " + c.getNamaCustomer());
        }
        util.AutoCompletion.enable(cbCustomer);
    }

    private void loadData() {
        tblModel.setRowCount(0);
        
        Date start = parseDate(txtTglMulai.getText());
        Date end = parseDate(txtTglAkhir.getText());
        
        String kat = cbKategori.getSelectedItem().toString();
        String idKat = kat.equals("Semua Kategori") ? "Semua Kategori" : kat.split(" - ")[0];
        
        String cus = cbCustomer.getSelectedItem().toString();
        String idCus = cus.equals("Semua Customer") ? "Semua Customer" : cus.split(" - ")[0];

        List<Object[]> data = laporanService.getLaporanRingkasan(start, end, idCus, idKat);
        
        double totalPendapatan = 0;
        for (Object[] row : data) {
            totalPendapatan += (double) row[4];
            row[4] = Formatter.formatRupiah((double) row[4]);
            tblModel.addRow(new Object[]{true, row[0], row[1], row[2], row[3], row[4]});
        }

        lblTotalTransaksi.setText("Total Transaksi: " + data.size());
        lblTotalPendapatan.setText("Total Pendapatan: " + Formatter.formatRupiah(totalPendapatan));
    }

    private Date parseDate(String text) {
        if (text == null || text.trim().isEmpty()) return null;
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(text);
        } catch (Exception e) {
            return null; // Ignore invalid format
        }
    }

    private void exportToExcel() {
        try {
            Date start = parseDate(txtTglMulai.getText());
            Date end = parseDate(txtTglAkhir.getText());
            String kat = cbKategori.getSelectedItem().toString();
            String idKat = kat.equals("Semua Kategori") ? "Semua Kategori" : kat.split(" - ")[0];
            String cus = cbCustomer.getSelectedItem().toString();
            String idCus = cus.equals("Semua Customer") ? "Semua Customer" : cus.split(" - ")[0];

            // Kumpulkan No Faktur yang tercentang
            java.util.Set<String> selectedFaktur = new java.util.HashSet<>();
            for (int i = 0; i < tblModel.getRowCount(); i++) {
                Boolean isSelected = (Boolean) tblModel.getValueAt(i, 0);
                if (isSelected != null && isSelected) {
                    selectedFaktur.add((String) tblModel.getValueAt(i, 1));
                }
            }

            if (selectedFaktur.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this, "Pilih minimal 1 transaksi untuk diekspor!", "Peringatan", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }

            List<Object[]> details = laporanService.getLaporanDetail(start, end, idCus, idKat);

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Laporan Penjualan");

            // Header Style
            CellStyle headerStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // Headers
            Row headerRow = sheet.createRow(0);
            String[] headers = {"No Faktur", "Tanggal", "Customer", "Nama Barang", "Kategori", "Harga Satuan", "Qty", "Subtotal"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data
            int rowNum = 1;
            double grandTotal = 0;
            for (Object[] row : details) {
                if (!selectedFaktur.contains(row[0].toString())) continue;
                
                Row r = sheet.createRow(rowNum++);
                r.createCell(0).setCellValue(row[0].toString()); // Faktur
                r.createCell(1).setCellValue(row[1].toString()); // Tanggal
                r.createCell(2).setCellValue(row[2].toString()); // Customer
                r.createCell(3).setCellValue(row[3].toString()); // Barang
                r.createCell(4).setCellValue(row[4].toString()); // Kategori
                r.createCell(5).setCellValue((Double) row[5]);   // Harga
                r.createCell(6).setCellValue((Integer) row[6]);  // Qty
                r.createCell(7).setCellValue((Double) row[7]);   // Subtotal
                grandTotal += (Double) row[7];
            }

            // Grand Total Row
            Row totalRow = sheet.createRow(rowNum + 1);
            Cell totalLabel = totalRow.createCell(6);
            totalLabel.setCellValue("GRAND TOTAL:");
            totalLabel.setCellStyle(headerStyle);
            
            Cell totalValue = totalRow.createCell(7);
            totalValue.setCellValue(grandTotal);
            totalValue.setCellStyle(headerStyle);

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            String home = System.getProperty("user.home");
            File file = new File(home + "/Downloads/Laporan_Penjualan_" + System.currentTimeMillis() + ".xlsx");
            
            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }
            workbook.close();

            javax.swing.JOptionPane.showMessageDialog(this, "Berhasil! Tersimpan di " + file.getAbsolutePath(), "Sukses", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            
            // Auto open
            Desktop.getDesktop().open(file);

        } catch (Exception ex) {
            ex.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, "Gagal export ke Excel: " + ex.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
}
