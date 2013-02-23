package com.nineteeneightyeight.blog;

import com.nineteeneightyeight.entity.UserEntity;

/**
 * 微博用户类,继承自UserEntity,负责维护用户资源信息
 * 
 * @author flytreeleft
 * 
 */
public class BlogUser extends UserEntity {
	private BlogPhoto blogPhoto = new BlogPhoto(); // 用户头像对象
	private BlogMusic blogMusic = new BlogMusic(); // 用户背景音乐对象

	public BlogUser() {
		super();
	}

	public BlogUser(int id, String name, int gender, String pwd) {
		super(id, name, gender, pwd);
	}

	public BlogUser(int id) {
		super(id);
	}

	/**
	 * 获取微博用户头像URL地址
	 * 
	 * @return
	 */
	public String getPhotoUrl() {
		return getBlogPhoto().getPhotoUrl();
	}

	/**
	 * 获取微博用户的头像对象
	 * 
	 * @return
	 */
	public BlogPhoto getBlogPhoto() {
		blogPhoto.setUserName(getName());

		return blogPhoto;
	}

	/**
	 * 获取背景音乐URL地址
	 * 
	 * @return
	 */
	public String getMusicUrl() {
		return getBlogMusic().getMusicUrl();
	}

	/**
	 * 获取背景音乐对象
	 * 
	 * @return
	 */
	public BlogMusic getBlogMusic() {
		blogMusic.setUserName(getName());

		return blogMusic;
	}

	/**
	 * 获取相册管理对象
	 * 
	 * @return
	 */
	public BlogAlbum getBlogAlbum() {
		BlogAlbum album = new BlogAlbum(this.getName());

		album.init();

		return album;
	}
}
