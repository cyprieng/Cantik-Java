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
 * Custom JButton jumping to last track
 *
 * @author cyprien
 */
public class Previous extends JButton {
	private static final long serialVersionUID = 6570346642101647547L;

	/**
	 * Constructor: init image and action
	 */
	public Previous() {
		super();

		// Set icon
		try {
			BufferedImage img = ImageIO
					.read(new File("assets/img/previous.png"));
			this.setIcon(new ImageIcon(img));
		} catch (IOException e) {
			Log.addEntry(e);
		}

		// Disable border
		Border emptyBorder = BorderFactory.createEmptyBorder();
		setBorder(emptyBorder);

		setBackground(Color.BLACK);

		// Add onclick event
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Playlist.getPlaylist().back();
			}
		});
	}
}
