<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
<%@ page import="com.nineteeneightyeight.blog.BlogUser,com.nineteeneightyeight.manager.*,com.nineteeneightyeight.entity.*,com.nineteeneightyeight.util.*" %>
<%
	//显示用户最新发布的状态,range表示只显示userid所对应用户的状态还是包括其所关注的好友的状态
	String range = request.getParameter("range");
	String userid = request.getParameter("userid");
	String indexStr = request.getParameter("index");
	String pageSizeStr = request.getParameter("pagesize");
	int id = -1;
	int index = 1;
	int pageSize = -1;
	int i = 0;
	int[] idArray = new int[1];
	BlogUser currUser = (BlogUser) request.getSession().getAttribute(ConstantUtil.CURRENT_USER);
	UserManager userMan = new UserManager();
	StatusManager statusMan = new StatusManager();
	List<BlogUser> userList = null;
	List<StatusEntity> statusList = null;
	//如果当前用户为空,则创建一个空对象防止空异常
	if(currUser == null){
		currUser = new BlogUser();
	}
	//转换id为整形
	try {
		id = Integer.parseInt(userid);
	} catch(Exception e) {
		id = -1;
	}
	//转换页号
	try {
		index = Integer.parseInt(indexStr);
	} catch(Exception e) {
		index = 1;
	}
	//转换页大小
	try {
		pageSize = Integer.parseInt(pageSizeStr);
	} catch(Exception e) {
		pageSize = -1;
	}
	//判断获取的状态是否需显示关注好友的状态,all为显示,否则为不显示
	if("all".equals(range)) {
		userList = userMan.getFriends(id);
		idArray = new int[userList.size()+1];
		
		for(BlogUser user : userList){
			idArray[i++] = user.getId();
		}
		idArray[i] = id;
	} else {
		idArray[0] = id;
	}
	//获取用户的状态集合
	statusList = statusMan.getStatusesOf(idArray,0,0);//(index-1)*pageSize,pageSize);
%>
<table class="status_table">
<tbody>
<%
	StatusParse statusParse = new StatusParse(); //初始化状态解析类
	CommentManager commentMan = new CommentManager(); //初始化评论管理类
	// 列出状态及其用户信息,若该状态的所有者为当前用户则提供删除操作
	for(StatusEntity status : statusList) {
		BlogUser user = status.getPublisher();
		List<CommentEntity> commentList = commentMan.getCommentsOf(status);
		//解析状态中的非字符内容
		statusParse.setStatus(status.getContent());
%>
	<tr id="status<%=status.getId()%>">
	<td>
		<div class="postfig float-left">
			<a style="hide-focus: true" href="myblog.jsp?name=<%=user.getName()%>" target="_blank" title="<%=user.getRealname().isEmpty()?"佚名":user.getRealname()%>">
				<img class="img_fra avatar-48" src="<%=user.getPhotoUrl()%>" onload="shrinkImage(this,48,48);">
			</a>
		</div>
		<div class="float-right">
			<div class="poundtop"></div>
			<div class="postcontent">
				<p>
					<a style="font-size: 13px; text-decoration: underline; hide-focus: true" 
					   class="name" href="myblog.jsp?name=<%=user.getName()%>"><%=user.getRealname().isEmpty()?"佚名":user.getRealname()%>
					</a>: <%=statusParse.parse()%>
				</p>
			</div>
			<div class="post-meta">
				<div class="msg_info">发表于&nbsp;<span class="time"><%=status.getPublishDate()%></span></div>
				<div class="comment-meta text-right">
					<table><tr><td>
<%		if(user.getId() == currUser.getId()) {		%>
					<input type="button" class="button-plane" style="position: relative;" 
							onclick="deleteStatus('<%=status.getId()%>');" value="删除"/>
<%		} else {	%>
					<input type="button" class="button-plane" style="position: relative;"/>
<%		} %>
					</td><td>
					<input type="button" class="button-plane" style="position: relative;" 
							onclick="getComments('<%=user.getId()%>','<%=status.getId()%>');" value="评论(<%=commentList.size()%>)"/>
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
<%	}	%>
</tbody>
</table>