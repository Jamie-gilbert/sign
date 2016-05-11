<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta>
<title>count</title>

<script type="text/javascript" src="jquery-1.9.1.min.js"></script>
<script src="assets/bootstrap/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="assets/bootstrap/css/bootstrap.min.css">
<!-- Bootstrap Core CSS -->
<link href="css/bootstrap.min.css" rel="stylesheet">

<!-- Custom CSS -->
<link href="css/sb-admin.css" rel="stylesheet">

<!-- Morris Charts CSS -->
<link href="css/plugins/morris.css" rel="stylesheet">

<!-- Custom Fonts -->
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet"
	type="text/css">
<!-- <link href="bootstrap/bootstrap-select.css" rel="stylesheet"
	type="text/css"> -->
<%
	String cid = request.getParameter("cid");
%>
<script type="text/javascript">
	var xmlhttp;
	var pid;
	var count=0,askcount=0;
	
	function initXmlHttp() {
		if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
			xmlhttp = new XMLHttpRequest();
		} else {// code for IE6, IE5
			xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
	};
	function getData(cid) {
		initXmlHttp();
		
		var url = "qrcode";

		var post = "type=getData&cid=" + cid;
		post = encodeURI(post);
		post = encodeURI(post); //最重要的部分,两次调用encodeURI ,就是编码两次
		xmlhttp.open("POST", url, true);
		xmlhttp.onreadystatechange = getDataCallback;
		xmlhttp.setRequestHeader("Content-Type",
				"application/x-www-form-urlencoded");
		xmlhttp.send(post);

	};
	function getDataCallback() {
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {

			var result = xmlhttp.responseXML;
			//var courseCell = document.getElementById("course");
			var student = result.documentElement
					.getElementsByTagName("student");
			count=student.length;
			for (var i = 0; i < count; i++) {
				var username = student[i].getElementsByTagName("username");
				var uname = username[0].firstChild.nodeValue;
				var classname = student[i].getElementsByTagName("classname");
				var cname = classname[0].firstChild.nodeValue;
				var userid = student[i].getElementsByTagName("userid");
				var uid = userid[0].firstChild.nodeValue;
				var sheetid = student[i].getElementsByTagName("sheetid");
				var sid = sheetid[0].firstChild.nodeValue;
				var div = "<div class='col-xs-3 col-md-2' style='margin-bottom: 10px;'> <div class='panel panel-default' ";
				div += "style='height: 99.8%; text-align: center;margin: auto;'>";
				div += "<div class='panel-heading' style='background-color: #2114EA'>";
				div += " <h3 class='panel-title'> <strong style='color: #FFFFFF;'>"+ uname;
				div += "</strong> </h3></div><div class='panel-body' style='height: 99.8%; background-color: #FFFFFF;'>";
				div += "<div class='col-xs-12 col-md-12' style='height: 99.8%;'> <h3> <strong style='color: #000000;'>";
				div += uid + "</strong></h3><input style='visibility: hidden;' value='"+sid+"' id='sheetid"+i+"'/></div>";
				div += "<div class='col-xs-12 col-md-12' style='height: 99.8%;'> <h3> <strong style='color: #000000;'>";
				div += cname + "</strong></h3></div>";
				div += " <div class='col-xs-12 col-md-12' style='height: 99.8%;'>";
				div += "<select id='state"+i+"' class='form-control' onchange='getcount_absenteeism(this)' style='text-align: center;margin: auto;'>";
				div += "<option value='0'>旷课</option> <option value='2'>请假</option> <option value='1'>已到</option></select></div></div></div></div>";
				$('#student_container').append(div);
			}
			$('#count_absenteeism')[0].innerText=count;
			$('#count_ask')[0].innerHTML=askcount;

		}

	};
	function getcount_absenteeism(selectobj) {
		
		
		
		var v= selectobj.value;
		if(v==0)
			{

			if(askcount>0)
				{
				askcount--;
				}

			}else if(v==2)
				{
			
					
					if(askcount<count)
						{
						askcount++;
						}
					
				}
		
		$('#count_absenteeism')[0].innerText=count-askcount;
		$('#count_ask')[0].innerHTML=askcount;
		
		
	};
function makeSure() {

	var result="";
		for(var i=0;i<count;i++)
			{
			 var sheetid=$('#sheetid'+i).val();
			 var state=$('#state'+i).val();
			 result+=sheetid+","+state+";";
			}
		$.ajax({
			type:'post',
			url:'qrcode',
			data:{'type':'makesure','result':result},
			async:false,
			success:function(r){
				
				if("1"==r)
					{
					alert("提交成功");
					
					}else{
						
						alert("提交失败");
					}
				
			},
			error:function(e){
				
			}
		});
		
	};
	
	</script>
</head>
<body style="background: #FFFFFF;" onload="getData(<%=cid%>)">

	<div class="container-fluid" style="margin-top: -50px;">
		<div class="row" style="background-color: blue; margin-bottom: 5px;">
			<div class="col-xs-6 col-md-6" style="text-align: right;">
				<h4>
					<font color="#FFFFFF">旷课人数：</font><font color="#FF0310"
						id="count_absenteeism"></font>
				</h4>
			</div>
			<div class="col-xs-6 col-md-6" style="text-align: left;">
				<h4>
					<font color="#FFFFFF">请假人数：</font><font color="#FF0310"
						id="count_ask"></font>
				</h4>
			</div>
		</div>
		<div class="row" id="student_container"></div>
		<div class="row">
			<div class="col-xs-12 col-md-12"
				style="text-align: center; margin-left: auto; margin-right: auto; margin-top: 50px;">
				<button id="btn_submit" class="btn btn-primary"
					 onclick="makeSure();"
					style="width: 20%; padding-top: 5px; padding-bottom: 5px; padding-left: 10px; padding-right: 10px;">
					<h3>确定</h3>
				</button>

			</div>
		</div>
	</div>
</body>
</html>