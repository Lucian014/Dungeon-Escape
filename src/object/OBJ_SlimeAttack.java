package object;

import game.GamePanel;
import game.Projectile;

import java.awt.*;

public class OBJ_SlimeAttack extends Projectile {

    GamePanel gamePanel;

    public OBJ_SlimeAttack(GamePanel gamePanel) {
        super(gamePanel);
        this.gamePanel = gamePanel;

        name = "Slime Attack";
        speed = 5;
        maxLife = 80;
        life = maxLife;
        attack = 4;
        useCost = 1;
        alive = false;
        getImage();
    }
    public void getImage() {

        up1 = setup("projectiles/slime_attack_1",1,1);
        up2 = setup("projectiles/slime_attack_2",1,1);
        down1 = setup("projectiles/slime_attack_1",1,1);
        down2 = setup("projectiles/slime_attack_2",1,1);
        left1 = setup("projectiles/slime_attack_1",1,1);
        left2 = setup("projectiles/slime_attack_2",1,1);
        right1 = setup("projectiles/slime_attack_1",1,1);
        right2 = setup("projectiles/slime_attack_2",1,1);

    }

    public Color getParticleColor() {
        return new Color(62, 134, 49);
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
