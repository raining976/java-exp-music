package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import model.MusicSheet;
import musicclient.MusicOperationClient;

public class SharedMusicSheetBlock extends JPanel {

	private static final long serialVersionUID = 1L;

	private Object[][] shareMusicData = { { "101", "music sheet 01" }, { "102", "music sheet 02" },
			{ "103", "music sheet 03" } };
	private String[] shareMusicColumnNames = { "分享者", "歌 单" };

	private List<MusicSheet> musicSheetData;

	public SharedMusicSheetBlock(MusicPlayerGUI app) {
		// 获取 共享歌单
		MusicOperationClient musicOperation = new MusicOperationClient();
		musicSheetData = musicOperation.queryAllMusicSheets();
		int i = 0;
		Object[][] objData = new Object[musicSheetData.size()][2];
		for (MusicSheet ms : musicSheetData) {
			objData[i][0] = ms.getCreator();
			objData[i][1] = ms.getName();
			++i;
		}
		this.shareMusicData = objData;

		this.setPreferredSize(new Dimension(250, 400));
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(layout);

		this.setBackground(Color.ORANGE);
		JLabel sharedMusicSheetLabel = new JLabel("共享歌单");

		MyTable sharedMusicSheetTable = new MyTable(shareMusicData, shareMusicColumnNames);

		sharedMusicSheetTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		// 监听鼠标点击 歌单列表中的歌单
		sharedMusicSheetTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int count = sharedMusicSheetTable.getSelectedRow();
				MusicSheet musicSheet = musicSheetData.get(count);
				app.refreshDisplaySheet(musicSheet);

			}

		});

		JScrollPane sharedMusicSheetTablePanel = new JScrollPane(sharedMusicSheetTable);
		this.add(Box.createVerticalStrut(5));
		this.add(sharedMusicSheetLabel);
		this.add(Box.createVerticalStrut(5));
		this.add(sharedMusicSheetTablePanel);

	}

}
