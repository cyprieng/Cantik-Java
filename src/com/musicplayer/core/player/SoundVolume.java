package com.musicplayer.core.player;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line.Info;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Port;

import com.musicplayer.core.Log;

/**
 * Manage the sound volume
 * 
 * @author cyprien
 * 
 */
public class SoundVolume {
	/**
	 * Source
	 */
	Info source = Port.Info.SPEAKER;

	/**
	 * Volume controller
	 */
	FloatControl volumeControl;

	/**
	 * Init the controller
	 */
	public SoundVolume() {
		if (AudioSystem.isLineSupported(source)) {
			try {
				Port outline = (Port) AudioSystem.getLine(source);
				outline.open();
				volumeControl = (FloatControl) outline
						.getControl(FloatControl.Type.VOLUME);
			} catch (LineUnavailableException e) {
				Log.addEntry(e);
			}
		}
	}

	/**
	 * Modify volume
	 * 
	 * @param newVolume
	 *            The new volume from 0.0 to 1.0
	 */
	public void setVolume(float newVolume) {
		volumeControl.setValue(newVolume);
	}

}
