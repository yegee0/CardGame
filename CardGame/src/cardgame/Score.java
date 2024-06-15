package cardgame;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Score {
    
	private int score;

    public Score() {
        this.score = 0;
    }

    public void incrementScore(int value) {
        this.score += value;
    }

    public void decrementScore(int value) {
        this.score -= value;
    }

    public void saveHighScore(String playerName, Score score) {
        try (FileWriter writer = new FileWriter("highscores.txt", true)) {
            writer.write(playerName + "," + score.getScore() + "\n"); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clearHighScores() {
        try (FileWriter writer = new FileWriter("highscores.txt")) {
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getHighScores() {
        List<String> highScores = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("highscores.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                highScores.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return highScores;
    }

    public void displayHighScores(String playerName, Score score) {
        List<String> highScores = score.getHighScores();
        StringBuilder highScoresMessage = new StringBuilder("High Scores:\n");
        for (String highScore : highScores) {
            String[] parts = highScore.split(",");
            highScoresMessage.append(parts[0]).append(": ").append(parts[1]).append("\n");
        }
    }


    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

   

	public void resetHighScore() {
        this.score = 0;
		
	}
}
