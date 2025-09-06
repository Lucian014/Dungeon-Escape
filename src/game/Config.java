package game;

import java.awt.image.BufferedImage;
import java.io.*;

public class Config {

    GamePanel gamePanel;

    public Config(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

    }

    public void saveConfig() {

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("config.txt"));

            //FULLSCREEN
            if(gamePanel.fullScreenOn) {
                bufferedWriter.write("On");
            }
            if(!gamePanel.fullScreenOn) {
                bufferedWriter.write("Off");
            }
            bufferedWriter.newLine();

            //Music Volume
            bufferedWriter.write(String.valueOf(gamePanel.sound.getMusicVolumeScale()));
            bufferedWriter.newLine();
            bufferedWriter.write(String.valueOf(gamePanel.sound.getSEVolumeScale()));
            bufferedWriter.newLine();
            bufferedWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void loadConfig() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("config.txt"));

            String s = bufferedReader.readLine();

            //Fullscreen
            gamePanel.fullScreenOn = s.equals("On");

            //Music volume
            s = bufferedReader.readLine();
            gamePanel.sound.setMusicVolumeScale(Integer.parseInt(s));

            s = bufferedReader.readLine();
            gamePanel.sound.setSEVolumeScale(Integer.parseInt(s));

            bufferedReader.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
