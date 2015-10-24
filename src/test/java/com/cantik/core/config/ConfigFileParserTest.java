package com.cantik.core.config;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;

/**
 * @author cyprien
 */
public class ConfigFileParserTest extends TestCase {
	@Rule
	public final static TemporaryFolder folder = new TemporaryFolder();

	private File configFile;

	@Before
	public void setUp() throws Exception {
		// Write test config file
		configFile = folder.newFile("correct.txt");
		PrintWriter writer = new PrintWriter(configFile.getAbsolutePath(), "UTF-8");
		writer.println("#Config  ");
		writer.println("test=abc");
		writer.println("aaa     =         azerty");
		writer.close();
	}

	@After
	public void tearDown() throws Exception {
		Files.delete(configFile.toPath());
	}

	@Test
	public void testGetConfigFileParser() throws Exception {
		// Load config
		ConfigFileParser test = ConfigFileParser.getConfigFileParser();
		test.loadConfigFile(configFile);

		// Test config
		assertEquals("abc", test.getParams("test"));
		assertEquals("azerty", test.getParams("aaa"));
		assertEquals(null, test.getParams("error"));
	}
}