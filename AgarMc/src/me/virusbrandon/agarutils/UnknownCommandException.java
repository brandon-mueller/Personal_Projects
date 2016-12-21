package me.virusbrandon.agarutils;

/**
 * Throw Me When The Player Has No
 * Idea What They Are Doing...
 * 
 * 
 * @author Brandon
 *
 */
public class UnknownCommandException extends Exception{
	private static final long serialVersionUID = 1L;

	public UnknownCommandException(String message){
		super(message);
	}
}

/*
 * © 2016 Brandon Mueller
 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
 */