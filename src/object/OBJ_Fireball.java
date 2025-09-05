package object;

import entity.Entity;
import game.GamePanel;
import game.Projectile;

public class OBJ_Fireball extends Projectile {

    GamePanel gamePanel;
    public OBJ_Fireball(GamePanel gamePanel) {
        super(gamePanel);
        this.gamePanel = gamePanel;

        name = "Fireball";
        speed = 10;
        maxLife = 80;
        life = maxLife;
        attack = 4;
        useCost = 1;
        alive = false;
        getImage();
    }

    public void getImage() {

        up1 = setup("projectiles/fireball_up_1",gamePanel.tileSize,gamePanel.tileSize);
        up2 = setup("projectiles/fireball_up_2",gamePanel.tileSize,gamePanel.tileSize);
        down1 = setup("projectiles/fireball_down_1",gamePanel.tileSize,gamePanel.tileSize);
        down2 = setup("projectiles/fireball_down_2",gamePanel.tileSize,gamePanel.tileSize);
        left1 = setup("projectiles/fireball_left_1",gamePanel.tileSize,gamePanel.tileSize);
        left2 = setup("projectiles/fireball_left_2",gamePanel.tileSize,gamePanel.tileSize);
        right1 = setup("projectiles/fireball_right_1",gamePanel.tileSize,gamePanel.tileSize);
        right2 = setup("projectiles/fireball_right_2",gamePanel.tileSize,gamePanel.tileSize);

    }
    public boolean haveResource(Entity user) {

        boolean haveResource = false;
        if(user.mana >= useCost) {
            haveResource = true;
        }
        return haveResource;
    }

    public void subtractResource(Entity user) {
        user.mana -= useCost;
    }
}
