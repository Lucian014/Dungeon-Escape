package game;

import entity.Entity;
import entity.NPC_BigRock;
import entity.NPC_Priest;
import entity.NPC_Merchant;
import interactive_tile.IT_DestructibleWall;
import interactive_tile.IT_DryTree;
import interactive_tile.IT_MetalPlate;
import interactive_tile.InteractiveTile;
import monster.MON_Bat;
import monster.MON_GreenSlime;
import monster.MON_Orc;
import monster.MON_SkeletonLord;
import object.*;

import java.lang.reflect.Constructor;

public class AssetSetter {

    GamePanel gamePanel;

    public AssetSetter(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }

    private <T extends Entity> T createEntity(Class<T> entityClass, int worldX, int worldY, Object... params) {
        try {
            // Handle OBJ_Chest specifically
            if (entityClass == OBJ_Chest.class && params.length > 0 && params[0] instanceof Entity) {
                OBJ_Chest chest = new OBJ_Chest(gamePanel, (Entity) params[0]);
                chest.worldX = worldX * gamePanel.tileSize;
                chest.worldY = worldY * gamePanel.tileSize;
                return entityClass.cast(chest);
            }

            // For other entities with parameters
            if (params.length > 0) {
                Class<?>[] paramTypes = new Class<?>[params.length + 1];
                paramTypes[0] = GamePanel.class;
                for (int i = 0; i < params.length; i++) {
                    paramTypes[i + 1] = params[i].getClass();
                }

                Constructor<T> constructor = entityClass.getConstructor(paramTypes);

                Object[] allParams = new Object[params.length + 1];
                allParams[0] = gamePanel;
                System.arraycopy(params, 0, allParams, 1, params.length);

                T entity = constructor.newInstance(allParams);
                entity.worldX = worldX * gamePanel.tileSize;
                entity.worldY = worldY * gamePanel.tileSize;
                return entity;
            }

            // For entities without parameters
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
        int mapNum = 0;
        gamePanel.object[mapNum][i++] = createEntity(OBJ_Coin_Bronze.class, 25, 23);
        gamePanel.object[mapNum][i++] = createEntity(OBJ_Coin_Bronze.class, 21, 19);
        gamePanel.object[mapNum][i++] = createEntity(OBJ_Coin_Bronze.class, 26, 21);
        gamePanel.object[mapNum][i++] = createEntity(OBJ_Axe.class, 33, 21);
        gamePanel.object[mapNum][i++] = createEntity(OBJ_Shield_Blue.class, 35, 21);
        gamePanel.object[mapNum][i++] = createEntity(OBJ_Potion_Red.class, 37, 21);
        gamePanel.object[mapNum][i++] = createEntity(OBJ_Potion_Red.class, 38, 21);
        gamePanel.object[mapNum][i++] = createEntity(OBJ_Heart.class,22,29);
        gamePanel.object[mapNum][i++] = createEntity(OBJ_ManaCrystal.class,22,31);
        gamePanel.object[mapNum][i++] = createEntity(OBJ_Door.class,14,28);
        gamePanel.object[mapNum][i++] = createEntity(OBJ_Door.class,12,12);
        gamePanel.object[mapNum][i++] = createEntity(OBJ_Chest.class,30,12, new OBJ_Key(gamePanel));
        gamePanel.object[mapNum][i++] = createEntity(OBJ_Lantern.class,18,20);
        gamePanel.object[mapNum][i++] = createEntity(OBJ_Chest.class,30,29,new OBJ_Shield_Blue(gamePanel));

        mapNum += 2;
        i = 0;
        gamePanel.object[mapNum][i++] = createEntity(OBJ_Chest.class,40,41, new OBJ_Pickaxe(gamePanel));
        gamePanel.object[mapNum][i++] = createEntity(OBJ_Chest.class,13,16, new OBJ_Potion_Red(gamePanel));
        gamePanel.object[mapNum][i++] = createEntity(OBJ_Chest.class,26,34, new OBJ_Potion_Red(gamePanel));
        gamePanel.object[mapNum][i++] = createEntity(OBJ_Chest.class,27,15, new OBJ_Potion_Red(gamePanel));
        gamePanel.object[mapNum][i++] = createEntity(OBJ_Door_Iron.class,18,23);

        mapNum = 3;
        i = 0;
        gamePanel.object[mapNum][i++] = createEntity(OBJ_Door_Iron.class,25,15);

    }

    public void setNPC() {
        int mapNum = 0;
        int i = 0;
        //MAP 1 NPCs
        gamePanel.npc[mapNum][i] = createEntity(NPC_Priest.class, 17, 20);

        //MAP 2 NPCS
        mapNum = 1;
        gamePanel.npc[mapNum][i] = createEntity(NPC_Merchant.class, 12, 7);

        mapNum = 2;
        gamePanel.npc[mapNum][i++] = createEntity(NPC_BigRock.class, 20, 25);
        gamePanel.npc[mapNum][i++] = createEntity(NPC_BigRock.class, 11, 18);
        gamePanel.npc[mapNum][i++] = createEntity(NPC_BigRock.class, 23, 14);



    }

    public void setMonster() {
        int i = 0;
        int mapNum = 0;
        gamePanel.monster[mapNum][i++] = createEntity(MON_GreenSlime.class, 23, 16);
        gamePanel.monster[mapNum][i++] = createEntity(MON_GreenSlime.class, 24, 37);
        gamePanel.monster[mapNum][i++] = createEntity(MON_GreenSlime.class, 21, 38);
        gamePanel.monster[mapNum][i++] = createEntity(MON_GreenSlime.class, 34, 42);
        gamePanel.monster[mapNum][i++] = createEntity(MON_GreenSlime.class, 38, 42);
        gamePanel.monster[mapNum][i++] = createEntity(MON_Orc.class, 12, 33);

        i = 0;
        mapNum += 2;

        gamePanel.monster[mapNum][i++] = createEntity(MON_Bat.class,34,39);
        gamePanel.monster[mapNum][i++] = createEntity(MON_Bat.class,36,25);
        gamePanel.monster[mapNum][i++] = createEntity(MON_Bat.class,39,26);
        gamePanel.monster[mapNum][i++] = createEntity(MON_Bat.class,28,11);
        gamePanel.monster[mapNum][i++] = createEntity(MON_Bat.class,10,19);

        mapNum = 3;
        i = 0;
        gamePanel.monster[mapNum][i++] = createEntity(MON_SkeletonLord.class,23,15);

    }

    public void setInteractiveTile() {
        int i = 0;
        int mapNum = 0;

        gamePanel.iTile[mapNum][i++] = createInteractiveTile(IT_DryTree.class,27, 12);
        gamePanel.iTile[mapNum][i++] = createInteractiveTile(IT_DryTree.class,28, 12);
        gamePanel.iTile[mapNum][i++] = createInteractiveTile(IT_DryTree.class,29, 12);
        gamePanel.iTile[mapNum][i++] = createInteractiveTile(IT_DryTree.class,31, 21);
        gamePanel.iTile[mapNum][i++] = createInteractiveTile(IT_DryTree.class,31, 12);
        gamePanel.iTile[mapNum][i++] = createInteractiveTile(IT_DryTree.class,32, 12);
        gamePanel.iTile[mapNum][i++] = createInteractiveTile(IT_DryTree.class,33, 12);
        gamePanel.iTile[mapNum][i++] = createInteractiveTile(IT_DryTree.class,25, 32);
        gamePanel.iTile[mapNum][i++] = createInteractiveTile(IT_DryTree.class,26, 32);
        gamePanel.iTile[mapNum][i++] = createInteractiveTile(IT_DryTree.class,27, 32);
        gamePanel.iTile[mapNum][i++] = createInteractiveTile(IT_DryTree.class,27, 31);

        mapNum += 2;
        i = 0;

        gamePanel.iTile[mapNum][i++] = createInteractiveTile(IT_DestructibleWall.class,18, 30);
        gamePanel.iTile[mapNum][i++] = createInteractiveTile(IT_DestructibleWall.class,17, 31);
        gamePanel.iTile[mapNum][i++] = createInteractiveTile(IT_DestructibleWall.class,17, 32);
        gamePanel.iTile[mapNum][i++] = createInteractiveTile(IT_DestructibleWall.class,17, 34);
        gamePanel.iTile[mapNum][i++] = createInteractiveTile(IT_DestructibleWall.class,18, 33);
        gamePanel.iTile[mapNum][i++] = createInteractiveTile(IT_DestructibleWall.class,10, 22);
        gamePanel.iTile[mapNum][i++] = createInteractiveTile(IT_DestructibleWall.class,10, 24);
        gamePanel.iTile[mapNum][i++] = createInteractiveTile(IT_DestructibleWall.class,38, 18);
        gamePanel.iTile[mapNum][i++] = createInteractiveTile(IT_DestructibleWall.class,38, 19);
        gamePanel.iTile[mapNum][i++] = createInteractiveTile(IT_DestructibleWall.class,38, 20);
        gamePanel.iTile[mapNum][i++] = createInteractiveTile(IT_DestructibleWall.class,38, 21);
        gamePanel.iTile[mapNum][i++] = createInteractiveTile(IT_DestructibleWall.class,18, 13);
        gamePanel.iTile[mapNum][i++] = createInteractiveTile(IT_DestructibleWall.class,18, 14);
        gamePanel.iTile[mapNum][i++] = createInteractiveTile(IT_DestructibleWall.class,22, 28);
        gamePanel.iTile[mapNum][i++] = createInteractiveTile(IT_DestructibleWall.class,30, 28);
        gamePanel.iTile[mapNum][i++] = createInteractiveTile(IT_DestructibleWall.class,32, 28);

        gamePanel.iTile[mapNum][i++] = createInteractiveTile(IT_MetalPlate.class,20, 22);
        gamePanel.iTile[mapNum][i++] = createInteractiveTile(IT_MetalPlate.class,8, 17);
        gamePanel.iTile[mapNum][i++] = createInteractiveTile(IT_MetalPlate.class,39, 31);


    }
}