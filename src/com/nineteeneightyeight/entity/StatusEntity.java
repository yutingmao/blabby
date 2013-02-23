package com.nineteeneightyeight.entity;

import com.nineteeneightyeight.blog.BlogUser;

/**
 * 状态实体类，负责保存和获取用户状态
 * 
 * @author flytreeleft
 * 
 */
public class StatusEntity {
	// 状态ID
	private int id = -1;
	// 状态发布时间
	private String publishDate = "";
	// 状态内容
	private String content = "";
	// 发布该状态的用户ID
	private int userId = -1;
	// 该状态的发布者
	private BlogUser publisher;

	public StatusEntity() {
		super();
	}

	public StatusEntity(int id) {
		super();
		this.id = id;
	}

	public StatusEntity(int id, String publishDate, String content) {
		super();
		this.id = id;
		this.publishDate = publishDate;
		this.content = content;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * @param publisher
	 *            本状态的发布者
	 */
	public void setPublisher(BlogUser publisher) {
		this.publisher = publisher;
	}

	/**
	 * @return返回状态的发布者
	 */
	public BlogUser getPublisher() {
		return publisher;
	}
}
