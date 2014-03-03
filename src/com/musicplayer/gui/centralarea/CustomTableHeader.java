package com.musicplayer.gui.centralarea;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.musicplayer.gui.GUIParameters;

/**
 * Customize the header of a JTable
 * 
 * @author cyprien
 * 
 */
public class CustomTableHeader {
	/**
	 * Customize the given header
	 * 
	 * @param header
	 *            The header to customize
	 */
	public static void customizeHeader(JTableHeader header) {
		header.setBackground(GUIParameters.LEFTBAR_BACKGROUND);

		for (int i = 0; i < header.getColumnModel().getColumnCount(); i++) {
			// Set renderer
			TableColumn column = header.getColumnModel().getColumn(i);
			column.setHeaderRenderer(new HeaderRenderer());
		}
	}

	/**
	 * Renderer of the header of the table
	 * 
	 * @author cyprien
	 * 
	 */
	public static class HeaderRenderer extends JLabel implements
			TableCellRenderer {
		private static final long serialVersionUID = -3824354933152657068L;

		/**
		 * Renderer of the header of the table
		 */
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean hasFocus, boolean isSelected, int row,
				int col) {
			// Set style
			setText(value.toString());
			setForeground(Color.WHITE);
			setBorder(BorderFactory.createEmptyBorder(10, 2, 10, 2));
			return this;
		}
	}
}
