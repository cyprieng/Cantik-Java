package com.musicplayer.gui.centralarea.playlistview;

import com.musicplayer.core.playlist.Playlist;

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
	 * Item "delete"
	 */
	private JMenuItem delete;

	/**
	 * Item "stop track"
	 */
	private JMenuItem stopTrack;

	/**
	 * Item "unset stop track"
	 */
	private JMenuItem stopTrackUnset;

	/**
	 * Init menu
	 *
	 * @param index
	 * 		The row index of the menu
	 */
	public PlaylistPopUpMenu(final int index) {
		super();

		// Add item "delete"
		delete = new JMenuItem("Remove from playlist");
		add(delete);
		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg) {
				Playlist.getPlaylist().removeSong(index); // Remove song
			}
		});

		addSeparator();

		// Add item "stop track"
		stopTrack = new JMenuItem("Stop after this song");
		add(stopTrack);
		stopTrack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg) {
				Playlist.getPlaylist().setStopTrack(index); // Set stop track
			}
		});

		// Add item "unset stop track"
		stopTrackUnset = new JMenuItem("Unset 'stop after this song'");
		add(stopTrackUnset);
		stopTrackUnset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg) {
				Playlist.getPlaylist().setStopTrack(-1); // Unset stop track
			}
		});
	}
}
