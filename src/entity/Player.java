package entity;

import game.GamePanel;
import game.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageFilter;
import java.io.BufferedReader;
import java.io.IOException;

public class Player extends Entity {

    GamePanel gamePanel;
    KeyHandler keyHandler;

    public final int screenX;
    public final int screenY;
    int hasKey = 0;

    public Player(GamePanel gamePanel, KeyHandler keyHandler){

        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;

        screenX = gamePanel.screenWidth / 2 - (gamePanel.tileSize / 2);
        screenY = gamePanel.screenHeight / 2 - (gamePanel.tileSize / 2);

        solidArea = new Rectangle(10,18,26,26);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        setDefaultValues();
        getPlayerImage();
    }
    public void setDefaultValues(){

        worldX = gamePanel.tileSize * 23;
        worldY = gamePanel.tileSize * 21;
        speed = 4;
        direction = "down";
    }

    public void getPlayerImage(){
        try{
            down1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_down_1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_down_2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_left_1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_left_2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_right_1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_right_2.png"));
            up1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_up_1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_up_2.png"));

        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void update() {
        int dx = 0;
        int dy = 0;

        // Collect input
        if (keyHandler.upPressed) {
            dy -= speed;
        }
        if (keyHandler.downPressed) {
            dy += speed;
        }
        if (keyHandler.leftPressed) {
            dx -= speed;
        }
        if (keyHandler.rightPressed) {
            dx += speed;
        }

        // Determine facing direction for animation
        if (dx < 0) direction = "left";
        if (dx > 0) direction = "right";
        if (dy < 0) direction = "up";
        if (dy > 0) direction = "down";

        //Tile Collision check
        collisionOn = false;
        gamePanel.checker.checkTile(this, 0, dy);

        //Check object collision
        int objIndex = gamePanel.checker.checkObject(this, true);
        pickUpObject(objIndex);
        if (!collisionOn) {
            worldY += dy;
        }

        collisionOn = false;
        gamePanel.checker.checkTile(this, dx, 0);
        if (!collisionOn) {
            worldX += dx;
        }
        // Sprite animation
        if (dx != 0 || dy != 0) {
            spriteCounter++;
            if (spriteCounter > 14) {
                spriteNum = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;
            }
        }
    }

    public void pickUpObject(int i) {
        if(i != 999) {

            String objectName = gamePanel.object[i].name;

            switch (objectName) {
                case "Key":
                    hasKey++;
                    gamePanel.object[i] = null;
                    System.out.println("Key:"+hasKey);
                    gamePanel.playSE(1);
                    break;
                case "Door":
                    if(hasKey > 0) {
                        gamePanel.object[i] = null;
                        hasKey--;
                        gamePanel.playSE(3);
                        System.out.println("Key:"+hasKey);
                    }
                    break;
                case "Boots":
                    gamePanel.playSE(2);
                    speed += 2;
                    gamePanel.object[i] = null;
                    break;
            }
        }
    }

    public void draw(Graphics2D g2){

        BufferedImage image = null;
        switch (direction){
            case "up":
                if(spriteNum == 1){
                    image = up1;
                }
                if(spriteNum == 2){
                    image = up2;
                }
                break;
            case "down":
                if(spriteNum == 1){
                    image = down1;
                }
                if(spriteNum == 2){
                    image = down2;
                }
                break;
            case "left":
                if(spriteNum == 1){
                    image = left1;
                }
                if(spriteNum == 2){
                    image = left2;
                }
                break;
            case "right":
                if(spriteNum == 1){
                    image = right1;
                }
                if(spriteNum == 2){
                    image = right2;
                }
                break;
        }


        g2.drawImage(image,screenX,screenY,gamePanel.tileSize,gamePanel.tileSize,null);
    }
}
