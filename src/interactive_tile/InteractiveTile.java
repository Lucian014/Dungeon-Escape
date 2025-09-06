package interactive_tile;

import entity.Entity;
import game.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class InteractiveTile extends Entity {

    GamePanel gamePanel;
    public boolean destructible = false;

    public InteractiveTile(GamePanel gamePanel,int col, int row) {
        super(gamePanel);
        this.gamePanel = gamePanel;
    }
    public void update() {
        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter == 20) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }
    public void draw(Graphics2D graphics2D) {
        int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
        int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;

        if (worldX + gamePanel.tileSize > gamePanel.player.worldX - gamePanel.player.screenX &&
                worldX - gamePanel.tileSize < gamePanel.player.worldX + gamePanel.player.screenX &&
                worldY + gamePanel.tileSize > gamePanel.player.worldY - gamePanel.player.screenY &&
                worldY - gamePanel.tileSize < gamePanel.player.worldY + gamePanel.player.screenY) {

            graphics2D.drawImage(down1, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);
        }
    }
    public void playSE(){}
    public InteractiveTile getDestroyedForm() {return null;}
    public boolean isCorrectItem(Entity entity) {return false;}

}
