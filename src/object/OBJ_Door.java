package object;

import entity.Entity;
import game.GamePanel;



public class OBJ_Door extends Entity {

    GamePanel gamePanel;
    public OBJ_Door(GamePanel gamePanel){
        super(gamePanel);
        this.gamePanel = gamePanel;
        type = type_obstacle;
        name =  "Door";
        down1 = setup("items/door",1,1);
        collision = true;
        solidArea.x = 0;
        solidArea.y = 16;
        solidArea.width = 48;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

    }

    public void interact() {

        gamePanel.gameState = gamePanel.dialogueState;
        gamePanel.ui.currentDialogue = "You need a key to open this !";
        gamePanel.keyHandler.enterPressed = false;
    }
}
