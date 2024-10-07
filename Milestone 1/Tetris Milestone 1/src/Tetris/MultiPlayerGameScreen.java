package Tetris;

import javax.swing.*;
import java.awt.*;

public class MultiPlayerGameScreen extends JPanel implements GameScreen.GameOverListener {
    private JFrame frame;
    private MainMenu mainMenu;
    private GameScreen player1GameScreen;
    private GameScreen player2GameScreen;
    private SharedTetrominoGenerator sharedGenerator;
    private boolean player1GameOver = false;
    private boolean player2GameOver = false;

    public MultiPlayerGameScreen(JFrame frame, MainMenu mainMenu) {
        this.frame = frame;
        this.mainMenu = mainMenu;

        setLayout(new BorderLayout());

        // Create a shared tetromino generator
        sharedGenerator = new SharedTetrominoGenerator();

        // Initialize game screens for both players
        player1GameScreen = new GameScreen(frame, mainMenu, GameConfig.PLAYER1_TYPE, sharedGenerator, "Player 1");
        player2GameScreen = new GameScreen(frame, mainMenu, GameConfig.PLAYER2_TYPE, sharedGenerator, "Player 2");

        // Set the game over listener for both GameScreens
        player1GameScreen.setGameOverListener(this);
        player2GameScreen.setGameOverListener(this);

        // Add the game screens to the panel
        JPanel gamePanels = new JPanel(new GridLayout(1, 2));
        gamePanels.add(player1GameScreen);
        gamePanels.add(player2GameScreen);

        add(gamePanels, BorderLayout.CENTER);

        // Add a shared end game button at the bottom
        JButton endGameButton = new JButton("End Game");
        endGameButton.addActionListener(e -> endBothGames());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(endGameButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(
            (GameConfig.BOARD_WIDTH * GameConfig.CELL_SIZE + 300) * 2, // Width for two screens
            GameConfig.BOARD_HEIGHT * GameConfig.CELL_SIZE + 100
        ));
    }

    private void endBothGames() {
        player1GameScreen.stopGame();
        player2GameScreen.stopGame();
        // Return to main menu
        frame.setContentPane(mainMenu);
        frame.revalidate();
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    @Override
    public void onGameOver(GameScreen gameScreen) {
        if (gameScreen == player1GameScreen) {
            player1GameOver = true;
        } else if (gameScreen == player2GameScreen) {
            player2GameOver = true;
        }

        // Check if both games are over
        if (player1GameOver && player2GameOver) {
            // Return to main menu
            SwingUtilities.invokeLater(() -> {
                frame.setContentPane(mainMenu);
                frame.revalidate();
                frame.pack();
                frame.setLocationRelativeTo(null);
            });
        }
    }
}
