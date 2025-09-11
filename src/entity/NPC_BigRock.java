package entity;

import game.GamePanel;
import interactive_tile.IT_MetalPlate;
import interactive_tile.InteractiveTile;

import java.awt.*;
import java.util.ArrayList;

public class NPC_BigRock extends Entity {
    public NPC_BigRock(GamePanel gamePanel) {

        super(gamePanel);
        name = "Big Rock";
        direction = "down";
        speed = 2;
        solidArea = new Rectangle(2,6,44,40);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        dialogueSet = -1;
        type = type_npc;

        getImage();
        setDialogue();
    }
    public void update() {}
    public void setDialogue(){

        dialogues[0][0] = "I'm a rock.";

    }
    public void getImage(){
        up1 = setup("npc/rock/bigrock",1,1);
        up2 = setup("npc/rock/bigrock",1,1);
        down1 = setup("npc/rock/bigrock",1,1);
        down2 = setup("npc/rock/bigrock",1,1);
        left1 = setup("npc/rock/bigrock",1,1);
        left2 = setup("npc/rock/bigrock",1,1);
        right1 = setup("npc/rock/bigrock",1,1);
        right2 = setup("npc/rock/bigrock",1,1);

    }

    public void speak() {

        facePlayer();
        startDialogue(this,dialogueSet);

        dialogueSet++;

        if(dialogues[dialogueSet][0] == null) {

            dialogueSet--;

        }
    }

    public void move(String direction) {

        this.direction = direction;
        collisionOn = false;

        gamePanel.checker.checkTile(this);
        gamePanel.checker.checkObject(this, false);
        gamePanel.checker.checkEntity(this, gamePanel.npc);
        gamePanel.checker.checkEntity(this, gamePanel.monster);
        gamePanel.checker.checkEntity(this, gamePanel.iTile);
        if(!collisionOn) {

            switch (direction) {
                case "up" : worldY -= speed; break;
                case "down" : worldY += speed; break;
                case "left" : worldX -= speed; break;
                case "right" : worldX += speed; break;
            }
        }
        detectPlate();
    }

    public void detectPlate() {

        ArrayList<InteractiveTile> plateList = new ArrayList<>();
        ArrayList<Entity> rockList = new ArrayList<>();

        //CREATE A PLATE LIST
        for(int i = 0; i < gamePanel.iTile[1].length; i++) {

            if(gamePanel.iTile[gamePanel.currentMap][i] != null && gamePanel.iTile[gamePanel.currentMap][i].name != null && gamePanel.iTile[gamePanel.currentMap][i].name.equals("Metal Plate")) {

                plateList.add(gamePanel.iTile[gamePanel.currentMap][i]);
            }
        }
        for(int i = 0; i < gamePanel.npc[1].length; i++) {
            if(gamePanel.npc[gamePanel.currentMap][i] != null && gamePanel.npc[gamePanel.currentMap][i].name.equals("Big Rock")) {
                rockList.add(gamePanel.npc[gamePanel.currentMap][i]);
            }
        }

        int count = 0;
        //Scan the plate list
        for(int i = 0; i < plateList.size(); i++) {

            int xDistance = Math.abs(worldX - plateList.get(i).worldX);
            int yDistance = Math.abs(worldY - plateList.get(i).worldY);
            int distance = Math.max(xDistance, yDistance);

            if(distance < 8) {

                if(linkedEntity == null) {
                    linkedEntity = plateList.get(i);
                    gamePanel.sound.playSoundEffect(3);
                }
            }
            else {
                if(linkedEntity == plateList.get(i)) {
                    linkedEntity = null;
                }
            }
        }

        for(int i = 0; i < rockList.size(); i++) {

            //Count the rock on the plate
            if(rockList.get(i).linkedEntity != null) {
                count++;
            }
        }

        //If all the rocks are on the plates, the iron door opens
        if(count == rockList.size()) {

            for(int i = 0; i < gamePanel.object[1].length; i++) {

                if(gamePanel.object[gamePanel.currentMap][i] != null && gamePanel.object[gamePanel.currentMap][i].name.equals("Iron Door")) {

                    gamePanel.object[gamePanel.currentMap][i] = null;
                    gamePanel.playSE(21);
                }
            }
        }
    }
}
