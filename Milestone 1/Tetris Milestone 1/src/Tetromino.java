import java.awt.Color;

public class Tetromino {
    private int[][] shape;
    private Color color;
    private int x, y;

    public Tetromino(int[][] shape, Color color) {
        this.shape = shape;
        this.color = color;
        this.x = 0;
        this.y = 0;
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
        int size = shape.length;
        int[][] newShape = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                newShape[j][size - 1 - i] = shape[i][j];
            }
        }

        shape = newShape;
    }
}
