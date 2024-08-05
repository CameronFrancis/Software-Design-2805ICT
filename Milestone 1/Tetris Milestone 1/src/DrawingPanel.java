import javax.swing.*;
import java.awt.*;

public class DrawingPanel extends JPanel {
    public DrawingPanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Enable antialiasing for smoother shapes
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw a rectangle
        g2d.setColor(Color.RED);
        g2d.fillRect(50, 50, 150, 100);

        // Draw an oval
        g2d.setColor(Color.BLUE);
        g2d.fillOval(250, 50, 150, 100);

        // Draw a line
        g2d.setColor(Color.GREEN);
        g2d.drawLine(50, 200, 200, 300);

        // Draw a polygon (triangle)
        g2d.setColor(Color.ORANGE);
        int[] xPoints = {400, 450, 500};
        int[] yPoints = {300, 200, 300};
        g2d.fillPolygon(xPoints, yPoints, 3);

        // Draw text
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString("Hello, World!", 50, 400);
    }
}
