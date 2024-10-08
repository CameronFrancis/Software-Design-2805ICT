package tests;

import Tetris.HighScoreManager;
import Tetris.HighScoreManager.HighScore;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class HighScoreTest {
    private HighScoreManager highScoreManager;
    private static final String TEST_FILE_PATH = "test_high_scores.json";

    @Before
    public void setUp() {
        // Set up a HighScoreManager with a test file path
        System.out.println("Setting up the HighScoreManager with a test file: " + TEST_FILE_PATH);
        highScoreManager = new HighScoreManager(TEST_FILE_PATH);
        highScoreManager.clearHighScores();
        System.out.println("High scores cleared. Starting with an empty list.");
    }

    // No need to delete the file in tearDown anymore
    @After
    public void tearDown() {
        System.out.println("Test finished.");
    }

    @Test
    public void testAddHighScore() {
        System.out.println("Starting test: testAddHighScore");
        
        // Add a high score
        HighScore highScore = new HighScore("TestPlayer", 500, new Date());
        System.out.println("Adding a high score for TestPlayer with 500 points.");
        highScoreManager.addHighScore(highScore);
        
        // Check if the high score was added
        List<HighScore> highScores = highScoreManager.getHighScores();
        System.out.println("High score list size: " + highScores.size());
        assertEquals(1, highScores.size());
        
        System.out.println("Checking the details of the added high score...");
        assertEquals("TestPlayer", highScores.get(0).getPlayerName());
        assertEquals(500, highScores.get(0).getScore());

        System.out.println("testAddHighScore completed successfully.\n");
    }

    @Test
    public void testClearHighScores() {
        System.out.println("Starting test: testClearHighScores");

        // Add two high scores
        HighScore highScore1 = new HighScore("Player1", 1000, new Date());
        HighScore highScore2 = new HighScore("Player2", 800, new Date());
        System.out.println("Adding two high scores: Player1 (1000 points) and Player2 (800 points).");
        highScoreManager.addHighScore(highScore1);
        highScoreManager.addHighScore(highScore2);

        // Clear high scores
        System.out.println("Clearing all high scores.");
        highScoreManager.clearHighScores();

        // Check if the high scores were cleared
        List<HighScore> highScores = highScoreManager.getHighScores();
        System.out.println("High score list size after clearing: " + highScores.size());
        assertEquals(0, highScores.size());

        System.out.println("testClearHighScores completed successfully.\n");
    }

    @Test
    public void testHighScoresPersistence() {
        System.out.println("Starting test: testHighScoresPersistence");

        // Add a high score
        HighScore highScore = new HighScore("PersistentPlayer", 750, new Date());
        System.out.println("Adding a high score for PersistentPlayer with 750 points.");
        highScoreManager.addHighScore(highScore);

        // Create a new HighScoreManager instance to simulate restarting the application
        System.out.println("Simulating application restart by creating a new HighScoreManager instance.");
        HighScoreManager newHighScoreManager = new HighScoreManager(TEST_FILE_PATH);

        // Check if the high score was persisted
        List<HighScore> highScores = newHighScoreManager.getHighScores();
        System.out.println("High score list size after reloading: " + highScores.size());
        assertEquals(1, highScores.size());

        System.out.println("Checking the details of the persisted high score...");
        assertEquals("PersistentPlayer", highScores.get(0).getPlayerName());
        assertEquals(750, highScores.get(0).getScore());

        System.out.println("testHighScoresPersistence completed successfully.\n");
    }
}
