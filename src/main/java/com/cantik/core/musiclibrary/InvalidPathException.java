package com.cantik.core.musiclibrary;

/**
 * Exception linked to an invalid folder path
 *
 * @author cyprien
 */
public class InvalidPathException extends Exception {
	/**
	 * Constructor with standard message
	 */
	public InvalidPathException() {
		super("Invalid library path");
	}
}
