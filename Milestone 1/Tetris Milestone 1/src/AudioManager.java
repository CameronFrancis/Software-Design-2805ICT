import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioManager {
    private static Clip backgroundClip;
    private static boolean soundEffectsEnabled = true;

    // Play Background Music (WAV format)
    public static void playBackgroundMusic() {
        try {
            if (backgroundClip != null && backgroundClip.isRunning()) {
                backgroundClip.stop();
            }
            // Use the converted WAV file
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(AudioManager.class.getResourceAsStream("/resources/background.wav"));
            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(audioInputStream);
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY); // Loop the music
            backgroundClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
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
        if (soundEffectsEnabled) {
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }
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
