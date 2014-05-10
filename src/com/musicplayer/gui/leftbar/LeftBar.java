package com.musicplayer.gui.leftbar;

import com.musicplayer.gui.GUIParameters;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Custom Jpanel showing all component of the left bar
 *
 * @author cyprien
 */
public class LeftBar extends JPanel {
	private static final long serialVersionUID = -2680840975440683979L;

	/**
	 * Add all elements
	 */
	public LeftBar() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBackground(GUIParameters.LEFTBAR_BACKGROUND);

		// Add search field
		JPanel searchWrap = new JPanel();
		searchWrap.setBackground(GUIParameters.LEFTBAR_BACKGROUND);
		searchWrap.setPreferredSize(new Dimension(250, 60));
		searchWrap.setMaximumSize(searchWrap.getPreferredSize());
		searchWrap.setMinimumSize(searchWrap.getPreferredSize());
		SearchField search = new SearchField();
		search.setAlignmentX(Component.CENTER_ALIGNMENT);
		searchWrap.add(search);
		Border empty = new EmptyBorder(20, 0, 0, 0);
		searchWrap.setBorder(empty);

		// Item active by default
		Item defaultItem = new Item("Library");
		defaultItem.setActive(true);

		// Add items
		this.add(searchWrap);
		this.add(new Separator());

		this.add(new CategoryLabel("collection"));
		this.add(defaultItem);
		this.add(new Item("Local File"));
		this.add(new Separator());

		this.add(new CategoryLabel("Now Playing"));
		this.add(new Item("Playlist"));
		this.add(new Item("Info"));
		this.add(new Separator());

		this.add(new Item("Settings"));
		this.add(new Separator());
	}
}
