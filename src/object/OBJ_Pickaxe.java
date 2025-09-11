package object;

import entity.Entity;
import game.GamePanel;

public class OBJ_Pickaxe extends Entity {

    public static final String objName = "Pickaxe";
    public OBJ_Pickaxe(GamePanel gamePanel) {
        super(gamePanel);

        type = type_pickaxe;
        name = objName;
        down1 = setup("items/pickaxe", 1, 1);
        attackValue = 6;
        attackArea.width = 30;
        attackArea.height = 30;
        description = "[Pickaxe]\nA pickaxe to dig your way \nout of this hell.";
        price = 75;
        knockBackPower = 2;
        motion1_duration = 10;
        motion2_duration = 20;
    }
}
