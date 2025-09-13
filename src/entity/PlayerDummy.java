package entity;

import game.GamePanel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class PlayerDummy extends Entity{

    public static final String npcName = "Player Dummy";
    public BufferedImage sheetRunning;
    public PlayerDummy(GamePanel gamePanel) {
        super(gamePanel);

        name = npcName;

            sheetRunning = loadARGB("/player/player/playerRunning.png");

        getImage();


    }
    public void getImage() {
        up1    = cut(sheetRunning,  0,  0, 16, 16, 1, 1);
        up2    = cut(sheetRunning, 16,  0, 16, 16, 1, 1);

        down1  = cut(sheetRunning, 32, 16, 16, 16, 1, 1);
        down2  = cut(sheetRunning, 48, 16, 16, 16, 1, 1);

        left1  = cut(sheetRunning, 16, 16, 16, 16, 1, 1);
        left2  = cut(sheetRunning, 48,  0, 16, 16, 1, 1);

        right1 = cut(sheetRunning, 32,  0, 16, 16, 1, 1);
        right2 = cut(sheetRunning,  0, 16, 16, 16, 1, 1);
    }
}
