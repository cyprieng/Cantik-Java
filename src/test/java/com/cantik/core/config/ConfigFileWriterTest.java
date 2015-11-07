package com.cantik.core.config;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author cyprien
 */
public class ConfigFileWriterTest {
	@Rule
	public final static TemporaryFolder folder = new TemporaryFolder();

	private File configFile;

	private ConfigFileParser cfp;

	@Before
	public void setUp() throws Exception {
		cfp = ConfigFileParser.getConfigFileParser();
		cfp.setParam("test", "12345");
		cfp.setParam("abc", "def");

		configFile = folder.newFile("test.txt");
	}

	@After
	public void tearDown() throws Exception {
		Files.delete(configFile.toPath());
	}

	@Test
	public void testWriteConfigFile() throws Exception {
		ConfigFileWriter.writeConfigFile(configFile.getAbsolutePath());

		List<String> content = Files.readAllLines(configFile.toPath(), StandardCharsets.UTF_8);
		List<String> test = Arrays.asList("abc = def", "test = 12345");

		assertEquals(test, content);
	}
}