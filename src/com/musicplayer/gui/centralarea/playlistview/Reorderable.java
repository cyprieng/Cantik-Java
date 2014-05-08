package com.musicplayer.gui.centralarea.playlistview;

/**
 * Interface for reorderable object
 *
 * @author cyprien
 */
public interface Reorderable {
	/**
	 * Reorder. Move from fromIndex to toIndex
	 *
	 * @param fromIndex
	 * 		Origin of the move
	 * @param toIndex
	 * 		Final place
	 */
	public void reorder(int fromIndex, int toIndex);
}