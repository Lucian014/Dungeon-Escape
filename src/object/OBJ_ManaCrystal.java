package object;

import entity.Entity;
import game.GamePanel;

public class OBJ_ManaCrystal extends Entity {

    public static final String objName = "Mana Crystal";
    GamePanel gamePanel;
    public OBJ_ManaCrystal(GamePanel gamePanel) {
        super(gamePanel);
        this.gamePanel = gamePanel;

        type = type_pickUpOnly;
        name = objName;
        value = 2;
        down1 = setup("items/manacrystal_full",1,1);
        image = setup("items/manacrystal_full",1,1);
        image2 = setup("items/manacrystal_blank",1,1);
    }

    public boolean use(Entity entity) {

        gamePanel.playSE(1);
        gamePanel.ui.addMessage("Mana +" + value);
        entity.mana += value;
        return true;
    }
}
