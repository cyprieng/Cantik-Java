package com.musicplayer.gui;

import java.awt.Color;

import javax.swing.JLabel;

/**
 * Custom JLabel class
 * 
 * @author cyprien
 * 
 */
public class CustomJLabel extends JLabel {
	private static final long serialVersionUID = -2832851741292905228L;

	/**
	 * Init with the right color and font
	 * 
	 * @param str
	 */
	public CustomJLabel(String str) {
		super(str);
		setFont(GUIParameters.getFont());
		setForeground(Color.WHITE);
	}
}
