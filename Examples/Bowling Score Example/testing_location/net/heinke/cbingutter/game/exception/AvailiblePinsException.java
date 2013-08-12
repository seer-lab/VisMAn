package net.heinke.cbingutter.game.exception;

@SuppressWarnings("serial")
public class AvailiblePinsException extends BowlingException {
	public AvailiblePinsException(){
		message = "Not enough availible pins for the entered score";
	}
}
