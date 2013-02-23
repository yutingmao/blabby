package com.nineteeneightyeight.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.nineteeneightyeight.entity.StatusEntity;
import com.nineteeneightyeight.util.ConstantUtil;

/**
 * 状态数据库操作类,负责从数据库中获取和保存状态信息等
 * 
 * @author flytreeleft
 * 
 */
public class StatusDao extends DBDao {

	@Override
	protected Object composeEntity(ResultSet resultSet) throws SQLException {
		StatusEntity status = new StatusEntity();

		if (resultSet != null) {
			// 按字段填充数据
			status.setId(resultSet.getInt("id"));
			status.setUserId(resultSet.getInt("user_id"));
			status.setPublishDate(resultSet.getString("publish_date"));
			status.setContent(resultSet.getString("content"));
		}

		return status;
	}

	/**
	 * 通过状态ID获取StatusEntity对象
	 * 
	 * @param statusId
	 *            状态ID
	 * @return
	 */
	public StatusEntity getStatusById(int statusId) {
		StatusEntity status = new StatusEntity();
		String query = "";

		if (statusId > ConstantUtil.INVALID_STATUS_ID) {

			query = "select * from StatusTable where id = " + statusId;

			try {
				Iterator<Object> it = executeQuery(query, 0, 1).iterator();
				if (it.hasNext()) {
					status = (StatusEntity) it.next();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return status;
	}

	/**
	 * 高级搜索状态,可以按内容搜索、按用户搜索、按发布起止日期搜索或三者的组合
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
		List<StatusEntity> statusList = new ArrayList<StatusEntity>();
		List<Object> list = null;
		String query = "select * from StatusTable ";

		if (content == null || content.isEmpty()) {
			// 若所搜索的内容为空,则处理为搜索全部状态
			content = "";
		}
		query += " where content like '%" + content + "%'";

		if (userIdArray != null && userIdArray.length > 0) {
			String ids = "";
			for (int id : userIdArray) {
				ids += id + ",";
			}
			ids = ids.substring(0, ids.length() - 1);
			// 按用户搜索
			query += " and user_id in (" + ids + ")";
		}
		if (startDate != null && !startDate.isEmpty()) {
			// 按发布状态的起始日期搜索
			query += " and publish_date >= '" + startDate + "'";
		}
		if (endDate != null && !endDate.isEmpty()) {
			// 按发布状态的终止日期搜索
			query += " and publish_date <= '" + endDate + "'";
		}
		// 按发布时间降序排列
		query += " order by publish_date desc";

		try {
			// 得到搜索结果集合
			list = executeQuery(query, index, count);
			// 将获取的对象进行类型转换
			for (Object obj : list) {
				statusList.add((StatusEntity) obj);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return statusList;
	}

	/**
	 * 删除状态
	 * 
	 * @param statusId
	 *            状态ID
	 * @return
	 */
	public boolean delStatus(int statusId) {
		String deleteStr = "";

		if (statusId <= ConstantUtil.INVALID_STATUS_ID) {
			return false;
		}

		deleteStr = "delete from StatusTable where id = " + statusId + "";

		try {
			return executeUpdate(deleteStr);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 保存状态
	 * 
	 * @param status
	 *            状态对象
	 * @return 成功返回true,否则返回false
	 */
	public boolean addStatus(StatusEntity status) {
		String insertStr = "";

		if (status == null || status.getUserId() <= ConstantUtil.INVALID_USER_ID) {
			return false;
		}

		insertStr = "insert into StatusTable (user_id,publish_date,content) values("
				+ status.getUserId() + ",'" + status.getPublishDate() + "','" + status.getContent()
				+ "')";
		try {
			return executeUpdate(insertStr);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
