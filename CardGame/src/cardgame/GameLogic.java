package cardgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("serial")
public class GameLogic extends JPanel {

    public int gridSize = 4;
    public JButton[][] cards;
    public ImageIcon[][] cardImages;
    public int numCardsSelected = 0;
    public JButton firstCardSelected;
    public JButton secondCardSelected;
    public boolean isChecking = false;
    public Timer timer;
    private int triesLeft;
    public JLabel triesLeftLabel;
    public JLabel levelLabel;
    public int level;
    public Score score;
    private String playerName;
    private Score scoreManager;
	private GamePanel gamePanel;
    private JPanel topPanel;
    
    
    public GameLogic(GamePanel gamePanel) {
        this.setGamePanel(gamePanel);
        this.scoreManager = new Score();
    }

    public GameLogic(String playerName, int level, Score score) {
        this.scoreManager = new Score();
        this.score = score;
        this.playerName = playerName;
        this.level = level;

        setLayout(new BorderLayout());
        
        // Initialize topPanel
        topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        Color[] levelColors = {new Color(100, 149, 237), new Color(147, 112, 219), new Color(196, 30, 58)};
        topPanel.setBackground(levelColors[level - 1]);

        levelLabel = new JLabel("LEVEL " + level);
        levelLabel.setForeground(new Color(245, 245, 245));
        levelLabel.setFont(new Font("Arial", Font.BOLD, 32));

        if (level == 1) {
            triesLeft = 18;
        } else if (level == 2) {
            triesLeft = 15;
        } else if (level == 3) {
            triesLeft = 12;
        }

        triesLeftLabel = new JLabel("  Tries Left:" + triesLeft);
        triesLeftLabel.setForeground(new Color(245, 245, 245));
        triesLeftLabel.setFont(new Font("Arial", Font.BOLD, 32));

        topPanel.add(levelLabel);
        topPanel.add(triesLeftLabel);
        add(topPanel, BorderLayout.NORTH);

        JPanel gamePanel = new JPanel(new GridLayout(gridSize, gridSize));
        initializeGame(gamePanel);
        assignCardImages();
        timer = new Timer();
        add(gamePanel, BorderLayout.CENTER);

    }


