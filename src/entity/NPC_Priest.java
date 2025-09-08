package entity;

import game.GamePanel;

import java.awt.*;
import java.util.Random;

public class NPC_Priest extends Entity{

    public NPC_Priest(GamePanel gamePanel) {
        super(gamePanel);

        direction = "down";
        speed = 2;
        getImage();
        setDialogue();
        solidArea = new Rectangle(8,16,30,30);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    public void setDialogue(){

        dialogues[0] = "Hello my son.";
        dialogues[1] = "So you've come to this island to get \nit.";
        dialogues[2] = "Many have tried but failed.";
        dialogues[3] = "Good luck warrior.";

    }
    public void getImage(){
        up1 = setup("npc/priest/priest_up_01",1,1);
        up2 = setup("npc/priest/priest_up_02",1,1);
        down1 = setup("npc/priest/priest_down_01",1,1);
        down2 = setup("npc/priest/priest_down_02",1,1);
        left1 = setup("npc/priest/priest_left_01",1,1);
        left2 = setup("npc/priest/priest_left_02",1,1);
        right1 = setup("npc/priest/priest_right_01",1,1);
        right2 = setup("npc/priest/priest_right_02",1,1);

    }

    public void setAction() {

        if (onPath) {

            int goalCol = 12;
            int goalRow = 9;

            searchPath(goalCol, goalRow);

        } else {

            actionLockCounter++;
            if (actionLockCounter == 120) {
                Random random = new Random();
                int i = random.nextInt(100) + 1;
                if (i <= 25) {
                    direction = "up";
                }
                if (i > 25 && i <= 50) {
                    direction = "down";
                }
                if (i > 50 && i <= 75) {
                    direction = "left";
                }
                if (i > 75) {
                    direction = "right";
                }
                actionLockCounter = 0;
            }
        }
    }
    public void speak() {
        super.speak();

        onPath = true;
    }
}
