package ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import util.ThemeUtil;

public class Toast extends JWindow {
    
    public static final int SUCCESS = 1;
    public static final int ERROR = 2;
    public static final int INFO = 3;

    private float opacity = 0.0f;
    private Timer fadeInTimer;
    private Timer fadeOutTimer;
    private Timer holdTimer;

    private Toast(JFrame owner, String message, int type) {
        super(owner);
        setLayout(new BorderLayout());
        setAlwaysOnTop(true);
        setBackground(new Color(0, 0, 0, 0)); // Transparent window background

        JPanel panel = new JPanel(new BorderLayout(15, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Shadow
                g2.setColor(new Color(0,0,0,30));
                g2.fillRoundRect(2, 2, getWidth()-4, getHeight()-4, 15, 15);
                
                // Background color based on type
                if (type == SUCCESS) g2.setColor(ThemeUtil.SUCCESS_COLOR);
                else if (type == ERROR) g2.setColor(ThemeUtil.ERROR_COLOR);
                else g2.setColor(ThemeUtil.OCEAN_BLUE);

                g2.fillRoundRect(0, 0, getWidth()-2, getHeight()-2, 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Icon based on type
        JLabel iconLabel = new JLabel();
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        iconLabel.setForeground(Color.WHITE);
        if (type == SUCCESS) iconLabel.setText("✓");
        else if (type == ERROR) iconLabel.setText("✕");
        else iconLabel.setText("i");
        
        JLabel lblMessage = new JLabel("<html><body style='width: 200px;'>" + message + "</body></html>");
        lblMessage.setFont(ThemeUtil.FONT_REGULAR);
        lblMessage.setForeground(Color.WHITE);

        panel.add(iconLabel, BorderLayout.WEST);
        panel.add(lblMessage, BorderLayout.CENTER);

        add(panel);
        pack();

        // Position: Top Right
        if (owner != null) {
            Point p = owner.getLocationOnScreen();
            setLocation(p.x + owner.getWidth() - getWidth() - 30, p.y + 70);
        } else {
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            setLocation(screen.width - getWidth() - 30, 70);
        }

        setOpacity(0.0f);
        
        // Timers for Animation
        fadeInTimer = new Timer(20, e -> {
            opacity += 0.05f;
            if (opacity >= 1.0f) {
                opacity = 1.0f;
                fadeInTimer.stop();
                holdTimer.start();
            }
            setOpacity(opacity);
        });

        holdTimer = new Timer(3000, e -> {
            holdTimer.stop();
            fadeOutTimer.start();
        });

        fadeOutTimer = new Timer(20, e -> {
            opacity -= 0.05f;
            if (opacity <= 0.0f) {
                opacity = 0.0f;
                fadeOutTimer.stop();
                dispose();
            }
            setOpacity(opacity);
        });
    }

    public static void show(JFrame owner, String message, int type) {
        Toast toast = new Toast(owner, message, type);
        toast.setVisible(true);
        toast.fadeInTimer.start();
    }
    
    public static void showSuccess(JFrame owner, String message) {
        show(owner, message, SUCCESS);
    }
    
    public static void showError(JFrame owner, String message) {
        show(owner, message, ERROR);
    }
    
    public static void showInfo(JFrame owner, String message) {
        show(owner, message, INFO);
    }
}
