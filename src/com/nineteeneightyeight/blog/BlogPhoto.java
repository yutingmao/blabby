package com.nineteeneightyeight.blog;

import java.io.File;

import com.nineteeneightyeight.util.SiteUtil;

/**
 * 用户头像管理类
 * 
 * @author flytreeleft
 * 
 */
public class BlogPhoto {
	private String userName = ""; // 头像用户的名称

	public BlogPhoto() {

	}

	public BlogPhoto(String userName) {
		this.userName = userName;
	}

	/**
	 * 获取头像的URL地址
	 * 
	 * @return 如果头像不存在,则返回默认头像
	 */
	public String getPhotoUrl() {
		String url = SiteUtil.DEFAULT_PHOTO_URL;
		String photoPath = SiteUtil.PHYSICAL_ADDRESS + getSavePath();
		File file = new File(photoPath);
		File[] files = file.listFiles();

		if (files != null && files.length > 0) {
			url = "users/" + userName + "/photo/" + files[0].getName();
		}

		return url;
	}

	/**
	 * 获取头像保存相对路径
	 * 
	 * @return
	 */
	public String getSavePath() {
		return "users/" + userName + "/photo/";
	}

	/**
	 * 获取头像名称
	 * 
	 * @return
	 */
	public String getPhotoName() {
		return "photo";
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
