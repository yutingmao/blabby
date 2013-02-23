package com.nineteeneightyeight.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.nineteeneightyeight.blog.BlogUser;
import com.nineteeneightyeight.util.ConstantUtil;

/**
 * 对数据库中UserTable进行操作的数据库控制类,对用户信息进行查询、插入、更改以及获取用户已关注的对象
 * 
 * @author flytreeleft
 * 
 */
public class UserDao extends DBDao {

	public UserDao() {

	}

	/**
	 * 通过ResultSet填充UserEntity实体
	 * 
	 * @throws SQLException
	 * */
	@Override
	protected Object composeEntity(ResultSet resultSet) throws SQLException {
		BlogUser user = new BlogUser();

		if (resultSet != null) {
			// 按字段填充数据
			user.setId(resultSet.getInt("id"));
			user.setName(resultSet.getString("name"));
			user.setRealName(resultSet.getString("realname"));
			user.setGender(resultSet.getInt("gender"));
			user.setPwd(resultSet.getString("pwd"));
			user.setAddress(resultSet.getString("address"));
			user.setBirthday(resultSet.getString("birthday"));
			user.setBrief(resultSet.getString("brief"));
		}

		return user;
	}

	/**
	 * 通过用户ID获取用户对象,该对象唯一
	 * 
	 * @param userId
	 *            用户ID
	 * @return UserEntity对象
	 * */
	public BlogUser getUserById(int userId) {
		BlogUser user = new BlogUser();
		String query = "";

		if (userId > ConstantUtil.INVALID_USER_ID) {

			query = "select * from UserTable where id = " + userId;

			try {
				Iterator<Object> it = executeQuery(query, 0, 1).iterator();
				if (it.hasNext()) {
					user = (BlogUser) it.next();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return user;
	}

	/**
	 * 通过用户名获取用户对象,该对象唯一
	 * 
	 * @param name
	 *            用户名
	 * @return
	 */
	public BlogUser getUserByName(String name) {
		BlogUser user = new BlogUser();
		String query = "";

		if (name != null && !name.isEmpty()) {

			name = name.replaceAll("'", "").replaceAll("%", "");

			query = "select * from UserTable where id > " + ConstantUtil.INVALID_USER_ID
					+ " and name = '" + name + "'";
			try {
				Iterator<Object> it = executeQuery(query, 0, 1).iterator();
				if (it.hasNext()) {
					user = (BlogUser) it.next();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return user;
	}

	/**
	 * 根据检索式搜索用户信息,返回记录为从编号为index开始,返回记录数为count
	 * 
	 * @param query
	 *            检索式
	 * @param index
	 *            返回记录的起始编号
	 * @param count
	 *            返回记录的总数,小于或等于0时返回所有记录
	 * @return
	 */
	private List<BlogUser> getUsersList(String query, int index, int count) {
		List<BlogUser> userList = new ArrayList<BlogUser>();
		List<Object> list = null;

		if (query != null && !query.isEmpty()) {

			if (index < 0) {
				index = 0;
			}
			if (count < 0) {
				count = 0;
			}

			try {
				// 得到搜索结果集合
				list = executeQuery(query, index, count);
				// 将获取的对象进行类型转换
				for (Object obj : list) {
					userList.add((BlogUser) obj);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return userList;
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

		String query = "select * from UserTable where id > " + ConstantUtil.INVALID_USER_ID
				+ " and name like '%%' ";

		if (realName != null && !realName.isEmpty()) {
			realName = realName.replaceAll("'", "").replaceAll("%", "");
			// 按真实名称搜索
			query += " and realname like '%" + realName + "%'";
		}

		if (gender >= 0 && gender <= 1) {
			// 按性别搜搜
			query += " and gender = " + gender + "";
		}

		if (address != null && !address.isEmpty()) {
			address = address.replaceAll("'", "").replaceAll("%", "");
			// 按地址搜索
			query += " and address like '%" + address + "%'";
		}

		if (startDate != null && !startDate.isEmpty()) {
			startDate = startDate.replaceAll("'", "").replaceAll("%", "");
			// 按起始出生日期搜搜
			query += " and birthday >= '" + startDate + "'";
		}

		if (endDate != null && !endDate.isEmpty()) {
			endDate = endDate.replaceAll("'", "").replaceAll("%", "");
			// 按终止出生日期搜索
			query += " and birthday <= '" + endDate + "'";
		}

		return getUsersList(query, index, count);
	}

	/**
	 * 获取最受欢迎的用户,即状态被回复数最高的用户,不包括用户评论自己的状态
	 * 
	 * @param count
	 *            返回的记录总数,小于等于0时返回空表
	 * @return
	 */
	public List<BlogUser> getPopularUsers(int count) {
		List<BlogUser> userList = new ArrayList<BlogUser>();
		String query = "";

		if (count > 0) {

			query = "select * from UserTable where id in "
					+ "(select to_id from CommentTable where from_id != to_id group by to_id order by count(to_id) desc)"
					+ " and id > " + ConstantUtil.INVALID_USER_ID;

			userList = getUsersList(query, 0, count);
		}

		return userList;
	}

	/**
	 * 获取活跃度最高的用户,即发状态和评论最多的用户
	 * 
	 * @param count
	 *            返回的记录数,小于等于0时返回空表
	 * @return
	 */
	public List<BlogUser> getActiveUsers(int count) {
		List<BlogUser> userList = new ArrayList<BlogUser>();
		String query = "";

		if (count > 0) {

			query = "select * from UserTable where id in "
					+ "(select id from ActiveTable group by id order by count(id) desc)"
					+ " and id > " + ConstantUtil.INVALID_USER_ID;

			userList = getUsersList(query, 0, count);
		}

		return userList;
	}

	/**
	 * 获取用户的活跃度,即所发布的状态数与已回复的状态数之和
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	public long getActivesOf(int userId) {
		Long actives = (long) 0;
		String query = "";

		if (userId > ConstantUtil.INVALID_USER_ID) {

			query = "select count(id) as actives from ActiveTable where id = " + userId;

			try {
				// 获取用户的活跃度
				Object obj = getValueOf("actives", query);
				if (obj != null) {
					actives = (Long) obj;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return actives;
	}

	/**
	 * 获取用户的受欢迎度,即状态被回复数
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	public long getPopularsOf(int userId) {
		Long populars = (long) 0;
		String query = "";

		if (userId > ConstantUtil.INVALID_USER_ID) {

			query = "select count(to_id) as populars from CommentTable where to_id = " + userId;

			try {
				// 获取用户的受欢迎度
				Object obj = getValueOf("populars", query);
				if (obj != null) {
					populars = (Long) obj;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return populars;
	}

	/**
	 * 获取用户的关注度,即该用户所关注的人数
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	public long getAttentionsOf(int userId) {
		Long attentions = (long) 0;
		String query = "";

		if (userId > ConstantUtil.INVALID_USER_ID) {

			query = "select count(viewer_id) as attentions from AttentionTable where viewer_id = "
					+ userId;

			try {
				// 获取用户的关注度
				Object obj = getValueOf("attentions", query);
				if (obj != null) {
					attentions = (Long) obj;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return attentions;
	}

	/**
	 * 获取用户的粉丝数,即被多少人关注
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	public long getFansOf(int userId) {
		Long fans = (long) 0;
		String query = "";

		if (userId > ConstantUtil.INVALID_USER_ID) {

			query = "select count(viewed_id) as fans from AttentionTable where viewed_id = "
					+ userId;

			try {
				// 获取用户的受关注度
				Object obj = getValueOf("fans", query);
				if (obj != null) {
					fans = (Long) obj;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return fans;
	}

	/**
	 * 获取关注某用户的所有用户的信息列表
	 * 
	 * @param viewedId
	 *            被关注者ID
	 * @return
	 */
	public List<BlogUser> getViewers(int viewedId) {
		List<BlogUser> viewersList = new ArrayList<BlogUser>();
		String query = "";

		if (viewedId > ConstantUtil.INVALID_USER_ID) {

			query = "select * from UserTable where id in "
					+ "( select viewer_id from AttentionTable where viewed_id = " + viewedId + ")";

			viewersList = getUsersList(query, 0, 0);
		}

		return viewersList;
	}

	/**
	 * 获取用户所关注的所有用户
	 * 
	 * @param viewerId
	 *            当前用户的ID
	 * */
	public List<BlogUser> getVieweds(int viewerId) {
		List<BlogUser> viewedsList = new ArrayList<BlogUser>();
		String query = "";

		if (viewerId > ConstantUtil.INVALID_USER_ID) {

			query = "select * from UserTable where id in "
					+ "(select viewed_id from AttentionTable where viewer_id = " + viewerId + ")";

			viewedsList = getUsersList(query, 0, 0);
		}

		return viewedsList;
	}

	/**
	 * 添加BlogUser对象
	 * 
	 * @param user
	 * @return 成功返回true,失败返回false
	 */
	public boolean addUser(BlogUser user) {
		String insertStr = "";

		if (user == null || user.getId() <= ConstantUtil.INVALID_USER_ID) {
			return false;
		}

		insertStr = "insert into UserTable (name,realname,gender,pwd,birthday,address,brief) values('"
				+ user.getName()
				+ "','"
				+ user.getRealname()
				+ "',"
				+ user.getGender()
				+ ",'"
				+ user.getPwd()
				+ "','"
				+ user.getBirthday()
				+ "','"
				+ user.getAddress()
				+ "','"
				+ user.getBrief() + "')";

		try {
			return executeUpdate(insertStr);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 更新BlogUser对象
	 * 
	 * @param user
	 * @return 成功返回true,失败返回false
	 */
	public boolean updateUser(BlogUser user) {
		String updateStr = "";

		if (user == null || user.getId() <= ConstantUtil.INVALID_USER_ID) {
			return false;
		}

		updateStr = "update UserTable set realname='" + user.getRealname() + "',gender="
				+ user.getGender() + ",pwd='" + user.getPwd() + "',birthday='" + user.getBirthday()
				+ "',address='" + user.getAddress() + "',brief='" + user.getBrief()
				+ "' where id = " + user.getId() + " and name = '" + user.getName() + "'";
		try {
			return executeUpdate(updateStr);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void main(String[] args) {
		UserDao dao = new UserDao();
		BlogUser user = new BlogUser();
		List<BlogUser> userList = null;

		dao.open();
		// userSet = dao.getUsersByAddress("def", 0, 0);
		// userSet = dao.getPopularUsers(10);
		// userSet = dao.getFriends(3);
		userList = dao.getActiveUsers(10);
		for (BlogUser entity : userList) {
			System.out.println(entity.getName());
		}

		user = dao.getUserByName("a' or '1'='1");
		System.out.println("user :" + user.getName());
		/*
		 * user = dao.getUserById(1); System.out.println(user.getName());
		 */

		/*
		 * user.setName("速度快经费上看到"); user.setPwd("da"); user.setGender(0);
		 * user.setAddress("aaaaaaa");
		 * 
		 * System.out.println(dao.saveUser(user));
		 */

		/*
		 * user = new BlogUser(); user.setName("adfkjsf"); user.setPwd("aa");
		 * user.setGender(1); user.setAddress("aaaaaaaaaaaaa");
		 * user.setBirthday(new Date(0));
		 */

		// System.out.println(dao.insertUserEntity(user));
		// dao.updateUserEntity(user);

		dao.close();
	}
}
