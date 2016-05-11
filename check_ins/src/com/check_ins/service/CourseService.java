package com.check_ins.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import utils.XmlUtils;

import com.check_ins.bo.CheckRecordBo;
import com.check_ins.bo.CourseBo;
import com.check_ins.bo.StudentBo;
import com.check_ins.dao.CourseDao;

public class CourseService {
	private static CourseDao dao;
	static {
		dao = new CourseDao();
	}

	public static void check(CheckRecordBo bo) {

	}

	public static String initRecord(String courseid) {
		String time = null;
		List<StudentBo> bos = StudentService.getStudents(courseid);
		CourseBo bo = dao.getCourse(courseid);
		List<CheckRecordBo> rbos = new ArrayList<CheckRecordBo>();
		for (StudentBo b : bos) {
			CheckRecordBo rbo = new CheckRecordBo();
			rbo.setCHECKDATE(bo.getCOURSERDATE());
			rbo.setCLASSID(b.getCLASSID());
			rbo.setCLASSROOM(bo.getCLASSROOM());
			rbo.setCOURSERID(courseid);
			rbo.setSTATE(0);
			rbo.setTEACHER(bo.getTEACHER());
			rbo.setUSERID(b.getUSERID());
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
			String df = format.format(date);
			time = df;
			rbo.setCHECKTIME(df);
			rbos.add(rbo);

		}
		dao.initSt_Data(rbos);
		return time;
	}

	public static String initCourse(String teacher) {
		String result = null;
		List<Map<String, String>> maps = dao.initCourse(teacher);
		result = XmlUtils.createXml(maps, "course");
		System.out.println(result);
		return result;
	}

	public static String addCourse(String teacher) {
		String result = null;
		List<Map<String, String>> maps = dao.addCourse(teacher);
		result = XmlUtils.createXml(maps, "course");
		System.out.println("111" + result);
		return result;
	}

	public static void updateUUID(String uuid, String courseid) {
		dao.updateUUID(uuid, courseid);
	}

	public static String getLastDate(String cid) {
		return dao.getLastDate(cid);
	}

	public static boolean updateRecord(String result) {
		String[] params = result.split(";");
		boolean flag = dao.updateRecord(params);
		return flag;

	}

	public static String getCourseRoom(String cid) {
		List<Map<String, String>> maps = dao.getCourseRoom(cid);
		String result = XmlUtils.createXml(maps, "room");
		return result;
	}

	public static String ScanSign(Map<String, String> map) {
		String result = null;
		int flag = dao.ScanSign(map);
		if (flag > 0) {
			result = "签到成功";
		} else if (flag == -2) {
			result = "二维码已更换，请点击二维码刷新";
		} else if (flag == 0) {
			result = "已签到";
		}

		else {
			result = "签到失败";
		}
		return result;
	}

	public static String getTime(String date) {
		List<String> list = dao.getTime(date);
		StringBuffer buffer = new StringBuffer();
		for (String s : list) {
			buffer.append(s);
			buffer.append(";");

		}
		return buffer.toString();
	}

	public static String getRoom(String date, String time) {
		List<Map<String, String>> list = dao.getRoom(date, time);
		StringBuffer buffer = new StringBuffer();
		for (Map<String, String> map : list) {
			String id = map.get("id");
			String name = map.get("name");
			buffer.append(id);
			buffer.append(",");
			buffer.append(name);
			buffer.append(";");

		}
		return buffer.toString();
	}

	public static String modifyCourse(String cid, String date, String time, String roomid) {
		String d = date + " " + time;
		int f = dao.modifyCourse(cid, d, roomid);
		return f + "";
	}

	public static void main(String[] args) {
		getTime("周四");
	}

}
