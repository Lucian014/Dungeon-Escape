package game;

import entity.PlayerDummy;
import monster.MON_SkeletonLord;
import object.OBJ_Door_Iron;

import java.awt.*;

public class CutsceneManager {

    GamePanel gamePanel;
    Graphics2D graphics2D;
    public int sceneNum;
    public int scenePhase;

    // Scene Number
    public final int NA = 0;
    public final int skeletonLord = 1;

    public CutsceneManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void draw(Graphics2D graphics2D) {

        this.graphics2D = graphics2D;

        switch (sceneNum) {

            case skeletonLord: scene_skeletonLord(); break;
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

            if(gamePanel.player.worldY < gamePanel.tileSize * 16) {
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
}
