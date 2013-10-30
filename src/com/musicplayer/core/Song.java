package com.musicplayer.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.farng.mp3.MP3File;
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
	private String title, album, artist, year, lyric;

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

			this.updateID3Tags();
		}

	}

	/**
	 * Update ID3 tag value
	 * 
	 * @see org.farng.mp3
	 */
	public void updateID3Tags() {
		try {
			File song = new File(path);
			MP3File mp3file = new MP3File(song);

			// Get ID3 tags
			if (mp3file.hasID3v1Tag()) {
				ID3v1 tag = new ID3v1(mp3file.getID3v1Tag());

				this.title = tag.getSongTitle();
				this.album = tag.getAlbumTitle();
				this.artist = tag.getLeadArtist();
				this.year = tag.getYearReleased();

				try {
					this.lyric = tag.getSongLyric();
				} catch (UnsupportedOperationException e) {
				}

			} else if (mp3file.hasID3v2Tag()) {
				ID3v2_4 tag = new ID3v2_4(mp3file.getID3v2Tag());

				this.title = tag.getSongTitle();
				this.album = tag.getAlbumTitle();
				this.artist = tag.getLeadArtist();
				this.year = tag.getYearReleased();

				try {
					this.lyric = tag.getSongLyric();
				} catch (UnsupportedOperationException e) {
				}
			}
		} catch (Exception e) {
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

	@Override
	public String toString() {
		return "Song [path=" + path + ", title=" + title + ", album=" + album
				+ ", artist=" + artist + ", year=" + year + ", lyric=" + lyric
				+ "]";
	}
}
