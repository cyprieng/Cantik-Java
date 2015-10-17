package com.cantik.gui.leftbar;

import com.cantik.gui.GUIParameters;
import com.cantik.gui.MainWindow;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Create a JPanel containing an item entry
 *
 * @author cyprien
 */
public class Item extends JPanel {
	private static final long serialVersionUID = -2428032016539504254L;

	/**
	 * Logger for the class
	 */
	private static Logger logger = Logger.getLogger(Item.class.getName());

	/**
	 * List of all items
	 */
	private static Set<Item> instance = new HashSet<Item>();

	/**
	 * JPanel of the active indicator
	 */
	private JPanel activeIndicator;

	/**
	 * Text of the item
	 */
	protected JLabel text;

	/**
	 * Indicate if the item must be always active
	 */
	private boolean keepActive = false;

	/**
	 * Init the item with the given title
	 *
	 * @param str
	 * 		The title of the item
	 */
	public Item(final String str) {
		super();
		instance.add(this);

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
			logger.log(Level.WARNING, e.getMessage());
		}

		// Add text
		text = new LeftbarJLabel(MainWindow.bundle.getString(str));
		Border empty = new EmptyBorder(0, 10, 0, 0);
		text.setBorder(empty);
		this.add(text);

		// Add mouse hover event
		final Item label = this;
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				label.setActive(false);
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				label.unsetActive(false);
			}
		});

		// Add event when clicking
		this.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				MainWindow.setCentralArea(str); // Change central area
				// Unset active all other items
				for (Item i : instance) {
					if (i != null)
						i.unsetActive(true);
				}
				label.setActive(true); // Active this item
			}
		});
	}

	/**
	 * Set this item active
	 *
	 * @param keepActive
	 * 		Indicate if this item must stay always active
	 */
	public void setActive(boolean keepActive) {
		this.setBackground(GUIParameters.LEFTBAR_ACTIVE_BACKGROUND); // Modify
		// background
		activeIndicator.setBackground(GUIParameters.LEFTBAR_ACTIVE); // Show
		// active
		// indicator
		// Change keepActive if it is false
		if (!this.keepActive)
			this.keepActive = keepActive;
	}

	/**
	 * Set this item non active
	 *
	 * @param forceUnset
	 * 		Unset active even if keepActive is set to true
	 */
	public void unsetActive(boolean forceUnset) {
		if (!keepActive || forceUnset) {
			this.setBackground(GUIParameters.LEFTBAR_BACKGROUND); // Modify
			// background
			activeIndicator.setBackground(GUIParameters.LEFTBAR_BACKGROUND); // Hide
			// active
			// indicator
			keepActive = false; // Disable always active
		}
	}
}
