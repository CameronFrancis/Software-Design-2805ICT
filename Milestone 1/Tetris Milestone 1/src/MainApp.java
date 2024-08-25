import javax.swing.*;
import java.awt.Dimension;

public class MainApp {
    public static void main(String[] args) {
        // Show the splash screen
        SplashScreen splash = new SplashScreen(5000, 450, 300);
        splash.showSplash();

        // After the splash screen, show the main application window
        SwingUtilities.invokeLater(() -> {
            JFrame mainFrame = new JFrame("Enhanced Tetris");
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Initialize the game screen
            GameScreen gameScreen = new GameScreen(mainFrame, new MainMenu(mainFrame));
            int cellSize = 30;
            int boardWidth = 10;  // Number of columns
            int boardHeight = 20; // Number of rows

            // Set the preferred size based on the game board size
            gameScreen.setPreferredSize(new Dimension(boardWidth * cellSize, boardHeight * cellSize));

            // Add the game screen or main menu panel to the main frame
            mainFrame.setContentPane(gameScreen); // or new MainMenu(mainFrame)

            // Use pack() to adjust window size to fit the preferred size of GameScreen
            mainFrame.pack();
            mainFrame.setLocationRelativeTo(null); // Center the window on the screen

            mainFrame.setVisible(true);
        });
    }
}
