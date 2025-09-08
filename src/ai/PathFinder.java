package ai;

import entity.Entity;
import game.GamePanel;

import java.util.ArrayList;

public class PathFinder {

    GamePanel gamePanel;
    Node [][] node;
    ArrayList<Node> openList = new ArrayList<>();
    public ArrayList<Node> pathList = new ArrayList<>();
    Node startNode, goalNode, currentNode;
    boolean goalReached = false;
    int step = 0;

    public PathFinder(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        instantiateNodes();
    }

    public void instantiateNodes() {
        node = new Node[gamePanel.maxWorldCol][gamePanel.maxWorldRow];
        int col = 0;
        int row = 0;

        while (col < gamePanel.maxWorldCol && row < gamePanel.maxWorldRow) {

            node[col][row] = new Node(col,row);
            col++;
            if(col == gamePanel.maxWorldCol) {
                col = 0;
                row++;
            }
        }
    }

    public void resetNodes() {

        int col = 0;
        int row = 0;

        while(col < gamePanel.maxWorldCol && row < gamePanel.maxWorldRow) {

            node[col][row].open = false;
            node[col][row].checked = false;
            node[col][row].solid = false;

            col++;
            if(col == gamePanel.maxWorldCol) {
                col = 0;
                row++;
            }
        }

        //Reset other settings
        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;

    }

    public void setNodes(int startCol, int startRow, int goalCol, int goalRow, Entity entity) {

        resetNodes();
        //SET START AND GOAL NODES
        startNode = node[startCol][startRow];
        currentNode = startNode;
        goalNode = node[goalCol][goalRow];
        openList.add(currentNode);
        int col = 0;
        int row = 0;

        while(col < gamePanel.maxWorldCol && row < gamePanel.maxWorldRow) {

            //SET SOLID NODE
            //CHECK TILES
            int tileNum = gamePanel.tileManager.mapTileNum[gamePanel.currentMap][col][row];
            if(gamePanel.tileManager.tile[tileNum].collision) {
                node[col][row].solid = true;
            }
            //CHECK INTERACTIVE TILES
            if(gamePanel.iTile[gamePanel.currentMap] != null) {
                for (int i = 0; i < gamePanel.iTile[1].length; i++) {
                    if (gamePanel.iTile[gamePanel.currentMap][i] != null && gamePanel.iTile[gamePanel.currentMap][i].destructible) {
                        int itCol = gamePanel.iTile[gamePanel.currentMap][i].worldX / gamePanel.tileSize;
                        int itRow = gamePanel.iTile[gamePanel.currentMap][i].worldY / gamePanel.tileSize;
                        node[itCol][itRow].solid = true;
                    }
                }
            }
            //SET COST
            getCost(node[col][row]);

            col++;
            if(col == gamePanel.maxWorldCol) {
                col = 0;
                row++;
            }
        }
    }

    public void getCost(Node node) {

        //G COST
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = xDistance + yDistance;

        //H COST
        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = xDistance + yDistance;

        //F COST
        node.fCost = node.gCost + node.hCost;

    }

    public boolean search() {

        while (!goalReached && step < 500) {

            int col = currentNode.col;
            int row = currentNode.row;

            //Chech the current node
            currentNode.checked = true;
            openList.remove(currentNode);

            //Open the up node
            if(row - 1 >= 0) {
                openNode(node[col][row - 1]);
            }
            //Open the left node
            if(col - 1 >= 0) {
                openNode(node[col - 1][row]);
            }
            //Open the down node
            if(row + 1 < gamePanel.maxWorldRow) {
                openNode(node[col][row + 1]);
            }
            //Open the right node
            if(col + 1 < gamePanel.maxWorldCol) {
                openNode(node[col + 1][row]);
            }
            //Find the best node (lowest fCost)
            int bestNodeIndex = 0;
            int bestNodeFCost = 999;

            for (int i = 0; i < openList.size(); i++) {
                if(openList.get(i).fCost < bestNodeFCost) {
                    bestNodeIndex = i;
                    bestNodeFCost = openList.get(i).fCost;
                }
                //If fCost is the same, check gCost
                else if (openList.get(i).fCost == bestNodeFCost) {
                    if(openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
                        bestNodeIndex = i;
                        bestNodeFCost = openList.get(i).fCost;
                    }
                }
            }
            // If there is no node to openList, end the search
            if(openList.size() == 0) {
                break;
            }
            currentNode = openList.get(bestNodeIndex);
            if(currentNode == goalNode) {
                goalReached = true;
                trackThePath();
            }
            step++;
        }
        return goalReached;
    }
    public void openNode(Node node) {
        if (!node.checked && !node.open && !node.solid) {

            node.open = true;
            node.parent = currentNode;
            openList.add(node);
        }
    }
    public void trackThePath() {

        Node current = goalNode;

        while (current != startNode) {
            pathList.add(0, current);
            current = current.parent;
        }
    }
}
