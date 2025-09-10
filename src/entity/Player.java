    package entity;

    import game.GamePanel;
    import game.KeyHandler;
    import object.*;
    import java.awt.*;
    import java.awt.image.BufferedImage;


    public class Player extends Entity {

    KeyHandler keyHandler;

    public final int screenX;
    public final int screenY;
    public boolean lightUpdated = false;
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

    }
    public void setDefaultValues() {

        worldX = gamePanel.tileSize * 23;
        worldY = gamePanel.tileSize * 21;
        defaultSpeed = 4;
        speed = defaultSpeed;
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
        coin = 1000;
        currentLight = null;
        currentWeapon = new OBJ_Sword_Normal(gamePanel);
        currentShield = new OBJ_Shield_Wood(gamePanel);
        projectile = new OBJ_Fireball(gamePanel);
        currentLight = new OBJ_Lantern(gamePanel);
        attack = getAttack(); // Influenced by player's strength and weapon's attack value
        defense = getDefense(); // Influenced by player's dexterity and shield's defense stats

        getImage();
        getAttackImage();
        getGuardingImage();
        setItems();

    }

    public void setDefaultPositions() {
        worldX = gamePanel.tileSize * 23;
        worldY = gamePanel.tileSize * 21;
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
        inventory.add(new OBJ_Key(gamePanel));
        inventory.add(new OBJ_Axe(gamePanel));

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
        up1 = setup("player/player/boy_up_1", 1, 1);
        up2 = setup("player/player/boy_up_2", 1, 1);
        down1 = setup("player/player/boy_down_1", 1, 1);
        down2 = setup("player/player/boy_down_2", 1, 1);
        left1 = setup("player/player/boy_left_1", 1, 1);
        left2 = setup("player/player/boy_left_2", 1, 1);
        right1 = setup("player/player/boy_right_1", 1, 1);
        right2 = setup("player/player/boy_right_2", 1, 1);
    }
    public void getAttackImage() {

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
    public void getGuardingImage() {
        guardUp = setup("player/player/boy_guard_up", 1, 1);
        guardDown = setup("player/player/boy_guard_down", 1, 1);
        guardLeft = setup("player/player/boy_guard_left", 1, 1);
        guardRight = setup("player/player/boy_guard_right", 1, 1);

    }
    public void getSleepingImage(BufferedImage image) {
        up1 = image;
        up2 = image;
        down1 = image;
        down2 = image;
        left1 = image;
        left2 = image;
        right1 = image;
        right2 = image;
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
                gamePanel.checker.checkObject(this, true);
                gamePanel.checker.checkEntity(this, gamePanel.npc);
                gamePanel.checker.checkEntity(this, gamePanel.monster);
                gamePanel.checker.checkEntity(this, gamePanel.iTile);
                gamePanel.checker.checkTile(this);

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

                    if (npcIndex != 999 && gamePanel.keyHandler.enterPressed) {
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
            gamePanel.gameState = gamePanel.dialogueState;
            gamePanel.npc[gamePanel.currentMap][i].speak();
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
        g2.drawImage(image,tempScreenX,tempScreenY,null);

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

            if(selectedItem.type == type_sword || selectedItem.type == type_axe) {

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

        boolean canObtain = false;
        //CHECK IF STACKABLE
        if (item.stackable) {
            int index = searchItemInInventory(item.name);
            if (index != 999) {
                inventory.get(index).amount++;
                canObtain = true;
            } else { //NEW ITEM
                if (inventory.size() != maxInventorySize) {
                    inventory.add(item);
                    canObtain = true;
                }
            }
        }
        else { //NOT STACKABLE
            if (inventory.size() != maxInventorySize) {
                inventory.add(item);
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
        gamePanel.ui.currentDialogue = "You are level " + level + " !";
        }
    }


}
