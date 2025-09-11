package object;

import entity.Entity;
import game.GamePanel;

public class OBJ_Potion_Red extends Entity {

    GamePanel gamePanel;
    public static final String objName = "Red Potion";
    public OBJ_Potion_Red(GamePanel gamePanel) {
        super(gamePanel);
        this.gamePanel = gamePanel;
        type = type_consumable;
        name = objName;
        value = 5;
        down1 = setup("items/potion_red",1,1);
        description = "[Red Potion]\nHeals you by " + value + " .";
        price = 25;
        stackable = true;

        setDialogue();
    }

    public void setDialogue() {
        dialogues[0][0] = "You drink the "+ name + " !\n" + "Your life has been recovered by " + value + " .";
    }
    public boolean use(Entity entity) {

        startDialogue(this,0);
        entity.life += value;
        gamePanel.playSE(10);
        return true;

    }
}
