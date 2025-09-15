    package entity;

    import game.GamePanel;
    import game.KeyHandler;
    import object.*;

    import javax.imageio.ImageIO;
    import java.awt.*;
    import java.awt.image.BufferedImage;
    import java.io.IOException;


    public class Player extends Entity {

    KeyHandler keyHandler;

    public final int screenX;
    public final int screenY;
    public boolean lightUpdated = false;
    public boolean killSkeletonLord;
    public boolean killGoblinBoss;

    BufferedImage sheetRunning, sheetSword, sheetAxe, sheetPickaxe, sheetGuard, sheetGuardBlue,sheetSwordRed;



        public Player(GamePanel gamePanel, KeyHandler keyHandler) {

        super(gamePanel);
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;
            try {
                sheetSword = ImageIO.read(getClass().getResource("/player/player/playerSword.png"));
                sheetAxe = loadARGB("/player/player/playerAxe.png");
                sheetGuard = loadARGB("/player/player/playerGuard.png");
                sheetPickaxe = loadARGB("/player/player/playerPickaxe.png");
                sheetRunning = loadARGB("/player/player/playerRunning.png");
                sheetSwordRed = loadARGB("/player/player/playerSwordRed.png");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        screenX = gamePanel.screenWidth / 2 - (gamePanel.tileSize / 2);
        screenY = gamePanel.screenHeight / 2 - (gamePanel.tileSize / 2);

        //SOLID AREA
        solidArea = new Rectangle(10, 18, 26, 26);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;


        setDefaultValues();

    }




    public void setDefaultValues() {

        gamePanel.currentMap = 0;
        worldX = gamePanel.tileSize * 23;
        worldY = gamePanel.tileSize * 17;
        defaultSpeed = 4;
        speed = defaultSpeed;
        direction = "down";
        //PLAYER STATUS
        maxLife = 8; // 2 lives = 1 heart
        life = maxLife;
        maxMana = 6;
        mana = maxMana;
        level = 1;
        strength = 2; // The greater the strength, the more damage he gives.
        dexterity = 1; // The greater the dex, the less damage he receives.
        exp = 0;
        nextLevelExp = 5;
        coin = 0;
        currentLight = null;
        currentWeapon = new OBJ_Sword_Normal(gamePanel);
        currentShield = new OBJ_Shield_Wood(gamePanel);
        projectile = new OBJ_Fireball(gamePanel);
        killSkeletonLord = false;
        killGoblinBoss = false;
        attack = getAttack(); // Influenced by player's strength and weapon's attack value
        defense = getDefense(); // Influenced by player's dexterity and shield's defense stats

        getImage();
        getAttackImage();
        getGuardingImage();
        setItems();

    }

    public void setDefaultPositions() {
        gamePanel.currentMap = 0;
        gamePanel.currentArea = gamePanel.outside;
        worldX = gamePanel.tileSize * 23;
        worldY = gamePanel.tileSize * 17;
        direction = "down";

    }

    public void restoreStatus() {
        life = maxLife;
        mana = maxMana;
        invincible = false;
        attacking = false;
        guarding = false;
        knockBack = false;
        lightUpdated = true;
        speed = defaultSpeed;
    }

    public void setItems() {

        inventory.clear();
        inventory.add(currentWeapon);

    }
    public int getAttack() {
        attackArea = currentWeapon.attackArea;
        motion1_duration = currentWeapon.motion1_duration;
        motion2_duration = currentWeapon.motion2_duration;
        return attack = strength * currentWeapon.attackValue;
    }

    public int getDefense() {

        return defense = dexterity * currentShield.defenseValue;
    }

        public void getImage() {
            up1    = cut(sheetRunning,  0,  0, 16, 16, 1, 1);
            up2    = cut(sheetRunning, 16,  0, 16, 16, 1, 1);

            down1  = cut(sheetRunning, 32, 16, 16, 16, 1, 1);
            down2  = cut(sheetRunning, 48, 16, 16, 16, 1, 1);

            left1  = cut(sheetRunning, 16, 16, 16, 16, 1, 1);
            left2  = cut(sheetRunning, 48,  0, 16, 16, 1, 1);

            right1 = cut(sheetRunning, 32,  0, 16, 16, 1, 1);
            right2 = cut(sheetRunning,  0, 16, 16, 16, 1, 1);
        }
        public void getAttackImage() {
            if (currentWeapon.type == type_sword) {
                attackDown1  = cut(sheetSword,    0,   0, 16, 32, 1, 2);
                attackDown2  = cut(sheetSword,   16,   0, 16, 32, 1, 2);

                attackLeft1  = cut(sheetSword,    0,  32, 32, 16, 2, 1);
                attackLeft2  = cut(sheetSword,    0,  48, 32, 16, 2, 1);

                attackRight1 = cut(sheetSword,    0,  64, 32, 16, 2, 1);
                attackRight2 = cut(sheetSword,    0,  80, 32, 16, 2, 1);

                attackUp1    = cut(sheetSword,    0,  96, 16, 32, 1, 2);
                attackUp2    = cut(sheetSword,   16,  96, 16, 32, 1, 2);
            }

            if (currentWeapon.type == type_sword_red) {
                attackDown1  = cut(sheetSwordRed,    0,   0, 16, 32, 1, 2);
                attackDown2  = cut(sheetSwordRed,   16,   0, 16, 32, 1, 2);

                attackLeft1  = cut(sheetSwordRed,    0,  32, 32, 16, 2, 1);
                attackLeft2  = cut(sheetSwordRed,    0,  48, 32, 16, 2, 1);

                attackRight1 = cut(sheetSwordRed,    0,  64, 32, 16, 2, 1);
                attackRight2 = cut(sheetSwordRed,    0,  80, 32, 16, 2, 1);

                attackUp1    = cut(sheetSwordRed,    0,  96, 16, 32, 1, 2);
                attackUp2    = cut(sheetSwordRed,   16,  96, 16, 32, 1, 2);
            }

            if (currentWeapon.type == type_axe) {
                attackDown1  = cut(sheetAxe,      0,   0, 16, 32, 1, 2);
                attackDown2  = cut(sheetAxe,     16,   0, 16, 32, 1, 2);

                attackUp1    = cut(sheetAxe,      0,  32, 16, 32, 1, 2);
                attackUp2    = cut(sheetAxe,     16,  32, 16, 32, 1, 2);

                attackLeft1  = cut(sheetAxe,      0,  64, 32, 16, 2, 1);
                attackLeft2  = cut(sheetAxe,      0,  80, 32, 16, 2, 1);

                attackRight1 = cut(sheetAxe,      0,  96, 32, 16, 2, 1);
                attackRight2 = cut(sheetAxe,      0, 112, 32, 16, 2, 1);
            }

            if (currentWeapon.type == type_pickaxe) {
                attackDown1  = cut(sheetPickaxe,  0,   0, 16, 32, 1, 2);
                attackDown2  = cut(sheetPickaxe, 16,   0, 16, 32, 1, 2);

                attackUp1    = cut(sheetPickaxe,  0,  32, 16, 32, 1, 2);
                attackUp2    = cut(sheetPickaxe, 16,  32, 16, 32, 1, 2);

                attackLeft1  = cut(sheetPickaxe,  0,  64, 32, 16, 2, 1);
                attackLeft2  = cut(sheetPickaxe,  0,  80, 32, 16, 2, 1);

                attackRight1 = cut(sheetPickaxe,  0,  96, 32, 16, 2, 1);
                attackRight2 = cut(sheetPickaxe,  0, 112, 32, 16, 2, 1);
            }
        }

        public void getGuardingImage() {

            guardDown = cut(sheetGuard, 0, 0, 16, 16, 1, 1);
            guardLeft = cut(sheetGuard, 16, 0, 16, 16, 1, 1);
            guardRight = cut(sheetGuard, 32, 0, 16, 16, 1, 1);
            guardUp = cut(sheetGuard, 48, 0, 16, 16, 1, 1);
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

            if(knockBack) {
                collisionOn = false;
                collision = false;
                gamePanel.checker.checkObject(this, true);
                gamePanel.checker.checkEntity(this, gamePanel.npc);
                gamePanel.checker.checkEntity(this, gamePanel.monster);
                gamePanel.checker.checkEntity(this, gamePanel.iTile);
                gamePanel.checker.checkTile(this,knockBackDirection);

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
            else if (attacking) {
                // Only handle attack animation
                attack();
            }
            else if(gamePanel.keyHandler.guardKeyPressed) {
                guarding = true;
                guardCounter++;


            }
            else {
                // Collect movement input
                if (gamePanel.keyHandler.upPressed) {
                    direction = "up";
                }
                if (gamePanel.keyHandler.downPressed) {
                    direction = "down";
                }
                if (gamePanel.keyHandler.leftPressed) {
                    direction = "left";
                }
                if (gamePanel.keyHandler.rightPressed) {
                    direction = "right";
                }

                // Reset collision flag
                collisionOn = false;
                boolean moving = gamePanel.keyHandler.upPressed || gamePanel.keyHandler.downPressed ||
                        gamePanel.keyHandler.leftPressed || gamePanel.keyHandler.rightPressed;
                if (moving) {
                    // Check Tile Collision
                    gamePanel.checker.checkTile(this);
                    int objIndex = gamePanel.checker.checkObject(this, true);
                    pickUpObject(objIndex);
                    int npcIndex = gamePanel.checker.checkEntity(this, gamePanel.npc);
                    gamePanel.checker.checkEntity(this, gamePanel.iTile);

                    if (npcIndex != 999) {
                        interactNPC(npcIndex);
                        gamePanel.keyHandler.enterPressed = false; // consume input
                    } else if (gamePanel.keyHandler.enterPressed) {
                        gamePanel.keyHandler.enterPressed = false;
                    }
                    if (!collisionOn) {
                        switch (direction) {
                            case "up": worldY -= speed;break;
                            case "down": worldY += speed;break;
                            case "left": worldX -= speed;break;
                            case "right": worldX += speed;break;
                        }
                    }
                    if (worldX != 0 || worldY != 0) {
                        spriteCounter++;
                        if (spriteCounter > 14) {
                            spriteNum = (spriteNum == 1) ? 2 : 1;
                            spriteCounter = 0;
                        }
                    }
                }
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
                gamePanel.checker.checkEntity(this, gamePanel.iTile);
                // Check events
                gamePanel.eventHandler.checkEvent();

                // Walking animation
                guarding = false;
                guardCounter = 0;
                gamePanel.keyHandler.guardKeyPressed = false;
            }

            if(gamePanel.keyHandler.shotKeyPressed && !projectile.alive && shotAvailableCounter == 120 && projectile.haveResource(this)) {

                //SET DEFAULT COORDINATES, DIRECTION AND USER
                projectile.set(worldX, worldY, direction, true, this);

                //SUBTRACT THE COST(MANA, AMMO)
                projectile.subtractResource(this);

                //CHECK VACANCY
                for(int i = 0; i < gamePanel.projectile[1].length; i++) {
                    if(gamePanel.projectile[gamePanel.currentMap][i] == null) {
                        gamePanel.projectile[gamePanel.currentMap][i] = projectile;
                        break;
                    }
                }

                gamePanel.playSE(11);

                shotAvailableCounter = 0;

                gamePanel.keyHandler.shotKeyPressed = false;
            }

            // Invincibility timer
            if (invincible) {
                invincibleCounter++;
                if (invincibleCounter > 60) {
                    invincible = false;
                    transparent = false;
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

    public void pickUpObject(int i) {

        if(i != 999) {
            //PICKUP ONLY ITEMS
            if(gamePanel.object[gamePanel.currentMap][i].type == type_pickUpOnly) {
                gamePanel.object[gamePanel.currentMap][i].use(this);
                gamePanel.object[gamePanel.currentMap][i] = null;
            }
                //OBSTACLE
                else if(gamePanel.object[gamePanel.currentMap][i].type == type_obstacle) {
                    if(keyHandler.enterPressed) {
                        gamePanel.object[gamePanel.currentMap][i].interact();
                    }
                } else {
                //INVENTORY ITEMS
                String text;
                if(canObtainItem(gamePanel.object[gamePanel.currentMap][i])) {
                    gamePanel.sound.playSoundEffect(1);
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
        if (i != 999) {
            if(gamePanel.keyHandler.enterPressed) {
                gamePanel.npc[gamePanel.currentMap][i].speak();
            }
        }
        gamePanel.npc[gamePanel.currentMap][i].move(direction);
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
                if(guarding) {
                    image = guardUp;
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
                if(guarding) {
                    image = guardDown;
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
                if(guarding) {
                    image = guardLeft;
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
                if(guarding) {
                    image = guardRight;
                }
                break;
        }

        if( invincible ) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }
        if(drawing) {
            g2.drawImage(image,tempScreenX,tempScreenY,null);
        }

        //RESET ALPHA
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        //DEBUG
    //        g2.setFont(new Font("Arial",Font.PLAIN,26));
    //        g2.setColor(Color.white);
    //        g2.drawString("Invincible: "+ invincibleCounter, gamePanel.tileSize, gamePanel.screenHeight / 2);
    }
    public void selectItem() {

        int itemIndex = gamePanel.ui.getItemIndexOnSlot(gamePanel.ui.playerSlotCol,gamePanel.ui.playerSlotRow);

        if(itemIndex < inventory.size()) {

            Entity selectedItem = inventory.get(itemIndex);

            if(selectedItem.type == type_sword || selectedItem.type == type_axe || selectedItem.type == type_pickaxe || selectedItem.type == type_sword_red) {

                currentWeapon = selectedItem;
                attack = getAttack();
                getAttackImage();
            }
            if(selectedItem.type == type_shield) {

                currentShield = selectedItem;
                defense = getDefense();
            }
            if(selectedItem.type == type_light) {
                if(currentLight == selectedItem) {
                    currentLight = null;
                } else {
                    currentLight = selectedItem;
                }
                lightUpdated = true;
            }
            if(selectedItem.type == type_consumable) {

                if(selectedItem.use(this)){
                    if(selectedItem.amount > 1) {
                        selectedItem.amount--;
                    }
                    else {
                        inventory.remove(itemIndex);
                    }
                }
            }
        }
    }
    public int searchItemInInventory(String itemName) {

        int itemIndex = 999;

        for(int i = 0; i < inventory.size(); i++) {
            if(inventory.get(i).name.equals(itemName)) {
                itemIndex = i;
                break;
            }
        }
        return itemIndex;
    }

    public boolean canObtainItem(Entity item) {

        Entity newItem = gamePanel.entityGenerator.getObject(item.name);

        boolean canObtain = false;
        //CHECK IF STACKABLE
        if (newItem.stackable) {
            int index = searchItemInInventory(newItem.name);
            if (index != 999) {
                inventory.get(index).amount++;
                canObtain = true;
            } else { //NEW ITEM
                if (inventory.size() != maxInventorySize) {
                    inventory.add(newItem);
                    canObtain = true;
                }
            }
        }
        else { //NOT STACKABLE
            if (inventory.size() != maxInventorySize) {
                inventory.add(newItem);
                canObtain = true;
            }
        }
        return canObtain;
    }

    public void contactMonster(int i) {

        if(i != 999) {
            if(!invincible && !gamePanel.monster[gamePanel.currentMap][i].dying) {
                gamePanel.playSE(6);
                int damage = gamePanel.monster[gamePanel.currentMap][i].attack - defense;
                if(damage < 1) {
                    damage = 1;
                }
                life -= damage;
                invincible = true;
                transparent = true;
            }
        }
    }

    public void damageMonster(int monsterIndex, Entity attacker, int attack, int knockBackPower) {
        if(monsterIndex != 999) {
            if(!gamePanel.monster[gamePanel.currentMap][monsterIndex].invincible) {
                gamePanel.playSE(5);
                if(knockBackPower > 0) {
                    setKnockBack(gamePanel.monster[gamePanel.currentMap][monsterIndex], attacker, knockBackPower);
                }
                if(gamePanel.monster[gamePanel.currentMap][monsterIndex].offBalance) {
                    attack *= 2;
                }

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

    public void damageProjectile(int i) {

        if(i != 999) {
            Entity projectile = gamePanel.projectile[gamePanel.currentMap][i];
            projectile.alive = false;
            generateParticle(projectile, projectile);
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

                dialogues[0][0] = "You are level " + level + " !";
                // Set the dialogue and then start it
                startDialogue(this, 0);
            }
        }
}
