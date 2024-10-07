package Tetris;

import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.awt.Color;

public class AIPlayer implements Runnable {
    private GameBoard gameBoard;
    private GameScreen gameScreen;
    private boolean isRunning;
    private Timer aiTimer;
    private Queue<Runnable> actionQueue;
    private Move currentMove;

    public AIPlayer(GameBoard gameBoard, GameScreen gameScreen) {
        this.gameBoard = gameBoard;
        this.gameScreen = gameScreen;
        this.isRunning = true;
        this.actionQueue = new LinkedList<>();
    }

    // Start the AI player in a separate thread
    public void start() {
        Thread aiThread = new Thread(this);
        aiThread.start();
    }

    @Override
    public void run() {
        // Adjust the delay to control the AI's speed (e.g., 200 milliseconds)
        aiTimer = new Timer(200, e -> makeMove());
        aiTimer.start();
    }

    private void makeMove() {
        if (!isRunning) return;

        // If there are actions in the queue, execute them step by step
        if (!actionQueue.isEmpty()) {
            actionQueue.poll().run();
            gameScreen.repaint();
        } else {
            // If the action queue is empty, plan the next move
            planNextMove();
        }
    }

    private void planNextMove() {
        Tetromino tetromino = gameBoard.getCurrentTetromino();

        // If there's no current tetromino, initialize one
        if (tetromino == null) {
            tetromino = gameScreen.getNextTetromino();
            tetromino.setX(gameBoard.getBoard()[0].length / 2 - tetromino.getShape()[0].length / 2);
            gameBoard.setCurrentTetromino(tetromino);
            // Generate new next Tetromino
            gameScreen.setNextTetromino(gameScreen.generateNewTetromino());
            gameScreen.getNextTetrominoPanel().repaint();
        }

        // Calculate the best move
        currentMove = calculateBestMove(tetromino, gameBoard);

        if (currentMove != null) {
            // Populate the action queue with movements
            enqueueActions(tetromino, currentMove);
        } else {
            // No valid move found, end the game
            stop();
            SwingUtilities.invokeLater(() -> gameScreen.stopGame());
        }
    }

    private void enqueueActions(Tetromino tetromino, Move move) {
        // Calculate rotations needed
        int rotationsNeeded = (move.rotation - tetromino.getRotation() + 4) % 4;
        for (int i = 0; i < rotationsNeeded; i++) {
            actionQueue.add(() -> gameScreen.rotateTetromino());
        }

        // Calculate horizontal movements needed
        int deltaX = move.x - tetromino.getX();
        Runnable moveAction = deltaX > 0 ? () -> gameScreen.moveTetrominoRight() : () -> gameScreen.moveTetrominoLeft();
        for (int i = 0; i < Math.abs(deltaX); i++) {
            actionQueue.add(moveAction);
        }

        // Add hard drop action
        actionQueue.add(() -> {
            gameScreen.hardDropTetromino();
            // After hard drop, handle placement and prepare for the next tetromino
            SwingUtilities.invokeLater(this::handleTetrominoPlacement);
        });
    }

    private void handleTetrominoPlacement() {
        // Place the Tetromino
        gameBoard.placeTetromino(gameBoard.getCurrentTetromino());
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

        // Plan the move for the new tetromino
        planNextMove();
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

            // Iterate over all possible horizontal positions
            int minX = -testTetromino.getShape()[0].length;
            int maxX = board.getBoard()[0].length;

            for (int x = minX; x < maxX; x++) {
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
    
    public void pause() {
        if (aiTimer != null) {
            aiTimer.stop();
        }
    }

    public void resume() {
        if (aiTimer != null) {
            aiTimer.start();
        }
    }
}
