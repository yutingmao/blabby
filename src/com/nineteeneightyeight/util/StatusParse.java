package com.nineteeneightyeight.util;

import com.nineteeneightyeight.blog.BlogFace;

/**
 * 状态解析类,负责解析状态中的表情和图片等非字符内容
 * 
 * @author flytreeleft
 * 
 */
public class StatusParse {
	private String status;

	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 状态解析,解析后的字符串含有HTML标签.解析中将"[]"中间的字符解释为表情名称,将"()"中间的字符解释为图片名称,
	 * 若所解释的非字符内容在服务器上存在,则添加上URL路径
	 * 
	 * @return
	 */
	public String parse() {
		String content = "";
		String statusTemp = "";
		String temp = "";
		BlogFace face = new BlogFace();
		int startMark = 0;
		int endMark = 0;

		statusTemp = status;
		while (statusTemp != null && !statusTemp.isEmpty()) {
			// 找到第一个"]"
			endMark = statusTemp.indexOf("]");
			if (endMark >= 0) { // 存在"]",则尝试找"["
				// 截取第一个"]"之前包括"]"的字符串
				temp = statusTemp.substring(0, endMark + 1);
				// 找到截取的字符串中最后一个"["
				startMark = temp.lastIndexOf("[");

				if (startMark >= 0 && (startMark + 1) != endMark) { // "[""]"中间有字符
					String markStr = temp.substring(startMark); // 取出"[]"中间的字符,包括"["和"]"
					// temp中去除"[]"中间的字符
					temp = temp.substring(0, startMark);
					// 获取表情的名称,去除"["和"]"
					String faceName = markStr.replace("[", "").replace("]", "");
					if (face.faceExist(faceName)) {
						// 表情存在,则将原来"[XXX]"替换为表情URL
						markStr = "<img style=\"width:18px;height:18px;\" src=\""
								+ face.getFaceUrl(faceName) + "\"/>";
					}
					// 重新组装
					temp += markStr;
				}
				content += temp;
				// 继续查找剩下的字符串
				statusTemp = statusTemp.substring(endMark + 1);
			} else { // 不存在,则结束查找
				content += statusTemp;
				break;
			}
		}

		return content;
	}

	public static void main(String[] args) {
		String content = "";
		String statusTemp = "askdfjkasdf[sadf][asdf[[[";
		String temp = "";
		BlogFace face = new BlogFace();
		int startMark = 0;
		int endMark = 0;

		while (statusTemp != null && !statusTemp.isEmpty()) {
			endMark = statusTemp.indexOf("]");
			if (endMark >= 0) {

				temp = statusTemp.substring(0, endMark + 1);
				startMark = temp.lastIndexOf("[");

				if (startMark >= 0 && (startMark + 1) != endMark) {
					String markStr = temp.substring(startMark);
					temp = temp.substring(0, startMark);

					String faceName = markStr.replace("[", "").replace("]", "");
					if (true) {
						markStr = "<img src=\"" + face.getFaceUrl(faceName) + "\"/>";
					}
					temp += markStr;
				}
				content += temp;
				statusTemp = statusTemp.substring(endMark + 1);
			} else {
				content += statusTemp;
				break;
			}
		}
		System.out.println(content);
	}
}
