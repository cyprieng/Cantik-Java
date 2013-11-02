package com.musicplayer.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import com.musicplayer.player.Player;

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
	 * Current track index
	 */
	private int curentTrack;

	/**
	 * Constructor of Playlist. Init witch an empty songList
	 */
	private Playlist() {
		this.songList = new LinkedList<Song>();
		this.curentTrack = 0;
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
	 * Add an ArrayList of song to the playlist
	 * 
	 * @param songArray
	 *            The ArrayList of song to add
	 */
	public void addSongArray(ArrayList<Song> songArray) {
		for (int i = 0; i < songArray.size(); i++) {
			this.addSong(songArray.get(i));
		}
	}

	/**
	 * Get the Song at the given index
	 * 
	 * @param index
	 *            Index of the song to retrieve
	 * @return the selected song or null if it not exists
	 */
	public Song getSong(int index) {
		try {
			return this.songList.get(index);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
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

	/**
	 * Start playing the playlist
	 */
	public void play() {
		if (this.getSong(0) != null) { // Song exists
			// Play
			Player.setFile(this.getSong(0).getPath());
			Player.play();

			this.curentTrack = 0;
		} else {
			Player.stopPlaying();
		}
	}

	/**
	 * Start playing the playlist from the selected song
	 * 
	 * @param index
	 *            Index of the first song to play
	 */
	public void play(int index) {
		if (this.getSong(index) != null) { // Song exists
			// Play
			Player.setFile(this.getSong(index).getPath());
			Player.play();

			this.curentTrack = index;
		} else {
			Player.stopPlaying();
		}
	}

	/**
	 * Jump to next track
	 */
	public void next() {
		this.play(this.curentTrack + 1);
	}

	/**
	 * Jump to last track
	 */
	public void back() {
		this.play(this.curentTrack - 1);
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
