package object;

import entity.Entity;
import game.GamePanel;



public class OBJ_Door_Iron extends Entity {

    GamePanel gamePanel;
    public OBJ_Door_Iron(GamePanel gamePanel){
        super(gamePanel);
        this.gamePanel = gamePanel;
        type = type_obstacle;
        name =  "Iron Door";
        down1 = setup("items/door_iron",1,1);
        collision = true;
        solidArea.x = 0;
        solidArea.y = 16;
        solidArea.width = 48;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        setDialogue();
    }

    public void setDialogue() {

        dialogues[0][0] = "It won't even flinch !";

    }

    public void interact() {

        gamePanel.gameState = gamePanel.dialogueState;
        gamePanel.keyHandler.enterPressed = false;
        startDialogue(this,0);
    }
}
