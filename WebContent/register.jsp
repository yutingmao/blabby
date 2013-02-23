<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>Blabby - Register </title>
	<meta http-equiv="X-UA-Compatible" content="IE=7"/>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel='stylesheet' href="css/global.css" type='text/css'/>
<link rel='stylesheet' href="css/index.css" type='text/css'/>
<script type="text/javascript" src="js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="js/main.js"></script>



</head>

<body class="register">

<div id="register">
	<h1><a href="" title="Powered by 1988"></a></h1>

	<div id="formh"></div>
	<form id="registerform" method="post">
		<p>
			<label>&nbsp;Username<br />
			<input type="text" id="user_name" name="name" class="input" maxlength="20" value=""/></label>
		</p>
		<p>
			<label>&nbsp;Password<br />
			<input type="password" id="user_pass" name="password" class="input" maxlength="15"/></label>
		</p>
		<p>
			<label>&nbsp;Re-input Password<br />
			<input type="password" id="user_rpass" class="input" maxlength="15"/></label>
		</p>
		<div class="submit">
			<div id="errd" class="errd" style="width: 185px;"><b id="err"></b></div>
			<div id="wait" class="waitmsg hidden"><img src="images/wait.gif"/><div class="ver_mid">　Please Waiting...</div></div>
			<div class="buttond">
				<input type="button" id="register_button" class="button-primary" value="Register" onclick="register();"/>
			</div>
		</div>
	</form>
	<div id="formb"></div>
	
	<div id="footer">
		&copy; 2013 @ Blabby
	</div>
</div>
<script type="text/javascript">
document.onkeydown = function(e){
    var e = e || window.event;
    if(e != $(".input")[0] && e.keyCode == 13){
        $("#register_button").click();
    }
}
</script>
</body>
</html>
