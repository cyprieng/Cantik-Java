package com.musicplayer.gui.centralarea;

import com.musicplayer.core.playlist.Playlist;
import com.musicplayer.gui.GUIParameters;
import com.musicplayer.gui.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * JPanel showing info on the current song
 *
 * @author cyprien
 */
public class TrackLyrics extends CentralArea implements Observer {
	private static final long serialVersionUID = 2431135003545294862L;

	/**
	 * Label showing lyric
	 */
	private JLabel lyric;

	/**
	 * Init panel
	 */
	public TrackLyrics() {
		super();
		Playlist.getPlaylist().addObserver(this);

		// Init jlabel
		lyric = new JLabel("");
		lyric.setForeground(Color.BLACK);
		lyric.setFont(lyric.getFont().deriveFont(20.0f));
		add(lyric, true, false);

		// Show info msg
		info.setText(MainWindow.bundle.getString("noSong"));
		showInfo();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (Playlist.getPlaylist().getCurrentSong() != null) { //Song exist
			if (!Playlist.getPlaylist().getCurrentSong().getLyric().isEmpty()) { // Lyric exist
				Font f = GUIParameters.getFont();

				hideInfo();
				lyric.setText("<html><style type='text/css'>html { font-family: '"
						+ f.getFamily()
						+ "'; font-size:20px;} </style><h1>" + MainWindow.bundle.getString("lyric") + "</h1>"
						+ Playlist.getPlaylist().getCurrentSong().getLyric()
						.replaceAll("\n", "<br>") + "<html>");
			} else { //No lyric
				lyric.setText("");

				// Show info msg
				info.setText(MainWindow.bundle.getString("noLyric"));
				showInfo();
			}
		} else { // No song
			lyric.setText("");

			// Show info msg
			info.setText(MainWindow.bundle.getString("noSong"));
			showInfo();
		}
	}
}
