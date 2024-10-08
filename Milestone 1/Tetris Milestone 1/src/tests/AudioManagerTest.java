package tests;

import Tetris.AudioManager;
import Tetris.GameConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AudioManagerTest {

    @Before
    public void setUp() {
        // Enable music and sound effects in GameConfig before each test
        GameConfig.MUSIC_ON = true;
        GameConfig.SOUND_EFFECTS_ON = true;
        AudioManager.enableSoundEffects();
    }

    @After
    public void tearDown() {
        // Reset GameConfig after each test
        AudioManager.stopBackgroundMusic();
        GameConfig.MUSIC_ON = false;
        GameConfig.SOUND_EFFECTS_ON = false;
    }

    @Test
    public void testPlayBackgroundMusic() throws InterruptedException {
        System.out.println("Testing Background Music... Please listen to the music.");
        AudioManager.playBackgroundMusic();
        
        // Sleep to allow the user to listen to the background music
        Thread.sleep(5000);  // 5 seconds

        System.out.println("Did you hear the background music? (Y/N)");
        // Simulate a user input for testing purposes
        // In a real interactive setup, you could get input from the user

        AudioManager.stopBackgroundMusic();
    }

    @Test
    public void testPlayLineEraseSound() throws InterruptedException {
        System.out.println("Testing Line Erase Sound... Please listen to the sound effect.");
        AudioManager.playLineEraseSound();

        // Sleep to allow the user to listen to the sound effect
        Thread.sleep(2000);  // 2 seconds

        System.out.println("Did you hear the line erase sound effect? (Y/N)");
    }

    @Test
    public void testPlayLevelUpSound() throws InterruptedException {
        System.out.println("Testing Level Up Sound... Please listen to the sound effect.");
        AudioManager.playLevelUpSound();

        // Sleep to allow the user to listen to the sound effect
        Thread.sleep(2000);  // 2 seconds

        System.out.println("Did you hear the level-up sound effect? (Y/N)");
    }

    @Test
    public void testPlayGameFinishSound() throws InterruptedException {
        System.out.println("Testing Game Finish Sound... Please listen to the sound effect.");
        AudioManager.playGameFinishSound();

        // Sleep to allow the user to listen to the sound effect
        Thread.sleep(2000);  // 2 seconds

        System.out.println("Did you hear the game finish sound effect? (Y/N)");
    }

    @Test
    public void testPlayMoveSound() throws InterruptedException {
        System.out.println("Testing Move Sound... Please listen to the sound effect.");
        AudioManager.playMoveSound();

        // Sleep to allow the user to listen to the sound effect
        Thread.sleep(2000);  // 2 seconds

        System.out.println("Did you hear the move sound effect? (Y/N)");
    }
}
