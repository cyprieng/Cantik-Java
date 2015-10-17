package com.cantik.gui.centralarea;

import com.cantik.gui.CustomScrollBar;

import javax.swing.*;
import java.awt.*;

/**
 * Custom JPanel with scrollbar
 *
 * @author cyprien
 */
public class CentralArea extends JPanel {
	private static final long serialVersionUID = -9189414251386758801L;

	/**
	 * Content of the jpanel
	 */
	protected JPanel content;

	/**
	 * Information label
	 */
	protected JLabel info;

	/**
	 * Scrollpane of content
	 */
	private JScrollPane jsp;

	/**
	 * Init JPanel with scrollbars
	 */
	public CentralArea() {
		super();
		super.setLayout(new BorderLayout());

		// Create content with cardlayout
		content = new JPanel(new CardLayout());
		content.setBackground(Color.WHITE);

		// Create panel for info
		JPanel panelInfo = new JPanel();
		info = new CenterJLabel("");
		panelInfo.add(info);
		content.add(panelInfo, "info");

		// Add scrollbar
		jsp = CustomScrollBar.getCustomJScrollPane(content, CustomScrollBar.BOTH);
		super.add(jsp);
	}

	/**
	 * Add component to this panel
	 *
	 * @param c
	 * 		Component to add
	 * @return The given component
	 */
	public Component add(Component c) {
		// By default: not in the center, and special component
		add(c, false, true);

		return c;
	}

	/**
	 * Add component to this panel
	 *
	 * @param c
	 * 		The component to add
	 * @param centerContent
	 * 		Component centered or not
	 * @param specialComponent
	 * 		Component is special or not (we need to set his size to 0 to ensure info label is centered)
	 * @return The given component
	 */
	public Component add(Component c, boolean centerContent, boolean specialComponent) {
		JPanel jp;

		// Center or not
		if (centerContent)
			jp = new JPanel();
		else
			jp = new JPanel(new BorderLayout());

		jp.add(c);

		// We set size to 0. If we do not do that, info label won't be centered
		if (specialComponent) {
			jp.setMinimumSize(new Dimension(0, 0));
			jp.setPreferredSize(new Dimension(0, 0));
		}

		// Add and show content
		content.add(jp, "content");
		CardLayout cl = (CardLayout) (content.getLayout());
		cl.show(content, "content");

		return c;
	}

	/**
	 * Show the info label
	 */
	public void showInfo() {
		CardLayout cl = (CardLayout) (content.getLayout());
		cl.show(content, "info");
	}

	/**
	 * Hide info label (show content)
	 */
	public void hideInfo() {
		CardLayout cl = (CardLayout) (content.getLayout());
		cl.show(content, "content");
	}

	/**
	 * Hide scrollbar of this panel (for example if you already have scrollbar in your child component)
	 */
	public void hideScrollbar() {
		jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
	}

	/**
	 * Show scrollbar
	 */
	public void showScrollbar() {
		jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	}
}
