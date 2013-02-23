<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
<%@ page import="com.nineteeneightyeight.blog.BlogUser,com.nineteeneightyeight.manager.*,com.nineteeneightyeight.entity.*,com.nineteeneightyeight.util.*" %>
<%
	response.setContentType("text/html; charset=UTF-8");
	request.setCharacterEncoding("UTF-8");
	String name = request.getParameter("name");
	String sexStr = request.getParameter("sex");
	int sex = 2;
	try{
		sex = Integer.parseInt(sexStr);
	}catch(Exception e){
		sex = 2;
	}
	
	UserManager userMan = new UserManager();
	List<BlogUser> userList = null;
	BlogUser currUser = (BlogUser) request.getSession().getAttribute(ConstantUtil.CURRENT_USER);
	if(currUser == null){
		currUser = new BlogUser();
	}
	
	userList = userMan.advancedSearch(name,sex,"","","",0,0);
%>
<table>
<tbody>
<%
	int i = 1;
	for(BlogUser user : userList){
		if(user.getId() == currUser.getId()) { continue; } //是本人则不显示
		if(i%2 == 1) { out.println("<tr>"); }
		out.println("<td class=\"data-col\">");
%>
	<div class="div2">
		<div class="postfig float-left">
			<a style="hide-focus: true" href="myblog.jsp?name=<%=user.getName()%>" target="_blank" title="<%=user.getRealname().isEmpty()?"佚名":user.getRealname()%>">
				<img class="img_fra" src="<%=user.getPhotoUrl()%>" onload="shrinkImage(this,48,48);">
			</a> 
		</div>
		<div class="city-info float-left" style="margin-left:10px;">
			<div class="text-left name">
				<a style="hide-focus: true" href="myblog.jsp?name=<%=user.getName()%>">
					<%=user.getRealname().isEmpty()?"佚名":user.getRealname()%>
				</a>
			</div>
			<div class="sex float-left <%=user.getGender()==1?"female":"male"%>"></div>
			<div class="float-left city"><%=user.getAddress().isEmpty()?"未知":user.getAddress()%></div>
		</div>
	</div>
<%
		out.println("</td>");
		if(i%2 == 0) { out.println("</tr>"); }
		i++;
	}
	if((i-1)%2 != 0) { out.println("</tr>"); }
	if(i <= 1 ){
		out.println("未找到结果...");
	}
%>
</tbody>
</table>