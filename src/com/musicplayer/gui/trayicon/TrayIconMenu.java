package com.musicplayer.gui.trayicon;

import com.musicplayer.core.Log;
import com.musicplayer.core.playlist.Playlist;
import com.musicplayer.core.song.Song;
import com.musicplayer.gui.GUIParameters;
import com.musicplayer.gui.MainWindow;
import org.jdesktop.swinghelper.tray.JXTrayIcon;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

/**
 * Show the tray icon in the systray
 *
 * @author cyprien
 */
public class TrayIconMenu implements Observer {
	/**
	 * Item showing current track info
	 */
	private JMenuItem artist, album, title;

	/**
	 * Tray icon
	 */
	private JXTrayIcon trayIcon;

	/**
	 * Last message shown by the tray icon
	 */
	private String trayIconLastMsg = "";

	/**
	 * Create the tray icon
	 */
	public TrayIconMenu() {
		// Check the SystemTray is supported
		if (!SystemTray.isSupported()) {
			return;
		}

		try {
			// Create tray
			final JPopupMenu popup = new JPopupMenu();
			trayIcon = new JXTrayIcon(ImageIO.read(new File("assets/img/icon.png")));
			final SystemTray tray = SystemTray.getSystemTray();

			// Set style of popup menu
			UIManager.put("MenuItem.selectionBackground", GUIParameters.LEFTBAR_ACTIVE);
			UIManager.put("Menu.selectionBackground", GUIParameters.LEFTBAR_ACTIVE);
			UIManager.put("MenuBar.selectionBackground", GUIParameters.LEFTBAR_ACTIVE);
			UIManager.put("MenuItem.selectionForeground", Color.WHITE);
			UIManager.put("Menu.selectionForeground", Color.WHITE);
			UIManager.put("MenuBar.selectionForeground", Color.WHITE);

			popup.setBackground(GUIParameters.LEFTBAR_BACKGROUND);
			popup.setForeground(Color.WHITE);
			popup.setBorder(BorderFactory.createEmptyBorder());

			// Create a pop-up menu components
			Playlist.getPlaylist().addObserver(this);
			artist = new CustomJMenuItem("No song");
			album = new CustomJMenuItem("");
			title = new CustomJMenuItem("");

			JMenuItem play = new CustomJMenuItem(MainWindow.bundle.getString("playPause"), ImageIO.read(new File("assets/img/play.png")));
			play.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Playlist.getPlaylist().playPause();
				}
			});

			JMenuItem back = new CustomJMenuItem(MainWindow.bundle.getString("back"), ImageIO.read(new File("assets/img/previous.png")));
			back.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Playlist.getPlaylist().back();
				}
			});

			JMenuItem next = new CustomJMenuItem(MainWindow.bundle.getString("next"), ImageIO.read(new File("assets/img/next.png")));
			next.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Playlist.getPlaylist().next();
				}
			});

			// Add components to pop-up menu
			popup.add(artist);
			popup.add(album);
			popup.add(title);
			popup.addSeparator();
			popup.add(play);
			popup.add(back);
			popup.add(next);

			// Finalize
			trayIcon.setJPopupMenu(popup);
			trayIcon.setImageAutoSize(true);
			tray.add(trayIcon);
		} catch (Exception e) {
			Log.addEntry(e);
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		// Get song and info
		Song s = Playlist.getPlaylist().getCurrentSong();

		if (s != null) {
			// Update track info
			title.setText(s.getTitle());
			album.setText(s.getAlbum());
			artist.setText(s.getArtist());

			// Notify
			if (!trayIconLastMsg.equals(s.getTitle() + s.getAlbum() + s.getArtist())) { // Check if track changed
				trayIconLastMsg = s.getTitle() + s.getAlbum() + s.getArtist();
				trayIcon.displayMessage(s.getTitle(), MainWindow.bundle.getString("album") + ": " + s.getAlbum() +
						"\n" + MainWindow.bundle.getString("artist") + ": " + s.getArtist(), TrayIcon.MessageType.INFO);
			}
		} else {
			trayIconLastMsg = "";

			title.setText("");
			album.setText("");
			artist.setText(MainWindow.bundle.getString("noSong"));
		}
	}
}
