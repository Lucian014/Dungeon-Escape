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
    public String currentDialogue = "";
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

        //PLAY STATE
        if (gamePanel.gameState == gamePanel.playState) {
        }
        //PAUSE STATE
        if (gamePanel.gameState == gamePanel.pauseState) {
            drawPauseScreen();
        }
        //DIALOGUE STATE
        if (gamePanel.gameState == gamePanel.dialogueState) {
            drawDialogueScreen();
        }
    }
    public void drawPauseScreen() {
        String text = "PAUSED";
        int x = getXforCenteredText(text);
        int y = gamePanel.screenHeight / 2;
        graphics2D.drawString(text, x, y);

    }

    public void drawDialogueScreen() {
        //WINDOW
        int x = gamePanel.tileSize * 2;
        int y = gamePanel.tileSize / 2;
        int width = gamePanel.screenWidth - (gamePanel.tileSize * 4);
        int height = gamePanel.tileSize * 4;
        drawSubWindow(x,y,width,height);

        graphics2D.setFont(graphics2D.getFont().deriveFont(Font.PLAIN,32F));
        x += gamePanel.tileSize;
        y += gamePanel.tileSize * 2;

        for(String line : currentDialogue.split("\n")) {
            graphics2D.drawString(line, x, y);
            y += 40;
        }
    }

    public void drawSubWindow(int x, int y, int width, int height) {

        Color color = new Color(0, 0, 0,190);
        graphics2D.setColor(color);
        graphics2D.fillRoundRect(x,y,width,height,35,35);

        color = new Color(255,255,255);
        graphics2D.setColor(color);
        graphics2D.setStroke(new BasicStroke(5));
        graphics2D.drawRoundRect(x+5,y+5,width-10,height-10,25,25);
    }
    public int getXforCenteredText(String text) {

        int length = (int)graphics2D.getFontMetrics().getStringBounds(text, graphics2D).getWidth();
        int x = gamePanel.screenWidth / 2 - length / 2;
        return x;
    }

}
