package com.musicplayer.gui.i18n;

import java.util.ListResourceBundle;

/**
 * Default text translation (en)
 *
 * @author cyprien
 */
public class Text extends ListResourceBundle {
	/**
	 * Matching between key and English
	 */
	private Object[][] contents = {
			// Music Library
			{"loadingLibrary", "Loading library..."},
			{"noLibrary", "No library"},
			{"year", "Year"},

			// Playlist
			{"title", "Title"},
			{"artist", "Artist"},
			{"album", "Album"},
			{"length", "Length"},
			{"emptyPlaylist", "Empty playlist"},
			{"removeFromPlaylist", "Remove from playlist"},
			{"stopAfter", "Stop after this song"},
			{"unsetStopAfter", "Unset 'stop after this song'"},

			// Track Lyrics
			{"noSong", "No song"},
			{"lyric", "Lyrics"},
			{"noLyric", "No lyric"},

			// Track Artist
			{"noBio", "No biography"},

			// Tray Icon
			{"playPause", "Play/Pause"},
			{"back", "Back"},
			{"next", "Next"},

			// Parameters Window
			{"parameters", "Parameters"},
			{"musicPath", "Music Path"},
			{"loggedIn", "Logged in"},
			{"authenticate", "Authenticate"},
			{"error", "Error"},
			{"invalidFolderName", "Invalid folder name"},

			// Leftbar
			{"collection", "Collection"},
			{"nowPlaying", "Now Playing"},
			{"Library", "Library"},
			{"Local File", "Local File"},
			{"Playlist", "Playlist"},
			{"Lyrics", "Lyrics"},
			{"Artist Info", "Artist Info"},
			{"Settings", "Settings"},

			// Search
			{"search", "Search"}

	};

	@Override
	protected Object[][] getContents() {
		return contents;
	}
}
