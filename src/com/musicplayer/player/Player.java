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

	/**
	 * Store the PlayerLaunchThread
	 * @see PlayerLaunchThread
	 */
	private static PlayerLaunchThread plc;

	@Override
	public void start(Stage primaryStage) {
	}

	/**
	 * Start launch function
	 * 
	 * @see javafx
	 */
	public void launcher() {
		launch();
	}

	/**
	 * Init MediaPlayer it if necessary
	 * 
	 */
	public static void initMediaPlayer() {
		if (mediaPlayer == null) {
			if (plc == null) {
				// Launch thread to launch "launcher" method
				plc = new PlayerLaunchThread();
				plc.start();
			}

			try {
				// Load file
				String fileToLoad = "file:///" + Player.file;
				Media m = new Media(fileToLoad);
				Player.mediaPlayer = new MediaPlayer(m);
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Set the file value, and launch initialization of MediaPlayer
	 * 
	 * @param file
	 *            path to the song to play
	 */
	public static void setFile(String file) {
		Player.file = file.replace("\\", "/");

		if (Player.mediaPlayer != null) { //Delete actual mediaPlayer
			Player.mediaPlayer.stop();
			Player.mediaPlayer = null;
		}

		Player.initMediaPlayer();
	}

	/**
	 * Start playing the player
	 */
	public static void play() {
		Player.mediaPlayer.play();
	}

	/**
	 * Pause the player
	 */
	public static void pause() {
		Player.mediaPlayer.pause();
	}
}
