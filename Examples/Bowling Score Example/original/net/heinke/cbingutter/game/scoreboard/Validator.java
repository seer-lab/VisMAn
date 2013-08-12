package net.heinke.cbingutter.game.scoreboard;

import net.heinke.cbingutter.game.Constants;
import net.heinke.cbingutter.game.frame.Frame;
import net.heinke.cbingutter.game.player.Player;

public class Validator {
	public static boolean validateScore(String input, Player p) {
		String pattern = "^[0-9/Xx]$";
		if (!input.matches(pattern)) {
			ScoreboardPainter.printMessage("Please enter 0-9, X or /");
			return false;
		}
		Frame f = p.getCurrentFrame();
		if (!input.equals("/") && !input.equalsIgnoreCase("X")) {
			int score = Integer.parseInt(input);
			if (score > Constants.MAX_PINS_PER_FRAME) {
				ScoreboardPainter.printMessage("Please enter 0-9, X or /");
				return false;
			} else if (score < 0) {
				ScoreboardPainter.printMessage("Please enter a positive score");
				return false;
			}
			if (f != null && !f.isCompleted()) {
				if (f.pinsRemaining() < score) {
					ScoreboardPainter
							.printMessage("You can't hit pins that don't exist");
					return false;
				}
			}
		} else if (f != null && !f.isCompleted() && input.equalsIgnoreCase("X")) {
			ScoreboardPainter
					.printMessage("Please enter your actual score or /");
			return false;
		} else if (input.equals("/") && (f == null || f.isCompleted())) {
			ScoreboardPainter.printMessage("Did you meant to enter X?");
			return false;
		}
		return true;
	}
	
	
	public static boolean validateNumberOfPlayers(String input){
		String pattern = "^[1-9]$";
		if (!input.matches(pattern)){
			ScoreboardPainter
					.printMessage("Please enter a number of players between 1 and 9");
			return false;
		}
		return true;
	}
	
	public static boolean validatePlayerName(String input){
		String pattern = "^$";
		if (input.matches(pattern)){
			ScoreboardPainter
					.printMessage("Please enter a number of players between 1 and 9");
			return false;
		}
		return true;
	}
}
