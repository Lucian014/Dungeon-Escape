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
        down1 = setup("items/potion_red",gamePanel.tileSize,gamePanel.tileSize);
        description = "[Red Potion]\nHeals you by " + value + " .";
    }
    public void use(Entity entity) {

        gamePanel.gameState = gamePanel.dialogueState;
        gamePanel.ui.currentDialogue = "You drink the "+ name + " !\n" + "Your life has been recovered by " + value + " .";
        entity.life += value;
        gamePanel.playSE(10);
    }
}
