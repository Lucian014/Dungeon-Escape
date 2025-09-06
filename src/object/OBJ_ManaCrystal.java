package object;

import entity.Entity;
import game.GamePanel;

public class OBJ_ManaCrystal extends Entity {

    GamePanel gamePanel;
    public OBJ_ManaCrystal(GamePanel gamePanel) {
        super(gamePanel);
        this.gamePanel = gamePanel;

        type = type_pickUpOnly;
        name = "Mana Crystal";
        value = 2;
        down1 = setup("items/manacrystal_full",gamePanel.tileSize,gamePanel.tileSize);
        image = setup("items/manacrystal_full",gamePanel.tileSize,gamePanel.tileSize);
        image2 = setup("items/manacrystal_blank",gamePanel.tileSize,gamePanel.tileSize);
    }

    public void use(Entity entity) {

        gamePanel.playSE(1);
        gamePanel.ui.addMessage("Mana +" + value);
        entity.mana += value;
    }
}
