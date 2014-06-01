package com.musicplayer.gui;

import com.musicplayer.core.Log;
import com.musicplayer.core.config.ConfigFileParser;
import com.musicplayer.core.config.ConfigFileWriter;
import com.musicplayer.core.musiclibrary.InvalidPathException;
import com.musicplayer.core.musiclibrary.MusicLibrary;
import com.musicplayer.core.scrobbler.Scrobbler;
import com.musicplayer.core.scrobbler.ScrobblerConfig;
import com.musicplayer.core.scrobbler.ScrobblerException;
import com.musicplayer.gui.centralarea.musiclibrary.MusicLibraryView;
import com.musicplayer.gui.leftbar.LeftbarJLabel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * Window for configuration
 *
 * @author cyprien
 */
public class ParametersWindow extends JDialog {
	private static final long serialVersionUID = -2776375961877258278L;

	/**
	 * Label to show if there is an error
	 */
	JLabel error;

	/**
	 * Text field for library path
	 */
	JTextField path;

	/**
	 * Create the window
	 */
	public ParametersWindow() {
		super(MainWindow.getMainWindow(), true);
		this.setTitle("Parameters");

		// Main Panel
		Container contentPane = getContentPane();
		JPanel form = new JPanel();
		form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
		form.setBackground(GUIParameters.LEFTBAR_BACKGROUND);
		form.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.add(form);

		// Error label
		error = new LeftbarJLabel("");
		error.setForeground(GUIParameters.LEFTBAR_ACTIVE);
		form.add(error);

		// Music path panel
		JPanel musicPath = new JPanel();
		musicPath.setLayout(new GridLayout(1, 2));
		musicPath.setBackground(GUIParameters.LEFTBAR_BACKGROUND);
		musicPath.setBorder(BorderFactory.createTitledBorder(null,
				"Music Path", TitledBorder.LEFT, TitledBorder.TOP,
				GUIParameters.getLeftFont(), Color.white));
		{
			// Label for the path
			JLabel pathLabel = new LeftbarJLabel("Music path: ");
			pathLabel.setBorder(new EmptyBorder(10, 10, 10, 0));
			musicPath.add(pathLabel);

			// Text field
			path = new JTextField(ConfigFileParser.getConfigFileParser()
					.getParams("library"));
			path.setBorder(BorderFactory.createMatteBorder(10, 0, 10, 10,
					musicPath.getBackground()));
			musicPath.add(path);
		}
		form.add(musicPath);

		// Lastfm panel
		JPanel lastfm = new JPanel();
		lastfm.setBackground(GUIParameters.LEFTBAR_BACKGROUND);
		lastfm.setLayout(new GridLayout(1, 2));
		lastfm.setBorder(BorderFactory.createTitledBorder(null, "Lastfm",
				TitledBorder.LEFT, TitledBorder.TOP, GUIParameters.getLeftFont(),
				Color.white));
		{
			// Button for lastfm
			final JButton lastfmButton = new JButton();
			Border emptyBorder = BorderFactory.createEmptyBorder();
			lastfmButton.setBorder(emptyBorder);
			lastfmButton.setBackground(lastfm.getBackground());
			try {
				BufferedImage img = ImageIO.read(new File("assets/img/lastfm_button.png"));
				lastfmButton.setIcon(new ImageIcon(img));
			} catch (IOException e) {
				Log.addEntry(e);
			}

			// Button text
			if (ConfigFileParser.getConfigFileParser().getParams("lastfm_session") != null) {
				lastfmButton.setText("Logged in");
			} else {
				lastfmButton.setText("Authenticate");
			}

			lastfmButton.setHorizontalTextPosition(SwingConstants.CENTER);
			lastfmButton.setFont(GUIParameters.getLeftFont());
			lastfmButton.setForeground(Color.WHITE);

			// Onclick
			final JDialog window = this;
			lastfmButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						// Get lastfm url
						final String token = Scrobbler.getoken();
						URI uri = new URI("http://www.last.fm/api/auth/?api_key=" + ScrobblerConfig.KEY +
								"&token=" + token);

						// When come back to the window
						window.addWindowFocusListener(new WindowAdapter() {
							@Override
							public void windowGainedFocus(WindowEvent e) {
								super.windowGainedFocus(e);

								try {
									String lastfm_session = Scrobbler.getSession(token); // Get key session

									// Show that we are logged in
									lastfmButton.setText("Logged in");

									// Save lastfm key
									ConfigFileParser.getConfigFileParser().setParam("lastfm_session", lastfm_session);
									try {
										ConfigFileWriter.writeConfigFile();
									} catch (Exception e1) {
										Log.addEntry(e1);
									}
								} catch (ScrobblerException e1) {
									// Unable to connect
									lastfmButton.setText("Error");
								}

								window.removeWindowFocusListener(this); // Disable listener
							}
						});

						// Go to the url
						Desktop.getDesktop().browse(uri);
					} catch (Exception e1) {
						Log.addEntry(e1);
					}
				}
			});
			lastfmButton.setBorder(new EmptyBorder(10, 10, 10, 0));
			lastfm.add(lastfmButton);
		}
		form.add(lastfm);

		// Button to apply
		JButton apply = new JButton("");
		apply.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Set icon
		try {
			BufferedImage img = ImageIO.read(new File("assets/img/apply.png"));
			apply.setIcon(new ImageIcon(img));
		} catch (IOException e) {
			Log.addEntry(e);
		}

		// Delete border
		Border emptyBorder = BorderFactory.createEmptyBorder();
		apply.setBorder(emptyBorder);
		apply.setBackground(form.getBackground());

		// Onclick
		final JDialog window = this;
		apply.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Test field
				error.setText("");
				File f = new File(path.getText());
				if (!f.exists() || !f.isDirectory()) {
					// Invalid folder
					error.setText("Invalid folder name");
					window.pack();
				} else {
					// Get configuration
					ConfigFileParser cfp = ConfigFileParser.getConfigFileParser();

					// Load library if path has changed
					if (!path.getText().equals(cfp.getParams("library"))) {
						try {
							MusicLibrary.getMusicLibrary().loadLibraryFolder(path.getText());
							MusicLibraryView.getMusiLibraryView().showLibrary("");
						} catch (InvalidPathException e1) {
							Log.addEntry(e1);
						}
					}

					// Save config
					cfp.setParam("library", path.getText());

					// Write it
					try {
						ConfigFileWriter.writeConfigFile();
					} catch (Exception e1) {
						e1.printStackTrace();
					}

					// Close window
					window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
				}
			}
		});
		form.add(apply);

		// Set default button
		JRootPane rootPane = SwingUtilities.getRootPane(apply);
		rootPane.setDefaultButton(apply);

		setResizable(false);
		pack();
		setLocationRelativeTo(MainWindow.getMainWindow());
		setVisible(true);
	}
}
