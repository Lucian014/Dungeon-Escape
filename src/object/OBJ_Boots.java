package object;

import entity.Entity;
import game.GamePanel;



public class OBJ_Boots extends Entity {

    public OBJ_Boots(GamePanel gamePanel){
        super(gamePanel);
        name =  "Boots";
        down1 = setup("items/key",1,1);
    }
}
