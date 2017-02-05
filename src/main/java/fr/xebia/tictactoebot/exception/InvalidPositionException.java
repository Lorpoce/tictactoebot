package fr.xebia.tictactoebot.exception;

public class InvalidPositionException extends Exception {
	private static final long serialVersionUID = 8787359761779922735L;

	public InvalidPositionException(String message) {
		super(message);
	}
}
