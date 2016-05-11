<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta>
<title>qrcode</title>
<script type="text/javascript" src="jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="jquery.qrcode.js"></script>
<script type="text/javascript" src="qrcode.js"></script>
<script src="assets/bootstrap/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="assets/bootstrap/css/bootstrap.min.css">
<%
	//result = "&uuid="+time+"&time="+rttime;
	String id = request.getParameter("id");
	String uuid = request.getParameter("uuid");
	String time = request.getParameter("time");
	String urlbase = request.getContextPath();
%>
</head>
<body>
	<div class="container">
		<div class="col-xs-12 col-md-12">
			<div class="col-xs-3 col-md-3"></div>
			<div id="qrcode" class="col-xs-5 col-md-5" style="margin-top: 50px;"
				onclick="changeQrcode();"></div>
			<div class="col-xs-4 col-md-4"></div>
		</div>
	</div>

	<script type="text/javascript">
	
		var urlbase ="<%=urlbase%>";
		var id ="<%=id%>";
		var uuid ="<%=uuid%>";
		var time ="<%=time%>";
		var xmlhttp;
		function initXmlHttp() {
			if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
				xmlhttp = new XMLHttpRequest();
			} else {// code for IE6, IE5
				xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
			}
		};
		//jQuery('#qrcode').qrcode("this plugin is great");
		jQuery('#qrcode').qrcode(
				{
					height : 400,
					width : 400,
					text : urlbase + "/sign?id=" + id + "&uuid=" + uuid
							+ "&time=" + time
				});

		function changeQrcode() {
			initXmlHttp();
			var url = "qrcode";

			var post = "type=change&id=" + id;
			post = encodeURI(post);
			post = encodeURI(post); //最重要的部分,两次调用encodeURI ,就是编码两次
			xmlhttp.open("POST", url, true);
			xmlhttp.onreadystatechange = changeQrcodeCallback;
			xmlhttp.setRequestHeader("Content-Type",
					"application/x-www-form-urlencoded");
			xmlhttp.send(post);

		};
		function changeQrcodeCallback() {
			if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {

				var result = xmlhttp.responseText;
				$('#qrcode').empty();
				jQuery('#qrcode').qrcode(
						{
							height : 400,
							width : 400,
							text : urlbase + "/sign?id=" + id + "&uuid="
									+ result + "&time=" + time
						});
			}

		};
	</script>
</body>

</html>