import java.awt.Color;

public class GameBoard {
    private int rows;
    private int cols;
    private Color[][] board;
    private Tetromino currentTetromino;

    public GameBoard(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.board = new Color[rows][cols];
    }

    public boolean canMove(Tetromino tetromino, int newX, int newY) {
        int[][] shape = tetromino.getShape();
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] != 0) {
                    int x = newX + j;
                    int y = newY + i;
                    if (x < 0 || x >= cols || y >= rows || (y >= 0 && board[y][x] != null)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void placeTetromino(Tetromino tetromino) {
        int[][] shape = tetromino.getShape();
        int x = tetromino.getX();
        int y = tetromino.getY();
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] != 0 && y + i >= 0) {
                    board[y + i][x + j] = tetromino.getColor();
                }
            }
        }
    }

    public void clearLines() {
        for (int i = 0; i < rows; i++) {
            boolean fullLine = true;
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == null) {
                    fullLine = false;
                    break;
                }
            }
            if (fullLine) {
                removeLine(i);
            }
        }
    }

    private void removeLine(int line) {
        for (int i = line; i > 0; i--) {
            System.arraycopy(board[i - 1], 0, board[i], 0, cols);
        }
        for (int j = 0; j < cols; j++) {
            board[0][j] = null;
        }
    }

    public boolean isGameOver() {
        for (int i = 0; i < cols; i++) {
            if (board[0][i] != null) {
                return true;
            }
        }
        return false;
    }

    public void setCurrentTetromino(Tetromino tetromino) {
        this.currentTetromino = tetromino;
    }

    public Tetromino getCurrentTetromino() {
        return currentTetromino;
    }

    public Color[][] getBoard() {
        return board;
    }
}
