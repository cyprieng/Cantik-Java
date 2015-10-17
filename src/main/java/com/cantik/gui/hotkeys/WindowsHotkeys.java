package com.cantik.gui.hotkeys;

import com.melloware.jintellitype.IntellitypeListener;
import com.melloware.jintellitype.JIntellitype;
import com.cantik.core.playlist.Playlist;

import java.io.File;

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
		// Load the correct dll
		if (System.getProperty("sun.arch.data.model").equals("64"))
			JIntellitype.setLibraryLocation(new File("JIntellitype64.dll"));
		else
			JIntellitype.setLibraryLocation(new File("JIntellitype.dll"));

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





