package com.csd.android.utils;

import android.util.Log;

import com.csd.android.ReleaseConstants;

/**
 * 调试日志的统一输出
 * 
 * @author caizenghui
 *
 */
public class LogUtils {
	public static void i(String TAG, String msg) {
		if (!ReleaseConstants.IS_RELEASE_MODE) {
			Log.i(TAG, msg);
		}
	}

	public static void e(String TAG, String msg) {
		if (!ReleaseConstants.IS_RELEASE_MODE) {
			Log.e(TAG, msg);
		}
	}


	public static void d(String TAG, String msg) {
		if (!ReleaseConstants.IS_RELEASE_MODE) {
			Log.d(TAG, msg);
		}
	}

	public static void v(String TAG, String msg) {
		if (!ReleaseConstants.IS_RELEASE_MODE) {
			Log.v(TAG, msg);
		}
	}

	public static void w(String TAG, String msg) {
		if (!ReleaseConstants.IS_RELEASE_MODE) {
			Log.w(TAG, msg);
		}
	}

}
