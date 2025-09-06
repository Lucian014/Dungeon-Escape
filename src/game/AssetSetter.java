package game;

import entity.Entity;
import entity.NPC_Priest;
import interactive_tile.IT_DryTree;
import interactive_tile.InteractiveTile;
import monster.MON_GreenSlime;
import object.*;

import java.lang.reflect.Constructor;

public class AssetSetter {

    GamePanel gamePanel;

    public AssetSetter(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }

    private <T extends Entity> T createEntity(Class<T> entityClass, int worldX, int worldY) {
        try {
            Constructor<T> constructor = entityClass.getConstructor(GamePanel.class);
            T entity = constructor.newInstance(gamePanel);
            entity.worldX = worldX * gamePanel.tileSize;
            entity.worldY = worldY * gamePanel.tileSize;
            return entity;
        } catch (Exception e) {
            System.err.println("Error creating entity: " + entityClass.getSimpleName());
            e.printStackTrace();
            return null;
        }
    }

    private <T extends InteractiveTile> T createInteractiveTile(Class<T> tileClass, int col, int row) {
        try {
            Constructor<T> constructor = tileClass.getConstructor(
                    GamePanel.class, int.class, int.class
            );
            return constructor.newInstance(gamePanel, col, row);
        } catch (Exception e) {
            System.err.println("Error creating interactive tile: " + tileClass.getSimpleName());
            e.printStackTrace();
            return null;
        }
    }

    public void setObject() {
        int i = 0;

        // Using the parameterized factory - much cleaner!
        gamePanel.object[i++] = createEntity(OBJ_Coin_Bronze.class, 25, 23);
        gamePanel.object[i++] = createEntity(OBJ_Coin_Bronze.class, 21, 19);
        gamePanel.object[i++] = createEntity(OBJ_Coin_Bronze.class, 26, 21);
        gamePanel.object[i++] = createEntity(OBJ_Axe.class, 33, 21);
        gamePanel.object[i++] = createEntity(OBJ_Shield_Blue.class, 35, 21);
        gamePanel.object[i++] = createEntity(OBJ_Potion_Red.class, 37, 21);
        gamePanel.object[i++] = createEntity(OBJ_Heart.class,22,29);
        gamePanel.object[i++] = createEntity(OBJ_ManaCrystal.class,22,31);

    }

    public void setNPC() {
        gamePanel.npc[0] = createEntity(NPC_Priest.class, 21, 21);
    }

    public void setMonster() {
        int i = 0;
        gamePanel.monster[i++] = createEntity(MON_GreenSlime.class, 23, 16);
        gamePanel.monster[i++] = createEntity(MON_GreenSlime.class, 24, 37);
        gamePanel.monster[i++] = createEntity(MON_GreenSlime.class, 21, 38);
        gamePanel.monster[i++] = createEntity(MON_GreenSlime.class, 34, 42);
        gamePanel.monster[i++] = createEntity(MON_GreenSlime.class, 38, 42);
    }

    public void setInteractiveTile() {
        int i = 0;
        // Using the special interactive tile factory
        gamePanel.iTile[i++] = createInteractiveTile(IT_DryTree.class,27, 12);
        gamePanel.iTile[i++] = createInteractiveTile(IT_DryTree.class,28, 12);
        gamePanel.iTile[i++] = createInteractiveTile(IT_DryTree.class,29, 12);
        gamePanel.iTile[i++] = createInteractiveTile(IT_DryTree.class,31, 21);
        gamePanel.iTile[i++] = createInteractiveTile(IT_DryTree.class,31, 12);
        gamePanel.iTile[i++] = createInteractiveTile(IT_DryTree.class,32, 12);
        gamePanel.iTile[i++] = createInteractiveTile(IT_DryTree.class,33, 12);
    }
}