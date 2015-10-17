package com.cantik.gui.centralarea.musiclibrary;

import com.cantik.core.musiclibrary.ArtistInfo;
import com.cantik.core.musiclibrary.MusicLibrary;
import com.cantik.core.musiclibrary.SearchMusicLibrary;
import com.cantik.core.playlist.Playlist;
import com.cantik.core.song.Song;
import com.cantik.gui.CustomScrollBar;
import com.cantik.gui.GUIParameters;
import com.cantik.gui.MainWindow;
import com.cantik.gui.centralarea.CentralArea;
import com.cantik.gui.centralarea.CustomTableHeader;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.MutableTreeTableNode;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * View of the music library
 *
 * @author cyprien
 */
public class MusicLibraryView extends CentralArea implements Observer {
	private static final long serialVersionUID = -617334771645897532L;

	/**
	 * Logger for the class
	 */
	private static Logger logger = Logger.getLogger(MusicLibraryView.class.getName());

	/**
	 * Store the view
	 */
	private static MusicLibraryView mlv;

	/**
	 * Model of the tree table
	 */
	private CustomTreeTableModel model;

	/**
	 * The tree table
	 */
	private JXTreeTable tree;

	/**
	 * Store the query
	 */
	private String query;

	/**
	 * Define either the library is loading or not
	 */
	private boolean loading;

	/**
	 * List of observers
	 */
	private ArrayList<Observer> listeners;

	/**
	 * Create the tree table
	 */
	private MusicLibraryView() {
		MusicLibrary.getMusicLibrary().addObserver(this);

		loading = false;
		listeners = new ArrayList<>();

		// Create JXTreeTable
		DefaultMutableTreeTableNode root = new DefaultMutableTreeTableNode(
				"Library");
		model = new CustomTreeTableModel(root);
		tree = new JXTreeTable(model);

		// Click event
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					// Double click => add the selected song
					addCurrentNodesToPlaylist();
				}
			}
		});

		// Keyboard event
		tree.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					addCurrentNodesToPlaylist();
				}
			}
		});

		// Style
		tree.setTreeCellRenderer(new CustomTreeCellRenderer(tree));
		tree.setRowHeight(50);
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
		tree.setSelectionBackground(GUIParameters.LEFTBAR_ACTIVE);
		tree.setSelectionForeground(Color.WHITE);

		// Icon
		tree.setOpenIcon(ArtistInfo.getDefaultArtistImage());
		tree.setClosedIcon(ArtistInfo.getDefaultArtistImage());
		tree.setLeafIcon(null);

		try {
			tree.setCollapsedIcon(new ImageIcon(ImageIO.read(new File(
					"assets/img/collapsed.png"))));
			tree.setExpandedIcon(new ImageIcon(ImageIO.read(new File(
					"assets/img/expanded.png"))));
		} catch (IOException e) {
			logger.log(Level.WARNING, e.getMessage());
		}

		// Show scrollbar
		JScrollPane jsp = CustomScrollBar.getCustomJScrollPane(tree, CustomScrollBar.BOTH);
		jsp.setBackground(GUIParameters.LEFTBAR_BACKGROUND);
		add(jsp);

		hideScrollbar(); // Hide parent scrollbar

		showLibrary(null);
	}

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
	 * Show the library for the given query
	 *
	 * @param query
	 * 		The query to search in the library
	 */
	public void showLibrary(final String query) {
		this.query = query;

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				setLoading(true);

				// Reset tree table
				DefaultMutableTreeTableNode root = new DefaultMutableTreeTableNode(
						"Library");
				model.setRoot(root);
				tree.repaint();
				CustomTableHeader.customizeHeader(tree.getTableHeader());

				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)); // Load cursor

				// Show wait msg
				info.setText(MainWindow.bundle.getString("loadingLibrary"));
				showInfo();

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
							logger.log(Level.WARNING, e.getMessage());
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

				setCursor(Cursor.getDefaultCursor()); // Reset cursor
				hideInfo(); // Hide wait msg

				// Test empty library
				if (tree.getRowCount() == 0) {
					// Show info msg
					info.setText(MainWindow.bundle.getString("noLibrary"));
					showInfo();
				}

				CustomTableHeader.customizeHeader(tree.getTableHeader()); // Set header style again

				setLoading(false);
			}
		});

		t.start();
	}

	/**
	 * Add the selected nodes to the playlist
	 */
	public void addCurrentNodesToPlaylist() {
		for (int i : tree.getSelectedRows()) {
			if (tree.getPathForRow(i).getLastPathComponent() instanceof DefaultMutableTreeTableNode) {
				addNodeToPlaylist((DefaultMutableTreeTableNode) tree.getPathForRow(i).getLastPathComponent()); // Add to playlist
			}
		}

		Playlist.getPlaylist().sendNotification(); // Notify changes
		tree.repaint(); // Repaint
	}

	/**
	 * Add a node to the playlist
	 *
	 * @param node
	 * 		The node to add to the playlist
	 */
	private void addNodeToPlaylist(DefaultMutableTreeTableNode node) {
		Object o = node.getUserObject(); // Get node

		if (o instanceof Song) { // Add one song
			Playlist.getPlaylist().addSongWithoutNotifying((Song) o);
		} else { // Add a folder
			int childNumber = node.getChildCount();

			// Scan folder
			for (int i = 0; i < childNumber; i++) {
				addNodeToPlaylist((DefaultMutableTreeTableNode) node.getChildAt(i));
			}
		}
	}

	/**
	 * Add observer to the list
	 *
	 * @param o
	 * 		Observer to add
	 */
	public void addObserver(Observer o) {
		listeners.add(o);
	}

	/**
	 * Define loading state and notify Observer
	 *
	 * @param b
	 * 		New loading state
	 */
	private void setLoading(boolean b) {
		loading = b;

		// Notify
		for (Observer o : listeners)
			o.update(null, null);
	}

	/**
	 * Get the loading state
	 *
	 * @return True if loading, false otherwise
	 */
	public boolean isLoading() {
		return loading;
	}

	@Override
	public void update(Observable o, Object arg) {
		// Update library
		showLibrary(query);
	}
}
