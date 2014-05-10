package com.musicplayer.gui.centralarea.musiclibrary;

import com.musicplayer.core.musiclibrary.ArtistInfo;
import com.musicplayer.core.song.Song;
import com.musicplayer.gui.GUIParameters;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * Custom tree cell renderer for the music library view
 *
 * @author cyprien
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
		final DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) value;
		final Object nodeInfo = node.getUserObject();

		if (nodeInfo instanceof String) {
			if (node.getParent() != tree.getModel().getRoot()
					&& node.getParent() != null) {
				// Album cover
				setIcon(new ImageIcon(ArtistInfo.getDefaultAlbumImage())); // Set to default

				// Start a thread to get the real cover
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						setIcon(new ImageIcon(ArtistInfo.getAlbumImage((String) node
								.getParent().getUserObject(), (String) nodeInfo)));
					}
				});
				t.start();
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
