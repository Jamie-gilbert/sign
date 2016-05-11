package com.check_ins.servlet;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.omg.IOP.TAG_ORB_TYPE;

import com.check_ins.service.StudentService;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcFlowChargeProvinceRequest;
import com.taobao.api.request.AlibabaAliqinFcSmsNumQueryRequest;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

public class ForgetPassServlet extends HttpServlet {
	private String appkey = "23353707";
	private String secret = "43a61707c2725d26f6cc040927769fa8";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setCharacterEncoding("utf-8");
		req.setCharacterEncoding("utf-8");
		System.out.println("aaaaa");
		Writer writer = resp.getWriter();
		String type = req.getParameter("type");
		if ("getcode".equals(type)) {
			String phoneNum = req.getParameter("pnum");
			// 获取验证码
			String url = "http://gw.api.taobao.com/router/rest";
			TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
			AlibabaAliqinFcSmsNumSendRequest request = new AlibabaAliqinFcSmsNumSendRequest();
			request.setExtend("123456");
			request.setSmsType("normal");
			request.setSmsFreeSignName("变更验证");
			String verCode = StudentService.initCoder(phoneNum);
			StringBuffer json = new StringBuffer();
			json.append("{\"code\":\"");
			json.append(verCode);
			json.append("\",\"product\":\"sign\"}");
			request.setSmsParamString(json.toString());
			request.setRecNum(phoneNum);
			request.setSmsTemplateCode("SMS_8221006");
			try {
				AlibabaAliqinFcSmsNumSendResponse response = client.execute(request);
				System.out.println("aaa__" + response.getBody());
			} catch (ApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if ("vercode".equals(type)) {
			// 确定
			String usernum = req.getParameter("usernum");
			String vercode = req.getParameter("vercode");
			String pnum = req.getParameter("pnum");
			String newpass = req.getParameter("newpass");
			System.out.println(usernum);
			String reString = StudentService.deleteVerCode(pnum, vercode, usernum, newpass);
			writer.write(reString);

		} else if ("pinfo".equals(type)) {
			// 修改手机信息
			String userid = req.getParameter("userid");
			String pass = req.getParameter("pass");
			String phoneNum = req.getParameter("phoneNum");
			String pid = req.getParameter("pid");
			String result = StudentService.modifyInfo(userid, pass, pid, phoneNum);
			writer.write(result);

		}
		writer.flush();
		writer.close();
	}
}
