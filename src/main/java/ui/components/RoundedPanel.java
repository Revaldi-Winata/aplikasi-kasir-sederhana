package ui.components;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Color;
import java.awt.LayoutManager;

public class RoundedPanel extends JPanel {
    private int cornerRadius = 15;
    private Color backgroundColor;
    private boolean drawShadow;

    public RoundedPanel(int radius, Color bgColor) {
        super();
        this.cornerRadius = radius;
        this.backgroundColor = bgColor;
        this.drawShadow = true;
        setOpaque(false);
    }

    public RoundedPanel(int radius, Color bgColor, boolean drawShadow) {
        super();
        this.cornerRadius = radius;
        this.backgroundColor = bgColor;
        this.drawShadow = drawShadow;
        setOpaque(false);
    }
    
    public RoundedPanel(LayoutManager layout, int radius, Color bgColor) {
        super(layout);
        this.cornerRadius = radius;
        this.backgroundColor = bgColor;
        this.drawShadow = true;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (drawShadow) {
            // Draw subtle drop shadow effect (layering semi-transparent rects)
            g2.setColor(new Color(0, 0, 0, 8)); 
            g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, cornerRadius, cornerRadius);
            g2.setColor(new Color(0, 0, 0, 4)); 
            g2.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 2, cornerRadius, cornerRadius);
        }

        // Draw the actual panel background
        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0, getWidth() - (drawShadow ? 2 : 0), getHeight() - (drawShadow ? 2 : 0), cornerRadius, cornerRadius);
        
        g2.dispose();
    }
}
