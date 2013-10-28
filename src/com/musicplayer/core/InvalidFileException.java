package com.musicplayer.core;

/**
 * Exception linked to an invalid file
 * 
 * @author cyprien
 * 
 */
public class InvalidFileException extends Exception {
	private static final long serialVersionUID = 15083144311040175L;

	/**
	 * Exception constructor with no specified path
	 */
	public InvalidFileException() {
		System.out.println("File does not exist!");
	}

	/**
	 * Exception constructor with specified path
	 */
	public InvalidFileException(String path) {
		System.out.println("File(" + path + ") does not exist!");
	}
}
