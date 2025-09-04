package object;

import entity.Entity;
import game.GamePanel;


public class OBJ_Key extends Entity {

    public OBJ_Key(GamePanel gamePanel){
        super(gamePanel);
        name =  "Key";
        down1 = setup("items/key");
    }
}
