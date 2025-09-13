package monster;

import entity.Entity;
import game.GamePanel;
import object.OBJ_Coin_Bronze;
import object.OBJ_Door_Iron;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;

import java.util.Random;

public class MON_SkeletonLord extends Entity {
    GamePanel gamePanel;
    public MON_SkeletonLord(GamePanel gamePanel) {

        super(gamePanel);
        this.gamePanel = gamePanel;
        type = type_monster;
        name = "Skeleton Lord";
        defaultSpeed = 1;
        speed = defaultSpeed;
        maxLife = 50;
        life = maxLife;
        attack = 10;
        knockBackPower = 5;
        defense = 2;
        exp = 50;
        boss = true;
        sleep = true;
        int size = gamePanel.tileSize * 5;
        solidArea.x = 48;
        solidArea.y = 48;
        solidArea.width = size - 48 * 2;
        solidArea.height = size - 48;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        attackArea.width = 170;
        attackArea.height = 170;
        motion1_duration = 25;
        motion2_duration = 50;
        getImage();
        getAttackImage();
        setDialogue();
    }

    public void getImage() {

        int i = 5;

        if(!inRage) {
            up1 = setup("monster/skeletonlord_up_1", i,i);
            up2 = setup("monster/skeletonlord_up_2", i,i);
            down1 = setup("monster/skeletonlord_down_1", i,i);
            down2 = setup("monster/skeletonlord_down_2", i,i);
            left1 = setup("monster/skeletonlord_left_1", i,i);
            left2 = setup("monster/skeletonlord_left_2", i,i);
            right1 = setup("monster/skeletonlord_right_1", i,i);
            right2 = setup("monster/skeletonlord_right_2", i,i);
        }
        if(inRage) {
            up1 = setup("monster/skeletonlord_phase2_up_1", i,i);
            up2 = setup("monster/skeletonlord_phase2_up_2", i,i);
            down1 = setup("monster/skeletonlord_phase2_down_1", i,i);
            down2 = setup("monster/skeletonlord_phase2_down_2", i,i);
            left1 = setup("monster/skeletonlord_phase2_left_1", i,i);
            left2 = setup("monster/skeletonlord_phase2_left_2", i,i);
            right1 = setup("monster/skeletonlord_phase2_right_1", i,i);
            right2 = setup("monster/skeletonlord_phase2_right_2", i,i);
        }

    }

    public void getAttackImage() {

        int i = 5;
        if(!inRage) {
            attackUp1 = setup("monster/skeletonlord_attack_up_1", i,i * 2);
            attackUp2 = setup("monster/skeletonlord_attack_up_2", i,i * 2);
            attackDown1 = setup("monster/skeletonlord_attack_down_1", i,i * 2);
            attackDown2 = setup("monster/skeletonlord_attack_down_2", i,i * 2);
            attackLeft1 = setup("monster/skeletonlord_attack_left_1", i * 2, i);
            attackLeft2 = setup("monster/skeletonlord_attack_left_2", i * 2, i);
            attackRight1 = setup("monster/skeletonlord_attack_right_1", i * 2, i);
            attackRight2 = setup("monster/skeletonlord_attack_right_2", i * 2, i);
        }
        if(inRage) {
            attackUp1 = setup("monster/skeletonlord_phase2_attack_up_1", i,i * 2);
            attackUp2 = setup("monster/skeletonlord_phase2_attack_up_2", i,i * 2);
            attackDown1 = setup("monster/skeletonlord_phase2_attack_down_1", i,i * 2);
            attackDown2 = setup("monster/skeletonlord_phase2_attack_down_2", i,i * 2);
            attackLeft1 = setup("monster/skeletonlord_phase2_attack_left_1", i * 2, i);
            attackLeft2 = setup("monster/skeletonlord_phase2_attack_left_2", i * 2, i);
            attackRight1 = setup("monster/skeletonlord_phase2_attack_right_1", i * 2, i);
            attackRight2 = setup("monster/skeletonlord_phase2_attack_right_2", i * 2, i);
        }

    }
    public void setDialogue() {

        dialogues[0][0] = "No one can steal my treasure!";
        dialogues[0][1] = "You will die here!";
        dialogues[0][2] = "WELCOME TO YOUR DOOM!";

    }
    public void setAction() {

            if(!inRage && life < maxLife / 2) {
                inRage = true;
                getImage();
                getAttackImage();
                defaultSpeed++;
                speed = defaultSpeed;
                attack *= 2;

            }

            if (getTileDistance(gamePanel.player) < 10) {

                moveTowardPlayer(60);

            } else {

                getRandomDirection(120);

            }
            if(!attacking) {
                checkAttackOrNot(60,gamePanel.tileSize * 6,gamePanel.tileSize * 3);
            }
        }

    public void damageReaction() {

        actionLockCounter = 0;
        onPath = true;
        sleep = false;
    }

    public void checkDrop() {

        gamePanel.bossBattleOn = false;
        gamePanel.player.killSkeletonLord = true;
        // Restore the previous music

        gamePanel.sound.stopMusic();
        gamePanel.sound.playMusic(19);

        //Remove the iron doors

        for(int i = 0; i < gamePanel.object[1].length; i++) {

            if(gamePanel.object[gamePanel.currentMap][i] != null && gamePanel.object[gamePanel.currentMap][i].name.equals(OBJ_Door_Iron.objName)) {
                gamePanel.sound.playSoundEffect(21);
                gamePanel.object[gamePanel.currentMap][i] = null;
            }
        }


        int i = new Random().nextInt(100) + 1;

        //SET MONSTER DROP RATE

        if (i < 50) {
            dropItem(new OBJ_Coin_Bronze(gamePanel));
        }
        if (i >= 50 && i < 75) {
            dropItem(new OBJ_Heart(gamePanel));
        }
        if (i > 75) {
            dropItem(new OBJ_ManaCrystal(gamePanel));
        }
    }

    public void update() {
        super.update();
    }

}
