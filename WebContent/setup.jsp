<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
<%@ page import="com.nineteeneightyeight.blog.BlogUser,com.nineteeneightyeight.manager.*,com.nineteeneightyeight.entity.*,com.nineteeneightyeight.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 0 Transitional//EN" "http://www.worg/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=7"/>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="Keywords" content="微博,微博客,交流,分享"/>
	<meta http-equiv="Pragma" content="no-cache"/>
	<meta http-equiv="Cache-Control" content="no-cache"/>
	<title>Blabby </title>
	<link rel="stylesheet" href="css/global.css" type="text/css"/>
	<link rel="stylesheet" href="css/user.css" type="text/css"/>
	<script type="text/javascript" src="js/jquery-1.4.2.min.js"></script>
	<script type="text/javascript" src="js/jquery.blockUI.js"></script>
 	<script type="text/javascript" src="js/ajaxfileupload.js"></script>
	<script type="text/javascript" src="js/image.js"></script>
	<script type="text/javascript" src="js/setup.js"></script>
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
<body onload="getStatus();">
<div id="wrapper">	
		<div id="topbar">
			<div id="user_page">
				<a href="myhome.jsp">Home</a> | 
				<a href="myblog.jsp?name=<%=currUser.getName()%>">My Blabby</a> | 
				<a href="myalbum.jsp?name=<%=currUser.getName()%>">My album</a> | 
				<a href="plaza.jsp">Discover</a> | 
				<a href="lookaround.jsp">Look Around</a>
			</div>
			<div id="user_func">
				<a href="setup.jsp">Set up</a>　|　
				<a href="UserAction?action=logout">Log off</a>
			</div>
		</div>
	<h1><a href="" title="Powered by 1988"></a></h1>
	<div id="mbody_top"></div>
	<div id="mbody">
	<div style="height: 1px;"></div>
<h2 style="margin-left:45px;">Set up</h2>
<div id="sub_menu">
	<table><tr>
		<td><input type="button" class="button-plane" onclick="showInfoEditor();" value="Data"/></td>
		<td><input type="button" class="button-plane" onclick="showPswEditor();" value="Password"/></td>
		<td><input type="button" class="button-plane" onclick="showPhotoEditor();" value="Picture"/></td>
		<td><input type="button" class="button-plane" onclick="showMusicEditor();" value="Music"/></td>
	</tr></table>
</div>
<div id="option_editor">
	<div class="errd red"><b id="err"></b></div>
	<form id="info_editor" class="">
		<table class="editor_table">
			<tr>
				<td align="center"><label>&nbsp;Name<span class="red">*</span>　</label></td>
				<td class="td_in"><input type="text" id="realname" name="realname" class="input" maxlength="10" value="<%=currUser.getRealname()%>"/></td>
			</tr>
			<tr>
				<td align="center"><label>&nbsp;Gender<span class="red">*</span>　</label></td>
				<td class="td_in"><!--radio必须指定name属性,且同一组的name值要一样-->
					<input type="radio" id="m" name="gender" value="0" <%=currUser.getGender()==0?"checked=\"checked\"":""%>/><label for="m">Male</label>
					<input type="radio" id="f" name="gender" value="1" <%=currUser.getGender()==1?"checked=\"checked\"":""%>/><label for="f">Female</label>
				</td>
			</tr>
			<tr>
				<td align="center"><label>&nbsp;Address　</label></td>
				<td class="td_in">
					<input type="text" id="address" class="input" name="address" value="<%=currUser.getAddress()%>"/>
				</td>
			</tr>
			<tr>
				<td align="center"><label>&nbsp;Date of Birth　</label></td>
<%
			String[] date = currUser.getBirthday().split("-");
			int i;
			if(date == null || date.length < 3){
				date = new String[3];
				date[0] = "2000";
				date[1] = "01";
				date[2] = "01";
			}
%>
				<td class="td_in">
					<span id="years">
						<select id="year">
<%			for(i = 2010; i >= 1949; i--) { %>
							<option <%=String.valueOf(i).equals(date[0])?"selected":""%> value="<%=i%>"><%=i%></option>
<%			} %>
						</select>
					</span>&nbsp;Year&nbsp;
					<span id="months">
						<select id="month">
<%			for(i = 1; i <= 12; i++) { 
				String month = "0"+String.valueOf(i);
				month = month.substring(month.length()-2);
%>
							<option <%=month.equals(date[1])?"selected":""%> value="<%=month%>"><%=month%></option>
<%			} %>
						</select>
					</span>&nbsp;Month&nbsp;
					<span id="days">
						<select id="day">
<%			for(i = 1; i <= 31; i++) { 
				String day = "0"+String.valueOf(i);
				day = day.substring(day.length()-2);
%>
							<option <%=day.equals(date[2])?"selected":""%> value="<%=day%>"><%=day%></option>
<%			} %>
						</select>
					</span>&nbsp;Day&nbsp;
				</td>
			</tr>
			<tr>
				<td align="center"><label>&nbsp;Details　</label></td>
				<td class="td_in">
					<textarea id="brief" name="brief" class="input" rows="5"><%=currUser.getBrief()%></textarea>	
				</td>
			</tr>
			<tr>
				<td></td>
				<td><br/>
					<input type="button" class="button-primary" onclick="saveInfo();" value="Save"/>
				</td>
			</tr>
		</table>
	</form>
	
	<form id="photo_editor" class="hidden" method="post" style="margin-left:200px;" enctype="multipart/form-data">
		 Choose a picture：<input type="file" id="photo" name="photo" style="height:20px" size="20"/><br/><br/>
		<input type="button" class="button-primary" style="margin-left:100px;" onclick="photoUpload();" value="Upload"/> 
		<br/>
		<div id="photo_show" style="margin-left:100px;">
			<br/>
			<div><img class="img_fra avatar-160 none_hover" id="photo_img" src="<%=currUser.getPhotoUrl()%>" onload="shrinkImage(this,160,160);"/></div>
		</div>
	</form>
	
	<form id="music_editor" class="hidden" method="post" style="margin-left:200px;" enctype="multipart/form-data">
		 Please Select Background Music：<input type="file" name="music" id="music" style="height:20px" size="20"/><br/><br/>
		<input type="button" class="button-primary" style="margin-left:100px;" onclick="musicUpload();" value="Upload"/> 
	</form>
			
	<form id="psw_editor" class="hidden" method="post">
		<table class="editor_table">
			<tr>
				<td align="center"><label>&nbsp;Old Password<span class="red">*</span>　</label></td>
				<td class="td_in"><input id="oldpass" type="password" name="op" class="input" maxlength="20"/></td>
			</tr>
			<tr>
				<td align="center"><label>&nbsp;New Password<span class="red">*</span>　</label></td>
				<td class="td_in"><input id="newpass" type="password" name="np" class="input" maxlength="20"/></td>
			</tr>
			<tr>
				<td align="center"><label>&nbsp;Re-Input Password<span class="red">*</span>　</label></td>
				<td class="td_in"><input id="repeatpass" type="password" class="input" maxlength="20"/></td>
			</tr>
			<tr>
				<td></td>
				<td><br/>
					<input type="button" class="button-primary" onclick="checkPsw();" value="Change"/>
				</td>
			</tr>
		</table>
	</form>
</div>
	</div>
	<div id="mbody_bottom"></div>
<div id="footer">
&copy; 2013 @ Blabby
</div>
</div> <!-- // wrapper -->
</body>
</html>