package com.musicplayer.core.player;

import java.io.InputStream;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class managing MP3 playback
 *
 * @author cyprien
 */
public class MP3Player extends Observable implements Runnable, Player {
	/**
	 * Logger for the class
	 */
	private static Logger logger = Logger.getLogger(MP3Player.class.getName());

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
	 * The InputStream of the song
	 */
	private InputStream song;

	/**
	 * The thread launching the player
	 */
	private Thread playerThread;

	/**
	 * Constructor: initialize the player
	 *
	 * @param str
	 * 		The path of the file to play
	 */
	public MP3Player(String str) {
		file = str;
		state = PlayerState.INITIALIZING;

		try {
			String urlAsString = "file:///" + this.file;

			song = new java.net.URL(urlAsString).openStream();

			this.player = new javazoom.jl.player.Player(song,
					javazoom.jl.player.FactoryRegistry.systemRegistry()
							.createAudioDevice()
			);

			this.playerThread = new Thread(this, "AudioPlayerThread");
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage());
		}
	}

	@Override
	public void pause() {
		state = PlayerState.PAUSED;
		setChanged();
		notifyObservers();
	}

	@Override
	public void stop() {
		synchronized (this) {
			state = PlayerState.STOPPED;
			notifyAll();
			setChanged();
			notifyObservers();
		}
	}

	@Override
	public void play() {
		if (playerThread != null && !playerThread.isAlive()) {
			// Launch thread
			playerThread.start();
		} else {
			// Change player state
			synchronized (this) {
				state = PlayerState.PLAYING;
				notifyAll();
				setChanged();
				notifyObservers();
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
					setChanged();
					notifyObservers();
				}
			} catch (Exception e) {
				logger.log(Level.WARNING, e.getMessage());
				break;
			}

			// Check if the player has been paused
			synchronized (this) {
				while (state == PlayerState.PAUSED) {
					try {
						wait();
					} catch (Exception e) {
						logger.log(Level.WARNING, e.getMessage());
					}
				}
			}
		}
	}

	@Override
	public String getFile() {
		return file;
	}

	@Override
	public PlayerState getState() {
		return state;
	}

	@Override
	public void skip(int percent) {
		try {
			// Restart
			String urlAsString = "file:///" + this.file;

			song = new java.net.URL(urlAsString).openStream();

			this.player = new javazoom.jl.player.Player(song,
					javazoom.jl.player.FactoryRegistry.systemRegistry()
							.createAudioDevice()
			);

			// Skip
			song.skip((long) (song.available() * (double) ((double) percent / 100.0)));
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage());
		}
	}
}
