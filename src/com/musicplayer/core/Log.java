package com.musicplayer.core;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Manage log
 *
 * @author cyprien
 */
public class Log {
	/**
	 * Store the global logget
	 */
	private static Logger logger;

	/**
	 * Filehandler for logs
	 */
	private static FileHandler fh;

	/**
	 * Initialise the logger
	 */
	public static void setup() {
		// Get global logger
		logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

		// Suppress the logging output to the console
		logger.setUseParentHandlers(false);

		// Add output file
		try {
			fh = new FileHandler("error.log", false);
		} catch (IOException e) {
		}

		fh.setFormatter(new SimpleFormatter());
		logger.addHandler(fh);
		logger.setLevel(Level.CONFIG);
	}

	/**
	 * Log an error
	 *
	 * @param level
	 * 		Level of the error
	 * @param msg
	 * 		Message of the error
	 */
	public static void addEntry(Level level, String msg) {
		// Setup if it has not been before
		if (fh == null)
			setup();

		logger.log(level, msg);
	}

	/**
	 * Log an error with a default level of WARNING
	 *
	 * @param msg
	 * 		Message of the error
	 */
	public static void addEntry(String msg) {
		addEntry(Level.WARNING, msg);
	}

	/**
	 * Log an exception
	 *
	 * @param level
	 * 		Level of the error
	 * @param e
	 * 		Exception to log
	 */
	public static void addEntry(Level level, Exception e) {
		addEntry(level, e.getMessage());
	}

	/**
	 * Log an exception with a default level of WARNING
	 *
	 * @param e
	 * 		Exception to log
	 */
	public static void addEntry(Exception e) {
		addEntry(e.getMessage());
	}
}
