package com.musicplayer.gui.centralarea;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Custom JPanel with scrollbar
 * 
 * @author cyprien
 * 
 */
public class CentralArea extends JPanel {
	private static final long serialVersionUID = -9189414251386758801L;

	/**
	 * Content of the jpanel
	 */
	protected JPanel content;

	/**
	 * Init JPanel with scrollbars
	 */
	public CentralArea() {
		super();
		super.setLayout(new BorderLayout());

		content = new JPanel();
		content.setBackground(Color.WHITE);

		// Add scrollbars
		JScrollPane scrollPane = new JScrollPane(content);
		scrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);

		super.add(scrollPane);
	}
}
