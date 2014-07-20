package com.musicplayer.gui.centralarea;

import com.musicplayer.core.InvalidFileException;
import com.musicplayer.core.Log;
import com.musicplayer.core.playlist.Playlist;
import com.musicplayer.core.song.Song;
import com.musicplayer.core.song.SongExtension;
import com.musicplayer.gui.MainWindow;
import org.jaudiotagger.audio.generic.Utils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;

/**
 * Class showing a File Chooser for adding song in the playlist
 *
 * @author cyprien
 */
public class LocalFileView extends CentralArea {
	private static final long serialVersionUID = -4136316670805228071L;

	/**
	 * Create the JFileChooser
	 */
	public LocalFileView() {
		// Use the system look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			Log.addEntry(e);
		}

		final JFileChooser fc = new JFileChooser();

		// Reset default look and feel
		try {
			UIManager.setLookAndFeel(UIManager
					.getCrossPlatformLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			Log.addEntry(e);
		}

		// Set selection options
		fc.setMultiSelectionEnabled(true);
		fc.setAcceptAllFileFilterUsed(false);
		fc.addChoosableFileFilter(new FileFilter() {
			@Override
			public String getDescription() {
				// Name of the filter
				StringBuilder str = new StringBuilder("Music files (");

				// Add all possible extensions
				String prefix = "";
				for (SongExtension ext : SongExtension.values()) {
					str.append(prefix);
					str.append(ext.name().toLowerCase());
					prefix = ", ";
				}
				str.append(")");

				return str.toString();
			}

			@Override
			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				}

				// Check if extension is in the allowed ones
				String extension = Utils.getExtension(f);
				if (extension != null) {
					for (SongExtension ext : SongExtension.values()) {
						if (ext.name().toLowerCase().equals(extension)) {
							return true;
						}
					}
				}

				return false;
			}
		});

		// Show FileChooser when this CentralArea component is shown
		addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				MainWindow.setCentralArea("Playlist"); // Show playlist

				// Show file chooser and get the resule
				int returnVal = fc.showOpenDialog(MainWindow.getMainWindow());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					// Add files to the playlist
					File[] files = fc.getSelectedFiles();
					for (File f : files) {
						try {
							Playlist.getPlaylist().addSong(
									new Song(f.getAbsolutePath()));
						} catch (InvalidFileException e2) {
							Log.addEntry(e2);
						}
					}
				}
			}

			public void componentHidden(ComponentEvent e) {
			}
		});
	}
}