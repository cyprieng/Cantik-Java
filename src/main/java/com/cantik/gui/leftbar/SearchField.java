package com.cantik.gui.leftbar;

import com.cantik.gui.GUIParameters;
import com.cantik.gui.MainWindow;
import com.cantik.gui.centralarea.musiclibrary.MusicLibraryView;

import javax.imageio.ImageIO;
import javax.swing.FocusManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Create a custom JTextField for the search bar
 *
 * @author cyprien
 */
public class SearchField extends JTextField {
	private static final long serialVersionUID = -1258623083307335060L;

	/**
	 * Logger for the class
	 */
	private static Logger logger = Logger.getLogger(SearchField.class.getName());

	/**
	 * Represent the round rect
	 */
	private Shape shape;

	/**
	 * Contructor: init the field
	 */
	public SearchField() {
		super();
		this.setPreferredSize(new Dimension(200, 30));
		this.setMaximumSize(this.getPreferredSize());
		this.setBackground(GUIParameters.LEFTBAR_BACKGROUND);
		this.setForeground(Color.WHITE);
		this.setFont(GUIParameters.getLeftFont());
		this.setOpaque(false);

		// Repaint when focus change
		final JTextField search = this;
		this.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				search.repaint();
			}

			public void focusLost(FocusEvent e) {
				search.repaint();
			}
		});

		// Add event: enter => search in music library
		this.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) { // Enter pressed
					if (!search.getText().equals(""))
						// Search query
						MusicLibraryView.getMusiLibraryView().showLibrary(
								search.getText());
					else
						// No query => all library
						MusicLibraryView.getMusiLibraryView().showLibrary(null);
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// No action needed when releasing key
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// No action needed when pressing key
			}
		});

		// Change text position in the field
		setBorder(javax.swing.BorderFactory.createCompoundBorder(
				javax.swing.BorderFactory.createTitledBorder(null,
						"Border Title",
						javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
						javax.swing.border.TitledBorder.DEFAULT_POSITION,
						GUIParameters.getLeftFont()), javax.swing.BorderFactory
						.createEmptyBorder(-15, 30, 0, 0)
		));
	}

	@Override
	protected void paintComponent(Graphics g) {
		// Draw round rect
		g.setColor(getBackground());
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);

		// Draw the search icon
		try {
			BufferedImage img = ImageIO.read(new File("assets/img/search.png"));
			g.drawImage(img, 5, 3, 24, 24, this);
		} catch (IOException e) {
			logger.log(Level.WARNING, e.getMessage());
		}

		super.paintComponent(g);

		// Placeholder
		if (getText().isEmpty()
				&& FocusManager.getCurrentKeyboardFocusManager()
				.getFocusOwner() != this) {
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setColor(GUIParameters.BORDER);
			g2.setFont(getFont().deriveFont(Font.ITALIC));
			g2.drawString(MainWindow.bundle.getString("search"), 30, 20);
			g2.dispose();
		}
	}

	@Override
	protected void paintBorder(Graphics g) {
		g.setColor(GUIParameters.BORDER);
		g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
	}

	@Override
	public boolean contains(int x, int y) {
		if (shape == null || !shape.getBounds().equals(getBounds())) {
			shape = new RoundRectangle2D.Float(0, 0, getWidth() - 1,
					getHeight() - 1, 30, 30);
		}
		return shape.contains(x, y);
	}
}
