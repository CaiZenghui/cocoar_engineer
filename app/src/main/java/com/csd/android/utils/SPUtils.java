package com.csd.android.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.csd.android.CCApplication;

/**
 *  对SharedPreferences的简单封装 
 * @author caizenghui
 *
 */
public class SPUtils {
	
	public static String getSharePrefStr(String spName, String field) {
		SharedPreferences sp = (SharedPreferences) CCApplication.getApplication().getSharedPreferences(spName, Context.MODE_PRIVATE);
		String s = sp.getString(field, "");
		return s;
	}

	public static int getSharePrefInt(String spName, String field) {
		SharedPreferences sp = (SharedPreferences) CCApplication.getApplication().getSharedPreferences(spName, Context.MODE_PRIVATE);
		int i = sp.getInt(field, 0);
		return i;
	}

	public static int getSharePrefInt(String spName, String field, int defValue) {
		SharedPreferences sp = (SharedPreferences) CCApplication.getApplication().getSharedPreferences(spName, Context.MODE_PRIVATE);
		int i = sp.getInt(field, defValue);
		return i;
	}
	
	public static boolean getSharePrefBoolean(String spName, String field, boolean defValue) {
		SharedPreferences sp = (SharedPreferences) CCApplication.getApplication().getSharedPreferences(spName, Context.MODE_PRIVATE);
		boolean i = sp.getBoolean(field, defValue);
		return i;
	}

	public static void putSharePref(String spName, String field, String value) {
		SharedPreferences sp = (SharedPreferences) CCApplication.getApplication().getSharedPreferences(spName, Context.MODE_PRIVATE);
		sp.edit().putString(field, value).commit();
	}
	
	public static void putSharePref(String spName, String field, boolean value) {
		SharedPreferences sp = (SharedPreferences) CCApplication.getApplication().getSharedPreferences(spName, Context.MODE_PRIVATE);
		sp.edit().putBoolean(field, value).commit();
	}

	public static void putSharePref(String spName, String field, int value) {
		SharedPreferences sp = (SharedPreferences) CCApplication.getApplication().getSharedPreferences(spName, Context.MODE_PRIVATE);
		sp.edit().putInt(field, value).commit();
	}

	public static void clearPreferences(String spName) {
		SharedPreferences sp = (SharedPreferences) CCApplication.getApplication().getSharedPreferences(spName, Context.MODE_PRIVATE);
		sp.edit().clear().commit();
	}

}
