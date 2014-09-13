package com.airshop.session.redis;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.Cookie;

public class CookiesMap implements Map<String, Cookie> {
	private Map<String, Cookie> store = new LinkedHashMap<String, Cookie>();

	public CookiesMap(Cookie[] arr) {
		for (Cookie c : arr)
			this.store.put(c.getName(), c);
	}

	public Cookie[] toArray() {
		Cookie[] ca = new Cookie[this.store.keySet().size()];
		this.store.values().toArray(ca);
		return ca;
	}

	public void clear() {
		this.store.clear();
	}

	public boolean containsKey(Object key) {
		return this.store.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return this.store.containsValue(value);
	}

	public Set<Map.Entry<String, Cookie>> entrySet() {
		return this.store.entrySet();
	}

	public Cookie get(Object key) {
		return (Cookie) this.store.get(key);
	}

	public boolean isEmpty() {
		return this.store.isEmpty();
	}

	public Set<String> keySet() {
		return this.store.keySet();
	}

	public Cookie put(String key, Cookie value) {
		return (Cookie) this.store.put(key, value);
	}

	public void putAll(Map<? extends String, ? extends Cookie> t) {
		this.store.putAll(t);
	}

	public Cookie remove(Object key) {
		return (Cookie) this.store.remove(key);
	}

	public int size() {
		return this.store.size();
	}

	public Collection<Cookie> values() {
		return this.store.values();
	}
}