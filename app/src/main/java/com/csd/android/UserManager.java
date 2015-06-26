package com.csd.android;

import com.csd.android.model.User;

public class UserManager {
	private static User user;

	public static User getUser() {
		return user;
	}

	public static void setUser(User user) {
		UserManager.user = user;
	}

}
