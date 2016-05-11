package com.check_ins.servlet;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.check_ins.service.CourseService;

public class ScanQrcodeServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		String cid = req.getParameter("cid");
		String uuid = req.getParameter("uuid");
		String userid = req.getParameter("userid");
		String time = req.getParameter("time");
		Map<String, String> map = new HashMap<>();
		map.put("userid", userid);
		map.put("time", time);
		map.put("courseid", cid);
		map.put("uuid", uuid);
		String result = CourseService.ScanSign(map);
		Writer writer = resp.getWriter();
		writer.write(result);
		writer.flush();
		writer.close();

	}
}
