/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class ThemeUtil {

    // === COLOR PALETTE (Tailwind Slate / Modern Defaults) ===
    public static final Color TEXT_PRIMARY = new Color(15, 23, 42);   // Slate 900
    public static final Color TEXT_SECONDARY = new Color(71, 85, 105); // Slate 600
    public static final Color BORDER_COLOR = new Color(203, 213, 225); // Slate 300
    public static final Color BG_WHITE = Color.WHITE;
    public static final Color BG_SOFT = new Color(248, 250, 252);     // Slate 50
    public static final Color HOVER_BG = new Color(241, 245, 249);    // Slate 100

    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);

    /**
     * Styles a JTextField to look modern with padding and soft borders.
     */
    public static void styleTextField(JTextField field) {
        field.setFont(FONT_REGULAR);
        field.setForeground(TEXT_PRIMARY);
        field.setBackground(BG_WHITE);
        field.setCaretColor(TEXT_PRIMARY);
        
        Border line = new LineBorder(BORDER_COLOR, 1, true);
        Border empty = new EmptyBorder(8, 12, 8, 12);
        field.setBorder(BorderFactory.createCompoundBorder(line, empty));
    }

    /**
     * Styles a JComboBox.
     */
    public static void styleComboBox(JComboBox<?> combo) {
        combo.setFont(FONT_REGULAR);
        combo.setForeground(TEXT_PRIMARY);
        combo.setBackground(BG_WHITE);
        combo.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(5, 5, 5, 5)
        ));
    }

    /**
     * Styles a JButton.
     */
    public static void styleButton(JButton btn, Color bgColor) {
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(FONT_BOLD);
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    /**
     * Styles a JTable to look like a modern web table.
     */
    public static void styleTable(JTable table, JScrollPane scrollPane) {
        // Table Body
        table.setFont(FONT_REGULAR);
        table.setForeground(TEXT_PRIMARY);
        table.setRowHeight(35);
        table.setShowGrid(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(HOVER_BG);
        table.setBackground(BG_WHITE);
        table.setSelectionBackground(new Color(59, 130, 246)); // Blue 500
        table.setSelectionForeground(Color.WHITE);
        
        // Remove standard cell border and add padding
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(new EmptyBorder(0, 10, 0, 10)); // Inner padding
                
                // Keep selection color even if focus is lost, and make text bold
                if (isSelected) {
                    c.setBackground(new Color(59, 130, 246));
                    c.setForeground(Color.WHITE);
                    c.setFont(ThemeUtil.FONT_BOLD);
                } else {
                    c.setBackground(BG_WHITE);
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
        header.setBackground(BG_SOFT);
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

        // ScrollPane
        scrollPane.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        scrollPane.getViewport().setBackground(BG_WHITE);
    }
}
