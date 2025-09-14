package data;

import entity.Entity;
import entity.Player;
import game.GamePanel;
import interactive_tile.*;
import object.*;

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
            // PLAYER STATS TABLE
            String playerStats = "CREATE TABLE IF NOT EXISTS player(" +
                    "id INTEGER PRIMARY KEY," +
                    "currentMap INTEGER," +
                    "currentArea INTEGER," +
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

            // INVENTORY TABLE
            String inventory = "CREATE TABLE IF NOT EXISTS inventory (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "player_id INTEGER," +
                    "item_name TEXT," +
                    "amount INTEGER," +
                    "item_type INTEGER," +
                    "FOREIGN KEY(player_id) REFERENCES player(id)" +
                    ");";
            statement.execute(inventory);

            // WORLD OBJECTS TABLE (for interactive tiles and objects like doors)
            String worldObjects = "CREATE TABLE IF NOT EXISTS world_objects (" +
                    "map_id INTEGER," +
                    "col INTEGER," +
                    "row INTEGER," +
                    "type TEXT," +
                    "state TEXT," +
                    "PRIMARY KEY (map_id, col, row)" +
                    ");";
            statement.execute(worldObjects);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void savePlayerStats(Player player) {
        try(Connection connection = DriverManager.getConnection(DB_URL)) {
            // SAVE OR UPDATE PLAYER STATS
            String query = "INSERT OR REPLACE INTO player (id, currentMap, currentArea, worldX, worldY, direction, speed, defaultSpeed, level, " +
                    "strength, dexterity, exp, nextLevelExp, coin, maxLife, life, maxMana, mana, " +
                    "currentWeaponName, currentShieldName, currentLightName) " +
                    "VALUES (1, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, gamePanel.currentMap);
            preparedStatement.setInt(2, gamePanel.currentArea);
            preparedStatement.setInt(3, player.worldX);
            preparedStatement.setInt(4, player.worldY);
            preparedStatement.setString(5, player.direction);
            preparedStatement.setInt(6, player.speed);
            preparedStatement.setInt(7, player.defaultSpeed);
            preparedStatement.setInt(8, player.level);
            preparedStatement.setInt(9, player.strength);
            preparedStatement.setInt(10, player.dexterity);
            preparedStatement.setInt(11, player.exp);
            preparedStatement.setInt(12, player.nextLevelExp);
            preparedStatement.setInt(13, player.coin);
            preparedStatement.setInt(14, player.maxLife);
            preparedStatement.setInt(15, player.life);
            preparedStatement.setInt(16, player.maxMana);
            preparedStatement.setInt(17, player.mana);
            preparedStatement.setString(18, player.currentWeapon != null ? player.currentWeapon.name : null);
            preparedStatement.setString(19, player.currentShield != null ? player.currentShield.name : null);
            preparedStatement.setString(20, player.currentLight != null ? player.currentLight.name : null);
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

    public void loadPlayerStats(Player player) {
        try(Connection connection = DriverManager.getConnection(DB_URL)) {
            // LOAD PLAYER STATS
            String query = "SELECT * FROM player WHERE id = 1;";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if(resultSet.next()) {
                gamePanel.currentMap = resultSet.getInt("currentMap");
                gamePanel.currentArea = resultSet.getInt("currentArea");
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
                        item.type = type;
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
            player.setDefaultValues();
        }
    }

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
            case "Blue Shield":
                item = new OBJ_Shield_Blue(gamePanel);
                break;
            case "Pickaxe":
                item = new OBJ_Pickaxe(gamePanel);
                break;
            case "Sword of Flame":
                item = new OBJ_SwordRed(gamePanel);
                break;
            default:
                System.out.println("Unknown item name: " + name + ". Item not loaded.");
        }
        return item;
    }

    public void resetPlayerData(Player player) {
        try(Connection connection = DriverManager.getConnection(DB_URL)) {
            // DELETE EXISTING PLAYER DATA
            String deletePlayer = "DELETE FROM player WHERE id = 1;";
            Statement statement = connection.createStatement();
            statement.execute(deletePlayer);

            // Delete existing inventory data
            String deleteInventory = "DELETE FROM inventory WHERE player_id = 1;";
            statement.execute(deleteInventory);

            // Delete existing world objects data
            String deleteObjects = "DELETE FROM world_objects;";
            statement.execute(deleteObjects);

            // SAVE DEFAULT PLAYER STATE
            savePlayerStats(player);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveWorldObjectState() {
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            // Clear existing object states for current map
            String deleteObjects = "DELETE FROM world_objects WHERE map_id = ?;";
            PreparedStatement deleteStmt = connection.prepareStatement(deleteObjects);
            deleteStmt.setInt(1, gamePanel.currentMap);
            deleteStmt.executeUpdate();

            // Save interactive tile states (e.g., DryTree)
            String insertObject = "INSERT INTO world_objects (map_id, col, row, type, state) VALUES (?, ?, ?, ?, ?);";
            PreparedStatement insertStmt = connection.prepareStatement(insertObject);
            for (int i = 0; i < gamePanel.iTile[gamePanel.currentMap].length; i++) {
                InteractiveTile tile = gamePanel.iTile[gamePanel.currentMap][i];
                if (tile != null) {
                    String type = tile instanceof IT_DryTree ? "DryTree" : null;
                    String state = null;
                    if (tile instanceof IT_DryTree dryTree) {
                        state = dryTree.life <= 0 ? "destroyed" : "intact";
                    }
                    if (type != null) {
                        insertStmt.setInt(1, gamePanel.currentMap);
                        insertStmt.setInt(2, tile.worldX / gamePanel.tileSize);
                        insertStmt.setInt(3, tile.worldY / gamePanel.tileSize);
                        insertStmt.setString(4, type);
                        insertStmt.setString(5, state);
                        insertStmt.executeUpdate();
                    }
                }
            }

            // Save object states (e.g., Door, Key, etc.)
            for (int i = 0; i < gamePanel.object[gamePanel.currentMap].length; i++) {
                Entity obj = gamePanel.object[gamePanel.currentMap][i];
                if (obj != null) {
                    String type = null;
                    if (obj instanceof OBJ_Door) {
                        type = "Door";
                    } else if (obj instanceof OBJ_Key) {
                        type = "Key";
                    } else if (obj instanceof OBJ_Potion_Red) {
                        type = "Red Potion";
                    } // Add other object types as needed
                    if (type != null) {
                        insertStmt.setInt(1, gamePanel.currentMap);
                        insertStmt.setInt(2, obj.worldX / gamePanel.tileSize);
                        insertStmt.setInt(3, obj.worldY / gamePanel.tileSize);
                        insertStmt.setString(4, type);
                        insertStmt.setString(5, "intact");
                        insertStmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadWorldObjectState() {
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            String query = "SELECT * FROM world_objects WHERE map_id = ?;";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, gamePanel.currentMap);
            ResultSet rs = stmt.executeQuery();

            // Create new arrays for tiles and objects
            InteractiveTile[][] newTiles = new InteractiveTile[gamePanel.maxMap][gamePanel.iTile[0].length];
            Entity[][] newObjects = new Entity[gamePanel.maxMap][gamePanel.object[0].length];

            // Copy existing tiles and objects (preserve defaults from AssetSetter)
            for (int i = 0; i < gamePanel.iTile[gamePanel.currentMap].length; i++) {
                newTiles[gamePanel.currentMap][i] = gamePanel.iTile[gamePanel.currentMap][i];
            }
            for (int i = 0; i < gamePanel.object[gamePanel.currentMap].length; i++) {
                newObjects[gamePanel.currentMap][i] = gamePanel.object[gamePanel.currentMap][i];
            }

            // Process saved states
            while (rs.next()) {
                int col = rs.getInt("col");
                int row = rs.getInt("row");
                String type = rs.getString("type");
                String state = rs.getString("state");

                if ("DryTree".equals(type)) {
                    int index = findTileIndex(col, row);
                    if (index == -1) {
                        index = findEmptyTileSlot();
                        if (index == -1) continue;
                    }
                    InteractiveTile tile = "destroyed".equals(state) ? new IT_Trunk(gamePanel, col, row) : new IT_DryTree(gamePanel, col, row);
                    newTiles[gamePanel.currentMap][index] = tile;
                } else {
                    int index = findObjectIndex(col, row);
                    if ("intact".equals(state)) {
                        if (index == -1) {
                            index = findEmptyObjectSlot();
                            if (index == -1) continue;
                        }
                        Entity obj = null;
                        if ("Door".equals(type)) {
                            obj = new OBJ_Door(gamePanel);
                        } else if ("Key".equals(type)) {
                            obj = new OBJ_Key(gamePanel);
                        } else if ("Red Potion".equals(type)) {
                            obj = new OBJ_Potion_Red(gamePanel);
                        } // Add other object types as needed
                        if (obj != null) {
                            obj.worldX = col * gamePanel.tileSize;
                            obj.worldY = row * gamePanel.tileSize;
                            newObjects[gamePanel.currentMap][index] = obj;
                        }
                    } else if ("removed".equals(state)) {
                        if (index != -1) {
                            newObjects[gamePanel.currentMap][index] = null;
                        }
                    }
                }
            }

            // Update gamePanel arrays
            gamePanel.iTile[gamePanel.currentMap] = newTiles[gamePanel.currentMap];
            gamePanel.object[gamePanel.currentMap] = newObjects[gamePanel.currentMap];
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int findTileIndex(int col, int row) {
        for (int i = 0; i < gamePanel.iTile[gamePanel.currentMap].length; i++) {
            InteractiveTile tile = gamePanel.iTile[gamePanel.currentMap][i];
            if (tile != null && tile.worldX / gamePanel.tileSize == col && tile.worldY / gamePanel.tileSize == row) {
                return i;
            }
        }
        return -1;
    }

    private int findEmptyTileSlot() {
        for (int i = 0; i < gamePanel.iTile[gamePanel.currentMap].length; i++) {
            if (gamePanel.iTile[gamePanel.currentMap][i] == null) {
                return i;
            }
        }
        return -1;
    }

    private int findObjectIndex(int col, int row) {
        for (int i = 0; i < gamePanel.object[gamePanel.currentMap].length; i++) {
            Entity obj = gamePanel.object[gamePanel.currentMap][i];
            if (obj != null && obj.worldX / gamePanel.tileSize == col && obj.worldY / gamePanel.tileSize == row) {
                return i;
            }
        }
        return -1;
    }

    private int findEmptyObjectSlot() {
        for (int i = 0; i < gamePanel.object[gamePanel.currentMap].length; i++) {
            if (gamePanel.object[gamePanel.currentMap][i] == null) {
                return i;
            }
        }
        return -1;
    }

    public void savePlayerPosition(int map, int area, int worldX, int worldY) {
        try(Connection connection = DriverManager.getConnection(DB_URL)) {
            String query = "UPDATE player SET currentMap = ?, currentArea = ?, worldX = ?, worldY = ? WHERE id = 1;";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, map);
            preparedStatement.setInt(2, area);
            preparedStatement.setInt(3, worldX);
            preparedStatement.setInt(4, worldY);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0) {
                // If no row was updated, insert a new one with default values
                String insertQuery = "INSERT INTO player (id, currentMap, currentArea, worldX, worldY) VALUES (1, ?, ?, ?, ?);";
                PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
                insertStmt.setInt(1, map);
                insertStmt.setInt(2, area);
                insertStmt.setInt(3, worldX);
                insertStmt.setInt(4, worldY);
                insertStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}