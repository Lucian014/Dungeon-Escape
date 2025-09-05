package game;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class Sound {
    Clip clip;
    URL[] soundURL = new URL[10];

    public Sound() {
        soundURL[0] = getClass().getResource("/sounds/MusicTheme.wav");
        soundURL[1] = getClass().getResource("/sounds/coin.wav");
        soundURL[2] = getClass().getResource("/sounds/powerup.wav");
        soundURL[3] = getClass().getResource("/sounds/unlock.wav");
        soundURL[4] = getClass().getResource("/sounds/fanfare.wav");
        soundURL[5] = getClass().getResource("/sounds/hitmonster.wav");
        soundURL[6] = getClass().getResource("/sounds/receivedamage.wav");
        soundURL[7] = getClass().getResource("/sounds/swordswinging.wav");

    }

    public void setFile(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);

            // Original format of the file
            AudioFormat baseFormat = ais.getFormat();

            // Convert to 16-bit PCM (safe for Java Sound)
            AudioFormat decodedFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(),
                    16,                                // force 16-bit
                    baseFormat.getChannels(),
                    baseFormat.getChannels() * 2,      // frame size = channels * 2 bytes (16-bit)
                    baseFormat.getSampleRate(),
                    false                              // little endian
            );

            // Get converted stream
            AudioInputStream decodedAis = AudioSystem.getAudioInputStream(decodedFormat, ais);

            clip = AudioSystem.getClip();
            clip.open(decodedAis);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        clip.start();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        clip.stop();
    }
}
