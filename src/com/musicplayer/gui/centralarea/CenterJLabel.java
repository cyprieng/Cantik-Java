package com.musicplayer.gui.centralarea;

import com.musicplayer.gui.leftbar.LeftbarJLabel;

import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JLabel positioned in the center of its parent
 *
 * @author cyprien
 */
public class CenterJLabel extends LeftbarJLabel {
	private static final long serialVersionUID = 3849599444981028032L;

	/**
	 * Logger for the class
	 */
	private static Logger logger = Logger.getLogger(CenterJLabel.class.getName());

	/**
	 * Create label with given text and apply the right font
	 *
	 * @param txt
	 * 		The text of the label
	 */
	public CenterJLabel(String txt) {
		super(txt);
		setForeground(Color.BLACK);
		setFont(getFont().deriveFont(50.0f));
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		// Center of the parent
		this.setBounds(this.getParent().getWidth() / 2 - this.getWidth() / 2, this
						.getParent().getHeight() / 2 - this.getHeight() / 2,
				this.getWidth(), this.getHeight()
		);

		try {
			finalize();
		} catch (Throwable e) {
			logger.log(Level.WARNING, e.getMessage());
		}
	}
}