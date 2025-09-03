package object;

import game.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Door extends SuperObject{
    GamePanel gamePanel;

    public OBJ_Door(GamePanel gamePanel){
        name =  "Door";
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/items/door.png"));
            utilityTool.scaleImage(image,gamePanel.tileSize, gamePanel.tileSize);
        }catch (
                IOException e){
            e.printStackTrace();
        }
        collision = true;
    }

}
