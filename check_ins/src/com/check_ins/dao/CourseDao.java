package com.check_ins.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.DBUtil;

import com.check_ins.bo.CheckRecordBo;
import com.check_ins.bo.CourseBo;

public class CourseDao {
	private Connection connection = null;
	private PreparedStatement statement = null;
	private ResultSet set = null;

	/**
	 * 初始化签到记录表
	 * 
	 * @param bo
	 */
	public void initSt_Data(List<CheckRecordBo> bos) {
		connection = DBUtil.getConnection();

		String sql = "INSERT INTO check_record (SHEETID,USERID,COURSEID,STATE,CLASSID,TEACHER,CHECKDATE,CHECKTIME,ROOMID)VALUES(UUID(),?,?,?,?,?,?,?,?)";
		try {
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(sql);
			for (CheckRecordBo bo : bos) {
				statement.setString(1, bo.getUSERID());
				statement.setString(2, bo.getCOURSERID());
				statement.setInt(3, bo.getSTATE());
				statement.setString(4, bo.getCLASSID());
				statement.setString(5, bo.getTEACHER());
				statement.setString(6, bo.getCHECKDATE());
				statement.setString(7, bo.getCHECKTIME());
				statement.setString(8, bo.getCLASSROOM());
				statement.addBatch();
			}
			statement.executeBatch();
			connection.commit();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.closeDB(connection, statement, set);
		}

	}

	/**
	 * 教师定位和二维码的课程集合
	 * 
	 * @param teachername
	 * @return
	 */
	public List<Map<String, String>> initCourse(String teachername) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		String sql = "SELECT COURSEID ,COURSENAME,UUIDS,ROOMID,COURSERDATE FROM course WHERE TEACHER=? AND (ROOMID IS NOT NULL AND ROOMID<>'') ";

