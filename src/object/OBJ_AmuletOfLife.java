package object;

import entity.Entity;
import game.GamePanel;

public class OBJ_AmuletOfLife extends Entity {

    public static final String objName = "Amulet Of Life";
    GamePanel gamePanel;
    public OBJ_AmuletOfLife(GamePanel gamePanel) {
        super(gamePanel);

        this.gamePanel = gamePanel;

        type = type_pickUpOnly;
        name = objName;
        down1 = setup("items/amuletOfLife",1,1);
        value = 2;
        setDialogues();
    }

    public void setDialogues() {

        dialogues[0][0] = "You pick up the Amulet of Life !";
        dialogues[0][1] = "You have found the Amulet of Life, the legendary\ntreasure !";

    }

    public boolean use(Entity entity) {

        gamePanel.gameState = gamePanel.cutsceneState;
        gamePanel.cutsceneManager.sceneNum = gamePanel.cutsceneManager.ending;


        return true;
    }
}
