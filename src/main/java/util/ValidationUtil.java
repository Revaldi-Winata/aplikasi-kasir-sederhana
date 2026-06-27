package util;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class ValidationUtil {

    private static class ErrorBorder implements Border {
        private Border originalBorder;

        public ErrorBorder(Border originalBorder) {
            this.originalBorder = originalBorder;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2.setColor(ThemeUtil.ERROR_COLOR);
            g2.drawRoundRect(x, y, width - 1, height - 1, 10, 10);
            
            g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth("!");
            int textHeight = fm.getAscent();
            g2.drawString("!", x + width - textWidth - 12, y + (height + textHeight) / 2 - 3);
            
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            Insets ins = originalBorder.getBorderInsets(c);
            return new Insets(ins.top, ins.left, ins.bottom, ins.right + 20);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }

    public static void addRequiredValidation(JTextField field) {
        final Border normalBorder = field.getBorder();
        final Border errorBorder = new ErrorBorder(normalBorder);

        Runnable validate = () -> {
            if (field.getText().trim().isEmpty()) {
                field.setBorder(normalBorder);
            } else {
                field.setBorder(normalBorder);
            }
        };

        field.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validate.run(); }
            public void removeUpdate(DocumentEvent e) { validate.run(); }
            public void changedUpdate(DocumentEvent e) { validate.run(); }
        });
        
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validate.run();
            }
        });
    }

    public static void addNumericValidation(JTextField field) {
        final Border normalBorder = field.getBorder();
        final Border errorBorder = new ErrorBorder(normalBorder);

        Runnable validate = () -> {
            String text = field.getText().trim();
            text = text.replace("Rp", "").replace(".", "").trim();
            
            if (text.isEmpty()) {
                field.setBorder(normalBorder);
                return;
            }
            try {
                Double.parseDouble(text);
                field.setBorder(normalBorder);
            } catch (NumberFormatException ex) {
                field.setBorder(errorBorder);
            }
        };

        field.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validate.run(); }
            public void removeUpdate(DocumentEvent e) { validate.run(); }
            public void changedUpdate(DocumentEvent e) { validate.run(); }
        });
        
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validate.run();
            }
        });
    }

    public static void addAlphabetValidation(JTextField field) {
        final Border normalBorder = field.getBorder();
        final Border errorBorder = new ErrorBorder(normalBorder);

        Runnable validate = () -> {
            String text = field.getText().trim();
            if (text.isEmpty()) {
                field.setBorder(normalBorder);
                return;
            }
            // Allow letters, spaces, and standard punctuation but NO numbers
            if (text.matches(".*\\d.*")) {
                field.setBorder(errorBorder);
            } else {
                field.setBorder(normalBorder);
            }
        };

        field.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validate.run(); }
            public void removeUpdate(DocumentEvent e) { validate.run(); }
            public void changedUpdate(DocumentEvent e) { validate.run(); }
        });
        
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validate.run();
            }
        });
    }
}
