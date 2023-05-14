package player;

import java.io.File;

public class TestPlayer {

	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		Player player = XMusic.player();
		File file = new File("D:\\CloudMusic\\薛之谦 - 丑八怪.mp3");
		player.load(file);
//		System.out.println(player.info());
		try {
			player.start();
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

		System.out.println(123);
	}

}
