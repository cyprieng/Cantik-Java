package com.cantik.core.config;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Files;

import static org.junit.Assert.*;

/**
 * @author cyprien
 */
public class ObjectFileWriterTest {
	@Rule
	public final static TemporaryFolder folder = new TemporaryFolder();

	private File testFile;

	@Before
	public void setUp() throws Exception {
		testFile = folder.newFile("test");
	}

	@After
	public void tearDown() throws Exception {
		Files.delete(testFile.toPath());
	}

	@Test
	public void testStore() throws Exception {
		TestClassSerializable ref = new TestClassSerializable();
		ObjectFileWriter.store(ref, testFile);
		Object actual = ObjectFileWriter.get(testFile);
		assertEquals(ref.getClass(), actual.getClass());
	}
}

class TestClassSerializable implements Serializable {
	public static final String test = "test";
}