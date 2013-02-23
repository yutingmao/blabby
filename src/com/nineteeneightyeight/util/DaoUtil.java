package com.nineteeneightyeight.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;

/**
 * 数据库连接类
 * 
 * @author flytreeleft
 * 
 */
public class DaoUtil {

	/**
	 * 静态方法,获取数据库连接对象
	 * */
	public static Connection getConnection() {
		String driverName = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://arlia.computing.dundee.ac.uk:3306/yutingmao";
		String userName = "YutingMao";
		String password = "ac31004";
		Connection conn = null;
		
		try {
			Class.forName(driverName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			conn = DriverManager.getConnection(url, userName, password);
		} catch (SQLException e) {
			conn = null;
			e.printStackTrace();
		}

		return conn;
	}
}
