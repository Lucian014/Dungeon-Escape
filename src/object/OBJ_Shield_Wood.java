package object;

import entity.Entity;
import game.GamePanel;

public class OBJ_Shield_Wood extends Entity {

    public static final String objName = "Wood Shield";
    public OBJ_Shield_Wood(GamePanel gamePanel) {
        super(gamePanel);
        type = type_shield;
        name = objName;
        down1 = setup("items/shield_wood", 1, 1);
        defenseValue = 1;
        description = "[" + name + "]\nAn old shield made from wood.";
        price = 35;
    }
}
