package com.cantik.core.musiclibrary;

import com.cantik.core.InvalidFileException;
import com.cantik.core.config.ObjectFileWriter;
import com.cantik.core.Core;
import com.cantik.core.song.Song;

import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manage the music library: retrieve and store it. It uses the singleton design
 * pattern.
 *
 * @author cyprien
 */
public class MusicLibrary extends Observable implements Runnable {
	/**
	 * Logger for the class
	 */
	private static Logger logger = Logger.getLogger(MusicLibrary.class.getName());

	/**
	 * Store the unique instance of MusicLibrary
	 */
	private static MusicLibrary musicLibrary;

	/**
	 * Structure storing the music library and the temporary one
	 */
	protected Map<String, Map<String, Set<Song>>> library;

	/**
	 * Mark if the music library is ready
	 */
	protected boolean ready;

	/**
	 * Path of the library
	 */
	private String libraryPath;

	/**
	 * Store the thread
	 */
	protected Thread t;

	/**
	 * Constructor which only init library var
	 */
	protected MusicLibrary() {
		this.library = new TreeMap<String, Map<String, Set<Song>>>(new CaseInsensitiveComparator());
		this.ready = false;
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
	 * 		The path to load
	 */
	@SuppressWarnings("unchecked")
	public void loadLibraryFolder(String path) throws InvalidPathException {
		if (path == null) { // Null path
			// Library is ready
			synchronized (this) {
				ready = true;
				this.notifyAll();
			}

			throw new InvalidPathException();
		}

		File f = new File(path);
		if (!f.exists() || !f.isDirectory()) { // Invalid folder
			// Library is ready
			synchronized (this) {
				ready = true;
				this.notifyAll();
			}

			throw new InvalidPathException();
		}

		this.libraryPath = path;

		try {
			// Get library from file
			this.library = (Map<String, Map<String, Set<Song>>>) ObjectFileWriter
					.get(new File(Core.getUserPath() + "cantik.library"));

			// Library is ready
			synchronized (this) {
				ready = true;
				this.notifyAll();
			}
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage());
		} finally {
			t = new Thread(this);
			t.setName("MusicLibrary");

			// Start the thread => scan the library
			t.start();
		}
	}

	/**
	 * Add song to the library
	 *
	 * @param song
	 * 		Song to add to the library
	 */
	public void addSong(Song song) {
		if (!library.containsKey(song.getArtist())) { // Artist does not exist => create artist
			library.put(song.getArtist(), new TreeMap<String, Set<Song>>(new CaseInsensitiveComparator()));
		}
		if (!((Map<String, Set<Song>>) library.get(song.getArtist()))
				.containsKey(song.getAlbum())) { // Album does not exist => create album
			((Map<String, Set<Song>>) library.get(song.getArtist())).put(
					song.getAlbum(), new HashSet<Song>());
		}

		if (!this.getSongs(song.getArtist(), song.getAlbum()).contains(song))
			// Create song if it does not exist
			((Set<Song>) ((Map<String, Set<Song>>) library
					.get(song.getArtist())).get(song.getAlbum())).add(song);
	}

	/**
	 * Scan folder to add them to the library
	 *
	 * @param folder
	 * 		Folder to scan
	 */
	public void scanFolder(File folder) {
		if (folder.isFile()) { // It is a file
			try {
				this.addSong(new Song(folder.getAbsolutePath())); // Add the song
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
		this.scanFolder(new File(this.libraryPath)); // Scan library

		// Library is ready
		synchronized (this) {
			ready = true;
			this.notifyAll();
		}

		try {
			// Store library in file
			ObjectFileWriter.store(this.library, new File(Core.getUserPath()
					+ "cantik.library"));
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage());
		}

		// Notify changes
		setChanged();
		notifyObservers();
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
			logger.log(Level.WARNING, e.getMessage());
			return null;
		}
	}

	/**
	 * Get the list of albums
	 *
	 * @param artist
	 * 		The artist of the albums to retrieve
	 * @return Enumeration of the albums
	 */
	public Set<String> getAlbums(String artist) {
		try {
			return ((Map<String, Set<Song>>) library.get(artist)).keySet();
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage());
			return null;
		}
	}

	/**
	 * Get the list of the songs
	 *
	 * @param artist
	 * 		The artist of the songs to retrieve
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
			logger.log(Level.WARNING, e.getMessage());
			return null;
		}
	}

	/**
	 * Get the list of the songs
	 *
	 * @param artist
	 * 		The artist of the songs to retrieve
	 * @param album
	 * 		The album of the songs to retrieve
	 * @return ArrayList of the songs
	 */
	public Set<Song> getSongs(String artist, String album) {
		try {
			return ((Set<Song>) ((Map<String, Set<Song>>) library.get(artist))
					.get(album));
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage());
			return null;
		}
	}

	/**
	 * Check if the music library is ready
	 *
	 * @return True if it is ready
	 */
	public boolean isReady() {
		return ready;
	}
}
