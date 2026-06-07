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
    public static final Color OCEAN_BLUE = new Color(14, 116, 144);   // Cyan-700 / Ocean
    public static final Color OCEAN_BLUE_DARK = new Color(21, 94, 117); // Cyan-800
    public static final Color SKY_BLUE = new Color(14, 165, 233);     // Sky-500
    public static final Color SUCCESS_COLOR = new Color(16, 185, 129); // Emerald-500 / Lush Green
    public static final Color SUCCESS_HOVER = new Color(5, 150, 105);  // Emerald-600
    public static final Color ERROR_COLOR = new Color(239, 68, 68);    // Red-500
    public static final Color ERROR_HOVER = new Color(220, 38, 38);    // Red-600
    
    public static final Color TEXT_PRIMARY = new Color(15, 23, 42);   // Slate-900
    public static final Color TEXT_SECONDARY = new Color(71, 85, 105); // Slate-600
    public static final Color BORDER_COLOR = new Color(203, 213, 225); // Slate-300
    public static final Color BORDER_FOCUS = SKY_BLUE;
    
    public static final Color BG_WHITE = Color.WHITE;
    public static final Color BG_SOFT = new Color(248, 250, 252);     // Slate-50
    public static final Color HOVER_BG = new Color(240, 249, 255);    // Sky-50 (Very light blue for zebra stripe)

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
     * Styles a JTextField to look modern with rounded borders and focus states.
     */
    public static void styleTextField(JTextField field) {
        field.setFont(FONT_REGULAR);
        field.setForeground(TEXT_PRIMARY);
        field.setBackground(BG_WHITE);
        field.setCaretColor(TEXT_PRIMARY);
        
        Border defaultBorder = BorderFactory.createCompoundBorder(
            new RoundedBorder(BORDER_COLOR, 8),
            new EmptyBorder(6, 10, 6, 10)
        );
        Border focusBorder = BorderFactory.createCompoundBorder(
            new RoundedBorder(BORDER_FOCUS, 8),
            new EmptyBorder(6, 10, 6, 10)
        );
        
        field.setBorder(defaultBorder);
        
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(focusBorder);
            }
            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(defaultBorder);
            }
        });
    }

    /**
     * Styles a JPasswordField.
     */
    public static void stylePasswordField(JPasswordField field) {
        styleTextField(field);
    }

    /**
     * Styles a JComboBox.
     */
    public static void styleComboBox(JComboBox<?> combo) {
        combo.setFont(FONT_REGULAR);
        combo.setForeground(TEXT_PRIMARY);
        combo.setBackground(BG_WHITE);
        combo.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(BORDER_COLOR, 8),
            new EmptyBorder(3, 5, 3, 5)
        ));
    }

    /**
     * Styles a JButton with hover animation.
     */
    public static void styleButton(JButton btn, Color bgColor) {
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(FONT_BOLD);
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Calculate hover color (slightly darker)
        Color hoverColor;
        if (bgColor.equals(SUCCESS_COLOR)) hoverColor = SUCCESS_HOVER;
        else if (bgColor.equals(ERROR_COLOR)) hoverColor = ERROR_HOVER;
        else if (bgColor.equals(OCEAN_BLUE)) hoverColor = OCEAN_BLUE_DARK;
        else hoverColor = bgColor.darker();

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if(btn.isEnabled()) btn.setBackground(hoverColor);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if(btn.isEnabled()) btn.setBackground(bgColor);
            }
        });
    }

    /**
     * Styles a JTable to look like a modern web table (Zebra Stripe).
     */
    public static void styleTable(JTable table, JScrollPane scrollPane) {
        // Table Body
        table.setFont(FONT_REGULAR);
        table.setForeground(TEXT_PRIMARY);
        table.setRowHeight(35);
        table.setPreferredScrollableViewportSize(new Dimension(table.getPreferredSize().width, 35 * 10));
        table.setShowGrid(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(226, 232, 240)); // Slate-200
        table.setBackground(BG_WHITE);
        table.setSelectionBackground(SKY_BLUE); 
        table.setSelectionForeground(Color.WHITE);
        
        // Remove standard cell border and add padding & zebra stripe
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(new EmptyBorder(0, 10, 0, 10)); // Inner padding
                
                if (isSelected) {
                    c.setBackground(SKY_BLUE);
                    c.setForeground(Color.WHITE);
                    c.setFont(ThemeUtil.FONT_BOLD);
                } else {
                    // Zebra stripe
                    if (row % 2 == 0) {
                        c.setBackground(BG_WHITE);
                    } else {
                        c.setBackground(HOVER_BG); // Sangat soft blue
                    }
                    c.setForeground(TEXT_PRIMARY);
                    c.setFont(ThemeUtil.FONT_REGULAR);
                }
                return c;
            }
        });

        // Table Header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setForeground(TEXT_SECONDARY);
        header.setBackground(BG_WHITE);
        header.setBorder(new MatteBorder(0, 0, 2, 0, BORDER_COLOR));
        
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.LEFT);
        
        // Wrap header renderer to add padding
        final DefaultTableCellRenderer defaultRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = defaultRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                ((JComponent) c).setBorder(BorderFactory.createCompoundBorder(
                    ((JComponent) c).getBorder(),
                    new EmptyBorder(10, 10, 10, 10)
                ));
                return c;
            }
        });

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
