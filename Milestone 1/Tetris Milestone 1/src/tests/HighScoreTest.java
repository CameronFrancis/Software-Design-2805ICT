package tests;

import Tetris.HighScoreManager;
import java.util.List;

public class HighScoreTest {
    public static void main(String[] args) {
        HighScoreManager highScoreManager = new HighScoreManager();
        List<HighScoreManager.HighScore> highScores = highScoreManager.getHighScores();

        System.out.println("High Scores:");
        for (HighScoreManager.HighScore highScore : highScores) {
            System.out.println("Name: " + highScore.getPlayerName() + ", Score: " + highScore.getScore());
        }
    }
}
