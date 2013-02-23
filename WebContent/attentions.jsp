<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
<%@ page import="com.nineteeneightyeight.blog.BlogUser,com.nineteeneightyeight.manager.*,com.nineteeneightyeight.entity.*,com.nineteeneightyeight.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!--显示用户关注的用户信息-->
<html>
<%
	//显示某用户所关注的用户,name为所要显示信息的用户名
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
	List<BlogUser> userList = null;
	
	userList = userMan.getFriends(user);
%>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=7"/>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="Description" content="<%=user.getRealname()%>'s Babbly"/>
	<meta name="Keywords" content="微博,微博客,交流,分享"/>
	<meta http-equiv="Pragma" content="no-cache"/>
	<meta http-equiv="Cache-Control" content="no-cache"/>
	<title>Babbly- <%=user.getRealname()%>'s Following</title>	
	<link rel="stylesheet" href="css/global.css" type="text/css"/>
	<link rel="stylesheet" href="css/user.css" type="text/css"/>
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
<body>
<div id="blog_user_name" class="hidden"><%=currUser.getName()%></div>
	<div id="wrapper">
		<div id="topbar">
			<div id="user_page">
				<a href="myhome">Home</a>　|　
				<a href="myblog?name=<%=currUser.getName()%>">My Blabby</a>　|　
				<a href="myalbum?name=<%=currUser.getName()%>">My album</a>　|　
				<a href="plaza.jsp">Discover</a> | 
				<a href="lookaround">Look Around</a>
			</div>
			<div id="user_func">
				<a href="setup">Set up</a>　|　
				<a href="UserAction?action=logout">Log off</a>
			</div>
		</div>
	<h1><a href="" title="Powered by 1988"></a></h1>
	<div id="mbody_top"></div>
	<div id="mbody">
	<div style="height: 1px;"></div>
<div id="main">
	<h2 id="sub_tit"><%=user.getRealname()%>的关注</h2>
	<div id="user_grid">
	<table><tbody>
<%
		int i = 1;
		if(userList.size() == 0 ){
			out.println("未找到结果...");
		}
		for(BlogUser blogUser : userList){
			if(i%2 == 1) { out.println("<tr>"); }
			out.println("<td class=\"data-col\">");
%>
		.
			<div class="postfig float-left">
				<a style="hide-focus: true" href="myblog?name=<%=blogUser.getName()%>" target="_blank" title="<%=blogUser.getRealname()%>">
					<img class="img_fra" src="<%=blogUser.getPhotoUrl()%>" onload="shrinkImage(this,48,48);">
				</a> 
			</div>
			<div class="city-info float-left" style="margin-left:10px;">
				<div class="text-left name">
					<a style="hide-focus: true" href="myblog?name=<%=blogUser.getName()%>">
						<%=blogUser.getRealname()%>
					</a>
				</div>
				<div class="sex float-left <%=blogUser.getGender()==1?"female":"male"%>"></div>
				<div class="float-left city"><%=blogUser.getAddress().isEmpty()?"未知":blogUser.getAddress()%></div>
				<div style="float:right; margin-top:-8px; position:relative;">
					<span id="concern-f">
<%			if(currUser.getId() != blogUser.getId()){ //不是本人
				if(!userMan.isFriend(currUser.getId(),blogUser.getId())) {	//当前用户与blogUser不是好友关系
%>
					<input type="button" class="button-plane" onclick="addAttention('<%=currUser.getId()%>','<%=blogUser.getId()%>');" value="添加关注"/>
<%				} else {	 %>
					<input type="button" class="button-plane" onclick="cancelAttention('<%=currUser.getId()%>','<%=blogUser.getId()%>');" value="取消关注"/>
<%				}	
			}
%>
					</span>
				</div>
			</div>
		</div>
<%
			out.println("</td>");
			if(i%2 == 0) { out.println("</tr>"); }
			i++;
		}
		if((i-1)%2 != 0) { out.println("</tr>"); }
%>
	</tbody></table>
	</div>
</div>
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
				<a href="attentions?name=<%=currUser.getName()%>">Followings</a>
			</div>
			<div class="concern_c">
				<span class="bold">&nbsp;&nbsp;<%=userMan.getFansOf(currUser)%></span><br/>
				<a href="fans?name=<%=currUser.getName()%>">Followers</a>
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