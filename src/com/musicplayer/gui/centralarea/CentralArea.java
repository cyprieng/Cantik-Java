package com.musicplayer.gui.centralarea;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;

import com.musicplayer.gui.CustomScrollBar;

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

		super.add(CustomScrollBar.getCustomJScrollPane(content));
	}
}
