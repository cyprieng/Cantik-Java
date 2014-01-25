package com.musicplayer.core;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Log all error in a file
 * 
 * @author cyprien
 * 
 */
public class Log {
	/**
	 * Add an entry in the log file
	 * 
	 * @param entry
	 *            The string to write
	 */
	public static void addEntry(String entry) {
		try (PrintWriter out = new PrintWriter(new BufferedWriter(
				new FileWriter("log.txt", true)))) {
			Calendar date = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YY HH:mm:ss");
			out.println(sdf.format(date.getTime()) + ": " + entry);
		} catch (IOException e) {
		}
	}

	/**
	 * Add an Exception in the log file
	 * 
	 * @param entry
	 *            The Exception to write
	 */
	public static void addEntry(Exception entry) {
		addEntry(entry.getMessage());
	}
}
