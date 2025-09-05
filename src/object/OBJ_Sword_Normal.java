package object;

import entity.Entity;
import game.GamePanel;

public class OBJ_Sword_Normal extends Entity {

    public OBJ_Sword_Normal(GamePanel gamePanel) {
        super(gamePanel);

        name = "Normal Sword";
        down1 = setup("items/sword_normal", gamePanel.tileSize, gamePanel.tileSize);
        attackValue = 4;
        attackArea.width = 36;
        attackArea.height = 36;
        description = "[" + name + "]\nAn old sword.";
        type = type_sword;
    }
}
