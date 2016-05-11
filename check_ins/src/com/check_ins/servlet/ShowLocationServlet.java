package com.check_ins.servlet;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.check_ins.service.CourseService;
import com.check_ins.service.StudentService;

public class ShowLocationServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setCharacterEncoding("utf-8");
		req.setCharacterEncoding("utf-8");
		String result="";
		String type = req.getParameter("type");
		if("none".equals(type))
		{
		String []data=	req.getParameterValues("data");
		if(data!=null&&data.length>0)
		{
			
		}
		}else if("all".equals(type)){
		String courseid = req.getParameter("courseid");
		 String room = CourseService.getCourseRoom(courseid);
		 String location=StudentService.getStudentLocation(courseid);
		 result=room+";;;"+location;
		 System.out.println(result);
		
		}
		Writer writer = resp.getWriter();
		writer.write(result);
		writer.flush();
		writer.close();
	}
}
