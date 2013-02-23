package com.nineteeneightyeight.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.nineteeneightyeight.entity.CommentEntity;
import com.nineteeneightyeight.util.ConstantUtil;

/**
 * 评论数据库操作类,负责评论的获取和保存等
 * 
 * @author flytreeleft
 * 
 */
public class CommentDao extends DBDao {

	@Override
	protected Object composeEntity(ResultSet resultSet) throws SQLException {
		CommentEntity comment = new CommentEntity();

		if (resultSet != null) {
			// 按字段填充数据
			comment.setId(resultSet.getInt("id"));
			comment.setStatusId(resultSet.getInt("status_id"));
			comment.setFromId(resultSet.getInt("from_id"));
			comment.setToId(resultSet.getInt("to_id"));
			comment.setCommentDate(resultSet.getString("comment_date"));
			comment.setComment(resultSet.getString("comment"));
		}

		return comment;
	}

	/**
	 * 获取状态的评论
	 * 
	 * @param statusId
	 *            状态ID
	 * @return
	 */
	public List<CommentEntity> getCommentsByStatusId(int statusId) {
		List<CommentEntity> commentList = new ArrayList<CommentEntity>();
		List<Object> list = null;
		String query = "";

		if (statusId > ConstantUtil.INVALID_STATUS_ID) {

			query = "select * from CommentTable where status_id = " + statusId
					+ " order by comment_date desc";

			try {
				// 得到搜索结果集合
				list = executeQuery(query, 0, 0);
				// 将获取的对象进行类型转换
				for (Object obj : list) {
					commentList.add((CommentEntity) obj);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return commentList;
	}

	/**
	 * 删除某条评论
	 * 
	 * @param commentId
	 *            评论ID
	 * @return
	 */
	public boolean delCommentById(int commentId) {
		String deleteStr = "";

		if (commentId <= ConstantUtil.INVALID_COMMENT_ID) {
			return false;
		}

		deleteStr = "delete from CommentTable where id = " + commentId + "";

		try {
			return executeUpdate(deleteStr);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 删除某状态下的所有评论
	 * 
	 * @param statusId
	 *            该状态ID
	 * @return
	 */
	public boolean delCommentByStatusId(int statusId) {
		String deleteStr = "";

		if (statusId <= ConstantUtil.INVALID_STATUS_ID) {
			return false;
		}

		deleteStr = "delete from CommentTable where status_id = " + statusId + "";

		try {
			return executeUpdate(deleteStr);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 保存评论对象
	 * 
	 * @param comment
	 *            评论对象
	 * @return
	 */
	public boolean addComment(CommentEntity comment) {
		String insertStr = "";

		if (comment == null) {
			return false;
		}

		insertStr = "insert into CommentTable (status_id,from_id,to_id,comment_date,comment) values("
				+ comment.getStatusId()
				+ ","
				+ comment.getFromId()
				+ ","
				+ comment.getToId()
				+ ",'" + comment.getCommentDate() + "','" + comment.getComment() + "')";
		try {
			return executeUpdate(insertStr);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
