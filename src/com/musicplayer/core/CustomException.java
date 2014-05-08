package com.musicplayer.core;

/**
 * Custom exception class
 *
 * @author cyprien
 */
public class CustomException extends Exception {
	private static final long serialVersionUID = 3793741385756457836L;

	/**
	 * Constructor, init Exception and log error
	 *
	 * @param msg
	 * 		The message of the Excetion
	 */
	public CustomException(String msg) {
		super(msg);
		Log.addEntry(msg);
	}
}
