package com.musicplayer.gui.player;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.Border;

import com.musicplayer.core.Log;
import com.musicplayer.core.playlist.Playlist;
import com.musicplayer.core.playlist.RepeatState;

/**
 * Custom JButton switching repeat state
 * 
 * @author cyprien
 * 
 */
public class Repeat extends JButton {
	private static final long serialVersionUID = 4971170300022461995L;

	/**
	 * Constructor: init the button
	 */
	public Repeat() {
		super();
		setIcon("assets/img/repeat.png");

		// Delete border
		Border emptyBorder = BorderFactory.createEmptyBorder();
		setBorder(emptyBorder);

		setBackground(Color.BLACK);

		this.addActionListener(new SwitchRepeat()); // Add event
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
	 * Action when clicking on the button
	 * 
	 * @author cyprien
	 * 
	 */
	private static class SwitchRepeat implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Change state and img
			switch (Playlist.getPlaylist().getRepeat()) {
			case OFF:
				Playlist.getPlaylist().setRepeat(RepeatState.SONG);
				((Repeat) e.getSource()).setIcon("assets/img/repeat_song.png");
				break;
			case SONG:
				Playlist.getPlaylist().setRepeat(RepeatState.ALL);
				((Repeat) e.getSource()).setIcon("assets/img/repeat_all.png");
				break;
			case ALL:
				Playlist.getPlaylist().setRepeat(RepeatState.OFF);
				((Repeat) e.getSource()).setIcon("assets/img/repeat.png");
				break;
			}
		}
	}
}
