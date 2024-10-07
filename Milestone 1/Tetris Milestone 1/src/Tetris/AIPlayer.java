package Tetris;

import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;


public class AIPlayer implements Runnable {
    private GameBoard gameBoard;
    private GameScreen gameScreen;
    private boolean isRunning;
    private Timer aiTimer;

    public AIPlayer(GameBoard gameBoard, GameScreen gameScreen) {
        this.gameBoard = gameBoard;
        this.gameScreen = gameScreen;
        this.isRunning = true;
    }

    // Start the AI player in a separate thread
    public void start() {
        Thread aiThread = new Thread(this);
        aiThread.start();
    }

    @Override
    public void run() {
        aiTimer = new Timer(500, e -> makeMove()); // Faster AI actions
        aiTimer.start();
    }

    private void makeMove() {
        if (!isRunning) return;

        Tetromino tetromino = gameBoard.getCurrentTetromino();
        if (tetromino == null) {
            // Initialize the first Tetromino
            tetromino = gameScreen.getNextTetromino();
            tetromino.setX(gameBoard.getBoard()[0].length / 2 - tetromino.getShape()[0].length / 2);
            gameBoard.setCurrentTetromino(tetromino);

            // Generate new next Tetromino
            gameScreen.setNextTetromino(gameScreen.generateNewTetromino());
            gameScreen.getNextTetrominoPanel().repaint();
            return;
        }

        // If the AI has already decided on a move, execute it
        if (executeMove(tetromino)) {
            gameScreen.repaint();
            return;
        }

        // Otherwise, calculate the best move
        Move bestMove = calculateBestMove(tetromino, gameBoard);
        if (bestMove != null) {
            // Apply the best move
            applyMove(tetromino, bestMove);

            // After moving and rotating, perform a hard drop
            hardDrop(tetromino);

            // Place the Tetromino
            gameBoard.placeTetromino(tetromino);
            int cleared = gameBoard.clearLines();
            gameScreen.updateScore(cleared);
            gameScreen.updateLevel();

            // Check if the game is over
            if (gameBoard.isGameOver()) {
                stop();
                SwingUtilities.invokeLater(() -> gameScreen.stopGame());
                return;
            }

            // Set the next Tetromino
            Tetromino newTetromino = gameScreen.getNextTetromino();
            newTetromino.setX(gameBoard.getBoard()[0].length / 2 - newTetromino.getShape()[0].length / 2);
            gameBoard.setCurrentTetromino(newTetromino);

            // Generate new next Tetromino
            gameScreen.setNextTetromino(gameScreen.generateNewTetromino());
            gameScreen.getNextTetrominoPanel().repaint();
        }

        gameScreen.repaint();
    }

    private boolean executeMove(Tetromino tetromino) {
        // This method can be used to execute pre-planned moves step by step if needed
        return false; // Not implemented in this simple AI
    }

    private void applyMove(Tetromino tetromino, Move move) {
        // Rotate to the desired orientation
        int rotationsNeeded = move.rotation - tetromino.getRotation();
        if (rotationsNeeded < 0) rotationsNeeded += 4;
        for (int i = 0; i < rotationsNeeded; i++) {
            tetromino.rotate();
            if (!gameBoard.canMove(tetromino, tetromino.getX(), tetromino.getY())) {
                // Rotate back if invalid
                tetromino.rotate();
                tetromino.rotate();
                tetromino.rotate();
            }
        }

        // Move to the desired X position
        int deltaX = move.x - tetromino.getX();
        while (deltaX != 0) {
            if (deltaX > 0) {
                if (gameBoard.canMove(tetromino, tetromino.getX() + 1, tetromino.getY())) {
                    tetromino.moveRight();
                }
                deltaX--;
            } else {
                if (gameBoard.canMove(tetromino, tetromino.getX() - 1, tetromino.getY())) {
                    tetromino.moveLeft();
                }
                deltaX++;
            }
        }
    }

    private void hardDrop(Tetromino tetromino) {
        // Move the Tetromino down until it can't move further
        while (gameBoard.canMove(tetromino, tetromino.getX(), tetromino.getY() + 1)) {
            tetromino.moveDown();
        }
    }

    private Move calculateBestMove(Tetromino tetromino, GameBoard board) {
        int bestScore = Integer.MIN_VALUE;
        Move bestMove = null;

        // Iterate over all possible rotations
        for (int rotation = 0; rotation < 4; rotation++) {
            Tetromino testTetromino = new Tetromino(tetromino);
            for (int r = 0; r < rotation; r++) {
                testTetromino.rotate();
            }

            // Iterate over all possible positions
            for (int x = -2; x < board.getBoard()[0].length + 2; x++) {
                Tetromino movedTetromino = new Tetromino(testTetromino);
                movedTetromino.setX(x);
                movedTetromino.setY(0);

                // Simulate moving down to the bottom
                if (!board.canMove(movedTetromino, x, 0)) continue;
                while (board.canMove(movedTetromino, movedTetromino.getX(), movedTetromino.getY() + 1)) {
                    movedTetromino.moveDown();
                }

                // Calculate the score of this position
                int score = evaluateBoard(movedTetromino, board);

                // Keep the move with the highest score
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = new Move(x, rotation, score);
                }
            }
        }

        return bestMove;
    }

    private int evaluateBoard(Tetromino tetromino, GameBoard board) {
        // Create a temporary board to simulate the placement
        Color[][] tempBoard = board.cloneBoard();
        GameBoard tempGameBoard = new GameBoard(tempBoard);
        tempGameBoard.placeTetromino(tetromino);

        // Simple heuristic: maximize cleared lines, minimize height
        int linesCleared = tempGameBoard.clearLines();
        int aggregateHeight = getAggregateHeight(tempGameBoard);
        int holes = getNumberOfHoles(tempGameBoard);
        int bumpiness = getBumpiness(tempGameBoard);

        int score = (linesCleared * 1000) - (aggregateHeight * 5) - (holes * 100) - (bumpiness * 5);

        return score;
    }

    // Heuristic functions
    private int getAggregateHeight(GameBoard board) {
        int height = 0;
        for (int x = 0; x < board.getBoard()[0].length; x++) {
            height += getColumnHeight(board, x);
        }
        return height;
    }

    private int getColumnHeight(GameBoard board, int x) {
        for (int y = 0; y < board.getBoard().length; y++) {
            if (board.getBoard()[y][x] != null) {
                return board.getBoard().length - y;
            }
        }
        return 0;
    }

    private int getNumberOfHoles(GameBoard board) {
        int holes = 0;
        for (int x = 0; x < board.getBoard()[0].length; x++) {
            boolean blockFound = false;
            for (int y = 0; y < board.getBoard().length; y++) {
                if (board.getBoard()[y][x] != null) {
                    blockFound = true;
                } else if (blockFound) {
                    holes++;
                }
            }
        }
        return holes;
    }

    private int getBumpiness(GameBoard board) {
        int bumpiness = 0;
        int previousHeight = getColumnHeight(board, 0);
        for (int x = 1; x < board.getBoard()[0].length; x++) {
            int currentHeight = getColumnHeight(board, x);
            bumpiness += Math.abs(currentHeight - previousHeight);
            previousHeight = currentHeight;
        }
        return bumpiness;
    }

    public void stop() {
        isRunning = false;
        if (aiTimer != null) {
            aiTimer.stop();
        }
    }

    // Inner class to represent a potential move
    private static class Move {
        int x;
        int rotation;
        int score;

        Move(int x, int rotation, int score) {
            this.x = x;
            this.rotation = rotation % 4;
            this.score = score;
        }
    }
}
