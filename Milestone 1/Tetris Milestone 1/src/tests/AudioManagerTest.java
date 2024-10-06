package tests;

import Tetris.AudioManager;
import Tetris.GameConfig;

public class AudioManagerTest {
    public static void main(String[] args) throws InterruptedException {
        // Enable music and sound effects
        GameConfig.MUSIC_ON = true;
        GameConfig.SOUND_EFFECTS_ON = true;

        // Enable sound effects within AudioManager
        AudioManager.enableSoundEffects();

        // Test Background Music
        System.out.println("Testing Background Music...");
        AudioManager.playBackgroundMusic();

        // Sleep to allow background music to play
        Thread.sleep(5000);

        AudioManager.stopBackgroundMusic();

        // Test Line Erase Sound Effect
        System.out.println("Testing Line Erase Sound...");
        AudioManager.playLineEraseSound();
        Thread.sleep(2000);

        // Test Level Up Sound Effect
        System.out.println("Testing Level Up Sound...");
        AudioManager.playLevelUpSound();
        Thread.sleep(2000);

        // Test Game Finish Sound Effect
        System.out.println("Testing Game Finish Sound...");
        AudioManager.playGameFinishSound();
        Thread.sleep(2000);

        // Test Move Sound Effect
        System.out.println("Testing Move Sound...");
        AudioManager.playMoveSound();
        Thread.sleep(2000);
    }
}
