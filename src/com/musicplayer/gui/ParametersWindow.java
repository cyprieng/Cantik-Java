package com.musicplayer.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.musicplayer.core.Log;
import com.musicplayer.core.config.ConfigFileParser;
import com.musicplayer.core.config.ConfigFileWriter;

/**
 * Window for configuration
 * 
 * @author cyprien
 * 
 */
public class ParametersWindow extends JFrame {
	private static final long serialVersionUID = -2776375961877258278L;

	/**
	 * Text field for library path and lastfm user
	 */
	JTextField path, lastfm_user;

	/**
	 * Feld for lastfm password
	 */
	JPasswordField lastfm_pass;

	/**
	 * Create the window
	 */
	public ParametersWindow() {
		super("Parameters");

		// Main Panel
		Container contentPane = getContentPane();
		JPanel form = new JPanel();
		form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
		form.setBackground(GUIParameters.LEFTBAR_BACKGROUND);
		form.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.add(form);

		// Music path panel
		JPanel musicPath = new JPanel();
		musicPath.setLayout(new GridLayout(1, 2));
		musicPath.setBackground(GUIParameters.LEFTBAR_BACKGROUND);
		musicPath.setBorder(BorderFactory.createTitledBorder(null,
				"Music Path", TitledBorder.LEFT, TitledBorder.TOP,
				GUIParameters.getFont(), Color.white));
		{
			// Label for the path
			JLabel pathLabel = new CustomJLabel("Music path: ");
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
		lastfm.setLayout(new GridLayout(2, 2));
		lastfm.setBorder(BorderFactory.createTitledBorder(null, "Lastfm",
				TitledBorder.LEFT, TitledBorder.TOP, GUIParameters.getFont(),
				Color.white));
		{
			// Label for username
			JLabel userLabel = new CustomJLabel("Lastfm username: ");
			userLabel.setBorder(new EmptyBorder(10, 10, 10, 0));
			lastfm.add(userLabel);

			// Text field
			lastfm_user = new JTextField(ConfigFileParser.getConfigFileParser()
					.getParams("lastfm_username"));
			lastfm_user.setBorder(BorderFactory.createMatteBorder(10, 0, 10,
					10, lastfm.getBackground()));
			lastfm.add(lastfm_user);
		}
		{
			// Label for password
			JLabel passLabel = new CustomJLabel("Lastfm password: ");
			passLabel.setBorder(new EmptyBorder(10, 10, 10, 0));
			lastfm.add(passLabel);

			// Field for pass
			lastfm_pass = new JPasswordField(ConfigFileParser
					.getConfigFileParser().getParams("lastfm_password"));
			lastfm_pass.setBorder(BorderFactory.createMatteBorder(10, 0, 10,
					10, lastfm.getBackground()));
			lastfm.add(lastfm_pass);
		}
		form.add(lastfm);

		// Button to apply
		JButton apply = new JButton("");
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
		apply.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Get new configuration
				ConfigFileParser cfp = ConfigFileParser.getConfigFileParser();
				cfp.setParam("library", path.getText());
				cfp.setParam("lastfm_username", lastfm_user.getText());
				cfp.setParam("lastfm_password",
						String.valueOf(lastfm_pass.getPassword()));

				// Write it
				try {
					ConfigFileWriter.writeConfigFile();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		form.add(apply);

		setResizable(false);
		pack();
		setLocationRelativeTo(MainWindow.getMainWindow());
		setVisible(true);
	}
}
