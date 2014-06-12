package com.musicplayer.gui.trayicon;

import com.musicplayer.gui.GUIParameters;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * JMenuItem with a custom style
 *
 * @author cyprien
 */
public class CustomJMenuItem extends JMenuItem {
	/**
	 * Create a menu item with just a text
	 *
	 * @param str
	 * 		Text to show
	 */
	public CustomJMenuItem(String str) {
		super(str);

		// Set color
		setBackground(GUIParameters.LEFTBAR_BACKGROUND);
		setForeground(Color.WHITE);
	}

	/**
	 * Create a menu item with a text and an icon
	 *
	 * @param str
	 * 		Text to show
	 * @param icon
	 * 		Icon of the item
	 */
	public CustomJMenuItem(String str, Image icon) {
		this(str);

		// Get icon width
		int width = ((BufferedImage) icon).getWidth();
		int height = ((BufferedImage) icon).getHeight();

		// Set new size
		int newWidth = (width < 15) ? width : 15;

		float ratio = (float) newWidth / (float) width;
		int newHeight = (int) (ratio * height);

		// Set scaled img
		setIcon(new ImageIcon(icon.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH)));
	}
}
