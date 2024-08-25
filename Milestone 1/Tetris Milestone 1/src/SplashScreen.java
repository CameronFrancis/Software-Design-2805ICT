import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JWindow {
    private int duration;
    private int maxWidth;
    private int maxHeight;

    public SplashScreen(int duration, int maxWidth, int maxHeight) {
        this.duration = duration;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
    }

    public void showSplash() {
        JPanel content = (JPanel) getContentPane();
        content.setBackground(Color.white);

        // Load the splash image
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/resources/splash.png"));
        Image originalImage = originalIcon.getImage();

        // Calculate the new dimensions maintaining the aspect ratio
        int originalWidth = originalIcon.getIconWidth();
        int originalHeight = originalIcon.getIconHeight();
        float aspectRatio = (float) originalWidth / originalHeight;

        int newWidth = maxWidth;
        int newHeight = maxHeight;
        if (originalWidth > originalHeight) {
            newHeight = Math.round(maxWidth / aspectRatio);
        } else {
            newWidth = Math.round(maxHeight * aspectRatio);
        }

        // Scale the image
        Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        // Set the window dimensions and center
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - newWidth) / 2;
        int y = (screen.height - newHeight) / 2;
        setBounds(x, y, newWidth, newHeight);

        // Build the splash screen
        JLabel label = new JLabel(scaledIcon);
        JLabel copyrt = new JLabel("Copyright 2024, MyApp", JLabel.CENTER);
        copyrt.setFont(new Font("Sans-Serif", Font.BOLD, 12));
        content.add(label, BorderLayout.CENTER);
        content.add(copyrt, BorderLayout.SOUTH);
        Color oraRed = new Color(156, 20, 20, 255);
        content.setBorder(BorderFactory.createLineBorder(oraRed, 10));

        // Display it
        setVisible(true);

        // Wait time
        try {
            Thread.sleep(duration);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setVisible(false);
    }

    public void showSplashAndExit() {
        showSplash();
        System.exit(0);
    }

    public static void main(String[] args) {
        SplashScreen splash = new SplashScreen(5000, 450, 300);
        splash.showSplashAndExit();
    }
}
