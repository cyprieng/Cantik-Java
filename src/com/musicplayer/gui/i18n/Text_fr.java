package com.musicplayer.gui.i18n;

import java.util.ListResourceBundle;

/**
 * French text translation
 *
 * @author cyprien
 */
public class Text_fr extends ListResourceBundle {
	/**
	 * Matching between key and French
	 */
	private Object[][] contents = {
			// Music Library
			{"loadingLibrary", "Chargement de la bibliothèque..."},
			{"noLibrary", "Pas de bibliothèque"},
			{"year", "Année"},

			// Playlist
			{"title", "Titre"},
			{"artist", "Artiste"},
			{"album", "Album"},
			{"length", "Durée"},
			{"emptyPlaylist", "Playlist vide"},
			{"removeFromPlaylist", "Supprimer de la playlist"},
			{"stopAfter", "Arrêter aprés cette piste"},
			{"unsetStopAfter", "Supprimer \"arrêter après cette piste\""},

			// Track Lyrics
			{"noSong", "Pas de piste"},
			{"lyric", "Paroles"},
			{"noLyric", "Pas de parole"},

			// Track Artist
			{"noBio", "Pas de biographie"},

			// Tray Icon
			{"playPause", "Play/Pause"},
			{"back", "Précédent"},
			{"next", "Suivant"},

			// Parameters Window
			{"parameters", "Paramètres"},
			{"musicPath", "Chemin de la musique"},
			{"loggedIn", "Connecté"},
			{"authenticate", "S'authentifier"},
			{"error", "Erreur"},
			{"invalidFolderName", "Dossier non valide"},

			// Leftbar
			{"collection", "Collection"},
			{"nowPlaying", "En cours de lecture"},
			{"Library", "Bibliothèque"},
			{"Local File", "Fichier local"},
			{"Playlist", "Playlist"},
			{"Lyrics", "Paroles"},
			{"Artist Info", "Info Artiste"},
			{"Settings", "Paramètres"},

			// Search
			{"search", "Rechercher"}

	};

	@Override
	protected Object[][] getContents() {
		return contents;
	}
}
