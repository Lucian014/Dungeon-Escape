package game;

public class Event {
    public int col;
    public int row;
    public String reqDirection;
    public Runnable action;
    public boolean canRetrigger;
    public boolean hasBeenTriggered;
    public boolean playerInArea;

    public Event(int col, int row, String reqDirection, Runnable action, boolean canRetrigger) {
        this.col = col;
        this.row = row;
        this.reqDirection = reqDirection;
        this.action = action;
        this.canRetrigger = canRetrigger;
        this.hasBeenTriggered = false;
        this.playerInArea = false;
    }
}

