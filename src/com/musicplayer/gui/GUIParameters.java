package com.musicplayer.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

import com.musicplayer.core.Log;

/**
 * Parameters of the UI (Color, font...)
 * 
 * @author cyprien
 * 
 */
public class GUIParameters {
	/**
	 * Color of borders
	 */
	public final static Color BORDER = new Color(0x4c6072);

	/**
	 * Background color of the left Bar
	 */
	public final static Color LEFTBAR_BACKGROUND = new Color(0x12171c);

	/**
	 * Background color behind an active left Bar item
	 */
	public final static Color LEFTBAR_ACTIVE_BACKGROUND = new Color(0x0e1115);

	/**
	 * Separator color in left Bar
	 */
	public final static Color LEFTBAR_SEPARATOR = new Color(0x425366);

	/**
	 * Category color in left Bar
	 */
	public final static Color LEFTBAR_CAT = new Color(0x626569);

	/**
	 * Color of the active indicator
	 */
	public final static Color LEFTBAR_ACTIVE = new Color(0xd34836);

	/**
	 * Get the global font
	 * 
	 * @return The global Font
	 * @see Font
	 */
	public static Font getFont() {
		try {
			Font f = Font.createFont(Font.TRUETYPE_FONT, new File(
					"assets/font/Fairview.ttf"));

			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(f);

			return f.deriveFont(20.0f);
		} catch (FontFormatException | IOException e) {
			Log.addEntry(e);
			return null;
		}
	}

	/**
	 * Get the font of the central area
	 * 
	 * @return The central Font
	 * @see Font
	 */
	public static Font getCentralFont() {
		try {
			Font f = Font.createFont(Font.TRUETYPE_FONT, new File(
					"assets/font/PTSans.ttf"));

			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(f);

			return f.deriveFont(20.0f);
		} catch (FontFormatException | IOException e) {
			Log.addEntry(e);
			return null;
		}
	}
}
