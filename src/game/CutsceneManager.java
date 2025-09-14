package game;

import entity.PlayerDummy;
import object.OBJ_AmuletOfLife;
import object.OBJ_Door_Iron;

import java.awt.*;

public class CutsceneManager {

    GamePanel gamePanel;
    Graphics2D graphics2D;
    public int sceneNum;
    public int scenePhase;
    int counter = 0;
    float alpha = 0f;
    int y;
    String endCredit;

    // Scene Number
    public final int NA = 0;
    public final int skeletonLord = 1;
    public final int ending = 2;
    public final int goblinBoss = 3;
    public final int beginning = 4;
    public CutsceneManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        endCredit = "Dungeon Escape\n" +
                "\n" +
                "Developed by\n" +
                "Lucian Covaliuc\n" +
                "\n" +
                "Lead Developer\n" +
                "Lucian Covaliuc\n" +
                "\n" +
                "Art & Design\n" +
                "Lucian Covaliuc\n" +
                "\n" +
                "Music & Sound\n" +
                "Lucian Covaliuc\n" +
                "\n" +
                "Story & Writing\n" +
                "Lucian Covaliuc\n" +
                "\n" +
                "Special Thanks\n" +
                "To our brave players,\n" +
                "beta testers(Lucian),\n" +
                "\n" +
                "Thank you for playing!\n" +
                "Your journey through the dungeon\n" +
                "has come to an end,\n" +
                "but new adventures await!";
    }

    public void draw(Graphics2D graphics2D) {

        this.graphics2D = graphics2D;

        switch (sceneNum) {

            case skeletonLord: scene_skeletonLord(); break;
            case ending: scene_ending(); break;
            case goblinBoss: scene_goblinBoss(); break;
            case beginning: scene_beginning(); break;
        }
    }

    public void scene_beginning() {
        // Initialize alpha for fade-in if not already set
        if (scenePhase == 0) {
            alpha = 0f;
            scenePhase = 1;
        }

        // Draw black background with full opacity during text phases
        if (scenePhase <= 2) {
            drawBlackBackground(1f);
        }

        // Display message with fading effect
        String message = "After a lot of searching across the globe, Sam has \nfound a lead on the amulet of life, to save his wife \nfrom an incurable illness. It's guarded on an island.";

        if (scenePhase == 1) {
            // Increase alpha for text fade-in effect (~2 seconds)
            alpha += 0.02f; // ~2 seconds at 60 FPS (0.02 * 100 frames)
            if (alpha > 1f) {
                alpha = 1f;
                scenePhase = 2;
            }
            drawString(alpha, 38f, gamePanel.screenHeight / 2 - 48, message, 40);
        }

        if (scenePhase == 2) {
            // Hold text for ~4.5 seconds (270 frames)
            drawString(alpha, 38f, gamePanel.screenHeight / 2 - 48, message, 40);
            if (counterReached(270)) {
                scenePhase = 3;
                alpha = 1f; // Reset alpha for black screen
            }
        }

        if (scenePhase == 3) {
            // Hold black screen for ~1 second (60 frames)
            drawBlackBackground(1f);
            if (counterReached(60)) {
                scenePhase = 4;
            }
        }

        if (scenePhase == 4) {
            // Fade out black background to reveal world map (~2 seconds)
            alpha -= 0.01f; // ~2 seconds at 60 FPS (0.01 * 200 frames)
            if (alpha < 0f) {
                alpha = 0f; // Clamp alpha to prevent negative values
            }
            drawBlackBackground(alpha);
            if (alpha <= 0f) {
                scenePhase = 5;
            }
        }

        if (scenePhase == 5) {
            // Wait briefly (~0.5 seconds) before transitioning
            if (counterReached(30)) {
                // Reset
                sceneNum = NA;
                scenePhase = 0;
                gamePanel.gameState = gamePanel.playState;

                // Change the music
                gamePanel.sound.stopMusic();
                gamePanel.sound.playMusic(0);
            }
        }
    }

    public void scene_skeletonLord() {

        if(scenePhase == 0) {

            gamePanel.bossBattleOn = true;

            //Shut the iron door
            for(int i = 0; i < gamePanel.object[i].length; i++) {

                if(gamePanel.object[gamePanel.currentMap][i] == null) {

                    gamePanel.object[gamePanel.currentMap][i] = new OBJ_Door_Iron(gamePanel);
                    gamePanel.object[gamePanel.currentMap][i].worldX = 25 * gamePanel.tileSize;
                    gamePanel.object[gamePanel.currentMap][i].worldY = 28 * gamePanel.tileSize;
                    gamePanel.object[gamePanel.currentMap][i].temp = true;
                    gamePanel.sound.playSoundEffect(21);
                    break;
                }
            }
            //Search vacant slot for the dummy

            for(int i = 0; i < gamePanel.npc[1].length; i++) {

                if(gamePanel.npc[gamePanel.currentMap][i] == null) {
                    gamePanel.npc[gamePanel.currentMap][i] = new PlayerDummy(gamePanel);
                    gamePanel.npc[gamePanel.currentMap][i].worldX = gamePanel.player.worldX;
                    gamePanel.npc[gamePanel.currentMap][i].worldY = gamePanel.player.worldY;
                    gamePanel.npc[gamePanel.currentMap][i].direction = gamePanel.player.direction;
                    break;
                }
            }

            gamePanel.player.drawing = false;

            scenePhase++;
        }
        if(scenePhase == 1) {

            gamePanel.player.worldY -= 2;

            if(gamePanel.player.worldY < gamePanel.tileSize * 17) {
                scenePhase++;

            }
        }
        if(scenePhase == 2) {

            //Search the boss
            for(int i = 0; i < gamePanel.monster[1].length; i++) {

                if(gamePanel.monster[gamePanel.currentMap][i] != null && gamePanel.monster[gamePanel.currentMap][i].name.equals("Skeleton Lord")) {

                    gamePanel.monster[gamePanel.currentMap][i].sleep = false;
                    gamePanel.ui.npc = gamePanel.monster[gamePanel.currentMap][i];
                    scenePhase++;
                    break;
                }
            }
        }
        if(scenePhase == 3) {

            //The boss speaks
            gamePanel.ui.drawDialogueScreen();
        }
        if(scenePhase == 4) {

            //Return to the player

            //Search the dummy
            for(int i = 0; i < gamePanel.npc[1].length; i++) {

                if(gamePanel.npc[gamePanel.currentMap][i] != null && gamePanel.npc[gamePanel.currentMap][i].name.equals(PlayerDummy.npcName)) {

                    //Restore player position
                    gamePanel.player.worldX = gamePanel.npc[gamePanel.currentMap][i].worldX;
                    gamePanel.player.worldY = gamePanel.npc[gamePanel.currentMap][i].worldY;
                    gamePanel.player.direction = gamePanel.npc[gamePanel.currentMap][i].direction;
                    //Delete the dummy
                    gamePanel.npc[gamePanel.currentMap][i] = null;
                    break;
                }
            }
            //Start drawing the player
            gamePanel.player.drawing = true;

            //Reset
            sceneNum = NA;
            scenePhase = 0;
            gamePanel.gameState = gamePanel.playState;

            // Change the music
            gamePanel.sound.stopMusic();
            gamePanel.sound.playMusic(23);
        }
    }

    public void scene_goblinBoss() {

        if(scenePhase == 0) {

            gamePanel.bossBattleOn = true;

            //Shut the iron door
            for(int i = 0; i < gamePanel.object[i].length; i++) {

                if(gamePanel.object[gamePanel.currentMap][i] == null) {

                    gamePanel.object[gamePanel.currentMap][i] = new OBJ_Door_Iron(gamePanel);
                    gamePanel.object[gamePanel.currentMap][i].worldX = 34 * gamePanel.tileSize;
                    gamePanel.object[gamePanel.currentMap][i].worldY = 21 * gamePanel.tileSize;
                    gamePanel.object[gamePanel.currentMap][i].temp = true;
                    gamePanel.sound.stopMusic();
                    break;
                }
            }
            //Search vacant slot for the dummy

            for(int i = 0; i < gamePanel.npc[1].length; i++) {

                if(gamePanel.npc[gamePanel.currentMap][i] == null) {
                    gamePanel.npc[gamePanel.currentMap][i] = new PlayerDummy(gamePanel);
                    gamePanel.npc[gamePanel.currentMap][i].worldX = gamePanel.player.worldX;
                    gamePanel.npc[gamePanel.currentMap][i].worldY = gamePanel.player.worldY;
                    gamePanel.npc[gamePanel.currentMap][i].direction = gamePanel.player.direction;
                    break;
                }
            }

            gamePanel.player.drawing = false;

            scenePhase++;
        }
        if(scenePhase == 1) {

            gamePanel.player.worldY -= 2;

            if(gamePanel.player.worldY < gamePanel.tileSize * 17) {
                scenePhase++;

            }
        }
        if(scenePhase == 2) {

            //Search the boss
            for(int i = 0; i < gamePanel.monster[1].length; i++) {

                if(gamePanel.monster[gamePanel.currentMap][i] != null && gamePanel.monster[gamePanel.currentMap][i].name.equals("Goblin Boss")) {

                    gamePanel.monster[gamePanel.currentMap][i].sleep = false;
                    gamePanel.ui.npc = gamePanel.monster[gamePanel.currentMap][i];
                    scenePhase++;
                    break;
                }
            }
        }
        if(scenePhase == 3) {

            //The boss speaks
            gamePanel.ui.drawDialogueScreen();
        }
        if(scenePhase == 4) {

            gamePanel.sound.playMusic(25);

            //Return to the player

            //Search the dummy
            for(int i = 0; i < gamePanel.npc[1].length; i++) {

                if(gamePanel.npc[gamePanel.currentMap][i] != null && gamePanel.npc[gamePanel.currentMap][i].name.equals(PlayerDummy.npcName)) {

                    //Restore player position
                    gamePanel.player.worldX = gamePanel.npc[gamePanel.currentMap][i].worldX;
                    gamePanel.player.worldY = gamePanel.npc[gamePanel.currentMap][i].worldY;
                    gamePanel.player.direction = gamePanel.npc[gamePanel.currentMap][i].direction;
                    //Delete the dummy
                    gamePanel.npc[gamePanel.currentMap][i] = null;
                    break;
                }
            }
            //Start drawing the player
            gamePanel.player.drawing = true;

            //Reset
            sceneNum = NA;
            scenePhase = 0;
            gamePanel.gameState = gamePanel.playState;

            // Change the music
            gamePanel.sound.stopMusic();
            gamePanel.sound.playMusic(23);
        }
    }

    public void scene_ending() {

        if (scenePhase == 0) {

            gamePanel.sound.stopMusic();
            gamePanel.ui.npc = new OBJ_AmuletOfLife(gamePanel);
            scenePhase++;
        }
        if (scenePhase == 1) {

            //Display dialogues
            gamePanel.ui.drawDialogueScreen();

        }
        if (scenePhase == 2) {

            //Play the fanfare
            gamePanel.sound.playSoundEffect(4);
            scenePhase++;

        }
        if (scenePhase == 3) {

            //Wait until the sound effect ends
            if (counterReached(300)) {
                scenePhase++;
            }

        }
        if (scenePhase == 4) {
            // Fade to black
            alpha += 0.005f;
            if (alpha > 1f) {
                alpha = 1f;
            }
            drawBlackBackground(alpha);
            if(alpha == 1f) {
                alpha = 0;
                scenePhase++;
            }
        }
        if (scenePhase == 5) {

            // Draw black background with full opacity
            drawBlackBackground(1f);

            // Fade in the text
            alpha += 0.005f;
            if (alpha > 1f) {
                alpha = 1f;
            }

            String text = "After the fierce battle with the Skeleton Lord, \n"
                    + "our warrior finally found the legendary treasure.\n"
                    + "His search is finally over.\n"
                    + "He can return home to his wife to heal her.";

            drawString(alpha, 38f, 200, text, 70);

            // Hold the scene for 600 frames
            if (counterReached(600)) {
                gamePanel.sound.playMusic(0);
                scenePhase++;
            }
        }
        if(scenePhase == 6) {

            drawBlackBackground(1f);

            drawString(1f, 120f, gamePanel.screenHeight / 2, "Dungeon Escape!", 40);

            if (counterReached(480)) {
                scenePhase++;
            }
        }

        if(scenePhase == 7) {

            drawBlackBackground(1f);

            y = gamePanel.screenHeight / 2;
            drawString(1f,38f,y, endCredit, 40);

            if(counterReached(80)) {
                scenePhase++;
            }
        }
        if(scenePhase == 8) {

            drawBlackBackground(1f);
            //Scrolling the credit
            y--;
            drawString(1f,38f,y, endCredit, 40);
            if(counterReached(60*15)) {
                scenePhase++;
            }
        }
        if(scenePhase == 9) {
            //Reset
            sceneNum = NA;
            scenePhase = 0;
            gamePanel.gameState = gamePanel.playState;

            // Change the music
            gamePanel.sound.stopMusic();
            gamePanel.sound.playMusic(0);
        }
    }

    public boolean counterReached(int target) {

        boolean reached = false;

        counter++;
        if(counter > target) {

            reached = true;
            counter = 0;
        }
        return reached;
    }

    public void drawBlackBackground(float alpha) {

        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        graphics2D.setColor(Color.black);
        graphics2D.fillRect(0,0,gamePanel.screenWidth,gamePanel.screenHeight);
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    public void drawString(float alpha, float fontSize, int y, String text, int lineHeight) {

        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        graphics2D.setColor(Color.white);
        graphics2D.setFont(graphics2D.getFont().deriveFont(fontSize));

        for(String line: text.split("\n")) {

            int x = gamePanel.ui.getXforCenteredText(line);
            graphics2D.drawString(line, x, y);
            y += lineHeight;

        }
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
}
