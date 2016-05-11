package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtil {
	private static String url = "jdbc:mysql://localhost/check_ins";
	private static String driverName = "com.mysql.jdbc.Driver";
	private static String user = "root";
	private static String password = "root";

	public static Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName(driverName);
			url += "?user=" + user + "&password=" + password
					+ "&useUnicode=true&characterEncoding=GBK";

			connection = DriverManager.getConnection(url);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}

	public static void closeDB(Connection connection,
			PreparedStatement preparedStatement, ResultSet set) {

		if (set != null) {
			try {
				set.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (preparedStatement != null) {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
