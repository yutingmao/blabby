package com.nineteeneightyeight.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.nineteeneightyeight.util.SiteUtil;

/**
 * 用于用户请求资源时,首先初始化站点信息,此处获取的是站点的物理地址
 * 
 * @author flytreeleft
 * 
 */
public class SiteFilter implements Filter {

	/**
	 * Default constructor.
	 */
	public SiteFilter() {

	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {

	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		// 站点物理地址为空,则获取物理地址
		if (SiteUtil.PHYSICAL_ADDRESS.isEmpty()) {

			HttpServletRequest req = (HttpServletRequest) request;

			SiteUtil.PHYSICAL_ADDRESS = req.getSession().getServletContext().getRealPath("/");
		}

		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {

	}

}
