package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import dao.SheetDao;
import db_model.Sheet;

public class AddSheetGui extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4114538756972311347L;
	JTextField nameField = new JTextField(); // 歌曲名
	JTextField picField = new JTextField(); // 封面
	JButton addbtn = new JButton("确定添加");

	public Sheet getNewSheet() {
		return newSheet;
	}

	Sheet newSheet;
	MusicPlayerGUI app;

	public AddSheetGui(MusicPlayerGUI app) {
		this.app = app;
		this.setTitle("添加歌单");
		this.setSize(400, 200);
		init();
		this.setLayout(null);
	}

	private void init() {
		JLabel jl1 = new JLabel("歌单名称:");
		JLabel jl2 = new JLabel("歌单封面地址:");
		jl1.setBounds(30, 10, 100, 25);
		nameField.setBounds(130, 10, 200, 30);
		jl2.setBounds(30, 50, 100, 25);
		picField.setBounds(130, 50, 200, 30);
		addbtn.setBounds(50, 115, 300, 30);
		this.add(jl1);
		this.add(jl2);
		this.add(nameField);
		this.add(picField);
		this.add(addbtn);

		addbtn.addActionListener(this);
	}

	String getNowDate() {
		SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
		sdf.applyPattern("yyyy-MM-dd");// a为am/pm的标记
		Date date = new Date();// 获取当前时间
		System.out.println("现在时间：" + sdf.format(date)); // 输出已经格式化的现在时间（24小时制）
		return sdf.format(date);
	}

	public void actionPerformed(ActionEvent e) {
		String name = String.valueOf(nameField.getText());
		String pic_path = String.valueOf(picField.getText());
		String creator = "Raining";
		String dateCreated = getNowDate();
		newSheet = new Sheet();
		newSheet.setName(name);
		newSheet.setCreator(creator);
		newSheet.setDateCreated(dateCreated);
		newSheet.setPicPath(pic_path);
//		System.out.println(name.length());
		SheetDao sheetDao = new SheetDao();
		if (name.length() != 0 && pic_path.length() != 0) {
			sheetDao.insert(newSheet);
			app.refreshLocalSheet();
		}
		this.setVisible(false);

	}
}