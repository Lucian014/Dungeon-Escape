package game;

import entity.Entity;

public class CollisionChecker {

    GamePanel gamePanel;

    public CollisionChecker(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void checkTile(Entity entity, int dx, int dy) {
        // Current solid area position in world
        int leftWorldX = entity.worldX + entity.solidArea.x + dx;
        int rightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width + dx;
        int topWorldY = entity.worldY + entity.solidArea.y + dy;
        int bottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height + dy;

        int leftCol = leftWorldX / gamePanel.tileSize;
        int rightCol = rightWorldX / gamePanel.tileSize;
        int topRow = topWorldY / gamePanel.tileSize;
        int bottomRow = bottomWorldY / gamePanel.tileSize;

        int tileNum1, tileNum2;

        // Check vertical collision
        if (dy < 0) { // moving up
            tileNum1 = gamePanel.tileManager.mapTileNum[leftCol][topRow];
            tileNum2 = gamePanel.tileManager.mapTileNum[rightCol][topRow];
            if (gamePanel.tileManager.tile[tileNum1].collision || gamePanel.tileManager.tile[tileNum2].collision) {
                entity.collisionOn = true;
            }
        }
        if (dy > 0) { // moving down
            tileNum1 = gamePanel.tileManager.mapTileNum[leftCol][bottomRow];
            tileNum2 = gamePanel.tileManager.mapTileNum[rightCol][bottomRow];
            if (gamePanel.tileManager.tile[tileNum1].collision || gamePanel.tileManager.tile[tileNum2].collision) {
                entity.collisionOn = true;
            }
        }

        // Check horizontal collision
        if (dx < 0) { // moving left
            tileNum1 = gamePanel.tileManager.mapTileNum[leftCol][topRow];
            tileNum2 = gamePanel.tileManager.mapTileNum[leftCol][bottomRow];
            if (gamePanel.tileManager.tile[tileNum1].collision || gamePanel.tileManager.tile[tileNum2].collision) {
                entity.collisionOn = true;
            }
        }
        if (dx > 0) { // moving right
            tileNum1 = gamePanel.tileManager.mapTileNum[rightCol][topRow];
            tileNum2 = gamePanel.tileManager.mapTileNum[rightCol][bottomRow];
            if (gamePanel.tileManager.tile[tileNum1].collision || gamePanel.tileManager.tile[tileNum2].collision) {
                entity.collisionOn = true;
            }
        }
    }
}