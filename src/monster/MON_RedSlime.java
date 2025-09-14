package monster;

import entity.Entity;
import game.GamePanel;
import object.OBJ_Coin_Bronze;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;
import object.OBJ_SlimeAttack;

import java.util.Random;

public class MON_RedSlime extends Entity {
    GamePanel gamePanel;
    public MON_RedSlime(GamePanel gamePanel){
        super(gamePanel);
        this.gamePanel = gamePanel;
        type = type_monster;
        name = "Red Slime";
        defaultSpeed = 1;
        speed = defaultSpeed;
        maxLife = 10;
        life = maxLife;
        attack = 8;
        defense = 0;
        exp = 2;
        solidArea.x = 3;
        solidArea.y = 18;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    public void getImage() {

        up1 = setup("monster/redslime_down_1",1,1);
        up2 = setup("monster/redslime_down_2",1,1);
        down1 = setup("monster/redslime_down_1",1,1);
        down2 = setup("monster/redslime_down_2",1,1);
        left1 = setup("monster/redslime_down_1",1,1);
        left2 = setup("monster/redslime_down_2",1,1);
        right1 = setup("monster/redslime_down_1",1,1);
        right2 = setup("monster/redslime_down_2",1,1);
    }

    public void setAction() {

        if(onPath) {
            //Check if it stops chasing
            checkStopChasingOrNot(gamePanel.player, 15,100);
            //Search the direction to the player
            searchPath(getGoalCol(gamePanel.player),getGoalRow(gamePanel.player));
            //Check if it shoots a projectile
        } else {
            //Check if it starts chasing
            checkStartChasingOrNot(gamePanel.player, 5,100);
            //Get a random direction
            getRandomDirection(120);
        }
    }
    public void damageReaction() {

        actionLockCounter = 0;
        onPath = true;
    }

    public void checkDrop() {

        int i = new Random().nextInt(100) + 1;

        //SET MONSTER DROP RATE

        if(i < 50) {
            dropItem(new OBJ_Coin_Bronze(gamePanel));
        }
        if(i >= 50 && i < 75) {
            dropItem(new OBJ_Heart(gamePanel));
        }
        if(i > 75){
            dropItem(new OBJ_ManaCrystal(gamePanel));
        }
    }
}
