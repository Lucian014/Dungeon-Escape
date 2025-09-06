package game;

import entity.NPC_Priest;
import interactive_tile.IT_DryTree;
import interactive_tile.InteractiveTile;
import monster.MON_GreenSlime;
import object.*;

public class AssetSetter {

    GamePanel gamePanel;

    public AssetSetter(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }

    public void setObject() {
        int i = 0;
        gamePanel.object[i] = new OBJ_Coin_Bronze(gamePanel);
        gamePanel.object[i].worldX = 25 * gamePanel.tileSize;
        gamePanel.object[i].worldY = 23 * gamePanel.tileSize;
        i++;
        gamePanel.object[i] = new OBJ_Coin_Bronze(gamePanel);
        gamePanel.object[i].worldX = 21 * gamePanel.tileSize;;
        gamePanel.object[i].worldY = 19 * gamePanel.tileSize;;
        i++;
        gamePanel.object[i] = new OBJ_Coin_Bronze(gamePanel);
        gamePanel.object[i].worldX = 26 * gamePanel.tileSize;;
        gamePanel.object[i].worldY = 21 * gamePanel.tileSize;;
        i++;
        gamePanel.object[i] = new OBJ_Axe(gamePanel);
        gamePanel.object[i].worldX = 33 * gamePanel.tileSize;;
        gamePanel.object[i].worldY = 21 * gamePanel.tileSize;;
        i++;
        gamePanel.object[i] = new OBJ_Shield_Blue(gamePanel);
        gamePanel.object[i].worldX = 35 * gamePanel.tileSize;;
        gamePanel.object[i].worldY = 21 * gamePanel.tileSize;;
        i++;
        gamePanel.object[i] = new OBJ_Potion_Red(gamePanel);
        gamePanel.object[i].worldX = 37 * gamePanel.tileSize;;
        gamePanel.object[i].worldY = 21 * gamePanel.tileSize;;
        i++;
        gamePanel.object[i] = new OBJ_Heart(gamePanel);
        gamePanel.object[i].worldX = 22 * gamePanel.tileSize;;
        gamePanel.object[i].worldY = 29 * gamePanel.tileSize;;
        i++;
        gamePanel.object[i] = new OBJ_ManaCrystal(gamePanel);
        gamePanel.object[i].worldX = 22 * gamePanel.tileSize;;
        gamePanel.object[i].worldY = 31 * gamePanel.tileSize;;
        i++;
    }

    public void setNPC() {

        gamePanel.npc[0] = new NPC_Priest(gamePanel);
        gamePanel.npc[0].worldX = gamePanel.tileSize * 21;
        gamePanel.npc[0].worldY = gamePanel.tileSize * 21;

    }

    public void setMonster() {

        int i = 0;

        gamePanel.monster[i] = new MON_GreenSlime(gamePanel);
        gamePanel.monster[i].worldX = gamePanel.tileSize  * 23;
        gamePanel.monster[i].worldY = gamePanel.tileSize  * 16;
        i++;
        gamePanel.monster[i] = new MON_GreenSlime(gamePanel);
        gamePanel.monster[i].worldX = gamePanel.tileSize  * 24;
        gamePanel.monster[i].worldY = gamePanel.tileSize  * 37;
        i++;
        gamePanel.monster[i] = new MON_GreenSlime(gamePanel);
        gamePanel.monster[i].worldX = gamePanel.tileSize  * 21;
        gamePanel.monster[i].worldY = gamePanel.tileSize  * 38;
        i++;
        gamePanel.monster[i] = new MON_GreenSlime(gamePanel);
        gamePanel.monster[i].worldX = gamePanel.tileSize  * 34;
        gamePanel.monster[i].worldY = gamePanel.tileSize  * 42;
        i++;
        gamePanel.monster[i] = new MON_GreenSlime(gamePanel);
        gamePanel.monster[i].worldX = gamePanel.tileSize  * 38;
        gamePanel.monster[i].worldY = gamePanel.tileSize  * 42;
        i++;


    }

    public void setInteractiveTile() {

        int i = 0;
        gamePanel.iTile[i] = new IT_DryTree(gamePanel,27,12);i++;
        gamePanel.iTile[i] = new IT_DryTree(gamePanel,28,12);i++;
        gamePanel.iTile[i] = new IT_DryTree(gamePanel,29,12);i++;
        gamePanel.iTile[i] = new IT_DryTree(gamePanel,31,21);i++;
        gamePanel.iTile[i] = new IT_DryTree(gamePanel,31,12);i++;
        gamePanel.iTile[i] = new IT_DryTree(gamePanel,32,12);i++;
        gamePanel.iTile[i] = new IT_DryTree(gamePanel,33,12);i++;
    }

}
