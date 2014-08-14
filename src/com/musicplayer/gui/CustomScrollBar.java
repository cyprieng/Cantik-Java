package com.musicplayer.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Custom UI for scrollbar
 *
 * @author cyprien
 */
public class CustomScrollBar extends BasicScrollBarUI {
	/**
	 * Logger for the class
	 */
	private static Logger logger = Logger.getLogger(CustomScrollBar.class.getName());

	/**
	 * Vertical scrollbar only
	 */
	public static final int VERTICAL = JScrollBar.VERTICAL;

	/**
	 * Horizontal scrollbar only
	 */
	public static final int HORIZONTAL = JScrollBar.HORIZONTAL;

	/**
	 * Vertical & horizontal scrollbars
	 */
	public static final int BOTH = -1;

	/**
	 * Get a {@link JScrollPane} with the custom scrollbar ui
	 *
	 * @param view
	 * 		The view to pass to the scrollpane constructor
	 * @param orientation
	 * 		Orientation of the scrollbar {@link #VERTICAL}, {@link #HORIZONTAL}, {@link #BOTH}
	 * @return The custom {@link JScrollPane}
	 */
	public static JScrollPane getCustomJScrollPane(Component view, int orientation) {
		JScrollPane jsp = new JScrollPane(view);

		if (orientation == VERTICAL || orientation == BOTH) {
			// Vertical scrollbar
			JScrollBar sbv = new JScrollBar(VERTICAL);
			final JScrollBar copyv = sbv;
			sbv.getModel().addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					copyv.repaint(); // Repaint when moving
				}
			});
			sbv.setUI(new CustomScrollBar());
			sbv.setUnitIncrement(30);
			jsp.setVerticalScrollBar(sbv);
		} else
			jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

		if (orientation == HORIZONTAL || orientation == BOTH) {
			// Horizontal scrollbar
			JScrollBar sbh = new JScrollBar(HORIZONTAL);
			final JScrollBar copyh = sbh;
			sbh.getModel().addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					copyh.repaint();// Repaint when moving
				}
			});
			sbh.setUI(new CustomScrollBar());
			sbh.setUnitIncrement(30);
			jsp.setHorizontalScrollBar(sbh);
		} else
			jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		return jsp;
	}

	@Override
	protected JButton createDecreaseButton(int orientation) {
		JButton btn = new JButton("");

		// Disable border
		Border emptyBorder = BorderFactory.createEmptyBorder();
		btn.setBorder(emptyBorder);
		btn.setBackground(new Color(0, 0, 0, 0));

		// Set icon
		try {
			BufferedImage img;

			if (scrollbar.getOrientation() == JScrollBar.VERTICAL)
				img = ImageIO.read(new File("assets/img/up.png"));
			else
				img = ImageIO.read(new File("assets/img/left.png"));

			btn.setIcon(new ImageIcon(img));
		} catch (IOException e) {
			logger.log(Level.WARNING, e.getMessage());
		}

		return btn;
	}

	@Override
	protected JButton createIncreaseButton(int orientation) {
		JButton btn = new JButton("");

		// Disable border
		Border emptyBorder = BorderFactory.createEmptyBorder();
		btn.setBorder(emptyBorder);
		btn.setBackground(new Color(0, 0, 0, 0));

		// Set icon
		try {
			BufferedImage img;

			if (scrollbar.getOrientation() == JScrollBar.VERTICAL)
				img = ImageIO.read(new File("assets/img/down.png"));
			else
				img = ImageIO.read(new File("assets/img/right.png"));

			btn.setIcon(new ImageIcon(img));
		} catch (IOException e) {
			logger.log(Level.WARNING, e.getMessage());
		}

		return btn;
	}

	@Override
	protected void paintDecreaseHighlight(Graphics g) {
		Insets insets = scrollbar.getInsets();
		Rectangle thumbR = getThumbBounds();
		g.setColor(new Color(137, 144, 144));

		if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
			int x = insets.left + decrButton.getWidth() / 2 - 2;
			int y = decrButton.getY() + decrButton.getHeight();
			int w = 4;
			int h = thumbR.y - y;
			g.fillRect(x, y, w, h);
		} else {
			int x, w;
			if (scrollbar.getComponentOrientation().isLeftToRight()) {
				x = decrButton.getX() + decrButton.getWidth();
				w = thumbR.x - x;
			} else {
				x = thumbR.x + thumbR.width;
				w = decrButton.getX() - x;
			}
			int y = insets.top + decrButton.getHeight() / 2 - 2;
			int h = 4;

			g.fillRect(x, y, w + 1, h);
		}
	}

	@Override
	protected void paintIncreaseHighlight(Graphics g) {
		Insets insets = scrollbar.getInsets();
		Rectangle thumbR = getThumbBounds();
		g.setColor(new Color(202, 207, 203));

		if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
			int x = insets.left + decrButton.getWidth() / 2 - 2;
			int y = thumbR.y;
			int w = 4;
			int h = incrButton.getY() - y;
			g.fillRect(x, y, w, h);
		} else {
			int x = thumbR.x + thumbR.width;
			int w = incrButton.getX() - x;
			int y = insets.top + decrButton.getHeight() / 2 - 2;
			int h = 4;

			g.fillRect(x, y, w + 1, h);
		}
	}

	@Override
	protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
		g.setColor(Color.WHITE);
		g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width,
				trackBounds.height);
		paintDecreaseHighlight(g);
		paintIncreaseHighlight(g);

	}

	@Override
	protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
		if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
			return;
		}

		int w = 16;
		int h = 16;

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		g.translate(thumbBounds.x, thumbBounds.y);

		GradientPaint gr = new GradientPaint(2, 0, new Color(158, 161, 162),
				18, 16, new Color(96, 99, 98));
		g2.setPaint(gr);
		g2.fill(new Ellipse2D.Double(2, 0, w - 1, h - 1));

		g2.setPaint(new Color(203, 207, 203));
		g2.fill(new Ellipse2D.Double(6, 4, 7, 7));

		g.translate(-thumbBounds.x, -thumbBounds.y);
	}

	@Override
	protected void setThumbBounds(int x, int y, int width, int height) {
		super.setThumbBounds(x, y, 14, 14);
	}

	@Override
	protected Rectangle getThumbBounds() {
		return new Rectangle(super.getThumbBounds().x,
				super.getThumbBounds().y, 14, 14);
	}

	@Override
	protected Dimension getMinimumThumbSize() {
		return new Dimension(14, 14);
	}

	@Override
	protected Dimension getMaximumThumbSize() {
		return new Dimension(14, 14);
	}
}
