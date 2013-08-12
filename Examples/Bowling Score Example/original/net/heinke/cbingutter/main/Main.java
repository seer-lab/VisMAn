package net.heinke.cbingutter.main;

import net.heinke.cbingutter.game.exception.BowlingException;
import net.heinke.cbingutter.game.scoreboard.ScoreboardPainter;
import net.heinke.cbingutter.game.scoreboard.Scorecard;

public class Main {
	public static void main(String[] args) {
		ScoreboardPainter.welcomeMessage();
		Scorecard sc = new Scorecard();
		sc.loadPlayers();
		try {
			sc.playGame();
		} catch (BowlingException e) {
			ScoreboardPainter.printMessage(e.getMessage() + " Now terminating");
			System.exit(1);
		}
	}
}
