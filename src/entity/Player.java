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

        //PLAYER STATUS
        maxLife = 6; // 2 lives = 1 heart
        life =  maxLife;

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
        // Only move player if in PLAY state
        if(gamePanel.gameState == gamePanel.playState) {
            int dx = 0;
            int dy = 0;

            // Collect input
            if (keyHandler.upPressed)    { dy -= speed; direction = "up"; }
            if (keyHandler.downPressed)  { dy += speed; direction = "down"; }
            if (keyHandler.leftPressed)  { dx -= speed; direction = "left"; }
            if (keyHandler.rightPressed) { dx += speed; direction = "right"; }

            // Check collisions for both axes
            collisionOn = false;

            // Check tile collisions
            gamePanel.checker.checkTile(this, dx, dy);

            // Check object collisions
            int objIndex = gamePanel.checker.checkObject(this, true);
            pickUpObject(objIndex);

            // Check NPC collisions - add to both axes check
            int npcIndex = gamePanel.checker.checkEntity(this, gamePanel.npc);
            if (npcIndex != 999) interactNPC(npcIndex);

            // Check monster collisions - add to both axes check
            int monsterIndex = gamePanel.checker.checkEntity(this, gamePanel.monster);
            contactMonster(monsterIndex);
            // Only move if no collision detected
            if (!collisionOn) {
                worldX += dx;
                worldY += dy;
            }

            // Check events
            gamePanel.eventHandler.checkEvent();

            // Animation
            if (dx != 0 || dy != 0) {
                spriteCounter++;
                if (spriteCounter > 14) {
                    spriteNum = (spriteNum == 1) ? 2 : 1;
                    spriteCounter = 0;
                }
            }
        }
        if(invincible) {
            invincibleCounter ++;
            if(invincibleCounter == 60) {
                invincible = false;
                invincibleCounter = 0;
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
            gamePanel.keyHandler.enterPressed = false;
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

        if( invincible ) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }
        g2.drawImage(image,screenX,screenY,null);

        //RESET ALPHA

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        //DEBUG
//        g2.setFont(new Font("Arial",Font.PLAIN,26));
//        g2.setColor(Color.white);
//        g2.drawString("Invincible: "+ invincibleCounter, gamePanel.tileSize, gamePanel.screenHeight / 2);
    }
    public void contactMonster(int i) {

        if(i != 999) {

            if(!invincible) {
                life -= 1;
                invincible = true;
            }
        }
    }
}
