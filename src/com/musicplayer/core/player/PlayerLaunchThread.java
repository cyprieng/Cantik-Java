package com.musicplayer.core.player;

/**
 * Thread for launching launcher method of Player
 * 
 * @author cyprien
 * 
 */
public class PlayerLaunchThread extends Thread {
	/**
	 * Thread run function: launch "launcher" method of Player
	 */
	public void run() {
		Player p = new Player();
		p.launcher();
	}
}
