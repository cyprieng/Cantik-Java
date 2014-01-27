package com.musicplayer.core.playlist;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import com.musicplayer.core.player.AdaptativePlayer;
import com.musicplayer.core.player.Player;
import com.musicplayer.core.player.PlayerState;
import com.musicplayer.core.scrobbler.Scrobbler;
import com.musicplayer.core.song.Song;

/**
 * Class to store and manage playlist. It uses singleton design pattern.
 * 
 * @author cyprien
 * 
 */
public class Playlist implements Observer {
	/**
	 * Unique Playlist instance
	 */
	private static Playlist playlist;

	/**
	 * Store the current player
	 */
	private Player player;

	/**
	 * List of Song
	 */
	private List<Song> songList;

	/**
	 * Current track index
	 */
	private int curentTrack;

	/**
	 * Random status
	 */
	private boolean random;

	/**
	 * Repeat status
	 */
	private RepeatState repeat;

	/**
	 * Constructor of Playlist. Init witch an empty songList
	 */
	private Playlist() {
		this.songList = new LinkedList<Song>();
		this.curentTrack = 0;
		this.random = false;
		this.repeat = RepeatState.OFF;
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
	 * Add a Set of song to the playlist
	 * 
	 * @param songSet
	 *            The Set of song to add
	 */
	public void addSongSet(Set<Song> songSet) {
		for (Song s : songSet)
			this.addSong(s);
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
	 * Start playing the playlist
	 */
	public void play() {
		this.play(0);
	}

	/**
	 * Start playing the playlist from the selected song
	 * 
	 * @param index
	 *            Index of the first song to play
	 */
	public void play(int index) {
		if (this.getSong(index) != null) { // Song exists
			// Stop last song
			if (player != null)
				player.stop();

			// Play
			AdaptativePlayer ap = new AdaptativePlayer(this.getSong(index)
					.getPath());
			ap.addObserver(this);
			player = ap;
			player.play();

			// Scrobble
			Scrobbler.updateNowPlaying(this.getSong(index));

			this.curentTrack = index;
		} else {
			player.stop();
		}
	}

	/**
	 * Jump to next track
	 */
	public void next() {
		if (this.repeat == RepeatState.SONG) { // Repeat song
			this.play(this.curentTrack);
		} else {
			if (random) { // Random song
				this.play((int) (Math.random() * (this.songList.size() + 1)));
			} else {
				if (this.curentTrack + 1 >= this.songList.size()) { // Playlist
																	// finished
					if (this.repeat == RepeatState.ALL) { // Replay all the
															// playlist
						this.play(0);
					}
				} else { // Play next track
					this.play(this.curentTrack + 1);
				}
			}
		}
	}

	/**
	 * Jump to last track
	 */
	public void back() {
		this.play(this.curentTrack - 1);
	}

	/**
	 * Set the random status
	 * 
	 * @param random
	 *            True to enable random, false to disable
	 */
	public void setRandom(boolean random) {
		this.random = random;
	}

	/**
	 * Set the repeat status
	 * 
	 * @param repeat
	 *            The repeat state
	 * @see RepeatState
	 */
	public void setRepeat(RepeatState repeat) {
		this.repeat = repeat;
	}

	/**
	 * Get the actual player
	 * 
	 * @return The player
	 * @see com.musicplayer.core.player.Player
	 */
	public Player getPlayer() {
		return player;
	}

	@Override
	public String toString() {
		String out = "";
		ListIterator<Song> li = this.songList.listIterator();

		while (li.hasNext())
			out += li.next() + "\n";

		return "Playlist: \n" + out;
	}

	@Override
	public void update(Observable player, Object arg) {
		// Go to next track at the end
		if (((Player) player).getState() == PlayerState.FNISHED) {
			// Scrobble
			Scrobbler.scrobble(this.getSong(curentTrack));
			this.next();
		}
	}
}
