package com.cantik.core.config;

import com.cantik.core.Core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Write config in a file
 *
 * @author cyprien
 */
public class ConfigFileWriter {
	/**
	 * Write the ConfigFileParser in the config file
	 *
	 * @throws Exception
	 * @see ConfigFileParser
	 */
	public static void writeConfigFile() throws Exception {
		String configPath = Core.getUserPath() + "cantik.config";
		writeFile(configPath);
	}

	/**
	 * Write the ConfigFileParser in the config file
	 *
	 * @param configPath
	 * 		Path of the config file to write.
	 * @throws Exception
	 * @see ConfigFileParser
	 */
	public static void writeConfigFile(String configPath) throws Exception {
		writeFile(configPath);
	}

	/**
	 * Write the ConfigFileParser in the config file
	 *
	 * @throws Exception
	 * @see ConfigFileParser
	 */
	private static void writeFile(String configPath) throws Exception {
		// Open config file
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(configPath)));

		// Run through the hashmap of ConfigFileParser
		HashMap<String, String> params = ConfigFileParser.getConfigFileParser().getHashMap();
		Set<String> keys = params.keySet();
		Iterator<String> it = keys.iterator();

		while (it.hasNext()) {
			// Write te key and value
			String key = it.next();
			bw.write(key + " = " + params.get(key) + "\r\n");
		}

		bw.close();
	}
}
