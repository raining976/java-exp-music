package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import dao.MusicDao;
import db_model.Music;
import javazoom.jl.player.Player;
import model.MusicSheet;
import musicclient.MusicOperationClient;
import soundmaker.MP3Player;

public class MusicPlayerGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private MusicSheetDisplayBlock displaySheet;
	private LocalMusicSheetBlock localSheet;
	private MusicPlayerBlock musicPlayer;

	boolean isLocal = false; // 当前选择的歌单是否为本地歌单
	MusicSheet curPlaySheet; // 当前播放的歌单
	int curPlayIndex = -1; // 当前播放的歌曲位于当前播放的歌单中的索引
	Player player = null;
	boolean isPlaying = false;
	MP3Player mp3Player = null; // 播放类的实例
	public boolean isRemote = false; // 是否为在线播放歌曲

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
		musicPlayer = new MusicPlayerBlock(firstSheet, this);
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
		// 歌单info刷新
		displaySheet.removeAll();
		displaySheet.add(new MusicSheetDisplayBlock(musicSheet, this));
		displaySheet.revalidate();

		// 歌曲列表刷新
		musicPlayer.removeAll();
		// 要先新建一个tmp对象以更新原有的对象,否则原有对象不会更新
		MusicPlayerBlock musicPlayerTmp = new MusicPlayerBlock(musicSheet, this);
		musicPlayer.add(musicPlayerTmp);
		musicPlayer.revalidate();
		musicPlayer = musicPlayerTmp;
	}

	// 刷新本地歌单的区域
	public void refreshLocalSheet() {
		localSheet.removeAll();
		LocalMusicSheetBlock localSheetTmp = new LocalMusicSheetBlock(this);
		localSheet.add(localSheetTmp);
		localSheet.revalidate();
		localSheet = localSheetTmp;
	}

	// 播放音乐 (从头开始播放)
	public void playMusic() {
		isRemote = false;
		musicPlayer.setTableSelectedRow(curPlayIndex);
		Object[] keys = this.curPlaySheet.getMusicItems().keySet().toArray(); // 获取所有的 md5
		if (this.isLocal) {
			String[] playlist = new String[keys.length];
			MusicDao mDao = new MusicDao();
			for (int i = 0; i < keys.length; ++i) {
				String md5 = keys[i].toString();
				Music m = mDao.findByMd5(md5);
				String path = m.getFilePath();
				playlist[i] = path;
			}
			if (mp3Player != null)
				mp3Player.pause();
			mp3Player = new MP3Player(playlist, curPlayIndex, this);
			musicPlayer.setPauseText();
			mp3Player.play();

		} else {
			String curMD5 = keys[this.curPlayIndex].toString();
			String basicPath = System.getProperty("user.dir") + "\\songs\\";
			String filepath = basicPath + this.curPlaySheet.getMusicItems().get(curMD5);
			File file = new File(filepath);
			// 如果已经下载 播放本地
			if (file.exists()) {
				String[] playlist = new String[keys.length];
				for (int i = 0; i < keys.length; ++i) {
					String md5 = keys[i].toString();
					String path = basicPath + this.curPlaySheet.getMusicItems().get(md5);
					playlist[i] = path;
				}
				if (mp3Player != null)
					mp3Player.pause();
				mp3Player = new MP3Player(playlist, curPlayIndex, this);

				musicPlayer.setPauseText();
				mp3Player.play();
			} else {
				// 如果没有下载 播放 线上
				isRemote = true;
				String[] playlist = new String[keys.length];
				String remoteBasicPath = "http://119.167.221.16:38080/music.server/music?md5=";
				for (int i = 0; i < keys.length; ++i) {
					String md5 = keys[i].toString();
					String path = remoteBasicPath + md5;
					playlist[i] = path;
				}
				if (mp3Player != null)
					mp3Player.pause();
				mp3Player = new MP3Player(playlist, curPlayIndex, this);

				musicPlayer.setPauseText();
				mp3Player.play();
			}
		}
	}

	// 播放当前歌单当前播放的下一首
	public void playNext() {
		MP3Player MP3PlayerTmp = this.mp3Player;
		if (mp3Player != null)
			mp3Player.pause();
		String[] list = MP3PlayerTmp.playList;
		int curIndex = MP3PlayerTmp.curIndex;
		curPlayIndex = curIndex + 1 == list.length ? 0 : curIndex + 1;
		this.mp3Player = new MP3Player(list, curPlayIndex, this);
		musicPlayer.setPauseText();
		musicPlayer.setTableSelectedRow(curPlayIndex);
		mp3Player.play();
	}

	// 播放当前歌单当前播放的的上一首
	public void playPre() {
		MP3Player MP3PlayerTmp = this.mp3Player;
		if (mp3Player != null) {
			mp3Player.pause();
		}
		String[] list = MP3PlayerTmp.playList;
		int curIndex = MP3PlayerTmp.curIndex;
		curPlayIndex = curIndex == 0 ? list.length - 1 : curIndex - 1;
		this.mp3Player = new MP3Player(list, curPlayIndex, this);
		musicPlayer.setPauseText();
		musicPlayer.setTableSelectedRow(curPlayIndex);
		mp3Player.play();
	}

}
