package entity;
import game.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Entity {

    GamePanel gamePanel;
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public BufferedImage attackUp1, attackUp2, attackDown1,attackDown2,attackLeft1,attackLeft2,attackRight1,attackRight2;
    public BufferedImage image, image2,image3;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public Rectangle attackArea = new Rectangle(0,0,0,0);

    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;
    String[] dialogues = new String[20];

    //STATE
    public int worldX, worldY;
    public String direction = "down";
    public int spriteNum = 1;
    int dialogueIndex = 0;
    public boolean collision = false;
    public boolean invincible = false;
    public boolean attacking = false;
    public boolean alive = true;
    public boolean dying = false;
    boolean hpBarOn = false;
    public boolean onPath = false;
    public boolean knockBack = false;

    //COUNTER
    public int spriteCounter = 0;
    public int invincibleCounter = 0;
    public int actionLockCounter = 0;
    int dyingCounter = 0;
    int hpBarCounter = 0;
    public int shotAvailableCounter = 0;
    int knockBackCounter = 0;

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
    public Entity currentWeapon;
    public Entity currentShield;
    public Projectile projectile;
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

    public Entity(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void draw(Graphics2D graphics2D) {

            BufferedImage image = null;
            int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
            int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;

            if (worldX + gamePanel.tileSize > gamePanel.player.worldX - gamePanel.player.screenX &&
                    worldX - gamePanel.tileSize < gamePanel.player.worldX + gamePanel.player.screenX &&
                    worldY + gamePanel.tileSize > gamePanel.player.worldY - gamePanel.player.screenY &&
                    worldY - gamePanel.tileSize < gamePanel.player.worldY + gamePanel.player.screenY) {

                switch (direction) {
                    case "up":
                        if (spriteNum == 1) {image = up1;}
                        if (spriteNum == 2) {image = up2;}
                        break;
                    case "down":
                        if (spriteNum == 1) {image = down1;}
                        if (spriteNum == 2) {image = down2;}
                        break;
                    case "left":
                        if (spriteNum == 1) {image = left1;}
                        if (spriteNum == 2) {image = left2;}
                        break;
                    case "right":
                        if (spriteNum == 1) {image = right1;}
                        if (spriteNum == 2) {image = right2;}
                        break;
                }

                //MONSTER HP bar
                if(type == 2 && hpBarOn) {

                    double oneScale = (double)gamePanel.tileSize / maxLife;
                    double hpBarValue = oneScale * life;


                    graphics2D.setColor(new Color(35,35,35));
                    graphics2D.fillRect(screenX - 1,screenY - 1,gamePanel.tileSize + 2, 12);

                    graphics2D.setColor(new Color(185, 185, 185));
                    graphics2D.fillRect(screenX, screenY, gamePanel.tileSize,10);

                    graphics2D.setColor(new Color(255,0,30));
                    graphics2D.fillRect(screenX, screenY, (int)hpBarValue,10);

                    hpBarCounter++;

                    if(hpBarCounter > 600) {
                        hpBarCounter = 0;
                        hpBarOn = false;
                    }
                }

                if( invincible ) {
                    changeAlpha(graphics2D,0.4F);
                    hpBarCounter = 0;
                    hpBarOn =true;
                }
                if(dying) {
                    dyingAnimation(graphics2D);
                }
                    graphics2D.drawImage(image, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);
                changeAlpha(graphics2D,1F);
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

    public void damageReaction() {}

    public void speak() {
        if(dialogues[dialogueIndex] == null){
            dialogueIndex = 0;
        }
        gamePanel.ui.currentDialogue = dialogues[dialogueIndex];
        dialogueIndex++;

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

    public void use(Entity entity) {}

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
                switch (direction) {
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
        } else {
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
        }

        // sprite animation
        spriteCounter++;
        if (spriteCounter > 24) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
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
    }


    public Color getParticleColor() {return null;}
    public int getParticleSize() {return 0;}
    public int getParticleSpeed() {return 0;}
    public int getParticleMaxLife() {return 0;}

    public void damagePlayer(int attack) {
        if(!gamePanel.player.invincible) {
            //We can give damage
            gamePanel.playSE(6);
            int damage = attack - gamePanel.player.defense;

            if(damage < 0) {
                damage = 0;
            }
            gamePanel.player.life -= damage;
            gamePanel.player.invincible = true;
        }
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
    }
