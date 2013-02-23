package com.nineteeneightyeight.manager;

import java.util.ArrayList;
import java.util.List;

import com.nineteeneightyeight.blog.BlogUser;
import com.nineteeneightyeight.dao.StatusDao;
import com.nineteeneightyeight.dao.UserDao;
import com.nineteeneightyeight.entity.StatusEntity;

/**
 * 状态管理类,负责维护用户状态信息
 * 
 * @author flytreeleft
 * 
 */
public class StatusManager {
	private StatusDao dao;

	public StatusManager() {
		dao = new StatusDao();
	}

	/**
	 * 获取最新的前count条状态
	 * 
	 * @param count
	 *            获取的状态数
	 * @return
	 */
	public List<StatusEntity> getStatuses(int count) {
		List<StatusEntity> list = new ArrayList<StatusEntity>();

		if (count > 0) {
			list = advancedSearch("", null, "", "", 0, count);
		}

		return list;
	}

	/**
	 * 搜索状态,简单搜索,仅针对状态内容
	 * 
	 * @param content
	 *            状态内容
	 * @param count
	 *            返回结果的数目,为0则返回所有状态
	 * @return
	 */
	public List<StatusEntity> searchStatuses(String content, int count) {
		return advancedSearch(content, null, "", "", 0, count);
	}

	/**
	 * 获取用户的状态信息,并获取每个状态的发布者
	 * 
	 * @param userIdArray
	 *            用户ID数组
	 * @param index
	 *            起始号 ,表示从编号(从0开始)为index的记录开始取数据
	 * @param count
	 *            所要取的记录总数 ,count=0标志获取所有记录
	 * @return
	 */
	public List<StatusEntity> getStatusesOf(int[] userIdArray, int index, int count) {
		List<StatusEntity> list = new ArrayList<StatusEntity>();

		if (userIdArray != null && userIdArray.length > 0) {
			list = advancedSearch("", userIdArray, "", "", index, count);
		}

		return list;
	}

	/**
	 * 获取某用户所发布的状态
	 * 
	 * @param user
	 *            用户对象
	 * @param index
	 *            起始号 ,表示从编号(从0开始)为index的记录开始取数据
	 * @param count
	 *            所要取的记录总数 ,count=0标志获取所有记录
	 * @return
	 */
	public List<StatusEntity> getStatusesOf(BlogUser user, int index, int count) {
		List<StatusEntity> list = new ArrayList<StatusEntity>();

		if (user != null) {
			int[] ids = new int[1];
			ids[0] = user.getId();

			list = advancedSearch("", ids, "", "", index, count);
		}

		return list;
	}

	/**
	 * 高级搜索状态,返回的状态中包含状态的发布者,可以按内容搜索、按用户搜索、按发布起止日期搜索或三者的组合
	 * 
	 * @param content
	 *            所要搜索的状态内容,为空则以剩下项为搜索条件
	 * @param userIdArray
	 *            用户ID数组,为空时以剩下项为搜索条件
	 * @param startDate
	 *            起始发布日期,为空则以剩下项为搜索条件
	 * @param endDate
	 *            终止发布日期,为空则以剩下项为搜索条件
	 * @param index
	 *            起始号 ,表示从编号(从0开始)为index的记录开始取数据
	 * @param count
	 *            所要取的记录总数 ,count=0标志获取所有记录
	 * @return
	 */
	public List<StatusEntity> advancedSearch(String content, int[] userIdArray, String startDate,
			String endDate, int index, int count) {

		List<StatusEntity> list = new ArrayList<StatusEntity>();

		// 获取状态集合
		dao.open();
		list = dao.advancedSearch(content, userIdArray, startDate, endDate, index, count);
		dao.close();
		// 获取每个状态的发布者
		if (!list.isEmpty()) {
			UserDao userDao = new UserDao();

			userDao.open();
			for (StatusEntity status : list) {
				status.setPublisher(userDao.getUserById(status.getUserId()));
			}
			userDao.close();
		}

		return list;
	}

	/**
	 * 保存用户状态
	 * 
	 * @param status
	 *            用户状态
	 * @return
	 */
	public boolean saveStatus(StatusEntity status) {
		boolean succ = false;

		if (status != null) {
			dao.open();
			succ = dao.addStatus(status);
			dao.close();
		}

		return succ;
	}

	/**
	 * 删除状态,其评论也将被全部删除
	 * 
	 * @param status
	 *            所要删除的状态
	 * @return
	 */
	public boolean delStatus(StatusEntity status) {
		boolean succ = false;

		if (status != null) {
			succ = delStatus(status.getId());
		}

		return succ;
	}

	/**
	 * 删除状态,其评论也将被全部删除
	 * 
	 * @param statusId
	 *            状态ID
	 * @return
	 */
	public boolean delStatus(int statusId) {

		try {
			// 删除本状态的所有评论
			new CommentManager().delCommentsOfStatus(statusId);
			// 删除本状态
			dao.open();
			return dao.delStatus(statusId);
		} finally {
			dao.close();
		}
	}
}
