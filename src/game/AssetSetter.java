package game;

import entity.NPC_Priest;
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

        gamePanel.object[0] = new OBJ_Door(gamePanel);
        gamePanel.object[0].worldX = gamePanel.tileSize * 21;
        gamePanel.object[0].worldY = gamePanel.tileSize * 22;

        gamePanel.object[1] = new OBJ_Door(gamePanel);
        gamePanel.object[1].worldX = gamePanel.tileSize * 23;
        gamePanel.object[1].worldY = gamePanel.tileSize * 25;
    }

    public void setNPC() {

        gamePanel.npc[0] = new NPC_Priest(gamePanel);
        gamePanel.npc[0].worldX = gamePanel.tileSize * 21;
        gamePanel.npc[0].worldY = gamePanel.tileSize * 21;

        gamePanel.npc[1] = new NPC_Priest(gamePanel);
        gamePanel.npc[1].worldX = gamePanel.tileSize * 1;
        gamePanel.npc[1].worldY = gamePanel.tileSize * 21;

        gamePanel.npc[2] = new NPC_Priest(gamePanel);
        gamePanel.npc[2].worldX = gamePanel.tileSize * 31;
        gamePanel.npc[2].worldY = gamePanel.tileSize * 21;

        gamePanel.npc[3] = new NPC_Priest(gamePanel);
        gamePanel.npc[3].worldX = gamePanel.tileSize * 21;
        gamePanel.npc[3].worldY = gamePanel.tileSize * 11;

        gamePanel.npc[4] = new NPC_Priest(gamePanel);
        gamePanel.npc[4].worldX = gamePanel.tileSize * 21;
        gamePanel.npc[4].worldY = gamePanel.tileSize * 31;
    }
}