    public void initializeGame(JPanel gamePanel) {
        cards = new JButton[gridSize][gridSize];
        cardImages = new ImageIcon[gridSize][gridSize];

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                String placeholderPath = "/no_image" + level + ".png";
                ImageIcon placeholderIcon = new ImageIcon(getClass().getResource(placeholderPath));
                cards[i][j] = new JButton(placeholderIcon);
                cards[i][j].setPreferredSize(new Dimension(100, 100));
                cards[i][j].addActionListener(new CardButtonListener(i, j, score));
                gamePanel.add(cards[i][j]);
            }
        }
    }

    public void assignCardImages() {
        File path = getImageDirectory(level);

        File[] allFiles = path.listFiles();
        if (allFiles != null) {
            int imageIndex = 0;
            int totalCards = gridSize * gridSize;
            int totalImages = (totalCards / 2) + (totalCards % 2); // unique images
            ImageIcon[] uniqueImages = new ImageIcon[totalImages];
            int duplicateIndex = 0;

            for (File file : allFiles) {
                if (file.isFile()) {
                    if (imageIndex >= totalImages) {
                        break;
                    }

                    String imagePath = file.getPath();
                    ImageIcon imageIcon = new ImageIcon(imagePath);
                    Image image = imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    uniqueImages[imageIndex] = new ImageIcon(image);
                    imageIndex++;
                }
            }

            // Duplicate
            for (int i = 0; i < totalCards; i += 2) {
                cardImages[i / gridSize][i % gridSize] = uniqueImages[duplicateIndex];
                cardImages[(i + 1) / gridSize][(i + 1) % gridSize] = uniqueImages[duplicateIndex];
                duplicateIndex++;

                if (duplicateIndex >= totalImages) {
                    duplicateIndex = 0;
                }
            }
            shuffleCards();
        }
    }

    public void shuffleCards() {
        Thread shuffleThread = new Thread(() -> {
            ArrayList<ImageIcon> cardList = new ArrayList<>();

            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    if (cardImages[i][j] != null) {
                        cardList.add(cardImages[i][j]);
                    }
                }
            }

            // Shuffle the list
            Collections.shuffle(cardList);

            int index = 0;
            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    if (cardImages[i][j] != null) {
                        cardImages[i][j] = cardList.get(index++);
                    }
                }
            }

            // Update UI components on the Event Dispatch Thread
            SwingUtilities.invokeLater(() -> {
                revalidate();
                repaint();
            });
        });

        shuffleThread.start();
    }


    public void restartGame(String playerName) {
        this.playerName = playerName; 

        level = 1; 
        triesLeft = 18;
        score.setScore(0);
        scoreManager.clearHighScores(); 
        removeAll();
        revalidate();
        repaint();

        JPanel gamePanel = new JPanel(new GridLayout(gridSize, gridSize));
        initializeGame(gamePanel);
        assignCardImages();
        timer = new Timer();
        add(gamePanel, BorderLayout.CENTER);

        levelLabel.setText("LEVEL " + level);
        triesLeftLabel.setText("  Tries Left: " + triesLeft);

        Color[] levelColors = {new Color(100, 149, 237), new Color(147, 112, 219), new Color(196, 30, 58)};
        topPanel.setBackground(levelColors[level - 1]);


        topPanel.removeAll();
        topPanel.add(levelLabel);
        topPanel.add(triesLeftLabel);
        add(topPanel, BorderLayout.NORTH);

        revalidate();
        repaint();
    }



    public File getImageDirectory(int level) {
        switch (level) {
            case 1:
                return new File("res/images");
            case 2:
                return new File("res/images2");
            case 3:
                return new File("res/images3");
            default:
                return new File("res/images");
        }
    }

    public class CardButtonListener implements ActionListener {
        private int row;
        private int col;
        private Score score;

        public CardButtonListener(int row, int col, Score score) {
            this.row = row;
            this.col = col;
            this.score = score;
        }

        public void actionPerformed(ActionEvent e) {
            JButton selectedCard = (JButton) e.getSource();
            selectedCard.setIcon(cardImages[row][col]);

            if (!isChecking) {
                if (numCardsSelected == 0) {
                    firstCardSelected = selectedCard;
                    numCardsSelected = 1;
                } else if (numCardsSelected == 1) {
                    secondCardSelected = selectedCard;
                    numCardsSelected = 2;
                    checkForMatch();
                }
            }
        }

        public void checkForMatch() {
            isChecking = true;
            if (cardImages[row][col] != cardImages[getRow(firstCardSelected)][getCol(firstCardSelected)]) {
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        String placeholderPath = "/no_image" + level + ".png";
                        ImageIcon placeholderIcon = new ImageIcon(getClass().getResource(placeholderPath));

                        firstCardSelected.setIcon(placeholderIcon);
                        secondCardSelected.setIcon(placeholderIcon);
                        isChecking = false;
                        checkGameStatus();

                        if (level == 1 && score.getScore() > 0) {
                            score.decrementScore(1);
                        } else if (level == 2 && score.getScore() > 0) {
                            score.decrementScore(2);
                        } else if (score.getScore() > 0) {
                            score.decrementScore(3);
                        }

                        score.saveHighScore(playerName, score);

                        score.displayHighScores(playerName,score);
                    }
                }, 400);
            } else {
                // Correct match
                cardImages[row][col] = null;
                cardImages[getRow(firstCardSelected)][getCol(firstCardSelected)] = null;
                checkGameStatus();
                isChecking = false;

                // Award points for correct match
                if (level == 1) {
                    score.incrementScore(5);
                } else if (level == 2) {
                    score.incrementScore(4);
                } else {
                    score.incrementScore(3);
                }

                score.saveHighScore(playerName, score);

                score.displayHighScores(playerName,score);
            }
            numCardsSelected = 0;

            if (triesLeft > 0 && isChecking) {
                triesLeft--;
            }
            triesLeftLabel.setText("  Tries Left: " + triesLeft);
        }

        public void checkGameStatus() {
            int totalPairs = gridSize * gridSize / 2;
            int matchedPairs = 0;

            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    if (cardImages[i][j] == null) {
                        matchedPairs++;
                    }
                }
            }

            if (matchedPairs == totalPairs * 2) {
                if (level < 3) {
                    JOptionPane.showMessageDialog(null, "Congrats you won Level " + level + "!!");
                    int option = JOptionPane.showConfirmDialog(null, "Do you want to continue to the next level?", "Level Complete", JOptionPane.YES_NO_OPTION);

                    if (option == JOptionPane.YES_OPTION) {
                        startNewLevel();
                    } else {
                        System.exit(0);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Congrats you won the game!!");
                    int option = JOptionPane.showConfirmDialog(null, "Do you want to play again?", "Game Over", JOptionPane.YES_NO_OPTION);

                    if (option == JOptionPane.YES_OPTION) {
                        restartGame(playerName);
                    } else {
                        System.exit(0);
                    }
                }
            } else if (triesLeft == 0) {
            	 if (level == 3) {
                     JOptionPane.showMessageDialog(null, "Final Score: " + score.getScore(), "Game Over", JOptionPane.INFORMATION_MESSAGE);
                 }
                
                JOptionPane.showMessageDialog(null, "You lost, try again!");
                int option = JOptionPane.showConfirmDialog(null, "Do you want to play again?", "Game Over", JOptionPane.YES_NO_OPTION);

                if (option == JOptionPane.YES_OPTION) {
                    if (level == 1 || level == 2) {
                        restartGame(playerName);
                    } else {
                        restartGame(playerName);
                    }
                } else {
                    System.exit(0);
                }
            } else if (level == 3 && triesLeft > 0 && !isChecking ) {
                shuffleCards();
            }
        }

        public void startNewLevel() {
            level++;

            if (level == 1) {
                triesLeft = 18;
            } else if (level == 2) {
                triesLeft = 15;
            } else if (level == 3) {
                triesLeft = 12;
            }

            removeAll();
            revalidate();
            repaint();

            JPanel gamePanel = new JPanel(new GridLayout(gridSize, gridSize));
            initializeGame(gamePanel);
            assignCardImages();
            timer = new Timer();
            add(gamePanel, BorderLayout.CENTER);

            levelLabel.setText("LEVEL " + level);
            triesLeftLabel.setText("  Tries Left: " + triesLeft);

            Color[] levelColors = {new Color(100, 149, 237), new Color(147, 112, 219), new Color(196, 30, 58)};
            topPanel.setBackground(levelColors[level - 1]); // Update panel color based on level

            topPanel.removeAll();
            topPanel.add(levelLabel);
            topPanel.add(triesLeftLabel);
            add(topPanel, BorderLayout.NORTH);

            revalidate();
            repaint();
        }



        public int getRow(JButton button) {
            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    if (cards[i][j] == button) {
                        return i;
                    }
                }
            }
            return -1;
        }

        public int getCol(JButton button) {
            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    if (cards[i][j] == button) {
                        return j;
                    }
                }
            }
            return -1;
        }
    }   
    
    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void resetHighScores() {
        scoreManager.clearHighScores();
    }

    public void setTriesLeft(int triesLeft) {
        this.triesLeft = triesLeft;
    }

    public void setScore(Score score) {
        this.score = score;
    }

	public GamePanel getGamePanel() {
		return gamePanel;
	}

	public void setGamePanel(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}
}
