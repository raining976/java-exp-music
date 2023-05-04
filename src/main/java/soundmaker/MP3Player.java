package soundmaker;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import javazoom.jl.player.Player;

public class MP3Player extends Thread {
	private String filename;
	private Player player;

	public MP3Player(String filename) {
		this.filename = filename;
	}

	@Override
	public void run() {
		try {
			/*
			 * 播放来自与网络的流
			 */
			// URL mp3url = new URL(filename);
//			 BufferedInputStream buffer = new BufferedInputStream(mp3url.openStream());
			/*
			 * 播放本地硬盘上的文件
			 */
			BufferedInputStream buffer = new BufferedInputStream(new FileInputStream(filename));

			player = new Player(buffer);
			player.play();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void main(String[] args) {
		String filename = System.getProperty("user.dir") + "/sound/1234.mp3"; // 本地
		// filename =
		// "http://localhost:8080/music.server/music?md5=4768976e9d101954cb65466d16e17f1d";
		// // 远程
		MP3Player mp3 = new MP3Player(filename);

		mp3.start();
	}
}
