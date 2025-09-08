package game;

import entity.Entity;



public class OBJ_Tent extends Entity {

    GamePanel gamePanel;

    public OBJ_Tent(GamePanel gamePanel) {
        super(gamePanel);
        this.gamePanel = gamePanel;
        type = type_consumable;
        name = "Tent";
        down1 = setup("items/tent", 1, 1);
        description = "[Tent]\nRest till the next morning.";
        price = 300;
        stackable = true;

    }

    public boolean use(Entity entity) {

        gamePanel.gameState = gamePanel.sleepState;
        gamePanel.sound.playSoundEffect(15);
        gamePanel.player.life = gamePanel.player.maxLife;
        gamePanel.player.mana = gamePanel.player.maxMana;
        gamePanel.player.getSleepingImage(down1);
        return true;
    }



}
