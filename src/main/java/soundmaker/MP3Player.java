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
	public int curIndex; // 当前播放的歌曲在播放列表中索引
	Player player;
	boolean isRemote = false; // 标记是否为在线播放

	public MP3Player(String[] playList, int curIndex, MusicPlayerGUI app) {
		isRemote = app.isRemote; // 通过app中的预先设置好的参数判断
		this.playList = playList;
		this.curIndex = curIndex;
		this.filePath = playList[curIndex];
		isPaused = false;
	}

	public void play() {
		synchronized (lock) {
			if (playerThread == null || !playerThread.isAlive()) {
				playerThread = new Thread(new PlayerRunnable()); // 开一个子线程给playerThread维护
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
				// 判断是否为远程在线播放
				if (isRemote) {
					URL mp3url = new URL(filePath);
					buffer = new BufferedInputStream(mp3url.openStream());
				} else {
					buffer = new BufferedInputStream(new FileInputStream(filePath));
				}

				player = new Player(buffer);

				// 线程不中断就不会跳出循环
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
