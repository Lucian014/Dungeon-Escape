package game;

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
        //23,7
        gamePanel.object[0] = new OBJ_Key(gamePanel);
        gamePanel.object[0].worldX = 23 * gamePanel.tileSize;
        gamePanel.object[0].worldY = 7 * gamePanel.tileSize;

        gamePanel.object[1] = new OBJ_Key(gamePanel);
        gamePanel.object[1].worldX = 23 * gamePanel.tileSize;
        gamePanel.object[1].worldY = 40 * gamePanel.tileSize;

        gamePanel.object[2] = new OBJ_Door(gamePanel);
        gamePanel.object[2].worldX = 37 * gamePanel.tileSize;
        gamePanel.object[2].worldY = 7 * gamePanel.tileSize;

        gamePanel.object[3] = new OBJ_Boots(gamePanel);
        gamePanel.object[3].worldX = 37 * gamePanel.tileSize;
        gamePanel.object[3].worldY = 42 * gamePanel.tileSize;

        gamePanel.object[4] = new OBJ_Chest(gamePanel);
        gamePanel.object[4].worldX = 10 * gamePanel.tileSize;
        gamePanel.object[4].worldY = 7 * gamePanel.tileSize;

    }
}
