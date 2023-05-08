package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SqliteUtil {

	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取数据库连接
	 * 
	 * @return
	 */
	public static Connection getConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1/music-player?user=root&password=admin123");
			System.out.println("Database Opened.");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Database Connection failed.");
		}

		return conn;
	}

	/**
	 * 关闭数据库连接
	 */
	public static void close(ResultSet rs, Statement stmt, Connection conn) {
		try {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Database Closed.");
	}

	/**
	 * 创建SHEET和MUSIC表
	 */
	public static void createTables() {
		Connection conn = null;
		Statement stmt = null;

		try {
			conn = SqliteUtil.getConnection();
			stmt = conn.createStatement();
			// 创建歌单表
			String sql_sheet = "CREATE TABLE IF NOT EXISTS SHEET (ID INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ "NAME TEXT NOT NULL, " + " DATE_CREATED TEXT, " + "CREATOR TEXT NOT NULL, " + "PIC_PATH TEXT)";
			stmt.executeUpdate(sql_sheet);
			// 创建歌曲表
			String sql_music = "CREATE TABLE IF NOT EXISTS MUSIC (ID INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ "NAME TEXT NOT NULL, " + "SHEET_ID INT NOT NULL, " + "MD5 CHAR(32) NOT NULL, "
					+ "FILE_PATH TEXT)";
			stmt.executeUpdate(sql_music);
			stmt.close();
			conn.close();
			System.out.println("Table created.");
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		} finally {
			SqliteUtil.close(null, stmt, conn);
		}
	}

	/**
	 * 列出所有数据表（排除Sqlite的管理表）
	 * 
	 * @return
	 */
	public static List<String> showTables() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> tables = new ArrayList<String>();
		String sql = "select table_name from information_schema.tables where table_schema='music-player' and table_type='BASE TABLE';";
		try {
			conn = SqliteUtil.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				tables.add(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SqliteUtil.close(null, ps, conn);
		}
		return tables;
	}

	/**
	 * 测试方法
	 * 
	 */
	public static void main(String[] args) {
		// 创建数据表
//		createTables();

		// 列出所有数据表
		Iterator<String> tables = showTables().iterator();

		while (tables.hasNext()) {
			System.out.println(tables.next());
		}
	}
}
