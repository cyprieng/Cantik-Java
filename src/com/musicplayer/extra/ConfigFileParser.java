package com.musicplayer.extra;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Parse config file. It uses singleton design pattern
 * 
 * @author cyprien
 * 
 */
public class ConfigFileParser {
	/**
	 * Store the config
	 */
	private static ConfigFileParser config;

	/*
	 * Define if we need to use lastfm or not
	 */
	private boolean lastfmStatus;

	/**
	 * Lastfm IDs
	 */
	private String lastfmUsername, lastfmPassword;

	/**
	 * Constructor: parse the file
	 */
	private ConfigFileParser() {
		// Default value
		this.lastfmStatus = false;
		this.lastfmUsername = "";
		this.lastfmPassword = "";

		// Test file
		File configFile = new File("config");
		if (configFile.exists() && configFile.isFile()) {
			try {
				// Read config file
				BufferedReader br = new BufferedReader(new InputStreamReader(
						new FileInputStream("config")));

				String str;
				while ((str = br.readLine()) != null) {
					String temp[] = str.split("="); // Get right and left part
													// of the line

					// Enable info
					if (temp[0].startsWith("enable")) {
						if (temp[1] != null) {
							this.lastfmStatus = (temp[1].replaceAll("\\s+", "")
									.startsWith("true"));
						}
					}

					// Username info
					else if (temp[0].startsWith("username")) {
						if (temp[1] != null) {
							this.lastfmUsername = temp[1]
									.replaceAll("\\s+", "");
						}
					}

					// Password info
					else if (temp[0].startsWith("password")) {
						if (temp[1] != null) {
							this.lastfmPassword = temp[1]
									.replaceAll("\\s+", "");
						}
					}
				}
				br.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Get the unique ConfigFileParser instance
	 * 
	 * @return the unique ConfigFileParser instance
	 */
	public static ConfigFileParser getConfigFileParser() {
		if (ConfigFileParser.config == null) {
			ConfigFileParser.config = new ConfigFileParser();
		}

		return ConfigFileParser.config;
	}

	/**
	 * Get the lastfm status
	 * 
	 * @return True if enabled
	 */
	public boolean isLastfmStatus() {
		return lastfmStatus;
	}

	/**
	 * Get lastfm username
	 * 
	 * @return Lastfm username
	 */
	public String getLastfmUsername() {
		return lastfmUsername;
	}

	/**
	 * Get lastfm password
	 * 
	 * @return Lastfm password
	 */
	public String getLastfmPassword() {
		return lastfmPassword;
	}

	@Override
	public String toString() {
		return "ConfigFileParser [lastfmStatus=" + lastfmStatus
				+ ", lastfmUsername=" + lastfmUsername + ", lastfmPassword="
				+ lastfmPassword + "]";
	}
}
