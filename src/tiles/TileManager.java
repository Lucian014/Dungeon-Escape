package tiles;

import game.GamePanel;
import game.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TileManager {

    GamePanel gamePanel;
    public Tile[] tile;
    public int[][][] mapTileNum;
    boolean drawPath = true;
    ArrayList<String> fileNames = new ArrayList<>();
    ArrayList<String> collisionStatus = new ArrayList<>();

    public TileManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        //READ TILE DATA FILE
        InputStream inputStream = getClass().getResourceAsStream("/maps/tiledata.txt");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        //GETTING TILE NAMES AND COLLISION INFO FROM THE FILE
        String line;
        try{
            while((line = bufferedReader.readLine()) != null){
                fileNames.add(line);
                collisionStatus.add(bufferedReader.readLine());
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //INITIALIZE TILE ARRAY BASED ON fileNames SIZE
        tile = new Tile[fileNames.size()];
        getTileImage();

        //GETTING MAX WORLD COL AND ROW FROM THE MAP FILE
        inputStream = getClass().getResourceAsStream("/maps/map.txt");
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String line2 = bufferedReader.readLine();
            String[] maxTile = line2.split(" ");
            gamePanel.maxWorldCol = maxTile.length;
            gamePanel.maxWorldRow = maxTile.length;
            mapTileNum = new int[gamePanel.maxMap][gamePanel.maxWorldCol][gamePanel.maxWorldRow];
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        loadMap("/maps/map.txt",0);
        loadMap("/maps/interior.txt",1);
        loadMap("/maps/dungeon01.txt",2);
        loadMap("/maps/dungeon02.txt",3);
        loadMap("/maps/map02.txt",4);
        loadMap("/maps/interior.txt",5);

    }
    public void getTileImage(){

        for(int i = 0; i < fileNames.size(); i++){
            String fileName;
            boolean collision;

            //GETTING FILE NAME
            fileName = fileNames.get(i);
            //GETTING COLLISION STATUS

            if(collisionStatus.get(i).equals("true")) {
                collision = true;
            }else {
                collision = false;
            }

            setup(i, fileName, collision);
        }
    }

    public void setup(int index, String imagePath, boolean collision){
        UtilityTool utilityTool = new UtilityTool();

        try{
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imagePath));
            tile[index].image = utilityTool.scaleImage(tile[index].image, gamePanel.tileSize, gamePanel.tileSize);
            tile[index].collision = collision;
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String filePath, int map){
        try{
            InputStream inputStream = getClass().getResourceAsStream(filePath);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            int col = 0;
            int row = 0;

            while(col < gamePanel.maxWorldCol && row < gamePanel.maxWorldRow) {
                String line = bufferedReader.readLine();

                while(col < gamePanel.maxWorldCol){

                    String[] numbers = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[map][col][row] = num;
                    col++;
                }
                if(col == gamePanel.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
            bufferedReader.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2){

        int worldCol = 0;
        int worldRow = 0;
        while(worldCol < gamePanel.maxWorldCol && worldRow < gamePanel.maxWorldRow){

            int tileNum = mapTileNum[gamePanel.currentMap][worldCol][worldRow];

            int worldX = worldCol * gamePanel.tileSize;
            int worldY = worldRow * gamePanel.tileSize;
            int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
            int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;

            if(worldX + gamePanel.tileSize> gamePanel.player.worldX - gamePanel.player.screenX  &&
                    worldX - gamePanel.tileSize< gamePanel.player.worldX + gamePanel.player.screenX  &&
                    worldY + gamePanel.tileSize> gamePanel.player.worldY - gamePanel.player.screenY  &&
                    worldY - gamePanel.tileSize< gamePanel.player.worldY + gamePanel.player.screenY ){

                    g2.drawImage(tile[tileNum].image, screenX, screenY, null);
            }
            worldCol++;

            if(worldCol == gamePanel.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
}
