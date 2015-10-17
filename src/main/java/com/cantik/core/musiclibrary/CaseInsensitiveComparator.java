package com.cantik.core.musiclibrary;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Insensitive comparator for Map
 *
 * @author cyprien
 */
public class CaseInsensitiveComparator implements Comparator<String>, Serializable {
	private static final long serialVersionUID = 7749239940116694120L;

	@Override
	public int compare(String s1, String s2) {
		int result = s1.compareToIgnoreCase(s2);
		if (result == 0)
			result = s1.compareTo(s2);
		return result;
	}
}
