package com.cantik.gui.player;

import com.cantik.gui.MainWindow;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Button to reduce the window: only show player
 *
 * @author cyprien
 */
public class Reduce extends JButton {
	/**
	 * Logger for the class
	 */
	private static Logger logger = Logger.getLogger(Reduce.class.getName());

	/**
	 * Dimension use to reset the size
	 */
	private static Dimension lastDim;

	/**
	 * Main Window
	 */
	private static JFrame window = MainWindow.getMainWindow();

	/**
	 * Constructor: init image and action
	 */
	public Reduce() {
		super();

		setIcon("assets/img/reduce.png");

		// Disable border
		Border emptyBorder = BorderFactory.createEmptyBorder();
		setBorder(emptyBorder);

		setBackground(Color.BLACK);

		this.addActionListener(new ReduceClick());
	}

	/**
	 * Change the icon of the button
	 *
	 * @param imgPath
	 * 		The new icon to set
	 */
	public void setIcon(String imgPath) {
		try {
			BufferedImage img = ImageIO.read(new File(imgPath));
			this.setIcon(new ImageIcon(img));
		} catch (IOException e) {
			logger.log(Level.WARNING, e.getMessage());
		}
	}

	/**
	 * Reduce the window
	 *
	 * @author cyprien
	 */
	private static class ReduceClick implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Change size and behavior
			lastDim = window.getSize();
			window.setResizable(false);
			window.setAlwaysOnTop(true);
			window.setSize(new Dimension(window.getWidth(),
					105 + window.getInsets().top + window.getInsets().bottom));

			// Delete other event
			for (ActionListener al : ((JButton) e.getSource())
					.getActionListeners()) {
				((JButton) e.getSource()).removeActionListener(al);
			}

			((JButton) e.getSource()).addActionListener(new MaxiClick()); // Change event

			((Reduce) e.getSource()).setIcon("assets/img/maxi.png"); // Change icon
		}

	}

	/**
	 * Reset the window
	 *
	 * @author cyprien
	 */
	private static class MaxiClick implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Reset window
			window.setResizable(true);
			window.setAlwaysOnTop(false);
			window.setSize(lastDim);

			// Delete other event
			for (ActionListener al : ((JButton) e.getSource())
					.getActionListeners()) {
				((JButton) e.getSource()).removeActionListener(al);
			}

			((JButton) e.getSource()).addActionListener(new ReduceClick()); // Change event

			((Reduce) e.getSource()).setIcon("assets/img/reduce.png"); // Change icon
		}

	}
}
