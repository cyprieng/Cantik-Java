package com.musicplayer.gui.player.volumecontrol;

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

/**
 * JButton showing the VolumePanel
 * 
 * @author cyprien
 * @see VolumePanel
 */
public class VolumeButton extends JButton {
	private static final long serialVersionUID = -6805154968692111306L;

	/**
	 * Store the volume panel
	 * 
	 * @see VolumePanel
	 */
	private VolumePanel vp;

	/**
	 * Init button
	 */
	public VolumeButton() {
		super();

		// Set icon
		try {
			BufferedImage img = ImageIO.read(new File("assets/img/volume.png"));
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
			public void actionPerformed(ActionEvent arg) {
				// Show/Hide volume panel
				if (getVolumePanel().isVisible())
					getVolumePanel().setVisible(false);
				else
					getVolumePanel().setVisible(true);
			}
		});
	}

	/**
	 * Get a VolumePanel
	 * 
	 * @return A VolumePanel
	 * @see VolumePanel
	 */
	private VolumePanel getVolumePanel() {
		if (vp == null) {
			vp = new VolumePanel(this);
		}

		return vp;
	}
}
