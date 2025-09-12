package object;

import entity.Entity;
import game.GamePanel;

public class OBJ_BlueHeart extends Entity {

    public static final String objName = "Blue Heart";
    GamePanel gamePanel;
    public OBJ_BlueHeart(GamePanel gamePanel) {
        super(gamePanel);

        this.gamePanel = gamePanel;

        type = type_pickUpOnly;
        name = objName;
        down1 = setup("items/blueheart",1,1);
        value = 2;
        setDialogues();
    }

    public void setDialogues() {

        dialogues[0][0] = "You pick up a beautiful blue heart !";
        dialogues[0][1] = "You have found the Blue Heart, the legendary treasure !";

    }

    public boolean use(Entity entity) {

        gamePanel.gameState = gamePanel.cutsceneState;
        gamePanel.cutsceneManager.sceneNum = gamePanel.cutsceneManager.ending;


        return true;
    }
}
