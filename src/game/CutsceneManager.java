package game;

import entity.PlayerDummy;
import monster.MON_SkeletonLord;
import object.OBJ_BlueHeart;
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
                    gamePanel.object[gamePanel.currentMap][i].worldX = 35 * gamePanel.tileSize;
                    gamePanel.object[gamePanel.currentMap][i].worldY = 21 * gamePanel.tileSize;
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
            gamePanel.ui.npc = new OBJ_BlueHeart(gamePanel);
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
                    + "He can return home.";

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
            gamePanel.sound.playMusic(9);
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
