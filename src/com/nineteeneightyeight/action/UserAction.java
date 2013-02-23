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
import com.nineteeneightyeight.manager.UserManager;
import com.nineteeneightyeight.util.ConstantUtil;

/**
 * 用户操作处理Action,包括登录、注册、登出等
 * 
 * @author flytreeleft
 * 
 */
public class UserAction extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserAction() {
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

		if ("log".equals(action)) {
			login(request, response);
		} else if ("reg".equals(action)) {
			register(request, response);
		} else if ("logout".equals(action)) {
			logout(request, response);
		} else if ("info".equals(action)) {
			saveInfo(request, response);
		} else if ("pwd".equals(action)) {
			savePwd(request, response);
		}
	}

	/**
	 * 登录
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void login(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String name = request.getParameter("name");
		String password = request.getParameter("password");
		BlogUser user = new BlogUser();
		UserManager userMan = new UserManager();
		Map<String, String> map = new HashMap<String, String>();

		user.setName(name);
		user.setPwd(password);

		if (userMan.login(user)) { // 登录成功

			request.getSession().setAttribute(ConstantUtil.CURRENT_USER, user); // 将用户保存到会话中
			
			map.put("url", "myhome");
		} else {
			map.put("error", "Wrong Password!");
		}
		// 在ajax中使用json作为返回类型时必须用JSONObject
		response.getWriter().println(JSONObject.fromObject(map));
	}

	/**
	 * 注册
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void register(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
        int idnum=25;
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		BlogUser user = new BlogUser();
		UserManager userMan = new UserManager();
		Map<String, String> map = new HashMap<String, String>();

		if (name != null && !name.isEmpty() && password != null && !password.isEmpty()) {
			user.setId(idnum);
			user.setName(name);
			user.setPwd(password);
			//map.put("error", name);// 跳转到主页
			
			
			if (userMan.register(user)) { // 注册成功
				
				// 分配资源
				new SourceManager(request).allocateSourceFor(user);
				request.getSession().setAttribute(ConstantUtil.CURRENT_USER, user); // 将用户保存到会话中
				
				map.put("url", "myhome");// 跳转到主页
			} else {
				map.put("error", "The username has been used!");
			}
		} else {
			map.put("error", "Cannot be blank!");
		}
		// 转换为JSON格式输出
		response.getWriter().println(JSONObject.fromObject(map));
	}

	/**
	 * 登出系统
	 * */
	private void logout(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		BlogUser currUser = (BlogUser) request.getSession().getAttribute(ConstantUtil.CURRENT_USER);
		if (currUser != null) { // 有用户登录
			
			request.getSession().removeAttribute(ConstantUtil.CURRENT_USER); // 将用户从会话中移除
		}

		response.sendRedirect("login.jsp"); // 跳转到登录页面
	}

	/**
	 * 保存新信息
	 * */
	private void saveInfo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String realName = request.getParameter("realname");
		String date = request.getParameter("date");
		String gender = request.getParameter("gender");
		String address = request.getParameter("address");
		String brief = request.getParameter("brief");
		BlogUser currUser = (BlogUser) request.getSession().getAttribute(ConstantUtil.CURRENT_USER);

		if (currUser == null) {
			response.getWriter().println("Hasn't login!");
			return;
		}
		if (realName == null || realName.isEmpty()) {
			response.getWriter().println("Please enter the name!");
			return;
		}
		// 保存信息
		currUser.setRealName(realName);
		currUser.setBirthday(date);
		currUser.setAddress(address);
		currUser.setBrief(brief);
		try {
			currUser.setGender(Integer.parseInt(gender));
		} catch (Exception e) {
			currUser.setGender(0);
		}
		// 保存用户的新信息
		if (new UserManager().update(currUser)) {
			response.getWriter().println("Changed successfully...");
		} else {
			response.getWriter().println("Failed!");
		}
	}

	/**
	 * 保存新密码
	 * */
	private void savePwd(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String oldPass = request.getParameter("oldpass");
		String newPass = request.getParameter("newpass");
		String repeatPass = request.getParameter("repeatpass");
		BlogUser currUser = (BlogUser) request.getSession().getAttribute(ConstantUtil.CURRENT_USER);

		if (currUser == null) {
			response.getWriter().println("The user hasn't login!");
			return;
		}
		if (oldPass == null || oldPass.isEmpty() || newPass == null || newPass.isEmpty()
				|| repeatPass == null || repeatPass.isEmpty()) {
			response.getWriter().println("Password cannot be blank!");
			return;
		}
		if (!newPass.equals(repeatPass)) {
			response.getWriter().println("Inconsistent!");
			return;
		}
		if (!currUser.getPwd().equals(oldPass)) {
			response.getWriter().println("The old password is incorrect!");
			return;
		}

		currUser.setPwd(newPass);
		// 保存密码
		if (new UserManager().update(currUser)) {
			response.getWriter().println("Changed successfully...");
		} else {
			response.getWriter().println("Failed!");
		}
		// 保存用户新信息
		request.getSession().setAttribute(ConstantUtil.CURRENT_USER, currUser);
	}
}
