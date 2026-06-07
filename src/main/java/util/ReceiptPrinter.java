package util;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;

public class ReceiptPrinter implements Printable {
    private Map<String, Object> header;
    private List<Map<String, Object>> details;

    public ReceiptPrinter(Map<String, Object> header, List<Map<String, Object>> details) {
        this.header = header;
        this.details = details;
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        Font fontHeader = new Font("Monospaced", Font.BOLD, 12);
        Font fontBody = new Font("Monospaced", Font.PLAIN, 10);
        Font fontFooter = new Font("Monospaced", Font.BOLD, 10);
        
        int y = 20;
        int lineSpacing = 12;
        int paperWidth = 210; // Slightly wider for 58mm thermal paper to avoid truncation

        // Header
        g2d.setFont(fontHeader);
        drawCenteredString(g2d, "TOKO BERKAH JAYA", paperWidth, y);
        y += lineSpacing;
        g2d.setFont(fontBody);
        drawCenteredString(g2d, "Kenyamanan Anda Prioritas Kami", paperWidth, y);
        y += lineSpacing * 2;

        // Info
        g2d.drawString("No      : " + header.get("no_faktur"), 0, y);
        y += lineSpacing;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        g2d.drawString("Tanggal : " + sdf.format(header.get("tanggal")), 0, y);
        y += lineSpacing;
        g2d.drawString("Kasir   : " + header.get("kasir"), 0, y);
        y += lineSpacing;
        g2d.drawString("-------------------------------------", 0, y);
        y += lineSpacing;

        // Items
        for (Map<String, Object> item : details) {
            String nama = (String) item.get("nama_barang");
            if (nama.length() > 24) nama = nama.substring(0, 24); // truncate if too long
            g2d.drawString(nama, 0, y);
            y += lineSpacing;
            
            int qty = (int) item.get("jumlah");
            double harga = (double) item.get("harga_jual");
            double subtotal = (double) item.get("subtotal");
            
            String qtyStr = qty + " x " + Formatter.formatRupiah(harga);
            String subStr = Formatter.formatRupiah(subtotal);
            
            g2d.drawString(qtyStr, 10, y);
            g2d.drawString(subStr, paperWidth - g2d.getFontMetrics().stringWidth(subStr) - 10, y);
            y += lineSpacing;
        }

        g2d.drawString("-------------------------------------", 0, y);
        y += lineSpacing;

        // Totals
        g2d.setFont(fontFooter);
        double total = (double) header.get("total");
        double bayar = (double) header.get("bayar");
        double kembali = (double) header.get("kembali");
        
        drawStringRightAligned(g2d, "Total: " + Formatter.formatRupiah(total), paperWidth - 10, y);
        y += lineSpacing;
        drawStringRightAligned(g2d, "Tunai: " + Formatter.formatRupiah(bayar), paperWidth - 10, y);
        y += lineSpacing;
        drawStringRightAligned(g2d, "Kembali: " + Formatter.formatRupiah(kembali), paperWidth - 10, y);
        y += lineSpacing * 2;

        g2d.setFont(fontBody);
        drawCenteredString(g2d, "Terima Kasih Atas", paperWidth, y);
        y += lineSpacing;
        drawCenteredString(g2d, "Kunjungan Anda", paperWidth, y);

        return PAGE_EXISTS;
    }
    
    private void drawCenteredString(Graphics2D g, String text, int width, int y) {
        FontMetrics metrics = g.getFontMetrics();
        int x = (width - metrics.stringWidth(text)) / 2;
        g.drawString(text, x, y);
    }
    
    private void drawStringRightAligned(Graphics2D g, String text, int rightX, int y) {
        FontMetrics metrics = g.getFontMetrics();
        int x = rightX - metrics.stringWidth(text);
        g.drawString(text, x, y);
    }
}
