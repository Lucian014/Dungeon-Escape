package entity;

import game.GamePanel;

public class PlayerDummy extends Entity{

    public static final String npcName = "Player Dummy";

    public PlayerDummy(GamePanel gamePanel) {
        super(gamePanel);

        name = npcName;
        getImage();
    }

    public void getImage() {
        up1 = setup("player/player/boy_up_1", 1, 1);
        up2 = setup("player/player/boy_up_2", 1, 1);
        down1 = setup("player/player/boy_down_1", 1, 1);
        down2 = setup("player/player/boy_down_2", 1, 1);
        left1 = setup("player/player/boy_left_1", 1, 1);
        left2 = setup("player/player/boy_left_2", 1, 1);
        right1 = setup("player/player/boy_right_1", 1, 1);
        right2 = setup("player/player/boy_right_2", 1, 1);
    }
}
