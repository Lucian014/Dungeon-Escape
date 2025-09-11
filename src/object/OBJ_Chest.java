package object;

import entity.Entity;
import game.GamePanel;

public class OBJ_Chest extends Entity {

    GamePanel gamePanel;
    Entity loot;
    boolean opened = false;
    public static final String objName = "Chest";

    public OBJ_Chest(GamePanel gamePanel,Entity loot) {
        super(gamePanel);
        this.gamePanel = gamePanel;
        name =  objName;
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
        setDialogue();
    }

    public void setDialogue() {

        dialogues[0][0] = "You open the chest and find a " + loot.name + " !\n...But your inventory is full.\"";
        dialogues[1][0] = "You open the chest and find the " + loot.name + " !\nYou obtain the " + loot.name + " !";
        dialogues[2][0] = "The chest is empty.";

    }
    public void interact() {

        if(!opened) {
            gamePanel.sound.playSoundEffect(3);

            if(!gamePanel.player.canObtainItem(loot)) {
                startDialogue(this,0);
            }
            else {
                startDialogue(this,1);
                down1 = image2;
                opened = true;
            }
        }
        else {
            startDialogue(this,2);
        }
        gamePanel.keyHandler.enterPressed = false;
    }
}