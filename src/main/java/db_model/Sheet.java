package db_model;

/**
 * 歌单实体类
 *
 */
public class Sheet {
	private int id;                  // 歌单ID（主键）
	private String uuid;             // 歌单UUID
	private String name;             // 歌单名称
	private String creator;          // 歌单创建者
	private String dateCreated;      // 创建日期
	private String picPath;          // 歌单封面图片路径

	public Sheet() {
	}

	public Sheet(String name) {
		this();
		this.name = name;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUuid() {
		return uuid;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

}