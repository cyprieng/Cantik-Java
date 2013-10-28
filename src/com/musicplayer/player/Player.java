package com.musicplayer.player;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


public class Player extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			String bip = "file:///test.mp3";
			Media hit = new Media(bip);
			MediaPlayer mediaPlayer = new MediaPlayer(hit);
			mediaPlayer.play();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void play() {
		launch();
	}
}
