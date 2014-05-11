package com.musicplayer.gui.leftbar;

import com.musicplayer.gui.GUIParameters;

import javax.swing.*;
import java.awt.*;

/**
 * Custom JLabel class
 *
 * @author cyprien
 */
public class LeftbarJLabel extends JLabel {
	private static final long serialVersionUID = -2832851741292905228L;

	/**
	 * Init with the right color and font
	 *
	 * @param str
	 */
	public LeftbarJLabel(String str) {
		super(str);
		setFont(GUIParameters.getLeftFont());
		setForeground(Color.WHITE);
	}
}
