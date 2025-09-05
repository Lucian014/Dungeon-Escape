package object;

import entity.Entity;
import game.GamePanel;

public class OBJ_Axe extends Entity {
    public OBJ_Axe(GamePanel gamePanel) {
        super(gamePanel);

        type = type_axe;
        name = "Woodcutter's Axe";
        down1 = setup("items/axe", gamePanel.tileSize, gamePanel.tileSize);
        attackValue = 2;
        attackArea.width = 30;
        attackArea.height = 30;
        description = "[Woodcutter's Axe]\nA bit rusty but can still cut things.";
    }
}
