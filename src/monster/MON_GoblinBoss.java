package monster;

import entity.Entity;
import game.GamePanel;
import object.*;

import java.awt.image.BufferedImage;
import java.util.Random;

public class MON_GoblinBoss extends Entity {
    GamePanel gamePanel;
    BufferedImage sheet;
    public MON_GoblinBoss(GamePanel gamePanel) {

        super(gamePanel);
        this.gamePanel = gamePanel;
        type = type_monster;
        name = "Goblin Boss";
        defaultSpeed = 1;
        speed = defaultSpeed;
        maxLife = 50;
        life = maxLife;
        attack = 6;
        knockBackPower = 5;
        defense = 2;
        exp = 50;
        boss = true;
        sleep = true;
        solidArea.x = 8;
        solidArea.y = 8;
        solidArea.width = 40;
        solidArea.height = 88;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        attackArea.width = 48;
        attackArea.height = 48;
        motion1_duration = 20;
        motion2_duration = 50;
        sheet = loadARGB("/monster/goblinsword.png");
        getImage();
        getAttackImage();
        setDialogue();


    }

    public void getImage() {

        int i = 2;
        int size = 64;
            up1 = cut(sheet, 5 * size,128,64,64,i,i);
            up2 = cut(sheet,7 * size,128,64,64,i,i);
            down1 = cut(sheet,0,0,64,64,i,i);
            down2 = cut(sheet,3 * size,0,64,64,i,i);
            left1 = cut(sheet,4 * size,192,64,64,i,i);
            left2 = cut(sheet, 6 * size,192,64,64,i,i);
            right1 = cut(sheet,2 * size,64,64,64,i,i);
            right2 = cut(sheet, size,64,64,64,i,i);
    }



    public void getAttackImage() {

        int i = 2;
        int size = 64;
        attackUp1 = cut(sheet,10 * size,128,64,60,i,i);
        attackUp2 = cut(sheet,9 * size,128,64,60,i,i);
        attackDown1 = cut(sheet,10 * size,0,64,64,i,i);
        attackDown2 = cut(sheet,9 * size,0,64,64,i,i);
        attackLeft1 = cut(sheet,7 * size,192,64,60,i,i);
        attackLeft2 = cut(sheet,8 * size ,192,64,60,i,i);
        attackRight1 = cut(sheet,7 * size,64,64,64,i,i);
        attackRight2 = cut(sheet,8 * size,64,64,64,i,i);

    }
    public void setDialogue() {

        dialogues[0][0] = "You've got guts to come here!";
        dialogues[0][1] = "Haven't fought a human in a while!";
        dialogues[0][2] = "You'll look great in my collection!";

    }
    public void setAction() {

        getImage();
        getAttackImage();
        if (getTileDistance(gamePanel.player) < 10) {
            moveTowardPlayer(60);
        } else {
            getRandomDirection(120);
        }
        if (!attacking) {
            checkAttackOrNot(60, gamePanel.tileSize * 2, gamePanel.tileSize * 1);
        }
    }
    public void damageReaction() {

        actionLockCounter = 0;
        onPath = true;
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
        dropItem(new OBJ_Key(gamePanel));
    }

    public void update() {
        super.update();
    }

}
