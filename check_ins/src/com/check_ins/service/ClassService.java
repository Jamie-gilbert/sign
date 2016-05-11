package com.check_ins.service;

import com.check_ins.dao.ClassDao;

public class ClassService {
	private static ClassDao dao;
	static {
		dao = new ClassDao();
	}
}
