package com.musicplayer.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicScrollBarUI;

import com.musicplayer.core.Log;

/**
 * Custom UI for scrollbar
 * 
 * @author cyprien
 * 
 */
public class CustomScrollBar extends BasicScrollBarUI {
	/**
	 * Get a {@link JScrollPane} with the custom scrollbar ui
	 * 
	 * @param view
	 *            The view to pass to the scrollpane constructor
	 * @return The custom {@link JScrollPane}
	 */
	public static JScrollPane getCustomJScrollPane(Component view) {
		JScrollPane jsp = new JScrollPane(view);

		// Vertical scrollbar
		JScrollBar sbv = new JScrollBar(JScrollBar.VERTICAL);
		final JScrollBar copyv = sbv;
		sbv.getModel().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				copyv.repaint(); // Repaint when moving
			}
		});
		sbv.setUI(new CustomScrollBar());
		sbv.setUnitIncrement(16);
		jsp.setVerticalScrollBar(sbv);

		// Horizontal scrollbar
		JScrollBar sbh = new JScrollBar(JScrollBar.HORIZONTAL);
		final JScrollBar copyh = sbh;
		sbh.getModel().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				copyh.repaint();// Repaint when moving
			}
		});
		sbh.setUI(new CustomScrollBar());
		sbh.setUnitIncrement(16);
		jsp.setHorizontalScrollBar(sbh);

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
			Log.addEntry(e);
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
			Log.addEntry(e);
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
