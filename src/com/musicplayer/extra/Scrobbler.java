package com.musicplayer.extra;

import com.musicplayer.core.Song;

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

		if (!config.isLastfmStatus()) { // Scrobbling not enabled
			throw new ScrobblerException();
		} else {
			if (Scrobbler.session == null) { // Generate lastfm session
				Scrobbler.session = Authenticator.getMobileSession(
						config.getLastfmUsername(), config.getLastfmPassword(),
						Scrobbler.KEY, Scrobbler.SECRET);
			}
		}
	}

	/**
	 * Update the Now Playing status on Lastfm
	 * 
	 * @param song
	 *            The song to scrobble
	 */
	public static void updateNowPlaying(Song song) {
		try {
			Scrobbler.init();

			// Update the now playing status
			ScrobbleData scrobbleData = new ScrobbleData(song.getArtist(),
					song.getTitle(), -1, song.getDuration(), song.getAlbum(),
					"", "", -1, "");
			Track.updateNowPlaying(scrobbleData, Scrobbler.session);
		} catch (ScrobblerException e) {
		}
	}

	/**
	 * Scrobble a song on Lastfm
	 * 
	 * @param song
	 *            The song to scrobble
	 */
	public static void scrobble(Song song) {
		try {
			Scrobbler.init();
			int now = (int) (System.currentTimeMillis() / 1000);

			// Scrobble the song
			Track.scrobble(song.getArtist(), song.getTitle(), now,
					Scrobbler.session);
		} catch (ScrobblerException e) {
		}
	}
}