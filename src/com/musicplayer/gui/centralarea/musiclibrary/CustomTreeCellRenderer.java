package com.musicplayer.gui.centralarea.musiclibrary;

import com.musicplayer.core.musiclibrary.ArtistInfo;
import com.musicplayer.core.song.Song;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.util.HashMap;

/**
 * Custom tree cell renderer for the music library view
 *
 * @author cyprien
 */
public class CustomTreeCellRenderer extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = -1612130507183462613L;

	/**
	 * Store connection between album node and cover image
	 */
	private HashMap<DefaultMutableTreeTableNode, Image> cover;

	/**
	 * Store connection beetwen artist node and image
	 */
	private HashMap<DefaultMutableTreeTableNode, Image> artist;

	public CustomTreeCellRenderer() {
		super();
		cover = new HashMap<DefaultMutableTreeTableNode, Image>();
		artist = new HashMap<DefaultMutableTreeTableNode, Image>();
	}

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
				if (cover.get(node) != null) { // Cover has already been loaded
					setIcon(new ImageIcon(cover.get(node)));
				} else { // Get cover
					setIcon(new ImageIcon(ArtistInfo.getDefaultAlbumImage())); // Set to default

					// Start a thread to get the real cover
					Thread t = new Thread(new Runnable() {
						@Override
						public void run() {
							// Store the cover
							cover.put(node, ArtistInfo.getAlbumImage((String) node
									.getParent().getUserObject(), (String) nodeInfo));
							setIcon(new ImageIcon(cover.get(node))); // Change it
						}
					});
					t.start();
				}
			} else { // Artist image
				if (artist.get(node) != null) { // Image has already been loaded
					setIcon(new ImageIcon(artist.get(node)));
				} else { // Get image
					// Start a thread to get the image
					Thread t = new Thread(new Runnable() {
						@Override
						public void run() {
							// Store the image
							artist.put(node, ArtistInfo.getArtistImage((String) node
									.getUserObject()));

							if (artist.get(node) != null)
								setIcon(new ImageIcon(artist.get(node))); // Change it
						}
					});
					t.start();
				}
			}
		} else if (nodeInfo instanceof Song) {
			// Song text
			setText((String) ((DefaultTreeTableModel) tree.getModel())
					.getValueAt(node, 0));
		}

		return this;
	}
}
