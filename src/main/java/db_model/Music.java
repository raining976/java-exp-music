package db_model;

/**
 * 歌曲实体类
 *
 */
public class Music {
	private int id; // 歌曲ID（主键）
	private String name; // 歌曲名称
	private int sheetId; // 所属歌单ID
	private String md5; // 歌曲MD5值
	private String filePath; // 歌曲文件的磁盘路径
	private String singer; // 歌曲演唱者

	public Music() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSheetId() {
		return sheetId;
	}

	public void setSheetId(int sheetId) {
		this.sheetId = sheetId;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSinger() {
		return singer;
	}

	public void setSinger(String singer) {
		this.singer = singer;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}