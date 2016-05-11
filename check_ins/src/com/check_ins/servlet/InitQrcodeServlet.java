package com.check_ins.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.check_ins.service.CourseService;
import com.check_ins.service.StudentService;

public class InitQrcodeServlet extends HttpServlet {

	public String cid = null;
	private Timer timer = null;
	private String date;

	@Override
	public void init() throws ServletException {

		timer = new Timer();
		setTimer();

	}

	@Override
	public void destroy() {
		if (timer != null)
			timer.cancel();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");

		String type = req.getParameter("type");
		System.out.println(type+"---22222");
		String result = null;
		PrintWriter writer = resp.getWriter();

		if ("initCourse".equals(type)) {

			String teacher = req.getParameter("teacher");
			teacher = URLDecoder.decode(teacher, "utf8");
			System.out.println(teacher);
			result = CourseService.initCourse(teacher);
		} else if ("initQrcode".equals(type)) {
			String courseid = req.getParameter("courseid");
			courseid = URLDecoder.decode(courseid, "utf8");
			String rttime = CourseService.initRecord(courseid);
			Date d = new Date();
			Long time = d.getTime();
			result = "&uuid=" + time + "&time=" + rttime;
			cid = courseid;
			updateUUID(time + "");

		} else if ("end".equals(type)) {
			// 关闭二维码页面
			if (timer != null)
				timer.cancel();

		} else if ("change".equals(type)) {
			// 点击二维码刷新二维码
			Date d = new Date();
			Long time = d.getTime();
			String courseid = req.getParameter("id");
			courseid = URLDecoder.decode(courseid, "utf8");
			cid = courseid;
			result = time + "";
			updateUUID(time + "");

		} else if ("getData".equals(type)) {
			String cid = req.getParameter("cid");

			String scandate = CourseService.getLastDate(cid);
			result = StudentService.getAbsenteeismer(cid, scandate);

		} else if ("makesure".equals(type)) {
			String reStr = req.getParameter("result");
			boolean flag = CourseService.updateRecord(reStr);
			if (flag) {
				result = "1";
			} else {
				result = "0";
			}
		} else if ("addcourse".equals(type)) {
			System.out.println("11111111");
			String teacher = req.getParameter("teacher");
			teacher = URLDecoder.decode(teacher, "utf8");
			System.out.println(teacher);
			result = CourseService.addCourse(teacher);
			System.out.println(result);
		}

		writer.write(result);
		writer.flush();
		writer.close();

	}

	/**
	 * 将生成的二维码的uuid存入数据库
	 * 
	 * @param uuid
	 */
	private void updateUUID(String uuid) {
		CourseService.updateUUID(uuid, cid);

	}

	/**
	 * 二维码更换计时器
	 */
	private void setTimer() {
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				date = new Date().getTime() + "";
				updateUUID(date);

			}
		}, 1000, 1000 * 15);

	}

}
