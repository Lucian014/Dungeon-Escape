package environment;

import game.GamePanel;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Lighting {

    GamePanel gamePanel;
    BufferedImage darknessFilter;
    int dayCounter = 0;
    public float filterAlpha = 0f;

    //DAY STATE
    public final int day = 0;
    public final int dusk = 1;
    public final int night = 2;
    public final int dawn = 3;
    public int dayState = day;


    public Lighting(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        setLightSource();

    }

    public void draw(Graphics2D graphics2D) {

        if(gamePanel.currentArea == gamePanel.outside) {
            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,filterAlpha));
        }
        if(gamePanel.currentArea == gamePanel.outside || gamePanel.currentArea == gamePanel.dungeon) {
            graphics2D.drawImage(darknessFilter,0,0,null);
        }

        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));

        String timeOfDay = "";
        switch (dayState) {
            case day: timeOfDay = "Day"; break;
            case dusk: timeOfDay = "Dusk"; break;
            case night: timeOfDay = "Night"; break;
            case dawn: timeOfDay = "Dawn"; break;

        }
        graphics2D.setColor(Color.WHITE);
        graphics2D.setFont(graphics2D.getFont().deriveFont(Font.BOLD,40F));
        graphics2D.drawString(timeOfDay, 800, 500);
    }

    public void setLightSource() {

        darknessFilter = new BufferedImage(gamePanel.screenWidth,gamePanel.screenHeight,BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = (Graphics2D) darknessFilter.getGraphics();

        if(gamePanel.player.currentLight == null) {
            graphics2D.setColor(new Color(0,0,0,0.92f));
        }
        else {
            //Get the center x and y of the light circle
            int centerX = gamePanel.player.screenX + (gamePanel.tileSize / 2);
            int centerY = gamePanel.player.screenY + (gamePanel.tileSize / 2);


            //Create a gradation effect within the light circle
            Color[] color = new Color[12];
            float[] fraction = new float[12];

            color[0] = new Color(0,0,0,0.1f);
            color[1] = new Color(0,0,0,0.42f);
            color[2] = new Color(0,0,0,0.52f);
            color[3] = new Color(0,0,0,0.61f);
            color[4] = new Color(0,0,0,0.69f);
            color[5] = new Color(0,0,0,0.76f);
            color[6] = new Color(0,0,0,0.82f);
            color[7] = new Color(0,0,0,0.87f);
            color[8] = new Color(0,0,0,0.88f);
            color[9] = new Color(0,0,0,0.89f);
            color[10] = new Color(0,0,0,0.90f);
            color[11] = new Color(0,0,0,0.92f);

            fraction[0] = 0f;
            fraction[1] = 0.4f;
            fraction[2] = 0.5f;
            fraction[3] = 0.6f;
            fraction[4] = 0.65f;
            fraction[5] = 0.7f;
            fraction[6] = 0.75f;
            fraction[7] = 0.8f;
            fraction[8] = 0.85f;
            fraction[9] = 0.9f;
            fraction[10] = 0.91f;
            fraction[11] = 1f;

            //Create a gradation paint settings for the light circle
            RadialGradientPaint gPaint = new RadialGradientPaint(centerX,centerY,gamePanel.player.currentLight.lightRadius,fraction,color);
            graphics2D.setPaint(gPaint);

        }

        //Set the gradient data on graphics2D
        graphics2D.fillRect(0,0,gamePanel.screenWidth,gamePanel.screenHeight);
        graphics2D.dispose();
    }

    public void resetDay() {
        dayState = day;
        filterAlpha = 0f;
    }

    public void update() {

        if(gamePanel.player.lightUpdated) {
            setLightSource();
            gamePanel.player.lightUpdated = false;
        }

        if(dayState == day) {

            dayCounter++;

            if(dayCounter > 60000) {
                dayState = dusk;
                dayCounter = 0;
            }
        }
        if(dayState == dusk) {
            filterAlpha += 0.001f;

            if(filterAlpha > 1f) {
                filterAlpha = 1f;
                dayState = night;
            }
        }
        if(dayState == night) {
            dayCounter++;
            if(dayCounter > 60000) {
                dayState = dawn;
                dayCounter = 0;
            }
        }
        if(dayState == dawn) {
            filterAlpha -= 0.001f;

            if(filterAlpha < 0f) {
                filterAlpha = 0f;
                dayState = day;
            }
        }
    }
}
