package com.cantik.core.musiclibrary;

import com.cantik.core.Core;
import com.cantik.core.config.ObjectFileWriter;
import com.cantik.core.scrobbler.ScrobblerConfig;
import com.cantik.core.song.Song;
import de.umass.lastfm.Album;
import de.umass.lastfm.Artist;
import de.umass.lastfm.ImageSize;
import de.umass.lastfm.Track;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Get info of an Artist
 *
 * @author cyprien
 */
public class ArtistInfo {
	/**
	 * Logger for the class
	 */
	private static Logger logger = Logger.getLogger(ArtistInfo.class.getName());

	/**
	 * Store albums cover
	 */
	private static HashMap<String, ImageIcon> albumCover = new HashMap<String, ImageIcon>();

	/**
	 * Store artists img
	 */
	private static HashMap<String, ImageIcon> artistImg = new HashMap<String, ImageIcon>();

	/**
	 * Default artist image
	 */
	private static ImageIcon defaultArtistImage;

	/**
	 * Default album image
	 */
	private static ImageIcon defaultAlbumImage;

	// Retrieve cover from file
	static {
		try {
			albumCover = (HashMap<String, ImageIcon>) ObjectFileWriter.get(new File(Core.getUserPath()
					+ "albumcover"));

			artistImg = (HashMap<String, ImageIcon>) ObjectFileWriter.get(new File(Core.getUserPath()
					+ "artistimg"));
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage());
		}
	}

	/**
	 * Get the default image for an artist
	 *
	 * @return the Image
	 */
	public static ImageIcon getDefaultArtistImage() {
		if (defaultArtistImage != null)
			return defaultArtistImage;

		try {
			// Load and store image
			defaultArtistImage = new ImageIcon(ImageIO.read(new File("assets/img/person.png")));
			return defaultArtistImage;
		} catch (IOException e) {
			logger.log(Level.WARNING, e.getMessage());
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
	public static ImageIcon getArtistImage(String artist) {
		if (artistImg.get(artist) != null)
			return artistImg.get(artist);

		try {
			// Get image url from lastfm
			Logger.getLogger("de.umass.lastfm").setLevel(Level.OFF);
			Artist a = Artist.getInfo(artist, ScrobblerConfig.KEY);
			String imgURL = a.getImageURL(ImageSize.MEDIUM);

			// Load and store image
			URL url = new URL(imgURL);
			ImageIcon image = new ImageIcon(ImageIO.read(url).getScaledInstance(50, 50, Image.SCALE_SMOOTH));
			artistImg.put(artist, image);
			return image;
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage());

			// Default image
			return getDefaultArtistImage();
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
	public static ImageIcon getAlbumImage(String artist, String album) {
		// Check if it was already stored
		if (albumCover.get(artist + album) != null) {
			return albumCover.get(artist + album);
		}

		// Get image from song
		MusicLibrary ml = MusicLibrary.getMusicLibrary();
		Song s = ml.getSongs(artist, album).iterator().next();
		if (s.getCover() != null) {
			ImageIcon ii = new ImageIcon(s.getCover().getScaledInstance(86, 86, Image.SCALE_SMOOTH));
			albumCover.put(artist + album, ii); // Store for next time
			return ii;
		}

		// Get cover from lastfm
		try {
			// Get url from lastfm
			Logger.getLogger("de.umass.lastfm").setLevel(Level.OFF);
			Album a = Album.getInfo(artist, album, ScrobblerConfig.KEY);
			String imgURL = a.getImageURL(ImageSize.MEDIUM);

			// Load and store image
			URL url = new URL(imgURL);
			ImageIcon image = new ImageIcon(ImageIO.read(url).getScaledInstance(86, 86, Image.SCALE_SMOOTH));
			albumCover.put(artist, image);
			return image;
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage());
		}

		// Default image
		return getDefaultAlbumImage();
	}

	/**
	 * Get album image of specified size
	 *
	 * @param artist
	 * 		Artist name
	 * @param album
	 * 		Album title
	 * @param dim
	 * 		Dimension of the image
	 * @return ImageIcon of the album
	 */
	public static ImageIcon getAlbumImage(String artist, String album, Dimension dim) {
		return new ImageIcon(getAlbumImage(artist, album).getImage().getScaledInstance(dim.width, dim.height, Image.SCALE_SMOOTH));
	}

	/**
	 * Get the default album cover
	 *
	 * @return an Image with the default album cover
	 */
	public static ImageIcon getDefaultAlbumImage() {
		if (defaultAlbumImage != null)
			return defaultAlbumImage;

		try {
			// Load and store image
			defaultAlbumImage = new ImageIcon(ImageIO.read(new File("assets/img/cover.png"))
					.getScaledInstance(86, 86, Image.SCALE_SMOOTH));
			return defaultAlbumImage;
		} catch (IOException e) {
			logger.log(Level.WARNING, e.getMessage());
			return null;
		}
	}

	/**
	 * Get the default album cover of the specified size
	 *
	 * @param dim
	 * 		Dimension of the image
	 * @return an Image with the default album cover
	 */
	public static ImageIcon getDefaultAlbumImage(Dimension dim) {
		return new ImageIcon(getDefaultAlbumImage().getImage().getScaledInstance(dim.width, dim.height, Image.SCALE_SMOOTH));
	}

	/**
	 * Save cover to file
	 */
	public static void save() {
		try {
			ObjectFileWriter.store(albumCover, new File(Core.getUserPath()
					+ "albumcover"));

			ObjectFileWriter.store(artistImg, new File(Core.getUserPath()
					+ "artistimg"));
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage());
		}
	}

	/**
	 * Get biography of an artist
	 *
	 * @param artist
	 * 		Name of the artist
	 * @return String with the biography
	 */
	public static String getBio(String artist) {
		Logger.getLogger("de.umass.lastfm").setLevel(Level.OFF);
		return Artist.getInfo(artist, Locale.getDefault(), "", ScrobblerConfig.KEY).getWikiText();
	}

	/**
	 * Get artist number of auditors
	 *
	 * @param artist
	 * 		Artist name
	 * @return Number of auditors (lastfm)
	 */
	public static int getNbAuditors(String artist) {
		Logger.getLogger("de.umass.lastfm").setLevel(Level.OFF);
		return Artist.getInfo(artist, ScrobblerConfig.KEY).getListeners();
	}

	/**
	 * Get URL of the artist image
	 *
	 * @param artist
	 * 		Artist name
	 * @return String with the URL (lastfm)
	 */
	public static String getArtistImageURL(String artist) {
		Logger.getLogger("de.umass.lastfm").setLevel(Level.OFF);
		return Artist.getInfo(artist, ScrobblerConfig.KEY).getImageURL(ImageSize.EXTRALARGE);
	}

	/**
	 * Get artist top tracks
	 *
	 * @param artist
	 * 		Artist name
	 * @return Collection of top Track (lastfm)
	 */
	public static Collection<Track> getTopTracks(String artist) {
		Logger.getLogger("de.umass.lastfm").setLevel(Level.OFF);
		return Artist.getTopTracks(artist, ScrobblerConfig.KEY);
	}
}
