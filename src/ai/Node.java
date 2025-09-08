package ai;

public class Node {

    Node parent;
    public int col;
    public int row;
    int hCost;
    int gCost;
    int fCost;
    boolean solid;
    boolean open;
    boolean checked;

    public Node(int col,int row) {
        this.col = col;
        this.row = row;
    }
}
