package com.bold.Util.DB;

import java.sql.DriverManager;
import java.sql.*;

public class DataBaseUtil {

	public static Connection getConnection()
	{
		Connection conn = null;
		try
		{
			// 加载驱动
			Class.forName("com.mysql.jdbc.Driver");
			// 数据库连接url
			String url = "jdbc:mysql://localhost:3306/diunalepro";
			// 获取数据库连接
			conn = DriverManager.getConnection(url, "root", "123456");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return conn;
	}

	public static void closeConnection(Connection conn)
	{
		// 判断是否为空
		if (conn != null)
		{
			try
			{
				conn.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
	}
}









