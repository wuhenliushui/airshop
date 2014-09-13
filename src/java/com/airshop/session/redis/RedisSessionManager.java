package com.airshop.session.redis;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

public class RedisSessionManager extends RedisOperater<RedisSession> implements HttpSessionContext {
	
	/** 保存所有的SessionID的ID号*/
	private List<String> ids = new Vector<String>();
	private static ServletContext servletContext = null;
	private static RedisSessionManager instance;
	
	private RedisSessionManager() {}

	public synchronized static RedisSessionManager getInstance(ServletContext context) {
		servletContext = context;
		return getInstance();
	}
	
	public synchronized static RedisSessionManager getInstance() {
		if(instance == null) {
			instance = new RedisSessionManager();
		}
		return instance;
	}
	
	public HttpSession getSession(String uuid) {
		RedisSession session = this.get(uuid);
		if(session == null) {
			session = new RedisSession(uuid);
			this.saveSession(session, uuid);
			ids.add(uuid);
		} else {
			if(System.currentTimeMillis() - session.getLastAccessedTime() > genMaxInactiveMillis(session)) {//session失效
				session = new RedisSession(uuid);
				this.saveSession(session, uuid);
			}
			session.setOldInstance();//以表示该实例在RedisSession上不是新建的
		}
		session.setServletContext(servletContext);
		session.setLastAccessTime(System.currentTimeMillis());
		return session;
	}	
	
	public void saveSession(RedisSession session, String uuid) {
		this.set(session, uuid);
	}
	
	public void removeSession(String uuid) {
		this.delete(uuid);
		ids.remove(uuid);
	}
	
	public Enumeration<String> getIds() {
		return new Enumeration<String>() {

			private Iterator<String> iter = ids.iterator();

			@Override
			public boolean hasMoreElements() {
				return iter.hasNext();
			}

			@Override
			public String nextElement() {
				return iter.next();
			}
		};
	}
	
	private long genMaxInactiveMillis(RedisSession session) {
		/*
		 * maxInactive < 0   则：取默认值30分钟；
		 * maxInactive = 0  则：永不失效；
		 * maxInactive > 0  则：用户自定义的时间失效
		 * */
		long maxInactive = Long.MAX_VALUE;
		if(session.getMaxInactiveInterval() < 0 ) {
			maxInactive = 30 * 60 * 1000;
		} else if(session.getMaxInactiveInterval() > 0) {
			maxInactive = session.getMaxInactiveInterval() * 1000;
		}
		return maxInactive;
	}
}
