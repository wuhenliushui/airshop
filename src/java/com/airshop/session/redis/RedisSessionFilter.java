package com.airshop.session.redis;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.airshop.session.consts.SessionConst;

/**
 * 基于filter使用redis session
 * 在web.xml中添加如下配置：
 <filter>
		<display-name>RedisSessionFilter</display-name>
		<filter-name>RedisSessionFilter</filter-name>
		<filter-class>com.airshop.session.redis.RedisSessionFilter</filter-class>
		<init-param>
			<param-name>uuidCookieName</param-name>
			<param-value>uuid</param-value>
		</init-param>
		<init-param>
			<param-name>uuidCookieDomain</param-name>
			<param-value>as.com</param-value>
		</init-param>
		<init-param>
			<param-name>uuidCookiePath</param-name>
			<param-value>/</param-value>
		</init-param>
		<init-param>
			<param-name>uuidCookieMaxAge</param-name>
			<param-value>-1</param-value>
		</init-param>
	</filter>
	<filter-mapping>
		<filter-name>RedisSessionFilter</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>
	
 */
public class RedisSessionFilter implements Filter {
	
	private  Log logger = LogFactory.getLog(this.getClass());

	private String uuidCookieName = SessionConst.UUID_COOKIE_NAME;

	private String uuidCookieDomain = SessionConst.UUID_COOKIE_DOMAIN;

	private String uuidCookiePath = SessionConst.UUID_COOKIE_PATH;
	
	private int uuidCookieMaxAge = SessionConst.UUID_COOKIE_MAXAGE;

	@Override
	public void init(FilterConfig config) throws ServletException {
		logger.debug("init redis session filter ......");
		if(config.getInitParameter("uuidCookieName") != null) {
			this.uuidCookieName = config.getInitParameter("uuidCookieName");
		}
		if(config.getInitParameter("uuidCookieDomain") != null) {
			this.uuidCookieDomain = config.getInitParameter("uuidCookieDomain");
		}
		if(config.getInitParameter("uuidCookiePath") != null) {
			this.uuidCookiePath = config.getInitParameter("uuidCookiePath");
		}
		if(config.getInitParameter("uuidCookieMaxAge") != null) {
			try {
				this.uuidCookieMaxAge = Integer.parseInt(config.getInitParameter("uuidCookieMaxAge"));
			} catch (NumberFormatException e) {
				throw new ServletException("uuidCookieMaxAge 配置项必须是整数，单位秒，"+
						"含义：uuidCookieMaxAge <0 表示30分钟；uuidCookieMaxAge=0 表示用不过期； uuidCookieMaxAge>0 表示过期时间");
			}
		}
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		Map<String,Cookie> cm = new CookiesMap(((HttpServletRequest) request).getCookies());
		Cookie uuidCookie = cm.get(uuidCookieName);
		String uuidCookieValue;
		if(uuidCookie == null || uuidCookie.getValue().equals("")) {//如果浏览器第一次访问本站，则创建一个UUID（注：Tomcat的JSESSIONID一般采用的是懒惰方式创建）
			uuidCookieValue = UUID.randomUUID().toString();//uuidCookieValue通过cookie存放在客户端，uuidCookieValue也就是uuidSession的Key
			Cookie uuidSessionCookie = new Cookie(uuidCookieName,uuidCookieValue);
			uuidSessionCookie.setDomain(uuidCookieDomain);
			uuidSessionCookie.setPath(uuidCookiePath);
			uuidSessionCookie.setMaxAge(uuidCookieMaxAge);

			((HttpServletResponse)response).addCookie(uuidSessionCookie);
		} else {
			uuidCookieValue = uuidCookie.getValue();
		}

		chain.doFilter(new RedisHttpRequest((HttpServletRequest) request,uuidCookieValue), 
												response);		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	
}
