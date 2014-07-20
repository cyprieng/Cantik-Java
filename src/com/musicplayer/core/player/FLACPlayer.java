package com.musicplayer.core.player;

import com.musicplayer.core.Log;
import org.kc7bfi.jflac.FLACDecoder;
import org.kc7bfi.jflac.PCMProcessor;
import org.kc7bfi.jflac.metadata.StreamInfo;
import org.kc7bfi.jflac.util.ByteData;

import javax.sound.sampled.*;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Observable;
import java.util.Vector;

/**
 * Class managing FLAC playback
 *
 * @author cyprien
 */
public class FLACPlayer extends Observable implements Player, Runnable,
		PCMProcessor {
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
	 * Format of the song
	 */
	private AudioFormat fmt;

	/**
	 * Data line information
	 */
	private DataLine.Info info;

	/**
	 * Source data line
	 */
	private SourceDataLine line;

	/**
	 * The InputStream of the song
	 */
	private FileInputStream is;

	/**
	 * List of listeners
	 */
	private Vector<LineListener> listeners = new Vector<LineListener>();

	/**
	 * Constructor: initialize the player
	 *
	 * @param str
	 * 		The path of the file to play
	 */
	public FLACPlayer(String str) {
		file = str;
		state = PlayerState.INITIALIZING;

		this.playerThread = new Thread(this, "AudioPlayerThread");
	}

	/**
	 * Decode and play an input FLAC file.
	 *
	 * @param inFileName
	 * 		The input FLAC file name
	 * @throws IOException
	 * 		Thrown if error reading file
	 * @throws LineUnavailableException
	 * 		Thrown if error playing file
	 */
	public void decode(String inFileName) throws IOException,
			LineUnavailableException {
		// Open file
		is = new FileInputStream(inFileName);

		// Decode file
		FLACDecoder decoder = new FLACDecoder(is);
		decoder.addPCMProcessor(this);
		try {
			decoder.decode();
		} catch (EOFException e) {
		}

		line.drain();
		line.close();

		// We're going to clear out the list of listeners as well, so that
		// everytime through
		// things are basically at the same starting point.
		listeners.clear();
		is.close();
	}

	/**
	 * Process the StreamInfo block.
	 *
	 * @param streamInfo
	 * 		the StreamInfo block
	 * @see org.kc7bfi.jflac.PCMProcessor#processStreamInfo(org.kc7bfi.jflac.metadata.StreamInfo)
	 */
	public void processStreamInfo(StreamInfo streamInfo) {
		try {
			fmt = streamInfo.getAudioFormat();
			info = new DataLine.Info(SourceDataLine.class, fmt,
					AudioSystem.NOT_SPECIFIED);
			line = (SourceDataLine) AudioSystem.getLine(info);

			// Add the listeners to the line at this point, it's the only
			// way to get the events triggered.
			int size = listeners.size();
			for (int index = 0; index < size; index++)
				line.addLineListener((LineListener) listeners.get(index));

			line.open(fmt, AudioSystem.NOT_SPECIFIED);
			line.start();
		} catch (LineUnavailableException e) {
			Log.addEntry(e);
		}
	}

	/**
	 * Process the decoded PCM bytes.
	 *
	 * @param pcm
	 * 		The decoded PCM data
	 * @see org.kc7bfi.jflac.PCMProcessor#processPCM(org.kc7bfi.jflac.util.ByteSpace)
	 */
	public void processPCM(ByteData pcm) {
		synchronized (this) {
			// Check if player is paused
			while (state == PlayerState.PAUSED) {
				try {
					wait();
				} catch (Exception e) {
					Log.addEntry(e);
				}
			}

			if (state != PlayerState.STOPPED)
				line.write(pcm.getData(), 0, pcm.getLen());
		}
	}

	/**
	 * Add listener
	 *
	 * @param listener
	 * 		The listener to add
	 */
	public void addListener(LineListener listener) {
		listeners.add(listener);
	}

	/**
	 * Remove listener
	 *
	 * @param listener
	 * 		The listener to remove
	 */
	public void removeListener(LineListener listener) {
		listeners.removeElement(listener);
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
			this.playerThread = new Thread(this, "AudioPlayerThread");
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
			decode(file);
			state = PlayerState.FNISHED;
			setChanged();
			notifyObservers();
		} catch (IOException | LineUnavailableException e) {
			Log.addEntry(e);
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
		if (is != null) {
			try {
				is.close(); // Stop playing

				// Restart play
				playerThread = new Thread(this, "AudioPlayerThread");
				playerThread.start();

				// Skip
				pause();
				is.skip((long) (is.available() * (double) ((double) percent / 100.0)));
				play();
			} catch (Exception e) {
				Log.addEntry(e);
			}
		}
	}
}