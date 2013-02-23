package com.nineteeneightyeight.blog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nineteeneightyeight.util.SiteUtil;

/**
 * 用户相册管理类,使用之前需调用init()初始化对象
 * 
 * @author flytreeleft
 * 
 */
public class BlogAlbum {
	private String userName;
	// 存储相册的Map集合
	private Map<String, List<String>> albumsMap = new HashMap<String, List<String>>();

	public BlogAlbum() {
		super();
	}

	public BlogAlbum(String userName) {
		super();
		this.userName = userName;
	}

	/**
	 * 初始化相册,首先搜索相册目录下的相册,然后再搜索每个相册下的图片文件
	 */
	public void init() {
		String realPath = this.getRealPath();
		File albumFile = new File(realPath);
		// 搜索相册目录
		if (albumFile.isDirectory()) {
			File[] albums = albumFile.listFiles();
			if (albums != null) {
				// 搜索相册,获取其名称,再搜索每个相册下的文件
				for (File album : albums) {
					// 搜索相册中的文件
					if (album != null && album.isDirectory()) {
						File[] pictures = album.listFiles();
						List<String> picList = new ArrayList<String>();
						// 获取相册中的图片名称
						if (pictures != null) {
							for (File pic : pictures) {
								picList.add(pic.getName());
							}
						}
						albumsMap.put(album.getName(), picList);
					}
				}
			}
		}
	}

	/**
	 * 获取相册目录下的所有相册名称
	 * 
	 * @return
	 */
	public List<String> getAlbums() {
		List<String> albums = new ArrayList<String>();

		for (String name : albumsMap.keySet()) {
			albums.add(name);
		}

		return albums;
	}

	/**
	 * 获取某相册中的所有图片
	 * 
	 * @param albumName
	 *            相册名称
	 * @return
	 */
	public List<String> getPicturesOf(String albumName) {
		return albumsMap.get(albumName);
	}

	/**
	 * 获取图片的URL地址
	 * 
	 * @param albumName
	 *            相册名称
	 * @param pictureName
	 *            图片名称
	 * @return
	 */
	public String getPictureUrl(String albumName, String pictureName) {
		return getAlbumUrl(albumName) + "/" + pictureName;
	}

	/**
	 * 获取相册URL地址
	 * 
	 * @param albumName
	 *            相册名称
	 * @return
	 */
	public String getAlbumUrl(String albumName) {
		return getAlbumSavePath(albumName);
	}

	/**
	 * 获取相册的在服务器上的保存路径
	 * 
	 * @param albumName
	 *            相册名称
	 * @return
	 */
	public String getAlbumSavePath(String albumName) {
		return getAlbumDirPath() + "/" + albumName;
	}

	/**
	 * 获取相册目录的相对地址
	 * 
	 * @return
	 */
	private String getAlbumDirPath() {
		return "users/" + userName + "/album/";
	}

	/**
	 * 获取相册在服务器上的实际存储的路径
	 * 
	 * @return
	 */
	private String getRealPath() {
		return SiteUtil.PHYSICAL_ADDRESS + "/" + this.getAlbumDirPath();
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Map<String, List<String>> getAlbumsMap() {
		return albumsMap;
	}

	public void setAlbumsMap(Map<String, List<String>> albumsMap) {
		this.albumsMap = albumsMap;
	}
}
