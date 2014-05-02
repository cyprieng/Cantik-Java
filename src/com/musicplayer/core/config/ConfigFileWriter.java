package com.musicplayer.core.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.musicplayer.core.Core;

/**
 * Write config in a file
 * 
 * @author cyprien
 * 
 */
public class ConfigFileWriter {
	/**
	 * Wirte the ConfigFileParser in the config file
	 * 
	 * @throws Exception
	 * @see ConfigFileParser
	 */
	public static void writeConfigFile() throws Exception {
		// Open config file
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(
				Core.getUserPath() + "config")));

		// Run through the hashmap of ConfigFileParser
		HashMap<String, String> params = ConfigFileParser.getConfigFileParser()
				.getHashMap();
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
