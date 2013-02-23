<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
<%@ page import="com.nineteeneightyeight.blog.BlogUser,com.nineteeneightyeight.manager.*,com.nineteeneightyeight.entity.*,com.nineteeneightyeight.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=7" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="Keywords" content="微博,微博客,交流,分享" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta http-equiv="Cache-Control" content="no-cache" />
	<title>Blabby</title>
	<link rel="stylesheet" href="css/global.css" type="text/css" />
	<link rel="stylesheet" href="css/user.css" type="text/css" />
	<script type="text/javascript" src="js/jquery-1.4.2.min.js"></script>
	<script type="text/javascript" src="js/image.js"></script>
	<script type="text/javascript" src="js/status.js"></script>
	<script type="text/javascript" src="js/search.js"></script>
	<script type="text/javascript" src="js/main.js"></script>
	<script type='text/javascript' src="dwr/engine.js"></script>
  	<script type='text/javascript' src="dwr/util.js"></script>
  	<script type="text/javascript">
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
<body>
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
<div style="height: 1px"></div>
<h2 style="margin-left: 45px" class="sub_title">Look Around</h2>
<div id="sub_menu">
	<table><tr>
		<td><input type="button" class="button-plane" onclick="showSimpleSearch();" value="Basic Search"/></td>
		<!--<td><input type="button" class="button-plane" onclick="showAdvanceSearch();" value="Advanced Search"/></td>-->
	</tr></table>
</div>
<div id="main">
	<div id="search_panel">
	<div id="condition">
	<div id="simple">
	<form id="simple_form" class="mid-text" style="margin-right: 95px" action="lookaround.jsp" method="post">
			Please Enter the Search Content <input id="scontent" name="content" class="input2" maxlength="20">
		<input type="submit" class="button-primary float-right" style="position: relative;bottom: 30px;right:250px;" 
			   value="Search'"/>
	</form>
	</div>
</div>
<div id="msg" class="none-div">
<h3>Searched Results</h3>
<div id="status_board">
	<table class="status_table">
	<tbody>
<%
	response.setContentType("text/html; charset=UTF-8");
	request.setCharacterEncoding("UTF-8");
	
	String content = request.getParameter("content");
	StatusParse statusParse = new StatusParse(); //初始化状态解析类
	CommentManager commentMan = new CommentManager(); //初始化评论管理类
	StatusManager statusMan = new StatusManager();
	List<StatusEntity> statusList = statusMan.searchStatuses(content,0);
	int count = 1; // 状态计数
	
	// 列出状态及其用户信息,若该状态的所有者为当前用户则提供删除操作
	for(StatusEntity status : statusList) {
		BlogUser user = status.getPublisher();
		//若状态为自己发的,则不显示
		if(user.getId() == currUser.getId()) {
			continue;
		}
		count++;
		//获取状态的评论列表
		List<CommentEntity> commentList = commentMan.getCommentsOf(status);
		//解析状态中的非字符内容
		statusParse.setStatus(status.getContent());
%>
		<tr id="status<%=status.getId()%>">
		<td>
			<div class="postfig float-left">
				<a style="hide-focus: true" href="myblog.jsp?name=<%=user.getName()%>" target="_blank" title="<%=user.getRealname()%>">
					<img class="img_fra avatar-48" src="<%=user.getPhotoUrl()%>" onload="shrinkImage(this,48,48);">
				</a>
			</div>
			<div class="float-right">
				<div class="poundtop"></div>
				<div class="postcontent">
					<p>
						<a style="font-size: 13px; text-decoration: underline; hide-focus: true" 
						   class="name" href="myblog.jsp?name=<%=user.getName()%>"><%=user.getRealname()%>
						</a>: <%=statusParse.parse()%>
					</p>
				</div>
				<div class="post-meta">
					<div class="msg_info">Sent at &nbsp;<span class="time"><%=status.getPublishDate()%></span></div>
					<div class="comment-meta text-right">
						<table><tr><td>
						<input type="button" class="button-plane" style="position: relative;"/>
						</td><td>
						<input type="button" class="button-plane" style="position: relative;" 
								onclick="getComments('<%=user.getId()%>','<%=status.getId()%>');" value="Comment(<%=commentList.size()%>)"/>
						</td>
						</tr></table>
					</div>
				</div>
				<div class="poundbottom"></div>
				<!--显示评论-->
				<div id="comment<%=status.getId()%>" class="reply">
				</div>	
			</div>
		</td></tr>
<%	}	
	if(count <= 1){
		out.println("Failed to find...");
	}
%>
	</tbody>
	</table>
</div>
</div>
</div>
</div>
<div id="sidebar">
<div id="sidebar-top"></div>
<ul>
	<li class="fir">
	<h2>Recommanded</h2>
	</li>
	<li class="side_tit">Most Popularity Top10</li>
	<li class="pat">
	<div id="top10c" class="data-div">
		<!--显示前10个最活跃的用户头像-->
		<table cellspacing="15">
<%
	List<BlogUser> userList = null;
	int i = 1; //控制每行的列数,默认每行显示5个用户

	userList = userMan.getActiveUsers(10);
	
	for(BlogUser user : userList){
		if(user.getId() == currUser.getId()) { continue; } //是本人则不显示
		if(i%3 == 1) { out.println("<tr>"); }
		out.println("<td>");
%>
		<a href="myblog.jsp?name=<%=user.getName()%>" target="_blank" title="<%=user.getRealname()%>">
			<img src="<%=user.getPhotoUrl()%>"onload="shrinkImage(this,48,48);"/>
		</a><br/>
<%
		if(!userMan.isFriend(currUser.getId(),user.getId())) {	//当前用户与user不是好友关系%>
			<input type="button" class="button-plane" onclick="addAttention('<%=currUser.getId()%>','<%=user.getId()%>');" value="Following"/>
<%		} else {	 %>
			<input type="button" class="button-plane" onclick="cancelAttention('<%=currUser.getId()%>','<%=user.getId()%>');" value="Unfollow"/>
<%		}	 %>
<%
		out.println("</td>");
		if(i%3 == 0) { out.println("</tr>"); }
		i++;
	}
	if((i-1)%3 != 0) { out.println("</tr>"); }
%>
		</table>
	</div>
	</li>
</ul>
<div id=sidebar-bottom></div>
</div>
<!-- // sidebar -->
<div class="blank"><a href="#">Return to the top</a></div>
</div>
<div id="mbody_bottom"></div>
<div id="footer">
&copy; 2013 @ Babbly
</div>
</div><!-- // wrapper -->
</body>
</html>