package object;

import entity.Entity;
import game.GamePanel;

public class OBJ_Sword_Normal extends Entity {

    public static final String objName = "Normal Sword";
    public OBJ_Sword_Normal(GamePanel gamePanel) {
        super(gamePanel);

        name = objName;
        down1 = setup("items/sword_normal", 1, 1);
        attackValue = 2;
        attackArea.width = 36;
        attackArea.height = 36;
        description = "[" + name + "]\nAn old sword.";
        type = type_sword;
        price = 30;
        knockBackPower = 2;
        motion1_duration = 10;
        motion2_duration = 30;
    }
}
