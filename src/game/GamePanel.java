package game;

import ai.PathFinder;
import data.DataManager;
import entity.Entity;
import entity.Player;
import environment.EnvironmentManager;
import interactive_tile.IT_DryTree;
import interactive_tile.InteractiveTile;
import tiles.Map;
import tiles.TileManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GamePanel extends JPanel implements Runnable{

    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3;
    int FPS = 60;
    public int fpsCount = 0;

    public final int tileSize = originalTileSize * scale; //48x48 tile
    public final int maxScreenCol = 20;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; //960 pixels
    public final int screenHeight = tileSize * maxScreenRow;  // 576 pixels

    //WORLD SETTINGS
    public int maxWorldCol;
    public int maxWorldRow;
    public final int maxMap = 10;
    public int currentMap = 0;

    //FULL SCREEN
    int screenWidth2 = screenWidth;
    int screenHeight2 = screenHeight;
    BufferedImage tempScreen;
    Graphics2D graphics2D;
    public  boolean fullScreenOn = false;

    //SYSTEM
    Thread gameThread;
    public KeyHandler keyHandler = new KeyHandler(this);
    public TileManager tileManager = new TileManager(this);
    public AssetSetter assetSetter = new AssetSetter(this);
    public CollisionChecker checker = new CollisionChecker(this);
    public Sound sound = Sound.getInstance();
    public UI ui = new UI(this);
    public EventHandler eventHandler =  new EventHandler(this);
    public Config config = new Config(this);
    public PathFinder pathFinder = new PathFinder(this);
    EnvironmentManager manager = new EnvironmentManager(this);
    Map map = new Map(this);
    public DataManager dataManager = new DataManager(this);

    //ENTITY AND OBJECT
    public Player player = new Player(this, keyHandler);
    public Entity[][] object= new Entity[maxMap][20];
    public Entity[][] npc = new Entity[maxMap][10];
    public Entity[][] monster = new Entity[maxMap][20];
    public InteractiveTile[][] iTile = new InteractiveTile[maxMap][50];
    ArrayList<Entity> entityList = new ArrayList<>();
    public Entity[][] projectile = new Entity[maxMap][20];
    public ArrayList<Entity> particleList = new ArrayList<>();

    //GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int characterState = 4;
    public final int optionsState = 5;
    public final int gameOverState = 6;
    public final int transitionState = 7;
    public final int tradeState = 8;
    public final int sleepState = 9;
    public final int mapState = 10;


    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    public void setupGame() {
        // Common setup
        assetSetter.setNPC();
        assetSetter.setMonster();
        manager.setup();

        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        graphics2D = (Graphics2D) tempScreen.getGraphics();
        if (fullScreenOn) {
            setFullScreen();
        }

        // Set default objects/tiles
        assetSetter.setObject();
        assetSetter.setInteractiveTile();
        // Load saved state (this will apply saved changes or use defaults if no save exists)
        dataManager.loadWorldObjectState();
        gameState = titleState;
    }

    public void resetGame(boolean restart) {
        player.setDefaultPositions();
        player.restoreStatus();
        assetSetter.setNPC();
        assetSetter.setMonster();
        manager.lighting.resetDay();

        if (restart) {
            // For new game - reset everything to defaults
            player.setDefaultValues();
            dataManager.resetPlayerData(player);
            assetSetter.setObject();
            assetSetter.setInteractiveTile();
            dataManager.saveWorldObjectState(); // Save fresh defaults
        } else {
            // For loading a saved game - load player data and world state
            dataManager.loadPlayerStats(player);
            assetSetter.setObject();
            assetSetter.setInteractiveTile();
            dataManager.loadWorldObjectState(); // Apply saved changes
        }
    }

    public void setFullScreen() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        Main.window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        screenWidth2 = (int) width;
        screenHeight2 = (int) height;
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double drawInterval = 1_000_000_000 / (double)FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;
        while(gameThread != null){

            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);

            lastTime = currentTime;

            if(delta >= 1){
                update();
                drawToTempScreen();
                drawToScreen();
                delta--;
                drawCount += 1;
            }
            if(timer >= 1_000_000_000){
                System.out.println("FPS: " + drawCount);
                fpsCount = drawCount;
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update(){
        if(gameState == playState) {
            //PLAYER
            player.update();
            //NPC
            for (int i = 0; i < npc[1].length; ++i) {
                if (npc[currentMap][i] != null) {
                    npc[currentMap][i].update();
                }
            }

            for (int i = 0; i < monster[1].length; i++) {
                if (monster[currentMap][i] != null) {
                    if (monster[currentMap][i].alive && !monster[currentMap][i].dying) {
                        monster[currentMap][i].update();
                    }
                    if (!monster[currentMap][i].alive) {
                        monster[currentMap][i].checkDrop();
                        monster[currentMap][i] = null;
                    }
                }
            }
            for (int i = 0; i < projectile[1].length; i++) {
                if (projectile[currentMap][i] != null) {
                    if (projectile[currentMap][i].alive) {
                        projectile[currentMap][i].update();
                    }
                    else if(!projectile[currentMap][i].alive) {
                        projectile[currentMap][i] = null;
                    }
                }
            }
            for (int i = 0; i < particleList.size(); i++) {
                if (particleList.get(i) != null) {
                    if (particleList.get(i).alive) {
                        particleList.get(i).update();
                    }
                    else {
                        particleList.remove(i);
                        i--;
                    }
                }
            }

            for (int i = 0; i < iTile[1].length; i++) {
                if (iTile[currentMap][i] != null) {
                    iTile[currentMap][i].update();
                    //SAVE TILE STATE IF A DRY TREE IS DESTROYED
                    if(iTile[currentMap][i] instanceof IT_DryTree dryTree && dryTree.life <= 0) {
                        dataManager.saveWorldObjectState();
                    }
                }
            }

            if(gameState == pauseState) {

            }
        }
    }

    public void drawToTempScreen() {

        if(gameState == titleState) {
            graphics2D.setColor(Color.BLACK);
            graphics2D.fillRect(0, 0, screenWidth, screenHeight);
            ui.draw(graphics2D);
        } //MAP SCREEN
        else if(gameState == mapState) {
            map.drawFullMapScreen(graphics2D);
        }
        else {

            //TILE
            tileManager.draw(graphics2D);
            eventHandler.drawEventDebug(graphics2D);
            for (int i = 0; i < iTile[1].length; ++i) {
                if (iTile[currentMap][i] != null) {
                    iTile[currentMap][i].draw(graphics2D);
                }
            }


            //ADD PLAYER
            entityList.add(player);

            //ADD NPCs
            for (int i = 0; i < npc[1].length; ++i) {
                if (npc[currentMap][i] != null) {
                    entityList.add(npc[currentMap][i]);
                }
            }
            //ADD OBJECTS
            for (int i = 0; i < object[1].length; ++i) {
                if (object[currentMap][i] != null) {
                    entityList.add(object[currentMap][i]);
                }
            }

            //ADD MONSTERS
            for (int i = 0; i < monster[1].length; ++i) {
                if (monster[currentMap][i] != null) {
                    entityList.add(monster[currentMap][i]);
                }
            }

            //ADD PROJECTILES
            for(int i = 0; i < projectile[1].length; ++i) {
                if(projectile[currentMap][i] != null) {
                    entityList.add(projectile[currentMap][i]);
                }
            }
            for (Entity entity : particleList) {
                if (entity != null) {
                    entityList.add(entity);
                }
            }



            //SORT
            entityList.sort(new Comparator<Entity>() {
                @Override
                public int compare(Entity e1, Entity e2) {

                    return Integer.compare(e1.worldY, e2.worldY);
                }
            });

            //DRAW ENTITIES
            for(int i = 0; i < entityList.size(); ++i) {
                entityList.get(i).draw(graphics2D);
            }

            //EMPTY ENTITY LIST
            entityList.clear();

            //ENVIRONMENT
            manager.draw(graphics2D);
            manager.update();

            //MINIMAP
            map.drawMiniMapScreen(graphics2D);
            //UI
            ui.draw(graphics2D);

            //DEBUG
            if(keyHandler.showDebugText) {
                graphics2D.setFont(new Font("Arial", Font.PLAIN, 20));
                graphics2D.setColor(Color.WHITE);
                int x = 10;
                int y = 400;
                int lineHeight = 20;
                int col = (player.worldX + player.solidArea.x) / tileSize;
                int row = (player.worldY + player.solidArea.y) / tileSize;
                graphics2D.drawString("WorldX: " + player.worldX, x, y); y += lineHeight;
                graphics2D.drawString("WorldY: " + player.worldY, x, y); y += lineHeight;
                graphics2D.drawString("Col: " + col, x, y + 15);y += lineHeight;
                graphics2D.drawString("Row: " + row, x, y + 20);
            }
        }
    }

    public void drawToScreen() {
        Graphics2D g = (Graphics2D) getGraphics();
        g.drawImage(tempScreen, 0, 0, screenWidth2,screenHeight2, null);
        g.dispose();
    }


    public void playSE(int i) {
        sound.playSoundEffect(i);
    }
}