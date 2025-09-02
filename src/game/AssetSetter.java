package game;

import object.OBJ_Boots;
import object.OBJ_Door;
import object.OBJ_Key;

public class AssetSetter {

    GamePanel gamePanel;

    public AssetSetter(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }

    public void setObject() {

        gamePanel.object[0] = new OBJ_Key();
        gamePanel.object[0].worldX = 23 * gamePanel.tileSize;
        gamePanel.object[0].worldY = 7 * gamePanel.tileSize;

        gamePanel.object[1] = new OBJ_Key();
        gamePanel.object[1].worldX = 23 * gamePanel.tileSize;
        gamePanel.object[1].worldY = 40 * gamePanel.tileSize;

        gamePanel.object[2] = new OBJ_Door();
        gamePanel.object[2].worldX = 37 * gamePanel.tileSize;
        gamePanel.object[2].worldY = 7 * gamePanel.tileSize;

        gamePanel.object[3] = new OBJ_Boots();
        gamePanel.object[3].worldX = 37 * gamePanel.tileSize;
        gamePanel.object[3].worldY = 42 * gamePanel.tileSize;

    }
}
