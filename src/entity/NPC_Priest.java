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
        solidArea = new Rectangle(8,16,30,30);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        dialogueSet = -1;

        setDialogue();

    }

    public void setDialogue(){

        dialogues[0][0] = "Hello my son.";
        dialogues[0][1] = "So you've come to this island to get \nit.";
        dialogues[0][2] = "Many have tried but failed.";
        dialogues[0][3] = "Good luck warrior.";

        dialogues[1][0] = "If you become tired, rest at the water.";
        dialogues[1][1] = "You can drink from the water to heal. Though \n if you rest monsters will appear";
        dialogues[1][2] = "Anyway, try to not die.";

        dialogues[2][0] = "For how long have i been here?";



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

        facePlayer();
        startDialogue(this,dialogueSet);

        dialogueSet++;

        if(dialogues[dialogueSet][0] == null) {

            dialogueSet--;

        }
        //onPath = true;
    }
}
