package com.musicplayer.gui.leftbar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.musicplayer.core.Log;
import com.musicplayer.gui.GUIParameters;

/**
 * Create a JPanel containing an item entry
 * 
 * @author cyprien
 * 
 */
public class Item extends JPanel {
	private static final long serialVersionUID = -2428032016539504254L;

	/**
	 * JPanel of the active indicator
	 */
	private JPanel activeIndicator;

	/**
	 * Init the item with the given title
	 * 
	 * @param str
	 *            The title of the item
	 */
	public Item(String str) {
		super();
		setPreferredSize(new Dimension(250, 40));
		setMaximumSize(getPreferredSize());
		setMinimumSize(getPreferredSize());

		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.setBackground(GUIParameters.LEFTBAR_BACKGROUND);

		// Init active indicator
		activeIndicator = new JPanel();
		activeIndicator.setPreferredSize(new Dimension(5, 40));
		activeIndicator.setMaximumSize(activeIndicator.getPreferredSize());
		activeIndicator.setMinimumSize(activeIndicator.getPreferredSize());
		activeIndicator.setBackground(GUIParameters.LEFTBAR_BACKGROUND);
		this.add(activeIndicator);

		// Add icon
		try {
			BufferedImage img = ImageIO.read(new File("assets/img/"
					+ str.toLowerCase() + ".png"));
			JLabel image = new JLabel(new ImageIcon(img));
			Border empty = new EmptyBorder(0, 30, 0, 0);
			image.setBorder(empty);
			this.add(image);
		} catch (IOException e) {
			Log.addEntry(e);
		}

		// Add text
		JLabel text = new JLabel(str);
		text.setFont(GUIParameters.getFont());
		text.setForeground(Color.WHITE);
		Border empty = new EmptyBorder(0, 10, 0, 0);
		text.setBorder(empty);
		this.add(text);

		// Add mouse hover event
		final Item label = this;
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				label.setActive();
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				label.unsetActive();
			}
		});
	}

	/**
	 * Set this item active
	 */
	public void setActive() {
		this.setBackground(GUIParameters.LEFTBAR_ACTIVE_BACKGROUND); // Modify
																		// background
		activeIndicator.setBackground(GUIParameters.LEFTBAR_ACTIVE); // Show
																		// active
																		// indicator
	}

	/**
	 * Set this item non active
	 */
	public void unsetActive() {
		this.setBackground(GUIParameters.LEFTBAR_BACKGROUND); // Modify
																// background
		activeIndicator.setBackground(GUIParameters.LEFTBAR_BACKGROUND); // Hide
																			// active
																			// indicator
	}
}
