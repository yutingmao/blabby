package com.nineteeneightyeight.blog;

import java.io.File;

import com.nineteeneightyeight.util.SiteUtil;


/**
 * 用户背景音乐管理类
 * 
 * @author flytreeleft
 * 
 */
public class BlogMusic {
	private String userName = ""; // 背景音乐用户的名称

	public BlogMusic() {

	}

	public BlogMusic(String userName) {
		this.userName = userName;
	}

	/**
	 * 获取背景音乐的URL地址
	 * 
	 * @return 如果背景音乐不存在,则返回空
	 */
	public String getMusicUrl() {
		String url = "";
		String musicPath = SiteUtil.PHYSICAL_ADDRESS + getSavePath();
		File file = new File(musicPath);
		File[] files = file.listFiles();

		if (files != null && files.length > 0) {
			url = "users/" + userName + "/music/" + files[0].getName();
		}

		return url;
	}

	/**
	 * 获取背景音乐保存相对路径
	 * 
	 * @return
	 */
	public String getSavePath() {
		return "users/" + userName + "/music/";
	}

	/**
	 * 获取背景音乐名称
	 * 
	 * @return
	 */
	public String getMusicName() {
		return "background";
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
