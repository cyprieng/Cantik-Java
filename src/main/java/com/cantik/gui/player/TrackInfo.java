package com.cantik.gui.player;

import com.cantik.gui.leftbar.LeftbarJLabel;
import com.cantik.core.playlist.Playlist;
import com.cantik.core.song.Song;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Custom JPanel showing track info
 *
 * @author cyprien
 */
public class TrackInfo extends JPanel implements Observer {
	private static final long serialVersionUID = -8229966239396964776L;

	/**
	 * JLabel showing info
	 */
	LeftbarJLabel title, album, artist;

	/*
	 * Init JLabel
	 */
	public TrackInfo() {
		Playlist.getPlaylist().addObserver(this);

		title = new LeftbarJLabel("");
		album = new LeftbarJLabel("");
		artist = new LeftbarJLabel("");

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
