package com.nineteeneightyeight.action;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nineteeneightyeight.entity.StatusEntity;
import com.nineteeneightyeight.manager.StatusManager;

/**
 * 状态处理Action,负责状态的发布和删除等
 * 
 * @author flytreeleft
 * 
 */
public class StatusAction extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public StatusAction() {
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

		if ("publish".equals(action)) {
			publish(request, response);
		} else if ("delete".equals(action)) {
			delete(request, response);
		}
	}

	/**
	 * 发布状态
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	private void publish(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String idStr = request.getParameter("userid");
		String status = request.getParameter("status");
		int id = -1;
		StatusEntity entity = new StatusEntity();
		StatusManager statusMan = new StatusManager();

		if (status == null || status.isEmpty()) {
			response.getWriter().println("Cannot be blank...");
			return;
		}
		// 状态过长,则返回
		if (status.length() > 140) {
			response.getWriter().println("Up to 140 Characters...");
			return;
		}

		try {
			id = Integer.parseInt(idStr);
		} catch (Exception e) {
			id = -1;
		}

		entity.setUserId(id);
		entity.setContent(status);
		entity.setPublishDate(new Date().toLocaleString());

		if (statusMan.saveStatus(entity)) {
			response.getWriter().println("Successfully Tweet...");
		} else {
			response.getWriter().println("Failed!");
		}
	}

	/**
	 * 删除状态
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void delete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String statusIdStr = request.getParameter("statusid");
		int statusId = -1;

		try {
			statusId = Integer.parseInt(statusIdStr);
		} catch (Exception e) {
			statusId = -1;
		}

		if (new StatusManager().delStatus(statusId)) {
			response.getWriter().println("Successfully Delete ...");
		} else {
			response.getWriter().println("Failed to Delete!");
		}
	}
}
