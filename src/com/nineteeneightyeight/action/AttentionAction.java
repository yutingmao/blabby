package com.nineteeneightyeight.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nineteeneightyeight.manager.UserManager;

/**
 * 关注Acton,负责关注的添加和取消操作
 * 
 * @author flytreeleft
 * 
 */
public class AttentionAction extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AttentionAction() {
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
		String action = request.getParameter("action");

		if ("add".equals(action)) {
			add(request, response);
		} else if ("cancel".equals(action)) {
			cancel(request, response);
		}
	}

	/**
	 * 添加关注
	 * */
	private void add(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String viewerIdStr = request.getParameter("viewerid");
		String viewedIdStr = request.getParameter("viewedid");
		int viewerid = -1;
		int viewedid = -1;

		try {
			viewerid = Integer.parseInt(viewerIdStr);
		} catch (Exception e) {
			viewerid = -1;
		}
		try {
			viewedid = Integer.parseInt(viewedIdStr);
		} catch (Exception e) {
			viewedid = -1;
		}

		UserManager userMan = new UserManager();

		if (userMan.addFriend(userMan.getUserById(viewerid), viewedid)) {
			response.getWriter().println("添加关注成功...");
		} else {
			response.getWriter().println("添加关注失败!");
		}
	}

	/**
	 * 取消关注
	 * */
	private void cancel(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String viewerIdStr = request.getParameter("viewerid");
		String viewedIdStr = request.getParameter("viewedid");
		int viewerid = -1;
		int viewedid = -1;

		try {
			viewerid = Integer.parseInt(viewerIdStr);
		} catch (Exception e) {
			viewerid = -1;
		}
		try {
			viewedid = Integer.parseInt(viewedIdStr);
		} catch (Exception e) {
			viewedid = -1;
		}

		UserManager userMan = new UserManager();

		if (userMan.delFriend(userMan.getUserById(viewerid), viewedid)) {
			response.getWriter().println("Successfully Unfollow...");
		} else {
			response.getWriter().println("Fail to Unfollow!");
		}
	}
}
