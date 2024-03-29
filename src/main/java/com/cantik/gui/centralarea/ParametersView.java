package com.cantik.gui.centralarea;

import com.cantik.gui.MainWindow;
import com.cantik.gui.ParametersWindow;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Show the Parameters window
 *
 * @author cyprien
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
				MainWindow.setCentralArea(MainWindow.getPreviousCardShown()); // Show previous card

				new ParametersWindow(); // Open window
			}
		});
	}
}
