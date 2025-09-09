package tiles;

import game.GamePanel;

import java.awt.image.BufferedImage;

public class Map extends TileManager {

    GamePanel gamePanel;
    BufferedImage[] worldMap;
    public boolean miniMapOn = false;
    public Map(GamePanel gamePanel) {
        super(gamePanel);
        this.gamePanel = gamePanel;

    }

    public void createWorldMap(){

        worldMap = new BufferedImage[gamePanel.maxMap];
        //int worldM
    }
}
