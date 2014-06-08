package com.musicplayer.gui.hotkeys;

import com.musicplayer.core.Log;
import com.musicplayer.core.playlist.Playlist;
import jxgrabkey.HotkeyConflictException;
import jxgrabkey.JXGrabKey;
import jxgrabkey.X11KeysymDefinitions;

import java.io.File;
import java.io.IOException;

/**
 * Register hotkeys for Linux
 *
 * @author cyprien
 */
public class LinuxHotkeys implements jxgrabkey.HotkeyListener {
	public LinuxHotkeys() {
		// Load JXGrabKey lib
		try {
			System.load(new File("libJXGrabKey.so").getCanonicalPath());
		} catch (IOException e) {
			Log.addEntry(e);
		}

		// Register hotkeys
		try {
			JXGrabKey.getInstance().registerX11Hotkey(1, 0, X11KeysymDefinitions.AUDIO_PLAY);
			JXGrabKey.getInstance().registerX11Hotkey(2, 0, X11KeysymDefinitions.AUDIO_NEXT);
			JXGrabKey.getInstance().registerX11Hotkey(3, 0, X11KeysymDefinitions.AUDIO_PREV);
		} catch (HotkeyConflictException e) {
			Log.addEntry(e);
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
