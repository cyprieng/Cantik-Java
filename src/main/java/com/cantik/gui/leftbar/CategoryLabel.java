package com.cantik.gui.leftbar;

import com.cantik.gui.GUIParameters;

import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 * Custom JLabel for the category title in left bar
 *
 * @author cyprien
 */
public class CategoryLabel extends LeftbarJLabel {
	private static final long serialVersionUID = -5281324916113409236L;

	/**
	 * Init the JLabel with the given title
	 *
	 * @param title
	 * 		The title of the category
	 */
	public CategoryLabel(String title) {
		super(title.toUpperCase());
		setForeground(GUIParameters.LEFTBAR_CAT);
		setAlignmentX(CENTER_ALIGNMENT);
		Border empty = new EmptyBorder(0, 0, 20, 0);
		setBorder(empty);
	}
}
