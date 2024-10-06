package Tetris;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class HighScoreManager {
    private List<HighScore> highScores;
    private static final String FILE_PATH = GameConfig.HIGH_SCORE_FILE_PATH;

    public HighScoreManager() {
        this.highScores = new ArrayList<>();
        loadHighScores();
    }

    // Method to add a new high score
    public void addHighScore(HighScore highScore) {
        highScores.add(highScore);
        Collections.sort(highScores);
        if (highScores.size() > 10) {
            highScores = highScores.subList(0, 10); // Keep only top 10 scores
        }
        saveHighScores();
    }

    // Method to retrieve the high scores
    public List<HighScore> getHighScores() {
        return highScores;
    }

    // Load high scores from JSON file
    private void loadHighScores() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 3) {
                        String playerName = parts[0];
                        int score = Integer.parseInt(parts[1]);
                        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(parts[2]);
                        highScores.add(new HighScore(playerName, score, date));
                    }
                }
                Collections.sort(highScores);
            } catch (Exception e) {
                System.err.println("Error loading high scores: " + e.getMessage());
            }
        }
    }

    // Save high scores to JSON file
    public void saveHighScores() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (HighScore highScore : highScores) {
                writer.write(highScore.getPlayerName() + "," + highScore.getScore() + "," + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(highScore.getDate()));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving high scores: " + e.getMessage());
        }
    }

    // Inner class to represent a High Score
    public static class HighScore implements Comparable<HighScore> {
        private String playerName;
        private int score;
        private Date date;

        public HighScore(String playerName, int score, Date date) {
            this.playerName = playerName;
            this.score = score;
            this.date = date;
        }

        public String getPlayerName() {
            return playerName;
        }

        public int getScore() {
            return score;
        }

        public Date getDate() {
            return date;
        }

        @Override
        public int compareTo(HighScore other) {
            return Integer.compare(other.score, this.score); // Sort in descending order
        }

        @Override
        public String toString() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return playerName + " - " + score + " points on " + dateFormat.format(date);
        }
    }
}
