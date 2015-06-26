package com.csd.android.utils;

import java.util.HashMap;

public class SessionUtil {

	private HashMap<Object, Object> container;

	private static SessionUtil session;

	private SessionUtil() {
		container = new HashMap<Object, Object>();
	}

	public static SessionUtil getSession() {
		if (session == null) {
			session = new SessionUtil();
			return session;
		}
		else {
			return session;
		}
	}

	public void put(Object key, Object value) {
		container.put(key, value);
	}

	public Object get(Object key) {
		return container.get(key);
	}

	public void cleanUpSession() {
		container.clear();
	}

	public void remove(Object key) {
		container.remove(key);
	}

}
