package game;

import object.OBJ_Chest;
import object.OBJ_Key;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UI {
    GamePanel gamePanel;
    Graphics2D graphics2D;

    Font arial_40, arial_80;
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;
    public boolean gameFinished = false;

    public UI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_80 = new Font("Arial", Font.BOLD, 80);
    }

    public void showMessage(String text) {
        message = text;
        messageOn = true;
    }

    public void draw(Graphics2D graphics2D) {
        this.graphics2D = graphics2D;
        graphics2D.setFont(arial_40);
        graphics2D.setColor(Color.WHITE);
        if (gamePanel.gameState == gamePanel.playState) {
        }
        if (gamePanel.gameState == gamePanel.pauseState) {
            drawPauseScreen();
        }
    }
    public void drawPauseScreen() {
        String text = "PAUSED";
        int x = getXforCenteredText(text);
        int y = gamePanel.screenHeight / 2;
        graphics2D.drawString(text, x, y);

    }

    public int getXforCenteredText(String text) {

        int length = (int)graphics2D.getFontMetrics().getStringBounds(text, graphics2D).getWidth();
        int x = gamePanel.screenWidth / 2 - length / 2;
        return x;
    }
}
