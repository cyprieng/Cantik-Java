package com.musicplayer.gui.hotkeys;

import com.melloware.jintellitype.IntellitypeListener;
import com.melloware.jintellitype.JIntellitype;
import com.musicplayer.core.playlist.Playlist;

/**
 * Register hotkeys for Windows
 *
 * @author cyprien
 */
public class WindowsHotkeys implements IntellitypeListener {
	/**
	 * Init Jintellitype
	 */
	public WindowsHotkeys() {
		// Initialize JIntellitype
		JIntellitype.getInstance();

		// Assign this class to be a IntellitypeListener
		JIntellitype.getInstance().addIntellitypeListener(this);
	}

	@Override
	public void onIntellitype(int i) {
		switch (i) {
			case JIntellitype.APPCOMMAND_MEDIA_PLAY_PAUSE:
				Playlist.getPlaylist().playPause();
				break;
			case JIntellitype.APPCOMMAND_MEDIA_NEXTTRACK:
				Playlist.getPlaylist().next();
				break;
			case JIntellitype.APPCOMMAND_MEDIA_PREVIOUSTRACK:
				Playlist.getPlaylist().back();
				break;
		}
	}
}





