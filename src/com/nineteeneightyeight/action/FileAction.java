package com.nineteeneightyeight.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.nineteeneightyeight.blog.BlogUser;
import com.nineteeneightyeight.manager.SourceManager;
import com.nineteeneightyeight.util.ConstantUtil;

/**
 * 文件上传Action,负责头像、背景音乐和相册的上传操作
 * 
 * @author flytreeleft
 * 
 */
public class FileAction extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FileAction() {
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
		String type = request.getParameter("type");

		if ("photo".equals(type)) {
			savePhoto(request, response);
		} else if ("music".equals(type)) {
			saveMusic(request, response);
		} else if ("album".equals(type)) {

		}
	}

	private void savePhoto(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		SourceManager sourceMan = new SourceManager(request);
		BlogUser currUser = (BlogUser) request.getSession().getAttribute(ConstantUtil.CURRENT_USER);
		Map<String, String> map = new HashMap<String, String>();
		
		if (currUser != null) {
			if (sourceMan.savePhoto(currUser.getBlogPhoto())) {
				map.put("msg", "头像保存成功...");
				map.put("url", currUser.getPhotoUrl());
			} else {
				map.put("error", currUser.getName());
				map.put("error", "头像保存失败!");
			}
		} else {
			map.put("error", "用户未登录!");
		}
		// 在ajax中使用json作为返回类型时必须用JSONObject
		response.getWriter().println(JSONObject.fromObject(map));
	}

	private void saveMusic(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		SourceManager sourceMan = new SourceManager(request);
		BlogUser currUser = (BlogUser) request.getSession().getAttribute(ConstantUtil.CURRENT_USER);
		Map<String, String> map = new HashMap<String, String>();

		if (currUser != null) {
			if (sourceMan.saveMusic(currUser.getBlogMusic())) {
				map.put("msg", "音乐保存成功...");
				map.put("url", "myhome.jsp");
			} else {
				map.put("error", "音乐保存失败!");
			}
		} else {
			map.put("error", "用户未登录!");
		}
		// 在ajax中使用json作为返回类型时必须用JSONObject
		response.getWriter().println(JSONObject.fromObject(map));
	}
}
