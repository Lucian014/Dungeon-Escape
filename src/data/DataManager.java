package data;

import entity.Entity;
import entity.Player;
import game.GamePanel;
import object.*;
import org.sqlite.core.DB;

import java.sql.*;

public class DataManager {

    private static final String DB_URL = "jdbc:sqlite:game_save.db";
    public GamePanel gamePanel;

    public DataManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        createTables();
    }

    private void createTables() {
        try(Connection connection = DriverManager.getConnection(DB_URL)) {
            //PLAYER STATS TABLE

            String playerStats = "CREATE TABLE IF NOT EXISTS player(" +
                    "id INTEGER PRIMARY KEY," +
                    "worldX INTEGER," +
                    "worldY INTEGER," +
                    "direction TEXT," +
                    "speed INTEGER," +
                    "defaultSpeed INTEGER," +
                    "level INTEGER," +
                    "strength INTEGER," +
                    "dexterity INTEGER," +
                    "exp INTEGER," +
                    "nextLevelExp INTEGER," +
                    "coin INTEGER," +
                    "maxLife INTEGER," +
                    "life INTEGER," +
                    "maxMana INTEGER," +
                    "mana INTEGER," +
                    "currentWeaponName TEXT," +
                    "currentShieldName TEXT," +
                    "currentLightName TEXT" +
                    ");";
            Statement statement = connection.createStatement();
            statement.execute(playerStats);

            String inventory = "CREATE TABLE IF NOT EXISTS inventory (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "player_id INTEGER," +
                    "item_name TEXT," +
                    "amount INTEGER," +
                    "item_type INTEGER," +
                    "FOREIGN KEY(player_id) REFERENCES player(id)" +
                    ");";
            statement.execute(inventory);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void savePlayerStats(Player player) {

        try(Connection connection = DriverManager.getConnection(DB_URL)) {
            // SAVE OR UPDATE PLAYER STATS
            String query = "INSERT OR REPLACE INTO player (id, worldX, worldY, direction, speed, defaultSpeed, level, " +
                    "strength, dexterity, exp, nextLevelExp, coin, maxLife, life, maxMana, mana, " +
                    "currentWeaponName, currentShieldName, currentLightName) " +
                    "VALUES (1, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, player.worldX);
            preparedStatement.setInt(2, player.worldY);
            preparedStatement.setString(3, player.direction);
            preparedStatement.setInt(4, player.speed);
            preparedStatement.setInt(5, player.defaultSpeed);
            preparedStatement.setInt(6, player.level);
            preparedStatement.setInt(7, player.strength);
            preparedStatement.setInt(8, player.dexterity);
            preparedStatement.setInt(9, player.exp);
            preparedStatement.setInt(10, player.nextLevelExp);
            preparedStatement.setInt(11, player.coin);
            preparedStatement.setInt(12, player.maxLife);
            preparedStatement.setInt(13, player.life);
            preparedStatement.setInt(14, player.maxMana);
            preparedStatement.setInt(15, player.mana);
            preparedStatement.setString(16, player.currentWeapon != null ? player.currentWeapon.name : null);
            preparedStatement.setString(17, player.currentShield != null ? player.currentShield.name : null);
            preparedStatement.setString(18, player.currentLight != null ? player.currentLight.name : null);
            preparedStatement.executeUpdate();

            // DELETE OLD INVENTORY
            String deleteInv = "DELETE FROM inventory WHERE player_id = 1;";
            Statement stmt = connection.createStatement();
            stmt.execute(deleteInv);

            // SAVE INVENTORY
            String insertInv = "INSERT INTO inventory (player_id, item_name, amount, item_type) VALUES (1, ?, ?, ?);";
            PreparedStatement invSQL = connection.prepareStatement(insertInv);
            for (Entity item : player.inventory) {
                if (item != null) {
                    invSQL.setString(1, item.name);
                    invSQL.setInt(2, item.amount);
                    invSQL.setInt(3, item.type);
                    invSQL.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Factory method to recreate items by name (extend this with all possible items in your game)
    private Entity getItemByName(String name) {
        Entity item = null;
        switch (name) {
            case "Normal Sword":
                item = new OBJ_Sword_Normal(gamePanel);
                break;
            case "Wood Shield":
                item = new OBJ_Shield_Wood(gamePanel);
                break;
            case "Key":
                item = new OBJ_Key(gamePanel);
                break;
            case "Woodcutter's Axe":
                item = new OBJ_Axe(gamePanel);
                break;
            case "Red Potion":
                item = new OBJ_Potion_Red(gamePanel);
                break;
            case "Lantern":
                item = new OBJ_Lantern(gamePanel);
                break;
            // Add cases for other items as needed (e.g., potions, lights, etc.)
            default:
                System.out.println("Unknown item name: " + name + ". Item not loaded.");
        }
        return item;
    }

    public void loadPlayerStats(Player player) {
        try(Connection connection = DriverManager.getConnection(DB_URL)) {
            // LOAD PLAYER STATS
            String query = "SELECT * FROM player WHERE id = 1;";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if(resultSet.next()) {
                player.worldX = resultSet.getInt("worldX");
                player.worldY = resultSet.getInt("worldY");
                player.direction = resultSet.getString("direction");
                player.speed = resultSet.getInt("speed");
                player.defaultSpeed = resultSet.getInt("defaultSpeed");
                player.level = resultSet.getInt("level");
                player.strength = resultSet.getInt("strength");
                player.dexterity = resultSet.getInt("dexterity");
                player.exp = resultSet.getInt("exp");
                player.nextLevelExp = resultSet.getInt("nextLevelExp");
                player.coin = resultSet.getInt("coin");
                player.maxLife = resultSet.getInt("maxLife");
                player.life = resultSet.getInt("life");
                player.maxMana = resultSet.getInt("maxMana");
                player.mana = resultSet.getInt("mana");

                String weaponName = resultSet.getString("currentWeaponName");
                String shieldName = resultSet.getString("currentShieldName");
                String lightName = resultSet.getString("currentLightName");

                // Load inventory
                player.inventory.clear();
                String invQuery = "SELECT * FROM inventory WHERE player_id = 1;";
                ResultSet invResultSet = statement.executeQuery(invQuery);
                while (invResultSet.next()) {
                    String itemName = invResultSet.getString("item_name");
                    int amount = invResultSet.getInt("amount");
                    int type = invResultSet.getInt("item_type");
                    Entity item = getItemByName(itemName);
                    if(item != null) {
                        item.amount = amount;
                        item.type = type;  // Restore type if your Entity uses it
                        player.inventory.add(item);

                        // Set equipped if name matches
                        if (weaponName != null && itemName.equals(weaponName)) {
                            player.currentWeapon = item;
                        }
                        if (shieldName != null && itemName.equals(shieldName)) {
                            player.currentShield = item;
                        }
                        if (lightName != null && itemName.equals(lightName)) {
                            player.currentLight = item;
                        }
                    }
                }

                // If equipped not found in inventory, create new and add
                if (player.currentWeapon == null && weaponName != null) {
                    player.currentWeapon = getItemByName(weaponName);
                    if (player.currentWeapon != null) {
                        player.inventory.add(player.currentWeapon);
                    }
                }
                if (player.currentShield == null && shieldName != null) {
                    player.currentShield = getItemByName(shieldName);
                    if (player.currentShield != null) {
                        player.inventory.add(player.currentShield);
                    }
                }
                if (player.currentLight == null && lightName != null) {
                    player.currentLight = getItemByName(lightName);
                    if (player.currentLight != null) {
                        player.inventory.add(player.currentLight);
                    }
                }
            } else {
                // No save found, fallback to defaults
                player.setDefaultValues();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            player.setDefaultValues();  // Fallback on error
        }
    }

    public void resetPlayerData(Player player) {
        try(Connection connection = DriverManager.getConnection(DB_URL)) {
            //DELETE EXISTING PLAYER DATA
            String deletePlayer = "DELETE FROM player WHERE id = 1;";
            Statement statement = connection.createStatement();
            statement.execute(deletePlayer);

            //Delete existing inventory data
            String deleteInventory = "DELETE FROM inventory WHERE player_id = 1;";
            statement.execute(deleteInventory);

            //SAVE DEFAULT PLAYER STATE
            savePlayerStats(player);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
