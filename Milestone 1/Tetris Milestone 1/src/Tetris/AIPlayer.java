package Tetris;

import java.util.Random;
import javax.swing.Timer;
import javax.swing.SwingUtilities;

public class AIPlayer implements Runnable {
    private GameBoard gameBoard;
    private GameScreen gameScreen;
    private boolean isRunning;
    private Random random;
    private Timer aiTimer;

    public AIPlayer(GameBoard gameBoard, GameScreen gameScreen) {
        this.gameBoard = gameBoard;
        this.gameScreen = gameScreen;
        this.isRunning = true;
        this.random = new Random();
    }

    // Start the AI player in a separate thread
    public void start() {
        Thread aiThread = new Thread(this);
        aiThread.start();
    }

    @Override
    public void run() {
        aiTimer = new Timer(GameConfig.AI_DELAY, e -> makeMove());
        aiTimer.start();
    }

    private void makeMove() {
        if (!isRunning) return;

        Tetromino tetromino = gameBoard.getCurrentTetromino();
        if (tetromino == null) return;

        // Implement simple AI logic for selecting moves
        // The AI will randomly decide whether to move left, right, or rotate
        if (random.nextBoolean()) {
            // Randomly choose to move left or right
            if (random.nextBoolean() && gameBoard.canMove(tetromino, tetromino.getX() + 1, tetromino.getY())) {
                tetromino.moveRight();
            } else if (gameBoard.canMove(tetromino, tetromino.getX() - 1, tetromino.getY())) {
                tetromino.moveLeft();
            }
        } else {
            // Randomly choose to rotate
            tetromino.rotate();
            if (!gameBoard.canMove(tetromino, tetromino.getX(), tetromino.getY())) {
                // Rotate back if the position is invalid
                tetromino.rotate();
                tetromino.rotate();
                tetromino.rotate();
            }
        }

        // Always try to move down
        if (gameBoard.canMove(tetromino, tetromino.getX(), tetromino.getY() + 1)) {
            tetromino.moveDown();
        } else {
            // Place the tetromino when it cannot move down further
            gameBoard.placeTetromino(tetromino);
            int cleared = gameBoard.clearLines();
            gameScreen.updateScore(cleared);
            gameScreen.updateLevel();

            // Check if the game is over
            if (gameBoard.isGameOver()) {
                stop();
                SwingUtilities.invokeLater(() -> gameScreen.stopGame());
            }

            // Set the next Tetromino
            Tetromino newTetromino = gameScreen.generateNewTetromino();
            newTetromino.setX(gameBoard.getBoard()[0].length / 2 - newTetromino.getShape()[0].length / 2);
            gameBoard.setCurrentTetromino(newTetromino);
        }

        gameScreen.repaint();
    }

    public void stop() {
        isRunning = false;
        if (aiTimer != null) {
            aiTimer.stop();
        }
    }
}
