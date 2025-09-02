package game;

import object.OBJ_Chest;
import object.OBJ_Key;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UI {
    GamePanel gamePanel;
    Font arial_40, arial_80;
    BufferedImage keyImage;
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;
    public boolean gameFinished = false;

    public UI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        OBJ_Key key = new OBJ_Key();
        keyImage = key.image;
        arial_80 = new Font("Arial", Font.BOLD, 80);
    }

    public void showMessage(String text) {
        message = text;
        messageOn = true;
    }

    public void draw(Graphics2D graphics2D){

        if(gameFinished){

            graphics2D.setFont(arial_40);
            graphics2D.setColor(Color.WHITE);

            String text = "You found the treasure";
            int textLength = (int)graphics2D.getFontMetrics().getStringBounds(text, graphics2D).getWidth();
            int x = gamePanel.screenWidth / 2 - textLength / 2;
            int y = gamePanel.screenHeight / 2 - (gamePanel.tileSize * 3);
            graphics2D.drawString(text, x, y);

            graphics2D.setFont(arial_80);
            graphics2D.setColor(Color.YELLOW);
            text = "Congratulations!";
            textLength = (int)graphics2D.getFontMetrics().getStringBounds(text, graphics2D).getWidth();
            x = gamePanel.screenWidth / 2 - textLength / 2;
            y = gamePanel.screenHeight / 2 + (gamePanel.tileSize * 2);
            graphics2D.drawString("Congratulations!" , x, y);

            gamePanel.gameThread = null;

        }else {
            graphics2D.setFont(arial_40);
            graphics2D.setColor(Color.WHITE);
            graphics2D.drawImage(keyImage, gamePanel.tileSize / 2, gamePanel.tileSize /2, gamePanel.tileSize, gamePanel.tileSize, null);
            graphics2D.drawString("x "+ gamePanel.player.hasKey,74,60);

            if(messageOn) {
                graphics2D.setFont(graphics2D.getFont().deriveFont(30F));
                graphics2D.drawString(message, gamePanel.tileSize / 2, gamePanel.tileSize * 5);

                messageCounter++;

                if(messageCounter > 120) {
                    messageCounter = 0;
                    messageOn = false;
                }
            }
        }
        }
}
