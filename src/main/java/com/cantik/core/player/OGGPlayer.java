package com.cantik.core.player;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class managing OGG playback
 *
 * @author cyprien
 */
public class OGGPlayer extends Observable implements Player, Runnable {
	/**
	 * Logger for the class
	 */
	private static Logger logger = Logger.getLogger(OGGPlayer.class.getName());

	/**
	 * Path of the song
	 */
	private String file;

	/**
	 * State of the player
	 */
	private PlayerState state;

	/**
	 * The thread launching the player
	 */
	private Thread playerThread;

	/**
	 * The InputStream of the song
	 */
	private AudioInputStream in;

	/**
	 * Constructor: initialize the player
	 *
	 * @param str
	 * 		The path of the file to play
	 */
	public OGGPlayer(String str) {
		file = str;
		state = PlayerState.INITIALIZING;

		this.playerThread = new Thread(this, "AudioPlayerThread");
	}

	/**
	 * Read the ogg file
	 *
	 * @param targetFormat
	 * 		Output format
	 * @param din
	 * 		Source
	 * @throws IOException
	 * 		Thrown if error reading file
	 * @throws LineUnavailableException
	 * 		Thrown if error playing file
	 */
	private void rawplay(AudioFormat targetFormat, AudioInputStream din)
			throws IOException, LineUnavailableException {
		byte[] data = new byte[4096];
		SourceDataLine line = getLine(targetFormat);
		if (line != null) {
			// Start
			line.start();
			int nBytesRead = 0;
			while (nBytesRead != -1 && state != PlayerState.STOPPED) {
				synchronized (this) {
					// Check if player is paused
					while (state == PlayerState.PAUSED) {
						try {
							wait();
						} catch (Exception e) {
							logger.log(Level.WARNING, e.getMessage());
						}
					}
				}

				nBytesRead = din.read(data, 0, data.length);
				if (nBytesRead != -1)
					line.write(data, 0, nBytesRead);
			}
			// Stop
			line.drain();
			line.stop();
			line.close();
			din.close();
		}
	}

	/**
	 * Get line to play audio
	 *
	 * @param audioFormat
	 * 		The ouput format
	 * @return The line
	 * @throws LineUnavailableException
	 * 		Thrown if error playing file
	 */
	private SourceDataLine getLine(AudioFormat audioFormat)
			throws LineUnavailableException {
		SourceDataLine res = null;
		DataLine.Info info = new DataLine.Info(SourceDataLine.class,
				audioFormat);
		res = (SourceDataLine) AudioSystem.getLine(info);
		res.open(audioFormat);
		return res;
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
		try {
			File f = new File(file);

			// Get AudioInputStream from given file.
			in = AudioSystem.getAudioInputStream(f);
			in.mark(Integer.MAX_VALUE); // Mark for rewind
			if (in != null) {
				AudioFormat baseFormat = in.getFormat();
				AudioFormat decodedFormat = new AudioFormat(
						AudioFormat.Encoding.PCM_SIGNED,
						baseFormat.getSampleRate(), 16,
						baseFormat.getChannels(), baseFormat.getChannels() * 2,
						baseFormat.getSampleRate(), false);
				// Get AudioInputStream that will be decoded by underlying
				// VorbisSPI
				AudioInputStream din = AudioSystem.getAudioInputStream(
						decodedFormat, in);
				// Play now !
				rawplay(decodedFormat, din);
				in.close();
				state = PlayerState.FNISHED;
				setChanged();
				notifyObservers();
			}
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage());
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
		if (in != null) {
			try {
				// Skip
				in.reset();
				in.skip((long) (in.available() * (double) ((double) percent / 100.0)));
			} catch (Exception e) {
				logger.log(Level.WARNING, e.getMessage());
			}
		}
	}
}
