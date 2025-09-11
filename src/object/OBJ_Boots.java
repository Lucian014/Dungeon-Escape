package object;

import entity.Entity;
import game.GamePanel;



public class OBJ_Boots extends Entity {

    public static final String objName = "Boots";

    public OBJ_Boots(GamePanel gamePanel){
        super(gamePanel);
        name =  objName;
        down1 = setup("items/key",1,1);
    }
}
