package com.musicplayer.gui.player;

import com.musicplayer.core.Log;
import com.musicplayer.core.playlist.Playlist;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Custom JButton switching the random state
 *
 * @author cyprien
 */
public class Random extends JButton {
	private static final long serialVersionUID = 2654540862809367609L;

	/**
	 * Constructor: init the button
	 */
	public Random() {
		super();
		setIcon("assets/img/random.png");

		// Delete border
		Border emptyBorder = BorderFactory.createEmptyBorder();
		setBorder(emptyBorder);

		setBackground(Color.BLACK);

		this.addActionListener(new SwitchRandom()); // Add event
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
			Log.addEntry(e);
		}
	}

	/**
	 * Action when clicking on the button
	 *
	 * @author cyprien
	 */
	private static class SwitchRandom implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (Playlist.getPlaylist().isRandom()) {
				// Random off
				Playlist.getPlaylist().setRandom(false);
				((Random) e.getSource()).setIcon("assets/img/random.png");
			} else {
				// Random on
				Playlist.getPlaylist().setRandom(true);
				((Random) e.getSource())
						.setIcon("assets/img/random_active.png");
			}
		}

	}
}
