package com.nineteeneightyeight.action;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.ScriptSession;
import org.directwebremoting.ServerContext;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.proxy.dwr.Util;

import com.nineteeneightyeight.blog.BlogMessage;
import com.nineteeneightyeight.blog.BlogUser;
import com.nineteeneightyeight.util.ConstantUtil;

/**
 * 广播管理Action类,负责相应客户端的取消息(get)和发送消息(send)
 * 
 * @author flytreeleft
 * 
 */
public class BroadcastAction extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BroadcastAction() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html; charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		String action = request.getParameter("action"); // 客户端的动作

		if ("send".equals(action)) {
			sendMessage(request, response);
		}
	}

	/**
	 * 将用户发送的消息发送给所有在线用户
	 */
	@SuppressWarnings("unchecked")
	private void sendMessage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String msg = request.getParameter("msg");// 客户端发送的消息
		ServerContext wctx = ServerContextFactory.get(getServletContext());
		Collection<ScriptSession> sessions = wctx.getAllScriptSessions();
		Util util = new Util();
		BlogUser currUser = (BlogUser) request.getSession().getAttribute(ConstantUtil.CURRENT_USER);

		if (currUser == null || msg == null || msg.isEmpty() || sessions == null || sessions.isEmpty()) {
			return;
		}
		
		util.addScriptSessions(sessions);

		util.addFunctionCall("showMessage",new BlogMessage(currUser.getName(),msg).toString());
	}
}
