package com.nineteeneightyeight.util;

/**
 * 站点工具类,负责维护站点的实际物理地址
 * 
 * @author flytreeleft
 * 
 */
public class SiteUtil {
	/** 站点在服务器上的物理地址,文件操作时需要,在用户请求时初始化该值 */
	public static String PHYSICAL_ADDRESS = "";
	/** 默认头像URL */
	public static final String DEFAULT_PHOTO_URL = "/Microblog/users/default/photo/default.jpg";
}
