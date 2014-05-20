package com.musicplayer.core.scrobbler;

import com.musicplayer.core.CustomException;

/**
 * Exception linked to a misconfig of Lastfm
 *
 * @author cyprien
 */
public class ScrobblerException extends CustomException {
	private static final long serialVersionUID = 5405605093471491000L;

	/**
	 * Constructor with standard message
	 */
	public ScrobblerException() {
		super("Error connecting to lastfm");
	}
}
