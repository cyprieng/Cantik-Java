package com.cantik.core.config;

import com.cantik.core.Core;

import java.io.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Parse config file. It uses singleton design pattern
 *
 * @author cyprien
 */
public class ConfigFileParser {
	/**
	 * Logger for the class
	 */
	private static Logger logger = Logger.getLogger(ConfigFileParser.class.getName());

	/**
	 * Store the config
	 */
	private static ConfigFileParser config;

	/**
	 * Store all the params and value of the config
	 */
	private HashMap<String, String> params;

	/**
	 * Constructor: parse the file
	 */
	private ConfigFileParser() {
		params = new HashMap<>();

		// Get File
		File configFile = new File(Core.getUserPath() + "cantik.config");

		try {
			this.loadConfigFile(configFile);
		} catch (IOException e) {
			logger.log(Level.WARNING, e.getMessage());
		}
	}

	/**
	 * Constructor: parse the given file
	 *
	 * @param configFile
	 * 		File to parse
	 */
	private ConfigFileParser(File configFile) {
		params = new HashMap<>();

		try {
			this.loadConfigFile(configFile);
		} catch (IOException e) {
			logger.log(Level.WARNING, e.getMessage());
		}
	}

	/**
	 * Load the given config file in the parameters.
	 *
	 * @param configFile
	 * 		Config file to parse.
	 * @throws IOException
	 * 		In case an error happen while reading the file.
	 */
	public void loadConfigFile(File configFile) throws IOException {
		// Read config file
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(configFile)));

		String str;
		while ((str = br.readLine()) != null) {
			String temp[] = str.split("=", 2); // Get right and left part of the line

			if (temp.length == 2) { // Add param
				this.params.put(temp[0].trim(), temp[1].trim());
			}
		}
		br.close();
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
	 * Retrieve the selected parameter
	 *
	 * @param param
	 * 		The parameter to retrieve
	 * @return The value of the parameter
	 */
	public String getParams(String param) {
		return params.get(param);
	}

	/**
	 * Update a value of the config
	 *
	 * @param param
	 * 		The parameter to change
	 * @param value
	 * 		The value to set
	 */
	public void setParam(String param, String value) {
		params.put(param, value);
	}

	/**
	 * Get the hashmap
	 *
	 * @return The hashmap
	 */
	public HashMap<String, String> getHashMap() {
		return params;
	}
}
