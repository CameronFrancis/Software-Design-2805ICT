import javax.swing.*;
import java.awt.Dimension;

public class MainApp {
    public static void main(String[] args) {
        // Show the splash screen
        SplashScreen splash = new SplashScreen(5000, 450, 300);
        splash.showSplash();

        // After 5 seconds show splash
        SwingUtilities.invokeLater(() -> {
            JFrame mainFrame = new JFrame(GameConfig.GAME_TITLE);
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Initialise main menu
            MainMenu mainMenu = new MainMenu(mainFrame);

            // set up size of mainmenu frame
            mainMenu.setPreferredSize(new Dimension(
                GameConfig.BOARD_WIDTH * GameConfig.CELL_SIZE, 
                GameConfig.BOARD_HEIGHT * GameConfig.CELL_SIZE
            ));

            // Set the content pane to main menu
            mainFrame.setContentPane(mainMenu);

            // pack() to size the frame to match the content
            mainFrame.pack();
            mainFrame.setLocationRelativeTo(null); // Center the frame on the screen

            mainFrame.setVisible(true);
        });
    }
}
