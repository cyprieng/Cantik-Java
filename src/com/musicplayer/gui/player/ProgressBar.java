package com.musicplayer.gui.player;

import java.awt.Color;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JSlider;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.musicplayer.core.player.PlayerState;
import com.musicplayer.core.playlist.Playlist;
import com.musicplayer.core.song.Song;

/**
 * Custom JSlider. Show song progression, and allow user to fast
 * forward/backward
 * 
 * @author cyprien
 * 
 */
public class ProgressBar extends JSlider implements Observer {
	private static final long serialVersionUID = 8825926361843351174L;

	/**
	 * Timer updating the value of the slider
	 */
	Timer timer;

	/**
	 * Define if we are playing
	 */
	Boolean playing;

	/**
	 * Define if we have to call the listener
	 */
	Boolean changeListener;

	/**
	 * Init the JSlider
	 */
	public ProgressBar() {
		super(0, 100);
		setValue(0);

		Playlist.getPlaylist().addObserver(this);
		playing = false;

		// Set UI
		this.setBackground(Color.BLACK);
		setUI(new CustomSliderUI(this));

		// Delete border
		Border emptyBorder = BorderFactory.createEmptyBorder();
		setBorder(emptyBorder);

		// Change Listener
		changeListener = true;
		this.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg) {
				if (changeListener) {
					// Skip
					int percent = (int) ((double) (((ProgressBar) arg
							.getSource()).getValue())
							/ (double) (((ProgressBar) arg.getSource())
									.getMaximum()) * 100);

					Playlist.getPlaylist().getPlayer().skip(percent);
				}
			}
		});
	}

	@Override
	public void setValue(int n) {
		// Set value without calling listener
		changeListener = false;
		super.setValue(n);
		changeListener = true;
	}

	@Override
	public void update(Observable playlist, Object arg) {
		if (arg != null && ((String) arg).equals("new track")) {
			// New track => init player
			Song s = Playlist.getPlaylist().getCurrentSong();
			setMaximum(s.getDuration());
			setValue(0);
		}

		if (Playlist.getPlaylist().getPlayerState() == PlayerState.PLAYING
				|| Playlist.getPlaylist().getPlayerState() == PlayerState.INITIALIZING) {
			// Start progressbar
			if (playing)
				return;

			playing = true;

			// Set timer
			timer = new Timer();
			final ProgressBar bar = this;
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					bar.setValue(bar.getValue() + 1); // Increment value
				}
			}, 1000, 1000);
		} else if (Playlist.getPlaylist().getPlayerState() == PlayerState.PAUSED) {
			// Stop progressbar
			if (timer != null) {
				try {
					timer.cancel();
				} catch (Exception e) {
				}
			}
			playing = false;
		} else {
			// Reset progressbar
			setValue(0);
			if (timer != null) {
				try {
					timer.cancel();
				} catch (Exception e) {
				}
			}
			playing = false;
		}
	}
}
