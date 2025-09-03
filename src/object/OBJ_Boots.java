package object;

import game.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Boots extends SuperObject{
    GamePanel gamePanel;

    public OBJ_Boots(GamePanel gamePanel){

        name =  "Boots";
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/items/boots.png"));
            utilityTool.scaleImage(image, gamePanel.tileSize, gamePanel.tileSize);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
