package game;

import entity.Entity;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventHandler{
    GamePanel gamePanel;
    Rectangle eventRect;
    int eventRectDefaultX, eventRectDefaultY;
    Map<Integer, ArrayList<Event>> mapEvents;
    private long lastTeleportTime = 0;
    int tempMap,tempCol,tempRow;
    Entity eventMaster;

    // Debug visualization toggle
    public boolean showEventDebug = false;

    public EventHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        eventMaster = new Entity(gamePanel);
        eventRect = new Rectangle();
        eventRect.x = 24;
        eventRect.y = 15;
        eventRect.width = 8;
        eventRect.height = 8;
        eventRectDefaultX = eventRect.x;
        eventRectDefaultY = eventRect.y;
        mapEvents = new HashMap<>();
        setupEvents();
        setDialogue();
    }

    void setDialogue() {

        eventMaster.dialogues[0][0] = "You fall into a pit!";

        eventMaster.dialogues[1][0] = "You drink the water.\nYour life has been recovered";

        eventMaster.dialogues[2][0] = "You fall into a pit!";

    }
    private void setupEvents() {
        // MAP 0 EVENTS
        ArrayList<Event> map0Events = new ArrayList<>();
        map0Events.add(new Event(0, 27, 16, "right", this::damagePit, true));
        map0Events.add(new Event(0, 23, 12, "up", this::healingPool, true));
        map0Events.add(new Event(0, 10, 39, "any", () -> teleport(1,gamePanel.indoor,12, 13), true)); //teleport to merchant
        map0Events.add(new Event(0,12,9,"any", () -> teleport(2,gamePanel.dungeon,9,41),true)); //teleport to dungeon
        mapEvents.put(0, map0Events);

        // MAP 1 EVENTS
        ArrayList<Event> map1Events = new ArrayList<>();
        map1Events.add(new Event(1, 12, 13, "down", () -> teleport(0,gamePanel.outside,10, 39), true)); //teleport outside
        map1Events.add(new Event(1, 12, 9, "up",() -> speak(gamePanel.npc[1][0]),true));

        mapEvents.put(1, map1Events);

        //MAP 2 EVENTS
        ArrayList<Event> map2Events = new ArrayList<>();
        map2Events.add(new Event(2, 9, 41, "down", () -> teleport(0,gamePanel.outside, 12, 9), true)); //teleport outside
        map2Events.add(new Event(2, 8, 7, "any",() -> teleport(3,gamePanel.dungeon,26,41),true)); // teleport to Final Boss
        mapEvents.put(2,map2Events);

        //MAP3 EVENTS
        ArrayList<Event> map3Events = new ArrayList<>();
        map3Events.add(new Event(3, 26, 41, "down", () -> teleport(2, gamePanel.outside, 8, 7), true)); //teleport in the main dungeon
        map3Events.add(new Event(3, 25, 27, "any", this::skeletonLord, true)); //teleport outside

        mapEvents.put(3,map3Events);
    }

    // Method to draw event trigger areas for debugging
    public void drawEventDebug(Graphics2D g2) {
        if (!showEventDebug) return;

        int currentMap = gamePanel.currentMap;
        ArrayList<Event> currentMapEvents = mapEvents.get(currentMap);
        if (currentMapEvents == null) return;

        // Save original stroke and composite
        Stroke originalStroke = g2.getStroke();
        Composite originalComposite = g2.getComposite();

        for (Event event : currentMapEvents) {
            // Calculate screen position for this event
            int worldX = event.col * gamePanel.tileSize;
            int worldY = event.row * gamePanel.tileSize;

            int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
            int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;

            // Only draw if the event tile is visible on screen
            if (screenX > -gamePanel.tileSize && screenX < gamePanel.screenWidth &&
                    screenY > -gamePanel.tileSize && screenY < gamePanel.screenHeight) {

                // Draw different colors for different event types
                Color eventColor = getEventColor(event);

                // Draw semi-transparent tile overlay
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
                g2.setColor(eventColor);
                g2.fillRect(screenX, screenY, gamePanel.tileSize, gamePanel.tileSize);

                // Draw border
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
                g2.setStroke(new BasicStroke(2));
                g2.drawRect(screenX, screenY, gamePanel.tileSize, gamePanel.tileSize);

                // Draw the actual event rectangle (the tiny trigger area)
                g2.setColor(Color.RED);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                int eventRectX = screenX + eventRectDefaultX;
                int eventRectY = screenY + eventRectDefaultY;
                g2.fillRect(eventRectX, eventRectY, eventRect.width, eventRect.height);

                // Draw direction indicator
                drawDirectionIndicator(g2, screenX, screenY, event.reqDirection, eventColor);

                // Draw event info text
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 10));
                String eventInfo = "(" + event.col + "," + event.row + ")";
                g2.drawString(eventInfo, screenX + 2, screenY + 12);
                g2.drawString(event.reqDirection, screenX + 2, screenY + 24);
            }
        }

        // Restore original graphics settings
        g2.setStroke(originalStroke);
        g2.setComposite(originalComposite);
    }

    private Color getEventColor(Event event) {
        // Determine color based on event type (you can customize this)
        if (event.action.toString().contains("teleport")) {
            return Color.BLUE;
        } else if (event.action.toString().contains("speak")) {
            return Color.GREEN;
        } else if (event.action.toString().contains("damagePit")) {
            return Color.RED;
        } else if (event.action.toString().contains("healingPool")) {
            return Color.CYAN;
        }
        return Color.YELLOW; // Default color
    }

    private void drawDirectionIndicator(Graphics2D g2, int screenX, int screenY, String direction, Color color) {
        g2.setColor(color);
        g2.setStroke(new BasicStroke(3));

        int centerX = screenX + gamePanel.tileSize / 2;
        int centerY = screenY + gamePanel.tileSize / 2;
        int arrowSize = 8;

        switch (direction.toLowerCase()) {
            case "up":
                g2.drawLine(centerX, centerY - arrowSize, centerX, centerY + arrowSize);
                g2.drawLine(centerX, centerY - arrowSize, centerX - 4, centerY - arrowSize + 4);
                g2.drawLine(centerX, centerY - arrowSize, centerX + 4, centerY - arrowSize + 4);
                break;
            case "down":
                g2.drawLine(centerX, centerY - arrowSize, centerX, centerY + arrowSize);
                g2.drawLine(centerX, centerY + arrowSize, centerX - 4, centerY + arrowSize - 4);
                g2.drawLine(centerX, centerY + arrowSize, centerX + 4, centerY + arrowSize - 4);
                break;
            case "left":
                g2.drawLine(centerX - arrowSize, centerY, centerX + arrowSize, centerY);
                g2.drawLine(centerX - arrowSize, centerY, centerX - arrowSize + 4, centerY - 4);
                g2.drawLine(centerX - arrowSize, centerY, centerX - arrowSize + 4, centerY + 4);
                break;
            case "right":
                g2.drawLine(centerX - arrowSize, centerY, centerX + arrowSize, centerY);
                g2.drawLine(centerX + arrowSize, centerY, centerX + arrowSize - 4, centerY - 4);
                g2.drawLine(centerX + arrowSize, centerY, centerX + arrowSize - 4, centerY + 4);
                break;
            case "any":
                // Draw a circle for "any" direction
                g2.drawOval(centerX - arrowSize/2, centerY - arrowSize/2, arrowSize, arrowSize);
                break;
        }
    }

    // Toggle method for debugging (call this when you press a debug key)
    public void toggleEventDebug() {
        showEventDebug = !showEventDebug;
        System.out.println("Event debug visualization: " + (showEventDebug ? "ON" : "OFF"));
    }

    public void checkEvent() {
        int currentMap = gamePanel.currentMap;
        ArrayList<Event> currentMapEvents = mapEvents.get(currentMap);
        if (currentMapEvents == null) return;

        for (Event event : currentMapEvents) {
            boolean currentlyInArea = hit(event.col, event.row, event.reqDirection);

            if (currentlyInArea && !event.playerInArea) {
                event.playerInArea = true;
                long TELEPORT_COOLDOWN = 1000;
                if ((!event.hasBeenTriggered || event.canRetrigger) &&
                        System.currentTimeMillis() - lastTeleportTime > TELEPORT_COOLDOWN) {
                    event.action.run();
                    event.hasBeenTriggered = true;
                    lastTeleportTime = System.currentTimeMillis();
                }
            } else if (!currentlyInArea && event.playerInArea) {
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
        eventMaster.startDialogue(eventMaster,0);
    }

    public void healingPool() {

        gamePanel.gameState = gamePanel.dialogueState;
        eventMaster.startDialogue(eventMaster,1);
        gamePanel.player.life = gamePanel.player.maxLife;
        gamePanel.player.mana = gamePanel.player.maxMana;
        gamePanel.assetSetter.setMonster();
    }

    public void teleport(int map, int area, int col, int row) {
        gamePanel.gameState = gamePanel.transitionState;
        gamePanel.nextArea = area;
        tempMap = map;
        tempCol = col;
        tempRow = row;
        int worldX = col * gamePanel.tileSize;
        int worldY = row * gamePanel.tileSize;
        gamePanel.dataManager.savePlayerPosition(map, area, worldX, worldY);
        gamePanel.sound.playSoundEffect(14);
    }

    public void speak(Entity entity) {
            gamePanel.gameState = gamePanel.dialogueState;
            entity.speak();

    }

    public void skeletonLord() {

        if(!gamePanel.player.killSkeletonLord){
            if(!gamePanel.bossBattleOn) {
                gamePanel.gameState = gamePanel.cutsceneState;
                gamePanel.cutsceneManager.sceneNum = gamePanel.cutsceneManager.skeletonLord;
            }
        }
    }
}