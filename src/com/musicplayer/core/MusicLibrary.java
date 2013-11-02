package com.musicplayer.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

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
	 * Structure storing the music library
	 */
	private Hashtable<String, Hashtable<String, ArrayList<Song>>> library;

	/**
	 * Constructor which only init library var
	 */
	private MusicLibrary() {
		super("MusicLibrary");
		this.library = new Hashtable<String, Hashtable<String, ArrayList<Song>>>();
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
	public void loadLibraryFolder(String path) {
		this.libraryPath = path;

		try {
			// Get library from file
			this.library = (Hashtable<String, Hashtable<String, ArrayList<Song>>>) ObjectFileWriter
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
		if (!library.containsKey(song.getArtist())) { // Artist does not exist
														// => create artist
			library.put(song.getArtist(),
					new Hashtable<String, ArrayList<Song>>());
		}
		if (!((Hashtable<String, ArrayList<Song>>) library
				.get(song.getArtist())).containsKey(song.getAlbum())) { // Album
																		// does
																		// not
																		// exist
																		// =>
																		// create
																		// album
			((Hashtable<String, ArrayList<Song>>) library.get(song.getArtist()))
					.put(song.getAlbum(), new ArrayList<Song>());
		}

		// Create song
		((ArrayList<Song>) ((Hashtable<String, ArrayList<Song>>) library
				.get(song.getArtist())).get(song.getAlbum())).add(song);
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
		// Scan library
		this.scanFolder(new File(this.libraryPath));
		try {
			// Store library in file
			ObjectFileWriter.store(this.library, new File(Core.getUserPath() + "library"));
		} catch (Exception e) {
		}
	}

	/**
	 * Get the list of artists
	 * 
	 * @return Enumeration of the artists
	 */
	public Enumeration<String> getArtists() {
		try {
			return library.keys();
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
	public Enumeration<String> getAlbums(String artist) {
		try {
			return ((Hashtable<String, ArrayList<Song>>) library.get(artist))
					.keys();
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
	public ArrayList<Song> getSongs(String artist, String album) {
		try {
			return ((ArrayList<Song>) ((Hashtable<String, ArrayList<Song>>) library
					.get(artist)).get(album));
		} catch (Exception e) {
			return null;
		}
	}
}
