package monster;

import entity.Entity;
import game.GamePanel;
import object.OBJ_SlimeAttack;

import java.util.Random;

public class MON_GreenSlime extends Entity {
    GamePanel gamePanel;
    public MON_GreenSlime(GamePanel gamePanel){
        super(gamePanel);
        this.gamePanel = gamePanel;
        type = type_monster;
        name = "Green Slime";
        speed = 1;
        maxLife = 6;
        life = maxLife;
        attack = 5;
        defense = 0;
        exp = 2;
        projectile = new OBJ_SlimeAttack(gamePanel);

        solidArea.x = 3;
        solidArea.y = 18;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    public void getImage() {

        up1 = setup("monster/greenslime_down_1",1,1);
        up2 = setup("monster/greenslime_down_2",1,1);
        down1 = setup("monster/greenslime_down_1",1,1);
        down2 = setup("monster/greenslime_down_2",1,1);
        left1 = setup("monster/greenslime_down_1",1,1);
        left2 = setup("monster/greenslime_down_2",1,1);
        right1 = setup("monster/greenslime_down_1",1,1);
        right2 = setup("monster/greenslime_down_2",1,1);
    }

    public void setAction() {

        actionLockCounter++;

        if(actionLockCounter == 80){
            Random random = new Random();
            int i = random.nextInt(100) + 1;
            if (i <= 25) {
                direction = "up";
            }
            if(i > 25 && i <= 50) {
                direction = "down";
            }
            if(i > 50 && i <= 75) {
                direction = "left";
            }
            if(i > 75) {
                direction = "right";
            }
            actionLockCounter = 0;
        }
        int i = new Random().nextInt(100) + 1;
        if(i > 99 && !projectile.alive && shotAvailableCounter >= 30) {
            projectile.set(worldX,worldY,direction,true,this);
            gamePanel.projectileList.add(projectile);
            shotAvailableCounter = 0;
        }
        if(shotAvailableCounter < 30) {
            shotAvailableCounter++;
        }
    }
    public void damageReaction() {

        actionLockCounter = 0;
        direction = gamePanel.player.direction;
    }
}
