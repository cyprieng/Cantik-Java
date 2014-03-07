package com.musicplayer.core.musiclibrary;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

import com.musicplayer.core.InvalidFileException;
import com.musicplayer.core.config.ConfigFileParser;
import com.musicplayer.core.song.Song;

/**
 * Music library with a filter
 * 
 * @author cyprien
 * 
 */
public class SearchMusicLibrary extends MusicLibrary {
	/**
	 * The query to find in the songs
	 */
	private String query;

	/**
	 * Init music library
	 * 
	 * @param query
	 *            The query to find in the songs
	 */
	public SearchMusicLibrary(String query) {
		super();
		this.query = query.toLowerCase();
		this.start();
	}

	@Override
	public void scanFolder(File folder) {
		if (folder.isFile()) { // It is a file
			try {
				Song s = new Song(folder.getAbsolutePath());
				if (s.getAlbum().toLowerCase().contains(query)
						|| s.getArtist().toLowerCase().contains(query)
						|| s.getTitle().toLowerCase().contains(query)) { // Check
																			// query
					this.addSong(s); // Add the song
				}
			} catch (InvalidFileException e) {
			}

		}

		if (folder.isDirectory()) { // It is a folder
			// Scan the subfolders
			File[] list = folder.listFiles();
			for (File f : list) {
				this.scanFolder(f);
			}
		}
	}

	@Override
	public void run() {
		this.libraryTemp = new HashMap<String, HashMap<String, HashSet<Song>>>(); // Init
																					// var

		this.scanFolder(new File(ConfigFileParser.getConfigFileParser()
				.getParams("library"))); // Scan library
		this.library = this.libraryTemp; // Set the library value to the
											// temporary one

		this.libraryTemp = null; // Destroy the temporary library

		// Library is ready
		synchronized (this) {
			ready = true;
			this.notifyAll();
		}
	}
}
