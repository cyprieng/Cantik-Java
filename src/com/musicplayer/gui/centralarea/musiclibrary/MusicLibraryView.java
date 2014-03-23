package com.musicplayer.gui.centralarea.musiclibrary;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.MutableTreeTableNode;

import com.musicplayer.core.Log;
import com.musicplayer.core.musiclibrary.ArtistInfo;
import com.musicplayer.core.musiclibrary.MusicLibrary;
import com.musicplayer.core.musiclibrary.SearchMusicLibrary;
import com.musicplayer.core.playlist.Playlist;
import com.musicplayer.core.song.Song;
import com.musicplayer.gui.CustomScrollBar;
import com.musicplayer.gui.GUIParameters;
import com.musicplayer.gui.centralarea.CentralArea;
import com.musicplayer.gui.centralarea.CustomTableHeader;

/**
 * View of the music library
 * 
 * @author cyprien
 * 
 */
public class MusicLibraryView extends CentralArea {
	private static final long serialVersionUID = -617334771645897532L;

	/**
	 * Model of the tree table
	 */
	private DefaultTreeTableModel model;

	/**
	 * The tree table
	 */
	private JXTreeTable tree;

	/**
	 * Store the view
	 */
	private static MusicLibraryView mlv;

	/**
	 * Get the music library view
	 * 
	 * @return The view
	 */
	public static MusicLibraryView getMusiLibraryView() {
		if (mlv == null) {
			mlv = new MusicLibraryView();
		}

		return mlv;
	}

	/**
	 * Create the tree table
	 */
	private MusicLibraryView() {
		// Create JXTreeTable
		DefaultMutableTreeTableNode root = new DefaultMutableTreeTableNode(
				"Library");
		model = new CustomTreeTableModel(root);
		tree = new JXTreeTable(model);

		// Click event
		final JXTreeTable copy = tree;
		tree.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
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
					// Double click => add the selected song
					TreePath path = copy.getPathForRow(copy.rowAtPoint(e
							.getPoint()));
					Object node = path.getLastPathComponent();

					if (node instanceof DefaultMutableTreeTableNode) {
						Object o = ((DefaultMutableTreeTableNode) node)
								.getUserObject();

						if (o instanceof Song) { // Add one song
							Playlist.getPlaylist().addSong((Song) o);
						} else if (((DefaultMutableTreeTableNode) node)
								.getParent() == copy.getTreeTableModel()
								.getRoot()) { // Add an artist
							Playlist.getPlaylist().addSongSet(
									MusicLibrary.getMusicLibrary().getSongs(
											(String) o));
						} else { // Add an album
							Playlist.getPlaylist()
									.addSongSet(
											MusicLibrary
													.getMusicLibrary()
													.getSongs(
															(String) ((DefaultMutableTreeTableNode) node)
																	.getParent()
																	.getUserObject(),
															(String) o));
						}
					}
				}
			}
		});

		// Style
		tree.setTreeCellRenderer(new CustomTreeCellRenderer());
		tree.setRowHeight(50);
		tree.setFont(GUIParameters.getCentralFont().deriveFont(15.0f));
		CustomTableHeader.customizeHeader(tree.getTableHeader());

		// Stripes
		HighlightPredicate hp = new HighlightPredicate() {
			@Override
			public boolean isHighlighted(Component renderer,
					ComponentAdapter adapter) {
				return adapter.row % 2 == 0;
			}
		};
		ColorHighlighter h = new ColorHighlighter(hp,
				GUIParameters.TABLE_EVEN_ROW, null);
		tree.addHighlighter(h);

		// Selection color
		tree.setSelectionBackground(GUIParameters.LEFTBAR_BACKGROUND);
		tree.setSelectionForeground(Color.WHITE);

		// Icon
		tree.setOpenIcon(new ImageIcon(ArtistInfo.getArtistImage()));
		tree.setClosedIcon(new ImageIcon(ArtistInfo.getArtistImage()));
		tree.setLeafIcon(null);

		try {
			tree.setCollapsedIcon(new ImageIcon(ImageIO.read(new File(
					"assets/img/collapsed.png"))));
			tree.setExpandedIcon(new ImageIcon(ImageIO.read(new File(
					"assets/img/expanded.png"))));
		} catch (IOException e) {
			Log.addEntry(e);
		}

		add(CustomScrollBar.getCustomJScrollPane(tree));

		showLibrary(null);
	}

	/**
	 * Show the library for the given query
	 * 
	 * @param query
	 *            The query to search in the library
	 */
	public void showLibrary(final String query) {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				// Reset tree table
				DefaultMutableTreeTableNode root = new DefaultMutableTreeTableNode(
						"Library");
				model.setRoot(root);
				tree.repaint();
				CustomTableHeader.customizeHeader(tree.getTableHeader());

				tree.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)); // Load
																				// cursor

				// Get library
				MusicLibrary library;
				if (query == null)
					library = MusicLibrary.getMusicLibrary();
				else
					library = new SearchMusicLibrary(query);

				// Wait library
				synchronized (library) {
					while (!library.isReady()) {
						try {
							library.wait();
						} catch (InterruptedException e) {
							Log.addEntry(e);
						}
					}
				}

				// Add nodes
				for (String s : library.getArtists()) { // Artists
					DefaultMutableTreeTableNode artist = new DefaultMutableTreeTableNode(
							s);
					model.insertNodeInto(artist, (MutableTreeTableNode) root,
							root.getChildCount());

					for (String al : library.getAlbums(s)) { // Albums
						DefaultMutableTreeTableNode album = new DefaultMutableTreeTableNode(
								al);
						model.insertNodeInto(album, artist,
								artist.getChildCount());

						for (Song song : library.getSongs(s, al)) { // Songs
							model.insertNodeInto(
									new DefaultMutableTreeTableNode(song),
									album, album.getChildCount());
						}
					}
				}

				tree.setCursor(Cursor.getDefaultCursor()); // Reset cursor
			}
		});

		t.start();
	}
}