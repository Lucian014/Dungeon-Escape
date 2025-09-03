package entity;

import game.GamePanel;

import java.util.Random;

public class NPC_Priest extends Entity{

    public NPC_Priest(GamePanel gamePanel) {
        super(gamePanel);

        direction = "down";
        speed = 1;
        getImage();
        setDialogue();
    }

    public void setDialogue(){

        dialogues[0] = "Hello my son.";
        dialogues[1] = "So you've come to this island to get \nit.";
        dialogues[2] = "Many have tried but failed.";
        dialogues[3] = "Good luck warrior.";

    }
    public void getImage(){
        up1 = setup("npc/priest/priest_up_01");
        up2 = setup("npc/priest/priest_up_02");
        down1 = setup("npc/priest/priest_down_01");
        down2 = setup("npc/priest/priest_down_02");
        left1 = setup("npc/priest/priest_left_01");
        left2 = setup("npc/priest/priest_left_02");
        right1 = setup("npc/priest/priest_right_01");
        right2 = setup("npc/priest/priest_right_02");

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
    }
    public void speak() {
        super.speak();
    }
}
