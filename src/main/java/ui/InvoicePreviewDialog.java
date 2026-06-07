package ui;

import ui.components.RoundedPanel;
import util.ThemeUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class InvoicePreviewDialog extends JDialog {

    private BufferedImage image;
    private File pdfFile;
    private util.ReceiptPrinter printer;

    public InvoicePreviewDialog(JFrame parent, BufferedImage image, File pdfFile, util.ReceiptPrinter printer) {
        super(parent, "Preview Faktur: " + pdfFile.getName(), true);
        this.image = image;
        this.pdfFile = pdfFile;
        this.printer = printer;

        initComponents();
        setSize(400, 600);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(ThemeUtil.BG_SOFT);

        // Header Alert
        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelInfo.setBackground(ThemeUtil.SUCCESS_COLOR);
        JLabel lblInfo = new JLabel("Faktur telah otomatis tersimpan sebagai PDF!");
        lblInfo.setForeground(Color.WHITE);
        lblInfo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panelInfo.add(lblInfo);
        add(panelInfo, BorderLayout.NORTH);

        // Preview Canvas
        JPanel panelCanvas = new JPanel(new GridBagLayout());
        panelCanvas.setBackground(Color.DARK_GRAY);
        
        // Buat custom JLabel untuk menggambar image yang sudah di-scale 50% ke ukuran normal layar
        JLabel lblImage = new JLabel(new ImageIcon(image.getScaledInstance(image.getWidth() / 2, image.getHeight() / 2, Image.SCALE_SMOOTH)));
        lblImage.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        panelCanvas.add(lblImage);

        JScrollPane scroll = new JScrollPane(panelCanvas);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        // Buttons Footer
        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelBtn.setOpaque(false);
        panelBtn.setBorder(new EmptyBorder(5, 10, 10, 10));

        JButton btnOpen = new JButton("Buka PDF");
        ThemeUtil.styleButton(btnOpen, ThemeUtil.OCEAN_BLUE);
        btnOpen.addActionListener(e -> {
            try {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(pdfFile);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal membuka PDF: " + ex.getMessage());
            }
        });

        JButton btnPrint = new JButton("Cetak Fisik");
        ThemeUtil.styleButton(btnPrint, ThemeUtil.OCEAN_BLUE);
        btnPrint.addActionListener(e -> {
            try {
                java.awt.print.PrinterJob pj = java.awt.print.PrinterJob.getPrinterJob();
                pj.setPrintable(printer);
                if (pj.printDialog()) {
                    pj.print();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal mencetak: " + ex.getMessage());
            }
        });

        JButton btnTutup = new JButton("Tutup");
        ThemeUtil.styleButton(btnTutup, ThemeUtil.ERROR_COLOR);
        btnTutup.addActionListener(e -> dispose());

        panelBtn.add(btnOpen);
        panelBtn.add(btnPrint);
        panelBtn.add(btnTutup);

        add(panelBtn, BorderLayout.SOUTH);
    }
}
