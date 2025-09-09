package tiles;

import game.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Map extends TileManager {

    GamePanel gamePanel;
    BufferedImage[] worldMap;
    public boolean miniMapOn = false;

    public Map(GamePanel gamePanel) {

        super(gamePanel);
        this.gamePanel = gamePanel;

        createWorldMap();
    }

    public void createWorldMap() {

        worldMap = new BufferedImage[gamePanel.maxMap];
        int worldMapWidth = gamePanel.maxWorldCol * gamePanel.tileSize;
        int worldMapHeight = gamePanel.maxWorldRow * gamePanel.tileSize;

        for(int i = 0; i < gamePanel.maxMap; i++) {
            worldMap[i] = new BufferedImage(worldMapWidth, worldMapHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 =(Graphics2D) worldMap[i].createGraphics();

            int col = 0;
            int row = 0;

            while(col < gamePanel.maxWorldCol && row < gamePanel.maxWorldRow) {

                int tileNum = mapTileNum[i][col][row];
                int x = gamePanel.tileSize * col;
                int y = gamePanel.tileSize * row;
                g2.drawImage(tile[tileNum].image, x, y, null);

                col++;
                if(col == gamePanel.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
        }
    }

    public void drawFullMapScreen(Graphics2D graphics2D) {

        graphics2D.setColor(new Color(239,222,170));
        graphics2D.fillRect(0,0,gamePanel.screenWidth, gamePanel.screenHeight);

        //Draw the map
        int width = 500;
        int height = 500;
        int x = gamePanel.screenWidth / 2 - width / 2;
        int y = gamePanel.screenHeight / 2 - height / 2;
        graphics2D.drawImage(worldMap[gamePanel.currentMap], x, y, width, height, null);

        //Draw the player
        double scale = (double) (gamePanel.tileSize * gamePanel.maxWorldCol) / width;
        int playerX = (int)(x + gamePanel.player.worldX / scale);
        int playerY = (int)(y + gamePanel.player.worldY / scale);
        int playerSize = (int)(gamePanel.tileSize / scale);
        graphics2D.drawImage(gamePanel.player.down1, playerX, playerY, playerSize, playerSize, null);

        //Hint
        graphics2D.setFont(gamePanel.ui.maruMonica.deriveFont(32F));
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString("Press 'M' to close", 735, 550);

    }

    public void drawMiniMapScreen(Graphics2D graphics2D) {

        if(miniMapOn) {

            int width = 200;
            int height = 200;
            int x = gamePanel.screenWidth - width - 10;
            int y = 60;

            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
            graphics2D.drawImage(worldMap[gamePanel.currentMap], x, y, width, height, null);

            //Draw the player
            double scale = (double) (gamePanel.tileSize * gamePanel.maxWorldCol) / width;
            int playerX = (int)(x + gamePanel.player.worldX / scale);
            int playerY = (int)(y + gamePanel.player.worldY / scale);
            int playerSize = (int)(gamePanel.tileSize / 3);
            graphics2D.drawImage(gamePanel.player.down1, playerX - 5, playerY - 5, playerSize, playerSize, null);

            //Hint
            graphics2D.setFont(gamePanel.ui.maruMonica.deriveFont(32F));
            graphics2D.setColor(Color.WHITE);
            graphics2D.drawString("Press 'M' to close", 735, 550);
            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        }
    }
}
