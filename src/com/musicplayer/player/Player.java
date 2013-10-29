package com.musicplayer.player;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Class which manage the playing of music files
 * 
 * @author cyprien
 * 
 */
public class Player extends Application {
	/**
	 * Path of the file to read
	 */
	private static String file;

	/**
	 * Store the MediaPlayer
	 */
	private static MediaPlayer mediaPlayer;

	@Override
	public void start(Stage primaryStage) {
	}

	/**
	 * Get MediaPlayer and init it if necesssry
	 * 
	 * @return MediaPlayer initialized with file
	 */
	public static MediaPlayer getMediaPlayer() {
		if (mediaPlayer == null) {
			// Launch thread to launch "launcher" method
			PlayerLaunchThread pc = new PlayerLaunchThread();
			pc.start();

			try {
				// Load file
				String fileToLoad = "file:///" + Player.file;
				Media m = new Media(fileToLoad);
				Player.mediaPlayer = new MediaPlayer(m);
			} catch (Exception e) {
			}
		}
		return mediaPlayer;
	}

	/**
	 * Set the file value
	 * 
	 * @param file
	 *            path to the song to play
	 */
	public static void setFile(String file) {
		Player.file = file.replace("\\", "/");
	}

	/**
	 * Start launch function
	 * 
	 * @see javafx
	 */
	public void launcher() {
		launch();
	}
}
