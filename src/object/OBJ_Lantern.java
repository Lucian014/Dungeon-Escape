package object;

import entity.Entity;
import game.GamePanel;

public class OBJ_Lantern extends Entity {


    public OBJ_Lantern(GamePanel gamePanel) {
        super(gamePanel);

        type = type_light;
        name = "Lantern";
        down1 = setup("items/lantern",gamePanel.tileSize,gamePanel.tileSize);
        description = "[Lantern]\nIlluminates your way out \nof trouble.";
        price = 200;
        lightRadius = 250;

    }
}
