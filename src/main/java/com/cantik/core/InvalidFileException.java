package com.cantik.core;

/**
 * Exception linked to an invalid file
 *
 * @author cyprien
 */
public class InvalidFileException extends Exception {
	private static final long serialVersionUID = 15083144311040175L;

	/**
	 * Exception constructor with no specified path
	 */
	public InvalidFileException() {
		super("File does not exist");
	}

	/**
	 * Exception constructor with specified path
	 */
	public InvalidFileException(String path) {
		super("File(" + path + ") does not exist");
	}
}
