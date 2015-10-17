package com.cantik.gui.centralarea.musiclibrary;

import com.cantik.core.musiclibrary.ArtistInfo;
import com.cantik.core.song.Song;
import org.jdesktop.swingx.JXTreeTable;
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
	private HashMap<DefaultMutableTreeTableNode, ImageIcon> cover;

	/**
	 * Store connection beetwen artist node and image
	 */
	private HashMap<DefaultMutableTreeTableNode, ImageIcon> artist;

	/**
	 * JXTreeTable managed by this renderer
	 */
	private JXTreeTable treeTable;

	public CustomTreeCellRenderer(JXTreeTable treeTable) {
		super();
		cover = new HashMap<DefaultMutableTreeTableNode, ImageIcon>();
		artist = new HashMap<DefaultMutableTreeTableNode, ImageIcon>();
		this.treeTable = treeTable;
	}

	@Override
	public Component getTreeCellRendererComponent(final JTree tree, Object value,
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
					setIcon(cover.get(node));
				} else { // Get cover
					setIcon((ArtistInfo.getDefaultAlbumImage(new Dimension(50, 50)))); // Set to default

					// Start a thread to get the real cover
					Thread t = new Thread(new Runnable() {
						@Override
						public void run() {
							// Store the cover
							cover.put(node, ArtistInfo.getAlbumImage((String) node
									.getParent().getUserObject(), (String) nodeInfo, new Dimension(50, 50)));
							setIcon(cover.get(node)); // Change it

							treeTable.repaint(); // Repaint
						}
					});
					t.start();
				}
			} else { // Artist image
				if (artist.get(node) != null) { // Image has already been loaded
					setIcon(artist.get(node));
				} else { // Get image
					// Start a thread to get the image
					Thread t = new Thread(new Runnable() {
						@Override
						public void run() {
							// Store the image
							artist.put(node, ArtistInfo.getArtistImage((String) node
									.getUserObject()));

							if (artist.get(node) != null)
								setIcon(artist.get(node)); // Change it

							treeTable.repaint(); // Repaint
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
