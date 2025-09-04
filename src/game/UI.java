package game;

import object.OBJ_Chest;
import object.OBJ_Heart;
import object.OBJ_Key;
import entity.Entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class UI {
    GamePanel gamePanel;
    Graphics2D graphics2D;
    Font maruMonica, purisaBold;
    BufferedImage heart_full, heart_half, heart_blank;
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;
    public boolean gameFinished = false;
    public String currentDialogue = "";
    public int commandNum = 0;
    public int titleScreenState = 0; // 0: the first screen, 1: 2nd screen


    public UI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        try {
            InputStream inputStream = getClass().getResourceAsStream("/fonts/x12y16pxMaruMonica.ttf");
            maruMonica = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            inputStream = getClass().getResourceAsStream("/fonts/Purisa Bold.ttf");
            purisaBold = Font.createFont(Font.TRUETYPE_FONT, inputStream);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

        //CREATE HUG OBJECT
        Entity heart = new OBJ_Heart(gamePanel);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;
    }

    public void showMessage(String text) {
        message = text;
        messageOn = true;
    }

    public void draw(Graphics2D graphics2D) {
        this.graphics2D = graphics2D;
        graphics2D.setFont(maruMonica);
        graphics2D.setColor(Color.WHITE);

        //TITLE STATE
        if (gamePanel.gameState == gamePanel.titleState) {
            drawTitleScreen();
        }
        //PLAY STATE - just draw regular UI elements
        else if (gamePanel.gameState == gamePanel.playState) {

            drawPlayerLife();
        }
        //DIALOGUE STATE
        else if (gamePanel.gameState == gamePanel.dialogueState) {
            drawPlayerLife();
            drawDialogueScreen();
        }
    }

    public void drawPauseScreen(Graphics2D graphics2D) {
        // Use the passed graphics2D parameter, not the class field graphics2D
        // Semi-transparent overlay
        Color overlay = new Color(0, 0, 0, 180);
        graphics2D.setColor(overlay);
        graphics2D.fillRect(0, 0, gamePanel.screenWidth, gamePanel.screenHeight);

        // PAUSED text
        String text = "PAUSED";
        graphics2D.setFont(graphics2D.getFont().deriveFont(Font.BOLD, 64F));
        int x = getXforCenteredText(text, graphics2D);
        int y = gamePanel.screenHeight / 2 - gamePanel.tileSize;


        // Shadow
        graphics2D.setColor(Color.BLACK);
        graphics2D.drawString(text, x + 4, y + 4);

        // Main text
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString(text, x, y);
    }

    public void drawDialogueScreen() {
        //WINDOW
        int x = gamePanel.tileSize * 2;
        int y = gamePanel.tileSize / 2;
        int width = gamePanel.screenWidth - (gamePanel.tileSize * 4);
        int height = gamePanel.tileSize * 4;
        drawSubWindow(x, y, width, height);

        graphics2D.setFont(graphics2D.getFont().deriveFont(Font.PLAIN, 32F));
        x += gamePanel.tileSize;
        y += gamePanel.tileSize * 2;

        for (String line : currentDialogue.split("\n")) {
            graphics2D.drawString(line, x, y);
            y += 40;
        }
    }

    public void drawSubWindow(int x, int y, int width, int height) {

        Color color = new Color(0, 0, 0, 190);
        graphics2D.setColor(color);
        graphics2D.fillRoundRect(x, y, width, height, 35, 35);

        color = new Color(255, 255, 255);
        graphics2D.setColor(color);
        graphics2D.setStroke(new BasicStroke(5));
        graphics2D.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    public int getXforCenteredText(String text) {

        int length = (int) graphics2D.getFontMetrics().getStringBounds(text, graphics2D).getWidth();
        int x = gamePanel.screenWidth / 2 - length / 2;
        return x;
    }

    public int getXforCenteredText(String text, Graphics2D graphics2D) {


        int length = (int) graphics2D.getFontMetrics().getStringBounds(text, graphics2D).getWidth();
        int x = gamePanel.screenWidth / 2 - length / 2;
        return x;
    }

    public void drawTitleScreen() {

        if (titleScreenState == 0) {
            graphics2D.setColor(new Color(100, 163, 232));
            graphics2D.fillRect(0, 0, gamePanel.screenWidth, gamePanel.screenHeight);

            //TITLE NAME
            graphics2D.setFont(graphics2D.getFont().deriveFont(Font.BOLD, 96F));
            String text = "Dungeon Escape";
            int x = getXforCenteredText(text);
            int y = gamePanel.screenHeight / 2 - gamePanel.tileSize * 3;

            //SHADOW
            graphics2D.setColor(Color.black);
            graphics2D.drawString(text, x + 6, y + 6);

            //MAIN COLOR
            graphics2D.setColor(Color.WHITE);
            graphics2D.drawString(text, x, y);

            //TITLE CARD
            x = gamePanel.screenWidth / 2 - gamePanel.tileSize;
            y += gamePanel.tileSize;
            graphics2D.drawImage(gamePanel.player.down1, x, y, gamePanel.tileSize * 2, gamePanel.tileSize * 2, null);

            //MENU
            graphics2D.setFont(graphics2D.getFont().deriveFont(Font.BOLD, 40F));

            text = "NEW GAME";
            x = getXforCenteredText(text);
            y += gamePanel.tileSize * 3;
            graphics2D.drawString(text, x, y);
            if (commandNum == 0) {
                graphics2D.drawString(">", x - gamePanel.tileSize, y);
            }

            text = "LOAD GAME";
            x = getXforCenteredText(text);
            y += gamePanel.tileSize;
            graphics2D.drawString(text, x, y);
            if (commandNum == 1) {
                graphics2D.drawString(">", x - gamePanel.tileSize, y);
            }

            text = "SETTINGS";
            x = getXforCenteredText(text);
            y += gamePanel.tileSize;
            graphics2D.drawString(text, x, y);
            if (commandNum == 2) {
                graphics2D.drawString(">", x - gamePanel.tileSize, y);
            }

            text = "QUIT GAME";
            x = getXforCenteredText(text);
            y += gamePanel.tileSize;
            graphics2D.drawString(text, x, y);
            if (commandNum == 3) {
                graphics2D.drawString(">", x - gamePanel.tileSize, y);
            }
        } else if (titleScreenState == 1) {

            graphics2D.setColor(Color.WHITE);
            graphics2D.setFont(graphics2D.getFont().deriveFont(42f));

            String text = "Select your class: ";
            int x = getXforCenteredText(text);
            int y = gamePanel.tileSize * 3;
            graphics2D.drawString(text, x, y);

            text = "Fighter";
            x = getXforCenteredText(text);
            y += gamePanel.tileSize * 2;
            graphics2D.drawString(text, x, y);
            if (commandNum == 0) {
                graphics2D.drawString(">", x - gamePanel.tileSize, y);
            }

            text = "Thief";
            x = getXforCenteredText(text);
            y += gamePanel.tileSize;
            graphics2D.drawString(text, x, y);
            if (commandNum == 1) {
                graphics2D.drawString(">", x - gamePanel.tileSize, y);
            }

            text = "Wizard";
            x = getXforCenteredText(text);
            y += gamePanel.tileSize;
            graphics2D.drawString(text, x, y);
            if (commandNum == 2) {
                graphics2D.drawString(">", x - gamePanel.tileSize, y);
            }

            text = "Back";
            x = getXforCenteredText(text);
            y += gamePanel.tileSize * 2;
            graphics2D.drawString(text, x, y);
            if (commandNum == 3) {
                graphics2D.drawString(">", x - gamePanel.tileSize, y);
            }
        }
    }

    public void drawPlayerLife() {

        int x = gamePanel.tileSize / 2;
        int y = gamePanel.tileSize / 2;
        int i = 0;

        //DRAW MAX LIFE
        while (i < gamePanel.player.maxLife / 2) {
            graphics2D.drawImage(heart_blank, x, y, null);
            i++;
            x += gamePanel.tileSize;
        }

        //RESET
        x = gamePanel.tileSize / 2;
        y = gamePanel.tileSize / 2;
        i = 0;

        //DRAW CURRENT LIFE
        while (i < gamePanel.player.life) {
            graphics2D.drawImage(heart_half, x, y, null);
            i++;
            if (i < gamePanel.player.life) {
                graphics2D.drawImage(heart_full, x, y, null);
            }
            i++;
            x += gamePanel.tileSize;
        }
    }
}