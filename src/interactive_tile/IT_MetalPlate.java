package interactive_tile;

import game.GamePanel;

public class IT_MetalPlate extends InteractiveTile{

    public IT_MetalPlate(GamePanel gamePanel, int col, int row) {
        super(gamePanel, col, row);
        this.gamePanel = gamePanel;

        this.worldX = col * gamePanel.tileSize;
        this.worldY = row * gamePanel.tileSize;

        down1 = setup("tiles_interactive/metalplate",1,1);

        name = "Metal Plate";
        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = 0;
        solidArea.height = 0;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}
