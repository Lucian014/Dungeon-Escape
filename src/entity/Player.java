package entity;

import game.GamePanel;
import game.KeyHandler;
import game.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {

    KeyHandler keyHandler;

    public final int screenX;
    public final int screenY;

    public Player(GamePanel gamePanel, KeyHandler keyHandler){

        super(gamePanel);
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
        up1 = setup("player/player/boy_up_1");
        up2 = setup("player/player/boy_up_2");
        down1 = setup("player/player/boy_down_1");
        down2 = setup("player/player/boy_down_2");
        left1 = setup("player/player/boy_left_1");
        left2 = setup("player/player/boy_left_2");
        right1 = setup("player/player/boy_right_1");
        right2 = setup("player/player/boy_right_2");

    }

    public void update() {
        int dx = 0;
        int dy = 0;

        // Collect input
        if (keyHandler.upPressed)    { dy -= speed; direction = "up"; }
        if (keyHandler.downPressed)  { dy += speed; direction = "down"; }
        if (keyHandler.leftPressed)  { dx -= speed; direction = "left"; }
        if (keyHandler.rightPressed) { dx += speed; direction = "right"; }

        // ===== Vertical movement =====
        collisionOn = false;
        gamePanel.checker.checkTile(this, 0, dy);
        int objIndexV = gamePanel.checker.checkObject(this, true);
        pickUpObject(objIndexV);
        int npcIndexV = gamePanel.checker.checkEntity(this, gamePanel.npc);
        if (npcIndexV != 999) interactNPC(npcIndexV);

        if (!collisionOn) {
            worldY += dy;
        }

        // ===== Horizontal movement =====
        collisionOn = false;
        gamePanel.checker.checkTile(this, dx, 0);
        int objIndexH = gamePanel.checker.checkObject(this, true);
        pickUpObject(objIndexH);
        int npcIndexH = gamePanel.checker.checkEntity(this, gamePanel.npc);
        if (npcIndexH != 999) interactNPC(npcIndexH);

        if (!collisionOn) {
            worldX += dx;
        }

        // ===== Animation =====
        if (dx != 0 || dy != 0) {
            spriteCounter++;
            if (spriteCounter > 14) {
                spriteNum = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;
            }
        }
    }

    public void pickUpObject(int i) {

    }

    public void interactNPC(int i) {
        if(i != 999) {

            if(gamePanel.keyHandler.enterPressed) {
                gamePanel.gameState = gamePanel.dialogueState;
                gamePanel.npc[i].speak();
            }
        }
        gamePanel.keyHandler.enterPressed = false;
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


        g2.drawImage(image,screenX,screenY,null);
    }
}
