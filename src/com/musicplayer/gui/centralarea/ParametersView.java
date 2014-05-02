package com.musicplayer.gui.centralarea;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import com.musicplayer.gui.MainWindow;
import com.musicplayer.gui.ParametersWindow;

/**
 * Show the Parameters window
 * 
 * @author cyprien
 * 
 */
public class ParametersView extends CentralArea {
	private static final long serialVersionUID = 8490343603154453550L;

	/**
	 * Show the parameters window when this panel is shown
	 */
	public ParametersView() {
		// Show window when this CentralArea component is shown
		addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				MainWindow.setCentralArea("Library"); // Show library

				new ParametersWindow(); // Ope window
			}

			public void componentHidden(ComponentEvent e) {
			}
		});
	}
}
