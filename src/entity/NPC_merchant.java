package entity;

import game.GamePanel;
import object.*;

import java.awt.*;

public class NPC_merchant extends Entity{
    GamePanel gamePanel;
    public NPC_merchant(GamePanel gamePanel) {
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

        dialogues[0] = "Looks like you've found me \nbrotha! What shall i offer \nyou lad?";


    }
    public void getImage(){
        up1 = setup("npc/merchant/merchant_1",1,1);
        up2 = setup("npc/merchant/merchant_1",1,1);
        down1 = setup("npc/merchant/merchant_1",1,1);
        down2 = setup("npc/merchant/merchant_1",1,1);
        left1 = setup("npc/merchant/merchant_1",1,1);
        left2 = setup("npc/merchant/merchant_1",1,1);
        right1 = setup("npc/merchant/merchant_1",1,1);
        right2 = setup("npc/merchant/merchant_1",1,1);


    }
    public void setItems() {
        inventory.add(new OBJ_Potion_Red(gamePanel));
        inventory.add(new OBJ_Key(gamePanel));
        inventory.add(new OBJ_Sword_Normal(gamePanel));
        inventory.add(new OBJ_Shield_Blue(gamePanel));
        inventory.add(new OBJ_Axe(gamePanel));
    }
    public void speak() {

        super.speak();
        gamePanel.gameState = gamePanel.tradeState;
        gamePanel.ui.npc = this;
    }

}
