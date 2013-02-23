<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
<%@ page import="com.nineteeneightyeight.blog.BlogUser,com.nineteeneightyeight.manager.*,com.nineteeneightyeight.entity.*,com.nineteeneightyeight.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=7"/>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="Keywords" content="微博,微博客,交流,分享"/>
	<meta http-equiv="Pragma" content="no-cache"/>
	<meta http-equiv="Cache-Control" content="no-cache"/>
	<title>Babbly </title>
	<link rel="stylesheet" href="/Microblog/css/global.css" type="text/css"/>
	<link rel="stylesheet" href="/Microblog/css/user.css" type="text/css"/>
	<script type="text/javascript" src="js/jquery-1.4.2.min.js"></script>
	<script type="text/javascript" src="js/image.js"></script>
	<script type="text/javascript" src="js/status.js"></script>
	<script type="text/javascript" src="js/main.js"></script>
  	<script type='text/javascript' src="dwr/engine.js"></script>
  	<script type='text/javascript' src="dwr/util.js"></script>	
	<script type="text/javascript">
		$(document).ready(function(){
			$("#publishtext").mousedown(function() {
				// 点击文本域时隐藏表情
				$("#emotion").addClass("hidden");
			});
		});
		dwr.engine.setActiveReverseAjax(true);
	</script>
</head>
<%
	//显示当前登录用户的信息,直接从session中获取对象
	UserManager userMan = new UserManager();
	BlogUser currUser = (BlogUser) request.getSession().getAttribute(ConstantUtil.CURRENT_USER);
	if(currUser == null){
		currUser = new BlogUser();
	}
%>
<body onload="getStatus();">
<div id="blog_user_name" class="hidden"><%=currUser.getName()%></div>
<div id="wrapper">	
		<div id="topbar">
			<div id="user_page">
				<a href="myhome">My Home</a> | 
				<a href="myblog/<%=currUser.getName()%>">My Babbly</a> | 
				<a href="myalbum/<%=currUser.getName()%>">My Album</a> | 
				<a href="plaza">Discovery</a> | 
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
<div class="posttop post"></div>
<div id="postbox" class="post">
	<form id="new_post" class="newpost-hack" method="post">
		<img src="<%=currUser.getPhotoUrl()%>" class='avatar' onload="shrinkImage(this,48,48);"/>
		<div id="waitpublish" class="waitmsg hidden"><img src="images/wait.gif"/><div class="ver_mid">　Sending...</div></div>
		<div id="box_main1">
			<label for="posttext">Say Something!</label>
				<div class="postfunc">
					<input type="button" class="button-plane" style="position: relative;" 
							onclick="showEmotion('emotion');" value="icon"/>
				</div><br/>
			<textarea name="status" id="publishtext" class="post_area" rows="3" cols="60" onpropertychange="countStatusChar(this,'140')"></textarea>
			<input type="hidden" name="p_type" value="no_img"/>
			<input type="button" class="button-primary hidden" style="position: relative; bottom: 43px; right: -2px;" value="发图片"/>
			<input type="button" class="button-primary float-right" value="Tweet" 
				   style="position: relative; bottom: 0px; right: 20px;" onclick="publishStatus('<%=currUser.getId()%>');"/>
		</div>
		<div id="showmsg" class="hidden" style="position: relative;bottom:5px;right:-350px;"></div>
	</form>
	<div id="emotion" class="emotion hidden"></div>
	</div> <!-- // postbox -->
<div class="postbtm post"></div>
<div id="countBody">140</div>

<div id="main">
	<h2>What's new</h2>
	<!--显示登录用户及其关注好友的状态,使用AJAX-->
	<div id="status_range" value="all"></div>
	<div id="user_id" value="<%=currUser.getId()%>"></div>
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