package object;

import entity.Entity;
import game.GamePanel;

public class OBJ_Chest extends Entity {

    public OBJ_Chest(GamePanel gamePanel){
        super(gamePanel);
        name =  "Chest";
        down1 = setup("items/chest",1,1);
    }
}