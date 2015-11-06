package com.cantik.gui.centralarea.playlistview;

import com.cantik.core.playlist.Playlist;
import com.cantik.gui.MainWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Popup menu for the row of the playlist
 *
 * @author cyprien
 */
public class PlaylistPopUpMenu extends JPopupMenu {
	private static final long serialVersionUID = 687098535945310862L;

	/**
	 * Init menu
	 *
	 * @param index
	 * 		The row index of the menu
	 */
	public PlaylistPopUpMenu(final int index) {
		super();

		// Add item "delete"
		JMenuItem delete = new JMenuItem(MainWindow.bundle.getString("removeFromPlaylist"));
		add(delete);
		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg) {
				Playlist.getPlaylist().removeSong(index); // Remove song
			}
		});

		addSeparator();

		// Add item "stop track"
		JMenuItem stopTrack = new JMenuItem(MainWindow.bundle.getString("stopAfter"));
		add(stopTrack);
		stopTrack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg) {
				Playlist.getPlaylist().setStopTrack(index); // Set stop track
			}
		});

		// Add item "unset stop track"
		JMenuItem stopTrackUnset = new JMenuItem(MainWindow.bundle.getString("unsetStopAfter"));
		add(stopTrackUnset);
		stopTrackUnset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg) {
				Playlist.getPlaylist().setStopTrack(-1); // Unset stop track
			}
		});
	}
}
