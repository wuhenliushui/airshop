package com.airshop.session.redis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

public class RedisHttpRequest extends HttpServletRequestWrapper {

	private String uuid;
	
	public synchronized void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public RedisHttpRequest(HttpServletRequest request, String uuid) {
		super(request);
		this.setUuid(uuid);
	}
	
	@Override
	public HttpSession getSession() {
		return RedisSessionManager.getInstance(super.getSession().getServletContext()).getSession(uuid);
	}

	@Override
	public HttpSession getSession(boolean create) {
		return RedisSessionManager.getInstance(super.getSession().getServletContext()).getSession(uuid);
	}
}
