<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="com.nineteeneightyeight.blog.BlogUser,com.nineteeneightyeight.manager.*,com.nineteeneightyeight.entity.*,com.nineteeneightyeight.util.*" %>
<html>
<head>
	<title>Blabby </title>
	<meta name="Description" content="迷你部落格,简单的交流平台"/>
	<meta name="Keywords" content="微博,微博客,交流,分享"/>
	<meta http-equiv="X-UA-Compatible" content="IE=7"/>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<link rel='stylesheet' href="/Microblog/css/global.css" type='text/css'/>
	<link rel='stylesheet' href="/Microblog/css/index.css" type='text/css'/>
	<script type="text/javascript" src="js/jquery-1.4.2.min.js"></script>
	<script type="text/javascript" src="js/main.js"></script>
	<script type="text/javascript" src="js/image.js"></script>
</head>

<body class="login">

<div id="login">
	<h1><a href="" title="Powered by 1988" ></a></h1>

	<div id="formh"></div>
	<form id="loginform" method="post">
		<p>
			<label>&nbsp;Username<br />
			<input type="text" id="user_name" class="input" name="name" maxlength="20" size="20"/></label>
		</p>
		<p>
			<label>&nbsp;Password<br />
			<input type="password" id="user_pass" class="input" name="password" maxlength="15" size="20"/></label>
		</p>
		<div class="submit">
			<div class="buttond">
				<input type="button" class="button-primary" style="_margin-right: 10px;" onclick="window.location.href = 'register';" value="Register"/>
			</div>	
			<div class="buttond"><input type="button" id="login_button" class="button-primary" value="Log in" onclick="login();"/></div>
		</div>
		<div id="errd" class="errd"><b id="err"></b></div>
	 	<div id="wait" class="waitmsg hidden"><img src="images/wait.gif"/><div class="ver_mid">　Waiting...</div></div>
	</form>
	<div id="formb"></div>
	
	<div id="user">
		<div class="user-tit"><span></span>They are all in Blabby...</div>
		<div id="user_main">
			<!--显示最受欢迎的用户头像和信息-->
			<table cellspacing="15">
<%
	UserManager userMan = new UserManager();
	List<BlogUser> userList = null;
	int i = 1; //控制每行的列数,默认每行显示5个用户

	userList = userMan.getUsers(10);
	
	for(BlogUser user : userList){
		if(i%5 == 1) { out.println("<tr>"); }
		out.println("<td>");
%>
		<a href="myblog?name=<%=user.getName()%>" target="_blank" title="<%=user.getRealname()%>">
			<img style="width:48px;height:48px;" src="<%=user.getPhotoUrl()%>"onload="shrinkImage(this,48,48);"/>
		</a>
<%
		out.println("</td>");
		if(i%5 == 0) { out.println("</tr>"); }
		i++;
	}
	if((i-1)%5 != 0) { out.println("</tr>"); }
%>
			</table>
		</div>
	</div>
</div>

<div id="talk">
	<div class="talk-tit"><span></span>My Blabby...</div>
    <div id="talk_main">
      <div id="talk_table">
      	<!--滚动显示最新发布的状态-->
      	<table>
<%
	StatusManager statusMan = new StatusManager();
	List<StatusEntity> statusList = statusMan.getStatuses(8);
	StatusParse parse = new StatusParse();
	
	for(StatusEntity status : statusList){
		BlogUser user = status.getPublisher();
		parse.setStatus(status.getContent());
%>
		<tr><td>
			<a href="myblog?name=<%=user.getName()%>" target="_blank">
				<img style="width:48px;height:48px;" src="<%=user.getPhotoUrl()%>" onload="shrinkImage(this,48,48);"/>
			</a>
			<div class="talk-con">
				<a href="myblog?name=<%=user.getName()%>" target="_blank"><%=user.getRealname()%></a>: <p><%=parse.parse()%></p>
			</div>
			<div class="talk-time"><%=status.getPublishDate()%></div>
		</td></tr>
<%	} %>
		</table>
      </div>
    </div>
</div>

<div id="footer">
&copy; 2013 @ Babbly
</div>
<script type="text/javascript">
document.onkeydown = function(e){
    var e = e || window.event;
    if(e != $(".input")[0] && e.keyCode == 13){
        $("#login_button").click();
    }
}
</script>
</body>
</html>