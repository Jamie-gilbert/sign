package com.check_ins.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import utils.DBUtil;

public class Test {

	public static void main(String[] args) {
		Connection connection = DBUtil.getConnection();
		PreparedStatement statement = null;
		String sql = "INSERT INTO class (CLASSID,CLASSNAME,SNUMBER,HEADTEACHER,MONITOR) VALUES('1234','111',32,'AAA',?)";
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, "ff");
			statement.execute();
			sql = "UPDATE class SET SNUMBER=455 WHERE CLASSID=?";
			statement.execute(sql);
		statement.setString(1, "1234");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.closeDB(connection, statement, null);
		}
	}
}
