package com.musicplayer.gui.centralarea.TrackArtist;

import com.musicplayer.core.musiclibrary.ArtistInfo;
import com.musicplayer.core.musiclibrary.MusicLibrary;
import com.musicplayer.core.playlist.Playlist;
import com.musicplayer.core.song.Song;
import com.musicplayer.gui.GUIParameters;
import com.musicplayer.gui.MainWindow;
import com.musicplayer.gui.centralarea.CentralArea;
import de.umass.lastfm.Track;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

/**
 * JPanel showing artist info
 *
 * @author cyprien
 */
public class TrackArtist extends CentralArea implements Observer {
	private static final long serialVersionUID = 2431135003545294862L;

	/**
	 * Label showing biography
	 */
	private JLabel bio;

	/**
	 * Top tracks table
	 */
	private JTable topTracks;

	/**
	 * Model for {@link #topTracks}
	 */
	private TopTracksModel tableModel;

	/**
	 * Panel for the above elements
	 */
	private JPanel panel;

	/**
	 * hread updating artist info
	 */
	private Thread t;

	/**
	 * Store last artist shown
	 */
	private String lastArtist;

	/**
	 * Init panel
	 */
	public TrackArtist() {
		super();
		Playlist.getPlaylist().addObserver(this);

		// Create panel
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(new EmptyBorder(0, 10, 0, 10));
		add(panel, true, false);

		// Init jlabel
		bio = new JLabel("");
		bio.setForeground(Color.BLACK);
		bio.setFont(bio.getFont().deriveFont(20.0f));
		bio.setMaximumSize(new Dimension(600, 5000));
		panel.add(bio);

		// Create table
		String[] columnNames = {"", "", ""};
		tableModel = new TopTracksModel(columnNames, 0);
		topTracks = new JTable(tableModel) {
			@Override
			public Component prepareRenderer(TableCellRenderer renderer,
											 int rowIndex, int vColIndex) {
				JComponent c = (JComponent) super.prepareRenderer(renderer, rowIndex,
						vColIndex);

				if (rowIndex % 2 == 0) // Even line
					c.setBackground(Color.WHITE);
				else
					c.setBackground(GUIParameters.TABLE_EVEN_ROW);

				return c;
			}
		};
		topTracks.setAlignmentX(Component.LEFT_ALIGNMENT);
		topTracks.setGridColor(GUIParameters.TABLE_EVEN_ROW);
		panel.add(topTracks);

		// Add action to play song in table
		topTracks.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) { // Double click
					Object o = tableModel.getRawValueAt(topTracks.getSelectedRow(), 2);

					if (o instanceof Song) { // Playable
						int indexOf = Playlist.getPlaylist().getSongList().indexOf(o);
						if (indexOf >= 0) { // Play song
							Playlist.getPlaylist().play(indexOf);
						} else { // Add song and play it
							Playlist.getPlaylist().addSong((Song) o);
							Playlist.getPlaylist().play(Playlist.getPlaylist().getSongList().size() - 1);
						}
					}
				}
			}
		});

		// Set column width
		int[] columnsWidth = {
				300, 250, 50
		};
		int i = 0;
		for (int width : columnsWidth) {
			TableColumn column = topTracks.getColumnModel().getColumn(i++);
			column.setMinWidth(width);
			column.setMaxWidth(width);
			column.setPreferredWidth(width);
		}

		// Show info msg
		info.setText(MainWindow.bundle.getString("noSong"));
		showInfo();
	}

	/**
	 * Update artist info
	 */
	public void updateInfo() {
		Song s = Playlist.getPlaylist().getCurrentSong();
		if (s != null) { //Song exist
			if (s.getArtist().equals(lastArtist))
				return; // Same artist => do not need to reload

			// Store artist
			lastArtist = s.getArtist();

			// Reset table
			tableModel.setRowCount(0);

			final String artist = s.getArtist();
			final String bioTxt = ArtistInfo.getBio(artist);
			if (!bioTxt.isEmpty()) { // Lyric exist
				final Font f = GUIParameters.getFont();

				bio.setText("<html><style type='text/css'>html {width:500px; font-family: '"
						+ f.getFamily()
						+ "'; font-size:20px;}h1{margin-bottom:0;} </style><h1>" + artist + "</h1>"
						+ "<h3>" + NumberFormat.getInstance().format(ArtistInfo.getNbAuditors(artist)) + " Auditeurs</h6>"
						+ "<p><img src = \"" + ArtistInfo.getArtistImageURL(artist) + "\" />"
						+ bioTxt.replaceAll("(?m)^User-contributed.*\\s+$", "")
						.replaceAll("\n", "<br>").replaceAll("<a.*</a>\\.?", "") + "</p>"
						+ "<html>");
				hideInfo();

				int i = 0;
				Iterator it = ArtistInfo.getTopTracks(artist).iterator();
				while (it.hasNext() && i < 5) { // Get top tracks
					Track t = (Track) it.next();

					// Find top track in library
					Set<Song> songs = MusicLibrary.getMusicLibrary().getSongs(artist);
					Iterator it2 = songs.iterator();
					boolean find = false;
					Song s2 = null;
					while (it2.hasNext() && !find) {
						s2 = (Song) it2.next();
						if (s2.getTitle().toLowerCase().equals(t.getName().toLowerCase())) {
							find = true;
						}
					}

					// If found => add song to cell
					if (find)
						tableModel.addRow(new Object[]{t.getName(), NumberFormat.getInstance().format(t.getListeners()), s});
					else
						tableModel.addRow(new Object[]{t.getName(), NumberFormat.getInstance().format(t.getListeners()), ""});
					i++;
				}
				tableModel.setRowCount(5);
				topTracks.repaint();


			} else { //No bio
				bio.setText("");

				// Show info msg
				info.setText(MainWindow.bundle.getString("noBio"));
				showInfo();
			}
		} else { // No song
			bio.setText("");

			// Show info msg
			info.setText(MainWindow.bundle.getString("noSong"));
			showInfo();
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		t = new Thread(new Runnable() {
			public void run() {
				updateInfo();
			}
		});
		t.start();
	}
}
