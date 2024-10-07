package Tetris;
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
import java.util.Date;

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
    private Tetromino nextTetromino;
    private JPanel nextTetrominoPanel;
    private AIPlayer aiPlayer;

    public static final Color PURPLE = new Color(128, 0, 128);

    public GameScreen(JFrame frame, MainMenu mainMenu) {
        this.frame = frame;
        this.mainMenu = mainMenu;
        this.gameBoard = new GameBoard(GameConfig.BOARD_HEIGHT, GameConfig.BOARD_WIDTH);
        this.currentLevel = 1;
        this.rowsCleared = 0;
        this.score = 0;
        this.isPaused = false;
        nextTetromino = generateNewTetromino();

         // Setup AI if enabled
         if (GameConfig.PLAYER_TYPE == "AI") {
            aiPlayer = new AIPlayer(gameBoard, this);
            aiPlayer.start();
        } else {
            // Setup human-controlled gameplay (timer, key listener, etc.)
            this.timer = new Timer(GameConfig.TIMER_DELAY, this);
            this.timer.start();
            setFocusable(true);
            addKeyListener(this);
        }

        // Initialize current Tetromino with nextTetromino
        Tetromino currentTetromino = nextTetromino;
        currentTetromino.setX(gameBoard.getBoard()[0].length / 2 - currentTetromino.getShape()[0].length / 2);
        gameBoard.setCurrentTetromino(currentTetromino);

        // Generate a new next Tetromino
        nextTetromino = generateNewTetromino();

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
        gbc.gridy++;

        // Add next tetromino preview panel
        nextTetrominoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawNextTetromino(g);
            }
        };
        nextTetrominoPanel.setPreferredSize(new Dimension(100, 100));
        // nextTetrominoPanel.setBorder(BorderFactory.createTitledBorder("Next Tetromino"));
        gameInfoPanel.add(nextTetrominoPanel, gbc);
                
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
        
        // Initialize next tetromino
        nextTetromino = generateNewTetromino();

        // Dynamically adjust the panel sizes based on their contents
        revalidate();
        repaint();

        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isPaused && !GameConfig.AI_PLAY) {
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
    
    // Method to draw the next tetromino in the preview panel
    private void drawNextTetromino(Graphics g) {
        if (nextTetromino != null) {
            int[][] shape = nextTetromino.getShape();
            int cellSize = 20; // Size of each cell in the preview
            int panelWidth = nextTetrominoPanel.getWidth();
            int panelHeight = nextTetrominoPanel.getHeight();
            int shapeWidth = shape[0].length * cellSize;
            int shapeHeight = shape.length * cellSize;
            int offsetX = (panelWidth - shapeWidth) / 2;
            int offsetY = (panelHeight - shapeHeight) / 2;
    
            g.setColor(nextTetromino.getColor());
            for (int i = 0; i < shape.length; i++) {
                for (int j = 0; j < shape[i].length; j++) {
                    if (shape[i][j] != 0) {
                        g.fillRect(offsetX + j * cellSize, offsetY + i * cellSize, cellSize, cellSize);
                        g.setColor(Color.BLACK);
                        g.drawRect(offsetX + j * cellSize, offsetY + i * cellSize, cellSize, cellSize);
                        g.setColor(nextTetromino.getColor());
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
                    AudioManager.playGameFinishSound();
                    JOptionPane.showMessageDialog(this, "Game Over");
                
                    // Stop the AI player if it's running
                    if (aiPlayer != null) {
                        aiPlayer.stop();
                    }
                
                    // Prompt to save high score
                    promptToSaveHighScore();
                
                    AudioManager.stopBackgroundMusic();
                    frame.setContentPane(mainMenu);
                    frame.revalidate();
                    return;
                }
                
                // Set current Tetromino to nextTetromino
                Tetromino newTetromino = nextTetromino;
                newTetromino.setX(gameBoard.getBoard()[0].length / 2 - newTetromino.getShape()[0].length / 2);
                gameBoard.setCurrentTetromino(newTetromino);

                // Generate new next Tetromino
                nextTetromino = generateNewTetromino();
                nextTetrominoPanel.repaint(); // Update the preview panel
            }
        } else {
            // First time initialization
            Tetromino newTetromino = nextTetromino;
            newTetromino.setX(gameBoard.getBoard()[0].length / 2 - newTetromino.getShape()[0].length / 2);
            gameBoard.setCurrentTetromino(newTetromino);
    
            // Generate new next Tetromino
            nextTetromino = generateNewTetromino();
            nextTetrominoPanel.repaint(); // Update the preview panel
        }
    }

    protected void updateLevel() {
        // Increase level every 10 rows cleared
        if (rowsCleared >= currentLevel * 10) {
            currentLevel++;
            levelLabel.setText("Current Level: " + currentLevel);

            // Decrease the timer delay to make the game harder
            int newDelay = Math.max(GameConfig.TIMER_DELAY - (currentLevel * 50), 100);
            timer.setDelay(newDelay);
            AudioManager.playLevelUpSound(); // Play level up sound
        }
    }

    protected void updateScore(int rowsCleared) {
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

    public Tetromino generateNewTetromino() {
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
        // tetromino.setX(gameBoard.getBoard()[0].length / 2 - tetromino.getShape()[0].length / 2);
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
                        AudioManager.playMoveSound();
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (gameBoard.canMove(tetromino, tetromino.getX() + 1, tetromino.getY())) {
                        tetromino.moveRight();
                        AudioManager.playMoveSound();
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (gameBoard.canMove(tetromino, tetromino.getX(), tetromino.getY() + 1)) {
                        tetromino.moveDown();
                        AudioManager.playMoveSound();
                    }
                    break;
                case KeyEvent.VK_UP:
                    tetromino.rotate();
                    if (!gameBoard.canMove(tetromino, tetromino.getX(), tetromino.getY())) {
                        tetromino.rotate();
                        tetromino.rotate();
                        tetromino.rotate();
                    } else {
                        AudioManager.playMoveSound();
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
            AudioManager.stopBackgroundMusic();; // Pause background music
            
            JOptionPane.showMessageDialog(this, "Game is paused, press P to continue.");
        } else {
            timer.start();
            AudioManager.playBackgroundMusic(); // Resume background music
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public void stopGame() {
        // Confirm if the user really wants to exit the game
        int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit the game?", "Exit Game", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            timer.stop();
            AudioManager.stopBackgroundMusic(); // Stop background music
    
            // Stop the AI player if it's running
            if (aiPlayer != null) {
                aiPlayer.stop();
            }
    
            // Prompt the user to save their high score
            promptToSaveHighScore();
    
            // Return to the main menu
            frame.setContentPane(mainMenu);
            frame.revalidate();
        } else {
            // If the user doesn't want to exit, resume the game
            timer.start();
            AudioManager.playBackgroundMusic(); // Resume background music
        }
    }
    private void promptToSaveHighScore() {
        String playerName = JOptionPane.showInputDialog(this, "Enter your name to save your score:");
        if (playerName != null && !playerName.trim().isEmpty()) {
            HighScoreManager highScoreManager = new HighScoreManager();
            HighScoreManager.HighScore highScore = new HighScoreManager.HighScore(playerName, score, new Date());
            highScoreManager.addHighScore(highScore);
            highScoreManager.saveHighScores();
            JOptionPane.showMessageDialog(this, "Your score has been saved!");
        }
    }
    public Tetromino getNextTetromino() {
        return nextTetromino;
    }
    
    public void setNextTetromino(Tetromino tetromino) {
        this.nextTetromino = tetromino;
    }

    public JPanel getNextTetrominoPanel() {
        return nextTetrominoPanel;
    }

    
    
    
}