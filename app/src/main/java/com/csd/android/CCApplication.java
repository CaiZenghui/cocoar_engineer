package com.csd.android;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.csd.android.constants.SPConstants;
import com.csd.android.net.CCHttpEngine;
import com.csd.android.net.HttpCallBack;
import com.csd.android.net.NetConstants;
import com.csd.android.net.ResponseBean;
import com.csd.android.utils.LogUtils;
import com.csd.android.utils.SPUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.leakcanary.LeakCanary;

import java.io.IOException;
import java.util.HashMap;

public class CCApplication extends Application {

	public static Context sContext;

	private static CCApplication application;

	public LocationClient location_client;

	public static final CCApplication getApplication() {
		return application;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		LeakCanary.install(this);
		Fresco.initialize(this);
		application = this;
		
		LocationBroadcastReceiver locationBroadcastReceiver = new LocationBroadcastReceiver();
		IntentFilter filter=new IntentFilter("location");
		registerReceiver(locationBroadcastReceiver, filter);
		
		pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
		
		initLocationClient();
		
		
	}

	private LocationListener locationListener = new LocationListener();
	private Intent intent = new Intent("location");
	private PendingIntent pendingIntent;

	private AlarmManager alarm;

	private class LocationBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (location_client==null){
				location_client = new LocationClient(CCApplication.getApplication());
			}
			location_client.start();
		}

	}


	public void initLocationClient() {
		if (location_client==null){
			location_client = new LocationClient(this);
		}
		location_client.registerLocationListener(locationListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000 * 60);
		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
		option.setIsNeedAddress(false);// 开启反地理编码（显示地址信息）
		location_client.setLocOption(option);
		location_client.start();
		if (alarm==null){
			alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
			alarm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, 1000 * 60, pendingIntent);
		}
	}

	public void stopLocation() {
		location_client.unRegisterLocationListener(locationListener);
		location_client.stop();
	}

	class LocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			LogUtils.d("location_baidu", "app LocationListener");
			if (location == null) {
				return;
			}
			SPUtils.putSharePref(SPConstants.SHARED_PREFS_FILE_NAME_LOCATION, SPConstants.SHARED_PREFS_KEY_LAT, location.getLatitude() + "");
			SPUtils.putSharePref(SPConstants.SHARED_PREFS_FILE_NAME_LOCATION, SPConstants.SHARED_PREFS_KEY_LNG, location.getLongitude() + "");
			postLocation(location);
		}

	}

	public void postLocation(BDLocation location) {
		LogUtils.d("location_baidu", "app LocationListener postLocation");
		if (UserManager.getUser() == null) {
			return;
		}
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("lng", location.getLongitude() + "");
		hashMap.put("lat", location.getLatitude() + "");
		new CCHttpEngine(NetConstants.NET_ID_POST_LOCATION, hashMap, null, new HttpCallBack() {

			@Override
			public void onSuccess(ResponseBean responseBean) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onNetUnavailable(String net_unAvailabel) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFailure(IOException e) {
				// TODO Auto-generated method stub

			}
		}).executeTask();
		LogUtils.d("location_baidu", "app LocationListener postLocation end");
	}
}
