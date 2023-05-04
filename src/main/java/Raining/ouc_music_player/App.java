package Raining.ouc_music_player;

import gui.MusicPlayerGUI;

public class App {
	public static void main(String[] args) {
		String title = new String("raining");
		MusicPlayerGUI gui = new MusicPlayerGUI(title);

		gui.setVisible(true);
	}
}
