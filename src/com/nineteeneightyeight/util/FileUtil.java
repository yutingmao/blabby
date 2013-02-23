package com.nineteeneightyeight.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;

/**
 * 文件管理
 * 
 * @author flytreeleft
 * 
 */
public class FileUtil {
	private final int BUFFER_SIZE = 16 * 1024;
	private String tempPath;
	private final int MAX_FILE_SIZE = 10 * 1024 * 1024;

	public FileUtil() {
		super();
		// 初始化文件临时存放目录
		tempPath = SiteUtil.PHYSICAL_ADDRESS + "/temp";
	}

	/**
	 * 保存上传到服务器的文件
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param savePath
	 *            保存路径
	 * @param fileName
	 *            保存的文件名称,若为空或null,则按用时间标记作为保存的文件名称
	 * @param clearDir
	 *            是否删除目录下的文件
	 * @return 成功则返回true,否则返回false
	 */
	public boolean saveFile(HttpServletRequest request, String savePath, String fileName,
			boolean clearDir) {
		// 这里对request进行封装，RequestContext提供了对request多个访问方法
		RequestContext requestContext = new ServletRequestContext(request);
		// 判断表单是否是Multipart类型的,不是则返回false
		if (!FileUpload.isMultipartContent(requestContext)) {
			return false;
		}

		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 设置文件的缓存路径
		factory.setRepository(new File(tempPath));
		ServletFileUpload upload = new ServletFileUpload(factory);
		// 设置上传文件大小的上限，-1表示无上限
		upload.setSizeMax(MAX_FILE_SIZE);
		List<?> items = new ArrayList<Object>();
		try {
			// 上传文件，并解析出所有的表单字段，包括普通字段和文件字段
			items = upload.parseRequest(request);
		} catch (FileUploadException e1) {
			System.out.println("There are some problems with uploading files" + e1.getMessage());
			return false;
		}
		// 下面对每个字段进行处理，分普通字段和文件字段
		Iterator<?> it = items.iterator();
		while (it.hasNext()) {
			FileItem fileItem = (FileItem) it.next();
			// 如果不是普通字段
			if (!fileItem.isFormField()) {

				/*
				 * System.out.println(fileItem.getFieldName() + "   " +
				 * fileItem.getName() + "   " + fileItem.isInMemory() + "    " +
				 * fileItem.getContentType() + "   " + fileItem.getSize());
				 */
				// 保存文件，其实就是把缓存里的数据写到目标路径下
				if (fileItem.getName() != null && fileItem.getSize() != 0) {
					File file = null;
					String name = fileItem.getName();

					if (name != null) {
						savePath = SiteUtil.PHYSICAL_ADDRESS + "/" + savePath;

						if (fileName != null && !fileName.isEmpty()) {
							if (clearDir) {
								deleteFiles(savePath);
							}
							file = new File(savePath + "/" + fileName + getFileSuffix(name));
						} else {
							Calendar calendar = Calendar.getInstance();
							file = new File(savePath + "/"
									+ String.valueOf(calendar.getTimeInMillis())
									+ getFileSuffix(name));
						}

						try {
							fileItem.write(file);
						} catch (Exception e) {
							e.printStackTrace();
							return false;
						}
					}
				}
			}
		}

		return true;
	}

	/**
	 * 删除目录下的所有文件
	 * 
	 * @param filePath
	 *            目录路径
	 */
	public void deleteFiles(String filePath) {
		File dir = new File(filePath);

		if (dir.isDirectory()) {
			File[] files = dir.listFiles();

			if (files != null) {
				for (File file : files) {
					file.delete();
				}
			}
		}
	}

	/**
	 * 查看文件是否存在
	 * 
	 * @param fileDir
	 *            文件存放目录
	 * @param fileName
	 *            所要查看的文件名,不包括后缀
	 * @return
	 */
	public boolean fileExist(String fileDir, String fileName) {
		String realPath = "";

		if (fileDir != null && !fileDir.isEmpty() && fileName != null && !fileName.isEmpty()) {
			// 获取文件存放的真实物理地址
			realPath = SiteUtil.PHYSICAL_ADDRESS + "/" + fileDir;
			File dir = new File(realPath);
			// 搜索目录下所有文件,匹配文件名
			if (dir.isDirectory()) {
				File[] files = dir.listFiles();
				if (files != null) {
					for (File file : files) {
						String name = file.getName();
						// 去掉文件的后缀
						name = name.substring(0, name.lastIndexOf("."));
						// 相等,则表示文件存在
						if (name.equals(fileName)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * 复制文件
	 * 
	 * @param src
	 *            源文件
	 * @param dst
	 *            目标文件
	 * */
	public void copyFile(File src, File dst) {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new BufferedInputStream(new FileInputStream(src), BUFFER_SIZE);
			out = new BufferedOutputStream(new FileOutputStream(dst), BUFFER_SIZE);
			byte[] buffer = new byte[BUFFER_SIZE];
			int len = 0;
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 获取文件后缀名
	 * 
	 * @param fileName
	 *            文件名
	 * @return 文件后缀名称,包括"."
	 */
	private String getFileSuffix(String fileName) {
		String suffix = "";

		if (fileName != null && !fileName.isEmpty())
			suffix = fileName.substring(fileName.lastIndexOf("."));

		return suffix;
	}
}
