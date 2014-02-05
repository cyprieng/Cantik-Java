package com.musicplayer.gui.player;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.Border;

import com.musicplayer.core.Log;
import com.musicplayer.core.player.PlayerState;
import com.musicplayer.core.playlist.Playlist;

/**
 * Custom JButton playing or pausing the current track
 * 
 * @author cyprien
 * 
 */
public class PlayPauseButton extends JButton implements Observer {
	private static final long serialVersionUID = -6417580002540736687L;

	/**
	 * Constructor: init the button
	 */
	public PlayPauseButton() {
		super();
		setIcon("assets/img/play.png");

		Playlist.getPlaylist().addObserver(this);

		// Delete border
		Border emptyBorder = BorderFactory.createEmptyBorder();
		setBorder(emptyBorder);

		setBackground(Color.BLACK);

		this.addActionListener(new PlayClick()); // Add event
	}

	/**
	 * Change the icon of the button
	 * 
	 * @param imgPath
	 *            The new icon to set
	 */
	public void setIcon(String imgPath) {
		try {
			BufferedImage img = ImageIO.read(new File(imgPath));
			this.setIcon(new ImageIcon(img));
		} catch (IOException e) {
			Log.addEntry(e);
		}
	}

	/**
	 * Action when clicking on play
	 * 
	 * @author cyprien
	 * 
	 */
	private static class PlayClick implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Playlist.getPlaylist().play(); // Play

			// Delete other event
			for (ActionListener al : ((JButton) e.getSource())
					.getActionListeners()) {
				((JButton) e.getSource()).removeActionListener(al);
			}

			((JButton) e.getSource()).addActionListener(new PauseClick()); // Change
																			// event

			((PlayPauseButton) e.getSource()).setIcon("assets/img/pause.png"); // Change
																				// icon
		}

	}

	/**
	 * Action when clicking on pause
	 * 
	 * @author cyprien
	 * 
	 */
	private static class PauseClick implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Playlist.getPlaylist().getPlayer().pause(); // Pause

			// Delete other event
			for (ActionListener al : ((JButton) e.getSource())
					.getActionListeners()) {
				((JButton) e.getSource()).removeActionListener(al);
			}

			((JButton) e.getSource()).addActionListener(new PlayClick()); // Change
																			// event

			((PlayPauseButton) e.getSource()).setIcon("assets/img/play.png"); // Change
																				// icon
		}

	}

	@Override
	public void update(Observable playlist, Object arg) {
		// Delete other event
		for (ActionListener al : this.getActionListeners()) {
			this.removeActionListener(al);
		}

		if (Playlist.getPlaylist().getPlayer().getState() == PlayerState.INITIALIZING
				|| Playlist.getPlaylist().getPlayer().getState() == PlayerState.PLAYING) {
			// Player is playing
			this.setIcon("assets/img/pause.png");
			this.addActionListener(new PauseClick());
		} else {
			// Player is paused
			this.setIcon("assets/img/play.png");
			this.addActionListener(new PlayClick());
		}
	}
}
