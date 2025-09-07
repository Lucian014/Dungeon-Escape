package game;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventHandler {
    GamePanel gamePanel;
    Rectangle eventRect;
    int eventRectDefaultX, eventRectDefaultY;
    Map<Integer, ArrayList<Event>> mapEvents; // Changed to store ArrayList of events per map

    public EventHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        eventRect = new Rectangle();
        eventRect.x = 23;
        eventRect.y = 23;
        eventRect.width = 2;
        eventRect.height = 2;
        eventRectDefaultX = eventRect.x;
        eventRectDefaultY = eventRect.y;
        mapEvents = new HashMap<>();
        setupEvents();
    }

    private void setupEvents() {
        // Create ArrayList for map 0 events
        ArrayList<Event> map0Events = new ArrayList<>();
        map0Events.add(new Event(0, 27, 16, "right", this::damagePit, true));
        map0Events.add(new Event(0, 23, 12, "up", this::healingPool, true));
        map0Events.add(new Event(0,10,39,"any", () -> teleport(1,12,13),true));
        // Put the ArrayList in the map
        mapEvents.put(0, map0Events);

        // You can add events for other maps like this:
        // ArrayList<Event> map1Events = new ArrayList<>();
        // map1Events.add(new Event(1, 10, 10, "any", this::teleport, true));
        // mapEvents.put(1, map1Events);
    }

    public void checkEvent() {
        int currentMap = gamePanel.currentMap;

        // Get events for the current map only
        ArrayList<Event> currentMapEvents = mapEvents.get(currentMap);
        if (currentMapEvents == null) return; // No events for this map

        for (Event event : currentMapEvents) {
            boolean currentlyInArea = hit(event.col, event.row, event.reqDirection);

            if (currentlyInArea && !event.playerInArea) {
                // Player just entered the event area
                event.playerInArea = true;
                if (!event.hasBeenTriggered || event.canRetrigger) {
                    event.action.run();
                    event.hasBeenTriggered = true;
                }
            } else if (!currentlyInArea && event.playerInArea) {
                // Player just left the event area
                event.playerInArea = false;
            }
        }
    }

    public boolean hit(int eventCol, int eventRow, String reqDirection) {
        boolean hit = false;

        gamePanel.player.solidArea.x = gamePanel.player.solidArea.x + gamePanel.player.worldX;
        gamePanel.player.solidArea.y = gamePanel.player.solidArea.y + gamePanel.player.worldY;
        eventRect.x = eventCol * gamePanel.tileSize + eventRect.x;
        eventRect.y = eventRow * gamePanel.tileSize + eventRect.y;

        if(gamePanel.player.solidArea.intersects(eventRect)) {
            if(gamePanel.player.direction.equals(reqDirection) || reqDirection.equals("any")) {
                hit = true;
            }
        }

        gamePanel.player.solidArea.x = gamePanel.player.solidAreaDefaultX;
        gamePanel.player.solidArea.y = gamePanel.player.solidAreaDefaultY;
        eventRect.x = eventRectDefaultX;
        eventRect.y = eventRectDefaultY;

        return hit;
    }

    public void damagePit() {
        gamePanel.player.life -= 1;
        gamePanel.ui.currentDialogue = "You fall into a pit!";
        gamePanel.gameState = gamePanel.dialogueState;
    }

    public void healingPool() {
        gamePanel.gameState = gamePanel.dialogueState;
        gamePanel.ui.currentDialogue = "You drink the water.\nYour life has been recovered";
        gamePanel.player.life = gamePanel.player.maxLife;
        gamePanel.player.mana = gamePanel.player.maxMana;
        gamePanel.assetSetter.setMonster();
    }

    public void teleport(int map, int col, int row) {
        gamePanel.gameState = gamePanel.dialogueState;
        gamePanel.currentMap = map;
        gamePanel.ui.currentDialogue = "Teleport!";
        gamePanel.player.worldX = gamePanel.tileSize * col;
        gamePanel.player.worldY = gamePanel.tileSize * row;
    }
}
