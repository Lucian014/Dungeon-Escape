package game;

import entity.Entity;
import entity.Player;
import interactive_tile.InteractiveTile;
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
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;

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
    Sound se = new Sound();
    Sound music = new Sound();
    public UI ui = new UI(this);
    public EventHandler eventHandler =  new EventHandler(this);

    //ENTITY AND OBJECT
    public Player player = new Player(this, keyHandler);
    public Entity[] object= new Entity[20];
    public Entity[] npc = new Entity[10];
    public Entity[] monster = new Entity[20];
    ArrayList<Entity> entityList = new ArrayList<>();
    public ArrayList<Entity> projectileList = new ArrayList<>();
    public ArrayList<Entity> particleList = new ArrayList<>();
    public InteractiveTile[] iTile = new InteractiveTile[50];
    //GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int characterState = 4;
    public final int optionsState = 5;

    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);

    }
    public void setupGame() {
        assetSetter.setNPC();
        assetSetter.setMonster();
        assetSetter.setObject();
        assetSetter.setInteractiveTile();
        playMusic(0);
        stopMusic();
        gameState = titleState;

        tempScreen = new BufferedImage(screenWidth,screenHeight,BufferedImage.TYPE_INT_ARGB);
        graphics2D = (Graphics2D) tempScreen.getGraphics();

       // setFullScreen();
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
            for (Entity entity : npc) {
                if (entity != null) {
                    entity.update();
                }
            }

        for (int i = 0; i < monster.length; i++) {
            if (monster[i] != null) {
                if (monster[i].alive && !monster[i].dying) {
                    monster[i].update();
                }
                if (!monster[i].alive) {
                    monster[i].checkDrop();
                    monster[i] = null;
                }
            }
        }
        for (int i = 0; i < projectileList.size(); i++) {
            if (projectileList.get(i) != null) {
                if (projectileList.get(i).alive) {
                    projectileList.get(i).update();
                }
                else {
                    projectileList.remove(i);
                    i--;
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

        for (InteractiveTile interactiveTile : iTile) {
            if (interactiveTile != null) {
                interactiveTile.update();
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
        } else {

            //TILE
            tileManager.draw(graphics2D);

            for (InteractiveTile interactiveTile : iTile) {
                if (interactiveTile != null) {
                    interactiveTile.draw(graphics2D);
                }
            }

            //ADD PLAYER
            entityList.add(player);

            //ADD NPCs
            for (Entity entity : npc) {
                if (entity != null) {
                    entityList.add(entity);
                }
            }

            //ADD OBJECTS
            for (Entity entity : object) {
                if (entity != null) {
                    entityList.add(entity);
                }
            }

            //ADD MONSTERS
            for (Entity entity : monster) {
                if (entity != null) {
                    entityList.add(entity);
                }
            }

            //ADD PROJECTILES
            for (Entity entity : projectileList) {
                if (entity != null) {
                    entityList.add(entity);
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
    public void playMusic(int i) {

        music.setFile(i);
        music.play();
        music.loop();
    }
    public void stopMusic() {
        music.stop();
    }
    public void playSE(int i) {

        se.setFile(i);
        se.play();
    }

}
