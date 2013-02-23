package com.nineteeneightyeight.manager;

import java.util.ArrayList;
import java.util.List;

import com.nineteeneightyeight.dao.CommentDao;
import com.nineteeneightyeight.dao.UserDao;
import com.nineteeneightyeight.entity.CommentEntity;
import com.nineteeneightyeight.entity.StatusEntity;

/**
 * 评论管理类,负责维护用户评论信息
 * 
 * @author flytreeleft
 * 
 */
public class CommentManager {
	private CommentDao dao;

	public CommentManager() {
		dao = new CommentDao();
	}

	/**
	 * 删除某状态下的所有评论
	 * 
	 * @param status
	 *            状态对象
	 * @return
	 */
	public boolean delCommentsOfStatus(StatusEntity status) {
		boolean succ = false;

		if (status != null) {
			succ = delCommentsOfStatus(status.getId());
		}

		return succ;
	}

	/**
	 * 删除某状态下的所有评论
	 * 
	 * @param statusId
	 *            状态的ID
	 * @return
	 */
	public boolean delCommentsOfStatus(int statusId) {

		try {
			dao.open();
			return dao.delCommentByStatusId(statusId);
		} finally {
			dao.close();
		}
	}

	/**
	 * 删除某条评论
	 * 
	 * @param commentId
	 *            所要删除的评论的ID
	 * @return
	 */
	public boolean delComment(int commentId) {

		try {
			dao.open();
			return dao.delCommentById(commentId);
		} finally {
			dao.close();
		}
	}

	/**
	 * 保存评论
	 * 
	 * @param comment
	 *            评论
	 * @return
	 */
	public boolean saveComment(CommentEntity comment) {
		boolean succ = false;

		if (comment != null) {
			dao.open();
			succ = dao.addComment(comment);
			dao.close();
		}

		return succ;
	}

	/**
	 * 获取某状态的所有评论
	 * 
	 * @param status
	 *            状态对象
	 * @return
	 */
	public List<CommentEntity> getCommentsOf(StatusEntity status) {
		List<CommentEntity> list = new ArrayList<CommentEntity>();

		if (status != null) {
			list = getCommentsOf(status.getId());
		}

		return list;
	}

	/**
	 * 获取状态的所有评论,同时获取发表评论的用户信息
	 * 
	 * @param statusId
	 *            状态ID
	 * @return
	 */
	public List<CommentEntity> getCommentsOf(int statusId) {
		List<CommentEntity> list = null;

		// 获取评论集合
		dao.open();
		list = dao.getCommentsByStatusId(statusId);
		dao.close();
		// 获取每个评论的发表者
		if (list != null && list.size() > 0) {
			UserDao userDao = new UserDao();

			userDao.open();
			for (CommentEntity comment : list) {
				comment.setCommentator(userDao.getUserById(comment.getFromId()));
			}
			userDao.close();
		}

		return list;
	}
}
