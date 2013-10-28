package com.musicplayer.core;

import java.io.File;
import java.io.IOException;

import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;
import org.farng.mp3.id3.ID3v1;
import org.farng.mp3.id3.ID3v2_4;

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
	 * ID3 tags values
	 */
	private String title;
	private String album;
	private String artist;
	private String year;
	private String lyrics;

	/**
	 * Song constructor
	 * 
	 * @param path
	 *            path of the song
	 * 
	 */
	public Song(String path) {
		this.path = path;

		// Default value
		this.title = new File(path).getName();
		this.album = "";
		this.artist = "";
		this.year = "1970";
		this.lyrics = "";

		// Get ID3 tag
		try {
			this.updateID3Tags();
		} catch (InvalidFileException e) {
		}

	}

	/**
	 * Update ID3 tag value
	 * 
	 * @see org.farng.mp3
	 */
	public void updateID3Tags() throws InvalidFileException {
		// Get and test file
		File song = new File(path);
		if (!song.exists() || !song.isFile()) {
			throw new InvalidFileException(path);
		} else {
			try {
				MP3File mp3file = new MP3File(song);

				// Get ID3 tags
				if (mp3file.hasID3v2Tag()) {
					ID3v2_4 tag = new ID3v2_4(mp3file.getID3v2Tag());

					this.title = tag.getSongTitle();
					this.album = tag.getAlbumTitle();
					this.artist = tag.getLeadArtist();
					this.year = tag.getYearReleased();
					this.lyrics = tag.getSongLyric();
				} else if (mp3file.hasID3v1Tag()) {
					ID3v1 tag = new ID3v1(mp3file.getID3v1Tag());

					this.title = tag.getSongTitle();
					this.album = tag.getAlbumTitle();
					this.artist = tag.getLeadArtist();
					this.year = tag.getYearReleased();
					this.lyrics = tag.getSongLyric();
				}
			} catch (IOException | TagException e) {
			}

		}
	}

	@Override
	public String toString() {
		return "Song [path=" + path + ", title=" + title + ", album=" + album
				+ ", artist=" + artist + ", year=" + year + ", lyrics="
				+ lyrics + "]";
	}
}
