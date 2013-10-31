package com.musicplayer.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

/**
 * Class which stores data about a song: path and ID3 tag
 * 
 * @author cyprien
 * 
 */
public class Song {
	/**
	 * Path of the song
	 */
	private String path;

	/**
	 * Tags values
	 */
	private String title, album, artist, year, lyric;

	/**
	 * Song duration in second
	 */
	private int duration;

	/**
	 * Song constructor
	 * 
	 * @param path
	 *            path of the song
	 * 
	 */
	public Song(String path) throws InvalidFileException {
		// Test file
		File song = new File(path);
		if (!song.exists() || !song.isFile()) {
			throw new InvalidFileException(path);
		} else {
			this.path = path;

			// Default value
			this.title = new File(path).getName();
			this.album = "";
			this.artist = "";
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
	 * @see jaudiotagger
	 */
	public void updateTags() {
		try {
			// Read file
			AudioFile f = AudioFileIO.read(new File(this.path));
			Tag tag = f.getTag();

			// Read tags
			this.title = tag.getFirst(FieldKey.TITLE);
			this.album = tag.getFirst(FieldKey.ALBUM);
			this.artist = tag.getFirst(FieldKey.ARTIST);
			this.year = tag.getFirst(FieldKey.YEAR);
			this.lyric = tag.getFirst(FieldKey.LYRICS);
			this.duration = f.getAudioHeader().getTrackLength();
		} catch (CannotReadException | IOException | TagException
				| ReadOnlyFileException | InvalidAudioFrameException e) {
		}
	}

	/**
	 * Retrieve lyric from web API if no lyric defined in ID3 tags
	 * 
	 * @see http://api.ntag.fr/lyrics/doc.php
	 * @return lyric in a String
	 */
	public String getLyric() {
		if (this.lyric == "") { // No ID3 tag value
			String lyric = "";
			try {
				// Get lyric from API
				URL url = new URL("http://api.ntag.fr/lyrics/?artist="
						+ this.artist + "&title=" + this.title);
				BufferedReader in = new BufferedReader(new InputStreamReader(
						url.openStream()));
				String str;
				while ((str = in.readLine()) != null) {
					lyric += str + "\n";
				}
				in.close();
			} catch (IOException e) {
			}

			this.lyric = lyric; // Store lyric for next time
			return lyric;

		} else { // Return ID3 tag value
			return this.lyric;
		}
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

	@Override
	public String toString() {
		return "Song [path=" + path + ", title=" + title + ", album=" + album
				+ ", artist=" + artist + ", year=" + year + ", lyric=" + lyric
				+ ", duration=" + duration + "]";
	}
}
