package com.musicplayer.core.musiclibrary;

import com.musicplayer.core.Log;
import com.musicplayer.core.scrobbler.ScrobblerConfig;
import com.musicplayer.core.song.Song;
import de.umass.lastfm.Album;
import de.umass.lastfm.Artist;
import de.umass.lastfm.ImageSize;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Get info of an Artist
 *
 * @author cyprien
 */
public class ArtistInfo {
	/**
	 * Store albums cover
	 */
	private static HashMap<String, Image> albumCover = new HashMap<String, Image>();

	/**
	 * Store artists img
	 */
	private static HashMap<String, Image> artistImg = new HashMap<String, Image>();

	/**
	 * Default artist image
	 */
	private static Image defaultArtistImage;

	/**
	 * Default album image
	 */
	private static Image defaultAlbumImage;

	/**
	 * Get the default image for an artist
	 *
	 * @return the Image
	 */
	public static Image getDefaultArtistImage() {
		if (defaultArtistImage != null)
			return defaultArtistImage;

		try {
			// Load and store image
			defaultArtistImage = ImageIO.read(new File("assets/img/person.png"));
			return defaultArtistImage;
		} catch (IOException e) {
			Log.addEntry(e);
			return null;
		}
	}

	/**
	 * Get the image of an artist
	 *
	 * @param artist
	 * 		Name of the artist
	 * @return The Image
	 */
	public static Image getArtistImage(String artist) {
		if (artistImg.get(artist) != null)
			return artistImg.get(artist);

		try {
			// Get image url from lastfm
			Logger.getLogger("de.umass.lastfm").setLevel(Level.OFF);
			Artist a = Artist.getInfo(artist, ScrobblerConfig.KEY);
			String imgURL = a.getImageURL(ImageSize.MEDIUM);

			// Load and store image
			URL url = new URL(imgURL);
			Image image = ImageIO.read(url).getScaledInstance(50, 50, Image.SCALE_SMOOTH);
			artistImg.put(artist, image);
			return image;
		} catch (Exception e) {
			Log.addEntry(e);
			return null;
		}
	}

	/**
	 * Get the album cover
	 *
	 * @param artist
	 * 		Artist of the album
	 * @param album
	 * 		Album name
	 * @return The cover of the album
	 */
	public static Image getAlbumImage(String artist, String album) {
		// Check if it was already stored
		if (albumCover.get(artist + album) != null) {
			return albumCover.get(artist + album);
		}

		// Get image from song
		MusicLibrary ml = MusicLibrary.getMusicLibrary();
		Song s = ml.getSongs(artist, album).iterator().next();
		if (s.getCover() != null) {
			albumCover.put(artist + album,
					s.getCover().getScaledInstance(50, 50, Image.SCALE_SMOOTH)); // Store for next time
			return s.getCover().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		}

		// Get cover from lastfm
		try {
			// Get url from lastfm
			Logger.getLogger("de.umass.lastfm").setLevel(Level.OFF);
			Album a = Album.getInfo(artist, album, ScrobblerConfig.KEY);
			String imgURL = a.getImageURL(ImageSize.MEDIUM);

			// Load and store image
			URL url = new URL(imgURL);
			Image image = ImageIO.read(url).getScaledInstance(50, 50, Image.SCALE_SMOOTH);
			albumCover.put(artist, image);
			return image;
		} catch (Exception e) {
			Log.addEntry(e);
		}

		// Default image
		try {
			albumCover.put(artist + album,
					ImageIO.read(new File("assets/img/cover.png"))
							.getScaledInstance(50, 50, Image.SCALE_SMOOTH)
			); // Store for next time
			return ImageIO.read(new File("assets/img/cover.png"))
					.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		} catch (IOException e) {
			Log.addEntry(e);
			return null;
		}
	}

	/**
	 * Get the default album cover
	 *
	 * @return an Image with the default album cover
	 */
	public static Image getDefaultAlbumImage() {
		if (defaultAlbumImage != null)
			return defaultAlbumImage;

		try {
			// Load and store image
			defaultAlbumImage = ImageIO.read(new File("assets/img/cover.png"))
					.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
			return defaultAlbumImage;
		} catch (IOException e) {
			Log.addEntry(e);
			return null;
		}
	}
}
