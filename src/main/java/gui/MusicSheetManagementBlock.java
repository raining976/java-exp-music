package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

import dao.SheetDao;
import db_model.Sheet;

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
				SheetDao dao = new SheetDao();
				if (sheet != null) {
					dao.delete(sheet.getId());
					app.refreshLocalSheet();
					System.out.println("删除歌单 " + sheet.getId());
				}

			}

		});
		this.add(createMusicSheetButton);
		this.add(deleteMusicSheetButton);
	}

}
