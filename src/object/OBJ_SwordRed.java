package object;

import entity.Entity;
import game.GamePanel;

public class OBJ_SwordRed extends Entity {

    public static final String objName = "Sword of flame";
    public OBJ_SwordRed(GamePanel gamePanel) {
        super(gamePanel);

        name = objName;
        down1 = setup("items/sword_red", 1, 1);
        attackValue = 6;
        attackArea.width = 36;
        attackArea.height = 36;
        description = "[" + name + "]\nA flame made from a \ndragon's breath.";
        type = type_sword_red;
        price = 80;
        knockBackPower = 4;
        motion1_duration = 8;
        motion2_duration = 30;
    }
}
