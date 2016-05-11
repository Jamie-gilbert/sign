<!DOCTYPE html>
<html>
<head>
<title>basic example</title>
</head>
<body>
	<script type="text/javascript" src="jquery-1.9.1.min.js"></script>
	<script type="text/javascript" src="jquery.qrcode.js"></script>
	<script type="text/javascript" src="qrcode.js"></script>


	<div id="qrcodeTable"></div>
	<!--<div id="qrcodeCanvas"></div>  -->
	<script type="text/javascript">
		var info = "http://jetienne.com";
		//jQuery('#qrcode').qrcode("this plugin is great");
		jQuery('#qrcodeTable').qrcode({
			render : "table",
			text : info
		});

		/* jQuery('#qrcodeCanvas').qrcode({
			text	: info
		});	 */
	</script>

</body>
</html>
