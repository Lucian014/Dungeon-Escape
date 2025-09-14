package entity;

import game.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class NPC_Priest extends Entity{

    BufferedImage sheet;
    BufferedImage[] Priest = new BufferedImage[8];
    public NPC_Priest(GamePanel gamePanel) {
        super(gamePanel);

        direction = "down";
        speed = 2;
        solidArea = new Rectangle(8,16,30,30);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        dialogueSet = -1;

        sheet = loadARGB("/npc/priest/Priest.png");
        setDialogue();
        getImage();


    }

    public void setDialogue(){

        dialogues[0][0] = "Hello my son.";
        dialogues[0][1] = "So you've come to this island to get it, is that right?\nThe amulet of life";
        dialogues[0][2] = "Many have tried but failed.";
        dialogues[0][3] = "I have to warn you though, to get to it you have to\naquire two  keys.";
        dialogues[0][4] = "One of them is hidden on this island. The second one \nis on  another island.";
        dialogues[0][5] = "Only a creature of the sky can take you there. But \nyou have to be worthy.";
        dialogues[0][6] = "Your purpose has to be of pure heart. Otherwise \nthey'll drop you in the water to die.";
        dialogues[0][7] = "Also, the key is guarded by a thief. A goblin king.";
        dialogues[0][8] = "With the two keys you'll be able to get into the\n Skeleton Lord's dungeon.";
        dialogues[0][9] = "That dungeon is full of dangers so you'll need a\nsource of light to navigate through.There\n is a merchant on the other island which can help.";
        dialogues[0][10] = "Good luck warrior. Shall you face no defeat.";

        dialogues[1][0] = "If you become tired, rest at the water.";
        dialogues[1][1] = "You can drink from the water to heal. Though \n if you rest monsters will appear";
        dialogues[1][2] = "Anyway, try to not die.";

        dialogues[2][0] = "For how long have i been here?";



    }
    public void getImage(){

        for(int i = 0; i < Priest.length; i++) {
            Priest[i] = sheet.getSubimage(i * 16, 0, 16, 16);
        }
        down1 = setup(Priest[0],1,1);
        down2 = setup(Priest[1],1,1);
        left1 = setup(Priest[2],1,1);
        left2 = setup(Priest[3],1,1);
        right1 = setup(Priest[4],1,1);
        right2 = setup(Priest[5],1,1);
        up1 = setup(Priest[6],1,1);
        up2 = setup(Priest[7],1,1);

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
