package game;

import entity.Entity;
import entity.Player;
import tiles.TileManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GamePanel extends JPanel implements Runnable{

    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3;
    int FPS = 60;

    public final int tileSize = originalTileSize * scale; //48x48 tile
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; //768 pixels
    public final int screenHeight = tileSize * maxScreenRow;  // 576 pixels

    //WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;

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
    public Entity[] object= new Entity[10];
    public Entity[] npc = new Entity[10];
    public Entity[] monster = new Entity[20];
    ArrayList<Entity> entityList = new ArrayList<>();

    //GAME STATE
    public int gameState;
    public final int titleState = 0;
    public int playState = 1;
    public int pauseState = 2;
    public int dialogueState = 3;

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
        playMusic(0);
        stopMusic();
        gameState = titleState;
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
                repaint();
                delta--;
                drawCount += 1;
            }
            if(timer >= 1_000_000_000){
                System.out.println("FPS: " + drawCount);
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
        }

        for (int i = 0; i < monster.length; i++) {
            if (monster[i] != null) {
                if (monster[i].alive && !monster[i].dying) {
                    monster[i].update();
                }
                if (!monster[i].alive) {
                    monster[i] = null;
                }
            }
        }

        if(gameState == pauseState) {

        }
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if(gameState == titleState) {
            ui.draw(g2);
        } else {

            //TILE
            tileManager.draw(g2);

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

            for (Entity entity : monster) {
                if (entity != null) {
                    entityList.add(entity);
                }
            }


            //SORT
            Collections.sort(entityList, new Comparator<Entity>() {
                @Override
                public int compare(Entity e1, Entity e2) {

                    return Integer.compare(e1.worldY, e2.worldY);
                }
            });

            //DRAW ENTITIES
            for(int i = 0; i < entityList.size(); ++i) {
                entityList.get(i).draw(g2);
            }

            //EMPTY ENTITY LIST
            entityList.clear();
            //UI
            ui.draw(g2);


        }
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
