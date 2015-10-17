package com.cantik.gui.leftbar;

import com.cantik.gui.GUIParameters;

import javax.swing.*;
import java.awt.*;

/**
 * Draw a line in a JPanel
 *
 * @author cyprien
 */
public class Separator extends JPanel {
	private static final long serialVersionUID = 964341531448230546L;

	/**
	 * Init dimension of the separator
	 */
	public Separator() {
		super();
		setPreferredSize(new Dimension(250, 60));
		setMaximumSize(getPreferredSize());
		setMinimumSize(getPreferredSize());
	}

	@Override
	public void paintComponent(Graphics g) {
		// Get the current size of this component
		Dimension d = this.getSize();

		// draw in the right color
		g.setColor(GUIParameters.LEFTBAR_SEPARATOR);

		// draw a centered horizontal line
		g.drawLine(0, d.height / 2, d.width, d.height / 2);
	}
}
