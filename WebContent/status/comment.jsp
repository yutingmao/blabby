<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="java.util.*"%>
<%@ page import="com.nineteeneightyeight.blog.BlogUser,com.nineteeneightyeight.manager.*,com.nineteeneightyeight.entity.*,com.nineteeneightyeight.util.*" %>
<%
	String userIdStr = request.getParameter("userid"); // 所评论状态对应的用户ID
	String statusIdStr = request.getParameter("statusid"); // 所评论状态的ID
	BlogUser currUser = (BlogUser) request.getSession().getAttribute(ConstantUtil.CURRENT_USER);
	List<CommentEntity> commentList = null;
	int userid = -1;
	int statusid = -1;
	try {
		userid = Integer.parseInt(userIdStr);
	} catch(Exception e) {
		userid = -1;
	}
	try {
		statusid = Integer.parseInt(statusIdStr);
	} catch(Exception e) {
		statusid = -1;
	}
	//如果当前用户为空,则创建一个空对象防止空异常
	if(currUser == null){
		currUser = new BlogUser();
	}
	CommentManager commentMan = new CommentManager();
	commentList = commentMan.getCommentsOf(statusid);
%>
<div>
<div class="comment_d">
	<table class="comment_table" cellspacing="0" cellpadding="0">
	<tbody>
<%
	//解析评论
	StatusParse parse = new StatusParse();
	//显示状态下的所有评论
	for(CommentEntity comment : commentList) {
		BlogUser user = comment.getCommentator();
		parse.setStatus(comment.getComment());
%>
	<tr>
	<td>
		<div class="comment_div">
			<a style="hide-focus: true" href="myblog.jsp?name=<%=user.getName()%>" target="_blank" title="<%=user.getRealname().isEmpty()?"佚名":user.getRealname()%>">
				<img style="position: relative; margin-left: 5px; top: -3px" class="img_fra avatar-32 float-left" 
					 src="<%=user.getPhotoUrl()%>" onload="shrinkImage(this,32,32);">
			</a>
			<div style="margin-left: 55px">
				<a style="hide-focus: true" href="myblog.jsp?name=<%=user.getName()%>"><%=user.getRealname().isEmpty()?"佚名":user.getRealname()%></a> 
				:&nbsp;<%=parse.parse()%><br>
				<div class="comment-meta text-right">
					<span class="time"><%=comment.getCommentDate()%></span>　
<%		if(user.getId() == currUser.getId()) {  %>
 					<input type="button" class="button-plane" style="position: relative;" 
						onclick="deleteComment('<%=userid%>','<%=statusid%>','<%=comment.getId()%>');" value="删&nbsp;除"/>
<%		} %>
				</div>
			</div>
		</div>
	</td></tr>
<%	} %>
	</tbody>
	</table>
</div>
<div class="comment_footer">共 <span id="com_fot<%=statusid%>"><%=commentList.size()%></span> 条评论 </div>
<div class="comment_poster">
	<form id="comment_form<%=statusid%>">
		<textarea style="width: 370px" id="commenttext<%=statusid%>" 
				  onpropertychange="countCommentChar(this,'<%=statusid%>','140')" name="text"></textarea>
				  <input value="1416" type="hidden" name="mid"> 
		<div style="margin-top: 6px; margin-left: 5px; font-size: 14px" id="countArea<%=statusid%>" class="float-left">140</div>
		<input type="button" style="margin-top: 5px; margin-right: -12px; hide-focus: true" class="button-primary float-right" 
			   onclick="comment('<%=currUser.getId()%>','<%=userid%>','<%=statusid%>');" value="评&nbsp;论"/>
	</form>
</div>
</div>