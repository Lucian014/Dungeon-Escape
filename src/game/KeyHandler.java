package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;

public class KeyHandler implements KeyListener {

    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, attackPressed, shotKeyPressed;
    GamePanel gamePanel;

    //DEBUG
    boolean showDebugText = false;
    public KeyHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();

        //PLAY STATE
        if(gamePanel.gameState == gamePanel.playState) {
            playState(code);
        }
        //PAUSE STATE
        else if(gamePanel.gameState == gamePanel.pauseState) {
            pauseState(code);
        }
        //DIALOGUE STATE
        else if(gamePanel.gameState == gamePanel.dialogueState) {
            dialogueState(code);
        }
        //CHARACTER STATE
        else if(gamePanel.gameState == gamePanel.characterState) {
            characterState(code);
        }
        //TITLE STATE
        else if(gamePanel.gameState == gamePanel.titleState) {
            titleState(code);
        }
    }

    public void titleState(int code) {
        if(gamePanel.ui.titleScreenState == 0) { // Main menu
            handleMainMenu(code);
        }
        else if(gamePanel.ui.titleScreenState == 1) { // Character selection
            handleCharacterSelection(code);
        }
    }

    private void handleMainMenu(int code) {
        if(code == KeyEvent.VK_W){
            gamePanel.ui.commandNum--;
            if(gamePanel.ui.commandNum < 0) {
                gamePanel.ui.commandNum = 3;
            }
        }
        if(code == KeyEvent.VK_S){
            gamePanel.ui.commandNum++;
            if(gamePanel.ui.commandNum > 3) {
                gamePanel.ui.commandNum = 0;
            }
        }
        if(code == KeyEvent.VK_ENTER) {
            switch(gamePanel.ui.commandNum) {
                case 0: // New Game
                    gamePanel.ui.titleScreenState = 1;
                    // Reset selection for character screen
                    break;
                case 1: // Load Game (example)
                    // Add load game functionality
                    break;
                case 2: // Options (example)
                    // Add options functionality
                    break;
                case 3: // Quit
                    System.exit(0);
                    break;
            }
        }
    }
    public void playState(int code) {
        if(code == KeyEvent.VK_W){
            upPressed = true;
        }
        if(code == KeyEvent.VK_A){
            leftPressed = true;
        }
        if(code == KeyEvent.VK_S){
            downPressed = true;
        }
        if(code == KeyEvent.VK_D){
            rightPressed = true;
        }
        if(code == KeyEvent.VK_P) {
            gamePanel.gameState = gamePanel.pauseState;
        }
        if(code == KeyEvent.VK_C) {
            gamePanel.gameState = gamePanel.characterState;
        }
        if(code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        if(code == KeyEvent.VK_SPACE) {
            attackPressed = true;
        }
        if(code == KeyEvent.VK_T) {
            if(!showDebugText){
                showDebugText = true;
            } else if (showDebugText) {
                showDebugText = false;
            }
        }
        if(code == KeyEvent.VK_F) {
            shotKeyPressed = true;
        }
    }

    public void pauseState(int code) {
        if(code == KeyEvent.VK_P) {
            gamePanel.gameState = gamePanel.playState;
        }
    }

    public void dialogueState(int code) {
        if(code == KeyEvent.VK_ENTER){
            gamePanel.gameState = gamePanel.playState;
            gamePanel.ui.currentDialogue = "";
        }
    }
    public void characterState(int code) {
        if(code == KeyEvent.VK_C) {
            gamePanel.gameState = gamePanel.playState;
        }
        if(code == KeyEvent.VK_W) {
            if(gamePanel.ui.slotRow != 0){
                gamePanel.ui.slotRow--;
                gamePanel.playSE(9);
            }
        }
        if(code == KeyEvent.VK_A) {
            if(gamePanel.ui.slotCol != 0){
                gamePanel.ui.slotCol--;
                gamePanel.playSE(9);
            }

        }
        if(code == KeyEvent.VK_S) {
            if(gamePanel.ui.slotRow != 3){
                gamePanel.ui.slotRow++;
                gamePanel.playSE(9);
            }
        }
        if(code == KeyEvent.VK_D) {
            if(gamePanel.ui.slotCol != 4){
                gamePanel.ui.slotCol++;
                gamePanel.playSE(9);
            }
        }

        if(code == KeyEvent.VK_ENTER) {
            gamePanel.player.selectItem();
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {

        int code = e.getKeyCode();

        if(code == KeyEvent.VK_W){
            upPressed = false;
        }
        if(code == KeyEvent.VK_A){
            leftPressed = false;
        }
        if(code == KeyEvent.VK_S){
            downPressed = false;
        }
        if(code == KeyEvent.VK_D){
            rightPressed = false;
        }
        if(code == KeyEvent.VK_F){
            shotKeyPressed = false;
        }
    }
    private void handleCharacterSelection(int code) {
        if(code == KeyEvent.VK_W){
            gamePanel.ui.commandNum--;
            if(gamePanel.ui.commandNum < 0) {
                gamePanel.ui.commandNum = 3;
            }
        }
        if(code == KeyEvent.VK_S){
            gamePanel.ui.commandNum++;
            if(gamePanel.ui.commandNum > 3) {
                gamePanel.ui.commandNum = 0;
            }
        }
        if(code == KeyEvent.VK_ENTER) {
            switch(gamePanel.ui.commandNum) {
                case 0: // Fighter
                case 1: // Another class
                case 2: // Thief
                    startGame();
                    break;
                case 3: // Back
                    gamePanel.ui.titleScreenState = 0;
                    gamePanel.ui.commandNum = 0; // Reset to "New Game" selection
                    break;
            }
        }
    }

    private void startGame() {
        System.out.println("Starting game with selected character...");
        gamePanel.gameState = gamePanel.playState;
        gamePanel.ui.titleScreenState = 0; // Reset to main menu
        gamePanel.ui.commandNum = 0; // Reset selection to top
    }
}