package com.musicplayer.core;

import java.io.File;

/**
 * Class where are the core methods
 * 
 * @author cyprien
 * 
 */
public class Core {
	/**
	 * Get the path of the user's home directory
	 * 
	 * @return The user's home directory
	 */
	public static String getUserPath() {
		// Test if "user.home" works
		File home = new File(System.getProperty("user.home"));
		if (home.exists() && home.isDirectory()) {
			return home.getAbsolutePath() + File.separator;
		}

		// Else use the project folder
		return "." + File.separator;
	}
}
