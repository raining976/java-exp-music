package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;

import dao.MusicDao;
import dao.SheetDao;
import db_model.Music;
import db_model.Sheet;
import model.MusicSheet;

public class MusicSheetManagementBlock extends JPanel {

	private static final long serialVersionUID = 1L;

	public MusicSheetManagementBlock(MusicPlayerGUI app) {
		this.setPreferredSize(new Dimension(250, 50));
		this.setLayout(new FlowLayout(FlowLayout.LEFT));

		JButton createMusicSheetButton = new JButton("创建新歌单");
		// 为创建歌单绑定事件
		createMusicSheetButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				AddSheetGui addSheet = new AddSheetGui(app);
				addSheet.setVisible(true);
			}
		});
		JButton deleteMusicSheetButton = new JButton("删除歌单");
		deleteMusicSheetButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Sheet sheet = app.getLocalSheet().curSheet;
				Sheet preSheet = app.getLocalSheet().preSheet;
				SheetDao dao = new SheetDao();
				if (sheet != null) {
					dao.delete(sheet.getId());
					app.refreshLocalSheet();
					MusicSheet mSheet = createMusicSheet(preSheet);
					app.refreshDisplaySheet(mSheet);
					System.out.println("删除歌单 " + sheet.getId());
				}

			}

		});
		this.add(createMusicSheetButton);
		this.add(deleteMusicSheetButton);
	}

	// 通过 sheet 创建 musicSheet
	MusicSheet createMusicSheet(Sheet s) {
		if (s == null)
			return null;
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
