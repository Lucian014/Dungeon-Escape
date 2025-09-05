package object;

import game.GamePanel;
import game.Projectile;

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

        up1 = setup("projectiles/slime_attack_1",gamePanel.tileSize,gamePanel.tileSize);
        up2 = setup("projectiles/slime_attack_2",gamePanel.tileSize,gamePanel.tileSize);
        down1 = setup("projectiles/slime_attack_1",gamePanel.tileSize,gamePanel.tileSize);
        down2 = setup("projectiles/slime_attack_2",gamePanel.tileSize,gamePanel.tileSize);
        left1 = setup("projectiles/slime_attack_1",gamePanel.tileSize,gamePanel.tileSize);
        left2 = setup("projectiles/slime_attack_2",gamePanel.tileSize,gamePanel.tileSize);
        right1 = setup("projectiles/slime_attack_1",gamePanel.tileSize,gamePanel.tileSize);
        right2 = setup("projectiles/slime_attack_2",gamePanel.tileSize,gamePanel.tileSize);

    }
}
