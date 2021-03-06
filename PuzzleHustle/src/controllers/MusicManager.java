package controllers;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class MusicManager {
	private static MediaPlayer musicPlayer;
	private static MediaPlayer soundPlayer;
	private static boolean isMuted = false;

	public static void startMusic() {
		String musicFile = "bensound-straight.mp3";
		Media sound = new Media(new File(musicFile).toURI().toString());
		musicPlayer = new MediaPlayer(sound);
		musicPlayer.setOnEndOfMedia(new Runnable() {
		       public void run() {
		         musicPlayer.seek(Duration.ZERO);
		       }
		   });
		musicPlayer.play();
		musicPlayer.setVolume(.05);
	}

	public static void muteMusic() {
		if (isMuted) {
			musicPlayer.setMute(false);
			soundPlayer.setMute(false);
		} else {
			musicPlayer.setMute(true);
			soundPlayer.setMute(true);
		}
		isMuted = !isMuted;
	}

	public static void playSquish() {
		String musicFile = "Bir Poop Splat-SoundBible.com-157212383.mp3";
		Media sound = new Media(new File(musicFile).toURI().toString());
		soundPlayer = new MediaPlayer(sound);
		if (!isMuted) {
			soundPlayer.play();
		}
	}
}
