package com.cantik.gui.centralarea.trackartist;

import com.cantik.core.song.Song;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Model for the TopTracks table
 *
 * @author cyprien
 * @see TrackArtist
 */
public class TopTracksModel extends DefaultTableModel {
	/**
	 * ImageIcon for play
	 */
	private ImageIcon play;

	/**
	 * Call super constructor
	 *
	 * @param columnNames
	 * 		Names of the columns
	 * @param i
	 * 		Number of row
	 */
	public TopTracksModel(String[] columnNames, int i) {
		super(columnNames, i);

		play = new ImageIcon("assets/img/play_mini.png");
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == 2) {
			Object o = super.getValueAt(rowIndex, columnIndex);

			// Song => we can play
			if (o instanceof Song)
				return play;
		}

		return super.getValueAt(rowIndex, columnIndex);
	}

	/**
	 * Get the value of a cell without formatting ({@link #getValueAt(int, int)} ()})
	 *
	 * @param rowIndex
	 * 		Index of the row
	 * @param columnIndex
	 * 		Index of the column
	 * @return Value of the cell
	 */
	public Object getRawValueAt(int rowIndex, int columnIndex) {
		return super.getValueAt(rowIndex, columnIndex);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		// No editing
		return false;
	}

	@Override
	public Class getColumnClass(int column) {
		return getValueAt(0, column).getClass();
	}
}
