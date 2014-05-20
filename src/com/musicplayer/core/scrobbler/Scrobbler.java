package com.musicplayer.core.scrobbler;

import com.musicplayer.core.Log;
import com.musicplayer.core.config.ConfigFileParser;
import com.musicplayer.core.song.Song;
import de.umass.lastfm.Authenticator;
import de.umass.lastfm.Session;
import de.umass.lastfm.Track;
import de.umass.lastfm.scrobble.ScrobbleData;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class which manage scrobbling on Lastfm
 *
 * @author cyprien
 * @see Lastfm API
 */
public class Scrobbler {
	/**
	 * KEY API for Lastfm
	 */
	public final static String KEY = "b00af6e57f866d6ed898336da1b9f836";

	/**
	 * Secret API key for Lastfm
	 */
	public final static String SECRET = "bea5b2dcd02de18c1b77d48fba9e206c";

	/**
	 * Store lastfm session
	 */
	private static Session session;

	/**
	 * Init the lastfm scrobbler
	 *
	 * @throws ScrobblerException
	 */
	public static void init() throws ScrobblerException {
		// Get the config
		ConfigFileParser config = ConfigFileParser.getConfigFileParser();

		if (config.getParams("lastfm_session") == null) { // Scrobbling not enabled
			throw new ScrobblerException();
		} else {
			if (Scrobbler.session == null) { // Create lastfm session
				Logger.getLogger("de.umass.lastfm").setLevel(Level.OFF);
				Scrobbler.session = Session.createSession(KEY, SECRET, config.getParams("lastfm_session"));

				if (Scrobbler.session == null)
					throw new ScrobblerException();
			}
		}
	}

	/**
	 * Update the Now Playing status on Lastfm
	 *
	 * @param song
	 * 		The song to scrobble
	 */
	public static void updateNowPlaying(final Song song) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					Scrobbler.init();

					// Update the now playing status
					ScrobbleData scrobbleData = new ScrobbleData(song
							.getArtist(), song.getTitle(), -1, song
							.getDuration(), song.getAlbum(), "", "", -1, "");
					Track.updateNowPlaying(scrobbleData, Scrobbler.session);
				} catch (ScrobblerException e) {
					Log.addEntry(e);
				}
			}
		});
		t.start();
	}

	/**
	 * Scrobble a song on Lastfm
	 *
	 * @param song
	 * 		The song to scrobble
	 */
	public static void scrobble(final Song song) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					Scrobbler.init();
					int now = (int) (System.currentTimeMillis() / 1000);

					// Scrobble the song
					Track.scrobble(song.getArtist(), song.getTitle(), now,
							Scrobbler.session);
				} catch (ScrobblerException e) {
					Log.addEntry(e);
				}
			}
		});
		t.start();
	}

	/**
	 * Get a token from lastfm
	 *
	 * @return the token
	 */
	public static String getoken() {
		Logger.getLogger("de.umass.lastfm").setLevel(Level.OFF);
		return Authenticator.getToken(KEY);
	}

	/**
	 * Get the lastfm session key linked to the given token
	 *
	 * @param token
	 * 		The token used to connect
	 * @return The lastfm session key
	 * @throws ScrobblerException
	 * 		If the token is not valid
	 */
	public static String getSession(String token) throws ScrobblerException {
		try {
			Logger.getLogger("de.umass.lastfm").setLevel(Level.OFF);
			return Authenticator.getSession(token, KEY, SECRET).getKey();
		} catch (Exception e) {
			throw (new ScrobblerException());
		}
	}
}
