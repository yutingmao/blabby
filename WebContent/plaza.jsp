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
	<title>Babbly</title>
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
<h2 style="margin-left: 45px" class="sub_title">找围友</h2>
<div id="sub_menu">
	<table><tr>
		<td><input type="button" class="button-plane" onclick="showSimpleSearch();" value="基本查找"/></td>
		<td><input type="button" class="button-plane" onclick="showAdvanceSearch();" value="高级查找"/></td>
	</tr></table>
</div>
<div id="main">
	<div id="search_panel">
	<div id="condition">
	<div id="simple">
	<form id="simple_form" class="mid-text" style="margin-right: 95px" method="post">
			请输入要查找的姓名 <input id="sname" class="input2" maxlength="20">
		<input type="button" class="button-primary float-right" style="position: relative;bottom: 30px;right:250px;" 
				onclick="simpleSearch();" value="查&nbsp;找"/>
	</form>
	</div>
	<div id="advance" class="hidden" style="margin-right: 95px">
	<form id="advance_form" class="mid-text" method="post">
	<table class="mar-center" cellspacing="8">
		<tbody>
			<tr>
				<td>姓 名 &nbsp;</td>
				<td><input id="aname" class="input2" maxlength="20"></td>
			</tr>
			<tr>
				<td>性 别 &nbsp;</td>
				<td>
					<select id="sex">
						<option selected value="2">&nbsp;不限&nbsp;</option>
						<option value="0">&nbsp;男&nbsp;</option>
						<option value="1">&nbsp;女&nbsp;</option>
					</select>
				</td>
			</tr>
			<tr>
				<td><input value="search" type="hidden" name="type"><input class="hidden"></td>
				<td align=right>
					<input type="button" class="button-primary float-right" style="margin-right: 95px" 
							onclick="advancedSearch();" value="查&nbsp;找"/>
				</td>
			</tr>
		</tbody>
	</table>
	</form>
	</div>
</div>
<div id="msg" class="none-div">
<h3>Searched Results</h3>
<div id="rs_panel"></div>
</div>
</div>
</div>
<div id="sidebar">
<div id="sidebar-top"></div>
<ul>
	<li class="fir">
	<h2>Recommended</h2>
	</li>
	<li class="side_tit">Most Popularity Top10</li>
	<li class="pat">
	<div id="top10c" class="data-div">
		<!--显示前10个最受欢迎的用户头像-->
		<table cellspacing="15">
<%
	List<BlogUser> userList = null;
	int i = 1; //控制每行的列数,默认每行显示5个用户

	userList = userMan.getPopularUsers(10);
	
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
			<input type="button" class="button-plane" onclick="addAttention('<%=currUser.getId()%>','<%=user.getId()%>');" value="添加关注"/>
<%		} else {	 %>
			<input type="button" class="button-plane" onclick="cancelAttention('<%=currUser.getId()%>','<%=user.getId()%>');" value="取消关注"/>
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