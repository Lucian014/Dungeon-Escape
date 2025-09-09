package object;

import entity.Entity;
import game.GamePanel;

public class OBJ_Sword_Normal extends Entity {

    public OBJ_Sword_Normal(GamePanel gamePanel) {
        super(gamePanel);

        name = "Normal Sword";
        down1 = setup("items/sword_normal", 1, 1);
        attackValue = 4;
        attackArea.width = 36;
        attackArea.height = 36;
        description = "[" + name + "]\nAn old sword.";
        type = type_sword;
        price = 60;
        knockBackPower = 2;
        motion1_duration = 5;
        motion2_duration = 25;
    }
}
