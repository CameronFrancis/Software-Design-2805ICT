public class AudioManagerTest {
    public static void main(String[] args) {
        // Enable sound effects
        AudioManager.enableSoundEffects();

        // Test Background Music
        System.out.println("Testing Background Music...");
        AudioManager.playBackgroundMusic();
        
        try {
            // Sleep for 10 seconds to allow background music to play
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        AudioManager.stopBackgroundMusic();

        // Test Line Erase Sound Effect
        System.out.println("Testing Line Erase Sound...");
        AudioManager.playLineEraseSound();

        // Test Level Up Sound Effect
        System.out.println("Testing Level Up Sound...");
        AudioManager.playLevelUpSound();

        // Test Game Finish Sound Effect
        System.out.println("Testing Game Finish Sound...");
        AudioManager.playGameFinishSound();

        // Test Move Sound Effect
        System.out.println("Testing Move Sound...");
        AudioManager.playMoveSound();
    }
}
