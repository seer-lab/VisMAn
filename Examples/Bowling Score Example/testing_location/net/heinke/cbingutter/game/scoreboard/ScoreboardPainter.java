package net.heinke.cbingutter.game.scoreboard;

import java.util.List;

import net.heinke.cbingutter.game.Constants;
import net.heinke.cbingutter.game.frame.Frame;
import net.heinke.cbingutter.game.player.Player;

public class ScoreboardPainter {

	private static String strikeArt = "  ______   _________  _______     _____  ___  ____   ________  _ \n"
			+ ".' ____ \\ |  _   _  ||_   __ \\   |_   _||_  ||_  _| |_   __  || |\n"
			+ "| (___ \\_||_/ | | \\_|  | |__) |    | |    | |_/ /     | |_ \\_|| |\n"
			+ " _.____`.     | |      |  __ /     | |    |  __'.     |  _| _ | |\n"
			+ "| \\____) |   _| |_    _| |  \\ \\_  _| |_  _| |  \\ \\_  _| |__/ ||_|\n"
			+ " \\______.'  |_____|  |____| |___||_____||____||____||________|(_)\n\n";

	private static String turkeyArt = "                 .--.\n"
			+ " {\\             / q {\\\n" + " { `\\           \\ (-(~`\n"
			+ "{ '.{`\\          \\ \\ )\n" + "{'-{ ' \\  .-\"\"'-. \\ \\\n"
			+ "{._{'.' \\/       '.) \\\n" + "{_.{.   {`            |\n"
			+ "{._{ ' {   ;'-=-.     |\n" + " {-.{.' {  ';-=-.`    /\n"
			+ "  {._.{.;    '-=-   .'\n" + "   {_.-' `'.__  _,-'\n"
			+ "    jgs   |||`\n" + "         .='==,\n\n";

	private static String spareArt = "  ______   _______     _       _______     ________  \n"
			+ ".' ____ \\ |_   __ \\   / \\     |_   __ \\   |_   __  |\n"
			+ "| (___ \\_|  | |__) | / _ \\      | |__) |    | |_ \\_|\n"
			+ " _.____`.   |  ___/ / ___ \\     |  __ /     |  _| _ \n"
			+ "| \\____) | _| |_  _/ /   \\ \\_  _| |  \\ \\_  _| |__/ |\n"
			+ " \\______.'|_____||____| |____||____| |___||________|\n\n";

	public static void printScoreCard(List<Player> players,
			int longestPlayerName) {
		printColumnHeadings(longestPlayerName, players.get(0));
		for (Player p : players) {
			printTopLine(p, longestPlayerName);
			printBottomLine(p, longestPlayerName);
		}
	}

	private static String printTopLine(Player p, int longestPlayerName) {
		StringBuilder topLine = new StringBuilder();
		topLine.append(String.format("%-" + longestPlayerName + "s|",
				p.getPlayerName()));
		topLine.append(String.format("%-3s||   ", p.getRunningTotal()));
		List<Frame> regFrames = Constants.FRAMES_PER_MATCH > p.getFrames()
				.size() ? p.getFrames() : p.getFrames().subList(0,
				Constants.FRAMES_PER_MATCH - 1);
		for (Frame f : regFrames) {
			topLine.append(String.format("|%3s|", f.toString()));
		}
		if (Constants.FRAMES_PER_MATCH <= p.getFrames().size()) {
			Frame g = p.getFrames().get(Constants.FRAMES_PER_MATCH - 1);
			topLine.append("|" + g.toString());

			for (Frame f : p.getFrames().subList(Constants.FRAMES_PER_MATCH,
					p.getFrames().size())) {
				topLine.append(" " + f.toString());
			}
			topLine.append("|");
		}

		topLine.append("\n");
		System.out.print(topLine.toString());
		return topLine.toString();
	}

