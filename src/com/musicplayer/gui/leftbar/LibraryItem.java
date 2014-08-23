package com.musicplayer.gui.leftbar;

import com.musicplayer.core.musiclibrary.MusicLibrary;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.Observable;
import java.util.Observer;

/**
 * Specific Item for library
 *
 * @author cyprien
 */
public class LibraryItem extends Item implements Observer {
	/**
	 * Loading icon
	 */
	private JLabel loading;

	/**
	 * Init loading icon
	 *
	 * @param str
	 * 		Text of the Item
	 */
	public LibraryItem(String str) {
		super(str);

		// Create loading icon
		loading = new JLabel(new ImageIcon("assets/img/loader.gif"));
		loading.setBorder(new EmptyBorder(0, 10, 0, 0));
		loading.setVisible(false);
		add(loading);

		// Add to observer & update
		MusicLibrary.getMusicLibrary().addObserver(this);
		update(MusicLibrary.getMusicLibrary(), null);
	}

	@Override
	public void update(Observable o, Object arg) {
		// Show / hide loading icon
		if (MusicLibrary.getMusicLibrary().isReady())
			loading.setVisible(false);
		else
			loading.setVisible(true);
	}
}
