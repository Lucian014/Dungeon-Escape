package entity;
import game.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Entity {

    GamePanel gamePanel;
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public BufferedImage attackUp1, attackUp2, attackDown1,attackDown2,attackLeft1,attackLeft2,attackRight1,attackRight2, guardUp,guardDown,guardLeft,guardRight;
    public BufferedImage image, image2,image3;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public Rectangle attackArea = new Rectangle(0,0,0,0);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;
    public String[][] dialogues = new String[20][20];
    public Entity attacker;
    public Entity linkedEntity;


    //STATE
    public int worldX, worldY;
    public String direction = "down";
    public int spriteNum = 1;
    public int dialogueIndex = 0;
    public int dialogueSet = 0;
    public boolean collision = false;
    public boolean invincible = false;
    public boolean attacking = false;
    public boolean alive = true;
    public boolean dying = false;
    boolean hpBarOn = false;
    public boolean onPath = false;
    public boolean knockBack = false;
    public String knockBackDirection;
    public boolean guarding = false;
    public boolean transparent = false;
    public boolean offBalance = false;
    public boolean inRage = false;
    //COUNTER
    public int spriteCounter = 0;
    public int invincibleCounter = 0;
    public int actionLockCounter = 0;
    int dyingCounter = 0;
    int hpBarCounter = 0;
    public int shotAvailableCounter = 0;
    int knockBackCounter = 0;
    public int guardCounter = 0;
    int offBalanceCounter = 0;


    //CHARACTER STATS
    public int maxLife;
    public int life;
    public int defaultSpeed;
    public int speed;
    public String name;
    public int maxMana;
    public int mana;
    public int level;
    public int strength;
    public int dexterity;
    public int attack;
    public int defense;
    public int exp;
    public int nextLevelExp;
    public int coin;
    public int motion1_duration;
    public int motion2_duration;
    public Entity currentWeapon;
    public Entity currentShield;
    public Projectile projectile;
    public Entity currentLight;

    //ITEM ATTRIBUTES
    public ArrayList<Entity> inventory = new ArrayList<>();
    public final int maxInventorySize = 20;
    public int attackValue;
    public int defenseValue;
    public String description = "";
    public int useCost;
    public int value;
    public int price;
    public int knockBackPower = 0;
    public boolean stackable = false;
    public int amount = 1;
    public int lightRadius;

    //TYPE
    public int type;
    public final int type_player = 0;
    public final int type_npc = 1;
    public final int type_monster = 2;
    public final int type_sword = 3;
    public final int type_axe = 4;
    public final int type_shield = 5;
    public final int type_consumable = 6;
    public final int type_pickUpOnly = 7;
    public final int type_obstacle = 8;
    public final int type_light = 9;
    public final int type_pickaxe = 10;

    public Entity(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public int getLeftX() {
        return worldX + solidArea.x;
    }
    public int getRightX() {
        return worldX + solidArea.x + solidArea.width;
    }
    public int getTopY() {
        return worldY + solidArea.y;
    }
    public int getBottomY() {
        return worldY + solidArea.y + solidArea.height;
    }
    public int getCol(){
        return (worldX + solidArea.x) / gamePanel.tileSize;
    }
    public int getRow(){
        return (worldY + solidArea.y) / gamePanel.tileSize;
    }
    public int getCenterX() {
        return worldX +left1.getWidth() / 2;
    }
    public int getCenterY() {
        return worldY +up1.getHeight() / 2;
    }

    public void draw(Graphics2D g2) {

        BufferedImage image = null;

        // Calculate screen position relative to player
        int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
        int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;

        // Offset temp values for attacks
        int tempScreenX = screenX;
        int tempScreenY = screenY;

        // Only draw if within screen bounds
        if (worldX + gamePanel.tileSize * 5 > gamePanel.player.worldX - gamePanel.player.screenX &&
                worldX - gamePanel.tileSize < gamePanel.player.worldX + gamePanel.player.screenX &&
                worldY + gamePanel.tileSize * 5 > gamePanel.player.worldY - gamePanel.player.screenY &&
                worldY - gamePanel.tileSize < gamePanel.player.worldY + gamePanel.player.screenY) {

            // === Direction + Sprite selection ===
            switch (direction) {
                case "up":
                    if (!attacking) {
                        image = (spriteNum == 1) ? up1 : up2;
                    } else {
                        tempScreenY = screenY - up1.getHeight();
                        image = (spriteNum == 1) ? attackUp1 : attackUp2;
                    }
                    break;

                case "down":
                    if (!attacking) {
                        image = (spriteNum == 1) ? down1 : down2;
                    } else {
                        image = (spriteNum == 1) ? attackDown1 : attackDown2;
                    }
                    break;

                case "left":
                    if (!attacking) {
                        image = (spriteNum == 1) ? left1 : left2;
                    } else {
                        tempScreenX = screenX - left1.getWidth();
                        image = (spriteNum == 1) ? attackLeft1 : attackLeft2;
                    }
                    break;

                case "right":
                    if (!attacking) {
                        image = (spriteNum == 1) ? right1 : right2;
                    } else {
                        image = (spriteNum == 1) ? attackRight1 : attackRight2;
                    }
                    break;
            }

            // === Monster HP bar ===
            if (type == 2 && hpBarOn) {
                double oneScale = (double) gamePanel.tileSize / maxLife;
                double hpBarValue = oneScale * life;

                g2.setColor(new Color(35, 35, 35));
                g2.fillRect(screenX - 1, screenY - 16, gamePanel.tileSize + 2, 12); // lifted above sprite

                g2.setColor(new Color(185, 185, 185));
                g2.fillRect(screenX, screenY - 15, gamePanel.tileSize, 10);

                g2.setColor(new Color(255, 0, 30));
                g2.fillRect(screenX, screenY - 15, (int) hpBarValue, 10);

                hpBarCounter++;
                if (hpBarCounter > 600) {
                    hpBarCounter = 0;
                    hpBarOn = false;
                }
            }

            // === Invincibility effect ===
            if (invincible) {
                changeAlpha(g2, 0.4F);
                hpBarCounter = 0;
                hpBarOn = true;
            }

            // === Dying animation ===
            if (dying) {
                dyingAnimation(g2);
            }

            // === Draw final sprite ===
            g2.drawImage(image, tempScreenX, tempScreenY, null);

            // Reset alpha
            changeAlpha(g2, 1F);
        }
    }



    public BufferedImage setup(String imagePath, int scaleWidth, int scaleHeigth) {
        UtilityTool utilityTool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/" + imagePath + ".png"));
            image = utilityTool.scaleImage(image, gamePanel.tileSize * scaleWidth, gamePanel.tileSize * scaleHeigth);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.out.println("Could not load resource: /" + imagePath + ".png");
        }
        return image;
    }

    public void setAction() {}

    public void move(String direction) {}

    public void damageReaction() {}

    public void speak() {}

    public void facePlayer() {

        switch (gamePanel.player.direction) {
            case "up" :
                direction = "down";
                break;
            case "down" :
                direction = "up";
                break;
            case "left" :
                direction = "right";
                break;
            case "right" :
                direction = "left";
                break;
        }
    }

    public void startDialogue(Entity entity, int setNum) {

        gamePanel.gameState = gamePanel.dialogueState;
        gamePanel.ui.npc = entity;
        dialogueSet = setNum;

    }

    public boolean use(Entity entity) {return false;}
    public void interact(){}
    public void checkDrop() {

    }

    public void dropItem(Entity droppedItem) {
        for(int i = 0; i < gamePanel.object[1].length; i++) {
            if(gamePanel.object[gamePanel.currentMap][i] == null) {
                gamePanel.object[gamePanel.currentMap][i] = droppedItem;
                gamePanel.object[gamePanel.currentMap][i].worldX = worldX;
                gamePanel.object[gamePanel.currentMap][i].worldY = worldY;
                break;
            }
        }
    }

    public void checkCollision() {
        collisionOn = false;
        gamePanel.checker.checkTile(this);
        gamePanel.checker.checkObject(this, false);
        gamePanel.checker.checkEntity(this, gamePanel.npc);
        gamePanel.checker.checkEntity(this, gamePanel.monster);
        gamePanel.checker.checkEntity(this, gamePanel.iTile);
        boolean contactPlayer = gamePanel.checker.checkPlayer(this);
        if (this.type == type_monster && contactPlayer) {
            damagePlayer(attack);
        }
    }

    public void checkStopChasingOrNot(Entity target, int distance, int rate) {

        if(getTileDistance(target) > distance) {
            int i = new Random().nextInt(rate);
            if(i == 0) {
                onPath = false;
            }
        }
    }
    public void checkShootOrNot(int rate, int shotInterval) {
        int i = new Random().nextInt(rate);
        if(i > 196 && !projectile.alive && shotAvailableCounter >= shotInterval) {
            projectile.set(worldX,worldY,direction,true,this);
            //CHECK VACANCY
            for(i = 0; i < gamePanel.projectile[1].length; i++) {
                if(gamePanel.projectile[1][i] == null) {
                    gamePanel.projectile[gamePanel.currentMap][i] = projectile;
                    break;
                }
            }

            shotAvailableCounter = 0;
        }
        if(shotAvailableCounter < 30) {
            shotAvailableCounter++;
        }
    }
    public void checkStartChasingOrNot(Entity target, int distance, int rate) {

        if(getTileDistance(target) < distance) {
            int i = new Random().nextInt(rate);
            if(i == 0) {
                onPath = true;
            }
        }
    }
    public void checkAttackOrNot(int rate, int straight, int horizontal) {

        boolean targetInRange = false;
        int xDis = getXdistance(gamePanel.player);
        int yDis = getYdistance(gamePanel.player);

        switch (direction) {
            case "up":
                if(gamePanel.player.getCenterY() < getCenterY() && yDis < straight && xDis < horizontal) {
                    targetInRange = true;
                }
                break;
            case "down":
                if(gamePanel.player.getCenterY() > getCenterY() && yDis < straight && xDis < horizontal) {
                    targetInRange = true;
                }
                break;
            case "left":
                if(gamePanel.player.getCenterX() < getCenterX() && xDis < straight && yDis < horizontal) {
                    targetInRange = true;
                }
                break;
            case "right":
                if(gamePanel.player.getCenterX() > getCenterX() && xDis < straight && yDis < horizontal) {
                    targetInRange = true;
                }
                break;
        }

        if(targetInRange) {
            //Check if it initiates the attack
            int i = new Random().nextInt(rate);
            if(i == 0) {
                attacking = true;
                spriteNum = 1;
                spriteCounter = 0;
                shotAvailableCounter = 0;
            }
        }
    }

    public void update() {

        if(knockBack) {
            checkCollision();
            //Reset knockback if collision
            if(collisionOn) {
                knockBackCounter = 0;
                knockBack = false;
                speed = defaultSpeed;
            }
            else if (!collision) {
                switch (knockBackDirection) {
                    case "up":    worldY -= speed; break;
                    case "down":  worldY += speed; break;
                    case "left":  worldX -= speed; break;
                    case "right": worldX += speed; break;
                }
            }
            knockBackCounter++;
            if(knockBackCounter > 10) {
                knockBackCounter = 0;
                knockBack = false;
                speed = defaultSpeed;
            }
        }
        else if(attacking) {
            attack();

        }
        else {
            setAction();
            checkCollision();

            // Move only if no collision
            if (!collisionOn) {
                switch (direction) {
                    case "up":    worldY -= speed; break;
                    case "down":  worldY += speed; break;
                    case "left":  worldX -= speed; break;
                    case "right": worldX += speed; break;
                }
            }
            // sprite animation
            spriteCounter++;
            if (spriteCounter > 24) {
                spriteNum = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;
            }
        }


        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter == 40) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        if (shotAvailableCounter < 90) {
            shotAvailableCounter++;
        }
        if(offBalance) {
            offBalanceCounter++;
            if(offBalanceCounter > 60) {
                offBalance = false;
                offBalanceCounter = 0;
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

        if (spriteCounter <= motion1_duration) {
            spriteNum = 1;
        }
        if (spriteCounter > motion1_duration && spriteCounter <= motion2_duration) {
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

            if(type == type_monster) {
                if(gamePanel.checker.checkPlayer(this)) {
                    damagePlayer(attack);
                }
            } else {//PLAYER

                //CHECK MONSTER COLLISION
                int monsterIndex = gamePanel.checker.checkEntity(this, gamePanel.monster);
                gamePanel.player.damageMonster(monsterIndex, this, attack, currentWeapon.knockBackPower);

                //CHECK INTERACTIVE TILE COLLISION
                int iTileIndex = gamePanel.checker.checkEntity(this, gamePanel.iTile);
                gamePanel.player.damageInteractiveTile(iTileIndex);

                //CHECK PROJECTILE COLLISION
                int projectileIndex = gamePanel.checker.checkEntity(this, gamePanel.projectile);
                gamePanel.player.damageProjectile(projectileIndex);
            }
            // Restore original values
            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;
        }

        if (spriteCounter > motion2_duration) {
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }

    }
    public int getXdistance(Entity target) {
        return Math.abs(getCenterX() - target.getCenterX());
    }
    public int getYdistance(Entity target) {
        return Math.abs(getCenterY() - target.getCenterY());
    }
    public int getTileDistance(Entity target) {
        return (getXdistance(target) + getYdistance(target)) / gamePanel.tileSize;
    }
    public int getGoalCol(Entity target) {
        return (target.worldX + target.solidArea.x) / gamePanel.tileSize;
    }
    public int getGoalRow(Entity target) {
        return (target.worldY + target.solidArea.y) / gamePanel.tileSize;
    }
    public void getRandomDirection(int interval) {
        actionLockCounter++;
        if(actionLockCounter > interval){
            Random random = new Random();
            int i = random.nextInt(100) + 1;
            if (i <= 25) {
                direction = "up";
            }
            if(i > 25 && i <= 50) {
                direction = "down";
            }
            if(i > 50 && i <= 75) {
                direction = "left";
            }
            if(i > 75) {direction = "right";}
            actionLockCounter = 0;
        }
    }

    public void moveTowardPlayer(int interval) {

        actionLockCounter++;

        if(actionLockCounter > interval) {

            if(getXdistance(gamePanel.player) > getYdistance(gamePanel.player)) {
                if(gamePanel.player.getCenterX() < getCenterX()) {
                    direction = "left";
                }
                else {
                    direction = "right";
                }
            }
            else if(getXdistance(gamePanel.player) < getYdistance(gamePanel.player)) {
                if(gamePanel.player.getCenterY() < getCenterY()) {
                    direction = "up";
                }
                else {
                    direction = "down";
                }
            }
            actionLockCounter = 0;
        }
    }
    public Color getParticleColor() {return null;}
    public int getParticleSize() {return 0;}
    public int getParticleSpeed() {return 0;}
    public int getParticleMaxLife() {return 0;}
    public String getOppositeDirection(String direction) {
        String oppositeDirection = "";
        switch (direction) {
            case "up": oppositeDirection = "down"; break;
            case "down": oppositeDirection = "up"; break;
            case "left": oppositeDirection = "right"; break;
            case "right": oppositeDirection = "left"; break;
        }
        return oppositeDirection;
    }
    public void damagePlayer(int attack) {
        if(!gamePanel.player.invincible) {
            //We can give damage
            gamePanel.playSE(6);
            int damage = attack - gamePanel.player.defense;

            //Get an opposite direction of this attacker
            String canGuardDirection = getOppositeDirection(direction);
            if(gamePanel.player.guarding && gamePanel.player.direction.equals(canGuardDirection)) {
                //PARRY
                if(gamePanel.player.guardCounter < 10) {
                    damage = 0;
                    gamePanel.sound.playSoundEffect(17);
                    setKnockBack(this, gamePanel.player, knockBackPower);
                    offBalance = true;
                    spriteCounter =- 100;
                }
                else {
                    damage /= 3;
                    gamePanel.sound.playSoundEffect(16);
                }

            } else {
                gamePanel.sound.playSoundEffect(5);
                if(damage < 1) {
                    damage = 1;
                }
            }
            if(damage != 0) {
                setKnockBack(gamePanel.player, this, knockBackPower);
            }
            gamePanel.player.life -= damage;
            gamePanel.player.invincible = true;
        }
    }
    public void setKnockBack(Entity target, Entity attacker, int knockBackPower) {

        this.attacker = attacker;
        target.knockBackDirection = attacker.direction;
        target.speed += knockBackPower;
        target.knockBack = true;

    }
    public void dyingAnimation(Graphics2D graphics2D) {

        int i = 5;


        dyingCounter ++;
        if(dyingCounter <= i) {changeAlpha(graphics2D,0f);}
        if(dyingCounter > i && dyingCounter <= i * 2) {changeAlpha(graphics2D,1f);}
        if(dyingCounter > i * 2 && dyingCounter <= i * 3) {changeAlpha(graphics2D,0f);}
        if(dyingCounter > i * 3 && dyingCounter <= i * 4) {changeAlpha(graphics2D,1f);}
        if(dyingCounter > i * 4 && dyingCounter <= i * 5) {changeAlpha(graphics2D,1f);}
        if(dyingCounter > i * 5 && dyingCounter <= i * 6) {changeAlpha(graphics2D,0f);}
        if(dyingCounter > i * 6 && dyingCounter <= i * 7) {changeAlpha(graphics2D,1f);}
        if(dyingCounter > i * 7 && dyingCounter <= i * 8) {changeAlpha(graphics2D,0f);
        }
        if(dyingCounter > i * 8) {
            alive = false;
        }
    }

    public void generateParticle(Entity generator, Entity target) {

        Color color = generator.getParticleColor();
        int size = generator.getParticleSize();
        int speed = generator.getParticleSpeed();
        int maxLife = generator.getParticleMaxLife();

        Particle particle1 = new Particle(gamePanel,target,color,size,speed,maxLife, -2, -1);
        Particle particle2 = new Particle(gamePanel,target,color,size,speed,maxLife, 2,  -1);
        Particle particle3 = new Particle(gamePanel,target,color,size,speed,maxLife, -2, 1);
        Particle particle4 = new Particle(gamePanel,target,color,size,speed,maxLife, 2,  1);

        gamePanel.particleList.add(particle1);
        gamePanel.particleList.add(particle2);
        gamePanel.particleList.add(particle3);
        gamePanel.particleList.add(particle4);

    }

    public void changeAlpha(Graphics2D graphics2D, float alphaValue) {
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
    }

    public void searchPath(int goalCol, int goalRow) {

        int startCol = (worldX + solidArea.x) / gamePanel.tileSize;
        int startRow = (worldY + solidArea.y) / gamePanel.tileSize;

        gamePanel.pathFinder.setNodes(startCol, startRow, goalCol, goalRow,this);

        if(gamePanel.pathFinder.search()) {

            //Next worldX & worldY
            int nextX = gamePanel.pathFinder.pathList.get(0).col * gamePanel.tileSize;
            int nextY = gamePanel.pathFinder.pathList.get(0).row * gamePanel.tileSize;

            //Entity's solid area position
            int enLeftX = worldX + solidArea.x;
            int enRightX = worldX + solidArea.x + solidArea.width;
            int enTopY = worldY + solidArea.y;
            int enBottomY = worldY + solidArea.y + solidArea.height;

            if(enTopY > nextY && enLeftX >= nextX && enRightX < nextX + gamePanel.tileSize) {
                direction = "up";
            }
            else if(enTopY < nextY && enLeftX >= nextX && enRightX < nextX + gamePanel.tileSize) {
                direction = "down";
            }
            else if(enTopY >= nextY && enBottomY < nextY + gamePanel.tileSize) {
                // left or right
                if(enLeftX > nextX) {
                    direction = "left";
            }
                if(enLeftX < nextX) {
                    direction = "right";
                }
            }
            else if(enTopY > nextY && enLeftX > nextX) {
                //up or left
                direction = "up";
                checkCollision();
                if(collisionOn) {
                    direction = "left";
                }
            }
            else if(enTopY > nextY && enLeftX < nextX) {
                //up or right
                direction = "up";
                checkCollision();
                if(collisionOn) {
                    direction = "right";
                }
            }
            else if(enTopY < nextY && enLeftX > nextX) {
                //down or left
                direction = "down";
                checkCollision();
                if(collisionOn) {
                    direction = "left";
                }
            }
            else if(enTopY < nextY && enLeftX < nextX) {
                //down or right
                direction = "down";
                checkCollision();
                if(collisionOn) {
                    direction = "right";
                }
            }
            //If it reaches the goal, stop the path
//            int nextCol = gamePanel.pathFinder.pathList.get(0).col;
//            int nextRow = gamePanel.pathFinder.pathList.get(0).row;
//            if(nextCol == goalCol && nextRow == goalRow) {
//                onPath = false;
            }
        }
    public int getDetected(Entity user,Entity target[][], String targetName) {

        int index = 999;

        // Check the surrounding tiles

        int nextWorldX  = user.getLeftX();
        int nextWorldY  = user.getTopY();

        switch (user.direction) {
            case "up": nextWorldY = user.getTopY() - gamePanel.player.speed; break;
            case "down": nextWorldY = user.getBottomY() + gamePanel.player.speed; break;
            case "left": nextWorldX = user.getLeftX() - gamePanel.player.speed; break;
            case "right": nextWorldX = user.getRightX() + gamePanel.player.speed; break;
            }
            int col = nextWorldX / gamePanel.tileSize;
            int row = nextWorldY / gamePanel.tileSize;

            for(int i = 0; i < target[1].length; i++) {
                if(target[gamePanel.currentMap][i] != null)
                    if(target[gamePanel.currentMap][i].getCol() == col &&
                            target[gamePanel.currentMap][i].getRow() == row &&
                            target[gamePanel.currentMap][i].name.equals(targetName)) {
                        index = i;
                        break;
                    }
            }
            return index;
        }
    }
