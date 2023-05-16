package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;

import model.MusicSheet;

public class MusicPlayerBlock extends JPanel {

	private static final long serialVersionUID = 1L;

	private Object[][] musicData = { { "Yesterday.mp3", "Guns and Roses", "10 min", "", "" },
			{ "Night train.mp3", "Guns and Roses", "10 min", "", "" },
			{ "November rain.mp3", "Guns and Roses", "10 min", "", "" } };
	private String[] musicColumnNames = { "曲名", "歌手", "时长" };

	int curIndex; // 当前播放歌曲的索引
	private String workPath;
	private String songPath = "\\songs\\";
	private boolean isDownloaded = false; // 歌曲是否下载
	private JButton playMusicButton;
	private MyTable musicTable;

	public MusicPlayerBlock(MusicSheet ms, MusicPlayerGUI app) {
		workPath = System.getProperty("user.dir");
		this.setPreferredSize(new Dimension(550, 300));
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(layout);

		// 设置歌曲列表数据
		Object[][] Data = new Object[ms.getMusicItems().size()][3];
		int i = 0;
		// 检查当前歌单中的歌曲是否已经下载
		String fileName1 = null;
		for (Entry<String, String> entry : ms.getMusicItems().entrySet()) {
			fileName1 = entry.getValue();
			if (fileName1 != null) {
				break;
			}
		}
		if (new File(workPath + songPath + fileName1).exists()) {
			isDownloaded = true; // 设置当前歌单已经下载的标识
		}

		for (String value : ms.getMusicItems().values()) {
//			if (isDownloaded) {
//				System.out.println("歌曲已经下载");
//				String path = workPath + songPath + value;
//				List song = MP3Util.getMp3Info(path);
//
//				Data[i][0] = song.get(0);
//				Data[i][1] = song.get(1);
//				Data[i][2] = song.get(2);
//			} 
			Data[i][0] = value;
			Data[i][1] = Data[i][2] = "";
			i++;
		}
		this.musicData = Data;

		musicTable = new MyTable(musicData, musicColumnNames);
		// 设置双击播放歌曲

		musicTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					curIndex = musicTable.getSelectedRow();
					// 更新当前播放的歌曲和歌单信息
					app.curPlaySheet = ms;
					app.curPlayIndex = curIndex;
					app.playMusic();
				}

			}

		});

		musicTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		JScrollPane musicTablePanel = new JScrollPane(musicTable);

		this.add(musicTablePanel);

		JSlider slider = new JSlider();
		this.add(slider, "普通滑动条");

		JPanel musicPlayerPanel = new JPanel();
		musicPlayerPanel.setLayout(new FlowLayout());
		musicPlayerPanel.setBackground(Color.GRAY);
		JButton previousMusicButton = new JButton("上一首");
		previousMusicButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (app.curPlayIndex != -1)
					app.playPre();
			}

		});
		playMusicButton = new JButton("播放");
		// 给播放按钮绑定点击事件
		playMusicButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (app.curPlayIndex != -1) {
					String text = playMusicButton.getText();
					// 根据当前的button文字进行播放或者暂停的操作
					if (text == "播放") {
						app.mp3Player.play();
					} else {
						app.mp3Player.pause();
					}
					playMusicButton.setText(text == "播放" ? "暂停" : "播放");
				}

			}

		});
		JButton nextMusicButtonButton = new JButton("下一首");
		// 给下一首按钮添加鼠标点击事件
		nextMusicButtonButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (app.curPlayIndex != -1)
					app.playNext();
			}

		});
		musicPlayerPanel.add(previousMusicButton);
		musicPlayerPanel.add(playMusicButton);
		musicPlayerPanel.add(nextMusicButtonButton);

		this.add(musicPlayerPanel);
	}

	// 设置将播放的button文字设置为暂停
	public void setPauseText() {
		playMusicButton.setText("暂停");
	}

	// 设置 table 的选择焦点
	public void setTableSelectedRow(int index) {
		musicTable.clearSelection(); // 先清空原有的
		musicTable.setRowSelectionInterval(index, index);

	}
}
