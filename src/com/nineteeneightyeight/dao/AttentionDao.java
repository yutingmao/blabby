package com.nineteeneightyeight.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.nineteeneightyeight.util.ConstantUtil;

/**
 * 关注表数据库操作对象,负责关系的添加、删除和查询关注关系是否存在
 * 
 * @author flytreeleft
 * 
 */
public class AttentionDao extends DBDao {

	@Override
	protected Object composeEntity(ResultSet resultSet) throws SQLException {
		return null;
	}

	/**
	 * 查看是否存在关注记录,即viewer_id为viewerId和viewed_id为viewedId的记录是否存在
	 * 
	 * @param viewerId
	 *            关注者ID
	 * @param viewedId
	 *            被关注者ID
	 * @return
	 */
	public boolean existAttention(int viewerId, int viewedId) {
		String query = "";
		Object obj = null;
		boolean exist = false;

		if (viewerId > ConstantUtil.INVALID_USER_ID && viewedId > ConstantUtil.INVALID_USER_ID) {

			query = "select count(viewed_id) as size from AttentionTable where viewer_id = "
					+ viewerId + " and viewed_id = " + viewedId;
			try {
				// 获取检索结果
				obj = getValueOf("size", query);
				// 符合条件的记录存在
				if (obj != null) {
					exist = true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return exist;
	}

	/**
	 * 添加关注
	 * 
	 * @param viewerId
	 *            当前用户ID
	 * @param viewedId
	 *            所要关注的用户ID
	 * */
	public boolean addAttention(int viewerId, int viewedId) {
		String insertStr = "";
		boolean succ = false;

		if (viewerId > ConstantUtil.INVALID_USER_ID && viewedId > ConstantUtil.INVALID_USER_ID) {

			insertStr = "insert into AttentionTable values(" + viewerId + "," + viewedId + ")";

			try {
				succ = executeUpdate(insertStr);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return succ;
	}

	/**
	 * 删除关注关系
	 * 
	 * @param viewerId
	 *            关注者ID
	 * @param viewedId
	 *            被关注者ID
	 * @return 删除成功为true,否则为false
	 */
	public boolean delAttention(int viewerId, int viewedId) {
		String deleteStr = "";
		boolean succ = false;

		if (viewerId > ConstantUtil.INVALID_USER_ID && viewedId > ConstantUtil.INVALID_USER_ID) {

			deleteStr = "delete from AttentionTable where viewer_id = " + viewerId
					+ " and viewed_id = " + viewedId;
			try {
				succ = executeUpdate(deleteStr);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return succ;
	}
}
