package object;

import entity.Entity;
import game.GamePanel;



public class OBJ_Heart extends Entity {

    public OBJ_Heart(GamePanel gamePanel){
        super(gamePanel);
        name =  "Heart";

        image = setup("heart/heart_full",1,1);
        image2 = setup("heart/heart_half",1,1);
        image3 = setup("heart/heart_blank",1,1);
    }
}
