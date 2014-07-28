package com.musicplayer.gui.centralarea.playlistview;

import com.musicplayer.core.Core;
import com.musicplayer.core.playlist.Playlist;
import com.musicplayer.core.song.Song;
import com.musicplayer.gui.CustomScrollBar;
import com.musicplayer.gui.GUIParameters;
import com.musicplayer.gui.MainWindow;
import com.musicplayer.gui.centralarea.CentralArea;
import com.musicplayer.gui.centralarea.CustomTableHeader;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

/**
 * View of the playlist
 *
 * @author cyprien
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

		// Create table model
		String[] columnNames = {MainWindow.bundle.getString("title"), MainWindow.bundle.getString("artist"),
				MainWindow.bundle.getString("album"), MainWindow.bundle.getString("length")};
		tableModel = new CustomTableModel(columnNames, 0);

		// Create custom Table
		table = new JTable(tableModel) {
			private static final long serialVersionUID = 2010293022314400308L;

			@Override
			public Component prepareRenderer(TableCellRenderer renderer,
											 int rowIndex, int vColIndex) {
				JComponent c = (JComponent) super.prepareRenderer(renderer, rowIndex,
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

				for (int i = 0; i < table.getSelectedRowCount(); i++) {
					if (table.getSelectedRows()[i] == rowIndex) {
						// Selected tracks
						c.setBackground(GUIParameters.LEFTBAR_BACKGROUND);
						c.setForeground(Color.WHITE);
					}
				}

				// Add line below stop after track
				if (rowIndex == Playlist.getPlaylist().getStopTrack()) {
					c.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
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
		table.setGridColor(GUIParameters.TABLE_EVEN_ROW);
		table.setDragEnabled(true);
		table.setDropMode(DropMode.INSERT_ROWS);
		table.setTransferHandler(new TableRowTransferHandler(table));
		table.setRowSelectionAllowed(true);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		// Show scrollbar
		JScrollPane jsp = CustomScrollBar.getCustomJScrollPane(table);
		jsp.setBackground(GUIParameters.LEFTBAR_BACKGROUND);
		add(jsp);

		// Catch delete key press
		table.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_DELETE) { // Delete key
					Playlist.getPlaylist().removeSongs(table.getSelectedRows());
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {

			}
		});

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

		info.setText(MainWindow.bundle.getString("emptyPlaylist"));
		update(null, null); // Update to initialize the table
	}

	@Override
	public void update(Observable playlist, Object arg) {
		tableModel.setRowCount(0); // Reset table
		hideInfo();

		// Add every song of the playlist
		for (Song s : Playlist.getPlaylist().getSongList()) {
			// Add row
			tableModel.addRow(new Object[]{s.getTitle(), s.getArtist(),
					s.getAlbum(), Core.stringifyDuration(s.getDuration())});
		}

		if (tableModel.getRowCount() == 0) { // Show text for empty playlist
			showInfo();
		}

		table.repaint();
	}
}
