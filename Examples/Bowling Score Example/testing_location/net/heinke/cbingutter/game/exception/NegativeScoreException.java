package net.heinke.cbingutter.game.exception;

@SuppressWarnings("serial")
public class NegativeScoreException extends BowlingException {

	public NegativeScoreException() {
		message = "A negative score is not valid";
	}

}
