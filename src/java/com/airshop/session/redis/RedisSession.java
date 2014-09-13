package com.airshop.session.redis;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

public class RedisSession  implements HttpSession,Serializable {

	private static final long serialVersionUID = 8112578025779168737L;

    private Map<String, Object> store = new HashMap<String, Object>();
	
	private String uuid;
	
	private long creationTime;
	
	private long lastAccessedTime;
	
	private int maxIntervalSecond = -1;
	
	private boolean newInstance;
	
	private transient ServletContext sevletContext;
	
	public RedisSession(String uuid) {
		this.uuid = uuid;
		this.creationTime = System.currentTimeMillis();
		this.lastAccessedTime = creationTime;
		this.newInstance = true;
	}

	@Override
	public void setMaxInactiveInterval(int maxInactiveInterval) {
		this.maxIntervalSecond = maxInactiveInterval;
		RedisSessionManager.getInstance().saveSession(this, uuid);
	}	


	@Override
	public void setAttribute(String key, Object value) {
		lastAccessedTime = System.currentTimeMillis();
		store.put(key, value);
		RedisSessionManager.getInstance().saveSession(this, uuid);
	}

	@Override
	public void putValue(String key, Object value) {
		store.put(key, value);
		RedisSessionManager.getInstance().saveSession(this, key);
	}

	@Override
	public void removeAttribute(String key) {
		store.remove(key);
		RedisSessionManager.getInstance().saveSession(this, uuid);
	}

	@Override
	public void removeValue(String key) {
		store.remove(key);
		RedisSessionManager.getInstance().saveSession(this, uuid);
	}

	@Override
	public void invalidate() {
		store.clear();
		RedisSessionManager.getInstance().removeSession(uuid);
	}
	
	@Override
	public HttpSessionContext getSessionContext() {
		return RedisSessionManager.getInstance();
	}

	@Override
	public Object getAttribute(String key) {
		lastAccessedTime = System.currentTimeMillis();
		Object v = store.get(key);
		if(v != null) {
			RedisSessionManager.getInstance().saveSession(this, uuid);
		}
		return v;
	}

	@Override
	public Object getValue(String key) {
		lastAccessedTime = System.currentTimeMillis();
		return store.get(key);
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		return new Enumeration<String> () {

			private Iterator<String> iter = store.keySet().iterator();
			
			@Override
			public boolean hasMoreElements() {
				store.keySet();
				return iter.hasNext();
			}

			@Override
			public String nextElement() {
				return iter.next();
			}
		};
	}

	@Override
	public long getCreationTime() {
		return creationTime;
	}

	@Override
	public String getId() {
		return this.uuid;
	}

	@Override
	public long getLastAccessedTime() {
		return this.lastAccessedTime;
	}

	@Override
	public int getMaxInactiveInterval() {
		return this.maxIntervalSecond;
	}
	
	@Override
	public ServletContext getServletContext() {
		return this.sevletContext;
	}
	
	@Override
	public String[] getValueNames() {
		lastAccessedTime = System.currentTimeMillis();
		String[] keys = new String[store.keySet().size()];
		store.keySet().toArray(keys);
		return keys;
	}

	@Override
	public boolean isNew() {
		return this.newInstance;
	}
	
	/**
	 * 仅仅提供给 {@link RedisSessionManager#getSession(String)} 调用
	 * */
	void setOldInstance() {
		this.newInstance = false;
	}
	
	/**
	 * 仅仅提供给 {@link RedisSessionManager#getSession(String)} 调用
	 * */
	void setServletContext(ServletContext servletContext) {
		this.sevletContext = servletContext;
	}
	/**
	 * 仅仅提供给 {@link RedisSessionManager#getSession(String)} 调用
	 * */
	void setLastAccessTime(long lastAccessedTime) {
		this.lastAccessedTime = lastAccessedTime;
	}

}
