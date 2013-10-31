package com.musicplayer.extra;

/**
 * Exception linked to a misconfig of Lastfm
 * 
 * @author cyprien
 * 
 */
public class ScrobblerException extends Exception {
	private static final long serialVersionUID = 5405605093471491000L;

	/**
	 * Constructor which show a warning
	 */
	public ScrobblerException() {
		System.out
				.println("Scrobble impossible: not actived or wrong username/password!");
	}
}
