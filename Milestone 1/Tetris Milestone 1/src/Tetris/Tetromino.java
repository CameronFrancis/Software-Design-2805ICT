package Tetris;
import java.awt.Color;

public class Tetromino {
    private int[][] shape;
    private Color color;
    private int x, y;
    private int rotation; // Add this field to track rotation state

    public Tetromino(int[][] shape, Color color) {
        this.shape = shape;
        this.color = color;
        this.x = 0;
        this.y = 0;
        this.rotation = 0; // Initialize rotation
    }

    public int[][] getShape() {
        return shape;
    }

    public Color getColor() {
        return color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void moveLeft() {
        x--;
    }

    public void moveRight() {
        x++;
    }

    public void moveDown() {
        y++;
    }

    public void rotate() {
        int rows = shape.length;
        int cols = shape[0].length;
        int[][] newShape = new int[cols][rows]; // Swap rows and columns for rotation

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                newShape[j][rows - 1 - i] = shape[i][j];
            }
        }

        shape = newShape;
        rotation = (rotation + 1) % 4; // Update rotation state
    }

    // Copy constructor
    public Tetromino(Tetromino other) {
        this.shape = deepCopyShape(other.shape);
        this.color = other.color;
        this.x = other.x;
        this.y = other.y;
        this.rotation = other.rotation; // Copy rotation state
    }

    private int[][] deepCopyShape(int[][] original) {
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone();
        }
        return copy;
    }

    public int getRotation() {
        return rotation % 4;
    }
}
