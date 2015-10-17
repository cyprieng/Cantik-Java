package com.cantik.gui.player.volumecontrol;

import com.cantik.core.player.SoundVolume;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * JSlider managing the sound volume
 *
 * @author cyprien
 */
public class VolumeControl extends JSlider {
	private static final long serialVersionUID = -8931165783249348524L;

	/**
	 * Init the slider
	 */
	public VolumeControl() {
		super(JSlider.VERTICAL, 0, 100, 50);

		this.setBackground(Color.BLACK);

		// Size
		setPreferredSize(new Dimension(20, 100));
		setMinimumSize(getPreferredSize());
		setMaximumSize(getPreferredSize());

		// Onclick event
		this.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg) {
				// Set volume
				SoundVolume.setVolume((float) getValue() / 100.0f);
			}
		});
	}
}
