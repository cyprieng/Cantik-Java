package com.musicplayer.core;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Class to store and manage playlist. It uses singleton design pattern.
 * 
 * @author cyprien
 * 
 */
public class Playlist {
	/**
	 * Unique Playlist instance
	 */
	private static Playlist playlist;

	/**
	 * List of Song
	 */
	private List<Song> songList;

	/**
	 * Constructor of Playlist. Init witch an empty songList
	 */
	private Playlist() {
		this.songList = new LinkedList<Song>();
	}

	/**
	 * Get the unique Playlist instance
	 * 
	 * @return the unique Playlist instance
	 */
	public static Playlist getPlaylist() {
		if (playlist == null) {
			playlist = new Playlist();
		}
		return playlist;
	}

	/**
	 * Add a song to the playlist
	 * 
	 * @param song
	 *            The Song to add to the list
	 */
	public void addSong(Song song) {
		this.songList.add(song);
	}

	/**
	 * Get the Song at the given index
	 * 
	 * @param index
	 *            Index of the song to retrieve
	 * @return the selected song
	 */
	public Song getSong(int index) {
		return this.songList.get(index);
	}

	/**
	 * Remove the song at the given index
	 * 
	 * @param index
	 *            Index of the song to remove
	 */
	public void removeSong(int index) {
		this.songList.remove(index);
	}

	/**
	 * Empty the song list
	 */
	public void reset() {
		this.songList.removeAll(songList);
	}

	/**
	 * Randomize the song list
	 */
	public void randomize() {
		Collections.shuffle(this.songList);
	}

	@Override
	public String toString() {
		String out = "";
		ListIterator<Song> li = this.songList.listIterator();

		while (li.hasNext())
			out += li.next() + "\n";

		return "Playlist: \n" + out;
	}
}
