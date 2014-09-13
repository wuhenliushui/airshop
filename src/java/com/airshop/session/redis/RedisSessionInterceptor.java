package com.airshop.session.redis;

import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.airshop.session.consts.SessionConst;

/**
 * springmvc interceptor方式使用redis session
 * @author liuqian
 */
public class RedisSessionInterceptor extends HandlerInterceptorAdapter implements InitializingBean {

	private  Log logger = LogFactory.getLog(this.getClass());

	@Value("${session.redis.uuidCookieName}")
	private String uuidCookieName;

	@Value("${session.redis.uuidCookieDomain}")
	private String uuidCookieDomain;

	@Value("${session.redis.uuidCookiePath}")
	private String uuidCookiePath;
	
	@Value("${session.redis.uuidCookieMaxAge}")
	private Integer uuidCookieMaxAge;
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		Map<String,Cookie> cm = new CookiesMap(request.getCookies());
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
		return super.preHandle(request, response, handler);
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		logger.debug("check redis session interceptor ......");
		if(StringUtils.isEmpty(this.uuidCookieName)) {
			this.uuidCookieName = SessionConst.UUID_COOKIE_NAME;
		}
		if(StringUtils.isEmpty(this.uuidCookieDomain)) {
			this.uuidCookieDomain = SessionConst.UUID_COOKIE_DOMAIN;
		}
		if(StringUtils.isEmpty(this.uuidCookiePath)) {
			this.uuidCookiePath = SessionConst.UUID_COOKIE_PATH;
		}
		if(this.uuidCookieMaxAge == null) {
			this.uuidCookieMaxAge = SessionConst.UUID_COOKIE_MAXAGE;
		} 
	}

}
