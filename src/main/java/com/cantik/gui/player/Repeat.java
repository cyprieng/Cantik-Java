package com.cantik.gui.player;

import com.cantik.core.playlist.Playlist;
import com.cantik.core.playlist.RepeatState;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Custom JButton switching repeat state
 *
 * @author cyprien
 */
public class Repeat extends JButton {
	private static final long serialVersionUID = 4971170300022461995L;

	/**
	 * Logger for the class
	 */
	private static Logger logger = Logger.getLogger(Repeat.class.getName());

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
	 * 		The new icon to set
	 */
	public void setIcon(String imgPath) {
		try {
			BufferedImage img = ImageIO.read(new File(imgPath));
			this.setIcon(new ImageIcon(img));
		} catch (IOException e) {
			logger.log(Level.WARNING, e.getMessage());
		}
	}

	/**
	 * Action when clicking on the button
	 *
	 * @author cyprien
	 */
	private static class SwitchRepeat implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Change state and img
			switch (Playlist.getPlaylist().getRepeat()) {
				case OFF:
					Playlist.getPlaylist().setRepeat(RepeatState.ALL);
					((Repeat) e.getSource()).setIcon("assets/img/repeat_all.png");
					break;
				case ALL:
					Playlist.getPlaylist().setRepeat(RepeatState.SONG);
					((Repeat) e.getSource()).setIcon("assets/img/repeat_song.png");
					break;
				case SONG:
					Playlist.getPlaylist().setRepeat(RepeatState.OFF);
					((Repeat) e.getSource()).setIcon("assets/img/repeat.png");
					break;
			}
		}
	}
}
