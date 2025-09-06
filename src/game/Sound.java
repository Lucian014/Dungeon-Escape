package game;

import javax.sound.sampled.*;
import java.net.URL;

public class Sound {
    Clip clip;
    URL[] soundURL = new URL[20];
    FloatControl fc;
    int volumeScale = 3;
    float volume;

    public Sound() {
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
            fc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            checkVolume();

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

    public void checkVolume() {

        switch (volumeScale) {
            case 0: volume = -80f; break;
            case 1: volume = -20f; break;
            case 2: volume = -12f; break;
            case 3: volume = -5f; break;
            case 4: volume = 1f; break;
            case 5: volume = 6f; break;
        }
        fc.setValue(volume);
    }
}
