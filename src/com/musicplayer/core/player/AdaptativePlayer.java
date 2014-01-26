package com.musicplayer.core.player;

/**
 * Class managing all format playback
 * 
 * @author cyprien
 * 
 */
public class AdaptativePlayer implements Player {
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
		if (fileExt.equals("MP3"))
			player = new MP3Player(str);
		else if (fileExt.equals("FLAC"))
			player = new FLACPlayer(str);
		else if (fileExt.equals("OGG"))
			player = new OGGPlayer(str);
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

}
