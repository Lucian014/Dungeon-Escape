    package game;

    import object.OBJ_Chest;
    import object.OBJ_Heart;
    import object.OBJ_Key;
    import entity.Entity;
    import object.OBJ_ManaCrystal;

    import java.awt.*;
    import java.awt.image.BufferedImage;
    import java.io.IOException;
    import java.io.InputStream;
    import java.util.ArrayList;

    public class UI {
    GamePanel gamePanel;
    Graphics2D graphics2D;
    Font maruMonica, purisaBold;
    BufferedImage heart_full, heart_half, heart_blank, crystal_full, crystal_blank;
    public boolean messageOn = false;
    ArrayList<String> message = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();
    public boolean gameFinished = false;
    public String currentDialogue = "";
    public int commandNum = 0;
    public int titleScreenState = 0; // 0: the first screen, 1: 2nd screen
    public int slotCol = 0;
    public int slotRow = 0;

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
        Entity crystal = new OBJ_ManaCrystal(gamePanel);
        crystal_full = crystal.image;
        crystal_blank = crystal.image2;
    }
    public void addMessage(String text) {

        message.add(text);
        messageCounter.add(0);
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
        if (gamePanel.gameState == gamePanel.playState) {
            drawPlayerLife();
            drawMessage();
            drawFPS();

        }
        //DIALOGUE STATE
        if (gamePanel.gameState == gamePanel.dialogueState) {
            drawPlayerLife();
            drawDialogueScreen();
        }
        //PAUSE STATE
        if (gamePanel.gameState == gamePanel.pauseState) {
            drawPlayerLife();
            drawPauseScreen(graphics2D);
        }
        // CHARACTER STATE
        if (gamePanel.gameState == gamePanel.characterState) {
            drawCharacterScreen();
            drawInventory();
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
    public void drawCharacterScreen() {
        // Subwindow frame
        final int frameX = gamePanel.tileSize * 2;
        final int frameY = gamePanel.tileSize;
        final int frameWidth = gamePanel.tileSize * 6;
        final int frameHeight = gamePanel.tileSize * 10;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // Text setup
        graphics2D.setColor(Color.WHITE);
        graphics2D.setFont(graphics2D.getFont().deriveFont(28F));
        final int paddingX = 40;
        final int lineHeight = 32;

        int textX = frameX + paddingX;
        int textY = frameY + gamePanel.tileSize;
        int tailX = frameX + frameWidth - paddingX;

        // Draw regular stats
        String[][] regularStats = {
                {"Level", String.valueOf(gamePanel.player.level)},
                {"Life", gamePanel.player.life + "/" + gamePanel.player.maxLife},
                {"Mana", gamePanel.player.mana + "/" + gamePanel.player.maxMana},
                {"Strength", String.valueOf(gamePanel.player.strength)},
                {"Dexterity", String.valueOf(gamePanel.player.dexterity)},
                {"Attack", String.valueOf(gamePanel.player.attack)},
                {"Defense", String.valueOf(gamePanel.player.defense)},
                {"Exp", String.valueOf(gamePanel.player.exp)},
                {"Next Level", String.valueOf(gamePanel.player.nextLevelExp)},
                {"Coin", String.valueOf(gamePanel.player.coin)}
        };

        for (String[] stat : regularStats) {
            graphics2D.drawString(stat[0], textX, textY);
            int valueX = getXforAllignToRight(stat[1], tailX);
            graphics2D.drawString(stat[1], valueX, textY);
            textY += lineHeight;
        }

        // Draw Weapon - moved lower and more to the right
        textY += 10;
        graphics2D.drawString("Weapon", textX, textY);
        if (gamePanel.player.currentWeapon != null) {
            // Adjust these values to move the image lower and more to the right
            int weaponX = tailX - gamePanel.tileSize + 20; // Move 20 pixels more to the right
            int weaponY = textY - gamePanel.tileSize + 15; // Move 25 pixels lower

            graphics2D.drawImage(gamePanel.player.currentWeapon.down1,
                    weaponX, weaponY,
                    gamePanel.tileSize, gamePanel.tileSize, null);
        }
        textY += gamePanel.tileSize + 5 ; // Extra space for image

        // Draw Shield - moved lower and more to the right
        graphics2D.drawString("Shield", textX, textY);
        if (gamePanel.player.currentShield != null) {
            // Adjust these values to move the image lower and more to the right
            int shieldX = tailX - gamePanel.tileSize + 20; // Move 20 pixels more to the right
            int shieldY = textY - gamePanel.tileSize + 15; // Move 25 pixels lower

            graphics2D.drawImage(gamePanel.player.currentShield.down1,
                    shieldX, shieldY,
                    gamePanel.tileSize, gamePanel.tileSize, null);
        }
    }
    public void drawMessage() {

            int messageX = gamePanel.tileSize;
            int messageY = gamePanel.screenHeight / 2;
            graphics2D.setFont(graphics2D.getFont().deriveFont(Font.BOLD, 32F));

            for(int i =0; i < message.size(); i++) {

                if(message.get(i) != null) {

                    graphics2D.setColor(Color.BLACK);
                    graphics2D.drawString(message.get(i), messageX + 2, messageY + 2);
                    graphics2D.setColor(Color.WHITE);
                    graphics2D.drawString(message.get(i), messageX, messageY);

                    int counter = messageCounter.get(i) + 1;
                    messageCounter.set(i,counter);
                    messageY += 50;

                    if(messageCounter.get(i) > 120 ) {
                        message.remove(i);
                        messageCounter.remove(i);
                    }
                }
            }

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
    public void drawInventory() {
        // FRAME
        int frameX = gamePanel.tileSize * 11;
        int frameY = gamePanel.tileSize + 25;
        int frameWidth = gamePanel.tileSize * 6;
        int frameHeight = gamePanel.tileSize * 5;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // SLOT
        final int slotXstart = frameX + 25;
        final int slotYstart = frameY + 25;
        int slotX = slotXstart;
        int slotY = slotYstart;
        int slotSize = gamePanel.tileSize ;

        // Grid dimensions
        final int columns = 5;

        // DRAW PLAYER'S ITEMS (only up to maxSlots)
        for(int i = 0; i < gamePanel.player.inventory.size(); i++) {
            Entity item = gamePanel.player.inventory.get(i);

            if(gamePanel.player.inventory.get(i) == gamePanel.player.currentWeapon || gamePanel.player.inventory.get(i) == gamePanel.player.currentShield) {
                graphics2D.setColor(new Color(240,190,90));
                graphics2D.fillRoundRect(slotX,slotY,gamePanel.tileSize,gamePanel.tileSize, 10, 10);
            }

            // Draw item image with proper scaling
            if (item != null && item.down1 != null) {
                graphics2D.drawImage(item.down1, slotX, slotY, slotSize, slotSize, null);
            } else {
                // Draw empty slot or debug indicator
                graphics2D.setColor(Color.GRAY);
                graphics2D.fillRect(slotX, slotY, slotSize, slotSize);
            }

            // Move to next column
            slotX += slotSize;

            // Move to next row when we reach the end of a row
            if ((i + 1) % columns == 0) {
                slotX = slotXstart;
                slotY += slotSize;
            }
        }

        // CURSOR
        int cursorX = slotXstart + (slotSize * slotCol);
        int cursorY = slotYstart + (slotSize * slotRow);

        // DRAW CURSOR
        graphics2D.setColor(Color.WHITE);
        graphics2D.setStroke(new BasicStroke(3));
        graphics2D.drawRoundRect(cursorX, cursorY, slotSize, slotSize, 10, 10);

        // DESCRIPTION FRAME

        int dFrameX = frameX;
        int dFrameY = frameY + frameHeight + 30;
        int dFrameWidth = frameWidth;
        int dFrameHeight = gamePanel.tileSize * 3;
        //DRAW DESCRIPTION TEXT
        int textX = dFrameX + 20;
        int textY = dFrameY + gamePanel.tileSize;
        graphics2D.setFont(graphics2D.getFont().deriveFont(20F));

        int itemIndex = getItemIndexOnSlot();

        if(itemIndex < gamePanel.player.inventory.size()) {

            drawSubWindow(dFrameX,dFrameY,dFrameWidth,dFrameHeight);

            for(String line: gamePanel.player.inventory.get(itemIndex).description.split("\n")){
                graphics2D.drawString(line,textX,textY);
                textY += 32;
            }
        }
    }
    public int getItemIndexOnSlot() {
        return slotCol + (slotRow * 5);
    }

    public void drawPlayerLife() {
        int x = gamePanel.tileSize / 2;
        int y = gamePanel.tileSize / 2;

        // --- DRAW MAX LIFE (empty hearts) ---
        for (int i = 0; i < gamePanel.player.maxLife / 2; i++) {
            graphics2D.drawImage(heart_blank, x, y, null);
            x += gamePanel.tileSize;
        }

        // --- DRAW CURRENT LIFE ---
        x = gamePanel.tileSize / 2;
        int remainingLife = gamePanel.player.life;
        for (int i = 0; i < gamePanel.player.maxLife / 2; i++) {
            if (remainingLife >= 2) {
                graphics2D.drawImage(heart_full, x, y, null);
                remainingLife -= 2;
            } else if (remainingLife == 1) {
                graphics2D.drawImage(heart_half, x, y, null);
                remainingLife -= 1;
            }
            x += gamePanel.tileSize;
        }

        // --- DRAW MANA ---
        x = gamePanel.tileSize / 2;
        y += gamePanel.tileSize + 6; // a bit below hearts

        // Max mana (empty crystals)
        for (int i = 0; i < gamePanel.player.maxMana; i++) {
            graphics2D.drawImage(crystal_blank, x, y, gamePanel.tileSize, gamePanel.tileSize, null);
            x += gamePanel.tileSize;
        }

        // Current mana (full crystals)
        x = gamePanel.tileSize / 2;
        for (int i = 0; i < gamePanel.player.mana; i++) {
            graphics2D.drawImage(crystal_full, x, y, gamePanel.tileSize, gamePanel.tileSize, null);
            x += gamePanel.tileSize;
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
    public int getXforAllignToRight(String text, int tailX) {

        int length = (int) graphics2D.getFontMetrics().getStringBounds(text, graphics2D).getWidth();
        int x = tailX - length;
        return x;
    }

    public void drawFPS() {
        graphics2D.setColor(Color.WHITE);
        graphics2D.setFont(graphics2D.getFont().deriveFont(32F));
        graphics2D.drawString("FPS: " + gamePanel.fpsCount, gamePanel.screenWidth - gamePanel.tileSize * 3, gamePanel.tileSize);

    }
}