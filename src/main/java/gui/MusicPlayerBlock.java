package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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

	private int curIndex; // 当前播放歌曲的索引
	private String curId; // 当前播放歌曲的id

	public MusicPlayerBlock(MusicSheet ms) {
		this.setPreferredSize(new Dimension(550, 300));
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(layout);

		// 设置歌曲列表数据
		Object[][] Data = new Object[ms.getMusicItems().size()][3];
		int i = 0;
		for (String value : ms.getMusicItems().values()) {
			Data[i][0] = value;
			Data[i][1] = Data[i][2] = "";
			i++;
		}
		this.musicData = Data;

		MyTable musicTable = new MyTable(musicData, musicColumnNames);
		// 设置双击播放歌曲
		Object[] keys = ms.getMusicItems().keySet().toArray();
		musicTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					curIndex = musicTable.getSelectedRow();
					curId = keys[curIndex].toString();
					System.out.println("第" + curIndex + "行" + " id: " + curId);
					// TODO 双击播放音乐

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
		JButton playMusicButton = new JButton("播放");
		JButton nextMusicButtonButton = new JButton("下一首");

		musicPlayerPanel.add(previousMusicButton);
		musicPlayerPanel.add(playMusicButton);
		musicPlayerPanel.add(nextMusicButtonButton);

		this.add(musicPlayerPanel);
	}
}
