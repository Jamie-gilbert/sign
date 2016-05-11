package com.check_ins.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import utils.DBUtil;

public class TeacherDao {
	private Connection connection = null;
	private PreparedStatement statement = null;
	private ResultSet set = null;

	public String login(String username, String pass) {

		String result = null;
		connection = DBUtil.getConnection();
		String sql = "SELECT TEACHERNAME FROM TEACHER WHERE TEACHERID=?AND PASSWORD=?";
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, username);
			statement.setString(2, pass);
			set = statement.executeQuery();
			if (set != null) {
				if(set.next())
				{
					result=set.getString("TEACHERNAME");
				}
			} else {
				result=null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.closeDB(connection, statement, set);
		}

		return result;
	}
}
