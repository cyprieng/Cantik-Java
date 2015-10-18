package com.cantik.core.config;

import com.cantik.core.Core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
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
		params = new HashMap<String, String>();

		// Test file
		File configFile = new File(Core.getUserPath() + "cantik.config");
		if (configFile.exists() && configFile.isFile()) {
			try {
				// Read config file
				BufferedReader br = new BufferedReader(new InputStreamReader(
						new FileInputStream(Core.getUserPath() + "cantik.config")));

				String str;
				while ((str = br.readLine()) != null) {
					String temp[] = str.split("=", 2); // Get right and left
					// part
					// of the line

					if (temp.length == 2) { // Add param
						params.put(temp[0].trim(), temp[1].trim());
					}
				}
				br.close();
			} catch (Exception e) {
				logger.log(Level.WARNING, e.getMessage());
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