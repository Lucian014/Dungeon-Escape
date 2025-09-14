package object;

import entity.Entity;
import game.GamePanel;
import game.Projectile;

import java.awt.*;

public class OBJ_Fireball extends Projectile {

    GamePanel gamePanel;
    public static final String objName = "Fireball";
    public OBJ_Fireball(GamePanel gamePanel) {
        super(gamePanel);
        this.gamePanel = gamePanel;

        name = objName;
        speed = 10;
        maxLife = 80;
        life = maxLife;
        attack = 4;
        useCost = 1;
        knockBackPower = 5;
        alive = false;
        getImage();
    }

    public void getImage() {

        up1 = setup("projectiles/fireball_up_1",1,1);
        up2 = setup("projectiles/fireball_up_2",1,1);
        down1 = setup("projectiles/fireball_down_1",1,1);
        down2 = setup("projectiles/fireball_down_2",1,1);
        left1 = setup("projectiles/fireball_left_1",1,1);
        left2 = setup("projectiles/fireball_left_2",1,1);
        right1 = setup("projectiles/fireball_right_1",1,1);
        right2 = setup("projectiles/fireball_right_2",1,1);

    }
    public boolean haveResource(Entity user) {

        boolean haveResource = false;
        if(user.mana >= useCost) {
            haveResource = true;
        }
        return haveResource;
    }

    public void subtractResource(Entity user) {
        user.mana -= 2 * useCost;
    }

    public Color getParticleColor() {
        return new Color(240,50,0);
    }
    public int getParticleSize() {
        int size = 10; // 6 pixels
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
