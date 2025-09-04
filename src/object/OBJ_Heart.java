package object;

import entity.Entity;
import game.GamePanel;



public class OBJ_Heart extends Entity {

    public OBJ_Heart(GamePanel gamePanel){
        super(gamePanel);
        name =  "Heart";

        image = setup("heart/heart_full");
        image2 = setup("heart/heart_half");
        image3 = setup("heart/heart_blank");
    }
}
