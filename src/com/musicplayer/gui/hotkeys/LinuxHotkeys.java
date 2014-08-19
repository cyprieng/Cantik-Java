package com.musicplayer.gui.hotkeys;

import com.musicplayer.core.playlist.Playlist;
import jxgrabkey.HotkeyConflictException;
import jxgrabkey.JXGrabKey;
import jxgrabkey.X11KeysymDefinitions;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Register hotkeys for Linux
 *
 * @author cyprien
 */
public class LinuxHotkeys implements jxgrabkey.HotkeyListener {
	/**
	 * Logger for the class
	 */
	private static Logger logger = Logger.getLogger(LinuxHotkeys.class.getName());

	public LinuxHotkeys() {
		// Load JXGrabKey lib
		try {
			if (System.getProperty("sun.arch.data.model").equals("64"))
				System.load(new File("libJXGrabKey64.so").getCanonicalPath());
			else
				System.load(new File("libJXGrabKey.so").getCanonicalPath());
		} catch (IOException e) {
			logger.log(Level.WARNING, e.getMessage());
		}

		// Register hotkeys
		try {
			JXGrabKey.getInstance().registerX11Hotkey(1, 0, X11KeysymDefinitions.AUDIO_PLAY);
			JXGrabKey.getInstance().registerX11Hotkey(2, 0, X11KeysymDefinitions.AUDIO_NEXT);
			JXGrabKey.getInstance().registerX11Hotkey(3, 0, X11KeysymDefinitions.AUDIO_PREV);
		} catch (HotkeyConflictException e) {
			logger.log(Level.WARNING, e.getMessage());
		}

		JXGrabKey.getInstance().addHotkeyListener(this);
	}

	@Override
	public void onHotkey(int i) {
		switch (i) {
			case 1:
				Playlist.getPlaylist().playPause();
				break;
			case 2:
				Playlist.getPlaylist().next();
				break;
			case 3:
				Playlist.getPlaylist().back();
				break;
		}
	}
}
