import javax.swing.*;

public class MainApp {
    public static void main(String[] args) {
        // Show the splash screen
        SplashScreen splash = new SplashScreen(5000, 450, 300);
        splash.showSplash();

        // After the splash screen, show the main application window
        SwingUtilities.invokeLater(() -> {
            JFrame mainFrame = new JFrame("Enhanced Tetris");
            mainFrame.setSize(400, 600);
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Add the main menu panel to the main frame
            MainMenu mainMenu = new MainMenu(mainFrame);
            mainFrame.setContentPane(mainMenu);

            mainFrame.setVisible(true);
        });
    }
}
