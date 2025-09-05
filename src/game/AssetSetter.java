package game;

import entity.NPC_Priest;
import monster.MON_GreenSlime;
import object.OBJ_Boots;
import object.OBJ_Chest;
import object.OBJ_Door;
import object.OBJ_Key;

public class AssetSetter {

    GamePanel gamePanel;

    public AssetSetter(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }

    public void setObject() {

    }

    public void setNPC() {

        gamePanel.npc[0] = new NPC_Priest(gamePanel);
        gamePanel.npc[0].worldX = gamePanel.tileSize * 21;
        gamePanel.npc[0].worldY = gamePanel.tileSize * 21;

    }

    public void setMonster() {

        int i = 0;

        gamePanel.monster[i] = new MON_GreenSlime(gamePanel);
        gamePanel.monster[i].worldX = gamePanel.tileSize  * 23;
        gamePanel.monster[i].worldY = gamePanel.tileSize  * 16;
        i++;
        gamePanel.monster[i] = new MON_GreenSlime(gamePanel);
        gamePanel.monster[i].worldX = gamePanel.tileSize  * 24;
        gamePanel.monster[i].worldY = gamePanel.tileSize  * 37;
        i++;
        gamePanel.monster[i] = new MON_GreenSlime(gamePanel);
        gamePanel.monster[i].worldX = gamePanel.tileSize  * 21;
        gamePanel.monster[i].worldY = gamePanel.tileSize  * 38;
        i++;
        gamePanel.monster[i] = new MON_GreenSlime(gamePanel);
        gamePanel.monster[i].worldX = gamePanel.tileSize  * 34;
        gamePanel.monster[i].worldY = gamePanel.tileSize  * 42;
        i++;
        gamePanel.monster[i] = new MON_GreenSlime(gamePanel);
        gamePanel.monster[i].worldX = gamePanel.tileSize  * 38;
        gamePanel.monster[i].worldY = gamePanel.tileSize  * 42;
        i++;


    }

}
