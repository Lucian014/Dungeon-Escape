package object;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Key extends SuperObject{

    public OBJ_Key(){

        name =  "Key";
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/items/key.png"));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
