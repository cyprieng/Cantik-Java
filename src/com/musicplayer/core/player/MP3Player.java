package com.musicplayer.core.player;

import com.musicplayer.core.Log;

/**
 * Class managing MP3 playback
 * 
 * @author cyprien
 * 
 */
public class MP3Player implements Runnable, Player {
	/**
	 * Path of the song
	 */
	private String file;

	/**
	 * State of the player
	 */
	private PlayerState state;

	/**
	 * The player strictly speaking
	 */
	private javazoom.jl.player.Player player;

	/**
	 * The thread launching the player
	 */
	private Thread playerThread;

	/**
	 * Constructor: initialize the player
	 * 
	 * @param str
	 *            The path of the file to play
	 */
	public MP3Player(String str) {
		file = str;
		state = PlayerState.INITIALIZING;

		try {
			String urlAsString = "file:///" + this.file;

			this.player = new javazoom.jl.player.Player(new java.net.URL(
					urlAsString).openStream(),
					javazoom.jl.player.FactoryRegistry.systemRegistry()
							.createAudioDevice());

			this.playerThread = new Thread(this, "AudioPlayerThread");
		} catch (Exception e) {
			Log.addEntry(e);
		}
	}

	@Override
	public void pause() {
		state = PlayerState.PAUSED;
	}

	@Override
	public void stop() {
		state = PlayerState.STOPPED;
	}

	@Override
	public void play() {
		if (playerThread != null && !playerThread.isAlive()) {
			// Launch thread
			playerThread.start();
		} else {
			// Change player state
			synchronized (player) {
				state = PlayerState.PLAYING;
				player.notifyAll();
			}
		}
	}

	@Override
	public void run() {
		while (state != PlayerState.FNISHED && state != PlayerState.STOPPED) { // Not
																				// stopped
																				// or
																				// finished
			try {
				if (!player.play(1)) { // Play the song
					// Song is finished
					state = PlayerState.FNISHED;
				}
			} catch (Exception e) {
				Log.addEntry(e);
				break;
			}

			// Check if the player has been paused
			synchronized (player) {
				while (state == PlayerState.PAUSED) {
					try {
						player.wait();
					} catch (Exception e) {
						Log.addEntry(e);
					}
				}
			}
		}
	}

	@Override
	public String getFile() {
		return file;
	}
}
