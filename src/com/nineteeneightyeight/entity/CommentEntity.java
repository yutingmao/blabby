package com.nineteeneightyeight.entity;

import com.nineteeneightyeight.blog.BlogUser;

/**
 * 评论实体类，负责保存和获取用户评论情况
 * 
 * @author flytreeleft
 * 
 */
public class CommentEntity {
	// 评论ID
	private int id = -1;
	// 所评论状态的ID
	private int statusId = -1;
	// 发布该评论的用户的ID
	private int fromId = -1;
	// 所评论状态所对应用户的ID
	private int toId = -1;
	// 评论发表的时间
	private String commentDate = "";
	// 评论的内容
	private String comment = "";
	// 发表评论的用户
	private BlogUser commentator;

	public CommentEntity() {
		super();
	}

	public CommentEntity(int id) {
		super();
		this.id = id;
	}

	public CommentEntity(int id, int statusId, int fromId, int toId,
			String commentDate, String comment) {
		super();
		this.id = id;
		this.statusId = statusId;
		this.fromId = fromId;
		this.toId = toId;
		this.commentDate = commentDate;
		this.comment = comment;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	/**
	 * 获取发布该评论的用户的ID
	 * */
	public int getFromId() {
		return fromId;
	}

	/**
	 * 保存发布该评论的用户的ID
	 * */
	public void setFromId(int fromId) {
		this.fromId = fromId;
	}

	/**
	 * 获取所评论状态所对应的用户的ID
	 * */
	public int getToId() {
		return toId;
	}

	/**
	 * 保存所评论状态所对应的用户的ID
	 * */
	public void setToId(int toId) {
		this.toId = toId;
	}

	public String getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(String commentDate) {
		this.commentDate = commentDate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return返回该评论的发布者
	 */
	public BlogUser getCommentator() {
		return commentator;
	}

	/**
	 * @param commentator
	 *            发布该评论的用户
	 */
	public void setCommentator(BlogUser commentator) {
		this.commentator = commentator;
	}
}
