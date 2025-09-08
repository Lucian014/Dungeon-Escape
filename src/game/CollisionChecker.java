package game;

import entity.Entity;

import java.awt.*;

public class CollisionChecker {

    GamePanel gamePanel;

    public CollisionChecker(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void checkTile(Entity entity) {
        entity.collisionOn = false;

        int predictedX = entity.worldX;
        int predictedY = entity.worldY;

        switch(entity.direction) {
            case "up": predictedY -= entity.speed; break;
            case "down": predictedY += entity.speed; break;
            case "left": predictedX -= entity.speed; break;
            case "right": predictedX += entity.speed; break;
        }

        int leftWorldX = predictedX + entity.solidArea.x;
        int rightWorldX = predictedX + entity.solidArea.x + entity.solidArea.width;
        int topWorldY = predictedY + entity.solidArea.y;
        int bottomWorldY = predictedY + entity.solidArea.y + entity.solidArea.height;

        int leftCol = leftWorldX / gamePanel.tileSize;
        int rightCol = (rightWorldX - 1) / gamePanel.tileSize;
        int topRow = topWorldY / gamePanel.tileSize;
        int bottomRow = (bottomWorldY - 1) / gamePanel.tileSize;

            switch(entity.direction) {
                case "up":
                    int topLeftTile = gamePanel.tileManager.mapTileNum[gamePanel.currentMap][leftCol][topRow];
                    int topRightTile = gamePanel.tileManager.mapTileNum[gamePanel.currentMap][rightCol][topRow];
                    entity.collisionOn = gamePanel.tileManager.tile[topLeftTile].collision ||
                            gamePanel.tileManager.tile[topRightTile].collision;
                    break;

                case "down":
                    int bottomLeftTile = gamePanel.tileManager.mapTileNum[gamePanel.currentMap][leftCol][bottomRow];
                    int bottomRightTile = gamePanel.tileManager.mapTileNum[gamePanel.currentMap][rightCol][bottomRow];
                    entity.collisionOn = gamePanel.tileManager.tile[bottomLeftTile].collision ||
                            gamePanel.tileManager.tile[bottomRightTile].collision;
                    break;

                case "left":
                    int leftTopTile = gamePanel.tileManager.mapTileNum[gamePanel.currentMap][leftCol][topRow];
                    int leftBottomTile = gamePanel.tileManager.mapTileNum[gamePanel.currentMap][leftCol][bottomRow];
                    entity.collisionOn = gamePanel.tileManager.tile[leftTopTile].collision ||
                            gamePanel.tileManager.tile[leftBottomTile].collision;
                    break;

                case "right":
                    int rightTopTile = gamePanel.tileManager.mapTileNum[gamePanel.currentMap][rightCol][topRow];
                    int rightBottomTile = gamePanel.tileManager.mapTileNum[gamePanel.currentMap][rightCol][bottomRow];
                    entity.collisionOn = gamePanel.tileManager.tile[rightTopTile].collision ||
                            gamePanel.tileManager.tile[rightBottomTile].collision;
                    break;
            }
    }
    public int checkObject(Entity entity, boolean player) {
        int index = 999;

        for (int i = 0; i < gamePanel.object[1].length; i++) {
            if (gamePanel.object[gamePanel.currentMap][i] != null) {

                // Get entity's solid area position
                entity.solidArea.x = entity.worldX + entity.solidAreaDefaultX;
                entity.solidArea.y = entity.worldY + entity.solidAreaDefaultY;

                // Get object's solid area position
                gamePanel.object[gamePanel.currentMap][i].solidArea.x = gamePanel.object[gamePanel.currentMap][i].worldX + gamePanel.object[gamePanel.currentMap][i].solidAreaDefaultX;
                gamePanel.object[gamePanel.currentMap][i].solidArea.y = gamePanel.object[gamePanel.currentMap][i].worldY + gamePanel.object[gamePanel.currentMap][i].solidAreaDefaultY;


                switch (entity.direction) {
                    case "up":    entity.solidArea.y -= entity.speed; break;
                    case "down":  entity.solidArea.y += entity.speed; break;
                    case "left":  entity.solidArea.x -= entity.speed; break;
                    case "right": entity.solidArea.x += entity.speed; break;
                }

                if (entity.solidArea.intersects(gamePanel.object[gamePanel.currentMap][i].solidArea)) {
                    if (gamePanel.object[gamePanel.currentMap][i].collision) {
                        entity.collisionOn = true;
                    }
                    if (player) {
                        index = i; // so player can pick up object
                    }
                }
                // Reset solid areas
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gamePanel.object[gamePanel.currentMap][i].solidArea.x = gamePanel.object[gamePanel.currentMap][i].solidAreaDefaultX;
                gamePanel.object[gamePanel.currentMap][i].solidArea.y = gamePanel.object[gamePanel.currentMap][i].solidAreaDefaultY;
            }
        }
        return index;
    }
    //NPC OR MONSTER
    public int checkEntity(Entity entity, Entity[][] target) {
        int index = 999;

        // Store original solid area positions
        int entityLeftWorldX = entity.worldX + entity.solidAreaDefaultX;
        int entityTopWorldY = entity.worldY + entity.solidAreaDefaultY;

        // Make a temporary rectangle to simulate movement
        Rectangle entityArea = new Rectangle(entityLeftWorldX, entityTopWorldY,
                entity.solidArea.width, entity.solidArea.height);

        // Shift according to direction
        switch (entity.direction) {
            case "up":    entityArea.y -= entity.speed; break;
            case "down":  entityArea.y += entity.speed; break;
            case "left":  entityArea.x -= entity.speed; break;
            case "right": entityArea.x += entity.speed; break;
        }

        for (int i = 0; i < target[gamePanel.currentMap].length; i++) {
            if (target[gamePanel.currentMap][i] != null && target[gamePanel.currentMap][i] != entity) {
                int targetLeftWorldX = target[gamePanel.currentMap][i].worldX + target[gamePanel.currentMap][i].solidAreaDefaultX;
                int targetTopWorldY = target[gamePanel.currentMap][i].worldY + target[gamePanel.currentMap][i].solidAreaDefaultY;

                Rectangle targetArea = new Rectangle(targetLeftWorldX, targetTopWorldY,
                        target[gamePanel.currentMap][i].solidArea.width, target[gamePanel.currentMap][i].solidArea.height);

                if (entityArea.intersects(targetArea)) {
                    entity.collisionOn = true;
                    index = i;
                }
            }
        }
        return index;
    }

    public boolean checkPlayer(Entity entity) {

        boolean contactPlayer = false;

        // Entity rectangle in world space
        int entityLeftWorldX = entity.worldX + entity.solidAreaDefaultX;
        int entityTopWorldY = entity.worldY + entity.solidAreaDefaultY;
        Rectangle entityArea = new Rectangle(entityLeftWorldX, entityTopWorldY,
                entity.solidArea.width, entity.solidArea.height);

        // Shift entityArea in movement direction
        switch (entity.direction) {
            case "up":
                entityArea.y -= entity.speed;
                break;
            case "down":
                entityArea.y += entity.speed;
                break;
            case "left":
                entityArea.x -= entity.speed;
                break;
            case "right":
                entityArea.x += entity.speed;
                break;
        }

        // Player rectangle in world space
        int playerLeftWorldX = gamePanel.player.worldX + gamePanel.player.solidAreaDefaultX;
        int playerTopWorldY = gamePanel.player.worldY + gamePanel.player.solidAreaDefaultY;
        Rectangle playerArea = new Rectangle(playerLeftWorldX, playerTopWorldY,
                gamePanel.player.solidArea.width, gamePanel.player.solidArea.height);

        // Collision check
        if (entityArea.intersects(playerArea)) {
            entity.collisionOn = true;
            gamePanel.player.collisionOn = true; // block player as well
            contactPlayer = true;
        }
        return contactPlayer;
    }
}