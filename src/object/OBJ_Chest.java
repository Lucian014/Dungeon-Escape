package object;

import entity.Entity;
import game.GamePanel;

public class OBJ_Chest extends Entity {

    GamePanel gamePanel;
    Entity loot;
    boolean opened = false;

    public OBJ_Chest(GamePanel gamePanel,Entity loot) {
        super(gamePanel);
        this.gamePanel = gamePanel;
        name =  "Chest";
        this.loot = loot;
        type = type_obstacle;
        image = setup("items/chest",1,1);
        image2 = setup("items/chest_opened",1,1);
        down1 = setup("items/chest",1,1);
        collision = true;

        solidArea.x = 4;
        solidArea.y = 16;
        solidArea.width = 40;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

    }
    public void interact() {

        gamePanel.gameState = gamePanel.dialogueState;

        if(!opened) {
            gamePanel.sound.playSoundEffect(3);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("You open the chest and find the " + loot.name + " !");

            if(gamePanel.player.inventory.size() == gamePanel.player.maxInventorySize) {
                stringBuilder.append("\n...But your inventory is full.");
            }
            else {
                stringBuilder.append("\nYou obtain the " + loot.name + "!");
                gamePanel.player.inventory.add(loot);
                down1 = image2;
            }
            gamePanel.ui.currentDialogue = stringBuilder.toString();
            opened = true;
        }
        else {
            gamePanel.ui.currentDialogue = "The chest is empty.";
        }
        gamePanel.keyHandler.enterPressed = false;
    }
}