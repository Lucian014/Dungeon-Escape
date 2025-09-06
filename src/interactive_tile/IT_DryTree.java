package interactive_tile;

import entity.Entity;
import game.GamePanel;

import java.awt.*;

public class IT_DryTree extends InteractiveTile{

    GamePanel gamePanel;
    public IT_DryTree(GamePanel gamePanel, int col, int row) {
        super(gamePanel, col, row);
        this.gamePanel = gamePanel;
        this.worldX = col * gamePanel.tileSize;
        this.worldY = row * gamePanel.tileSize;

        down1 = setup("tiles_interactive/drytree",gamePanel.tileSize,gamePanel.tileSize);
        destructible = true;
        life = 2;
    }
    public boolean isCorrectItem(Entity entity) {

        return entity.currentWeapon.type == type_axe;
    }
    public void playSE(){
        gamePanel.playSE(12);
    }
    public InteractiveTile getDestroyedForm() {
        return new IT_Trunk(gamePanel,worldX / gamePanel.tileSize, worldY / gamePanel.tileSize);
    }

    public Color getParticleColor() {
        return new Color(65,50,30);
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
