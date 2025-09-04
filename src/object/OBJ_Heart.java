package object;

import game.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Heart extends SuperObject{
    public OBJ_Heart(GamePanel gamePanel){
        name =  "Heart";
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/heart/heart_full.png"));
            image2 = ImageIO.read(getClass().getResourceAsStream("/heart/heart_half.png"));
            image3 = ImageIO.read(getClass().getResourceAsStream("/heart/heart_blank.png"));
            image = utilityTool.scaleImage(image,gamePanel.tileSize, gamePanel.tileSize);
            image2 = utilityTool.scaleImage(image2,gamePanel.tileSize, gamePanel.tileSize);
            image3 = utilityTool.scaleImage(image3, gamePanel.tileSize, gamePanel.tileSize);
        }catch (
                IOException e){
            e.printStackTrace();
        }
        collision = true;
    }
}
