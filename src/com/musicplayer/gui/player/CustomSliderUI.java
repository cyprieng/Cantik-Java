package com.musicplayer.gui.player;

import com.musicplayer.core.Core;
import com.musicplayer.gui.GUIParameters;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;

/**
 * Custom UI for JSlider
 *
 * @author cyprien
 */
public class CustomSliderUI extends BasicSliderUI {
	/**
	 * Constructor: call super()
	 *
	 * @param slider
	 * 		Apply the UI to this slider
	 */
	public CustomSliderUI(JSlider slider) {
		super(slider);
	}

	@Override
	public void paintTrack(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		Rectangle t = trackRect;
		Rectangle t1 = thumbRect;

		// Played
		g2d.setColor(GUIParameters.LEFTBAR_ACTIVE);
		g2d.fillRect(0, t.y + 4, t1.x + t1.width / 2, 11);

		// To play
		g2d.setColor(Color.WHITE);
		g2d.fillRect(t1.x + t1.width / 2, t.y + 4, t.width, 11);
	}

	@Override
	public void paintThumb(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		Rectangle t = thumbRect;

		// Background
		g2d.setColor(Color.WHITE);
		g2d.fillRect(t.x, t.y, 30, t.height);

		// Draw string
		g2d.setColor(Color.BLACK);
		g2d.drawString(Core.stringifyDuration(this.slider.getValue()), t.x + 2,
				t.y + 13);

		// Modify thumb size
		this.thumbRect.width = 30;
	}

}
