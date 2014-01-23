package com.musicplayer.core;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.musicplayer.core.config.ObjectFileWriter;
import com.musicplayer.core.song.Song;

/**
 * Manage the music library: retrieve and store it. It uses the singleton design
 * pattern.
 * 
 * @author cyprien
 * 
 */
public class MusicLibrary extends Thread {
	/**
	 * Store the unique instance of MusicLibrary
	 */
	private static MusicLibrary musicLibrary;

	/**
	 * Path of the library
	 */
	private String libraryPath;

	/**
	 * Structure storing the music library and the temporary one
	 */
	private HashMap<String, HashMap<String, HashSet<Song>>> library,
			libraryTemp;

	/**
	 * Constructor which only init library var
	 */
	private MusicLibrary() {
		super("MusicLibrary");
		this.library = new HashMap<String, HashMap<String, HashSet<Song>>>();
	}

	/**
	 * Get the unique instance of MusicLibrary
	 * 
	 * @return the unique instance of MusicLibrary
	 */
	public static MusicLibrary getMusicLibrary() {
		if (musicLibrary == null) {
			musicLibrary = new MusicLibrary();
		}

		return musicLibrary;
	}

	/**
	 * Load the path in the library
	 * 
	 * @param path
	 *            The path to load
	 */
	@SuppressWarnings("unchecked")
	public void loadLibraryFolder(String path) {
		this.libraryPath = path;

		try {
			// Get library from file
			this.library = (HashMap<String, HashMap<String, HashSet<Song>>>) ObjectFileWriter
					.get(new File(Core.getUserPath() + "library"));
		} catch (Exception e) {

		} finally {
			// Start the thread => scan the library
			this.start();
		}
	}

	/**
	 * Add song to the library
	 * 
	 * @param song
	 *            Song to add to the library
	 */
	public void addSong(Song song) {
		if (!libraryTemp.containsKey(song.getArtist())) { // Artist does not
															// exist
															// => create artist
			libraryTemp.put(song.getArtist(),
					new HashMap<String, HashSet<Song>>());
		}
		if (!((HashMap<String, HashSet<Song>>) libraryTemp
				.get(song.getArtist())).containsKey(song.getAlbum())) { // Album
																		// does
																		// not
																		// exist
																		// =>
																		// create
																		// album
			((HashMap<String, HashSet<Song>>) libraryTemp.get(song.getArtist()))
					.put(song.getAlbum(), new HashSet<Song>());
		}

		// Create song
		((HashSet<Song>) ((HashMap<String, HashSet<Song>>) libraryTemp.get(song
				.getArtist())).get(song.getAlbum())).add(song);
	}

	/**
	 * Scan folder to add them to the library
	 * 
	 * @param folder
	 *            Folder to scan
	 */
	public void scanFolder(File folder) {
		if (folder.isFile()) { // It is a file
			try {
				this.addSong(new Song(folder.getAbsolutePath())); // Add the
																	// song
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

	/**
	 * Thread run function => scan the folder and store it in a file
	 */
	public void run() {
		this.libraryTemp = new HashMap<String, HashMap<String, HashSet<Song>>>(); // Init
																					// var
		this.scanFolder(new File(this.libraryPath)); // Scan library
		this.library = this.libraryTemp; // Set the library value to the
											// temporary one
		this.libraryTemp = null; // Destroy the temporary library

		try {
			// Store library in file
			ObjectFileWriter.store(this.library, new File(Core.getUserPath()
					+ "library"));
		} catch (Exception e) {
		}
	}

	/**
	 * Get the list of artists
	 * 
	 * @return Enumeration of the artists
	 */
	public Set<String> getArtists() {
		try {
			return library.keySet();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Get the list of albums
	 * 
	 * @param artist
	 *            The artist of the albums to retrieve
	 * @return Enumeration of the albums
	 */
	public Set<String> getAlbums(String artist) {
		try {
			return ((HashMap<String, HashSet<Song>>) library.get(artist))
					.keySet();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Get the list of the songs
	 * 
	 * @param artist
	 *            The artist of the songs to retrieve
	 * @return ArrayList of the songs
	 */
	public Set<Song> getSongs(String artist) {
		try {
			Set<Song> songs = new HashSet<Song>();

			// Get songs for each albums
			Set<String> albums = this.getAlbums(artist);
			for (String album : albums) {
				songs.addAll(this.getSongs(artist, album));
			}

			return songs;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Get the list of the songs
	 * 
	 * @param artist
	 *            The artist of the songs to retrieve
	 * @param album
	 *            The album of the songs to retrieve
	 * @return ArrayList of the songs
	 */
	public Set<Song> getSongs(String artist, String album) {
		try {
			return ((HashSet<Song>) ((HashMap<String, HashSet<Song>>) library
					.get(artist)).get(album));
		} catch (Exception e) {
			return null;
		}
	}
}
