package game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;


public class Main {

    public static JFrame window;

    public static void main(String[] args) {

        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Search for Hope");
        GamePanel gamePanel = new GamePanel();

        gamePanel.config.loadConfig();
        if(gamePanel.fullScreenOn) {
            window.setUndecorated(true);
        }
        try {
            Image icon = ImageIO.read(Main.class.getResource("/items/amuletOfLife.png")); // Adjust path as needed
            window.setIconImage(icon);
        } catch (IOException e) {
            System.out.println("Could not load icon: " + e.getMessage());
        }
        window.add(gamePanel);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        gamePanel.setupGame();
        gamePanel.startGameThread();
        }
    }
