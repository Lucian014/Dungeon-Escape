package object;

import entity.Entity;
import game.GamePanel;



public class OBJ_Heart extends Entity {

    GamePanel gamePanel;
    public OBJ_Heart(GamePanel gamePanel){
        super(gamePanel);
        this.gamePanel = gamePanel;
        name =  "Heart";
        type = type_pickUpOnly;
        value = 2;
        down1 = setup("heart/heart_full",1,1);
        image = setup("heart/heart_full",1,1);
        image2 = setup("heart/heart_half",1,1);
        image3 = setup("heart/heart_blank",1,1);
    }

    public void use(Entity entity) {

        gamePanel.playSE(1);
        gamePanel.ui.addMessage("Life +" + value);
        entity.life += value;
    }
}
