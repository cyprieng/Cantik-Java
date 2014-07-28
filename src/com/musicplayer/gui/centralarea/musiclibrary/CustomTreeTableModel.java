package com.musicplayer.gui.centralarea.musiclibrary;

import com.musicplayer.core.Core;
import com.musicplayer.core.musiclibrary.MusicLibrary;
import com.musicplayer.core.playlist.Playlist;
import com.musicplayer.core.song.Song;
import com.musicplayer.gui.MainWindow;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;

import java.util.Set;

/**
 * Model for the music library view
 *
 * @author cyprien
 */
public class CustomTreeTableModel extends DefaultTreeTableModel {
	/**
	 * Name of columns
	 */
	private final static String[] COLUMN_NAMES = {MainWindow.bundle.getString("title"), "",
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
				case 2:
					return ((Song) o).getYear();
				case 3:
					return Core.stringifyDuration(((Song) o).getDuration());
			}
		}

		// Get added value
		if (column == 1) {
			if (o instanceof Song) { // Song
				if (Playlist.getPlaylist().getSongList().contains((Song) o)) { // Song is in playlist
					return "✓";
				}
			} else if (o instanceof String) {
				if (((DefaultMutableTreeTableNode) node).getParent() != this.getRoot()
						&& ((DefaultMutableTreeTableNode) node).getParent() != null) { // Album
					// Get album songs
					Set<Song> songs = MusicLibrary.getMusicLibrary().getSongs((String) ((DefaultMutableTreeTableNode) node)
							.getParent().getUserObject(), (String) o);
					boolean added = true;

					// Check if all songs are in playlist
					for (Song s : songs) {
						if (!Playlist.getPlaylist().getSongList().contains(s)) {
							added = false;
							break;
						}
					}

					if (added)
						return "✓";
				} else { // Artist
					// Get artists songs
					Set<Song> songs = MusicLibrary.getMusicLibrary().getSongs((String) o);
					boolean added = true;

					// Check if all songs are in playlist
					for (Song s : songs) {
						if (!Playlist.getPlaylist().getSongList().contains(s)) {
							added = false;
							break;
						}
					}

					if (added)
						return "✓";
				}
			}
		}

		return super.getValueAt(node, column);
	}
}