	private static void printBottomLine(Player p, int longestPlayerName) {
		StringBuilder bottomLine = new StringBuilder();
		int padding = longestPlayerName + Constants.SECOND_ROW_PADDING;
		bottomLine.append(String.format("%" + padding + "s", ""));
		List<Frame> printFrames = Constants.FRAMES_PER_MATCH > p.getFrames()
				.size() ? p.getFrames() : p.getFrames().subList(0,
				Constants.FRAMES_PER_MATCH - 1);
		for (Frame f : printFrames) {
			bottomLine.append(String.format("|%3s|", f.printAbsoluteScore()));
		}
		if (Constants.FRAMES_PER_MATCH <= p.getFrames().size()) {
			Frame g = p.getFrames().get(Constants.FRAMES_PER_MATCH - 1);
			if (Constants.FRAMES_PER_MATCH < p.getFrames().size())
				bottomLine
						.append(String.format("|%5s|", g.printAbsoluteScore()));
			else
				bottomLine
						.append(String.format("|%3s|", g.printAbsoluteScore()));
		}
		bottomLine.append("\n\n");
		System.out.print(bottomLine.toString());
	}

	private static void printColumnHeadings(int longestPlayerName,
			Player firstPlayer) {
		int framesToPrint = firstPlayer.getFrames().size() > Constants.FRAMES_PER_MATCH ? Constants.FRAMES_PER_MATCH
				: firstPlayer.getFrames().size();
		StringBuilder headers = new StringBuilder();
		int padding = longestPlayerName + 1;
		headers.append(String.format("%" + padding + "s", ""));
		headers.append("Tot");
		padding = Constants.SECOND_ROW_PADDING - 5;
		headers.append(String.format("%" + padding + "s", ""));
		for (int i = 1; i <= framesToPrint; ++i) {
			headers.append(String.format(" %3s ", i));
		}
		headers.append("\n");
		System.out.print(headers.toString());
	}

	public static void printResultEffects(Frame f, Player p) {
		if (f.isStrike()) {
			int framesPlayed = p.getFrames().size();
			if (framesPlayed > 2
					&& p.getFrames().get(framesPlayed - 2).isStrike()
					&& p.getFrames().get(framesPlayed - 3).isStrike()) {
				System.out.print(turkeyArt);
			} else {
				System.out.print(strikeArt);
			}
		}

		else if (f.isSpare()) {
			System.out.print(spareArt);
		}
	}

	public static void printMessage(String string) {
		System.out.println(string);

	}

	public static void printWinners(List<Player> leaders) {

		if (leaders.size() == 1) {
			printMessage("Thanks for playing, the winner is "
					+ leaders.get(0).getPlayerName());
		} else {
			String output = "Thanks for playing, we have a tie between ";
			for (Player p : leaders) {
				output += ", " + p.getPlayerName() ;
			}
			printMessage(output);
		}
	}

	public static void welcomeMessage() {
		String g = "  _____    _____          _   _ _ _______   ____  ______ _      _____ ________      ________   _____ _______ _  _____     \n"
				+ " |_   _|  / ____|   /\\   | \\ | ( )__   __| |  _ \\|  ____| |    |_   _|  ____\\ \\    / /  ____| |_   _|__   __( )/ ____|\n"
				+ "   | |   | |       /  \\  |  \\| |/   | |    | |_) | |__  | |      | | | |__   \\ \\  / /| |__      | |    | |  |/| (___   \n"
				+ "   | |   | |      / /\\ \\ | . ` |    | |    |  _ <|  __| | |      | | |  __|   \\ \\/ / |  __|     | |    | |     \\___ \\ \n"
				+ "  _| |_  | |____ / ____ \\| |\\  |    | |    | |_) | |____| |____ _| |_| |____   \\  /  | |____   _| |_   | |     ____) |  \n"
				+ " |_____|  \\_____/_/    \\_\\_| \\_|    |_|    |____/|______|______|_____|______|   \\/   |______| |_____|  |_|    |_____/  \n"
				+

				" _   _  ____ _______    _____ _    _ _______ _______ ______ _____ \n"
				+ "| \\ | |/ __ \\__   __|  / ____| |  | |__   __|__   __|  ____|  __ \\\n"
				+ "|  \\| | |  | | | |    | |  __| |  | |  | |     | |  | |__  | |__) |\n"
				+ "| . ` | |  | | | |    | | |_ | |  | |  | |     | |  |  __| |  _  /\n"
				+ "| |\\  | |__| | | |    | |__| | |__| |  | |     | |  | |____| | \\ \\\n"
				+ "|_| \\_|\\____/  |_|     \\_____|\\____/   |_|     |_|  |______|_|  \\_\\\n\n";
		System.out.print(g);
	}

}
