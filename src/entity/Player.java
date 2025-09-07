    package entity;

    import game.GamePanel;
    import game.KeyHandler;
    import game.Sound;
    import game.UtilityTool;
    import object.*;

    import javax.imageio.ImageIO;
    import java.awt.*;
    import java.awt.image.BufferedImage;
    import java.io.IOException;
    import java.util.ArrayList;

    public class Player extends Entity {

    KeyHandler keyHandler;

    public final int screenX;
    public final int screenY;
    int standCounter = 0;
    public boolean attackCanceled = false;
    public ArrayList<Entity> inventory = new ArrayList<>();
    int maxInventorySize = 20;
    public Player(GamePanel gamePanel, KeyHandler keyHandler) {

        super(gamePanel);
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;

        screenX = gamePanel.screenWidth / 2 - (gamePanel.tileSize / 2);
        screenY = gamePanel.screenHeight / 2 - (gamePanel.tileSize / 2);

        //SOLID AREA
        solidArea = new Rectangle(10, 18, 26, 26);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;


        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
        setItems();
    }

    public void setDefaultValues() {

        worldX = gamePanel.tileSize * 23;
        worldY = gamePanel.tileSize * 13;
        speed = 4;
        direction = "down";

        //PLAYER STATUS
        maxLife = 6; // 2 lives = 1 heart
        life = maxLife;
        maxMana = 6;
        mana = maxMana;
        level = 1;
        strength = 1; // The greater the strength, the more damage he gives.
        dexterity = 1; // The greater the dex, the less damage he receives.
        exp = 0;
        nextLevelExp = 5;
        coin = 0;
        currentWeapon = new OBJ_Sword_Normal(gamePanel);
        currentShield = new OBJ_Shield_Wood(gamePanel);
        projectile = new OBJ_Fireball(gamePanel);
        attack = getAttack(); // Influenced by player's strength and weapon's attack value
        defense = getDefense(); // Influenced by player's dexterity and shield's defense stats

    }

    public void setDefaultPositions() {
        worldX = gamePanel.tileSize * 23;
        worldY = gamePanel.tileSize * 21;
        direction = "down";

    }

    public void restoreLifeAndMana() {
        life = maxLife;
        mana = maxMana;
        invincible = false;
    }

    public void setItems() {

        inventory.clear();
        inventory.add(currentWeapon);
        inventory.add(new OBJ_Key(gamePanel));
        inventory.add(new OBJ_Axe(gamePanel));

    }
    public int getAttack() {
        attackArea = currentWeapon.attackArea;
        return attack = strength * currentWeapon.attackValue;
    }

    public int getDefense() {

        return defense = dexterity * currentShield.defenseValue;
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

        if(currentWeapon.type == type_sword){

            attackUp1 = setup("player/player/boy_attack_up_1", 1, 2);
            attackUp2 = setup("player/player/boy_attack_up_2", 1, 2);
            attackDown1 = setup("player/player/boy_attack_down_1", 1, 2);
            attackDown2 = setup("player/player/boy_attack_down_2", 1, 2);
            attackLeft1 = setup("player/player/boy_attack_left_1", 2, 1);
            attackLeft2 = setup("player/player/boy_attack_left_2", 2, 1);
            attackRight1 = setup("player/player/boy_attack_right_1", 2, 1);
            attackRight2 = setup("player/player/boy_attack_right_2", 2, 1);

        }
        if(currentWeapon.type == type_axe){

            attackUp1 = setup("player/player/boy_axe_up_1", 1, 2);
            attackUp2 = setup("player/player/boy_axe_up_2", 1, 2);
            attackDown1 = setup("player/player/boy_axe_down_1", 1, 2);
            attackDown2 = setup("player/player/boy_axe_down_2", 1, 2);
            attackLeft1 = setup("player/player/boy_axe_left_1", 2, 1);
            attackLeft2 = setup("player/player/boy_axe_left_2", 2, 1);
            attackRight1 = setup("player/player/boy_axe_right_1", 2, 1);
            attackRight2 = setup("player/player/boy_axe_right_2", 2, 1);

        }

    }

    public void update() {


        if (gamePanel.gameState == gamePanel.playState ) {
            if(!gamePanel.sound.musicThemeFlag) {
                gamePanel.sound.stopSE(13);
                gamePanel.sound.playMusic(0);
                gamePanel.sound.musicThemeFlag = true;
                gamePanel.sound.gameOverFlag = false;
            }


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

                // Check Tile Collision
                gamePanel.checker.checkTile(this, dx, dy);

                //Check Object collision
                int objIndex = gamePanel.checker.checkObject(this, true);
                pickUpObject(objIndex);

                // Check NPC Collision
                int npcIndex = gamePanel.checker.checkEntity(this, gamePanel.npc);
                if (npcIndex != 999 && gamePanel.keyHandler.enterPressed) {
                    interactNPC(npcIndex);
                    gamePanel.keyHandler.enterPressed = false; // consume input
                }
                // Check Monster Collision
                int monsterIndex = gamePanel.checker.checkEntity(this, gamePanel.monster);
                contactMonster(monsterIndex);

                // Check Interactive Tile Collision
                int iTileIndex = gamePanel.checker.checkEntity(this, gamePanel.iTile);

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

            if(gamePanel.keyHandler.shotKeyPressed && !projectile.alive && shotAvailableCounter == 120 && projectile.haveResource(this)) {

                //SET DEFAULT COORDINATES, DIRECTION AND USER
                projectile.set(worldX, worldY, direction, true, this);

                //SUBTRACT THE COST(MANA, AMMO)
                projectile.subtractResource(this);
                //ADD IT TO THE LIST
                gamePanel.projectileList.add(projectile);
                gamePanel.playSE(11);

                shotAvailableCounter = 0;

                gamePanel.keyHandler.shotKeyPressed = false;
            }

            // Invincibility timer
            if (invincible) {
                invincibleCounter++;
                if (invincibleCounter == 60) {
                    invincible = false;
                    invincibleCounter = 0;
                }
            }
            if(life > maxLife) {
                life = maxLife;
            }
            if(mana > maxMana) {
                mana = maxMana;
            }
            if(life <= 0) {
                gamePanel.gameState = gamePanel.gameOverState;
                gamePanel.sound.stopMusic();
                gamePanel.sound.playSoundEffect(13);
                gamePanel.sound.gameOverFlag = true;
                gamePanel.sound.musicThemeFlag = false;
            }
        }
        if(shotAvailableCounter < 120) {
            shotAvailableCounter++;
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
                damageMonster(monsterIndex, attack); // plays monster hit sound (SE 5)
            }

            int iTileIndex = gamePanel.checker.checkEntity(this, gamePanel.iTile);
            damageInteractiveTile(iTileIndex);

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

        if(i != 999) {

            //PICKUP ONLY ITEMS
            if(gamePanel.object[gamePanel.currentMap][i].type == type_pickUpOnly) {

                gamePanel.object[gamePanel.currentMap][i].use(this);
                gamePanel.object[gamePanel.currentMap][i] = null;
            } else {

                String text;
                if(inventory.size() != maxInventorySize) {
                    inventory.add(gamePanel.object[gamePanel.currentMap][i]);
                    gamePanel.playSE(1);
                    text = "Got a " + gamePanel.object[gamePanel.currentMap][i].name + "!";
                }
                else {
                    text = "You cannot carry other stuff anymore!";
                }
                gamePanel.ui.addMessage(text);
                gamePanel.object[gamePanel.currentMap][i] = null;
            }
            //INVENTORY ITEMS


        }
    }

    public void interactNPC(int i) {
        if(gamePanel.keyHandler.enterPressed){
        if(i != 999) {
                gamePanel.gameState = gamePanel.dialogueState;
                gamePanel.npc[gamePanel.currentMap][i].speak();
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
    public void selectItem() {

        int itemIndex = gamePanel.ui.getItemIndexOnSlot();

        if(itemIndex < inventory.size()) {

            Entity selectedItem = inventory.get(itemIndex);

            if(selectedItem.type == type_sword || selectedItem.type == type_axe) {

                currentWeapon = selectedItem;
                attack = getAttack();
                getPlayerAttackImage();
            }
            if(selectedItem.type == type_shield) {

                currentShield = selectedItem;
                defense = getDefense();

            }
            if(selectedItem.type == type_consumable) {

                selectedItem.use(this);
                inventory.remove(itemIndex);
            }
        }
    }
    public void contactMonster(int i) {

        if(i != 999) {
            if(!invincible && !gamePanel.monster[gamePanel.currentMap][i].dying) {
                gamePanel.playSE(6);
                int damage = gamePanel.monster[gamePanel.currentMap][i].attack - defense;
                if(damage < 0) {
                    damage = 0;
                }
                life -= damage;
                invincible = true;
            }
        }
    }

    public void damageMonster(int monsterIndex, int attack) {
        if(monsterIndex != 999) {
            if(!gamePanel.monster[gamePanel.currentMap][monsterIndex].invincible) {
                gamePanel.playSE(5);
                int damage = attack - gamePanel.monster[gamePanel.currentMap][monsterIndex].defense;
                if(damage < 0) {
                    damage = 0;
                }
                gamePanel.monster[gamePanel.currentMap][monsterIndex].life -= damage;
                gamePanel.ui.addMessage(damage + " damage!");
                gamePanel.monster[gamePanel.currentMap][monsterIndex].invincible = true;
                gamePanel.monster[gamePanel.currentMap][monsterIndex].damageReaction();
                if(gamePanel.monster[gamePanel.currentMap][monsterIndex].life <= 0) {
                    gamePanel.monster[gamePanel.currentMap][monsterIndex].dying = true;
                    gamePanel.ui.addMessage("You killed the " + gamePanel.monster[gamePanel.currentMap][monsterIndex].name + " !" );
                    gamePanel.ui.addMessage("Exp + " + gamePanel.monster[gamePanel.currentMap][monsterIndex].exp + " !" );
                    exp += gamePanel.monster[gamePanel.currentMap][monsterIndex].exp;
                    checkLevelUp();
                }
            }
        }
        }
    public void damageInteractiveTile(int i) {

            if(i != 999 && gamePanel.iTile[gamePanel.currentMap][i].destructible && !gamePanel.iTile[gamePanel.currentMap][i].invincible && gamePanel.iTile[gamePanel.currentMap][i].isCorrectItem(this)) {

                gamePanel.iTile[gamePanel.currentMap][i].playSE();
                gamePanel.iTile[gamePanel.currentMap][i].life--;
                gamePanel.iTile[gamePanel.currentMap][i].invincible = true;

                //Generate particles
                generateParticle(gamePanel.iTile[gamePanel.currentMap][i], gamePanel.iTile[gamePanel.currentMap][i]);

                if(gamePanel.iTile[gamePanel.currentMap][i].life == 0) {
                    gamePanel.iTile[gamePanel.currentMap][i] = gamePanel.iTile[gamePanel.currentMap][i].getDestroyedForm();
                }
            }
    }
    public void checkLevelUp() {
        if(exp >= nextLevelExp) {

            level++;
            nextLevelExp = nextLevelExp * 2;
            maxLife += 2;
            strength++;
            dexterity++;
            attack = getAttack();
            defense = getDefense();

        gamePanel.playSE(8);
        gamePanel.gameState = gamePanel.dialogueState;
        gamePanel.ui.currentDialogue = "You are level " + level + " !";
        }
    }


}
