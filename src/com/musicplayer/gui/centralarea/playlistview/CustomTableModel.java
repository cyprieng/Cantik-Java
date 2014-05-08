package com.musicplayer.gui.centralarea.playlistview;

import com.musicplayer.core.playlist.Playlist;

import javax.swing.table.DefaultTableModel;

/**
 * Custom table model without editing, and reorderable
 *
 * @author cyprien
 */
public class CustomTableModel extends DefaultTableModel implements Reorderable {
	private static final long serialVersionUID = 395762231811337795L;

	/**
	 * Call super constructor
	 *
	 * @param columnNames
	 * 		Names of the columns
	 * @param i
	 * 		Number of row
	 */
	public CustomTableModel(String[] columnNames, int i) {
		super(columnNames, i);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		// No editing
		return false;
	}

	/**
	 * Reorder table
	 *
	 * @param fromIndex
	 * 		Row to move
	 * @param toIndex
	 * 		Index where the row will be after
	 */
	public void reorder(int fromIndex, int toIndex) {
		Playlist.getPlaylist().reorder(fromIndex, toIndex);
	}
}
