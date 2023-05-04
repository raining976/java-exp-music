package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.MusicSheet;
import musicclient.MusicOperationClient;

public class MusicSheetDisplayBlock extends JPanel {

	private static final long serialVersionUID = 1L;
	private String picPath = "/Users/xiaodong/Music/guns and roses/fig-guns and roses.jpg";
	private final String picBasicUrl = "D:\\desktop\\oucMusicStatic\\musicCover\\";
	private final String musicBasicUrl = "D:\\desktop\\oucMusicStatic\\musicSource\\";

	public MusicSheetDisplayBlock(MusicSheet musicSheet) {
		this.setPreferredSize(new Dimension(550, 200));
		this.setLayout(new FlowLayout(FlowLayout.LEFT));

		MusicOperationClient moc = new MusicOperationClient();
		// 下载封面
		picPath = picBasicUrl + musicSheet.getPicture();
		File picFile = new File(picPath);
		if (!picFile.exists()) {
			moc.downloadMusicSheetPicture(musicSheet.getUuid(), picBasicUrl);
			System.out.println("封面不存在,正在下载...");
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

		musicSheetButtonPanel.add(playAllMusicButton);
		musicSheetButtonPanel.add(downloadAllMusicButton);

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
