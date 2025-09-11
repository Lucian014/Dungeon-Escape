package interactive_tile;

import entity.Entity;
import game.GamePanel;

import java.awt.*;

public class IT_DestructibleWall extends InteractiveTile{

    GamePanel gamePanel;
    public IT_DestructibleWall(GamePanel gamePanel, int col, int row) {
        super(gamePanel, col, row);
        this.gamePanel = gamePanel;
        this.worldX = col * gamePanel.tileSize;
        this.worldY = row * gamePanel.tileSize;
        collision = true;
        down1 = setup("tiles_interactive/destructiblewall",1,1);
        destructible = true;
        life = 3;

    }
    public boolean isCorrectItem(Entity entity) {

        return entity.currentWeapon.type == type_pickaxe;
    }
    public void playSE(){
        gamePanel.playSE(20);
    }

    public Color getParticleColor() {
        return new Color(65,65,65);
    }
    public int getParticleSize() {
        int size = 6; // 6 pixels
        return size;
    }
    public int getParticleSpeed() {
        int speed = 1;
        return speed;
    }

    public int getParticleMaxLife() {
        int maxLife = 20;
        return maxLife;
    }
}
