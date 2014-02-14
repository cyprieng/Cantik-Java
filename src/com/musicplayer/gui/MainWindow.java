package com.musicplayer.gui;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;

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
	 * Init window
	 */
	private MainWindow() {
		window = new JFrame("MusicPlayer");
		window.setLayout(new BorderLayout());
		Container container = window.getContentPane();

		// Add elements
		container.add(new LeftBar(), BorderLayout.WEST);
		container.add(new Player(), BorderLayout.SOUTH);

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

	public static void main(String[] args) {
		getMainWindow();
	}

}
