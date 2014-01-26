package com.musicplayer.core.player;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import com.musicplayer.core.Log;

/**
 * Class managing OGG playback
 * 
 * @author cyprien
 * 
 */
public class OGGPlayer implements Player, Runnable {
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
	 * Constructor: initialize the player
	 * 
	 * @param str
	 *            The path of the file to play
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
	 *            Output format
	 * @param din
	 *            Source
	 * @throws IOException
	 *             Thrown if error reading file
	 * @throws LineUnavailableException
	 *             Thrown if error playing file
	 * 
	 */
	private void rawplay(AudioFormat targetFormat, AudioInputStream din)
			throws IOException, LineUnavailableException {
		byte[] data = new byte[4096];
		SourceDataLine line = getLine(targetFormat);
		if (line != null) {
			// Start
			line.start();
			int nBytesRead = 0, nBytesWritten = 0;
			while (nBytesRead != -1) {
				synchronized (this) {
					// Check if player is paused
					while (state == PlayerState.PAUSED) {
						try {
							wait();
						} catch (Exception e) {
							Log.addEntry(e);
						}
					}
				}

				// Check if the player is stopped
				if (state == PlayerState.STOPPED)
					break;

				nBytesRead = din.read(data, 0, data.length);
				if (nBytesRead != -1)
					nBytesWritten = line.write(data, 0, nBytesRead);
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
	 *            The ouput format
	 * @return The line
	 * @throws LineUnavailableException
	 *             Thrown if error playing file
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
	}

	@Override
	public void stop() {
		state = PlayerState.STOPPED;
		pause();
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
			}
		}
	}

	@Override
	public void run() {
		try {
			File f = new File(file);

			// Get AudioInputStream from given file.
			AudioInputStream in = AudioSystem.getAudioInputStream(f);
			AudioInputStream din = null;
			if (in != null) {
				AudioFormat baseFormat = in.getFormat();
				AudioFormat decodedFormat = new AudioFormat(
						AudioFormat.Encoding.PCM_SIGNED,
						baseFormat.getSampleRate(), 16,
						baseFormat.getChannels(), baseFormat.getChannels() * 2,
						baseFormat.getSampleRate(), false);
				// Get AudioInputStream that will be decoded by underlying
				// VorbisSPI
				din = AudioSystem.getAudioInputStream(decodedFormat, in);
				// Play now !
				rawplay(decodedFormat, din);
				in.close();
				state = PlayerState.FNISHED;
			}
		} catch (Exception e) {
			Log.addEntry(e);
		}
	}

	@Override
	public String getFile() {
		return file;
	}
}
