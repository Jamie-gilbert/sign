package com.check_ins.service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Random;

import utils.XmlUtils;

import com.check_ins.bo.LocationBo;
import com.check_ins.bo.RecordBo;
import com.check_ins.bo.StudentBo;
import com.check_ins.dao.StudentDao;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class StudentService {
	private static StudentDao dao;
	static {
		dao = new StudentDao();
	}

	/**
	 * 根据课程获取学生信息
	 * 
	 * @param courseid
	 * @return
	 */
	public static List<StudentBo> getStudents(String courseid) {
		List<StudentBo> bos = dao.getStudents(courseid);
		return bos;
	}

	/**
	 * 根据课程和签到日期获未签到学生
	 * 
	 * @param courseid
	 * @param scanDate
	 * @return
	 */
	public static String getAbsenteeismer(String courseid, String scanDate) {
		String result = null;
		List<Map<String, String>> maps = dao.getAbsenteeismer(courseid, scanDate);
		result = XmlUtils.createXml(maps, "student");
		System.out.println(result);
		return result;
	}

	/**
	 * 登陆
	 * 
	 * @param studentId
	 * @param passWord
	 * @param phoneNumber
	 * @param phoneId
	 * @return
	 */
	public static String login(String studentId, String passWord, String phoneNumber, String phoneId) {
		String flag = dao.login(studentId, passWord, phoneNumber, phoneId);
		return flag;
	}

	public static void updateLocation(String data) {

		JSONObject object = JSONObject.fromObject(data);
		LocationBo bo = (LocationBo) JSONObject.toBean(object, LocationBo.class);
		dao.updateLocation(bo);

	}

	public static String getStudentLocation(String courseid) {
		String result = null;
		List<Map<String, String>> maps = dao.getStudentLocation(courseid);
		result = XmlUtils.createXml(maps, "location");

		return result;
	}

	/**
	 * 获取用户的签到记录
	 * 
	 * @param userid
	 * @return
	 */
	public static String getRecordData(String userid) {
		String result = null;
		List<RecordBo> bos = dao.getRecordData(userid);
		JSONArray array = JSONArray.fromObject(bos);
		result = array.toString();
		return result;
	}
	public static String modifyInfo(String userid,String pass,String phoneId,String phoneNum) {
		String result=null;
		result=dao.modifyInfo(userid, pass, phoneId, phoneNum)+"";
		System.out.println(result);
		return result;
	}

	/**
	 * 判断验证码是否正确
	 * 
	 * @param phoneNum
	 * @param verCode
	 * @return
	 */
	public static String deleteVerCode(String phoneNum, String verCode, String usernum, String newpass) {
		String result =null;
	 result= dao.deleteVerCode(phoneNum, verCode,usernum,newpass)+"";
		return result;
	}
/**
 * 初始化验证码
 * @param phoneNum
 * @return
 */
	public static String initCoder(String phoneNum) {
		StringBuffer verCode = new StringBuffer();
		Random random = new Random();
		int i = 0;
		while (i < 4) {
			int r = random.nextInt(10);
			verCode.append(r);
			i++;
		}
		dao.initCoder(phoneNum, verCode.toString());
		return verCode.toString();
	}

	public static void main(String[] args) {
		System.out.println(getRecordData("1208010101"));
	}

	public static int updatePass(String userid, String newPass) {
		int result = -1;
		result = dao.updatePass(userid, newPass);
		return result;
	}
}
