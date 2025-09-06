package game;

import javax.sound.sampled.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Sound {
    private static Sound instance;

    // Store multiple clips for different sounds
    private final Map<Integer, Clip> clips = new HashMap<>();
    private final URL[] soundURL = new URL[20];
    private final Map<Integer, FloatControl> volumeControls = new HashMap<>();

    // Separate volume controls for music and sound effects
    private int musicVolumeScale = 3;
    private int seVolumeScale = 3;
    private float musicVolume;
    private float seVolume;

    // Private constructor to prevent external instantiation
    private Sound() {
        initializeSoundURLs();
        updateMusicVolume(); // Initialize volume levels
        updateSEVolume();
    }

    // Global access point - thread-safe singleton
    public static synchronized Sound getInstance() {
        if (instance == null) {
            instance = new Sound();
        }
        return instance;
    }

    private void initializeSoundURLs() {
        soundURL[0] = getClass().getResource("/sounds/MusicTheme.wav");
        soundURL[1] = getClass().getResource("/sounds/coin.wav");
        soundURL[2] = getClass().getResource("/sounds/powerup.wav");
        soundURL[3] = getClass().getResource("/sounds/unlock.wav");
        soundURL[4] = getClass().getResource("/sounds/fanfare.wav");
        soundURL[5] = getClass().getResource("/sounds/hitmonster.wav");
        soundURL[6] = getClass().getResource("/sounds/receivedamage.wav");
        soundURL[7] = getClass().getResource("/sounds/swordswinging.wav");
        soundURL[8] = getClass().getResource("/sounds/levelup.wav");
        soundURL[9] = getClass().getResource("/sounds/cursor.wav");
        soundURL[10] = getClass().getResource("/sounds/drinkpotion.wav");
        soundURL[11] = getClass().getResource("/sounds/fireball.wav");
        soundURL[12] = getClass().getResource("/sounds/cuttree.wav");
    }

    public void setFile(int i) {
        try {
            if (soundURL[i] == null) {
                System.err.println("Sound URL not found for index: " + i);
                return;
            }

            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);

            // Original format of the file
            AudioFormat baseFormat = ais.getFormat();

            // Convert to 16-bit PCM (safe for Java Sound)
            AudioFormat decodedFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(),
                    16,
                    baseFormat.getChannels(),
                    baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(),
                    false
            );

            // Get converted stream
            AudioInputStream decodedAis = AudioSystem.getAudioInputStream(decodedFormat, ais);

            // Close existing clip if it exists
            if (clips.containsKey(i)) {
                clips.get(i).close();
            }

            Clip clip = AudioSystem.getClip();
            clip.open(decodedAis);
            clips.put(i, clip);

            FloatControl fc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControls.put(i, fc);

            // Set the correct volume based on whether it's music or sound effect
            if (i == 0) { // Assuming index 0 is music
                fc.setValue(musicVolume);
            } else { // All others are sound effects
                fc.setValue(seVolume);
            }

        } catch (Exception e) {
            System.err.println("Error loading sound file for index " + i + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Play specific sound by index
    public void playSoundEffect(int i) {
        if (!clips.containsKey(i)) {
            setFile(i);
        }

        if (clips.containsKey(i)) {
            Clip clip = clips.get(i);
            if (clip.isRunning()) {
                clip.stop();
            }

            // Ensure correct volume is set before playing
            if (volumeControls.containsKey(i)) {
                volumeControls.get(i).setValue(seVolume);
            }

            clip.setFramePosition(0);
            clip.start();
        }
    }

    // Play music (looping)
    public void playMusic(int i) {
        if (!clips.containsKey(i)) {
            setFile(i);
        }

        if (clips.containsKey(i)) {
            Clip clip = clips.get(i);
            if (clip.isRunning()) {
                clip.stop();
            }

            // Ensure correct volume is set before playing
            if (volumeControls.containsKey(i)) {
                volumeControls.get(i).setValue(musicVolume);
            }

            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void setMusicVolumeScale(int scale) {
        this.musicVolumeScale = Math.max(0, Math.min(5, scale));
        updateMusicVolume();
    }

    public void setSEVolumeScale(int scale) {
        this.seVolumeScale = Math.max(0, Math.min(5, scale));
        updateSEVolume();
    }

    public int getMusicVolumeScale() {
        return musicVolumeScale;
    }

    public int getSEVolumeScale() {
        return seVolumeScale;
    }

    private void updateMusicVolume() {
        switch (musicVolumeScale) {
            case 0: musicVolume = -80f; break;
            case 1: musicVolume = -20f; break;
            case 2: musicVolume = -12f; break;
            case 3: musicVolume = -5f; break;
            case 4: musicVolume = 1f; break;
            case 5: musicVolume = 6f; break;
        }

        // Apply to music clip (index 0)
        if (volumeControls.containsKey(0)) {
            volumeControls.get(0).setValue(musicVolume);
        }
    }

    private void updateSEVolume() {
        switch (seVolumeScale) {
            case 0: seVolume = -80f; break;
            case 1: seVolume = -20f; break;
            case 2: seVolume = -12f; break;
            case 3: seVolume = -5f; break;
            case 4: seVolume = 1f; break;
            case 5: seVolume = 6f; break;
        }

        // Apply to all sound effect clips (indices 1-12)
        for (int i = 1; i <= 12; i++) {
            if (volumeControls.containsKey(i)) {
                volumeControls.get(i).setValue(seVolume);
            }
        }
    }

    public void stopMusic() {
        for (Clip clip : clips.values()) {
            if (clip != null && clip.isRunning()) {
                clip.stop();
            }
        }
    }


}