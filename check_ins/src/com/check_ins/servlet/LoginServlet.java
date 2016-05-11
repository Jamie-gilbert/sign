package com.check_ins.servlet;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.check_ins.service.StudentService;

public class LoginServlet extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String num = null;
		String pass = null;
		String phoneNum=null;
		String phoneId=null;
		resp.setCharacterEncoding("utf-8");
		num = req.getParameter("n");
		pass = req.getParameter("p");
		phoneNum=req.getParameter("pn");
		phoneId=req.getParameter("pid");
		String flag=StudentService.login(num, pass, phoneNum, phoneId);
		System.out.println(flag);
		System.out.println(phoneId);
		Writer writer=resp.getWriter();
		writer.write(flag+"");
		writer.flush();
		writer.close();
		
	}
}
