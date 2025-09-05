package object;

import entity.Entity;
import game.GamePanel;

public class OBJ_Shield_Blue extends Entity {

    public OBJ_Shield_Blue(GamePanel gamePanel) {
        super(gamePanel);

        type = type_shield;
        name = "Blue Shield";
        down1 = setup("items/shield_blue", gamePanel.tileSize, gamePanel.tileSize);
        defenseValue = 2;
        description = "[" + name + "]\nA shiny blue shield.";


    }
}
