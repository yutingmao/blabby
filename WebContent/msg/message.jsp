<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*,com.nineteeneightyeight.blog.BlogMessage,com.nineteeneightyeight.util.*"%>
<%
	@SuppressWarnings("unchecked")
	List<BlogMessage> msgList = (List<BlogMessage>) request.getAttribute(ConstantUtil.MSG_LIST);
%>
<div>
	<table>
<%	for(BlogMessage message : msgList) {	%>
		<tr>
			<td><%=message.getDate().toLocaleString()%>:<br/>
				<%=message.getBroadcaster()%> 说:  <%=message.getMessage()%>
			</td>
		</tr>
<%	}	 %>
	</table>
</div>