package com.check_ins.servlet;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.check_ins.service.StudentService;

public class UpdatePassServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		String userid = req.getParameter("userid");
		String newPass = req.getParameter("newpass");
		int flag = StudentService.updatePass(userid, newPass);
		Writer writer = resp.getWriter();
		writer.write(flag + "");
		writer.flush();
		writer.close();
	};
}
