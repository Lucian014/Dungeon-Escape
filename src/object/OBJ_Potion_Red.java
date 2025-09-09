package object;

import entity.Entity;
import game.GamePanel;

public class OBJ_Potion_Red extends Entity {

    GamePanel gamePanel;

    public OBJ_Potion_Red(GamePanel gamePanel) {
        super(gamePanel);
        this.gamePanel = gamePanel;
        type = type_consumable;
        name = "Red Potion";
        value = 5;
        down1 = setup("items/potion_red",1,1);
        description = "[Red Potion]\nHeals you by " + value + " .";
        price = 25;
        stackable = true;
    }
    public boolean use(Entity entity) {

        gamePanel.gameState = gamePanel.dialogueState;
        gamePanel.ui.currentDialogue = "You drink the "+ name + " !\n" + "Your life has been recovered by " + value + " .";
        entity.life += value;
        gamePanel.playSE(10);
        return true;

    }
}
