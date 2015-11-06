package com.cantik.gui.player.volumecontrol;

import com.cantik.gui.MainWindow;

import javax.swing.*;
import java.awt.*;

/**
 * Floating JPanel showing VolumeControl
 *
 * @author cyprien
 */
public class VolumePanel extends JPanel {
	private static final long serialVersionUID = 8107614292050804904L;

	/**
	 * Store the slider
	 */
	private VolumeControl vc;

	/**
	 * The calling button
	 */
	private JButton button;

	/**
	 * Init button
	 *
	 * @param vb
	 * 		The calling button. This JPanel will be placed next to this
	 * 		button.
	 */
	public VolumePanel(JButton vb) {
		// Init
		super();
		setOpaque(false);
		setLayout(null);
		button = vb;

		// Add volume control
		vc = new VolumeControl();
		add(vc);

		// Add to main window
		MainWindow.getMainWindow().setGlassPane(this);
		setVisible(false);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		// Slider position
		vc.setBounds(button.getParent().getX() + button.getX() + 20, button
						.getParent().getY()
						+ button.getY()
						- vc.getPreferredSize().height - 20,
				vc.getPreferredSize().width, vc.getPreferredSize().height
		);
	}
}
