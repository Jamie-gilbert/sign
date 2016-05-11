package com.check_ins.service;

import com.check_ins.dao.TeacherDao;

public class TeacherService {
	private static TeacherDao dao;
	static {
		dao = new TeacherDao();
	}

	public static String login(String username, String pass) {
		String flag = dao.login(username, pass);
		return flag;
	}

}
