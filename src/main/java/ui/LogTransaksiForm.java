package ui;

import service.LogTransaksiService;
import util.ThemeUtil;
import util.Formatter;
import util.ReceiptPrinter;
import ui.components.RoundedPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.print.PrinterJob;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;

public class LogTransaksiForm extends JPanel {
    private JTable tableLog;
    private DefaultTableModel tableModel;
    private JComboBox<Integer> comboLimit;
    private JLabel lblPageInfo;
    private JButton btnPrev;
    private JButton btnNext;
    private JButton btnCetak;

    private LogTransaksiService service;
    private int currentPage = 1;
    private int totalPages = 1;
    private int limit = 10;
    
    public LogTransaksiForm() {
        this.service = new LogTransaksiService();
        setLayout(new BorderLayout(15, 15));
        setBackground(ThemeUtil.BG_SOFT);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        initUI();
        loadData();
    }

    private void initUI() {
        // --- Header ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel lblTitle = new JLabel("Log Transaksi");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(ThemeUtil.TEXT_PRIMARY);
        headerPanel.add(lblTitle, BorderLayout.WEST);

        btnCetak = new JButton("Cetak Ulang Faktur");
        btnCetak.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCetak.setBackground(ThemeUtil.OCEAN_BLUE);
        btnCetak.setForeground(Color.WHITE);
        btnCetak.setFocusPainted(false);
        btnCetak.setEnabled(false);
        btnCetak.addActionListener(e -> cetakUlang());
        headerPanel.add(btnCetak, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // --- Table ---
        String[] columns = {"No Faktur", "Tanggal", "Total", "Bayar", "Kembali", "Kasir"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableLog = new JTable(tableModel);
        tableLog.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableLog.setRowHeight(30);
        tableLog.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableLog.getTableHeader().setBackground(ThemeUtil.OCEAN_BLUE);
        tableLog.getTableHeader().setForeground(Color.WHITE);
        tableLog.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableLog.getSelectionModel().addListSelectionListener(e -> {
            btnCetak.setEnabled(tableLog.getSelectedRow() != -1);
        });

        JScrollPane scrollPane = new JScrollPane(tableLog);
        scrollPane.setBorder(BorderFactory.createLineBorder(ThemeUtil.BORDER_COLOR));

        RoundedPanel cardPanel = new RoundedPanel(15, Color.WHITE);
        cardPanel.setLayout(new BorderLayout());
        cardPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        cardPanel.add(scrollPane, BorderLayout.CENTER);

        add(cardPanel, BorderLayout.CENTER);

        // --- Pagination ---
        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        paginationPanel.setOpaque(false);

        paginationPanel.add(new JLabel("Tampilkan:"));
        comboLimit = new JComboBox<>(new Integer[]{10, 25, 50, 100});
        comboLimit.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboLimit.addActionListener(e -> {
            limit = (int) comboLimit.getSelectedItem();
            currentPage = 1;
            loadData();
        });
        paginationPanel.add(comboLimit);

        btnPrev = new JButton("< Prev");
        btnPrev.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnPrev.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                loadData();
            }
        });
        paginationPanel.add(btnPrev);

        lblPageInfo = new JLabel("Halaman 1 dari 1");
        lblPageInfo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        paginationPanel.add(lblPageInfo);

        btnNext = new JButton("Next >");
        btnNext.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnNext.addActionListener(e -> {
            if (currentPage < totalPages) {
                currentPage++;
                loadData();
            }
        });
        paginationPanel.add(btnNext);

        add(paginationPanel, BorderLayout.SOUTH);
    }

    public void loadData() {
        int totalRows = service.getTotalTransactionsCount();
        totalPages = (int) Math.ceil((double) totalRows / limit);
        if (totalPages == 0) totalPages = 1;

        int offset = (currentPage - 1) * limit;
        List<Map<String, Object>> logs = service.getTransactionsLog(limit, offset);

        tableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        for (Map<String, Object> log : logs) {
            tableModel.addRow(new Object[]{
                log.get("no_faktur"),
                sdf.format(log.get("tanggal")),
                Formatter.formatRupiah((double) log.get("total")),
                Formatter.formatRupiah((double) log.get("bayar")),
                Formatter.formatRupiah((double) log.get("kembali")),
                log.get("kasir")
            });
        }

        lblPageInfo.setText("Halaman " + currentPage + " dari " + totalPages);
        btnPrev.setEnabled(currentPage > 1);
        btnNext.setEnabled(currentPage < totalPages);
        btnCetak.setEnabled(false);
    }

    private void cetakUlang() {
        int selectedRow = tableLog.getSelectedRow();
        if (selectedRow == -1) return;

        String noFaktur = (String) tableModel.getValueAt(selectedRow, 0);
        Map<String, Object> header = service.getTransactionHeader(noFaktur);
        List<Map<String, Object>> details = service.getTransactionDetails(noFaktur);

        if (header != null && !details.isEmpty()) {
            try {
                util.ReceiptPrinter printer = new util.ReceiptPrinter(header, details);
                java.awt.image.BufferedImage img = util.PdfGenerator.createReceiptImage(printer, details.size());
                java.io.File pdfFile = util.PdfGenerator.saveAsPdf(img, noFaktur);
                
                InvoicePreviewDialog dialog = new InvoicePreviewDialog((JFrame) SwingUtilities.getWindowAncestor(this), img, pdfFile, printer);
                dialog.setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal memuat pratinjau faktur: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Data transaksi tidak lengkap!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
