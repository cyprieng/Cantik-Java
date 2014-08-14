package com.musicplayer.gui.player;

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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Custom JLabel showing the cover of the current song
 *
 * @author cyprien
 */
public class Cover extends JLabel implements Observer {
	private static final long serialVersionUID = -4531448604982404154L;

	/**
	 * Logger for the class
	 */
	private static Logger logger = Logger.getLogger(Cover.class.getName());

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
			logger.log(Level.WARNING, e.getMessage());
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		// Default icon
		ImageIcon ii = ArtistInfo.getDefaultAlbumImage();

		if (Playlist.getPlaylist().getCurrentSong() != null) { // There is a song playing
			Song s = Playlist.getPlaylist().getCurrentSong();

			if (ArtistInfo.getAlbumImage(s.getArtist(), s.getAlbum()) != null)
				ii = ArtistInfo.getAlbumImage(s.getArtist(), s.getAlbum(), new Dimension(86, 86));
		}

		// Set icon
		this.setIcon(ii);
	}
}
