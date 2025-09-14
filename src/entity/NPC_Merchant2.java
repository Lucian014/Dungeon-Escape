package entity;

import game.GamePanel;
import object.*;

import java.awt.*;

public class NPC_Merchant2 extends Entity{
    GamePanel gamePanel;
    public NPC_Merchant2(GamePanel gamePanel) {
        super(gamePanel);
        this.gamePanel = gamePanel;
        direction = "down";
        speed = 0;
        solidArea = new Rectangle(8,16,32,32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        getImage();
        setDialogue();
        setItems();
    }

    public void setDialogue(){

        dialogues[0][0] = "Hello good sir! What could i help \nyou with?";

        dialogues[1][0] = "Come again, sir!";

        dialogues[2][0] = "You need more coin to buy that.";

        dialogues[3][0] = "You cannot carry any more!";

        dialogues[4][0] = "You cannot sell an equipped item! ";


    }
    public void getImage(){
        up1 = setup("npc/merchant/merchant2",1,1);
        up2 = setup("npc/merchant/merchant2",1,1);
        down1 = setup("npc/merchant/merchant2",1,1);
        down2 = setup("npc/merchant/merchant2",1,1);
        left1 = setup("npc/merchant/merchant2",1,1);
        left2 = setup("npc/merchant/merchant2",1,1);
        right1 = setup("npc/merchant/merchant2",1,1);
        right2 = setup("npc/merchant/merchant2",1,1);


    }
    public void setItems() {
        inventory.add(new OBJ_Potion_Red(gamePanel));
        inventory.add(new OBJ_Lantern(gamePanel));
    }
    public void speak() {

        dialogueSet = 0;
        dialogueIndex = 0;

        gamePanel.gameState = gamePanel.tradeState;
        gamePanel.ui.npc = this;

    }

}
