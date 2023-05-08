package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

import dao.MusicDao;
import db_model.Music;
import model.MusicSheet;
import musicclient.MusicOperationClient;

public class MusicSheetDisplayBlock extends JPanel {

	private static final long serialVersionUID = 1L;
	private String picPath = "/Users/xiaodong/Music/guns and roses/fig-guns and roses.jpg";
	private final String picBasicUrl = "D:\\desktop\\oucMusicStatic\\musicCover\\";
	private final String musicBasicUrl = "D:\\desktop\\oucMusicStatic\\musicSource\\";
	private boolean isLocal = false;

	public MusicSheetDisplayBlock(MusicSheet musicSheet, MusicPlayerGUI app) {
		this.setPreferredSize(new Dimension(550, 200));
		this.setLayout(new FlowLayout(FlowLayout.LEFT));

		MusicOperationClient moc = new MusicOperationClient();
		// 下载封面
		this.isLocal = app.isLocal;
		String pic_path = musicSheet.getPicture();
		if (!isLocal) {
			picPath = picBasicUrl + pic_path;
			File picFile = new File(picPath);
			if (!picFile.exists()) {
				moc.downloadMusicSheetPicture(musicSheet.getUuid(), picBasicUrl);
				System.out.println("封面不存在,正在下载...");
			}
		} else {
			picPath = pic_path;
		}

		ImageIcon musicSheetPicture = new ImageIcon(picPath);
		int musicSheetPictureWidth = 250;
		int musicSheetPictureHeight = 250 * musicSheetPicture.getIconHeight() / musicSheetPicture.getIconWidth();
		musicSheetPicture.setImage(musicSheetPicture.getImage().getScaledInstance(musicSheetPictureWidth,
				musicSheetPictureHeight, Image.SCALE_DEFAULT));

		JLabel musicSheetPictureLabel = new JLabel(musicSheetPicture);
		musicSheetPictureLabel.setPreferredSize(new Dimension(musicSheetPictureWidth, musicSheetPictureHeight));

		JPanel musicSheetInfoPanel = new JPanel();
		musicSheetInfoPanel.setPreferredSize(new Dimension(250, 200));
		musicSheetInfoPanel.setLayout(new BoxLayout(musicSheetInfoPanel, BoxLayout.Y_AXIS));

		JLabel musicSheetTitleLabel = new JLabel(musicSheet.getName());
		JLabel musicSheetCreatorLabel = new JLabel(
				musicSheet.getDateCreated() + " 由 " + musicSheet.getCreator() + " 创建");

		JPanel musicSheetButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton playAllMusicButton = new JButton("播放全部");
		// 播放当前歌单中的全部歌曲
		playAllMusicButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				// TODO 多线程 生产者消费者模式
			}

		});
		JButton downloadAllMusicButton = new JButton("下载全部");
		// 下载当前歌单中的全部歌曲
		downloadAllMusicButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Object[] keys = musicSheet.getMusicItems().keySet().toArray();
				for (Object key : keys) {
					String musicPath = musicBasicUrl + musicSheet.getMusicItems().get(key);
					File musicFile = new File(musicPath);
					if (!musicFile.exists()) {
						System.out.println("歌曲不存在,正在下载...");
						moc.downloadMusicFile(key.toString(), musicBasicUrl);
					}
				}
			}

		});
		JButton addMusic = new JButton("添加歌曲");
		// 为创建歌单绑定事件
		MusicDao mDao = new MusicDao();
		addMusic.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
//				AddMusicGui addMusic = new AddMusicGui(app);
//				addMusic.setVisible(true);
				JFileChooser fileChooser = new JFileChooser();
				int res = fileChooser.showOpenDialog(app);
				if (res == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					String filePath = selectedFile.getAbsolutePath();
					int sheetId = app.getLocalSheet().curSheet.getId();
					String name = filePath.substring(filePath.lastIndexOf("\\") + 1, filePath.indexOf('.'));
					FileInputStream fis;
					String Md5 = null;
					try {
						fis = new FileInputStream(filePath);
						Md5 = DigestUtils.md5Hex(IOUtils.toByteArray(fis));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					Music music = new Music();
					music.setName(name);
					music.setSheetId(sheetId);
					music.setFilePath(filePath);
					music.setMd5(Md5);
					mDao.insert(music);
					Map<String, String> mum = musicSheet.getMusicItems();
					mum.put(Md5, name);
					musicSheet.setMusicItems(mum);
					app.refreshDisplaySheet(musicSheet);

				}
			}
		});

		musicSheetButtonPanel.add(playAllMusicButton);
		if (!this.isLocal)
			musicSheetButtonPanel.add(downloadAllMusicButton);
		if (this.isLocal)
			musicSheetButtonPanel.add(addMusic);
		musicSheetInfoPanel.add(Box.createVerticalStrut(20));
		musicSheetInfoPanel.add(musicSheetTitleLabel);
		musicSheetInfoPanel.add(Box.createVerticalStrut(10));
		musicSheetInfoPanel.add(musicSheetCreatorLabel);
		musicSheetInfoPanel.add(Box.createVerticalStrut(30));
		musicSheetInfoPanel.add(musicSheetButtonPanel);

		musicSheetButtonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		this.add(musicSheetPictureLabel);
		this.add(musicSheetInfoPanel);

	}

}
