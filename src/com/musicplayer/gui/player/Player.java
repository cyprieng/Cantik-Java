package com.musicplayer.gui.player;

import java.awt.Color;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.musicplayer.core.playlist.Playlist;
import com.musicplayer.gui.CustomJLabel;

/**
 * Custom Jpanel showing all component of the player
 * 
 * @author cyprien
 * 
 */
public class Player extends JPanel implements Observer {
	private static final long serialVersionUID = 8685108070111620141L;

	/**
	 * JLabel showing the duration of the song
	 */
	CustomJLabel duration;

	/**
	 * Constructor: init all component
	 */
	public Player() {
		super();
		setBackground(Color.BLACK);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		Border empty = new EmptyBorder(0, 75, 0, 0);

		// Previous
		Previous previous = new Previous();
		previous.setBorder(empty);

		// Play
		PlayPauseButton play = new PlayPauseButton();
		empty = new EmptyBorder(20, 10, 20, 0);
		play.setBorder(empty);

		// Next
		Next next = new Next();
		empty = new EmptyBorder(0, 10, 0, 0);
		next.setBorder(empty);

		// Cover
		Cover cover = new Cover();
		empty = new EmptyBorder(0, 100, 0, 0);
		cover.setBorder(empty);

		// Info
		TrackInfo info = new TrackInfo();
		empty = new EmptyBorder(0, 20, 0, 50);
		info.setBorder(empty);

		// Progress bar
		ProgressBar bar = new ProgressBar();

		// Duration
		duration = new CustomJLabel("0:00");
		empty = new EmptyBorder(0, 20, 0, 0);
		duration.setBorder(empty);

		Playlist.getPlaylist().addObserver(this);

		// Random
		Random random = new Random();
		empty = new EmptyBorder(0, 20, 0, 0);
		random.setBorder(empty);

		// Repeat
		Repeat repeat = new Repeat();
		empty = new EmptyBorder(0, 5, 0, 20);
		repeat.setBorder(empty);

		// Add all component
		add(previous);
		add(play);
		add(next);
		add(cover);
		add(info);
		add(bar);
		add(duration);
		add(random);
		add(repeat);
	}

	@Override
	public void update(Observable playlist, Object arg) {
		// Calc duration
		int duration = Playlist.getPlaylist().getCurrentSong().getDuration();
		int min = duration / 60;
		int second = duration - min * 60;

		// Update duration
		if (second < 10)
			this.duration.setText(min + ":0" + second);
		else
			this.duration.setText(min + ":" + second);
	}
}
