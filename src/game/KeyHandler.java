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
        else if(gamePanel.gameState == gamePanel.optionsState) {
            optionState(code);
        }
    }

    public void titleState(int code) {
        if(gamePanel.ui.titleScreenState == 0) { // Main menu
            handleMainMenu(code);
            gamePanel.playMusic(0);
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
        if(code == KeyEvent.VK_ESCAPE) {
            gamePanel.gameState = gamePanel.optionsState;
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
    public void optionState(int code) {

        if(code == KeyEvent.VK_ESCAPE) {
            gamePanel.gameState = gamePanel.playState;
        }
        if(code ==KeyEvent.VK_ENTER) {
            enterPressed = true;
        }

        int maxCommandNum = 0;
        switch (gamePanel.ui.subState) {
            case 0: maxCommandNum = 5; break;
            case 3: maxCommandNum = 1; break;
        }

        if(code ==KeyEvent.VK_W) {
            gamePanel.ui.commandNum--;
            gamePanel.playSE(9);
            if(gamePanel.ui.commandNum < 0) {
                gamePanel.ui.commandNum = maxCommandNum;
            }
        }
        if(code ==KeyEvent.VK_S) {
            gamePanel.ui.commandNum++;
            gamePanel.playSE(9);
            if(gamePanel.ui.commandNum > maxCommandNum) {
                gamePanel.ui.commandNum = 0;
            }
        }

        if(code == KeyEvent.VK_A) {
            if(gamePanel.ui.subState == 0) {
                if(gamePanel.ui.commandNum == 1 && gamePanel.music.volumeScale > 0) {
                    gamePanel.music.volumeScale--;
                    gamePanel.music.checkVolume();
                    gamePanel.playSE(9);
                }
                if(gamePanel.ui.commandNum == 2 && gamePanel.se.volumeScale > 0) {
                    gamePanel.se.volumeScale--;
                    gamePanel.playSE(9);
                }
            }
        }
        if(code == KeyEvent.VK_D) {
            if(gamePanel.ui.subState == 0) {
                if(gamePanel.ui.commandNum == 1 && gamePanel.music.volumeScale < 5) {
                    gamePanel.music.volumeScale++;
                    gamePanel.music.checkVolume();
                    gamePanel.playSE(9);
                }
            }
                if(gamePanel.ui.commandNum == 2 && gamePanel.se.volumeScale < 5) {
                    gamePanel.se.volumeScale++;
                    gamePanel.playSE(9);
                }
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