package soundmaker;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import gui.MusicPlayerGUI;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class MP3Player {
	private String filePath;
	private final Object lock = new Object();
	private boolean isPaused;
	private Thread playerThread;
	public String[] playList; // 播放列表
	public int curIndex;
	Player player;
	boolean isRemote = false;

	public MP3Player(String[] playList, int curIndex, MusicPlayerGUI app) {
		isRemote = app.isRemote;
		this.playList = playList;
		this.curIndex = curIndex;
		this.filePath = playList[curIndex];

		isPaused = false;
	}

	public void play() {
		synchronized (lock) {
			if (playerThread == null || !playerThread.isAlive()) {
				playerThread = new Thread(new PlayerRunnable());
				playerThread.start();
			} else {
				lock.notifyAll();
			}
			isPaused = false;
		}
	}

	public void pause() {
		synchronized (lock) {
			isPaused = true;
		}
	}

	private class PlayerRunnable implements Runnable {

		@Override
		public void run() {
			try {
				BufferedInputStream buffer;
				if (isRemote) {
					URL mp3url = new URL(filePath);
					buffer = new BufferedInputStream(mp3url.openStream());
				} else {
					buffer = new BufferedInputStream(new FileInputStream(filePath));
				}

				player = new Player(buffer);

				while (!Thread.currentThread().isInterrupted()) {
					synchronized (lock) {
						while (isPaused) {
							lock.wait();
						}
					}

					if (player.play(1) == false) {
						break;
					}

				}

			} catch (IOException | JavaLayerException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
