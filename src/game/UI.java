    package game;

    import object.*;
    import entity.Entity;

    import java.awt.*;
    import java.awt.image.BufferedImage;
    import java.io.IOException;
    import java.io.InputStream;
    import java.util.ArrayList;

    public class UI {
    GamePanel gamePanel;
    Graphics2D graphics2D;
    public Font maruMonica, purisaBold;
    BufferedImage heart_full, heart_half, heart_blank, crystal_full, crystal_blank, coin;
    public boolean messageOn = false;
    ArrayList<String> message = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();
    public boolean gameFinished = false;
    public String currentDialogue = "";
    public int commandNum = 0;
    public int titleScreenState = 0; // 0: the first screen, 1: 2nd screen
    public int playerSlotCol = 0;
    public int playerSlotRow = 0;
    public int npcSlotCol = 0;
    public int npcSlotRow = 0;
    public int charIndex = 0;
    int subState = 0;
    int counter = 0;
    private float counterDeath = 0;
    public String combinedText = "";
    public Entity npc;
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
        Entity coin_bronze = new OBJ_Coin_Bronze(gamePanel);
        coin = coin_bronze.down1;
        charIndex = 0;
        combinedText = "";
        currentDialogue = "";
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
            drawMonsterLife();
        }
        //DIALOGUE STATE
        if (gamePanel.gameState == gamePanel.dialogueState) {
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
            drawInventory(gamePanel.player, true);
        }

        //OPTION STATE
        if(gamePanel.gameState == gamePanel.optionsState) {
            drawOptionsScreen();
        }

        //GAME OVER STATE
        if(gamePanel.gameState == gamePanel.gameOverState) {
            drawGameOverScreen();
        }

        //TRANSITION STATE
        if(gamePanel.gameState == gamePanel.transitionState) {
            drawTransitionScreen();
        }

        //TRADE STATE
        if(gamePanel.gameState == gamePanel.tradeState) {
            drawTradeScreen();
        }

        //SLEEP STATE
        if(gamePanel.gameState == gamePanel.sleepState) {
            drawSleepScreen();
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
        int x = gamePanel.tileSize / 2;
        int y = gamePanel.tileSize / 2;
        int width = gamePanel.screenWidth - (gamePanel.tileSize * 6);
        int height = gamePanel.tileSize * 4;
        drawSubWindow(x, y, width, height);

        graphics2D.setFont(graphics2D.getFont().deriveFont(Font.PLAIN, 32F));
        x += gamePanel.tileSize;
        y += gamePanel.tileSize;

        if(npc.dialogues[npc.dialogueSet][npc.dialogueIndex] != null) {

           // currentDialogue = npc.dialogues[npc.dialogueSet][npc.dialogueIndex];

            char[] characters = npc.dialogues[npc.dialogueSet][npc.dialogueIndex].toCharArray();

            if(charIndex < characters.length) {

                gamePanel.sound.playSoundEffect(22);
                String s = String.valueOf(characters[charIndex]);
                combinedText = combinedText + s;
                currentDialogue = combinedText;
                charIndex++;
            }

            if(gamePanel.keyHandler.enterPressed) {

                charIndex = 0;
                combinedText = "";

                if(gamePanel.gameState == gamePanel.dialogueState || gamePanel.gameState == gamePanel.cutsceneState) {

                    npc.dialogueIndex++;
                    gamePanel.keyHandler.enterPressed = false;
                }
            }
        }
        else { // If no text is in the array

            npc.dialogueIndex = 0;

            if(gamePanel.gameState == gamePanel.dialogueState) {
                gamePanel.gameState = gamePanel.playState;
            }
            if(gamePanel.gameState == gamePanel.cutsceneState) {
                gamePanel.cutsceneManager.scenePhase++;
            }
        }

        for(String line : currentDialogue.split("\n")) {
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
    public void drawOptionsScreen() {

        graphics2D.setColor(Color.WHITE);
        graphics2D.setFont(graphics2D.getFont().deriveFont(32F));

        int frameX = gamePanel.tileSize * 6;
        int frameY = gamePanel.tileSize ;
        int frameWidth = gamePanel.tileSize * 8;
        int frameHeight = gamePanel.tileSize * 10;
        drawSubWindow(frameX,frameY,frameWidth,frameHeight);

        switch (subState) {
            case 0: options_top(frameX,frameY); break;
            case 1: options_fullScreenNotification(frameX,frameY); break;
            case 2: options_displayControls(frameX,frameY); break;
            case 3: options_quitGameConfirmation(frameX, frameY);

        }
        gamePanel.keyHandler.enterPressed = false;
    }
    public void drawGameOverScreen() {
        counterDeath++;
        graphics2D.setColor(new Color(0,0,0, 223));
        graphics2D.fillRect(0,0,gamePanel.screenWidth,gamePanel.screenHeight);

        int x;
        int y;
        String text;
        graphics2D.setFont(graphics2D.getFont().deriveFont(Font.BOLD,110f));
        text = "You Died";
        //SHADOW
        graphics2D.setColor(Color.BLACK);
        x = getXforCenteredText(text,graphics2D);
        y = gamePanel.screenHeight / 2;
        graphics2D.drawString(text, x, y);
        //MAIN
        if((int)counterDeath >= 255){
            counterDeath = 255;
        } else {
            counterDeath += 0.5F;
        }

        graphics2D.setColor(new Color(139, 0, 0, (int)counterDeath));
        graphics2D.drawString(text, x - 4, y - 4);

        //Retry
        graphics2D.setFont(graphics2D.getFont().deriveFont(50f));
        text = "Retry";
        x = getXforCenteredText(text);
        y += gamePanel.tileSize * 2;
        graphics2D.drawString(text,x,y);
        if(commandNum == 0) {
            graphics2D.drawString(">", x - 40, y);
        }
        //Back to the title screen
        text = "Quit";
        x = getXforCenteredText(text);
        y += 55;
        graphics2D.drawString(text,x,y);
        if(commandNum == 1) {
            graphics2D.drawString(">", x - 40, y);
        }

        //Back to title field
    }
    public void drawTransitionScreen() {
            counter++;
            // Fade out (0-25 frames) then fade in (26-50 frames)
            if (counter <= 25) {
                // Fade to black
                int alpha = Math.min(counter * 10, 255);
                graphics2D.setColor(new Color(0, 0, 0, alpha));
                graphics2D.fillRect(0, 0, gamePanel.screenWidth, gamePanel.screenHeight);
            } else if (counter <= 50) {
                // Still black screen, perform teleport at midpoint
                if (counter == 26) {
                    // Perform the actual teleport when screen is fully black
                    gamePanel.currentMap = gamePanel.eventHandler.tempMap;
                    gamePanel.player.worldX = gamePanel.tileSize * gamePanel.eventHandler.tempCol;
                    gamePanel.player.worldY = gamePanel.tileSize * gamePanel.eventHandler.tempRow;
                }

                // Fade in from black
                int alpha = Math.max(255 - ((counter - 25) * 10), 0);
                graphics2D.setColor(new Color(0, 0, 0, alpha));
                graphics2D.fillRect(0, 0, gamePanel.screenWidth, gamePanel.screenHeight);
                gamePanel.changeArea();
            } else {
                // Transition complete
                counter = 0;
                gamePanel.gameState = gamePanel.playState;
            }
        }
    public void drawTradeScreen(){
            switch (subState) {
                case 0: trade_select(); break;
                case 1: trade_buy(); break;
                case 2: trade_sell(); break;
            }
            gamePanel.keyHandler.enterPressed = false;
    }
    public void drawSleepScreen(){

        counter++;
        if(counter < 120) {
            gamePanel.manager.lighting.filterAlpha += 0.01f;
            if(gamePanel.manager.lighting.filterAlpha > 1f) {
                gamePanel.manager.lighting.filterAlpha = 1f;
            }
        }
        if(counter >= 120) {
            gamePanel.manager.lighting.filterAlpha -= 0.01f;
            if(gamePanel.manager.lighting.filterAlpha <= 0f) {
                gamePanel.manager.lighting.filterAlpha = 0f;
                counter = 0;
                gamePanel.manager.lighting.dayState = gamePanel.manager.lighting.day;
                gamePanel.gameState = gamePanel.playState;
                gamePanel.player.getImage();
            }
        }
    }
    public void drawInventory(Entity entity, boolean cursor) {

        int frameX = 0;
        int frameY = 0;
        int frameWidth = gamePanel.tileSize * 6;
        int frameHeight = gamePanel.tileSize * 5;
        int slotCol = 0;
        int slotRow = 0;

        if(entity == gamePanel.player) {
            frameX = gamePanel.tileSize * 11;
            frameY = gamePanel.tileSize + 25;
            slotCol = playerSlotCol;
            slotRow = playerSlotRow;
        }
        else {
            frameX = gamePanel.tileSize * 2;
            frameY = gamePanel.tileSize + 25;
            slotCol = npcSlotCol;
            slotRow = npcSlotRow;
        }

        //FRAME
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
        for(int i = 0; i < entity.inventory.size(); i++) {
            Entity item = entity.inventory.get(i);
            if(item == entity.currentWeapon || item == entity.currentShield || item == entity.currentLight) {
                graphics2D.setColor(new Color(240,190,90));
                graphics2D.fillRoundRect(slotX,slotY,gamePanel.tileSize,gamePanel.tileSize, 10, 10);
            }
            // Draw item image with proper scaling
            if (item != null && item.down1 != null) {
                graphics2D.drawImage(item.down1, slotX, slotY, slotSize, slotSize, null);

                if(item.amount > 1 && entity == gamePanel.player) {
                    graphics2D.setFont(graphics2D.getFont().deriveFont(Font.BOLD, 32F));
                    int amountX;
                    int amountY;

                    String s = "" + item.amount;
                    amountX = getXforAllignToRight(s, slotX + 44);
                    amountY = slotY + gamePanel.tileSize;

                    //SHADOW
                    graphics2D.setColor(new Color(60,60,60));
                    graphics2D.drawString(s, amountX, amountY);

                    //NUMBER
                    graphics2D.setColor(Color.WHITE);
                    graphics2D.drawString(s, amountX - 3, amountY - 3);
                }
            } else {
                // Draw an empty slot
                graphics2D.setColor(Color.GRAY);
                graphics2D.fillRect(slotX, slotY, slotSize, slotSize);
            }

            // Move to the next column
            slotX += slotSize;

            // Move to the next row when we reach the end of a row
            if ((i + 1) % columns == 0) {
                slotX = slotXstart;
                slotY += slotSize;
            }
        }
        // CURSOR
        if(cursor){
            int cursorX = slotXstart + (slotSize * slotCol);
            int cursorY = slotYstart + (slotSize * slotRow);

            // DRAW CURSOR
            graphics2D.setColor(Color.WHITE);
            graphics2D.setStroke(new BasicStroke(3));
            graphics2D.drawRoundRect(cursorX, cursorY, slotSize, slotSize, 10, 10);

            // DESCRIPTION FRAME

            int dFrameX = frameX;
            int dFrameY = frameY + frameHeight + 10;
            int dFrameWidth = frameWidth;
            int dFrameHeight = gamePanel.tileSize * 3;
            //DRAW DESCRIPTION TEXT
            int textX = dFrameX + 20;
            int textY = dFrameY + gamePanel.tileSize;
            graphics2D.setFont(graphics2D.getFont().deriveFont(25F));

            int itemIndex = getItemIndexOnSlot(slotCol,slotRow);

            if(itemIndex < entity.inventory.size()) {

                drawSubWindow(dFrameX,dFrameY,dFrameWidth,dFrameHeight);

                for(String line: entity.inventory.get(itemIndex).description.split("\n")){
                    graphics2D.drawString(line,textX,textY);
                    textY += 32;
                }
            }
        }
    }

    public void drawPlayerLife() {
            // Smaller size: 24x24 (adjust as needed, e.g., 16 for tinier)
            int heartSize = gamePanel.tileSize / 2 + 10;  // Hearts and crystals same size for consistency
            int startX = gamePanel.tileSize / 2;  // Left margin: 24
            int startY = gamePanel.tileSize / 2;  // Top margin: 24
            int heartsPerRow = 8;  // Fixed: 8 hearts across

            // Calculate heart positions once (for both max and current)
            int numHearts = gamePanel.player.maxLife / 2;
            int[][] heartPositions = new int[numHearts][2];  // [x, y] for each heart
            int currentX = startX;
            int currentY = startY;
            for (int i = 0; i < numHearts; i++) {
                heartPositions[i][0] = currentX;
                heartPositions[i][1] = currentY;
                currentX += heartSize;
                if ((i + 1) % heartsPerRow == 0) {  // After drawing a full row, move to next
                    currentX = startX;
                    currentY += heartSize;  // Full row height (no half-tile weirdness)
                }
            }

            // --- DRAW MAX LIFE (empty hearts) ---
            for (int i = 0; i < numHearts; i++) {
                int hx = heartPositions[i][0];
                int hy = heartPositions[i][1];
                graphics2D.drawImage(heart_blank, hx, hy, heartSize, heartSize, null);
            }

            // --- DRAW CURRENT LIFE (overlay full/half on the same positions) ---
            int remainingLife = gamePanel.player.life;
            for (int i = 0; i < numHearts; i++) {
                int hx = heartPositions[i][0];
                int hy = heartPositions[i][1];
                if (remainingLife >= 2) {
                    graphics2D.drawImage(heart_full, hx, hy, heartSize, heartSize, null);
                    remainingLife -= 2;
                } else if (remainingLife == 1) {
                    graphics2D.drawImage(heart_half, hx, hy, heartSize, heartSize, null);
                    remainingLife -= 1;
                }
                // If remainingLife == 0, it stays blank (already drawn)
            }

            // --- DRAW MANA (below hearts) ---
            int manaStartY = currentY + 33;  // Just below last heart row + small gap
            int manaPerRow = gamePanel.player.maxMana;  // For now, 10; wrap if more
            if (manaPerRow > 10) manaPerRow = 10;  // Optional: Limit row to 10 for layout

            currentX = startX - 1;
            currentY = manaStartY;
            int numCrystals = gamePanel.player.maxMana;

            // Max mana (empty crystals)
            for (int i = 0; i < numCrystals; i++) {
                graphics2D.drawImage(crystal_blank, currentX, currentY, heartSize, heartSize, null);
                currentX += heartSize;
                if ((i + 1) % manaPerRow == 0) {
                    currentX = startX;
                    currentY += heartSize;
                }
            }

            // Current mana (overlay full crystals)
            remainingLife = gamePanel.player.mana;  // Reuse var, or use 'currentMana'
            currentX = startX - 1;
            currentY = manaStartY;
            for (int i = 0; i < gamePanel.player.mana; i++) {
                graphics2D.drawImage(crystal_full, currentX, currentY, heartSize, heartSize, null);
                currentX += heartSize;
                if ((i + 1) % manaPerRow == 0) {
                    currentX = startX;
                    currentY += heartSize;
                }
            }
        }

    public void drawMonsterLife() {
        // === Monster HP bar ===
        for(int i = 0; i < gamePanel.monster[1].length; i++) {
            Entity monster = gamePanel.monster[gamePanel.currentMap][i];

            if(monster != null) {
                // Regular monsters: Only if in camera and hpBarOn
                if (!monster.boss && monster.inCamera()) {
                    if (monster.hpBarOn) {
                        double oneScale = (double) gamePanel.tileSize / monster.maxLife;
                        double hpBarValue = oneScale * monster.life;

                        graphics2D.setColor(new Color(35, 35, 35));
                        graphics2D.fillRect(monster.getScreenX() - 1, monster.getScreenY() - 16, gamePanel.tileSize + 2, 12);

                        graphics2D.setColor(new Color(185, 185, 185));
                        graphics2D.fillRect(monster.getScreenX(), monster.getScreenY() - 15, gamePanel.tileSize, 10);

                        graphics2D.setColor(new Color(255, 0, 30));
                        graphics2D.fillRect(monster.getScreenX(), monster.getScreenY() - 15, (int) hpBarValue, 10);

                        monster.hpBarCounter++;
                        if (monster.hpBarCounter > 600) {
                            monster.hpBarCounter = 0;
                            monster.hpBarOn = false;
                        }
                    }
                }
                // Boss bar: Always draw if boss exists (no inCamera or hpBarOn check)
                else if (monster.boss && monster.inCamera()) {

                    double oneScale = (double) gamePanel.tileSize * 8 / monster.maxLife;
                    double hpBarValue = oneScale * monster.life;

                    int x = gamePanel.screenWidth / 2 - gamePanel.tileSize * 4;
                    int y = gamePanel.tileSize * 10;

                    // Background (dark outline) - full size
                    graphics2D.setColor(new Color(35, 35, 35));
                    graphics2D.fillRect(x - 1, y - 1, gamePanel.tileSize * 8 + 2, 24);  // 22 + 2 for border

                    // Gray base bar - full width/height
                    graphics2D.setColor(new Color(185, 185, 185));
                    graphics2D.fillRect(x, y, gamePanel.tileSize * 8, 22);

                    // Red HP fill - MATCH HEIGHT TO GRAY (was 10 → now 22)
                    graphics2D.setColor(new Color(255, 0, 30));
                    graphics2D.fillRect(x, y, (int) hpBarValue, 22);  // FIXED: Full 22px height

                    // Name above
                    graphics2D.setFont(graphics2D.getFont().deriveFont(Font.BOLD, 24F));
                    graphics2D.setColor(Color.WHITE);
                    graphics2D.drawString(monster.name, x + 4, y - 10);
                }
            }
        }
    }
    public void drawFPS() {
        graphics2D.setColor(Color.WHITE);
        graphics2D.setFont(graphics2D.getFont().deriveFont(32F));
        graphics2D.drawString("FPS: " + gamePanel.fpsCount, gamePanel.screenWidth - gamePanel.tileSize * 3, gamePanel.tileSize);

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
    //CHECK DrawMessage LATER FOR THE DIALOGUE WINDOW BUG
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
    public void options_top(int frameX, int frameY) {
            int textX, textY;

            String text = "Options";
            textX = getXforCenteredText(text);
            textY = frameY + gamePanel.tileSize;
            graphics2D.drawString(text, textX, textY);

            //FULL SCREEN ON/OFF
            textX = frameX + gamePanel.tileSize - 15;
            textY += gamePanel.tileSize * 2;
            graphics2D.drawString("Full Screen", textX, textY);
            if(commandNum == 0) {
                graphics2D.drawString(">", textX - 20, textY);
                if (gamePanel.keyHandler.enterPressed) {
                    gamePanel.fullScreenOn = !gamePanel.fullScreenOn;
                    subState = 1;
                }
            }
            //MUSIC
            textY += gamePanel.tileSize;
            graphics2D.drawString("Music", textX, textY);
            if(commandNum == 1) {
                graphics2D.drawString(">", textX - 20, textY);
            }
            //SE
            textY += gamePanel.tileSize;
            graphics2D.drawString("Sound Effects", textX, textY);
            if(commandNum == 2) {
                graphics2D.drawString(">", textX - 20, textY);
            }
            //CONTROL
            textY += gamePanel.tileSize;
            graphics2D.drawString("Controls", textX, textY);
            if(commandNum == 3) {
                graphics2D.drawString(">", textX - 20, textY);
                if(gamePanel.keyHandler.enterPressed) {
                    subState = 2;
                    commandNum = 0;
                }
            }

            //END GAME
            textY += gamePanel.tileSize;
            graphics2D.drawString("Quit Game", textX, textY);
            if(commandNum == 4) {
                graphics2D.drawString(">", textX - 20, textY);
                if(gamePanel.keyHandler.enterPressed){
                    subState = 3;
                    commandNum = 0;
                }
            }
            //BACK
            textY += gamePanel.tileSize * 2;
            graphics2D.drawString("Back", textX, textY);
            if(commandNum == 5) {
                graphics2D.drawString(">", textX - 20, textY);
                if(gamePanel.keyHandler.enterPressed) {
                    gamePanel.gameState = gamePanel.playState;
                    commandNum = 0;
                }
            }

            //FULL SCREEN CHECK BOX
            textX = frameX + gamePanel.tileSize * 7;
            textY = frameY + gamePanel.tileSize * 2 + 24;
            graphics2D.setStroke(new BasicStroke(3));
            graphics2D.drawRect(textX, textY, 28, 28);
            if(gamePanel.fullScreenOn) {
                graphics2D.fillRect(textX, textY, 28, 28);
            }

            //MUSIC VOLUME
            textX = frameX + gamePanel.tileSize * 6 + 4;
            textY += gamePanel.tileSize;
            graphics2D.drawRect(textX - gamePanel.tileSize, textY, 120, 24);
            int musicVolumeWidth = 24 * gamePanel.sound.getMusicVolumeScale(); // Changed this line
            graphics2D.fillRect(textX - gamePanel.tileSize, textY, musicVolumeWidth, 24);

            //SE VOLUME
            textY += gamePanel.tileSize;
            graphics2D.drawRect(textX - gamePanel.tileSize, textY, 120, 24);
            int seVolumeWidth = 24 * gamePanel.sound.getSEVolumeScale(); // Changed this line
            graphics2D.fillRect(textX - gamePanel.tileSize, textY, seVolumeWidth, 24);

            gamePanel.config.saveConfig();
        }
    public void options_fullScreenNotification(int frameX, int frameY) {

        int textX = frameX + gamePanel.tileSize;
        int textY = frameY + gamePanel. tileSize * 3;

        currentDialogue = "The change will take \neffect after restarting \nthe game. ";

        for(String line: currentDialogue.split("\n")) {
            graphics2D.drawString(line,textX,textY);
            textY += 40;
        }

        //BACK
        textY = frameY + gamePanel.tileSize * 9;
        graphics2D.drawString("Back", textX, textY);
        if(commandNum == 0) {
            graphics2D.drawString(">", textX - 25, textY);
            if(gamePanel.keyHandler.enterPressed) {
                subState = 0;
            }
        }
    }
    public void options_displayControls(int frameX, int frameY) {

        int textX;
        int textY;

        //TITLE
        String text = "Controls";
        textX = getXforCenteredText(text);
        textY = frameY + gamePanel.tileSize;
        graphics2D.drawString(text, textX, textY);

        textX = frameX + gamePanel.tileSize;
        textY += gamePanel.tileSize;
        graphics2D.drawString("Move", textX, textY);
        textY += gamePanel.tileSize;
        graphics2D.drawString("Confirm", textX, textY);
        textY += gamePanel.tileSize;
        graphics2D.drawString("Attack", textX, textY);
        textY += gamePanel.tileSize;
        graphics2D.drawString("Shoot", textX, textY);
        textY += gamePanel.tileSize;
        graphics2D.drawString("Character Screen", textX, textY);
        textY += gamePanel.tileSize;
        graphics2D.drawString("Pause", textX, textY);
        textY += gamePanel.tileSize;
        graphics2D.drawString("Options", textX, textY);
        textY += gamePanel.tileSize;

        textX = frameX + gamePanel.tileSize * 6;
        textY = frameY + gamePanel.tileSize * 2;
        graphics2D.drawString("WASD", textX, textY);
        textY += gamePanel.tileSize;
        graphics2D.drawString("ENTER", textX, textY);
        textY += gamePanel.tileSize;
        graphics2D.drawString("SPACE", textX, textY);
        textY += gamePanel.tileSize;
        graphics2D.drawString("F", textX, textY);
        textY += gamePanel.tileSize;
        graphics2D.drawString("C", textX, textY);
        textY += gamePanel.tileSize;
        graphics2D.drawString("P", textX, textY);
        textY += gamePanel.tileSize;
        graphics2D.drawString("ESC", textX, textY);
        textY += gamePanel.tileSize;

        //BACK
        textX = frameX + gamePanel.tileSize;
        textY = frameY + gamePanel.tileSize * 9;
        graphics2D.drawString("Back", textX, textY);
        if (commandNum == 0) {
            graphics2D.drawString(">", textX - 25, textY);
            if (gamePanel.keyHandler.enterPressed) {
                subState = 0;
                commandNum = 3;
            }
        }
    }
    public void options_quitGameConfirmation(int frameX,int frameY) {

        int textX = frameX + gamePanel.tileSize;
        int textY = frameY + gamePanel.tileSize * 3;

        currentDialogue = "Quit the game and \nreturn to the title screen?";

        for(String line: currentDialogue.split("\n")) {
            graphics2D.drawString(line,textX,textY);
            textY += 40;
        }

        //YES
        String text = "Yes";
        textX = getXforCenteredText(text);
        textY += gamePanel.tileSize * 3;
        graphics2D.drawString(text,textX,textY);
        if(commandNum == 0) {
            graphics2D.drawString(">", textX - 25, textY);
            if(gamePanel.keyHandler.enterPressed) {
                subState = 0;
                if(gamePanel.currentMap != 3) {
                    gamePanel.dataManager.savePlayerStats(gamePanel.player);
                }
                else {
                    gamePanel.dataManager.savePlayerPosition(3,gamePanel.dungeon,26 * gamePanel.tileSize,40 * gamePanel.tileSize);
                }
                gamePanel.gameState = gamePanel.titleState;
            }
        }
        //NO
        text = "No";
        textX = getXforCenteredText(text);
        textY += gamePanel.tileSize ;
        graphics2D.drawString(text,textX,textY);
        if(commandNum == 1) {
            graphics2D.drawString(">", textX - 25, textY);
            if(gamePanel.keyHandler.enterPressed) {
                subState = 0;
                commandNum = 4;
            }
        }
    }
    public void trade_select() {

        npc.dialogueSet = 0;
        drawDialogueScreen();
        //DRAW WINDOW
        int x = gamePanel.tileSize * 15;
        int y = gamePanel.tileSize * 4;
        int width = gamePanel.tileSize * 3;
        int height = (int)(gamePanel.tileSize * 3.5);
        drawSubWindow(x,y,width, height);

        //DRAW TEXTS
        x += gamePanel.tileSize;
        y += gamePanel.tileSize;
        graphics2D.drawString("Buy", x, y);
        if(commandNum == 0) {
            graphics2D.drawString(">", x - 24, y);
            if (gamePanel.keyHandler.enterPressed) {
                subState = 1;
            }
        }
        y += gamePanel.tileSize;
        graphics2D.drawString("Sell", x, y);
        if(commandNum == 1) {
            graphics2D.drawString(">", x - 24, y);
            if (gamePanel.keyHandler.enterPressed) {
                subState = 2;
            }
        }
        y += gamePanel.tileSize;
        graphics2D.drawString("Leave", x, y);
        if(commandNum == 2) {
            graphics2D.drawString(">", x - 24, y);
            if (gamePanel.keyHandler.enterPressed) {
                commandNum = 0;
                npc.startDialogue(npc,1);
            }
        }
    }
    public void trade_buy() {
        //DRAW PLAYER INVENTORY
        drawInventory(gamePanel.player, false);

        //DRAW NPC INVENTORY
        drawInventory(npc,true);
        graphics2D.setFont(graphics2D.getFont().deriveFont(Font.PLAIN,40));
        //DRAW HINT WINDOW
        int x = gamePanel.tileSize * 2;
        int y = gamePanel.tileSize * 10 - 6;
        int width = gamePanel.tileSize * 6;
        int height = gamePanel.tileSize * 2;
        drawSubWindow(x,y,width,height);
        graphics2D.drawString("[ESC] Back", x + 24,y + 60);
        //DRAW PLAYER COIN WINDOW
        x = gamePanel.tileSize * 11;
        y = gamePanel.tileSize * 10 - 6;
        width = gamePanel.tileSize * 6;
        height = gamePanel.tileSize * 2;
        drawSubWindow(x,y,width,height);
        graphics2D.drawString("Your coins: " + gamePanel.player.coin , x + 24,y + 60);
        //DRAW PRICE WINDOW
        int itemIndex = getItemIndexOnSlot(npcSlotCol,npcSlotRow);
        if(itemIndex < npc.inventory.size()) {
            x = (int) (gamePanel.tileSize * 5.5);
            y = (int) (gamePanel.tileSize * 5.5);
            width = (int) (gamePanel.tileSize * 2.5);
            height = gamePanel.tileSize;
            drawSubWindow(x, y, width, height);
            graphics2D.drawImage(coin, x + 10, y + 8, 32, 32,  null);

            // Calculate price once and store it
            int price = npc.inventory.get(itemIndex).price;
            String text = ""+price;
            graphics2D.setFont(graphics2D.getFont().deriveFont(Font.PLAIN,32));
            x = getXforAllignToRight(text,gamePanel.tileSize * 8 - 30);
            graphics2D.drawString(text,x, y+35);

            //BUY AN ITEM
            if(gamePanel.keyHandler.enterPressed) {
                // Check if player has enough money
                if(gamePanel.player.coin < price) {
                    subState = 0;
                    npc.startDialogue(npc,2);
                    gamePanel.keyHandler.enterPressed = false;
                }
                else {
                    if(gamePanel.player.canObtainItem(npc.inventory.get(itemIndex))) {
                        gamePanel.player.coin -= npc.inventory.get(itemIndex).price;
                    }
                    else {
                        subState = 0;
                        npc.startDialogue(npc,3);
                        gamePanel.keyHandler.enterPressed = false;
                    }
                }
                // Reset enter key to prevent multiple purchases
                gamePanel.keyHandler.enterPressed = false;
            }
        }
    }
    public void trade_sell() {
        //DRAW PLAYER INVENTORY
        drawInventory(gamePanel.player, true);
        graphics2D.setFont(graphics2D.getFont().deriveFont(Font.PLAIN, 40));
        //DRAW HINT WINDOW
        int x = gamePanel.tileSize * 2;
        int y = gamePanel.tileSize * 10 - 6;
        int width = gamePanel.tileSize * 6;
        int height = gamePanel.tileSize * 2;
        drawSubWindow(x, y, width, height);
        graphics2D.drawString("[ESC] Back", x + 24, y + 60);

        //DRAW PLAYER COIN WINDOW
        x = gamePanel.tileSize * 11;
        drawSubWindow(x, y, width, height);
        graphics2D.drawString("Your coins: " + gamePanel.player.coin, x + 24, y + 60);

        //DRAW PRICE WINDOW
        int itemIndex = getItemIndexOnSlot(playerSlotCol, playerSlotRow);
        if (itemIndex < gamePanel.player.inventory.size()) {

            x = (int) (gamePanel.tileSize * 14.5);
            y = (int) (gamePanel.tileSize * 5.5);
            width = (int) (gamePanel.tileSize * 2.5);
            height = gamePanel.tileSize;
            drawSubWindow(x, y, width, height);
            graphics2D.drawImage(coin, x + 10, y + 8, 32, 32, null);

            // Calculate sell price once
            int price = (int)(gamePanel.player.inventory.get(itemIndex).price / 1.5);
            String text = "" + price;
            graphics2D.setFont(graphics2D.getFont().deriveFont(Font.PLAIN, 32));
            x = getXforAllignToRight(text, gamePanel.tileSize * 17 - 30);
            graphics2D.drawString(text, x, y + 35);

            //SELL AN ITEM
            if (gamePanel.keyHandler.enterPressed) {
                // Check if item is equipped
                if(gamePanel.player.inventory.get(itemIndex) == gamePanel.player.currentWeapon ||
                        gamePanel.player.inventory.get(itemIndex) == gamePanel.player.currentShield) {
                    subState = 0;
                    commandNum = 0;
                    npc.startDialogue(npc, 4);
                    gamePanel.keyHandler.enterPressed = false;
                    drawDialogueScreen();
                }
                // If not equipped, complete the sale
                else {
                    if(gamePanel.player.inventory.get(itemIndex).amount > 1) {
                        gamePanel.player.inventory.get(itemIndex).amount--;
                    }
                    else {
                        gamePanel.player.inventory.remove(itemIndex);
                    }
                    gamePanel.player.coin += price;  // Use consistent price variable
                }

                // Reset enter key to prevent multiple sales
                gamePanel.keyHandler.enterPressed = false;
            }
        }
    }
    public int getItemIndexOnSlot(int slotCol,int slotRow) {
        return slotCol + (slotRow * 5);
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

}