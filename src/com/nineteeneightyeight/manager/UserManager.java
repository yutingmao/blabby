package com.nineteeneightyeight.manager;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.nineteeneightyeight.blog.BlogUser;
import com.nineteeneightyeight.dao.AttentionDao;
import com.nineteeneightyeight.dao.UserDao;

/**
 * 用户信息管理类,负责验证/保存BlogUser对象,初始化用户系统信息
 * 
 * @author flytreeleft
 * 
 */
public class UserManager {
	private UserDao dao;

	public UserManager() {
		dao = new UserDao();
	}

	/**
	 * 对用户身份的验证,判断用户是否为合法用户,若为合法用户则获取其详细信息
	 * 
	 * @param user
	 *            登录系统的用户对象
	 * @return 若为合法用户则返回true,否则返回false
	 * */
	public boolean login(BlogUser user) {
		BlogUser entity = null;
		boolean succ = false;

		if (user == null) {
			return succ;
		}

		dao.open();
		entity = dao.getUserByName(user.getName());
		// 登录成功,则获取详细信息
		if (entity != null && user.getName().equals(entity.getName())
				&& user.getPwd().equals(entity.getPwd())) {

			user.setId(entity.getId());
			user.setName(entity.getName());
			user.setGender(entity.getGender());
			user.setRealName(entity.getRealname());
			user.setPwd(entity.getPwd());
			user.setBirthday(entity.getBirthday());
			user.setAddress(entity.getAddress());
			user.setBrief(entity.getBrief());

			succ = true;
		}

		dao.close();

		return succ;
	}

	/**
	 * 新用户注册
	 * */
	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	public boolean register(BlogUser user) {
		boolean succ = false;
		
		// 如果用户不存在则不允许注册

		
		if (user == null) {
			return succ;
		}
		// 添加对象到数据库
			
		dao.open();
		succ = dao.addUser(user);
		// 如果注册成功则再次从数据库中读取数据以得到用户的ID
		if (succ) {
			BlogUser entity = dao.getUserByName(user.getName());

			user.setId(entity.getId());
			user.setName(entity.getName());
			user.setGender(entity.getGender());
			user.setRealName(entity.getRealname());
			user.setPwd(entity.getPwd());
			user.setBirthday(entity.getBirthday());
			user.setAddress(entity.getAddress());
			user.setBrief(entity.getBrief());
			
			succ = true;
		}
		dao.close();

		return succ;
	}

	/**
	 * 获取前count个用户对象
	 * 
	 * @param count
	 *            取出的用户数目
	 * @return
	 */
	public List<BlogUser> getUsers(int count) {

		try {
			dao.open();
			return dao.advancedSearch("", -1, "", "", "", 0, count);
		} finally {
			dao.close();
		}
	}

	/**
	 * 获取用户对象
	 * 
	 * @param name
	 *            用户名
	 * @return
	 */
	public BlogUser getUserByName(String name) {

		try {
			dao.open();
			return dao.getUserByName(name);
		} finally {
			dao.close();
		}
	}

	/**
	 * 获取用户对象
	 * 
	 * @param id
	 *            用户ID
	 * @return
	 */
	public BlogUser getUserById(int id) {

		try {
			dao.open();
			return dao.getUserById(id);
		} finally {
			dao.close();
		}
	}

	/**
	 * 更新用户信息,包括修改密码等操作,对于密码是否可以修改则交给上层操作负责,即此处不对原密码的正确性进行检验
	 * */
	public boolean update(BlogUser user) {

		try {
			dao.open();
			return dao.updateUser(user);
		} finally {
			dao.close();
		}
	}

	/**
	 * 判断两个用户之间是否为好友关系
	 * 
	 * @param userId
	 *            关注者ID
	 * @param friendId
	 *            被关注者ID
	 * @return
	 */
	public boolean isFriend(int userId, int friendId) {
		AttentionDao dao = new AttentionDao();

		try {
			dao.open();
			return dao.existAttention(userId, friendId);
		} finally {
			dao.close();
		}
	}

	/**
	 * 获取某用户的粉丝列表
	 * 
	 * @param userName
	 *            用户名
	 * @return
	 */
	public List<BlogUser> getFans(String userName) {
		List<BlogUser> friends = new ArrayList<BlogUser>();

		if (userName != null && !userName.isEmpty()) {
			dao.open();
			friends = dao.getViewers(dao.getUserByName(userName).getId());
			dao.close();
		}

		return friends;
	}

	/**
	 * 通过用户名来获取其关注的用户列表
	 * 
	 * @param userName
	 *            用户名
	 * @return
	 */
	public List<BlogUser> getFriends(String userName) {
		return getFriends(getUserByName(userName));
	}

	/**
	 * 获取用户所关注的用户列表
	 * 
	 * @param user
	 *            微博用户对象
	 * @return user所关注的微博用户
	 */
	public List<BlogUser> getFriends(BlogUser user) {
		if (user == null) {
			return new ArrayList<BlogUser>();
		}

		return getFriends(user.getId());
	}

	/**
	 * 通过用户ID获取其所关注的所有对象
	 * 
	 * @param id
	 *            用户ID
	 * @return 用户所关注的所有对象
	 */
	public List<BlogUser> getFriends(int id) {

		try {
			dao.open();
			return dao.getVieweds(id);
		} finally {
			dao.close();
		}
	}

