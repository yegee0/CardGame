package cardgame;

public class Main {
	
	public static void main(String[] args) { 
	       
		new MainMenu();
		GamePanel gamePanel = new GamePanel(); 
        GameLogic game = new GameLogic(gamePanel);
        game.resetHighScores();
	         
	 }
}
