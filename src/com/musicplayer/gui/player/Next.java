package com.musicplayer.gui.player;

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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Custom JButton jumping to the next track
 *
 * @author cyprien
 */
public class Next extends JButton {
	private static final long serialVersionUID = 5952116559236798807L;

	/**
	 * Logger for the class
	 */
	private static Logger logger = Logger.getLogger(Next.class.getName());

	/**
	 * Constructor: init image and action
	 */
	public Next() {
		super();

		// Set icon
		try {
			BufferedImage img = ImageIO.read(new File("assets/img/next.png"));
			this.setIcon(new ImageIcon(img));
		} catch (IOException e) {
			logger.log(Level.WARNING, e.getMessage());
		}

		// Disable border
		Border emptyBorder = BorderFactory.createEmptyBorder();
		setBorder(emptyBorder);

		setBackground(Color.BLACK);

		// Add onclick event
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Playlist.getPlaylist().next();
			}
		});
	}
}
