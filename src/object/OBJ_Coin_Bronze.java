package object;

import entity.Entity;
import game.GamePanel;

public class OBJ_Coin_Bronze extends Entity {

    GamePanel gamePanel;

    public OBJ_Coin_Bronze(GamePanel gamePanel) {
        super(gamePanel);
        this.gamePanel = gamePanel;

        type = type_pickUpOnly;
        name = "Bronze Coin";
        down1 = setup("items/coin_bronze",gamePanel.tileSize,gamePanel.tileSize);
        value = 1;

    }

    public void use(Entity entity) {

        gamePanel.playSE(1);
        gamePanel.ui.addMessage("Coin +" + value);
        gamePanel.player.coin += value;
    }
}
