package com.musicplayer.core.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Store and get object from file
 * 
 * @author cyprien
 * 
 */
public class ObjectFileWriter {
	/**
	 * Store an object in file
	 * 
	 * @param o
	 *            The object to store
	 * @param f
	 *            The file in which we want to store the object
	 * @throws Exception
	 */
	public static void store(Object o, File f) throws Exception {
		try {
			FileOutputStream fos = new FileOutputStream(f);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(o);
			oos.flush();
			oos.close();
			fos.close();
		} catch (IOException ex) {
			throw new Exception();
		}
	}

	/**
	 * Get a file from a file
	 * 
	 * @param f
	 *            The file containing the object to retrieve
	 * @return The object contains in the file
	 * @throws Exception
	 */
	public static Object get(File f) throws Exception {
		try {
			FileInputStream fin = new FileInputStream(f);
			ObjectInputStream ois = new ObjectInputStream(fin);
			Object o = ois.readObject();
			ois.close();
			return o;
		} catch (Exception e) {
			throw new Exception();
		}
	}
}
