package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db_model.Music;
import utils.SqliteUtil;

/**
 * 歌曲数据操作类
 * 
 * 要求：仿照SheetDao自行实现
 *
 */
public class MusicDao implements BaseDao<Music> {
	/**
	 * 向歌曲表插入新的歌曲
	 */

	@Override
	public void insert(Music music) {
		// TODO Auto-generated method stub
		Connection conn = null;
		PreparedStatement ps = null;
		String sql = "INSERT INTO music (NAME, sheet_id, MD5, file_path) VALUES (?, ?, ?, ?)";
		try {
			conn = SqliteUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, music.getName());
			ps.setLong(2, music.getSheetId());
			ps.setString(3, music.getMd5());
			ps.setString(4, music.getFilePath());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SqliteUtil.close(null, ps, conn);
		}
	}

	/**
	 * 根据歌曲id删除歌曲
	 */
	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		Connection conn = null;
		PreparedStatement ps = null;
		String sql = "DELETE FROM music WHERE id=?";
		try {
			conn = SqliteUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SqliteUtil.close(null, ps, conn);
		}
	}

	/**
	 * 修改歌曲
	 */
	@Override
	public void update(Music music) {
		// TODO Auto-generated method stub
		Connection conn = null;
		PreparedStatement ps = null;
		String sql = "UPDATE SHEET SET NAME=?, sheet_id=?, MD5=?, file_path=? WHERE ID=?";
		try {
			conn = SqliteUtil.getConnection();
			ps = conn.prepareStatement(sql);

			ps.setString(1, music.getName());
			ps.setLong(2, music.getSheetId());
			ps.setString(3, music.getMd5());
			ps.setString(4, music.getFilePath());
			ps.setInt(5, music.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SqliteUtil.close(null, ps, conn);
		}
	}

	/**
	 * 列出所有歌曲
	 */
	@Override
	public List<Music> findAll() {
		// TODO Auto-generated method stub
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Music music = null;
		List<Music> musics = new ArrayList<Music>();
		String sql = "SELECT ID, NAME, sheet_id, MD5, file_path FROM music";
		try {
			conn = SqliteUtil.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				music = new Music();
				music.setId(rs.getInt(1));
				music.setName(rs.getString(2));
				music.setSheetId(rs.getInt(3));
				music.setMd5(rs.getString(4));
				music.setFilePath(rs.getString(5));
				musics.add(music);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SqliteUtil.close(null, ps, conn);
		}
		return musics;
	}

	/**
	 * 根据歌曲 id 查询歌曲
	 */
	@Override
	public Music findById(int id) {
		// TODO Auto-generated method stub
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Music music = null;
		String sql = "SELECT ID, NAME, sheet_id, MD5, file_path FROM music WHERE ID=?";
		try {
			conn = SqliteUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				music = new Music();
				music.setId(rs.getInt(1));
				music.setName(rs.getString(2));
				music.setSheetId(rs.getInt(3));
				music.setMd5(rs.getString(4));
				music.setFilePath(rs.getString(5));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SqliteUtil.close(null, ps, conn);
		}
		return music;
	}

	/**
	 * 基于歌单ID查询所有属于该歌单的歌曲
	 * 
	 * @param sheetId 歌单ID
	 * @return musics 该歌单下的所有歌曲
	 */
	public List<Music> findBySheetId(int sheetId) {

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Music music = null;
		List<Music> musics = new ArrayList<Music>();
		String sql = "SELECT ID, NAME, sheet_id, MD5, file_path FROM music where sheet_id=?";
		try {
			conn = SqliteUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, sheetId);
			rs = ps.executeQuery();
			while (rs.next()) {
				music = new Music();
				music.setId(rs.getInt(1));
				music.setName(rs.getString(2));
				music.setSheetId(rs.getInt(3));
				music.setMd5(rs.getString(4));
				music.setFilePath(rs.getString(5));
				musics.add(music);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SqliteUtil.close(null, ps, conn);
		}
		return musics;
	}

	// 测试
//	public static void main(String[] args) {
//		MusicDao dao = new MusicDao();
////		Music m = new Music();
////		m.setName("测试2");
////		m.setSheetId(3);
////		m.setMd5("test32424324323");
////		m.setFilePath("testfilepath");
////
////		dao.insert(m);
////		List<Music> ms = new ArrayList<Music>();
////		ms = dao.findBySheetId(1);
////		for (Music music : ms) {
////			System.out.println("名称:" + music.getName());
////		}
//	}
//
}