		connection = DBUtil.getConnection();
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, teachername);
			set = statement.executeQuery();
			if (set != null) {
				while (set.next()) {
					Map<String, String> map = new HashMap<String, String>();
					String COURSEID = set.getString("COURSEID");
					map.put("id", COURSEID);
					String COUSERNAME = set.getString("COURSENAME");
					map.put("name", COUSERNAME);
					String UUIDS = set.getString("UUIDS");
					map.put("uuid", UUIDS);
					String CLASSROOM = set.getString("ROOMID");
					map.put("room", CLASSROOM);
					String COUSERDATE = set.getString("COURSERDATE");
					map.put("date", COUSERDATE);
					list.add(map);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.closeDB(connection, statement, set);

		}

		return list;

	}

	/**
	 * 获取课程
	 * 
	 * @param courseid
	 * @return
	 */

	public CourseBo getCourse(String courseid) {
		CourseBo bo = new CourseBo();
		String sql = "SELECT COURSENAME,TEACHER,ROOMID,COURSERDATE FROM course  WHERE COURSEID=?";

		connection = DBUtil.getConnection();
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, courseid);
			set = statement.executeQuery();
			if (set != null) {
				if (set.next()) {
					String COUSERNAME = set.getString("COURSENAME");
					bo.setCOURSENAME(COUSERNAME);
					String TEACHER = set.getString("TEACHER");
					bo.setTEACHER(TEACHER);
					String CLASSROOM = set.getString("ROOMID");
					bo.setCLASSROOM(CLASSROOM);
					String COURSERDATE = set.getString("COURSERDATE");
					bo.setCOURSERDATE(COURSERDATE);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.closeDB(connection, statement, set);
		}

		return bo;

	}

	/**
	 * 获取课程的开始时间
	 * 
	 * @param cid
	 * @return
	 */
	public String getLastDate(String cid) {
		String time = null;
		String sql = "SELECT MAX(STR_TO_DATE(CHECKTIME,'%Y-%m-%d %H:%i:%s'))AS LASTDATE FROM check_record WHERE COURSEID=?";
		connection = DBUtil.getConnection();
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, cid);
			set = statement.executeQuery();
			if (set != null) {
				if (set.next()) {
					time = set.getString("LASTDATE");
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.closeDB(connection, statement, set);
		}

		return time;
	}

	/**
	 * 更新uuid
	 * 
	 * @param uuid
	 * @param courseid
	 */
	public void updateUUID(String uuid, String courseid) {
		connection = DBUtil.getConnection();
		String sql = "UPDATE course SET UUIDS=? WHERE  COURSEID=?";

		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, uuid);
			statement.setString(2, courseid);
			statement.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 更新记录表
	 * 
	 * @param params
	 * @return
	 */
	public boolean updateRecord(String[] params) {
		boolean flag = true;
		connection = DBUtil.getConnection();

		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = format.format(date);
		String sql = "UPDATE  check_record SET STATE=?, CHECKTIME=? WHERE SHEETID=?";
		try {
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(sql);
			for (String p : params) {
				String str[] = p.split(",");
				statement.setString(1, str[1]);
				statement.setString(2, time);
				statement.setString(3, str[0]);
				statement.addBatch();
			}
			statement.executeBatch();
			connection.commit();
		} catch (SQLException e) {
			flag = false;
			e.printStackTrace();
		} finally {
			DBUtil.closeDB(connection, statement, set);
		}
		return flag;

	}

	/**
	 * 扫描二维码签到
	 * 
	 * @param map
	 * @return
	 */
	public int ScanSign(Map<String, String> map) {
		int flag = -1;
		// SELECT UUIDS FROM course WHERECOURSEID=? COURSEID

		String sql = "SELECT UUIDS FROM course WHERE COURSEID=?";
		connection = DBUtil.getConnection();
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, map.get("courseid"));
			String uuid = null;
			set = statement.executeQuery();
			if (set != null) {
				if (set.next()) {
					uuid = set.getString("UUIDS");
				}

			}
			if (uuid != null && uuid.equals(map.get("uuid"))) {
				sql = "UPDATE  check_record SET STATE=? WHERE COURSEID=? AND CHECKTIME=? AND USERID=?";

				statement = connection.prepareStatement(sql);
				statement.setString(1, "1");
				statement.setString(2, map.get("courseid"));
				statement.setString(3, map.get("time"));
				statement.setString(4, map.get("userid"));
				flag = statement.executeUpdate();
			} else {
				flag = -2;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.closeDB(connection, statement, set);
		}
		System.out.println(flag);
		return flag;
	}

	/**
	 * 获取上课教室的位置
	 * 
	 * @param cid
	 * @return
	 */
	public List<Map<String, String>> getCourseRoom(String cid) {
		String sql = "SELECT R.NELONTITUDE,R.NELANTITUDE,R.SWLONTITUDE,R.SWLANTITUDE,R.ALTITUDE FROM	 course C,room R WHERE C.ROOMID=R.ROOMID AND C.COURSEID=?";
		List<Map<String, String>> maps = new ArrayList<Map<String, String>>();
		connection = DBUtil.getConnection();
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, cid);
			set = statement.executeQuery();

			while (set.next()) {
				Map<String, String> map = new HashMap<String, String>();
				String NELONTITUDE = set.getString("NELONTITUDE");
				map.put("NELONTITUDE", NELONTITUDE);
				String NELANTITUDE = set.getString("NELANTITUDE");
				map.put("NELANTITUDE", NELANTITUDE);
				String SWLONTITUDE = set.getString("SWLONTITUDE");
				map.put("SWLONTITUDE", SWLONTITUDE);
				String SWLANTITUDE = set.getString("SWLANTITUDE");
				map.put("SWLANTITUDE", SWLANTITUDE);
				String ALTITUDE = set.getString("ALTITUDE");
				map.put("ALTITUDE", ALTITUDE);
				maps.add(map);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.closeDB(connection, statement, set);
		}
		return maps;
	}

	/**
	 * 确定教室的课程的课程集合
	 * 
	 * @param teachername
	 * @return
	 */
	public List<Map<String, String>> addCourse(String teachername) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		String sql = "SELECT COURSEID ,COURSENAME,UUIDS,ROOMID,COURSERDATE FROM course WHERE TEACHER=?";

		connection = DBUtil.getConnection();
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, teachername);
			set = statement.executeQuery();
			if (set != null) {
				while (set.next()) {
					Map<String, String> map = new HashMap<String, String>();
					String COURSEID = set.getString("COURSEID");
					map.put("id", COURSEID);
					String COUSERNAME = set.getString("COURSENAME");
					map.put("name", COUSERNAME);
					String UUIDS = set.getString("UUIDS");
					map.put("uuid", UUIDS);
					String CLASSROOM = set.getString("ROOMID");
					map.put("room", CLASSROOM);
					String COUSERDATE = set.getString("COURSERDATE");
					map.put("date", COUSERDATE);
					list.add(map);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.closeDB(connection, statement, set);

		}

		return list;

	}

	/**
	 * 获取未安排课的时间段
	 * 
	 * @param date
	 * @return
	 */
	public List<String> getTime(String date) {
		List<String> list = new ArrayList<>();
		String[] time = { "1,2节", "3,4节", "5,6节", "7,8节", "9,10节" };
		String sql = "SELECT COURSERDATE FROM course WHERE COURSERDATE LIKE '%" + date + "%'";
		connection = DBUtil.getConnection();
		try {
			statement = connection.prepareStatement(sql);
			set = statement.executeQuery();
			if (set != null) {
				while (set.next()) {
					String COURSERDATE = set.getString("COURSERDATE");
					String t[] = COURSERDATE.split(" ");
					for (int i = 0; i < time.length; i++) {
						if (t[1].equals(time[i])) {
							time[i] = "null";
						}
					}
				}
				for (String s : time) {
					if (!"null".equals(s)) {

						list.add(s);
					}
				}

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.closeDB(connection, statement, set);
		}

		return list;
	}

	/**
	 * 获取为安排上课的教室
	 * 
	 * @param date
	 * @param time
	 * @return
	 */
	public List<Map<String, String>> getRoom(String date, String time) {

		List<Map<String, String>> list = new ArrayList<>();

		String sql = "SELECT ROOMID,ROOMNAME FROM room R WHERE ROOMID NOT IN(SELECT ROOMID FROM course WHERE COURSERDATE LIKE '%"
				+ date + "%" + time + "%')";
		connection = DBUtil.getConnection();
		try {
			statement = connection.prepareStatement(sql);
			set = statement.executeQuery();
			if (set != null) {
				while (set.next()) {
					Map<String, String> map = new HashMap<>();
					String ROOMID = set.getString("ROOMID");
					map.put("id", ROOMID);
					String ROOMNAME = set.getString("ROOMNAME");
					map.put("name", ROOMNAME);
					list.add(map);
				}

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.closeDB(connection, statement, set);
		}

		return list;

	}

	public int modifyCourse(String cid, String date, String roomid) {
		int flag = -1;
		String sql = "UPDATE course SET ROOMID=? ,COURSERDATE=? WHERE COURSEID=?";
		connection = DBUtil.getConnection();
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, roomid);
			statement.setString(2, date);
			statement.setString(3, cid);
			flag=statement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}
}
