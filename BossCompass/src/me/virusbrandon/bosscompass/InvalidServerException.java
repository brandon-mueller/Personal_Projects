package me.virusbrandon.bosscompass;

public class InvalidServerException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidServerException(String message){
		super(message);
	}
}
