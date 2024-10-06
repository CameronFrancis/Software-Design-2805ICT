import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GameScreen extends JPanel implements KeyListener, ActionListener {
    private JFrame frame;
    private MainMenu mainMenu;
    private GameBoard gameBoard;
    private Timer timer;
    private boolean isPaused;
    private int currentLevel;
    private int rowsCleared;
    private int score;
    private JLabel levelLabel;
    private JLabel scoreLabel;
    private JLabel linesClearedLabel;
    private JLabel nextTetrominoLabel;

    public static final Color PURPLE = new Color(128, 0, 128);

    public GameScreen(JFrame frame, MainMenu mainMenu) {
        this.frame = frame;
        this.mainMenu = mainMenu;
        this.gameBoard = new GameBoard(GameConfig.BOARD_HEIGHT, GameConfig.BOARD_WIDTH);
        this.currentLevel = 1;
        this.rowsCleared = 0;
        this.score = 0;
        this.isPaused = false;

        // Set up the timer, starting with a default delay
        this.timer = new Timer(GameConfig.TIMER_DELAY, this);

        setLayout(new BorderLayout());

        // Set the preferred size of the game screen
        setPreferredSize(new Dimension(
            GameConfig.BOARD_WIDTH * GameConfig.CELL_SIZE + 300, // Additional space for info panel
            GameConfig.BOARD_HEIGHT * GameConfig.CELL_SIZE + 100 // Additional space for button panel
        ));
        setFocusable(true);
        addKeyListener(this);

        // Setting up the game board panel and its border
        JPanel gameBoardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int offsetX = (getWidth() - GameConfig.BOARD_WIDTH * GameConfig.CELL_SIZE) / 2;
                int offsetY = (getHeight() - GameConfig.BOARD_HEIGHT * GameConfig.CELL_SIZE) / 2;
                drawGameBoard(g, offsetX, offsetY);
                // Draw border around the actual game board
                g.setColor(Color.BLACK);
                g.drawRect(offsetX, offsetY, GameConfig.BOARD_WIDTH * GameConfig.CELL_SIZE, GameConfig.BOARD_HEIGHT * GameConfig.CELL_SIZE);
            }
        };
        gameBoardPanel.setPreferredSize(new Dimension(
            GameConfig.BOARD_WIDTH * GameConfig.CELL_SIZE,
            GameConfig.BOARD_HEIGHT * GameConfig.CELL_SIZE
        ));
        gameBoardPanel.setLayout(new GridBagLayout()); // Center the game board
        add(gameBoardPanel, BorderLayout.CENTER);

        // Add game info panel to the left
        JPanel gameInfoPanel = new JPanel(new GridBagLayout());
        gameInfoPanel.setPreferredSize(new Dimension(200, GameConfig.BOARD_HEIGHT * GameConfig.CELL_SIZE));
        gameInfoPanel.setBackground(Color.LIGHT_GRAY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new java.awt.Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Add game info labels to the info panel
        gameInfoPanel.add(new JLabel("Game Info (Player 1)"), gbc);
        gbc.gridy++;
        gameInfoPanel.add(new JLabel("Player Type: AI"), gbc);
        gbc.gridy++;
        gameInfoPanel.add(new JLabel("Initial Level: " + currentLevel), gbc);
        gbc.gridy++;
        levelLabel = new JLabel("Current Level: " + currentLevel);
        gameInfoPanel.add(levelLabel, gbc);
        gbc.gridy++;
        linesClearedLabel = new JLabel("Line Erased: " + rowsCleared);
        gameInfoPanel.add(linesClearedLabel, gbc);
        gbc.gridy++;
        scoreLabel = new JLabel("Score: " + score);
        gameInfoPanel.add(scoreLabel, gbc);
        gbc.gridy++;
        nextTetrominoLabel = new JLabel("Next Tetromino:");
        gameInfoPanel.add(nextTetrominoLabel, gbc);
        add(gameInfoPanel, BorderLayout.WEST);

        // Add status panel at the top for music and sound status
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel musicStatusLabel = new JLabel("Music: OFF");
        JLabel soundStatusLabel = new JLabel("Sound: OFF");
        statusPanel.add(musicStatusLabel);
        statusPanel.add(soundStatusLabel);
        add(statusPanel, BorderLayout.NORTH);

        // Add end game button at the bottom
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton endGameButton = new JButton("End Game");
        endGameButton.addActionListener(e -> stopGame());
        buttonPanel.add(endGameButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Dynamically adjust the panel sizes based on their contents
        revalidate();
        repaint();

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
    }

    private void drawGameBoard(Graphics g, int offsetX, int offsetY) {
        Color[][] board = gameBoard.getBoard();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != null) {
                    g.setColor(board[i][j]);
                    g.fillRect(offsetX + j * GameConfig.CELL_SIZE, offsetY + i * GameConfig.CELL_SIZE, GameConfig.CELL_SIZE, GameConfig.CELL_SIZE);
                    g.setColor(Color.BLACK);
                    g.drawRect(offsetX + j * GameConfig.CELL_SIZE, offsetY + i * GameConfig.CELL_SIZE, GameConfig.CELL_SIZE, GameConfig.CELL_SIZE);
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
                        g.fillRect(offsetX + (x + j) * GameConfig.CELL_SIZE, offsetY + (y + i) * GameConfig.CELL_SIZE, GameConfig.CELL_SIZE, GameConfig.CELL_SIZE);
                        g.setColor(Color.BLACK);
                        g.drawRect(offsetX + (x + j) * GameConfig.CELL_SIZE, offsetY + (y + i) * GameConfig.CELL_SIZE, GameConfig.CELL_SIZE, GameConfig.CELL_SIZE);
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
                int cleared = gameBoard.clearLines(); // Update rows cleared after placing
                rowsCleared += cleared;
                linesClearedLabel.setText("Line Erased: " + rowsCleared);
                updateScore(cleared); // Update the score based on rows cleared
                updateLevel(); // Check if level needs to be updated
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

    private void updateLevel() {
        // Increase level every 10 rows cleared
        if (rowsCleared >= currentLevel * 10) {
            currentLevel++;
            levelLabel.setText("Current Level: " + currentLevel);

            // Decrease the timer delay to make the game harder
            int newDelay = Math.max(GameConfig.TIMER_DELAY - (currentLevel * 50), 100);
            timer.setDelay(newDelay);
        }
    }

    private void updateScore(int rowsCleared) {
        // Update score based on rows cleared
        switch (rowsCleared) {
            case 1:
                score += 100;
                break;
            case 2:
                score += 300;
                break;
            case 3:
                score += 600;
                break;
            case 4:
                score += 1000;
                break;
        }
        scoreLabel.setText("Score: " + score);
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
            togglePause();
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