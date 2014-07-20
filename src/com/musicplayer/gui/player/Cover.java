package com.musicplayer.gui.player;

import com.musicplayer.core.Log;
import com.musicplayer.core.musiclibrary.ArtistInfo;
import com.musicplayer.core.playlist.Playlist;
import com.musicplayer.core.song.Song;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

/**
 * Custom JLabel showing the cover of the current song
 *
 * @author cyprien
 */
public class Cover extends JLabel implements Observer {
	private static final long serialVersionUID = -4531448604982404154L;

	/**
	 * Constructor: init with default cover
	 */
	public Cover() {
		super();
		Playlist.getPlaylist().addObserver(this);

		try {
			BufferedImage img = ImageIO.read(new File("assets/img/cover.png"));
			this.setIcon(new ImageIcon(img));
		} catch (IOException e) {
			Log.addEntry(e);
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		// Default icon
		ImageIcon ii = new ImageIcon();
		try {
			ii = new ImageIcon(ImageIO.read(new File(
					"assets/img/cover.png")));
		} catch (IOException e) {
			Log.addEntry(e);
		}

		if (Playlist.getPlaylist().getCurrentSong() != null) { // There is a song playing
			if (Playlist.getPlaylist().getCurrentSong().getCover() == null) {
				// Get from web
				Song s = Playlist.getPlaylist().getCurrentSong();

				if (ArtistInfo.getAlbumImage(s.getArtist(), s.getAlbum()) != null)
					ii = new ImageIcon(ArtistInfo.getAlbumImage(s.getArtist(), s.getAlbum())
							.getScaledInstance(86, 86, Image.SCALE_SMOOTH));
			} else {
				// Song cover
				ii = new ImageIcon(Playlist.getPlaylist().getCurrentSong()
						.getCover().getScaledInstance(86, 86, Image.SCALE_SMOOTH));
			}
		}

		// Set icon
		this.setIcon(ii);
	}
}