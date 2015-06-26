package com.csd.android.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.csd.android.CCApplication;

public class NetUtil {

	public static NetworkInfo getActiveNetwork(Context context) {
		if (context == null)
			return null;
		ConnectivityManager mConnMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (mConnMgr == null)
			return null;
		NetworkInfo aActiveInfo = mConnMgr.getActiveNetworkInfo(); // 获取活动网络连接信息
		return aActiveInfo;
	}

	public static boolean isNetAvaliable() {// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		try {
			ConnectivityManager connectivity = (ConnectivityManager) CCApplication.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// 获取网络连接管理的对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					// 判断当前网络是否已经连接
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean isWifiActive(Context context) {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI) {
				return true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean isWapMode(Context context) {
		boolean result = false;
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = connectivity.getActiveNetworkInfo();

			if (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE) {
				ContentResolver resolver = context.getContentResolver();
				final Cursor cursor = resolver.query(Uri.parse("content://telephony/carriers/preferapn"), null, null, null, null);
				if (null != cursor && cursor.moveToFirst()) {
					final String user = cursor.getString(cursor.getColumnIndex("user"));
					if (!TextUtils.isEmpty(user)) {
						if (user.toLowerCase().startsWith("ctwap")) {
							result = true;
						}
					}
				}
				cursor.close();

				String netMode = info.getExtraInfo();
				if (null != netMode
						&& (netMode.toLowerCase().equals("cmwap") || netMode.toLowerCase().equals("3gwap") || netMode.toLowerCase().equals("uniwap"))) {
					result = true;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 参考网站：http://www.binkery.com/post/368.html
	 * 
	 * @param context
	 * @return true 为 2g
	 */
	public static boolean is2GorNot(Context context) {
		TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		int networkType = mTelephonyManager.getNetworkType();
		switch (networkType) {
			case TelephonyManager.NETWORK_TYPE_GPRS:
			case TelephonyManager.NETWORK_TYPE_EDGE:
			case TelephonyManager.NETWORK_TYPE_CDMA:
			case TelephonyManager.NETWORK_TYPE_1xRTT:
			case TelephonyManager.NETWORK_TYPE_IDEN:
				return true;
		}
		return false;
	}
}
