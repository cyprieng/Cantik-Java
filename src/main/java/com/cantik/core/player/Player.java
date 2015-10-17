package com.cantik.core.player;

/**
 * Interface of a player
 *
 * @author cyprien
 */
public interface Player {
	/**
	 * Start playing the player
	 */
	public void play();

	/**
	 * Pause the player
	 */
	public void pause();

	/**
	 * Stop the player
	 */
	public void stop();

	/**
	 * Skip n percent of the song
	 *
	 * @param percent
	 * 		The percent to skip
	 */
	public void skip(int percent);

	/**
	 * Get the file which is playing
	 *
	 * @return the path of the file
	 */
	public String getFile();

	/**
	 * Get the state of the player
	 *
	 * @return the state of the player
	 * @see PlayerState
	 */
	public PlayerState getState();
}
