package game;

import java.awt.*;
import java.util.ArrayList;

public class EventHandler {
    GamePanel gamePanel;
    Rectangle eventRect;
    int eventRectDefaultX, eventRectDefaultY;
    ArrayList<Event> events;

    public EventHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        eventRect = new Rectangle();
        eventRect.x = 23;
        eventRect.y = 23;
        eventRect.width = 2;
        eventRect.height = 2;
        eventRectDefaultX = eventRect.x;
        eventRectDefaultY = eventRect.y;

        events = new ArrayList<>();
        setupEvents();
    }

    private void setupEvents() {
        // PIT EVENT at col 27, row 15
        events.add(new Event(27, 15, "right", this::damagePit, true));
        // HEALING POOL at col 23, row 12
        events.add(new Event(23, 12, "up", this::healingPool, true));
        //TELEPORT at col 27 row 16
        //events.add(new Event(27,15, "right", this::teleport, true));
    }

    public void checkEvent() {
        for (Event event : events) {
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
            if(gamePanel.player.direction.equals(reqDirection) || reqDirection.contentEquals("any")) {
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

    public void teleport() {

        gamePanel.gameState = gamePanel.dialogueState;
        gamePanel.ui.currentDialogue = "Teleport!";
        gamePanel.player.worldX = gamePanel.tileSize * 37;
        gamePanel.player.worldY = gamePanel.tileSize * 10;
    }
}