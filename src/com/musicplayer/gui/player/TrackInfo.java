package com.musicplayer.gui.player;

import java.awt.Color;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.musicplayer.core.playlist.Playlist;
import com.musicplayer.core.song.Song;
import com.musicplayer.gui.CustomJLabel;

/**
 * Custom JPanel showing track info
 * 
 * @author cyprien
 * 
 */
public class TrackInfo extends JPanel implements Observer {
	private static final long serialVersionUID = -8229966239396964776L;

	/**
	 * JLabel showing info
	 */
	CustomJLabel title, album, artist;

	/*
	 * Init JLabel
	 */
	public TrackInfo() {
		Playlist.getPlaylist().addObserver(this);

		title = new CustomJLabel("");
		album = new CustomJLabel("");
		artist = new CustomJLabel("");

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(Color.BLACK);

		add(title);
		add(album);
		add(artist);
	}

	@Override
	public void update(Observable playist, Object arg) {
		// Get song and info
		Song s = Playlist.getPlaylist().getCurrentSong();

		if (s != null) {
			title.setText(s.getTitle());
			album.setText(s.getAlbum());
			artist.setText(s.getArtist());
		} else {
			title.setText("");
			album.setText("");
			artist.setText("");
		}
	}
}
