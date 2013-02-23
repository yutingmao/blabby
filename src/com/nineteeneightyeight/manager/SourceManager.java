package com.nineteeneightyeight.manager;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import com.nineteeneightyeight.blog.BlogAlbum;
import com.nineteeneightyeight.blog.BlogMusic;
import com.nineteeneightyeight.blog.BlogPhoto;
import com.nineteeneightyeight.blog.BlogUser;
import com.nineteeneightyeight.util.FileUtil;
import com.nineteeneightyeight.util.SiteUtil;

/**
 * 服务器资源管理类,负责为每个注册的用户创建服务器资源和空间,在用户登录时要检测其资源是否存在
 * 
 * @author flytreeleft
 * 
 */
public class SourceManager {
	private HttpServletRequest request = null;

	public SourceManager(HttpServletRequest request) {
		super();
		this.request = request;
	}

	/**
	 * 为用户分配服务器资源
	 * 
	 * @param user
	 *            所要分配空间的用户
	 */
	public void allocateSourceFor(BlogUser user) {
		String homePath = "";
		File file = null;

		if (user != null) {
			homePath = SiteUtil.PHYSICAL_ADDRESS + "/users/" + user.getName();
			// 创建相册目录
			file = new File(homePath + "/album/");
			if (!file.exists()) {
				file.mkdirs();
			}
			// 创建头像目录
			file = new File(homePath + "/photo/");
			if (!file.exists()) {
				file.mkdirs();
			}
			// 创建背景音乐目录
			file = new File(homePath + "/music/");
			if (!file.exists()) {
				file.mkdirs();
			}
		}
	}

	/**
	 * 保存头像
	 * 
	 * @param photo
	 *            用户的微博头像对象
	 * @return
	 */
	public boolean savePhoto(BlogPhoto photo) {
		if (photo != null) {
			FileUtil fileUtil = new FileUtil();

			return fileUtil.saveFile(request, photo.getSavePath(), photo.getPhotoName(), true);
		}

		return false;
	}

	/**
	 * 保存背景音乐
	 * 
	 * @param music
	 *            用户的背景音乐对象
	 * @return
	 */
	public boolean saveMusic(BlogMusic music) {
		if (music != null) {
			FileUtil fileUtil = new FileUtil();

			return fileUtil.saveFile(request, music.getSavePath(), music.getMusicName(), true);
		} else {
			return false;
		}
	}

	/**
	 * 保存图片到用户相册中
	 * 
	 * @param album
	 *            用户的相册对象
	 * @param albumName
	 *            相册名称
	 * @return
	 */
	public boolean saveAlbumOfPicture(BlogAlbum album, String albumName) {
		if (album != null) {
			FileUtil fileUtil = new FileUtil();

			return fileUtil.saveFile(request, album.getAlbumSavePath(albumName), "", false);
		} else {
			return false;
		}
	}
}
