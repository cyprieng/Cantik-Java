package com.musicplayer.gui.centralarea.playlistview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DropMode;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.musicplayer.core.Core;
import com.musicplayer.core.playlist.Playlist;
import com.musicplayer.core.song.Song;
import com.musicplayer.gui.CustomScrollBar;
import com.musicplayer.gui.GUIParameters;
import com.musicplayer.gui.centralarea.CentralArea;
import com.musicplayer.gui.centralarea.CustomTableHeader;

/**
 * View of the playlist
 * 
 * @author cyprien
 * 
 */
public class PlaylistView extends CentralArea implements Observer {
	private static final long serialVersionUID = -7003326588092613641L;

	/**
	 * Custom model of the playlist table
	 */
	private DefaultTableModel tableModel;

	/**
	 * Table containing the list of songs in the playlist
	 */
	private JTable table;

	/**
	 * Init the playlist
	 */
	public PlaylistView() {
		super();
		Playlist.getPlaylist().addObserver(this);
		content.setLayout(new BorderLayout());

		// Create table model
		String[] columnNames = { "Title", "Artist", "Album", "Length" };
		tableModel = new CustomTableModel(columnNames, 0);

		// Create custom Table
		table = new JTable(tableModel) {
			private static final long serialVersionUID = 2010293022314400308L;

			@Override
			public Component prepareRenderer(TableCellRenderer renderer,
					int rowIndex, int vColIndex) {
				Component c = super.prepareRenderer(renderer, rowIndex,
						vColIndex);

				if (rowIndex == Playlist.getPlaylist().getCurrentTrack()) {
					// Current track
					c.setBackground(GUIParameters.LEFTBAR_ACTIVE);
					c.setForeground(Color.WHITE);
				} else {
					// Other tracks
					c.setBackground(getRowColor(rowIndex));
					c.setForeground(getForeground());
				}

				if (table.getSelectedRow() == rowIndex) {
					// Selected tracks
					c.setBackground(GUIParameters.LEFTBAR_BACKGROUND);
					c.setForeground(Color.WHITE);
				}

				return c;
			}

			/**
			 * Get the color of the row
			 * 
			 * @param row
			 *            The row which we want the color
			 * @return The color of the row
			 */
			private Color getRowColor(int row) {
				if (row % 2 == 0) // Even line
					return Color.WHITE;
				else
					return GUIParameters.TABLE_EVEN_ROW;
			}
		};

		// Table header style
		CustomTableHeader.customizeHeader(table.getTableHeader());

		// Settings of table
		table.setFont(GUIParameters.getCentralFont().deriveFont(15.0f));
		table.setGridColor(GUIParameters.TABLE_EVEN_ROW);
		table.setDragEnabled(true);
		table.setDropMode(DropMode.INSERT_ROWS);
		table.setTransferHandler(new TableRowTransferHandler(table));
		add(CustomScrollBar.getCustomJScrollPane(table));

		// Mouse listener for the popup menu
		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					doPop(e); // Pop

					// Select item
					ListSelectionModel model = table.getSelectionModel();
					model.setSelectionInterval(table.rowAtPoint(e.getPoint()),
							table.rowAtPoint(e.getPoint()));
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					doPop(e); // Pop

					// Select menu
					ListSelectionModel model = table.getSelectionModel();
					model.setSelectionInterval(table.rowAtPoint(e.getPoint()),
							table.rowAtPoint(e.getPoint()));
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					// Double click => play the selected song
					Playlist.getPlaylist().play(table.getSelectedRow());
				}
			}

			/**
			 * Show the popup menu
			 * 
			 * @param e
			 *            The MouseEvent calling the menu
			 */
			private void doPop(MouseEvent e) {
				PlaylistPopUpMenu menu = new PlaylistPopUpMenu(table
						.rowAtPoint(e.getPoint()));
				menu.show(e.getComponent(), e.getX(), e.getY());
			}
		});

		update(null, null); // Update to initialize the table
	}

	@Override
	public void update(Observable playlist, Object arg) {
		tableModel.setRowCount(0); // Reset table

		// Add every song of the playlist
		for (Song s : Playlist.getPlaylist().getSongList()) {
			// Add row
			tableModel.addRow(new Object[] { s.getTitle(), s.getArtist(),
					s.getAlbum(), Core.stringifyDuration(s.getDuration()) });
		}
	}
}
