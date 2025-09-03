package object;

import game.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Key extends SuperObject{

    GamePanel gamePanel;
    public OBJ_Key(GamePanel gamePanel){

        name =  "Key";
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/items/key.png"));
            utilityTool.scaleImage(image,gamePanel.tileSize, gamePanel.tileSize);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
