package monster;

import entity.Entity;
import game.GamePanel;
import object.OBJ_Coin_Bronze;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;
import object.OBJ_SlimeAttack;

import java.util.Random;

public class MON_Bat extends Entity {
    GamePanel gamePanel;
    public MON_Bat(GamePanel gamePanel) {
        super(gamePanel);
        this.gamePanel = gamePanel;
        type = type_monster;
        name = "Bat";
        defaultSpeed = 4;
        speed = defaultSpeed;
        maxLife = 6;
        life = maxLife;
        attack = 5;
        defense = 0;
        exp = 7;
        projectile = new OBJ_SlimeAttack(gamePanel);

        solidArea.x = 3;
        solidArea.y = 15;
        solidArea.width = 42;
        solidArea.height = 21;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    public void getImage() {

        up1 = setup("monster/bat_down_1",1,1);
        up2 = setup("monster/bat_down_2",1,1);
        down1 = setup("monster/bat_down_1",1,1);
        down2 = setup("monster/bat_down_2",1,1);
        left1 = setup("monster/bat_down_1",1,1);
        left2 = setup("monster/bat_down_2",1,1);
        right1 = setup("monster/bat_down_1",1,1);
        right2 = setup("monster/bat_down_2",1,1);
    }

    public void setAction() {

        if(onPath) {
            //Check if it stops chasing
            //checkStopChasingOrNot(gamePanel.player, 15,100);
            //Search the direction to the player
            //searchPath(getGoalCol(gamePanel.player),getGoalRow(gamePanel.player));
            //Check if it shoots a projectile
            //checkShootOrNot(200,30);
        } else {
            //Check if it starts chasing
            //checkStartChasingOrNot(gamePanel.player, 5,100);

            //Get a random direction
            getRandomDirection(10);
        }
    }
    public void damageReaction() {

        actionLockCounter = 0;
        //onPath = true;
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
