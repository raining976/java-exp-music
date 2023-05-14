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
import soundmaker.PlayerThread;

public class MusicPlayerGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private MusicSheetDisplayBlock displaySheet;
	private LocalMusicSheetBlock localSheet;
	private MusicPlayerBlock musicPlayer;

	boolean isLocal = false;
	MusicSheet curPlaySheet;
	int curPlayIndex = -1;
	Player player = null;
	boolean isPlaying = false;
	PlayerThread playerThread = null;
	MP3Player mp3Player = null;
	public boolean isRemote = false;

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
		displaySheet.removeAll();
		displaySheet.add(new MusicSheetDisplayBlock(musicSheet, this));
		displaySheet.revalidate();

		musicPlayer.removeAll();
		MusicPlayerBlock musicPlayerTmp = new MusicPlayerBlock(musicSheet, this);
		musicPlayer.add(musicPlayerTmp);
		musicPlayer.revalidate();
		musicPlayer = musicPlayerTmp;
	}

	public void refreshLocalSheet() {
		localSheet.removeAll();
		LocalMusicSheetBlock localSheetTmp = new LocalMusicSheetBlock(this);
		localSheet.add(localSheetTmp);
		localSheet.revalidate();
		localSheet = localSheetTmp;
	}

	public void playMusic() {
		isRemote = false;
		musicPlayer.setTableSelectedRow(curPlayIndex);
		Object[] keys = this.curPlaySheet.getMusicItems().keySet().toArray();
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
