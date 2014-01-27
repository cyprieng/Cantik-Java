package com.musicplayer.core.player;

/**
 * Interface of a player
 * 
 * @author cyprien
 * 
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
