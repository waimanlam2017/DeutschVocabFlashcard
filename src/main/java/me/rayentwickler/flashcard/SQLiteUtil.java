package me.rayentwickler.flashcard;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteUtil {
	private Connection conn;

	public void connect() throws ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		String path = System.getProperty("user.dir");
		System.out.println("Working Directory = " + path);
		try {
			this.conn = DriverManager.getConnection("jdbc:sqlite:Vokabeln.sqlite");
			if (this.conn != null) {
				System.out.println("Connection to SQLite has been established.");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void executeUpdateSQL(String sql) {
		try {
			Statement stmt = this.conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ResultSet executeSelectSQL(String sql) {
		try {
			Statement stmt = this.conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void freeDB() {
		if (this.conn != null) {
			try {
				this.conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
