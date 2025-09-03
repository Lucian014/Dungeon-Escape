package object;

import game.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Chest extends SuperObject{

    GamePanel gamePanel;
    public OBJ_Chest(GamePanel gamePanel){
        name =  "Chest";
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/items/chest.png"));
            utilityTool.scaleImage(image, gamePanel.tileSize, gamePanel.tileSize);
        }catch (
                IOException e){
            e.printStackTrace();
        }
        collision = true;
    }

}