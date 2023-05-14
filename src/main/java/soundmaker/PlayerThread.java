package soundmaker;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import javazoom.jl.player.Player;

public class PlayerThread extends Thread {
	private Player player;
	private Clip clip;
	private boolean isPlaying = false;
	private boolean isPaused = false;

	public PlayerThread(String filename) {
		try {
			filename = "D:\\CloudMusic\\薛之谦 - 丑八怪.mp3";
			File file = new File(filename);
//			FileInputStream fis = new FileInputStream(file);
//			BufferedInputStream stream = new BufferedInputStream(fis);
//			player = new Player(stream);
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
			clip = AudioSystem.getClip();
			clip.open(audioIn);
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void run() {
		try {
			while (true) {
				System.out.println("isPaused: " + isPaused);
				if (isPlaying) {
					if (!isPaused) {
						clip.setFramePosition(0);
//						player.play();
						clip.start();
					} else {
						clip.stop();
//						player.close();
					}
				}

				Thread.sleep(100);

			}
		} catch (InterruptedException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	public void play() {
		isPlaying = true;
		System.out.println("isplaying: " + isPlaying);
	}

	public void pause() {
		isPaused = true;
		System.out.println("isPaused: " + isPaused);
	}

	public void resumePlaying() {
		isPaused = false;
		System.out.println("isPaused: " + isPaused);
	}
}
