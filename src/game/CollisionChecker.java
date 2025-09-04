package game;

import entity.Entity;

import java.awt.*;

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
    public int checkObject(Entity entity, boolean player) {
        int index = 999;

        for (int i = 0; i < gamePanel.object.length; i++) {
            if (gamePanel.object[i] != null) {

                // Backup entity solid area
                int entitySolidX = entity.solidArea.x;
                int entitySolidY = entity.solidArea.y;

                // Backup object solid area
                int objectSolidX = gamePanel.object[i].solidArea.x;
                int objectSolidY = gamePanel.object[i].solidArea.y;

                // Get positions in world
                entity.solidArea.x = entity.worldX + entity.solidAreaDefaultX;
                entity.solidArea.y = entity.worldY + entity.solidAreaDefaultY;
                gamePanel.object[i].solidArea.x = gamePanel.object[i].worldX + gamePanel.object[i].solidAreaDefaultX;
                gamePanel.object[i].solidArea.y = gamePanel.object[i].worldY + gamePanel.object[i].solidAreaDefaultY;

                // Simulate movement
                switch (entity.direction) {
                    case "up":    entity.solidArea.y -= entity.speed; break;
                    case "down":  entity.solidArea.y += entity.speed; break;
                    case "left":  entity.solidArea.x -= entity.speed; break;
                    case "right": entity.solidArea.x += entity.speed; break;
                }

                if (entity.solidArea.intersects(gamePanel.object[i].solidArea)) {
                    if (gamePanel.object[i].collision) {
                        entity.collisionOn = true;
                    }
                    if (player) {
                        index = i; // so player can pick up object
                    }
                }

                // Reset solid areas
                entity.solidArea.x = entitySolidX;
                entity.solidArea.y = entitySolidY;
                gamePanel.object[i].solidArea.x = objectSolidX;
                gamePanel.object[i].solidArea.y = objectSolidY;
            }
        }
        return index;
    }
    //NPC OR MONSTER
    public int checkEntity(Entity entity, Entity[] target) {
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

        for (int i = 0; i < target.length; i++) {
            if (target[i] != null && target[i] != entity) {
                int targetLeftWorldX = target[i].worldX + target[i].solidAreaDefaultX;
                int targetTopWorldY = target[i].worldY + target[i].solidAreaDefaultY;

                Rectangle targetArea = new Rectangle(targetLeftWorldX, targetTopWorldY,
                        target[i].solidArea.width, target[i].solidArea.height);

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