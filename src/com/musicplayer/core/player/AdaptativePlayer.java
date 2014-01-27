package com.musicplayer.core.player;

import java.util.Observable;
import java.util.Observer;

/**
 * Class managing all format playback
 * 
 * @author cyprien
 * 
 */
public class AdaptativePlayer extends Observable implements Player, Observer {
	/**
	 * Store the player
	 */
	private Player player;

	/**
	 * Constructor: detect file type, and create the appropriate player
	 * 
	 * @param str
	 *            The path of the file
	 */
	public AdaptativePlayer(String str) {
		// Get format
		String fileExt = str.substring(str.lastIndexOf(".") + 1, str.length())
				.toUpperCase();

		// Switch format
		if (fileExt.equals("MP3")) {
			MP3Player p = new MP3Player(str);
			p.addObserver(this);
			player = p;
		} else if (fileExt.equals("FLAC")) {
			FLACPlayer p = new FLACPlayer(str);
			p.addObserver(this);
			player = p;
		} else if (fileExt.equals("OGG")) {
			OGGPlayer p = new OGGPlayer(str);
			p.addObserver(this);
			player = p;
		}
	}

	@Override
	public void play() {
		player.play();
	}

	@Override
	public void pause() {
		player.pause();
	}

	@Override
	public void stop() {
		player.stop();
	}

	@Override
	public String getFile() {
		return player.getFile();
	}

	@Override
	public PlayerState getState() {
		return player.getState();
	}

	@Override
	public void update(Observable player, Object arg) {
		setChanged();
		notifyObservers();
	}
}
