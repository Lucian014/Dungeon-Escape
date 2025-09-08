package object;

import entity.Entity;
import game.GamePanel;


public class OBJ_Key extends Entity {

    GamePanel gamePanel;
    public OBJ_Key(GamePanel gamePanel){
        super(gamePanel);
        this.gamePanel = gamePanel;
        name =  "Key";
        down1 = setup("items/key",1,1);
        description = "[" + name + "]\nIt opens chests or doors.";
        price = 50;
        type = type_consumable;
        stackable = true;
    }

    public boolean use(Entity entity) {

        gamePanel.gameState = gamePanel.dialogueState;

        int objIndex = getDetected(entity, gamePanel.object, "Door");
        if (objIndex != 999) {
            gamePanel.ui.currentDialogue = "You use the " + name + " and open the door.";
            gamePanel.sound.playSoundEffect(3);
            gamePanel.object[gamePanel.currentMap][objIndex] = null;
            return true;
        }
        else {
            gamePanel.ui.currentDialogue = "Are you ok?";
            return false;
        }
    }
}
