package com.musicplayer.core.scrobbler;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.musicplayer.core.Log;
import com.musicplayer.core.config.ConfigFileParser;
import com.musicplayer.core.song.Song;

import de.umass.lastfm.Authenticator;
import de.umass.lastfm.Session;
import de.umass.lastfm.Track;
import de.umass.lastfm.scrobble.ScrobbleData;

/**
 * Class which manage scrobbling on Lastfm
 * 
 * @see Lastfm API
 * @author cyprien
 * 
 */
public class Scrobbler {
	/**
	 * KEY API for Lastfm
	 */
	private final static String KEY = "b00af6e57f866d6ed898336da1b9f836";

	/**
	 * Secret API key for Lastfm
	 */
	private final static String SECRET = "bea5b2dcd02de18c1b77d48fba9e206c";

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

		if (config.getParams("lastfm_username") == null
				|| config.getParams("lastfm_password") == null) { // Scrobbling
																	// not
																	// enabled
			throw new ScrobblerException();
		} else {
			if (Scrobbler.session == null) { // Generate lastfm session
				Logger.getLogger("de.umass.lastfm").setLevel(Level.OFF);

				Scrobbler.session = Authenticator.getMobileSession(
						config.getParams("lastfm_username"),
						config.getParams("lastfm_password"), Scrobbler.KEY,
						Scrobbler.SECRET);

				if (Scrobbler.session == null)
					throw new ScrobblerException();
			}
		}
	}

	/**
	 * Update the Now Playing status on Lastfm
	 * 
	 * @param song
	 *            The song to scrobble
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
	 *            The song to scrobble
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
}
