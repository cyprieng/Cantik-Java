package com.cantik.core.player;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line.Info;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Port;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manage the sound volume
 *
 * @author cyprien
 */
public class SoundVolume {
	/**
	 * Logger for the class
	 */
	private static Logger logger = Logger.getLogger(SoundVolume.class.getName());

	/**
	 * Source
	 */
	private final static Info[] SOURCE = {Port.Info.SPEAKER,
			Port.Info.LINE_OUT, Port.Info.HEADPHONE};

	/**
	 * Modify volume
	 *
	 * @param newVolume
	 * 		The new volume from 0.0 to 1.0
	 */
	public static void setVolume(float newVolume) {
		for (Info s : SOURCE) {
			if (AudioSystem.isLineSupported(s)) { // Check if line is supported
				try {
					// Set volume
					Port outline = (Port) AudioSystem.getLine(s);
					outline.open();
					((FloatControl) outline
							.getControl(FloatControl.Type.VOLUME))
							.setValue(newVolume);
				} catch (LineUnavailableException e) {
					logger.log(Level.WARNING, e.getMessage());
				}
			}
		}
	}

}
