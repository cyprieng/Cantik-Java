package com.musicplayer.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.musicplayer.core.config.ConfigFileParser;
import com.musicplayer.core.musiclibrary.MusicLibrary;
import com.musicplayer.gui.centralarea.TrackInfo;
import com.musicplayer.gui.centralarea.musiclibrary.MusicLibraryView;
import com.musicplayer.gui.centralarea.playlistview.PlaylistView;
import com.musicplayer.gui.leftbar.LeftBar;
import com.musicplayer.gui.player.Player;

/**
 * Main window
 * 
 * @author cyprien
 * 
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
	 * Init window
	 */
	private MainWindow() {
		window = new JFrame("MusicPlayer");
		window.setLayout(new BorderLayout());
		Container container = window.getContentPane();

		// Central area
		centralArea = new JPanel(new CardLayout());
		centralArea.add(MusicLibraryView.getMusiLibraryView(), "Library");
		centralArea.add(new PlaylistView(), "Playlist");
		centralArea.add(new TrackInfo(), "Info");

		// Add elements
		container.add(new LeftBar(), BorderLayout.WEST);
		container.add(new Player(), BorderLayout.SOUTH);
		container.add(centralArea, BorderLayout.CENTER);

		window.setPreferredSize(new Dimension(1024, 768));

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
	 *            The name of the panel to show
	 */
	public static void setCentralArea(String toShow) {
		CardLayout cl = (CardLayout) (centralArea.getLayout());
		cl.show(centralArea, toShow);
	}

	public static void main(String[] args) {
		MusicLibrary.getMusicLibrary().loadLibraryFolder(
				ConfigFileParser.getConfigFileParser().getParams("library"));
		getMainWindow();
	}

}
