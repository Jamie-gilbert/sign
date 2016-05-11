<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta>
<title>AddCourse</title>
<script type="text/javascript" src="jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="jquery.qrcode.js"></script>
<script type="text/javascript" src="qrcode.js"></script>
<script src="assets/bootstrap/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="assets/bootstrap/css/bootstrap.min.css">
<%
	String cid = request.getParameter("cid");
	String cname = new String(request.getParameter("cname").getBytes("ISO-8859-1"), "UTF-8");
%>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-xs-6 col-md-3" style="margin-top: 10px;">
				<div class="panel panel-default"
					style="height: 99.8%; text-align: center; margin: auto;">
					<div class="panel-heading" style="background-color: #2114EA;">
						<h3 class="panel-title">
							<strong style="color: #FFFFFF;">课程名称</strong>
						</h3>
					</div>
					<div class="panel-body"
						style="background-color: #FFFFFF; height: 99.8%;">
						<div class="col-xs-12 col-md-12" style="height: 99.8%;">
							<h3>
								<strong style="color: #000000;"><%=cname%></strong>
							</h3>
						</div>
					</div>
				</div>
			</div>
			<div class="col-xs-6 col-md-3" style="margin-top: 10px;">
				<div class="panel panel-default"
					style="height: 99.8%; text-align: center; margin: auto;">
					<div class="panel-heading" style="background-color: #2114EA;">
						<h3 class="panel-title">
							<strong style="color: #FFFFFF;">日期</strong>
						</h3>
					</div>
					<div class="panel-body"
						style="background-color: #FFFFFF; height: 99.8%;">
						<div class="col-xs-12 col-md-12" style="height: 99.8%;">
							<h3>
								<select class="form-control" id="week"
									onchange="weekSelected(this);"
									style="text-align: center; margin: auto;">
									<option id="empty"></option>
									<option id="Mon">周一</option>
									<option id="Tus">周二</option>
									<option id="Wen">周三</option>
									<option id="Thu">周四</option>
									<option id="Fri">周五</option>
									<option id="Sat">周六</option>
									<option id="Sun">周日</option>

								</select>
							</h3>
						</div>
					</div>
				</div>
			</div>
			<div class="col-xs-6 col-md-3" style="margin-top: 10px;">
				<div class="panel panel-default"
					style="height: 99.8%; text-align: center; margin: auto;">
					<div class="panel-heading" style="background-color: #2114EA;">
						<h3 class="panel-title">
							<strong style="color: #FFFFFF;">时间</strong>
						</h3>
					</div>
					<div class="panel-body"
						style="background-color: #FFFFFF; height: 99.8%;">
						<div class="col-xs-12 col-md-12" style="height: 99.8%;">
							<h3>
								<select class="form-control" id="class"
									onchange="classSelect(this);"
									style="text-align: center; margin: auto;">
									<option id="empty"></option>
									<option id="first">1,2节</option>
									<option id="second">3,4节</option>
									<option id="third">5,6节</option>
									<option id="fourth">7,8节</option>
									<option id="fifth">9,10节</option>
								</select>
							</h3>
						</div>
					</div>
				</div>
			</div>
			<div class="col-xs-6 col-md-3" style="margin-top: 10px;">
				<div class="panel panel-default"
					style="height: 99.8%; text-align: center; margin: auto;">
					<div class="panel-heading" style="background-color: #2114EA;">
						<h3 class="panel-title">
							<strong style="color: #FFFFFF;">教室</strong>
						</h3>
					</div>
					<div class="panel-body"
						style="background-color: #FFFFFF; height: 99.8%;">
						<div class="col-xs-12 col-md-12" style="height: 99.8%;">
							<h3>
								<select class="form-control" id="room"
									style="text-align: center; margin: auto;">
									<option id="empty"></option>

								</select>
							</h3>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12 col-md-12"
				style="text-align: center; margin-left: auto; margin-right: auto; margin-top: 50px;">
				<button id="btn_submit" class="btn btn-primary"
					onclick="makeSure();"
					style="width: 20%; padding-left: 10px; padding-right: 10px;">
					<h3>确定</h3>
				</button>

			</div>
		</div>
	</div>

	<script type="text/javascript">
		var cid='<%=cid%>';
		//选择在星期几上课，并选择出未安排课的课次
		function weekSelected(weekobj) {
			var week = weekobj.value;
			$.ajax({
				type : 'post',
				url : 'getcourseinfo',
				data : {
					'type' : 'inittime',
					'week' : week
				},
				async : false,
				success : function(result) {

					var list = new Array();
					if (result != null && "" != result) {
						list = result.split(";");
						$('#class').empty();
						var option = "<option id='empty'></option>"
						for (i = 0; i < list.length; i++) {
							if ("" != list[i] && list[i] != null) {
								var time = list[i];
								option += "<option id='time"+i+"'>" + time
										+ "</option>";
							}
						}
						$('#class').append(option);
					}

				},
				error : function(e) {

				}

			});

		};
		//选择在第几课上课，并选择出未安排课的教室
		function classSelect(classobj) {

			var wvalue = $('#week')[0].value;
			if (wvalue == null) {
				alert("请先选择上课日期");

			} else {

				var tvalue = $('#class')[0].value;
				$.ajax({
					type : 'post',
					url : 'getcourseinfo',
					data : {
						'type' : 'initroom',
						'date' : wvalue,
						'time' : tvalue
					},
					async : false,
					success : function(result) {

						var list = new Array();
						if (result != null && "" != result) {
							list = result.split(";");
							$('#room').empty();
							var option = "<option id='empty'></option>"
							for (i = 0; i < list.length; i++) {
								if ("" != list[i] && list[i] != null) {
									var room = list[i];
									var rs = new Array();
									rs = room.split(",");
									option += "<option id='"+rs[0]+"'>" + rs[1]
											+ "</option>";
								}
							}
							$('#room').append(option);
						}

					},
					error : function(e) {

					}

				});
			}
		};
		//确定按钮点击事件
		function makeSure() {
			debugger
			var week = $('#week')[0].value;
			var time = $('#class')[0].value;
			var room = $('#room')[0].selectedOptions[0].id;
			if (week == null || week == "" || time == null || time == ""
					|| room == null || room == "") {
				alert("日期,时间，教室不能为空")
			} else {

				$.ajax({
					type : 'post',
					url : 'getcourseinfo',
					data : {
						'type' : 'createdata',
						'week' : week,
						'time' : time,
						'room' : room,
						'cid' : cid
					},
					async : false,
					success : function(result) {
						if (result > 0) {
							alert('修改成功');
						}else{
							alert('课程已修改');
						}
					},
					error : function(e) {

					}

				});
			}
		};
	</script>
</body>

</html>