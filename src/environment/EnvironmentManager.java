package environment;

import game.GamePanel;

import java.awt.*;

public class EnvironmentManager {

    GamePanel gamePanel;
    public Lighting lighting;

    public EnvironmentManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }
    public void setup() {

        lighting = new Lighting(gamePanel);
    }

    public void update() {

        lighting.update();

    }
    public void draw(Graphics2D graphics2D) {

        lighting.draw(graphics2D);

    }
}
