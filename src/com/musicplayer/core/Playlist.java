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
	private static List<Song> songList;

	/**
	 * Constructor of Playlist. Init witch an empty songList
	 */
	private Playlist() {
		Playlist.songList = new LinkedList<Song>();
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
	public static void addSong(Song song) {
		Playlist.songList.add(song);
	}

	/**
	 * Remove the song at the given index
	 * 
	 * @param index
	 *            Index of the song to remove
	 */
	public static void removeSong(int index) {
		Playlist.songList.remove(index);
	}

	/**
	 * Empty the song list
	 */
	public static void reset() {
		Playlist.songList.removeAll(songList);
	}

	/**
	 * Randomize the song list
	 */
	public static void randomize() {
		Collections.shuffle(songList);
	}

	@Override
	public String toString() {
		String out = "";
		ListIterator<Song> li = Playlist.songList.listIterator();

		while (li.hasNext())
			out += li.next() + "\n";
		
		return "Playlist: \n"+ out;
	}
}
