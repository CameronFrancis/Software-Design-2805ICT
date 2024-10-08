package Tetris;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;

public class HighScoreManager {
    private List<HighScore> highScores;
    private String filePath;
    private static final String DEFAULT_FILE_PATH = GameConfig.HIGH_SCORE_FILE_PATH;

    // Default constructor uses the default file path
    public HighScoreManager() {
        this(DEFAULT_FILE_PATH);
    }

    // Constructor that accepts a file path for testing or custom files
    public HighScoreManager(String filePath) {
        this.filePath = filePath;
        this.highScores = new ArrayList<>();
        loadHighScores();
    }

    // Add a new high score
    public void addHighScore(HighScore highScore) {
        highScores.add(highScore);
        Collections.sort(highScores);  // Sort by score descending
        saveHighScores();  // Save after adding
    }

    public List<HighScore> getHighScores() {
        return highScores;
    }

    // Clear the high scores
    public void clearHighScores() {
        highScores.clear();
        saveHighScores();  // Save the empty list to the file
    }

    // Load the high scores from the file using Gson
    public void loadHighScores() {
        File file = new File(filePath);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                Gson gson = new Gson();
                Type highScoreListType = new TypeToken<List<HighScore>>(){}.getType();
                highScores = gson.fromJson(reader, highScoreListType);  // Load the list from JSON
                if (highScores == null) {
                    highScores = new ArrayList<>();  // If the file is empty, initialize an empty list
                }
            } catch (IOException e) {
                System.err.println("Error loading high scores: " + e.getMessage());
            }
        } else {
            highScores = new ArrayList<>();  // If no file exists, start with an empty list
        }
    }

    // Save the high scores to the file using Gson
    private void saveHighScores() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            writer.write(gson.toJson(highScores));
        } catch (IOException e) {
            System.err.println("Error saving high scores: " + e.getMessage());
        }
    }

    // Inner class for HighScore objects
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
            return Integer.compare(other.score, this.score);  // Sort by score descending
        }
    }
}

