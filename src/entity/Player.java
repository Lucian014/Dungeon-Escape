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


    public Player(GamePanel gamePanel, KeyHandler keyHandler) {

        super(gamePanel);
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;

        screenX = gamePanel.screenWidth / 2 - (gamePanel.tileSize / 2);
        screenY = gamePanel.screenHeight / 2 - (gamePanel.tileSize / 2);

        solidArea = new Rectangle(10, 18, 26, 26);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        attackArea.width = 36;
        attackArea.height = 36;

        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
    }

    public void setDefaultValues() {

        worldX = gamePanel.tileSize * 23;
        worldY = gamePanel.tileSize * 21;
        speed = 4;
        direction = "down";

        //PLAYER STATUS
        maxLife = 6; // 2 lives = 1 heart
        life = maxLife;

    }

    public void getPlayerImage() {
        up1 = setup("player/player/boy_up_1", 1, 1);
        up2 = setup("player/player/boy_up_2", 1, 1);
        down1 = setup("player/player/boy_down_1", 1, 1);
        down2 = setup("player/player/boy_down_2", 1, 1);
        left1 = setup("player/player/boy_left_1", 1, 1);
        left2 = setup("player/player/boy_left_2", 1, 1);
        right1 = setup("player/player/boy_right_1", 1, 1);
        right2 = setup("player/player/boy_right_2", 1, 1);
    }

    public void getPlayerAttackImage() {

        attackUp1 = setup("player/player/boy_attack_up_1", 1, 2);
        attackUp2 = setup("player/player/boy_attack_up_2", 1, 2);
        attackDown1 = setup("player/player/boy_attack_down_1", 1, 2);
        attackDown2 = setup("player/player/boy_attack_down_2", 1, 2);
        attackLeft1 = setup("player/player/boy_attack_left_1", 2, 1);
        attackLeft2 = setup("player/player/boy_attack_left_2", 2, 1);
        attackRight1 = setup("player/player/boy_attack_right_1", 2, 1);
        attackRight2 = setup("player/player/boy_attack_right_2", 2, 1);

    }

    public void update() {
        // Only update when in play state
        if (gamePanel.gameState == gamePanel.playState) {

            // Handle attack input
            if (gamePanel.keyHandler.attackPressed && !attacking) {
                boolean nearNPC = false;
                int npcIndex = gamePanel.checker.checkEntity(this, gamePanel.npc);


                if (npcIndex != 999) {
                    nearNPC = true;
                }
                if (!nearNPC) {
                    attacking = true;
                    spriteCounter = 0;
                }

                // Consume attack input
                gamePanel.keyHandler.attackPressed = false;
            }

            if (attacking) {
                // Only handle attack animation
                attack();
            } else {
                int dx = 0;
                int dy = 0;

                // Collect movement input
                if (gamePanel.keyHandler.upPressed) {
                    dy -= speed;
                    direction = "up";
                }
                if (gamePanel.keyHandler.downPressed) {
                    dy += speed;
                    direction = "down";
                }
                if (gamePanel.keyHandler.leftPressed) {
                    dx -= speed;
                    direction = "left";
                }
                if (gamePanel.keyHandler.rightPressed) {
                    dx += speed;
                    direction = "right";
                }

                // Reset collision flag
                collisionOn = false;

                // Check collisions
                gamePanel.checker.checkTile(this, dx, dy);
                int objIndex = gamePanel.checker.checkObject(this, true);
                pickUpObject(objIndex);

                int npcIndex = gamePanel.checker.checkEntity(this, gamePanel.npc);
                if (npcIndex != 999 && gamePanel.keyHandler.enterPressed) {
                    interactNPC(npcIndex);
                    gamePanel.keyHandler.enterPressed = false; // consume input
                }

                int monsterIndex = gamePanel.checker.checkEntity(this, gamePanel.monster);
                contactMonster(monsterIndex);

                // Move only if no collision
                if (!collisionOn) {
                    worldX += dx;
                    worldY += dy;
                }

                // Check events
                gamePanel.eventHandler.checkEvent();

                // Walking animation
                if (dx != 0 || dy != 0) {
                    spriteCounter++;
                    if (spriteCounter > 14) {
                        spriteNum = (spriteNum == 1) ? 2 : 1;
                        spriteCounter = 0;
                    }
                }
            }

            // Invincibility timer
            if (invincible) {
                invincibleCounter++;
                if (invincibleCounter == 60) {
                    invincible = false;
                    invincibleCounter = 0;
                }
            }
        }
    }


    public void attack() {
        spriteCounter++;

        if (spriteCounter == 1) {
            // Attack just started → decide sound
            int monsterIndex = gamePanel.checker.checkEntity(this, gamePanel.monster);
            if (monsterIndex == 999) {
                gamePanel.playSE(7); // air swing sound
            }
        }

        if (spriteCounter <= 5) {
            spriteNum = 1;
        }
        if (spriteCounter > 5 && spriteCounter <= 25) {
            spriteNum = 2;

            // Save player position
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            // Extend attack hitbox
            switch (direction) {
                case "up":    worldY -= attackArea.height; break;
                case "down":  worldY += attackArea.height; break;
                case "left":  worldX -= attackArea.width; break;
                case "right": worldX += attackArea.width; break;
            }

            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;

            // Check monster collision
            int monsterIndex = gamePanel.checker.checkEntity(this, gamePanel.monster);
            if (monsterIndex != 999) {
                damageMonster(monsterIndex); // plays monster hit sound (SE 5)
            }

            // Restore original values
            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;
        }

        if (spriteCounter > 25) {
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }
    }

    public void pickUpObject(int i) {

    }

    public void interactNPC(int i) {
        if(gamePanel.keyHandler.enterPressed){
        if(i != 999) {
                gamePanel.gameState = gamePanel.dialogueState;
                gamePanel.npc[i].speak();
            }
            gamePanel.keyHandler.enterPressed = false;
            } else {
            gamePanel.playSE(7);
            attacking = true;
        }
    }
    public void draw(Graphics2D g2){

        BufferedImage image = null;
        int tempScreenX = screenX;
        int tempScreenY = screenY;

        switch (direction){
            case "up":
                if(!attacking){
                    if(spriteNum == 1){image = up1;}
                    if(spriteNum == 2){image = up2;}
                }
                if(attacking) {
                    tempScreenY = screenY - gamePanel.tileSize;
                    if(spriteNum == 1){image = attackUp1;}
                    if(spriteNum == 2){image = attackUp2;}
                }
                break;
            case "down":
                if(!attacking){
                    if(spriteNum == 1){image = down1;}
                    if(spriteNum == 2){image = down2;}
                }
                if(attacking) {
                    if(spriteNum == 1){image = attackDown1;}
                    if(spriteNum == 2){image = attackDown2;}
                }
                break;
            case "left":
                if(!attacking){
                    if(spriteNum == 1){image = left1;}
                    if(spriteNum == 2){image = left2;}
                }
                if(attacking) {
                    tempScreenX = screenX - gamePanel.tileSize ;
                    if(spriteNum == 1){image = attackLeft1;}
                    if(spriteNum == 2){image = attackLeft2;}
                }
                break;
            case "right":
                if(!attacking){
                    if(spriteNum == 1){image = right1;}
                    if(spriteNum == 2){image = right2;}
                }
                if(attacking) {
                    if(spriteNum == 1){image = attackRight1;}
                    if(spriteNum == 2){image = attackRight2;}
                }
                break;
        }

        if( invincible ) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }
        g2.drawImage(image,tempScreenX,tempScreenY,null);

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
                gamePanel.playSE(6);
                life -= 1;
                invincible = true;
            }
        }
    }

    public void damageMonster(int monsterIndex) {

        if(monsterIndex != 999) {
            if(!gamePanel.monster[monsterIndex].invincible) {
                gamePanel.playSE(5);
                gamePanel.monster[monsterIndex].life--;
                gamePanel.monster[monsterIndex].invincible = true;
                gamePanel.monster[monsterIndex].damageReaction();
                if(gamePanel.monster[monsterIndex].life <= 0) {
                    gamePanel.monster[monsterIndex].dying = true;
                }
            }
        }
    }
}
