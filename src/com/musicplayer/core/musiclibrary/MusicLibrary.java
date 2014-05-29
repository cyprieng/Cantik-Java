package com.musicplayer.core.musiclibrary;

import com.musicplayer.core.Core;
import com.musicplayer.core.InvalidFileException;
import com.musicplayer.core.Log;
import com.musicplayer.core.config.ObjectFileWriter;
import com.musicplayer.core.song.Song;

import java.io.File;
import java.util.*;

/**
 * Manage the music library: retrieve and store it. It uses the singleton design
 * pattern.
 *
 * @author cyprien
 */
public class MusicLibrary extends Observable implements Runnable {
	/**
	 * Store the unique instance of MusicLibrary
	 */
	private static MusicLibrary musicLibrary;
	/**
	 * Structure storing the music library and the temporary one
	 */
	protected Map<String, Map<String, Set<Song>>> library, libraryTemp;
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
	 * Insensitive comparator for Map
	 */
	protected final Comparator<String> comparator = new Comparator<String>() {
		@Override
		public int compare(String s1, String s2) {
			int result = s1.compareToIgnoreCase(s2);
			if (result == 0)
				result = s1.compareTo(s2);
			return result;
		}
	};

	/**
	 * Constructor which only init library var
	 */
	protected MusicLibrary() {
		t = new Thread(this);
		t.setName("MusicLibrary");
		this.library = new TreeMap<String, Map<String, Set<Song>>>(comparator);
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
					.get(new File(Core.getUserPath() + "library"));

			// Library is ready
			synchronized (this) {
				ready = true;
				this.notifyAll();
			}
		} catch (Exception e) {
			Log.addEntry(e);
		} finally {
			if (!t.isAlive()) {
				// Start the thread => scan the library
				t.start();
			}
		}
	}

	/**
	 * Add song to the library
	 *
	 * @param song
	 * 		Song to add to the library
	 */
	public void addSong(Song song) {
		if (!libraryTemp.containsKey(song.getArtist())) { // Artist does not exist => create artist
			libraryTemp.put(song.getArtist(), new TreeMap<String, Set<Song>>(comparator));
		}
		if (!((Map<String, Set<Song>>) libraryTemp.get(song.getArtist()))
				.containsKey(song.getAlbum())) { // Album does not exist => create album
			((Map<String, Set<Song>>) libraryTemp.get(song.getArtist())).put(
					song.getAlbum(), new HashSet<Song>());
		}

		// Create song
		((Set<Song>) ((Map<String, Set<Song>>) libraryTemp
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
		this.libraryTemp = new TreeMap<String, Map<String, Set<Song>>>(comparator); // Init var
		this.scanFolder(new File(this.libraryPath)); // Scan library
		this.library = this.libraryTemp; // Set the library value to the temporary one
		this.libraryTemp = null; // Destroy the temporary library

		// Library is ready
		synchronized (this) {
			ready = true;
			this.notifyAll();
		}
		// Notiy changes
		setChanged();
		notifyObservers();

		try {
			// Store library in file
			ObjectFileWriter.store(this.library, new File(Core.getUserPath()
					+ "library"));
		} catch (Exception e) {
			Log.addEntry(e);
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
			Log.addEntry(e);
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
			Log.addEntry(e);
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
			Log.addEntry(e);
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
			Log.addEntry(e);
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
