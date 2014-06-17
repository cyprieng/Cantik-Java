package com.musicplayer.gui;

import com.musicplayer.core.config.ConfigFileParser;
import com.musicplayer.core.musiclibrary.InvalidPathException;
import com.musicplayer.core.musiclibrary.MusicLibrary;
import com.musicplayer.core.playlist.Playlist;
import com.musicplayer.gui.centralarea.LocalFileView;
import com.musicplayer.gui.centralarea.ParametersView;
import com.musicplayer.gui.centralarea.TrackInfo;
import com.musicplayer.gui.centralarea.musiclibrary.MusicLibraryView;
import com.musicplayer.gui.centralarea.playlistview.PlaylistView;
import com.musicplayer.gui.hotkeys.LinuxHotkeys;
import com.musicplayer.gui.hotkeys.WindowsHotkeys;
import com.musicplayer.gui.leftbar.LeftBar;
import com.musicplayer.gui.player.Player;
import com.musicplayer.gui.trayicon.TrayIconMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Main window
 *
 * @author cyprien
 */
public class MainWindow {
	/**
	 * Store the unique JFrame
	 */
	private static JFrame window;

	/**
	 * Store the central area panel
	 */
	private static JPanel centralArea;

	/**
	 * Store the current and the previous card shown
	 */
	private static String cardShown = "Library", previousCardShown;

	/**
	 * Init window
	 */
	private MainWindow() {
		window = new JFrame("MusicPlayer");
		window.setLayout(new BorderLayout());
		Container container = window.getContentPane();

		// Central area
		centralArea = new JPanel(new CardLayout());
		centralArea.add(MusicLibraryView.getMusiLibraryView(), "Library");
		centralArea.add(new LocalFileView(), "Local File");
		centralArea.add(new PlaylistView(), "Playlist");
		centralArea.add(new TrackInfo(), "Info");
		centralArea.add(new ParametersView(), "Settings");

		// Add elements
		container.add(new LeftBar(), BorderLayout.WEST);
		container.add(new Player(), BorderLayout.SOUTH);
		container.add(centralArea, BorderLayout.CENTER);

		// Size and maximized
		window.setPreferredSize(new Dimension(1024, 768));
		window.setExtendedState(window.getExtendedState() | JFrame.MAXIMIZED_BOTH);

		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();
		window.setVisible(true);
	}

	/**
	 * Get the MainWindow
	 *
	 * @return Return the main window and create it if necessary
	 */
	public static JFrame getMainWindow() {
		if (window == null) {
			new MainWindow();
		}

		return window;
	}

	/**
	 * Change the central area
	 *
	 * @param toShow
	 * 		The name of the panel to show
	 */
	public static void setCentralArea(String toShow) {
		// Update card historic
		previousCardShown = cardShown;
		cardShown = toShow;

		// Show card
		CardLayout cl = (CardLayout) (centralArea.getLayout());
		cl.show(centralArea, toShow);
	}

	/**
	 * Get the previous card shown
	 *
	 * @return Name of the previous card
	 */
	public static String getPreviousCardShown() {
		return previousCardShown;
	}

	public static void main(String[] args) {
		UIManager.getLookAndFeelDefaults().put("defaultFont", GUIParameters.getFont());
		getMainWindow();

		String OS = System.getProperty("os.name").toLowerCase();

		// Load hotkeys
		if (OS.contains("win"))
			new WindowsHotkeys();
		else if (OS.contains("nix") || OS.contains("nux") || OS.contains("aix"))
			new LinuxHotkeys();

		// Add keyboard shortcut: SPACE => Play / Pause
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_SPACE)
					Playlist.getPlaylist().playPause();

				return false;
			}
		});

		// Add systray
		new TrayIconMenu();

		try {
			// Load library
			MusicLibrary.getMusicLibrary().loadLibraryFolder(
					ConfigFileParser.getConfigFileParser().getParams("library"));
		} catch (InvalidPathException e) {
			// No library => settings
			setCentralArea("Settings");
		}
	}
}
