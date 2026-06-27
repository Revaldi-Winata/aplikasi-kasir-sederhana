package util;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import ui.components.RoundedBorder;
import ui.components.RoundedPanel;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ThemeUtil {

    // === NEW COLOR PALETTE (Toko Berkah Jaya) ===
    public static final Color OCEAN_BLUE = new Color(59, 130, 246);   // Blue-500
    public static final Color OCEAN_BLUE_DARK = new Color(37, 99, 235); // Blue-600
    public static final Color SKY_BLUE = new Color(14, 165, 233);     // Sky-500
    public static final Color SUCCESS_COLOR = new Color(16, 185, 129); // Emerald-500
    public static final Color SUCCESS_HOVER = new Color(5, 150, 105);  // Emerald-600
    public static final Color ERROR_COLOR = new Color(239, 68, 68);    // Red-500
    public static final Color ERROR_HOVER = new Color(220, 38, 38);    // Red-600
    public static final Color WARNING_COLOR = new Color(249, 115, 22); // Orange-500 (CTA)
    
    public static final Color TEXT_PRIMARY = new Color(30, 41, 59);   // Slate-800
    public static final Color TEXT_SECONDARY = new Color(100, 116, 139); // Slate-500
    public static final Color BORDER_COLOR = new Color(226, 232, 240); // Slate-200
    public static final Color BORDER_FOCUS = SKY_BLUE;
    
    public static final Color BG_WHITE = Color.WHITE;
    public static final Color BG_SOFT = new Color(248, 250, 252);     // Slate-50
    public static final Color HOVER_BG = new Color(241, 245, 249);    // Slate-100

    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);

    /**
     * Creates a white rounded card panel with shadow illusion.
     */
    public static RoundedPanel createCardPanel() {
        RoundedPanel panel = new RoundedPanel(15, BG_WHITE, true);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        return panel;
    }

    /**
     * Adds a DocumentFilter to prevent non-numeric input.
     */
    public static void makeNumberOnly(JTextField textField) {
        // Disabled per user request: allow UI validation to take over
    }

    /**
     * Formats the field as currency (Rp...) in real-time as the user types.
     */
    public static void makeCurrencyField(JTextField textField) {
        textField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                int code = e.getKeyCode();
                // Ignore navigation keys
                if (code == java.awt.event.KeyEvent.VK_LEFT || 
                    code == java.awt.event.KeyEvent.VK_RIGHT || 
                    code == java.awt.event.KeyEvent.VK_UP || 
                    code == java.awt.event.KeyEvent.VK_DOWN) {
                    return;
                }
                
                String text = textField.getText().replace("Rp", "").replace(".", "").trim();
                if (!text.isEmpty()) {
                    try {
                        double parsed = Double.parseDouble(text);
                        String formatted = util.Formatter.formatRupiah(parsed);
                        if (!textField.getText().equals(formatted)) {
                            textField.setText(formatted);
                        }
                    } catch (NumberFormatException ex) {
                        // Not a valid number, skip formatting and let ValidationUtil show error
                    }
                } else {
                    if (!textField.getText().isEmpty()) {
                        textField.setText("");
                    }
                }
            }
        });
    }

    public static void styleTextField(JTextField field) {
        field.setFont(FONT_REGULAR);
    }

    public static void styleTextArea(JTextArea area) {
        area.setFont(FONT_REGULAR);
    }

    public static void stylePasswordField(JPasswordField field) {
        field.setFont(FONT_REGULAR);
    }

    public static void styleComboBox(JComboBox<?> combo) {
        combo.setFont(FONT_REGULAR);
    }

    /**
     * Styles a JButton with hover animation.
     */
    public static void styleButton(JButton btn, Color bgColor) {
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(FONT_BOLD);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // FlatLaf automatically handles hover colors!
    }

    /**
     * Styles a JTable to look like a modern web table.
     */
    public static void styleTable(JTable table, JScrollPane scrollPane) {
        table.setFont(FONT_REGULAR);
        table.setRowHeight(44); // 44px minimum touch target size
        
        // Let FlatLaf handle the native zebra striping and hovering
        table.putClientProperty("JTable.showAlternateRowColor", true);
        
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(BG_WHITE);
    }

    /**
     * Loads an image from resources and scales it smoothly.
     */
    public static ImageIcon getScaledImage(String resourcePath, int width, int height) {
        try {
            java.net.URL imgURL = ThemeUtil.class.getResource(resourcePath);
            if (imgURL != null) {
                ImageIcon icon = new ImageIcon(imgURL);
                Image img = icon.getImage();
                Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImg);
            } else {
                System.err.println("Couldn't find file: " + resourcePath);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
