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

		// Add "delete" action
		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg) {
				Playlist.getPlaylist().removeSong(index); // Remove song
			}
		});
	}
}
