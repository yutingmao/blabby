<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.nineteeneightyeight.blog.BlogUser,com.nineteeneightyeight.manager.*,com.nineteeneightyeight.entity.*,com.nineteeneightyeight.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
	//显示某用户的信息及其发布的状态,name为所要显示信息的用户名
	String name = request.getParameter("name");
	UserManager userMan = new UserManager();
	BlogUser user = null;
	BlogUser currUser = (BlogUser) request.getSession().getAttribute(ConstantUtil.CURRENT_USER);
	//如果当前用户不存在,则新建一个空对象以防止空异常
	if(currUser == null){
		currUser = new BlogUser();
	}
	//所要查看的用户的信息,此处的对象不会为null
	user = userMan.getUserByName(name);
%>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=7"/>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="Description" content="<%=user.getRealname()%>的微博"/>
	<meta name="Keywords" content="微博,微博客,交流,分享"/>
	<meta http-equiv="Pragma" content="no-cache"/>
	<meta http-equiv="Cache-Control" content="no-cache"/>
	<title>Babbly</title>
	<link rel="stylesheet" href="/Microblog/css/global.css" type="text/css"/>
	<link rel="stylesheet" href="/Microblog/css/user.css" type="text/css"/>
	<script type="text/javascript" src="js/jquery-1.4.2.min.js"></script>
	<script type="text/javascript" src="js/image.js"></script>
	<script type="text/javascript" src="js/status.js"></script>
	<script type="text/javascript" src="js/main.js"></script>
	<script type='text/javascript' src="dwr/engine.js"></script>
  	<script type='text/javascript' src="dwr/util.js"></script>
  	<script type="text/javascript">
		dwr.engine.setActiveReverseAjax(true);
	</script>
</head>
<body onload="getStatus();">
<div id="blog_user_name" class="hidden"><%=currUser.getName()%></div>
<div id="wrapper">	
		<div id="topbar">
			<div id="user_page">
				<a href="myhome">My Home</a> | 
				<a	href="myblog?name=<%=currUser.getName()%>">My Babbly</a> | 
				<a href="myalbum.jsp?name=<%=currUser.getName()%>">My Album</a> | 
				<a href="plaza.jsp">Discovery</a> | 
				<a href="lookaround">Look Around</a>
			</div>
			<div id="user_func">
				<a href="setup">Setup</a>　|　
				<a href="UserAction?action=logout">Log off</a>
			</div>
		</div>
	<h1><a href="" title="Powered by 2013"></a></h1>
	<div id="mbody_top"></div>
	<div id="mbody">
	<div style="height: 1px;"></div>
<div id="main">
	<div id="user_info">
		<img src="<%=user.getPhotoUrl()%>" class="img_fra avatar-160 none_hover" style="float: left;" onload="shrinkImage(this,160,160);"/>
		<div class="user_info">
			<div style="height: 15px;">
				<div class="float-left"><%=user.getRealname()%>&nbsp;</div>
				<div style="float:left; bottom:-120px; position:relative; left:0px;">
					<span id="concern-f">

						<input type="button" class="button-plane" onclick="addAttention('<%=currUser.getId()%>','<%=user.getId()%>');" value="添加关注"/>

						<input type="button" class="button-plane" onclick="cancelAttention('<%=currUser.getId()%>','<%=user.getId()%>');" value="取消关注"/>

					</span>
				</div>
			</div><br/>
			<div class="info">
				<a href="myblog.jsp?name=<%=user.getName()%>">http://www.microblog.com/<%=user.getName()%></a><br/><br/>
				<div class="sex <%=user.getGender()==1?"female":"male"%>" style="margin-left:170px "></div>
				<div class="info_place"><%=user.getAddress().isEmpty()?"未知":user.getAddress()%></div><br/>
				<div class="intro"><%=user.getBrief().isEmpty()?"未填写个人简介":user.getBrief()%></div>
			</div>
		</div>
	</div>
	<h2>最新动态</h2>
	<!--显示登录用户的状态,使用AJAX-->
	<div id="status_range" value="onlyme"></div>
	<div id="user_id" value="<%=user.getId()%>"></div>
	<div id="status_board"></div>
	<div id="index" value="1"></div><div id="page_size" value="10"></div>
	<div id="page_button"></div>
</div> <!-- // main -->

<div id="sidebar">
	<div id="sidebar-top"></div>
<ul>
	<li class="fir">
		<img src="<%=currUser.getPhotoUrl()%>" onload="shrinkImage(this,48,48);" class="img_fra none_hover avatar-48 float-left"/>
		<span class="bold">&nbsp;&nbsp;&nbsp;<%=currUser.getRealname()%></span>
		<br/><br/>　<%=currUser.getAddress().isEmpty()?"Unknown":currUser.getAddress()%>
		<div class="sta">
			<div class="concern_c">
				<span class="bold">&nbsp;&nbsp;<%=userMan.getAttentionsOf(currUser)%></span><br/>
				<a href="attentions.jsp?name=<%=currUser.getName()%>">Followings</a>
			</div>
			<div class="concern_c">
				<span class="bold">&nbsp;&nbsp;<%=userMan.getFansOf(currUser)%></span><br/>
				<a href="fans.jsp?name=<%=currUser.getName()%>">Followers</a>
			</div>
			<div class="concern_c">
				<span class="bold">&nbsp;&nbsp;<%=userMan.getPopularsOf(currUser)%></span><br/>
				<a href="#">Popularity</a>
			</div>
			<div class="message_c">
				<span class="bold">&nbsp;&nbsp;<%=userMan.getActivesOf(currUser)%></span><br/>
				<a href="#">Activity</a>
			</div>
		</div>
	</li>
	<li class="side_tit">My Music</li>
	<li>
		<embed src="<%=currUser.getMusicUrl()%>" width="200" height="25" type="audio/mpeg" loop="-1" autostart="true" volume="0">
	</li>
	<li class="side_tit">My Broadcast</li>
	<li id="my_broadcast" style="height: 265px;">
		<div id="broadcastbox" class="post">
			<textarea name="msg" id="broadcasttext" class="broadcast_area" rows="3" cols="20"></textarea>
			<input type="submit" class="button-primary float-right" onclick="sendMessage();" value="Broadcast"/>
		</div>
	</li>
</ul>
	<div id="sidebar-bottom"></div>
</div> <!-- // sidebar -->
<div class="blank"><a href="#">Return to the top</a></div>
	</div>
	<div id="mbody_bottom"></div>
<div id="footer">
&copy; 2013 @ My Babbly
</div>
</div> <!-- // wrapper -->
</body>
</html>