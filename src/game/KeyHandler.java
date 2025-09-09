package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, attackPressed, shotKeyPressed;
    GamePanel gamePanel;
    private long lastNavigationTime = 0;
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
        //OPTION STATE
        else if(gamePanel.gameState == gamePanel.optionsState) {
            optionState(code);
        }
        //GAME OVER STATE
        else if(gamePanel.gameState == gamePanel.gameOverState) {
            gameOverState(code);
        }
        //TRADE STATE
        else if(gamePanel.gameState == gamePanel.tradeState) {
            tradeState(code);
        }
        //MAP STATE
        else if(gamePanel.gameState == gamePanel.mapState) {
            mapState(code);
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
        if(code == KeyEvent.VK_R) {
            switch (gamePanel.currentMap) {
                case 0: gamePanel.tileManager.loadMap("/maps/map.txt",0); break;
                case 1: gamePanel.tileManager.loadMap("/maps/interior", 1); break;
            }
        }
        if(code == KeyEvent.VK_F2) {

            gamePanel.eventHandler.toggleEventDebug();
        }
        if(code == KeyEvent.VK_F) {
            shotKeyPressed = true;
        }
        if(code == KeyEvent.VK_ESCAPE) {
            gamePanel.gameState = gamePanel.optionsState;
        }
        if(code == KeyEvent.VK_M) {
            gamePanel.gameState = gamePanel.mapState;
        }
        if(code == KeyEvent.VK_X) {
            gamePanel.map.miniMapOn = !gamePanel.map.miniMapOn;
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
        Sound sound = Sound.getInstance();

        if(code == KeyEvent.VK_C) {
            gamePanel.gameState = gamePanel.playState;
        }

        if(code == KeyEvent.VK_ENTER) {
            gamePanel.player.selectItem();
        }
        playerInventory(code);
    }

    public void optionState(int code) {
        Sound sound = Sound.getInstance();

        if(code == KeyEvent.VK_ESCAPE) {
            gamePanel.gameState = gamePanel.playState;
        }
        if(code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }

        int maxCommandNum = 0;
        switch (gamePanel.ui.subState) {
            case 0: maxCommandNum = 5; break;
            case 3: maxCommandNum = 1; break;
        }

        if(code == KeyEvent.VK_W) {
            gamePanel.ui.commandNum--;
            sound.playSoundEffect(9);
            if(gamePanel.ui.commandNum < 0) {
                gamePanel.ui.commandNum = maxCommandNum;
            }
        }
        if(code == KeyEvent.VK_S) {
            gamePanel.ui.commandNum++;
            sound.playSoundEffect(9);
            if(gamePanel.ui.commandNum > maxCommandNum) {
                gamePanel.ui.commandNum = 0;
            }
        }

        if(code == KeyEvent.VK_A) {
            if(gamePanel.ui.subState == 0) {
                if(gamePanel.ui.commandNum == 1 && gamePanel.sound.getMusicVolumeScale() > 0) {
                    gamePanel.sound.setMusicVolumeScale(gamePanel.sound.getMusicVolumeScale() - 1);
                    gamePanel.sound.playSoundEffect(9);
                }
                if(gamePanel.ui.commandNum == 2 && gamePanel.sound.getSEVolumeScale() > 0) {
                    gamePanel.sound.setSEVolumeScale(gamePanel.sound.getSEVolumeScale() - 1);
                    gamePanel.sound.playSoundEffect(9);
                }
            }
        }
        if(code == KeyEvent.VK_D) {
            if(gamePanel.ui.subState == 0) {
                if(gamePanel.ui.commandNum == 1 && gamePanel.sound.getMusicVolumeScale() < 5) {
                    gamePanel.sound.setMusicVolumeScale(gamePanel.sound.getMusicVolumeScale() + 1);
                    gamePanel.sound.playSoundEffect(9);
                }
                if(gamePanel.ui.commandNum == 2 && gamePanel.sound.getSEVolumeScale() < 5) {
                    gamePanel.sound.setSEVolumeScale(gamePanel.sound.getSEVolumeScale() + 1);
                    gamePanel.sound.playSoundEffect(9);
                }
            }
        }
    }

    public void gameOverState(int code) {

        if(code == KeyEvent.VK_W) {
            gamePanel.ui.commandNum--;
            if(gamePanel.ui.commandNum < 0) {
                gamePanel.ui.commandNum = 1;
            }
            gamePanel.sound.playSoundEffect(9);
        }

        if(code == KeyEvent.VK_S) {
            gamePanel.ui.commandNum++;
            if(gamePanel.ui.commandNum > 1) {
                gamePanel.ui.commandNum = 0;
            }
            gamePanel.sound.playSoundEffect(9);
        }
        if(code == KeyEvent.VK_ENTER) {
            if(gamePanel.ui.commandNum == 0) {
                gamePanel.gameState = gamePanel.playState;
                gamePanel.retry();
            }
            else if(gamePanel.ui.commandNum == 1) {
                gamePanel.gameState = gamePanel.titleState;
                gamePanel.restart();
            }
        }

    }

    public void tradeState(int code) {
        if(code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        long currentTime = System.currentTimeMillis();

        if(gamePanel.ui.subState == 0) {
            long NAVIGATION_DELAY = 150;
            if(currentTime - lastNavigationTime >= NAVIGATION_DELAY)
            if(code == KeyEvent.VK_W) {
                gamePanel.ui.commandNum--;
                if(gamePanel.ui.commandNum < 0) {
                    gamePanel.ui.commandNum = 2;
                }
                gamePanel.sound.playSoundEffect(9);
                lastNavigationTime = currentTime;
            }
            if(code == KeyEvent.VK_S) {
                gamePanel.ui.commandNum++;
                if(gamePanel.ui.commandNum > 2) {
                    gamePanel.ui.commandNum = 0;
                }
                gamePanel.sound.playSoundEffect(9);
                lastNavigationTime = currentTime;
            }
        }
        if(gamePanel.ui.subState == 1) {
            npcInventory(code);
            if(code == KeyEvent.VK_ESCAPE) {
                gamePanel.ui.subState = 0;
            }
        }
        if(gamePanel.ui.subState == 2) {
            playerInventory(code);
            if(code == KeyEvent.VK_ESCAPE) {
                gamePanel.ui.subState = 0;
            }
        }
    }

    public void mapState(int code) {
        if(code == KeyEvent.VK_M) {
            gamePanel.gameState = gamePanel.playState;
        }
    }

    public void playerInventory(int code) {
        if(code == KeyEvent.VK_W) {
            if(gamePanel.ui.playerSlotRow != 0){
                gamePanel.ui.playerSlotRow--;
                gamePanel.sound.playSoundEffect(9);
            }
        }
        if(code == KeyEvent.VK_A) {
            if(gamePanel.ui.playerSlotCol != 0){
                gamePanel.ui.playerSlotCol--;
                gamePanel.sound.playSoundEffect(9);
            }
        }
        if(code == KeyEvent.VK_S) {
            if(gamePanel.ui.playerSlotRow != 3){
                gamePanel.ui.playerSlotRow++;
                gamePanel.sound.playSoundEffect(9);
            }
        }
        if(code == KeyEvent.VK_D) {
            if(gamePanel.ui.playerSlotCol != 4){
                gamePanel.ui.playerSlotCol++;
                gamePanel.sound.playSoundEffect(9);
            }
        }
    }

    public void npcInventory(int code) {
        if(code == KeyEvent.VK_W) {
            if(gamePanel.ui.npcSlotRow != 0){
                gamePanel.ui.npcSlotRow--;
                gamePanel.sound.playSoundEffect(9);
            }
        }
        if(code == KeyEvent.VK_A) {
            if(gamePanel.ui.npcSlotCol != 0){
                gamePanel.ui.npcSlotCol--;
                gamePanel.sound.playSoundEffect(9);
            }
        }
        if(code == KeyEvent.VK_S) {
            if(gamePanel.ui.npcSlotRow != 3){
                gamePanel.ui.npcSlotRow++;
                gamePanel.sound.playSoundEffect(9);
            }
        }
        if(code == KeyEvent.VK_D) {
            if(gamePanel.ui.npcSlotCol != 4){
                gamePanel.ui.npcSlotCol++;
                gamePanel.sound.playSoundEffect(9);
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

    private void startGame() {
        System.out.println("Starting game with selected character...");
        gamePanel.gameState = gamePanel.playState;
        gamePanel.ui.titleScreenState = 0; // Reset to main menu
        gamePanel.ui.commandNum = 0; // Reset selection to top
    }

}