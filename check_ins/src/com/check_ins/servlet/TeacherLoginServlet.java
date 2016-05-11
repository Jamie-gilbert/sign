package com.check_ins.servlet;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.check_ins.service.TeacherService;

public class TeacherLoginServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");
		String username = req.getParameter("username");
		String pass = req.getParameter("pass");
		String flag = TeacherService.login(username, pass);
		String result = null;
		if (flag != null) {
			result = flag;

		} else {
			result = "0";
		}
		Writer writer = resp.getWriter();
		writer.write(result);
		writer.flush();
		writer.close();

	}
}
