package com.nineteeneightyeight.blog;

import java.util.*;

/**
 * 广播消息处理类
 * 
 * @author flytreeleft
 * 
 */
public class BlogMessage {
	private String broadcaster;
	private String message;
	private Date date;

	public BlogMessage() {

	}

	public BlogMessage(String broadcaster, String message) {
		this.broadcaster = broadcaster;
		this.message = message;
		this.date = new Date();
	}

	public String getBroadcaster() {
		return broadcaster;
	}

	public void setBroadcaster(String broadcaster) {
		this.broadcaster = broadcaster;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * 消息是否为空
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return message != null ? message.isEmpty() : true;
	}

	@SuppressWarnings("deprecation")
	public String toString() {
		String msg = "<div><table><tr>";
		msg += "<td>" + date.toLocaleString() + "<br/>";
		msg += broadcaster + " 说: <br/>" + message;
		msg += "</td>" + "</tr></table></div>";

		return /*broadcaster + ":" + */msg; // 加上发送方名称防止自己发送的消息在己方显示
	}
}
