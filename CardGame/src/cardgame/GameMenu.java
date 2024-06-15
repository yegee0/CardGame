package cardgame;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;

public class GameMenu {

    public static JMenuBar createMenuBar(GameLogic gamePanel) {

        JMenuBar menuBar = new JMenuBar();
        
        // Game Menu
        JMenu gameMenu = new JMenu("Game");
        JMenuItem restart = new JMenuItem("Restart");
        JMenuItem highScores = new JMenuItem("High Scores");

        gameMenu.add(restart);
        gameMenu.add(highScores);
        menuBar.add(gameMenu);
        
        menuBar.setLayout(new GridBagLayout()); //center jmenu
        // About Menu
        JMenu aboutMenu = new JMenu("About");
        JMenuItem dev = new JMenuItem("About the Developer");
        JMenuItem game = new JMenuItem("About the Game");

        aboutMenu.add(dev);
        aboutMenu.add(game);
        menuBar.add(aboutMenu);

        // Exit Menu
        JMenuItem exitMenu = new JMenuItem("Exit");
        menuBar.add(exitMenu);

        // Action Listeners
        dev.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Developer: Ata Yağız Güner\nStudent ID: 20230702106");
            }
        });

        game.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Matching card games are simple yet engaging games where players flip over cards from a deck aiming to find pairs with identical images or numbers.\n The objective is to remember the location of cards as they're flipped, eventually pairing all cards to win.\n These games enhance memory, concentration, and pattern recognition skills, making them popular for both children and adults.");
            }
        });

        exitMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        restart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gamePanel.restartGame(gamePanel.getPlayerName());
            }
        });

        highScores.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Score scoreManager = new Score();
                String playerName = gamePanel.getPlayerName();
                scoreManager.displayHighScores(playerName, gamePanel.score);
                
                // Save the high score before displaying it
                scoreManager.saveHighScore(playerName, gamePanel.score);
                
                List<String> highScores = scoreManager.getHighScores();

                String lastHighScore;

                if (!highScores.isEmpty()) {
                    String lastEntry = highScores.get(highScores.size() - 1);
                    String[] parts = lastEntry.split(",");
                    lastHighScore = parts[0] + ": " + parts[1];
                } else {
                    lastHighScore = "0";
                }

                JOptionPane.showMessageDialog(null, "High Score for " + lastHighScore, "High Scores", JOptionPane.INFORMATION_MESSAGE);
            }
        });


        return menuBar;
    }
}
