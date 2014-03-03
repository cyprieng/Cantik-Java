package com.musicplayer.core.musiclibrary;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import com.musicplayer.core.Log;
import com.musicplayer.core.song.Song;

/**
 * Get info of an Artist
 * 
 * @author cyprien
 * 
 */
public class ArtistInfo {
	/**
	 * Store albums cover
	 */
	private static HashMap<String, Image> albumCover = new HashMap<String, Image>();

	/**
	 * Image of artists
	 */
	private static Image artistImage;

	/**
	 * Get the image for an artist
	 * 
	 * @return the Image
	 */
	public static Image getArtistImage() {
		if (artistImage != null)
			return artistImage;

		try {
			// Load and store image
			artistImage = ImageIO.read(new File("assets/img/person.png"));
			return artistImage;
		} catch (IOException e) {
			Log.addEntry(e);
			return null;
		}
	}

	/**
	 * Get the album cover
	 * 
	 * @param artist
	 *            Artist of the album
	 * @param album
	 *            Album name
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
					s.getCover().getScaledInstance(50, 50, Image.SCALE_SMOOTH)); // Store
																					// for
																					// next
																					// time
			return s.getCover().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		}

		// Default image
		try {
			albumCover.put(artist + album,
					ImageIO.read(new File("assets/img/cover.png"))
							.getScaledInstance(50, 50, Image.SCALE_SMOOTH)); // Store
																				// for
																				// next
																				// time
			return ImageIO.read(new File("assets/img/cover.png"))
					.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		} catch (IOException e) {
			Log.addEntry(e);
			return null;
		}
	}
}
