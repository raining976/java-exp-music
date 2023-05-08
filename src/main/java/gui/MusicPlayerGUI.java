package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.MusicSheet;
import musicclient.MusicOperationClient;
import soundmaker.MP3Player;

public class MusicPlayerGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private MusicSheetDisplayBlock displaySheet;
	private LocalMusicSheetBlock localSheet;
	private MusicPlayerBlock musicPlayer;
	private MP3Player mp3Player;
	boolean isLocal = false;

	public MusicPlayerGUI(String title) {

		this.setTitle(title);
		this.setSize(900, 600);
		this.setLocation(100, 100);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container container = this.getContentPane();
		container.setLayout(new BorderLayout(4, 4));
		container.setBackground(Color.WHITE);

		/* WEST ************************************/
		JPanel westPanel = new JPanel();
		westPanel.setPreferredSize(new Dimension(250, 600));
		BoxLayout westPanelLayout = new BoxLayout(westPanel, BoxLayout.Y_AXIS);
		westPanel.setLayout(westPanelLayout);
		container.add("West", westPanel);

		westPanel.add(new SharedMusicSheetBlock(this));
		localSheet = new LocalMusicSheetBlock(this);
		westPanel.add(localSheet);
		westPanel.add(new MusicSheetManagementBlock(this));

		/* CENTER ************************************/
		JPanel centerPanel = new JPanel();
		BoxLayout centerPanelLayout = new BoxLayout(centerPanel, BoxLayout.Y_AXIS);
		centerPanel.setLayout(centerPanelLayout);
		container.add("Center", centerPanel);

		MusicSheet firstSheet = new MusicOperationClient().queryAllMusicSheets().get(0);
		// 添加歌单展示区域
		displaySheet = new MusicSheetDisplayBlock(firstSheet, this);
		centerPanel.add(displaySheet);

		// 歌曲列表部分
		musicPlayer = new MusicPlayerBlock(firstSheet);
		centerPanel.add(musicPlayer);
		// 更改窗口图标
		ImageIcon icon = null;
		java.net.URL imgURL = getClass().getResource("/images/music.png");
		if (imgURL != null) {
			icon = new ImageIcon(imgURL);
			this.setIconImage(icon.getImage());
		} else {
			JOptionPane.showMessageDialog(this, "icon image not found !");
		}
	}

	public LocalMusicSheetBlock getLocalSheet() {
		return localSheet;
	}

	// 刷新歌单展示区
	public void refreshDisplaySheet(MusicSheet musicSheet) {
		displaySheet.removeAll();
		displaySheet.add(new MusicSheetDisplayBlock(musicSheet, this));
		displaySheet.revalidate();

		musicPlayer.removeAll();
		musicPlayer.add(new MusicPlayerBlock(musicSheet));
		musicPlayer.revalidate();
	}

	public void refreshLocalSheet() {
		localSheet.removeAll();
		LocalMusicSheetBlock localSheetTmp = new LocalMusicSheetBlock(this);
		localSheet.add(localSheetTmp);
		localSheet.revalidate();
		localSheet = localSheetTmp;
	}
}
