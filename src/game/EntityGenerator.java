package game;

import entity.Entity;
import object.*;


public class EntityGenerator {

    GamePanel gamePanel;

    public EntityGenerator(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public Entity getObject(String itemName) {

        Entity obj = null;

        switch (itemName) {

            case OBJ_Axe.objName: obj = new OBJ_Axe(gamePanel); break;
            case OBJ_Boots.objName: obj = new OBJ_Boots(gamePanel); break;
            case OBJ_Key.objName: obj = new OBJ_Key(gamePanel); break;
            case OBJ_Lantern.objName: obj = new OBJ_Lantern(gamePanel); break;
            case OBJ_Potion_Red.objName: obj = new OBJ_Potion_Red(gamePanel); break;
            case OBJ_Shield_Blue.objName: obj = new OBJ_Shield_Blue(gamePanel); break;
            case OBJ_Shield_Wood.objName: obj = new OBJ_Shield_Wood(gamePanel); break;
            case OBJ_Sword_Normal.objName: obj = new OBJ_Sword_Normal(gamePanel); break;
            case OBJ_Door.objName: obj = new OBJ_Door(gamePanel); break;
            case OBJ_Door_Iron.objName: obj = new OBJ_Door_Iron(gamePanel); break;
            case OBJ_Fireball.objName: obj = new OBJ_Fireball(gamePanel); break;
            case OBJ_ManaCrystal.objName: obj = new OBJ_ManaCrystal(gamePanel); break;
            case OBJ_SwordRed.objName: obj = new OBJ_SwordRed(gamePanel);
        }
        return obj;
    }
}
