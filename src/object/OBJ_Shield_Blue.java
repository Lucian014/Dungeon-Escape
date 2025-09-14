package object;

import entity.Entity;
import game.GamePanel;

public class OBJ_Shield_Blue extends Entity {

    public static final String objName = "Blue Shield";

    public OBJ_Shield_Blue(GamePanel gamePanel) {
        super(gamePanel);

        type = type_shield_blue;
        name = objName;
        down1 = setup("items/shield_blue", 1, 1);
        defenseValue = 3;
        description = "[" + name + "]\nA shiny blue shield.";
        price = 80;

    }
}
