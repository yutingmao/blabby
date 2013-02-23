package com.nineteeneightyeight.action;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nineteeneightyeight.entity.CommentEntity;
import com.nineteeneightyeight.manager.CommentManager;

/**
 * 评论Action,负责评论的发布和删除等
 * 
 * @author flytreeleft
 * 
 */
public class CommentAction extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CommentAction() {
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

		if ("comment".equals(action)) {
			commit(request, response);
		} else if ("delete".equals(action)) {
			delete(request, response);
		}
	}

	/**
	 * 发布评论
	 * */
	@SuppressWarnings("deprecation")
	private void commit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String fromIdStr = request.getParameter("fromid");// 发表状态的用户的ID
		String toIdStr = request.getParameter("toid");// 所评论状态的用户的ID
		String statusIdStr = request.getParameter("statusid");// 所评论状态的ID
		String comment = request.getParameter("comment");
		int fromId = -1;
		int toId = -1;
		int statusId = -1;

		// 没有状态,则返回
		if (comment == null || comment.isEmpty()) {
			return;
		}
		// 超过140字符,则返回
		if (comment.length() > 140) {
			return;
		}
		// 转化ID
		try {
			fromId = Integer.parseInt(fromIdStr);
		} catch (Exception e) {
			fromId = -1;
		}
		try {
			toId = Integer.parseInt(toIdStr);
		} catch (Exception e) {
			toId = -1;
		}
		try {
			statusId = Integer.parseInt(statusIdStr);
		} catch (Exception e) {
			statusId = -1;
		}
		// 封装评论信息
		CommentEntity entity = new CommentEntity();

		entity.setFromId(fromId);
		entity.setToId(toId);
		entity.setStatusId(statusId);
		entity.setComment(comment);
		entity.setCommentDate(new Date().toLocaleString());
		// 保存评论
		if (new CommentManager().saveComment(entity)) {
			response.getWriter().println("Comment succeed");
		} else {
			response.getWriter().println("Please try again!");
		}
	}

	/**
	 * 删除评论
	 * */
	private void delete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String commentIdStr = request.getParameter("commentid");
		int commentId = -1;

		try {
			commentId = Integer.parseInt(commentIdStr);
		} catch (Exception e) {
			commentId = -1;
		}

		if (new CommentManager().delComment(commentId)) {
			response.getWriter().println("Succeed...");
		} else {
			response.getWriter().println("Failed!");
		}
	}
}
