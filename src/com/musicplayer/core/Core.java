package com.musicplayer.core;

import java.io.File;

/**
 * Class where are the core methods
 *
 * @author cyprien
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

	/**
	 * Return a string representing the duration in a human-friendly format
	 *
	 * @param duration
	 * 		The number of second
	 * @return the duration in a human-friendly format
	 */
	public static String stringifyDuration(int duration) {
		// Calc duration
		int min = duration / 60;
		int second = duration - min * 60;

		// Add lead 0 if necessary
		if (second < 10)
			return min + ":0" + second;
		else
			return min + ":" + second;
	}
}
