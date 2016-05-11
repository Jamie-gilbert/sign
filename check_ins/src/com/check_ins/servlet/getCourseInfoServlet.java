package com.check_ins.servlet;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.check_ins.service.CourseService;

public class getCourseInfoServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");
		String type = req.getParameter("type");
		Writer writer = resp.getWriter();
		String result = "";
		if ("inittime".equals(type)) {
			String week = req.getParameter("week");
			result = CourseService.getTime(week);

			System.out.println("2222" + result);
		} else if ("initroom".equals(type)) {
			String time = req.getParameter("time");
			String date = req.getParameter("date");
			result = CourseService.getRoom(date, time);
		} else if ("createdata".equals(type)) {
			String week = req.getParameter("week");
			String time = req.getParameter("time");
			String room = req.getParameter("room");
			String cid = req.getParameter("cid");
			result = CourseService.modifyCourse(cid, week, time, room);
		}

		writer.write(result);

		writer.flush();
		writer.close();
	}
}
