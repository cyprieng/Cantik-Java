package com.cantik.core;

import java.io.File;

/**
 * Class where are the core methods
 *
 * @author cyprien
 */
public class Core {
	/**
	 * Store the path of the config
	 */
	private static String configPath;

	/**
	 * Get the path of the user's home directory
	 *
	 * @return The user's home directory
	 */
	public static String getUserPath() {
		// Already defined
		if (configPath != null)
			return configPath;

		// Windows
		if (Core.getOS().equals("windows")) {
			String path = System.getenv("APPDATA") + File.separator + "Cantik";
			File test = new File(path);

			if (!test.exists()) { // Folder does not exist yet
				File appData = new File(System.getenv("APPDATA"));

				// Create it
				if (appData.exists() && appData.isDirectory()) {
					if (new File(path).mkdirs()) {
						configPath = path + File.separator;
						return configPath;
					}
				}
			} else {
				configPath = path + File.separator;
				return configPath;
			}
		}

		// Default folder
		String path = System.getProperty("user.home") + File.separator + ".cantik";
		File test = new File(path);

		if (!test.exists()) { // Folder does not exist yet
			File home = new File(System.getProperty("user.home"));

			// Create it
			if (home.exists() && home.isDirectory()) {
				if (new File(path).mkdirs()) {
					configPath = path + File.separator;
					return configPath;
				}
			}
		} else {
			configPath = path + File.separator;
			return configPath;
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

	/**
	 * Get the current OS
	 *
	 * @return "windows", "linux" or "unknown"
	 */
	public static String getOS() {
		// Get os name
		String OS = System.getProperty("os.name").toLowerCase();

		// Return OS cat
		if (OS.contains("win"))
			return "windows";
		else if (OS.contains("nix") || OS.contains("nux") || OS.contains("aix"))
			return "linux";
		else
			return "unknown";
	}
}
