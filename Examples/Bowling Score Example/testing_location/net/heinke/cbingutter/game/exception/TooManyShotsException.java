package net.heinke.cbingutter.game.exception;

@SuppressWarnings("serial")
public class TooManyShotsException extends BowlingException {

	public TooManyShotsException() {
		message = "Number of shot taken exceeds the per frame limit";
	}

}