	/**
	 * 为用户添加所要关注的对象
	 * 
	 * @param user
	 *            本用户
	 * @param friend
	 *            所要关注的对象
	 * @return 添加成功返回true,否则返回false
	 */
	public boolean addFriend(BlogUser user, BlogUser friend) {
		if (user == null || friend == null) {
			return false;
		}

		return addFriend(user, friend.getId());
	}

	/**
	 * 为用户添加所要关注的对象
	 * 
	 * @param user
	 *            本用户
	 * @param friendId
	 *            所要关注的对象的ID
	 * @return 添加成功返回true,否则返回false
	 */
	public boolean addFriend(BlogUser user, int friendId) {
		AttentionDao dao = new AttentionDao();
		boolean succ = false;

		if (user != null) {

			dao.open();
			// 添加关注到数据库中
			succ = dao.addAttention(user.getId(), friendId);
			dao.close();
		}

		return succ;
	}

	/**
	 * 取消对某用户的关注
	 * 
	 * @param user
	 *            本用户
	 * @param friend
	 *            所要取消关注的对象
	 * @return 取消成功则返回true,否则返回false
	 */
	public boolean delFriend(BlogUser user, BlogUser friend) {
		if (user == null || friend == null) {
			return false;
		}

		return delFriend(user, friend.getId());
	}

	/**
	 * 取消对某用户的关注
	 * 
	 * @param user
	 *            本用户
	 * @param friendId
	 *            所要取消关注的用户ID
	 * @return 成功则为true,否则为false
	 */
	public boolean delFriend(BlogUser user, int friendId) {
		AttentionDao dao = new AttentionDao();
		boolean succ = false;

		if (user != null) {

			dao.open();
			succ = dao.delAttention(user.getId(), friendId);
			dao.close();
		}

		return succ;
	}

	/**
	 * 按真实姓名搜索用户
	 * 
	 * @param realName
	 *            真实姓名
	 * @param index
	 *            起始号 ,表示从编号(从0开始)为index的记录开始取数据
	 * @param count
	 *            所要取的记录总数 ,count=0标志获取所有记录
	 * @return 結果集
	 */
	public List<BlogUser> searchFriendsByRealName(String realName, int index, int count) {
		return advancedSearch(realName, -1, "", "", "", index, count);
	}

	/**
	 * 高级搜索用户,可以按用户真实姓名、性别、地址和出生日期或四者的任意组合搜索,若均为空则返回所有结果
	 * 
	 * @param realName
	 *            用户真实姓名 ,为空则以剩下项为搜索条件
	 * @param gender
	 *            用户性别 ,只能取0或1,否则以剩下项为搜索条件
	 * @param address
	 *            用户地址 ,为空则以剩下项为搜索条件
	 * @param startDate
	 *            起始用户出生日期 ,为空则以剩下项为搜索条件
	 * @param endDate
	 *            终止用户出生日期 ,为空则以剩下项为搜索条件
	 * @param index
	 *            起始号 ,表示从编号(从0开始)为index的记录开始取数据
	 * @param count
	 *            所要取的记录总数 ,count=0标志获取所有记录
	 * @return
	 */
	public List<BlogUser> advancedSearch(String realName, int gender, String address,
			String startDate, String endDate, int index, int count) {

		try {
			dao.open();
			return dao.advancedSearch(realName, gender, address, startDate, endDate, index, count);
		} finally {
			dao.close();
		}
	}

	/**
	 * 获取前count个最受欢迎的用户,即其状态被评论次数最靠前的
	 * 
	 * @param count
	 *            返回的用户数目
	 * @return
	 */
	public List<BlogUser> getPopularUsers(int count) {

		try {
			dao.open();
			return dao.getPopularUsers(count);
		} finally {
			dao.close();
		}
	}

	/**
	 * 获取前count个最活跃的用户,即其所发布状态数和评论他人状态数之和靠前的
	 * 
	 * @param count
	 *            返回的用户数目
	 * @return
	 */
	public List<BlogUser> getActiveUsers(int count) {

		try {
			dao.open();
			return dao.getActiveUsers(count);
		} finally {
			dao.close();
		}
	}

	/**
	 * 获取用户的活跃度,发布状态数和评论他人状态数之和
	 * 
	 * @param user
	 *            所要查看的用户
	 * @return
	 */
	public int getActivesOf(BlogUser user) {
		long actives = (long) 0;

		if (user != null) {
			dao.open();
			actives = dao.getActivesOf(user.getId());
			dao.close();
		}

		return (int) actives;
	}

	/**
	 * 获取用户的受欢迎度,状态被评论次数
	 * 
	 * @param user
	 *            所要查看的用户
	 * @return
	 */
	public int getPopularsOf(BlogUser user) {
		long populars = (long) 0;

		if (user != null) {
			dao.open();
			populars = dao.getPopularsOf(user.getId());
			dao.close();
		}

		return (int) populars;
	}

	/**
	 * 获取用户的关注度,关注其他用户的数目
	 * 
	 * @param user
	 *            所要查看的用户
	 * @return
	 */
	public int getAttentionsOf(BlogUser user) {
		long attentions = (long) 0;

		if (user != null) {
			dao.open();
			attentions = dao.getAttentionsOf(user.getId());
			dao.close();
		}

		return (int) attentions;
	}

	/**
	 * 获取用户的粉丝数,被关注的次数
	 * 
	 * @param user
	 *            所要查看的用户
	 * @return
	 */
	public int getFansOf(BlogUser user) {
		long fans = (long) 0;

		if (user != null) {
			dao.open();
			fans = dao.getFansOf(user.getId());
			dao.close();
		}

		return (int) fans;
	}
}
