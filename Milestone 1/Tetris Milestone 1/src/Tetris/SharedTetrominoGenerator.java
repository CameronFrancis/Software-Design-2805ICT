package Tetris;

import java.awt.Color;
import java.util.LinkedList;
import java.util.Queue;

public class SharedTetrominoGenerator {
    private Queue<Tetromino> tetrominoQueue;

    public SharedTetrominoGenerator() {
        tetrominoQueue = new LinkedList<>();
    }

    public Tetromino getNextTetromino() {
        if (tetrominoQueue.isEmpty()) {
            generateTetrominoes(10); // Generate 10 more tetrominoes when the queue is empty
        }
        return tetrominoQueue.poll();
    }

    private void generateTetrominoes(int count) {
        for (int i = 0; i < count; i++) {
            tetrominoQueue.add(generateNewTetromino());
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
                Color.CYAN, Color.YELLOW, GameScreen.PURPLE, Color.ORANGE, Color.BLUE, Color.GREEN, Color.RED
        };
        int index = (int) (Math.random() * shapes.length);
        Tetromino tetromino = new Tetromino(shapes[index], colors[index]);
        return tetromino;
    }
}
