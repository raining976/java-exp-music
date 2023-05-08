package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db_model.Sheet;
import utils.SqliteUtil;

/**
 * 操作歌单表的数据操作对象类
 * 
 *
 */
public class SheetDao implements BaseDao<Sheet> {
	/**
	 * 向歌单表中插入新歌单记录
	 */
	@Override
	public void insert(Sheet sheet) {
		Connection conn = null;
		PreparedStatement ps = null;
		String sql = "INSERT INTO SHEET (NAME, DATE_CREATED, CREATOR, PIC_PATH) VALUES (?, ?, ?, ?)";
		try {
			conn = SqliteUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, sheet.getName());
			ps.setString(2, sheet.getDateCreated());
			ps.setString(3, sheet.getCreator());
			ps.setString(4, sheet.getPicPath());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SqliteUtil.close(null, ps, conn);
		}
	}

	/**
	 * 删除歌单（基于ID删除）
	 */
	@Override
	public void delete(int id) {
		Connection conn = null;
		PreparedStatement ps = null;
		String sql = "DELETE FROM SHEET WHERE id=?";
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
	 * 修改歌单
	 */
	@Override
	public void update(Sheet sheet) {
		Connection conn = null;
		PreparedStatement ps = null;
		String sql = "UPDATE SHEET SET NAME=?, DATE_CREATED=?, CREATOR=?, PIC_PATH=? WHERE ID=?";
		try {
			conn = SqliteUtil.getConnection();
			ps = conn.prepareStatement(sql);

			ps.setString(1, sheet.getName());
			ps.setString(2, sheet.getDateCreated());
			ps.setString(3, sheet.getCreator());
			ps.setString(4, sheet.getPicPath());
			ps.setInt(5, sheet.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SqliteUtil.close(null, ps, conn);
		}

	}

	/**
	 * 列出所有歌单
	 */
	@Override
	public List<Sheet> findAll() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Sheet sheet = null;
		List<Sheet> sheets = new ArrayList<Sheet>();
		String sql = "SELECT ID, NAME, DATE_CREATED, CREATOR, PIC_PATH FROM SHEET";
		try {
			conn = SqliteUtil.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				sheet = new Sheet();
				sheet.setId(rs.getInt(1));
				sheet.setName(rs.getString(2));
				sheet.setDateCreated(rs.getString(3));
				sheet.setCreator(rs.getString(4));
				sheet.setPicPath(rs.getString(5));
				sheets.add(sheet);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SqliteUtil.close(null, ps, conn);
		}
		return sheets;
	}

	/**
	 * 基于歌单ID查询歌单（只有一个，因为歌单ID是唯一的主键）
	 */
	@Override
	public Sheet findById(int id) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Sheet sheet = null;
		String sql = "SELECT ID, NAME, DATE_CREATED, CREATOR, PIC_PATH FROM SHEET WHERE ID=?";
		try {
			conn = SqliteUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				sheet = new Sheet();
				sheet.setId(rs.getInt(1));
				sheet.setName(rs.getString(2));
				sheet.setDateCreated(rs.getString(3));
				sheet.setCreator(rs.getString(4));
				sheet.setPicPath(rs.getString(5));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SqliteUtil.close(null, ps, conn);
		}
		return sheet;
	}

	/**
	 * 测试方法
	 * 
	 */
//	public static void main(String[] args) {
//		SheetDao dao = new SheetDao();
//
//		// 添加歌单
////		Sheet a = new Sheet();
////		a.setName("Use your illusion");
////		a.setDateCreated("2023-04-07");
////		a.setCreator("王晓东");
////		a.setPicPath("/home/user/a.jpg");
////
////		dao.insert(a); // 向数据表Sheet中插入一条记录
//
////		List<Sheet> sheets = dao.findAll(); // 查询所有数据表
////
////		for (Sheet sheet : sheets) {
////			System.out.println(sheet.getId() + " " + sheet.getName() + " " + sheet.getDateCreated() + " "
////					+ sheet.getCreator() + " " + sheet.getPicPath());
////		}
////
////		System.out.println(dao.findById(1).getName()); // 根据歌单ID查询歌单
//
//		dao.delete(1); // 删除ID为3的歌单
////
////		a.setId(1); // 修改ID为1的歌单
////		a.setCreator("枪炮与玫瑰");
////		dao.update(a);
//
//		List<Sheet> sheets = dao.findAll(); // 查询所有数据表
//
//		for (Sheet sheet : sheets) {
//			System.out.println(sheet.getId() + " " + sheet.getName() + " " + sheet.getDateCreated() + " "
//					+ sheet.getCreator() + " " + sheet.getPicPath());
//		}
//	}
}