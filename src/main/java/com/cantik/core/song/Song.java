package com.cantik.core.song;

import com.cantik.core.InvalidFileException;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class which stores data about a song: path and ID3 tag
 *
 * @author cyprien
 */
public class Song implements Serializable {
	private static final long serialVersionUID = -2359300133101740540L;

	/**
	 * Logger for the class
	 */
	private static Logger logger = Logger.getLogger(Song.class.getName());

	/**
	 * Path of the song
	 */
	private String path;

	/**
	 * Tags values
	 */
	private String title, album, artist, year, lyric;

	/**
	 * Cover of the song
	 */
	private BufferedImage cover;

	/**
	 * Song duration in second
	 */
	private int duration;

	/**
	 * Song constructor
	 *
	 * @param path
	 * 		path of the song
	 */
	public Song(String path) throws InvalidFileException {
		// Test file
		File song = new File(path);
		if (!song.exists() || !song.isFile() || !checkExtension(path)) {
			throw new InvalidFileException(path);
		} else {
			this.path = path;

			// Default value
			this.title = (new File(path).getName()).replaceFirst("[.][^.]+$",
					"");
			this.album = "unknown";
			this.artist = "unknown";
			this.year = "1970";
			this.lyric = "";
			this.duration = 0;

			// Get tags
			this.updateTags();
		}

	}

	/**
	 * Update tags value
	 *
	 * @see org.jaudiotagger
	 */
	public void updateTags() {
		try {
			// Disable log from jaudiotagger
			Logger.getLogger("org.jaudiotagger").setLevel(Level.OFF);

			// Read file
			AudioFile f = AudioFileIO.read(new File(this.path));
			Tag tag = f.getTag();

			// Read tags
			if (!"".equals(tag.getFirst(FieldKey.TITLE)))
				this.title = tag.getFirst(FieldKey.TITLE);
			if (!"".equals(tag.getFirst(FieldKey.ALBUM)))
				this.album = tag.getFirst(FieldKey.ALBUM);
			if (!"".equals(tag.getFirst(FieldKey.ARTIST)))
				this.artist = tag.getFirst(FieldKey.ARTIST);
			if (!"".equals(tag.getFirst(FieldKey.YEAR)))
				this.year = tag.getFirst(FieldKey.YEAR);
			if (!"".equals(tag.getFirst(FieldKey.LYRICS)))
				this.lyric = tag.getFirst(FieldKey.LYRICS);

			this.duration = f.getAudioHeader().getTrackLength();
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage());
		}
	}

	/**
	 * Retrieve lyric from web API if no lyric defined in ID3 tags
	 *
	 * @return lyric in a String
	 * @see http://api.ntag.fr/lyrics/doc.php
	 */
	public String getLyric() {
		if (this.lyric.isEmpty()) { // No ID3 tag value
			StringBuilder lyric = new StringBuilder();
			try {
				// Get lyric from API
				URL url = new URL("http://api.ntag.fr/lyrics/?artist="
						+ URLEncoder.encode(this.artist, "UTF-8") + "&title=" + URLEncoder.encode(this.title, "UTF-8"));

				BufferedReader in = new BufferedReader(new InputStreamReader(
						url.openStream(), "UTF-8"));
				String str;
				while ((str = in.readLine()) != null) {
					lyric.append(str).append("\r\n");
				}
				in.close();
			} catch (IOException e) {
				logger.log(Level.WARNING, e.getMessage());
			}

			this.lyric = lyric.toString(); // Store lyric for next time
			return lyric.toString();
		} else { // Return ID3 tag value
			return this.lyric;
		}
	}

	/**
	 * Check if the song has a correct extension
	 *
	 * @param filename
	 * 		The path of the file
	 * @return true if it is a correct extension
	 */
	public static boolean checkExtension(String fileName) {
		// Get extension
		String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1,
				fileName.length()).toUpperCase();

		// Check if it is in SongExtension enum
		for (SongExtension ext : SongExtension.values()) {
			if (ext.name().equals(fileExt)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Retrieve the song's path
	 *
	 * @return the path of the song
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Get the song title
	 *
	 * @return Title of the song
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Get the album title
	 *
	 * @return Album title
	 */
	public String getAlbum() {
		return album;
	}

	/**
	 * Get the Artist name
	 *
	 * @return Artist name
	 */
	public String getArtist() {
		return artist;
	}

	/**
	 * Get the year of the song
	 *
	 * @return Year of the song
	 */
	public String getYear() {
		return year;
	}

	/**
	 * Get the duration of the song
	 *
	 * @return Duration in second
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * Get the cover of the Song from the Tag
	 *
	 * @return BufferedImage of the cover
	 * @see BufferedImage
	 */
	public BufferedImage getCover() {
		if (cover == null) {
			// Disable log from jaudiotagger
			Logger.getLogger("org.jaudiotagger").setLevel(Level.OFF);

			try {
				// Read tags
				AudioFile f = AudioFileIO.read(new File(this.path));
				Tag tag = f.getTag();

				// Get cover
				if (tag.getFirstArtwork() != null)
					this.cover = (BufferedImage) tag.getFirstArtwork()
							.getImage();

			} catch (CannotReadException | IOException | TagException
					| ReadOnlyFileException | InvalidAudioFrameException e) {
				logger.log(Level.WARNING, e.getMessage());
			}
		}

		return cover;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Song song = (Song) o;

		return path.equals(song.path);
	}

	@Override
	public int hashCode() {
		return path.hashCode();
	}

	@Override
	public String toString() {
		return "Song [path=" + path + ", title=" + title + ", album=" + album
				+ ", artist=" + artist + ", year=" + year + ", duration="
				+ duration + "]";
	}
}
