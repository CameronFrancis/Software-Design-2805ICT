import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioManager {
    private static Clip backgroundClip;
    private static boolean soundEffectsEnabled = true;

    // Play Background Music (WAV format)
    public static void playBackgroundMusic() {
        if (GameConfig.MUSIC_ON) {
            try {
                if (backgroundClip != null && backgroundClip.isRunning()) {
                    backgroundClip.stop();
                }
                // Use the converted WAV file
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(AudioManager.class.getResourceAsStream(GameConfig.BACKGROUND_MUSIC_PATH));
                backgroundClip = AudioSystem.getClip();
                backgroundClip.open(audioInputStream);
                backgroundClip.loop(Clip.LOOP_CONTINUOUSLY); // Loop the music
                backgroundClip.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }
    }

    // Stop Background Music
    public static void stopBackgroundMusic() {
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
        }
    }

    // Play sound effect (WAV format)
    public static void playSoundEffect(String filePath) {
        if (GameConfig.SOUND_EFFECTS_ON && soundEffectsEnabled) {
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(AudioManager.class.getResourceAsStream(filePath));
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }
    }

    // Play Line Erase Sound Effect
    public static void playLineEraseSound() {
        playSoundEffect(GameConfig.ERASE_LINE_SOUND_PATH);
    }

    // Play Level Up Sound Effect
    public static void playLevelUpSound() {
        playSoundEffect(GameConfig.LEVEL_UP_SOUND_PATH);
    }

    // Play Game Finish Sound Effect
    public static void playGameFinishSound() {
        playSoundEffect(GameConfig.GAME_FINISH_SOUND_PATH);
    }

    // Play Move Sound Effect
    public static void playMoveSound() {
        playSoundEffect(GameConfig.MOVE_TURN_SOUND_PATH);
    }

    // Enable sound effects
    public static void enableSoundEffects() {
        soundEffectsEnabled = true;
    }

    // Disable sound effects
    public static void disableSoundEffects() {
        soundEffectsEnabled = false;
    }
}