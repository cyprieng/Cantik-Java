package com.musicplayer.gui.centralarea.musiclibrary;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;

import com.musicplayer.core.musiclibrary.ArtistInfo;
import com.musicplayer.core.song.Song;
import com.musicplayer.gui.GUIParameters;

/**
 * Custom tree cell renderer for the music library view
 * 
 * @author cyprien
 * 
 */
public class CustomTreeCellRenderer extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = -1612130507183462613L;

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);

		// Get node
		DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) value;
		Object nodeInfo = node.getUserObject();

		if (nodeInfo instanceof String) {
			if (node.getParent() != tree.getModel().getRoot()
					&& node.getParent() != null) {
				// Album cover
				setIcon(new ImageIcon(ArtistInfo.getAlbumImage((String) node
						.getParent().getUserObject(), (String) nodeInfo)));
			}
		} else if (nodeInfo instanceof Song) {
			// Song text
			setText((String) ((DefaultTreeTableModel) tree.getModel())
					.getValueAt(node, 0));
		}

		setFont(GUIParameters.getCentralFont().deriveFont(15.0f));

		return this;
	}
}
