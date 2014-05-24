package com.musicplayer.core.musiclibrary;

/**
 *
 */
public class InvalidPathException extends Exception {
	/**
	 * Constructor with standard message
	 */
	public InvalidPathException() {
		super("Invalid library path");
	}
}
