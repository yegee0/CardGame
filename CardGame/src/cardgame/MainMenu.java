package cardgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class MainMenu extends JFrame {

    private JButton startButton;
    private JButton selectLevelButton;
    private JButton instructionsButton;
    private JButton exitButton;
    private JLabel titleLabel;
    private GameLogic gamePanel;
    private JPopupMenu levelMenu;
    
     MainMenu() {

        setTitle("Memory Card Game");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center window
        setResizable(false);
        // Set background image
        ImageIcon backgroundImage = new ImageIcon(getClass().getResource("/background.jpg"));
        Image scaledImage = backgroundImage.getImage().getScaledInstance(800, 700, Image.SCALE_SMOOTH);
        backgroundImage = new ImageIcon(scaledImage);
        setContentPane(new JLabel(backgroundImage));
        setLayout(null);
        
        
        //Memory Text
        titleLabel = new JLabel("Memory Card Game");
        titleLabel.setFont(new Font("Arial", Font.ITALIC, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(195, 110, 400, 110);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel);


        // Sub panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // Make panel transparent
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        startButton = new JButton("Start Game");
        selectLevelButton = new JButton("Select Level");
        instructionsButton = new JButton("Instructions");
        exitButton = new JButton("Exit");

        Dimension buttonSize = new Dimension(150, 40); // Adjust button size here

        startButton.setPreferredSize(buttonSize);
        selectLevelButton.setPreferredSize(buttonSize);
        instructionsButton.setPreferredSize(buttonSize);
        exitButton.setPreferredSize(buttonSize);

        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectLevelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        instructionsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        GamePanel.customizeButton(startButton);
        GamePanel.customizeButton(selectLevelButton);
        GamePanel.customizeButton(instructionsButton);
        GamePanel.customizeButton(exitButton);

        
        levelMenu = new JPopupMenu();
        JMenuItem level1MenuItem = new JMenuItem("Level 1");
        JMenuItem level2MenuItem = new JMenuItem("Level 2");
        JMenuItem level3MenuItem = new JMenuItem("Level 3");

        
        
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               
            	startGame(1);
            }
        });

        selectLevelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	
            	levelMenu.show(selectLevelButton, 0, selectLevelButton.getHeight());
                
            }
        });

        instructionsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
            	JOptionPane.showMessageDialog(null,"Instructions:\n There are 3 levels in game. It gets gradually harder!\n Match all pairs of cards to win!");
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
            	System.exit(0);
            }
               
        });
        
        level1MenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
            	startGame(1); 
            }
        });

        level2MenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
            	startGame(2); 
            }
        });

        level3MenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
            	startGame(3); 
            }
        });

        levelMenu.add(level1MenuItem);
        levelMenu.add(level2MenuItem);
        levelMenu.add(level3MenuItem);
        
        // Create white boxes 
        JPanel startPanel = GamePanel.createOptionPanel(startButton);
        JPanel selectLevelPanel = GamePanel.createOptionPanel(selectLevelButton);
        JPanel instructionsPanel = GamePanel.createOptionPanel(instructionsButton);
        JPanel exitPanel = GamePanel.createOptionPanel(exitButton);

        buttonPanel.add(Box.createVerticalGlue()); // Push buttons to center
        buttonPanel.add(startPanel);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        buttonPanel.add(selectLevelPanel);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        buttonPanel.add(instructionsPanel);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        buttonPanel.add(exitPanel);
        buttonPanel.add(Box.createVerticalGlue());

        // Position button panel
        buttonPanel.setBounds(325, 230, 130, 210);
        
        add(buttonPanel);

        setVisible(true);
    }
 
     public void startGame(int level) {
    	    String playerName = null;
    	    while (playerName == null || playerName.isEmpty()) {
    	        playerName = JOptionPane.showInputDialog(null, "Enter your name:");
    	        if (playerName == null || playerName.isEmpty()) {
    	            JOptionPane.showMessageDialog(null, " Please enter a name.");
    	        }
    	    }
    	    if (gamePanel != null) {
    	        remove(gamePanel);
    	    }
    	    Score score = new Score();
    	    gamePanel = new GameLogic(playerName, level, score);

    	    setJMenuBar(GameMenu.createMenuBar(gamePanel));
    	    score.displayHighScores(playerName, score);   
    	    setContentPane(gamePanel);
    	    revalidate();
    	    gamePanel.requestFocusInWindow();
    	}
}