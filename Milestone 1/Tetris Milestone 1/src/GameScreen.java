import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GameScreen extends JPanel implements KeyListener, ActionListener {
    private JFrame frame;
    private MainMenu mainMenu;
    private GameBoard gameBoard;
    private Timer timer;
    private boolean isPaused;

    public static final Color PURPLE = new Color(128, 0, 128);

    public GameScreen(JFrame frame, MainMenu mainMenu) {
        this.frame = frame;
        this.mainMenu = mainMenu;
        this.gameBoard = new GameBoard(GameConfig.BOARD_HEIGHT, GameConfig.BOARD_WIDTH);
        this.timer = new Timer(GameConfig.TIMER_DELAY, this);
        this.isPaused = false;

        // Set the preferred size to match the game board size
        setPreferredSize(new Dimension(
            GameConfig.BOARD_WIDTH * GameConfig.CELL_SIZE,
            GameConfig.BOARD_HEIGHT * GameConfig.CELL_SIZE
        ));

        setFocusable(true);
        addKeyListener(this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isPaused) {
            moveTetrominoDown();
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGameBoard(g);
    }

    private void drawGameBoard(Graphics g) {
        Color[][] board = gameBoard.getBoard();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != null) {
                    g.setColor(board[i][j]);
                    g.fillRect(j * 30, i * 30, 30, 30);
                    g.setColor(Color.BLACK);
                    g.drawRect(j * 30, i * 30, 30, 30);
                }
            }
        }

        Tetromino tetromino = gameBoard.getCurrentTetromino();
        if (tetromino != null) {
            int[][] shape = tetromino.getShape();
            int x = tetromino.getX();
            int y = tetromino.getY();
            g.setColor(tetromino.getColor()); 
            for (int i = 0; i < shape.length; i++) {
                for (int j = 0; j < shape[i].length; j++) {
                    if (shape[i][j] != 0) {
                        g.fillRect((x + j) * 30, (y + i) * 30, 30, 30);
                        g.setColor(Color.BLACK);
                        g.drawRect((x + j) * 30, (y + i) * 30, 30, 30);
                        g.setColor(tetromino.getColor()); 
                    }
                }
            }
        }
    }

    private void moveTetrominoDown() {
        Tetromino tetromino = gameBoard.getCurrentTetromino();
        if (tetromino != null) {
            if (gameBoard.canMove(tetromino, tetromino.getX(), tetromino.getY() + 1)) {
                tetromino.moveDown();
            } else {
                gameBoard.placeTetromino(tetromino);
                gameBoard.clearLines();
                if (gameBoard.isGameOver()) {
                    timer.stop();
                    JOptionPane.showMessageDialog(this, "Game Over");
                    frame.setContentPane(mainMenu);
                    frame.revalidate();
                    return;
                }
                gameBoard.setCurrentTetromino(generateNewTetromino());
            }
        } else {
            gameBoard.setCurrentTetromino(generateNewTetromino());
        }
    }

    private Tetromino generateNewTetromino() {
        int[][][] shapes = {
            {{1, 1, 1, 1}},
            {{1, 1}, {1, 1}},
            {{0, 1, 0}, {1, 1, 1}},
            {{1, 0, 0}, {1, 1, 1}},
            {{0, 0, 1}, {1, 1, 1}},
            {{0, 1, 1}, {1, 1, 0}},
            {{1, 1, 0}, {0, 1, 1}}
        };

        Color[] colors = {
            Color.CYAN, Color.YELLOW, PURPLE, Color.ORANGE, Color.BLUE, Color.GREEN, Color.RED
        };

        int index = (int) (Math.random() * shapes.length);
        Tetromino tetromino = new Tetromino(shapes[index], colors[index]);
        tetromino.setX(gameBoard.getBoard()[0].length / 2 - tetromino.getShape()[0].length / 2);
        return tetromino;
    }

    @Override
    public void keyPressed(KeyEvent e) {
    Tetromino tetromino = gameBoard.getCurrentTetromino();
    if (e.getKeyCode() == KeyEvent.VK_P) {
        togglePause(); // Use a separate method to toggle pause
    }

    if (tetromino != null && !isPaused) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (gameBoard.canMove(tetromino, tetromino.getX() - 1, tetromino.getY())) {
                    tetromino.moveLeft();
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (gameBoard.canMove(tetromino, tetromino.getX() + 1, tetromino.getY())) {
                    tetromino.moveRight();
                }
                break;
            case KeyEvent.VK_DOWN:
                if (gameBoard.canMove(tetromino, tetromino.getX(), tetromino.getY() + 1)) {
                    tetromino.moveDown();
                }
                break;
            case KeyEvent.VK_UP:
                tetromino.rotate();
                if (!gameBoard.canMove(tetromino, tetromino.getX(), tetromino.getY())) {
                    tetromino.rotate();
                    tetromino.rotate();
                    tetromino.rotate();
                }
                break;
        }
        repaint();
    }
}
private void togglePause() {
    isPaused = !isPaused;
    if (isPaused) {
        timer.stop();
        JOptionPane.showMessageDialog(this, "Game is paused, press P to continue.");
    } else {
        timer.start();
    }
}

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public void stopGame() {
        timer.stop();
        int response = JOptionPane.showConfirmDialog(this, "Are you sure to stop the current game?", "Stop Game", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            frame.setContentPane(mainMenu);
            frame.revalidate();
        } else {
            timer.start();
        }
    }
}
