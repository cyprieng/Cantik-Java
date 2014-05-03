package com.musicplayer.gui.player;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.musicplayer.core.Log;
import com.musicplayer.core.playlist.Playlist;

/**
 * Custom JLabel showing the cover of the current song
 * 
 * @author cyprien
 * 
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
		if (Playlist.getPlaylist().getCurrentSong() == null
				|| Playlist.getPlaylist().getCurrentSong().getCover() == null)
			// Default cover
			try {
				this.setIcon(new ImageIcon(ImageIO.read(new File(
						"assets/img/cover.png"))));
			} catch (IOException e) {
				Log.addEntry(e);
			}
		else
			// Song cover
			this.setIcon(new ImageIcon(Playlist.getPlaylist().getCurrentSong()
					.getCover().getScaledInstance(86, 86, Image.SCALE_SMOOTH)));
	}

}
