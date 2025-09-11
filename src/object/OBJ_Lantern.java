package object;

import entity.Entity;
import game.GamePanel;

public class OBJ_Lantern extends Entity {

    public static final String objName = "Lantern";

    public OBJ_Lantern(GamePanel gamePanel) {
        super(gamePanel);

        type = type_light;
        name = objName;
        down1 = setup("items/lantern",1,1);
        description = "[Lantern]\nIlluminates your way out \nof trouble.";
        price = 200;
        lightRadius = 250;

    }
}
