package net.heinke.cbingutter.game.exception;

@SuppressWarnings("serial")
public class BowlingException extends Exception {
	protected String message;
	
	public BowlingException(String message){
		this.message = message;
	}
	public BowlingException(){
	}
	
	public String getMessage(){
		return message;
	}
}
