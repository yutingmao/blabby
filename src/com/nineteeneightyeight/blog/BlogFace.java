package com.nineteeneightyeight.blog;

import com.nineteeneightyeight.util.FileUtil;

/**
 * 表情管理类,负责查看某个表情是否存在
 * 
 * @author flytreeleft
 * 
 */
public class BlogFace {
	private String facePath = "images/faces/";
	private String suffix = ".gif";

	public String getFacePath() {
		return facePath;
	}

	public void setFacePath(String facePath) {
		this.facePath = facePath;
	}

	/**
	 * 查看表情是否存在
	 * 
	 * @param faceName
	 *            表情名称
	 * @return
	 */
	public boolean faceExist(String faceName) {
		return new FileUtil().fileExist(facePath, faceName);
	}

	/**
	 * 获取表情的URL地址
	 * 
	 * @param faceName
	 *            表情名称
	 * @return
	 */
	public String getFaceUrl(String faceName) {
		return facePath + faceName + suffix;
	}
}
