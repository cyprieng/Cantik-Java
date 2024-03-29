package com.cantik.gui;

import com.cantik.core.Core;
import com.cantik.core.musiclibrary.ArtistInfo;
import com.cantik.core.musiclibrary.MusicLibrary;
import com.cantik.gui.centralarea.LocalFileView;
import com.cantik.gui.centralarea.playlistview.PlaylistView;
import com.cantik.gui.centralarea.trackartist.TrackArtist;
import com.cantik.gui.hotkeys.LinuxHotkeys;
import com.cantik.gui.hotkeys.WindowsHotkeys;
import com.cantik.gui.leftbar.LeftBar;
import com.cantik.gui.player.Player;
import com.cantik.gui.trayicon.TrayIconMenu;
import com.cantik.core.config.ConfigFileParser;
import com.cantik.core.musiclibrary.InvalidPathException;
import com.cantik.core.playlist.Playlist;
import com.cantik.gui.centralarea.ParametersView;
import com.cantik.gui.centralarea.TrackLyrics;
import com.cantik.gui.centralarea.musiclibrary.MusicLibraryView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Main window
 *
 * @author cyprien
 */
public class MainWindow {
	/**
	 * Logger for the class
	 */
	private static Logger logger = Logger.getLogger(MainWindow.class.getName());

	/**
	 * Store the unique JFrame
	 */
	private static final JFrame window;

	/**
	 * Store the central area panel
	 */
	private static final JPanel centralArea;

	/**
	 * Store the left bar
	 */
	private static final LeftBar leftBar;

	/**
	 * Store the current and the previous card shown
	 */
	private static String cardShown = "Library", previousCardShown;

	/**
	 * Store the resource bundle
	 */
	public static ResourceBundle bundle;

	/**
	 * Init window
	 */
	static {
		// Load log config file
		try {
			LogManager.getLogManager().readConfiguration(new FileInputStream("logging.properties"));
		} catch (Exception e) {
			try {
				LogManager.getLogManager().readConfiguration(MainWindow.class.getClassLoader().getResourceAsStream("logging.properties"));
			} catch (IOException e1) {
			}
		}

		// Get the resource bundle
		bundle = ResourceBundle.getBundle("Text", Locale.getDefault());

		UIManager.getLookAndFeelDefaults().put("defaultFont", GUIParameters.getFont());

		// Set name and icon
		window = new JFrame("Cantik");
		try {
			window.setIconImage(ImageIO.read(new File("assets/img/icon.png")));
		} catch (IOException e) {
			logger.log(Level.WARNING, e.getMessage());
		}

		window.setLayout(new BorderLayout());
		Container container = window.getContentPane();

		// Central area
		centralArea = new JPanel(new CardLayout());
		centralArea.add(MusicLibraryView.getMusiLibraryView(), "Library");
		centralArea.add(new LocalFileView(), "LocalFile");
		centralArea.add(new PlaylistView(), "Playlist");
		centralArea.add(new TrackLyrics(), "Lyrics");
		centralArea.add(new TrackArtist(), "ArtistInfo");
		centralArea.add(new ParametersView(), "Settings");

		// Add elements
		leftBar = new LeftBar();
		container.add(leftBar, BorderLayout.WEST);
		container.add(new Player(), BorderLayout.SOUTH);
		container.add(centralArea, BorderLayout.CENTER);

		// Size and maximized
		window.setPreferredSize(new Dimension(1024, 768));
		window.setExtendedState(window.getExtendedState() | JFrame.MAXIMIZED_BOTH);

		// Save cover when closing
		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				ArtistInfo.save();
			}
		});

		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();

		String OS = Core.getOS();

		// Load hotkeys
		if ("windows".equals(OS))
			new WindowsHotkeys();
		else if ("linux".equals(OS))
			new LinuxHotkeys();

		// Add keyboard shortcut: SPACE => Play / Pause
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_SPACE
						&& !leftBar.getSearch().isFocusOwner())
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

	/**
	 * Get the MainWindow
	 *
	 * @return Return the main window and create it if necessary
	 */
	public static JFrame getMainWindow() {
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
		window.setVisible(true);
		window.requestFocus();
	}
}
