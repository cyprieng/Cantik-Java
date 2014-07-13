package com.musicplayer.gui.player;

import com.musicplayer.core.Log;
import com.musicplayer.gui.MainWindow;

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
 * Button to reduce the window: only show player
 *
 * @author cyprien
 */
public class Reduce extends JButton {
	/**
	 * Constructor: init image and action
	 */
	public Reduce() {
		super();

		// Set icon
		try {
			BufferedImage img = ImageIO.read(new File("assets/img/reduce.png"));
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
				// Set size
				JFrame window = MainWindow.getMainWindow();
				window.setSize(new Dimension(window.getWidth(),
						105 + (window.getHeight() - window.getContentPane().getHeight())));
			}
		});
	}
}
