package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import dao.MusicDao;
import dao.SheetDao;
import db_model.Music;
import db_model.Sheet;
import model.MusicSheet;

public class LocalMusicSheetBlock extends JPanel {

	private static final long serialVersionUID = 1L;

	Object[][] localMusicData = { { "music sheet 04" }, { "music sheet 05" }, { "music sheet 06" },
			{ "music sheet 04" }, { "music sheet 04" }, { "music sheet 04" }, { "music sheet 04" },
			{ "music sheet 04" }, { "music sheet 04" }, { "music sheet 04" } };
	String[] localMusicColumnNames = { "歌 单" };
	List<Sheet> sheets = new ArrayList<Sheet>();
	Sheet curSheet = null;
	Sheet preSheet = null;

	public LocalMusicSheetBlock(MusicPlayerGUI app) {
		this.setPreferredSize(new Dimension(250, 400));
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(layout);

		this.setBackground(Color.LIGHT_GRAY);
		JLabel localMusicSheetLabel = new JLabel("本地歌单");
		GetLocalSheet();
		JTable localMusicSheetTable = new MyTable(localMusicData, localMusicColumnNames);
		localMusicSheetTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int count = localMusicSheetTable.getSelectedRow();
				curSheet = sheets.get(count);
				preSheet = count == 0 ? null : sheets.get(count - 1);
				MusicSheet sheet = createMusicSheet(curSheet);
				app.isLocal = true;
				app.refreshDisplaySheet(sheet);
				System.out.println(curSheet.getId());
			}

		});
		localMusicSheetTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		JScrollPane localMusicSheetTablePanel = new JScrollPane(localMusicSheetTable);
		this.add(Box.createVerticalStrut(5));
		this.add(localMusicSheetLabel);
		this.add(Box.createVerticalStrut(5));
		this.add(localMusicSheetTablePanel);
	}

	public Sheet getCurSheet() {
		return curSheet;
	}

	void GetLocalSheet() {
		SheetDao sheetDao = new SheetDao();
		sheets = sheetDao.findAll();
		Object[][] sheetData = new Object[sheets.size()][1];
		int i = 0;
		for (Sheet s : sheets) {
			sheetData[i][0] = s.getName();
			i++;
		}
		this.localMusicData = sheetData;
	}

	// 通过 sheet 创建 musicSheet
	MusicSheet createMusicSheet(Sheet s) {
		MusicSheet sheet = new MusicSheet();
		MusicDao musicDao = new MusicDao();
		sheet.setName(s.getName());
		sheet.setCreator(s.getCreator());
		sheet.setPicture(s.getPicPath());
		sheet.setDateCreated(s.getDateCreated());

		List<Music> ms = new ArrayList<Music>();
		ms = musicDao.findBySheetId(s.getId());
		Map<String, String> mum = new HashMap<String, String>();
		for (Music m : ms) {
			mum.put(m.getMd5(), m.getName());
		}
		sheet.setMusicItems(mum);

		return sheet;
	}
}
