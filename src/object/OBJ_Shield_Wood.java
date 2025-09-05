package object;

import entity.Entity;
import game.GamePanel;

public class OBJ_Shield_Wood extends Entity {
    public OBJ_Shield_Wood(GamePanel gamePanel) {
        super(gamePanel);
        type = type_shield;
        name = "Wood Shield";
        down1 = setup("items/shield_wood", gamePanel.tileSize, gamePanel.tileSize);
        defenseValue = 1;
        description = "[" + name + "]\nAn old shield made from wood.";

    }
}
