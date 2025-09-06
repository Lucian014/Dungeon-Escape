package interactive_tile;

import entity.Entity;
import game.GamePanel;

public class IT_Trunk extends InteractiveTile {

    GamePanel gamePanel;
    public IT_Trunk(GamePanel gamePanel, int col, int row) {
        super(gamePanel, col, row);
        this.gamePanel = gamePanel;

        this.worldX = col * gamePanel.tileSize;
        this.worldY = row * gamePanel.tileSize;

        down1 = setup("tiles_interactive/trunk",gamePanel.tileSize,gamePanel.tileSize);

        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = 0;
        solidArea.height = 0;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}
