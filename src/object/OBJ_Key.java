package object;

import entity.Entity;
import game.GamePanel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class OBJ_Key extends Entity {

    GamePanel gamePanel;

    public OBJ_Key(GamePanel gamePanel) {
        super(gamePanel);
        this.gamePanel = gamePanel;
        name = "Key";
        down1 = setup("items/key", 1, 1);
        description = "[" + name + "]\nIt opens chests or doors.";
        price = 50;
        type = type_consumable;
        stackable = true;

        setDialogue();
    }
    public void setDialogue() {
        dialogues[0][0] = "You use the " + name + " and open the door.";
        dialogues[1][0] = "Are you ok?";
    }


    public boolean use(Entity entity) {

        int objIndex = getDetected(entity, gamePanel.object, "Door");
        if (objIndex != 999) {
            startDialogue(this, 0);
            gamePanel.sound.playSoundEffect(3);
            // Save the door as removed in the database
            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:game_save.db")) {
                String insertObject = "INSERT OR REPLACE INTO world_objects (map_id, col, row, type, state) VALUES (?, ?, ?, ?, ?);";
                PreparedStatement stmt = connection.prepareStatement(insertObject);
                stmt.setInt(1, gamePanel.currentMap);
                stmt.setInt(2, gamePanel.object[gamePanel.currentMap][objIndex].worldX / gamePanel.tileSize);
                stmt.setInt(3, gamePanel.object[gamePanel.currentMap][objIndex].worldY / gamePanel.tileSize);
                stmt.setString(4, "Door");
                stmt.setString(5, "removed");
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            gamePanel.object[gamePanel.currentMap][objIndex] = null;
            return true;
        } else {
            startDialogue(this, 1);
            return false;
        }
    }
}
