package com.musicplayer.gui.leftbar;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.musicplayer.gui.GUIParameters;

/**
 * Custom JLabel for the category title in left bar
 * 
 * @author cyprien
 * 
 */
public class CategoryLabel extends JLabel {
	private static final long serialVersionUID = -5281324916113409236L;

	/**
	 * Init the JLabel with the given title
	 * 
	 * @param title
	 *            The title of the category
	 */
	public CategoryLabel(String title) {
		super(title.toUpperCase());
		setFont(GUIParameters.getFont());
		setForeground(GUIParameters.LEFTBAR_CAT);
		setAlignmentX(Component.CENTER_ALIGNMENT);
		Border empty = new EmptyBorder(0, 0, 20, 0);
		setBorder(empty);
	}
}
