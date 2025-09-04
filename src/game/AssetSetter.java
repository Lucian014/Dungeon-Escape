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

        gamePanel.monster[0] = new MON_GreenSlime(gamePanel);
        gamePanel.monster[0].worldX = gamePanel.tileSize  * 23;
        gamePanel.monster[0].worldY = gamePanel.tileSize  * 16;

        gamePanel.monster[1] = new MON_GreenSlime(gamePanel);
        gamePanel.monster[1].worldX = gamePanel.tileSize  * 23;
        gamePanel.monster[1].worldY = gamePanel.tileSize  * 37;

        gamePanel.monster[2] = new MON_GreenSlime(gamePanel);
        gamePanel.monster[2].worldX = gamePanel.tileSize  * 21;
        gamePanel.monster[2].worldY = gamePanel.tileSize  * 34;

    }

}
