package controllers;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicManager {
	private static MediaPlayer musicPlayer;
	private static MediaPlayer soundPlayer;
	private static MediaPlayer slidePlayer;
	private static MediaPlayer spawnPlayer;
	private static boolean isMuted = false;

	public static void startMusic() {
		String musicFile = "bensound-straight.mp3";
		Media sound = new Media(new File(musicFile).toURI().toString());
		musicPlayer = new MediaPlayer(sound);
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
	public static void playSlide() {
		String musicFile = "slide.wav";
		Media sound = new Media(new File(musicFile).toURI().toString());
		slidePlayer = new MediaPlayer(sound);
		if (!isMuted) {
			slidePlayer.play();
		}
	}
	public static void playSpawn() {
		String musicFile = "spawn.wav";
		Media sound = new Media(new File(musicFile).toURI().toString());
		spawnPlayer = new MediaPlayer(sound);
		if (!isMuted) {
			spawnPlayer.play();
		}
	}
}
