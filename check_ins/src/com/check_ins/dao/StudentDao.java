package com.check_ins.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.DBUtil;

import com.check_ins.bo.LocationBo;
import com.check_ins.bo.RecordBo;
import com.check_ins.bo.StudentBo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class StudentDao {
	private Connection connection = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet set = null;

	/**
	 * 根据课程获取学生信息
	 * 
	 * @param courseid
	 * @return
	 */
	public List<StudentBo> getStudents(String courseid) {
		List<StudentBo> bos = new ArrayList<StudentBo>();
		String sql = "SELECT C.USERID,C.CLASSID FROM student C LEFT JOIN (SELECT CLASSID  FROM class_associate_course WHERE COURSEID=?) AS A ON A.CLASSID=C.CLASSID";
		connection = DBUtil.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, courseid);
			set = preparedStatement.executeQuery();
			if (set != null) {
				while (set.next()) {
					StudentBo bo = new StudentBo();
					String USERID = set.getString("USERID");
					bo.setUSERID(USERID);
					String CLASSID = set.getString("CLASSID");
					bo.setCLASSID(CLASSID);
					bos.add(bo);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.closeDB(connection, preparedStatement, set);
		}

		return bos;
	}

	/**
	 * 根据课程和签到日期获未签到学生
	 * 
	 * @param courseid
	 * @param scanDate
	 * @return
	 */
	public List<Map<String, String>> getAbsenteeismer(String courseid, String scanDate) {
		List<Map<String, String>> maps = new ArrayList<Map<String, String>>();
		connection = DBUtil.getConnection();
		String sql = "SELECT S.USERID,	S.USERNAME,	C.CLASSNAME,	R.SHEETID FROM	student S,	class C,	check_record R WHERE R.COURSEID=? AND S.USERID=R.USERID AND S.CLASSID=C.CLASSID AND R.STATE = 0	AND (	R.CHECKTIME BETWEEN STR_TO_DATE(	?,	'%Y-%m-%d %H:%i:%s'	)	AND NOW())";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, courseid);
			preparedStatement.setString(2, scanDate);
			set = preparedStatement.executeQuery();
			if (set != null) {
				while (set.next()) {
					Map<String, String> map = new HashMap<String, String>();
					String USERID = set.getString("USERID");
					map.put("userid", USERID);
					String USERNAME = set.getString("USERNAME");
					map.put("username", USERNAME);
					String CLASSNAME = set.getString("CLASSNAME");
					map.put("classname", CLASSNAME);
					String SHEETID = set.getString("SHEETID");
					map.put("sheetid", SHEETID);
					maps.add(map);

				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.closeDB(connection, preparedStatement, set);
		}

		return maps;
	}

	/*
	 * 登陆
	 */
	public String login(String studentId, String passWord, String phoneNumber, String phoneId) {
		String flag = null;
		String PASSWORD = null, PHONEID = null, PHONENUMBER = null, USERNAME = null, CLASSNAME = null;
		connection = DBUtil.getConnection();
		String sql = "SELECT S.USERNAME ,S.PASSWORD,S.PHONEID,C.CLASSNAME FROM student S,class C  WHERE S.CLASSID=C.CLASSID AND  USERID=?;";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, studentId);
			set = preparedStatement.executeQuery();
			if (set != null) {
				if (set.next()) {
					USERNAME = set.getString("USERNAME");
					PASSWORD = set.getString("PASSWORD");
					PHONEID = set.getString("PHONEID");

					CLASSNAME = set.getString("CLASSNAME");
				}
				if (PASSWORD != null && PASSWORD.equals(passWord)) {
					if (PHONEID != null) {
						if (PHONEID.equals(phoneId)) {
							// 登陆成功
							flag = "0";
							flag += ";;;" + Bo2String(USERNAME, CLASSNAME);

						} else {
							// 使用了另外的设备登陆
							flag = "1";
						}
					} else {
						// 第一次登陆

						flag = "2";
						flag += ";;;" + Bo2String(USERNAME, CLASSNAME);
						StringBuffer sqlbuff = new StringBuffer();
						sqlbuff.append("UPDATE student SET PHONEID='");
						sqlbuff.append(phoneId);
						sqlbuff.append("',PHONENUMBER='");
						sqlbuff.append(phoneNumber);
						sqlbuff.append("' WHERE USERID='");
						sqlbuff.append(studentId);
						sqlbuff.append("'");
						preparedStatement.execute(sqlbuff.toString());

					}
				} else {
					// 用户的账号密码不匹配
					flag = "3";
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.closeDB(connection, preparedStatement, set);
		}

		return flag;

	}

	/**
	 * 更新学生的位置
	 * 
	 * @param bo
	 */

	public void updateLocation(LocationBo bo) {
		connection = DBUtil.getConnection();
		String sql = "UPDATE student SET LATITUDE=?,LONTITUDE=?,RADIUS=?,LOCALDATE=?,ALTITUDE=? WHERE USERID=?";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, bo.getLATITUDE());
			preparedStatement.setString(2, bo.getLONTITUDE());
			preparedStatement.setString(3, bo.getRADIUS());
			preparedStatement.setString(4, bo.getLOCALTIME());
			preparedStatement.setString(5, bo.getALTITUDE());
			preparedStatement.setString(6, bo.getUSERID());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.closeDB(connection, preparedStatement, set);
		}

	}

	/**
	 * 获取上课学生的位置
	 * 
	 * @param courseid
	 * @return
	 */
	public List<Map<String, String>> getStudentLocation(String courseid) {
		List<Map<String, String>> maps = new ArrayList<Map<String, String>>();
		String sql = "SELECT C.USERID,C.LATITUDE,C.LONTITUDE,C.ALTITUDE FROM student C LEFT JOIN (SELECT CLASSID  FROM class_associate_course WHERE COURSEID=?) AS A ON A.CLASSID=C.CLASSID";
		connection = DBUtil.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, courseid);
			set = preparedStatement.executeQuery();
			while (set.next()) {
				Map<String, String> map = new HashMap<>();
				String USERID = set.getString("USERID");
				map.put("USERID", USERID);
				String LATITUDE = set.getString("LATITUDE");
				map.put("LATITUDE", LATITUDE);
				String LONTITUDE = set.getString("LONTITUDE");
				map.put("LONTITUDE", LONTITUDE);
				String ALTITUDE = set.getString("ALTITUDE");
				map.put("ALTITUDE", ALTITUDE);
				maps.add(map);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return maps;
	}

	/**
	 * 获取用户的签到记录
	 * 
	 * @param userid
	 * @return
	 */
	public List<RecordBo> getRecordData(String userid) {
		List<RecordBo> bos = new ArrayList<>();
		String sql = "SELECT CLASSID,ELECTIVE FROM student WHERE USERID=?";
		connection = DBUtil.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, userid);
			set = preparedStatement.executeQuery();
			StringBuffer buffer = new StringBuffer();
			if (set != null) {
				if (set.next()) {

					String classid = set.getString("CLASSID");
					String elective = set.getString("ELECTIVE");
					buffer.append("('");
					if (elective != null)

					{

						String strs[] = elective.split(";");
						for (String s : strs) {
							buffer.append(s);
							buffer.append("','");
						}

					}
					buffer.append(classid);
					buffer.append("')");
				}
				set.close();
			}
			sql = "SELECT CO.COURSENAME,(	SELECT	TEACHERNAME FROM	teacher WHERE TEACHERID = CO.TEACHER ) AS TEACHERNAME, CO.ROOMID,	CO.COURSERDATE,(SELECT COUNT(STATE) FROM check_record R, course C WHERE	R.COURSEID = C.COURSEID AND RO.COURSEID = R.COURSEID 	AND R.STATE = 1	GROUP BY C.COURSEID) AS STATE1,	(	SELECT	COUNT(STATE) FROM check_record R,course C WHERE R.COURSEID = C.COURSEID AND RO.COURSEID = R.COURSEID AND R.STATE = 2	GROUP BY C.COURSEID	) AS STATE2,	(SELECT	COUNT(STATE) FROM check_record R, course C WHERE R.COURSEID = C.COURSEID AND RO.COURSEID = R.COURSEID AND R.STATE = 0	GROUP BY 	C.COURSEID ) AS STATE0 FROM	course CO LEFT JOIN check_record RO ON CO.COURSEID = RO.COURSEID WHERE	CO.COURSEID IN (SELECT	COURSEID FROM	class_associate_course	WHERE	CLASSID IN "
					+ buffer.toString() + ")GROUP BY CO.COURSEID";
			preparedStatement = connection.prepareStatement(sql);
			set = preparedStatement.executeQuery();

			if (set != null) {
				while (set.next()) {
					RecordBo bo = new RecordBo();
					String COURSENAME = set.getString("COURSENAME");
					bo.setCourseName(COURSENAME);
					String TEACHERNAME = set.getString("TEACHERNAME");
					bo.setTeacher(TEACHERNAME);
					String ROOMID = set.getString("ROOMID");
					bo.setClassroom(ROOMID);
					String COURSERDATE = set.getString("COURSERDATE");
					bo.setTime(COURSERDATE);
					String STATE0 = set.getString("STATE0");

					bo.setAbsence((STATE0 != null ? STATE0 : 0 + ""));
					String STATE1 = set.getString("STATE1");
					bo.setSign((STATE1 != null ? STATE1 : 0 + ""));
					String STATE2 = set.getString("STATE2");
					bo.setVacation((STATE2 != null ? STATE2 : 0 + ""));
					bo.setUserId(userid);

					bos.add(bo);
				}

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.closeDB(connection, preparedStatement, set);
		}

		return bos;
	}

	public int updatePass(String userid, String pass) {
		int flag = -1;
		connection = DBUtil.getConnection();
		String sql = "UPDATE student SET PASSWORD=? WHERE USERID=?";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, pass);
			preparedStatement.setString(2, userid);
			flag = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.closeDB(connection, preparedStatement, set);
		}
		return flag;
	}

	/**
	 * 将bo对象转换成json字符串
	 * 
	 * @param username
	 * @param classname
	 * @return
	 */
	private String Bo2String(String username, String classname) {
		StudentBo bo = new StudentBo();
		bo.setUSERNAME(username);
		bo.setCLASSNAME(classname);
		String result = null;
		JSONObject object = JSONObject.fromObject(bo);
		result = object.toString();
		return result;
	}

	/**
	 * 初始化vercode表
	 * 
	 * @param phoneNum
	 */
	public void initCoder(String phoneNum, String verCode) {
		String sql = "INSERT INTO vercode(SHEETID,PHONENUM,VERCODE) VALUES(UUID(),?,?)";
		connection = DBUtil.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, phoneNum);
			preparedStatement.setString(2, verCode);
			preparedStatement.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.closeDB(connection, preparedStatement, null);
		}

	}

	/**
	 * 判断验证码是否正确，是则删除
	 * 
	 * @param phoneNum
	 * @param verCode
	 * @return
	 */
	public int deleteVerCode(String phoneNum, String verCode, String usernum, String newpass) {
		int flag = 0;
		String sql = "SELECT VERCODE FROM vercode WHERE PHONENUM=? AND VERCODE=?";
		connection = DBUtil.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, phoneNum);
			preparedStatement.setString(2, verCode);
			set = preparedStatement.executeQuery();
			if (set != null) {
				if (set.next()) {
					// 验证码正确
					StringBuffer buffer = new StringBuffer();
					buffer.append("DELETE FROM vercode WHERE PHONENUM='");
					buffer.append(phoneNum);
					buffer.append("' AND VERCODE='");
					buffer.append(verCode);
					buffer.append("'");
					preparedStatement.execute(buffer.toString());
					buffer.delete(0, buffer.length());
					buffer.append("SELECT * FROM  student WHERE USERID='");
					buffer.append(usernum);
					buffer.append("' AND PHONENUMBER='");
					buffer.append(phoneNum);
					buffer.append("'");
					set = preparedStatement.executeQuery(buffer.toString());
					if (set != null) {// 账号存在
						if (set.next()) {
							flag = 2;
							buffer.delete(0, buffer.length());
							buffer.append("UPDATE student SET PASSWORD='");
							buffer.append(newpass);
							buffer.append("' WHERE USERID='");
							buffer.append(usernum);
							buffer.append("' AND PHONENUMBER='");
							buffer.append(phoneNum);
							buffer.append("'");
							int n = preparedStatement.executeUpdate(buffer.toString());
						} else {
							flag = 3;// 账号不存在
						}
					}
				}

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.closeDB(connection, preparedStatement, set);
		}
		return flag;
	}

	public int modifyInfo(String userid, String pass, String phoneId, String phoneNum) {
		int flag = 0;
		String sql = "SELECT * FROM student WHERE USERID=? AND  PASSWORD=?";
		connection = DBUtil.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, userid);
			preparedStatement.setString(2, pass);
			set = preparedStatement.executeQuery();
			if (set != null) {
				if (set.next()) {
					StringBuffer buffer = new StringBuffer();
					buffer.append("UPDATE student SET USERID='");
					buffer.append(userid);
					if(phoneId!=null&&!"".equals(phoneId)){
					buffer.append("',PHONEID='");
					buffer.append(phoneId);
					}
					if(phoneNum!=null&&!"".equals(phoneNum)){
						buffer.append("',PHONENUMBER='");
						buffer.append(phoneNum);
						}
					buffer.append("' WHERE USERID='");
					buffer.append(userid);
					buffer.append("' AND PASSWORD='");
					buffer.append(pass);
					buffer.append("'");
					preparedStatement.execute(buffer.toString());
					flag=1;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return flag;

	}
}
