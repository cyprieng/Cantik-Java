package com.cantik.core.config;

import java.io.*;

/**
 * Store and get object from file
 *
 * @author cyprien
 */
public class ObjectFileWriter {
	/**
	 * Store an object in file
	 *
	 * @param o
	 * 		The object to store
	 * @param f
	 * 		The file in which we want to store the object
	 * @throws IOException
	 */
	public static void store(Object o, File f) throws IOException {
		FileOutputStream fos = new FileOutputStream(f);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(o);
		oos.flush();
		oos.close();
		fos.close();
	}

	/**
	 * Get a file from a file
	 *
	 * @param f
	 * 		The file containing the object to retrieve
	 * @return The object contains in the file
	 * @throws IOException, ClassNotFoundException
	 */
	public static Object get(File f) throws IOException, ClassNotFoundException {
		FileInputStream fin = new FileInputStream(f);
		ObjectInputStream ois = new ObjectInputStream(fin);
		Object o = ois.readObject();
		ois.close();
		return o;
	}
}
