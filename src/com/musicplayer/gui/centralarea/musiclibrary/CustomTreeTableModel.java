package com.musicplayer.gui.centralarea.musiclibrary;

import com.musicplayer.core.Core;
import com.musicplayer.core.song.Song;
import com.musicplayer.gui.MainWindow;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;

/**
 * Model for the music library view
 *
 * @author cyprien
 */
public class CustomTreeTableModel extends DefaultTreeTableModel {
	/**
	 * Name of columns
	 */
	private final static String[] COLUMN_NAMES = {MainWindow.bundle.getString("title"),
			MainWindow.bundle.getString("year"), MainWindow.bundle.getString("length")};

	/**
	 * Call super constructor
	 *
	 * @param root
	 * 		The root node
	 */
	public CustomTreeTableModel(DefaultMutableTreeTableNode root) {
		super(root);
	}

	@Override
	public int getColumnCount() {
		return COLUMN_NAMES.length;
	}

	@Override
	public String getColumnName(int column) {
		return COLUMN_NAMES[column];
	}

	@Override
	public boolean isCellEditable(Object node, int column) {
		return false;
	}

	@Override
	public Object getValueAt(Object node, int column) {
		Object o = ((DefaultMutableTreeTableNode) node).getUserObject();

		// Get column value
		if (o instanceof Song) {
			switch (column) {
				case 0:
					return ((Song) o).getTitle();
				case 1:
					return ((Song) o).getYear();
				case 2:
					return Core.stringifyDuration(((Song) o).getDuration());
			}
		}

		return super.getValueAt(node, column);
	}
}
