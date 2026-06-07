package util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class PdfGenerator {

    public static BufferedImage createReceiptImage(ReceiptPrinter printer, int numItems) {
        // Hitung tinggi kertas secara akurat berdasarkan logika di ReceiptPrinter
        // Header + Info + Separator = ~116px
        // 1 Item = 24px
        // Totals + Footer + Margin = ~110px
        int height = 226 + (numItems * 24);
        int width = 210;

        // Scale 2x agar resolusi tidak pecah saat dikonversi ke PDF
        BufferedImage image = new BufferedImage(width * 2, height * 2, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width * 2, height * 2);
        g2d.setColor(Color.BLACK);
        g2d.scale(2.0, 2.0);

        java.awt.print.PageFormat pf = new java.awt.print.PageFormat();
        java.awt.print.Paper paper = new java.awt.print.Paper();
        paper.setSize(width, height);
        paper.setImageableArea(0, 0, width, height);
        pf.setPaper(paper);

        try {
            printer.print(g2d, pf, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        g2d.dispose();
        
        return image;
    }

    public static File saveAsPdf(BufferedImage image, String noFaktur) throws Exception {
        File dir = new File("invoices");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File pdfFile = new File(dir, noFaktur + ".pdf");

        PDDocument doc = new PDDocument();
        // Ukuran asli canvas
        float pdfWidth = image.getWidth() / 2f;
        float pdfHeight = image.getHeight() / 2f;
        
        PDPage page = new PDPage(new PDRectangle(pdfWidth, pdfHeight));
        doc.addPage(page);

        PDImageXObject pdImage = LosslessFactory.createFromImage(doc, image);
        PDPageContentStream contentStream = new PDPageContentStream(doc, page);
        contentStream.drawImage(pdImage, 0, 0, pdfWidth, pdfHeight);
        contentStream.close();

        doc.save(pdfFile);
        doc.close();

        return pdfFile;
    }
}
