package object;

import entity.Entity;
import game.GamePanel;

public class OBJ_ManaCrystal extends Entity {

    GamePanel gamePanel;
    public OBJ_ManaCrystal(GamePanel gamePanel) {
        super(gamePanel);
        this.gamePanel = gamePanel;

        name = "Mana Crystal";
        image = setup("items/manacrystal_full",gamePanel.tileSize,gamePanel.tileSize);
        image2 = setup("items/manacrystal_blank",gamePanel.tileSize,gamePanel.tileSize);
    }
}
