package com.musicplayer.gui.centralarea;

import java.awt.Color;
import java.awt.Font;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;

import com.musicplayer.core.playlist.Playlist;
import com.musicplayer.gui.GUIParameters;

/**
 * JPanel showing info on the current song
 * 
 * @author cyprien
 * 
 */
public class TrackInfo extends CentralArea implements Observer {
	private static final long serialVersionUID = 2431135003545294862L;

	/**
	 * Label showing lyric
	 */
	private JLabel lyric;

	/**
	 * Init panel
	 */
	public TrackInfo() {
		super();
		Playlist.getPlaylist().addObserver(this);

		// Init jlabel
		lyric = new JLabel("");
		lyric.setForeground(Color.BLACK);
		lyric.setFont(GUIParameters.getCentralFont());
		content.add(lyric);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (Playlist.getPlaylist().getCurrentSong() != null) {
			Font f = GUIParameters.getCentralFont();

			lyric.setText("<html><style type='text/css'>html { font-family: '"
					+ f.getFamily()
					+ "'; font-size:20px;} </style><h1>Lyric</h1>"
					+ Playlist.getPlaylist().getCurrentSong().getLyric()
							.replaceAll("\n", "<br>") + "<html>");
		}
	}
}
