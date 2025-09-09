package monster;

import entity.Entity;
import game.GamePanel;
import object.OBJ_Coin_Bronze;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;
import object.OBJ_SlimeAttack;

import java.util.Random;

public class MON_Orc extends Entity {
    GamePanel gamePanel;
    public MON_Orc(GamePanel gamePanel){
        super(gamePanel);
        this.gamePanel = gamePanel;
        type = type_monster;
        name = "Orc";
        defaultSpeed = 1;
        speed = defaultSpeed;
        maxLife = 10;
        life = maxLife;
        attack = 5;
        defense = 2;
        exp = 5;
        solidArea.x = 4;
        solidArea.y = 4;
        solidArea.width = 40;
        solidArea.height = 44;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        attackArea.width = 48;
        attackArea.height = 48;
        motion1_duration = 10;
        motion2_duration = 50;
        getImage();
        getAttackImage();
    }

    public void getImage() {

        up1 = setup("monster/orc_up_1",1,1);
        up2 = setup("monster/orc_up_2",1,1);
        down1 = setup("monster/orc_down_1",1,1);
        down2 = setup("monster/orc_down_2",1,1);
        left1 = setup("monster/orc_left_1",1,1);
        left2 = setup("monster/orc_left_2",1,1);
        right1 = setup("monster/orc_right_1",1,1);
        right2 = setup("monster/orc_right_2",1,1);
    }

    public void getAttackImage() {
        attackUp1 = setup("monster/orc_attack_up_1",1,2);
        attackUp2 = setup("monster/orc_attack_up_2",1,2);
        attackDown1 = setup("monster/orc_attack_down_1",1,2);
        attackDown2 = setup("monster/orc_attack_down_2",1,2);
        attackLeft1 = setup("monster/orc_attack_left_1",2,1);
        attackLeft2 = setup("monster/orc_attack_left_2",2,1);
        attackRight1 = setup("monster/orc_attack_right_1",2,1);
        attackRight2 = setup("monster/orc_attack_right_2",2,1);
    }

    public void setAction() {
        // Always check for attacks first, regardless of path state
        if(!attacking) {
            checkAttackOrNot(20, gamePanel.tileSize * 2, gamePanel.tileSize * 1);
        }

        // Then handle movement/pathfinding
        if(!attacking) {  // Only do AI movement if not attacking
            if(onPath) {
                checkStopChasingOrNot(gamePanel.player, 15,100);
                searchPath(getGoalCol(gamePanel.player),getGoalRow(gamePanel.player));
            } else {
                checkStartChasingOrNot(gamePanel.player, 5,100);
                getRandomDirection();
            }
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

    public void update() {
        super.update();
    }
}
