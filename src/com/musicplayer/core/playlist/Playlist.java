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
public class Playlist extends Observable implements Observer {
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
	private int currentTrack;

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
		this.currentTrack = 0;
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

		setChanged();
		notifyObservers();
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

		setChanged();
		notifyObservers();
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

		if (index < currentTrack)
			currentTrack--;
		else if (index == currentTrack)
			next();

		setChanged();
		notifyObservers();
	}

	/**
	 * Empty the song list
	 */
	public void reset() {
		this.songList.removeAll(songList);

		setChanged();
		notifyObservers();
	}

	/**
	 * Start playing the playlist
	 */
	public void play() {
		if (this.player != null && this.player.getState() == PlayerState.PAUSED)
			// Player paused => resume
			this.player.play();
		else
			// Start the current song
			this.play(currentTrack);

		setChanged();
		notifyObservers();
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

			this.currentTrack = index;
		} else {
			player.stop();
		}

		setChanged();
		notifyObservers("new track");
	}

	/**
	 * Jump to next track
	 */
	public void next() {
		if (this.repeat == RepeatState.SONG) { // Repeat song
			this.play(this.currentTrack);
		} else {
			if (random) { // Random song
				this.play((int) (Math.random() * (this.songList.size() + 1)));
			} else {
				if (this.currentTrack + 1 >= this.songList.size()) { // Playlist
																		// finished
					if (this.repeat == RepeatState.ALL) { // Replay all the
															// playlist
						this.play(0);
					} else {
						this.player.stop();
					}
				} else { // Play next track
					this.play(this.currentTrack + 1);
				}
			}
		}
	}

	/**
	 * Jump to last track
	 */
	public void back() {
		this.play(this.currentTrack - 1);
	}

	/**
	 * Reorder the playlist. Move song at fromIndex to toIndex.
	 * 
	 * @param fromIndex
	 *            The index of the song to move
	 * @param toIndex
	 *            The index where the song will be after
	 */
	public void reorder(int fromIndex, int toIndex) {
		// Get the song and delete it from the list
		Song s = songList.get(fromIndex);
		songList.remove(fromIndex);

		// Update currentTrack index
		if (fromIndex > currentTrack && toIndex < currentTrack)
			currentTrack++;
		else if (fromIndex < currentTrack && toIndex > currentTrack)
			currentTrack--;
		else if (fromIndex == currentTrack)
			currentTrack = toIndex;

		// Add the song to the right place
		if (toIndex > songList.size() - 1) {
			songList.add(s);
			currentTrack = songList.size() - 1;
		} else
			songList.add(toIndex, s);

		setChanged();
		notifyObservers();
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
	 * Get the song which is playing
	 * 
	 * @return The current Song
	 * @see com.musicplayer.song.Song
	 */
	public Song getCurrentSong() {
		return this.getSong(currentTrack);
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

	/**
	 * Get the repeat state
	 * 
	 * @return The repeat state
	 * @see RepeatState
	 */
	public RepeatState getRepeat() {
		return repeat;
	}

	/**
	 * Return if the player is random
	 * 
	 * @return True if yes, false otherwise
	 */
	public boolean isRandom() {
		return random;
	}

	/**
	 * Get the list of the songs in the playlist
	 * 
	 * @return a List of the songs
	 */
	public List<Song> getSongList() {
		return songList;
	}

	/**
	 * Get current track index
	 * 
	 * @return the index of the current track
	 */
	public int getCurrentTrack() {
		return currentTrack;
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
			Scrobbler.scrobble(this.getSong(currentTrack));
			this.next();
		}

		setChanged();
		notifyObservers();
	}
}
