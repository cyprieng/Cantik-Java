package com.updater;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * Update a project from the web, and launch it
 *
 * @author cyprien
 */
public class Main {
	private static final String thisPath = "cantik.exe";

	/**
	 * URL of the info file which contain the timestamp of the release
	 */
	public static final String infoURL = "https://cyprieng.github.io/Cantik/download/info.txt";

	/**
	 * URL of the release
	 */
	public static final String zipURL = "https://cyprieng.github.io/Cantik/download//cantik.zip";

	/**
	 * Path of the project (where the zip will be extracted)
	 */
	public static final String projectPath = "cantik";

	/**
	 * Path of the jar to launch and update
	 */
	public static final String jarPath = projectPath + File.separator + "cantik.jar";

	public static void main(String[] args) {
		// Get timestamp of the release
		int timestamp = 0;
		try {
			URL url = new URL(infoURL);
			BufferedReader br = new BufferedReader(
					new InputStreamReader(url.openStream()));

			timestamp = Integer.parseInt(br.readLine());

			br.close();
		} catch (java.io.IOException e) {
		}

		File f = new File(jarPath);
		if (timestamp > f.lastModified() / 1000) { // The web release is more recent than the local one
			JFrame w = getUpdatingDialog(); // Show updating dialog
			try {
				// Download zip
				URL website = new URL(zipURL);
				ReadableByteChannel rbc = Channels.newChannel(website.openStream());
				FileOutputStream fos = new FileOutputStream(projectPath + ".zip");
				fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
				fos.close();

				// Extract zip
				try {
					ZipFile zipFile = new ZipFile(projectPath + ".zip");
					zipFile.extractAll(projectPath);
				} catch (ZipException e) {
				}

				// Delete zip
				File zip = new File(projectPath + ".zip");
				zip.delete();
			} catch (java.io.IOException e) {
				if (System.getProperty("os.name").toLowerCase().contains("win")) {
					// We try to run as admin
					w.dispose();
					try {
						Runtime.getRuntime().exec("Elevate.exe " + thisPath);
					} catch (IOException e1) {
					}
					return;
				}
			}

			w.dispose(); //Close window
		}

		try {
			// Run a java app in a separate system process
			final Process proc = new ProcessBuilder(
					"java",
					"-jar",
					f.getAbsolutePath())
					.directory(new File(projectPath))
					.start();

			// Then retrieve the process output
			InputStream in = proc.getInputStream();
			InputStream err = proc.getErrorStream();
		} catch (IOException e) {
		}
	}

	/**
	 * Get a JFrame with a progress bar
	 *
	 * @return a Jframe
	 */
	private static JFrame getUpdatingDialog() {
		// Set look and feel of the system
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}

		// Create JFrame
		JFrame frame = new JFrame("Updating");
		try {
			frame.setIconImage(ImageIO.read(new File("cantik.png")));
		} catch (IOException e) {
		}

		// Progress bar
		JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);

		// JPanel
		JPanel contentPane = new JPanel();
		contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(new BorderLayout());
		contentPane.add(progressBar, BorderLayout.CENTER);

		// Frame option
		frame.setContentPane(contentPane);
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setSize(new Dimension(400,
				25 + frame.getInsets().top + frame.getInsets().bottom));
		frame.setLocationRelativeTo(null);

		return frame;
	}
}
