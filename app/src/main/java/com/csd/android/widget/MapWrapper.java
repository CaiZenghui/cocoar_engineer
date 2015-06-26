package com.csd.android.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapLoadedCallback;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.csd.android.CCApplication;
import com.csd.android.R;
import com.csd.android.constants.SPConstants;
import com.csd.android.utils.SPUtils;
import com.csd.android.utils.ToastUtils;
import com.csd.android.utils.UIUtils;

public class MapWrapper extends FrameLayout implements OnClickListener, BDLocationListener, OnMapLoadedCallback, OnMapStatusChangeListener {

	public MapView mapView;
	public LocationClient mLocClient;
	public LatLng mLocation;
	public BaiduMap mBaiduMap;
	public SDKReceiver mReceiver;
	private ZoomControlView view_controller;
	private Context context;

	public LatLng getMyLocation() {
		return mLocation;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				requestDisallowInterceptTouchEvent(true);
				break;
			case MotionEvent.ACTION_UP:
				requestDisallowInterceptTouchEvent(false);
				break;
		}
		return super.dispatchTouchEvent(ev);
	}

	public MapWrapper(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView(context);
	}

	private void initView(Context context) {
		// 注册 SDK 广播监听者
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
		mReceiver = new SDKReceiver();
		context.registerReceiver(mReceiver, iFilter);
		SDKInitializer.initialize(CCApplication.getApplication());
		LayoutInflater.from(context).inflate(R.layout.layout_map_view, this);
		initMap(context);
	}

	/**
	 * 构造广播监听类，监听 SDK key 验证以及网络异常广播
	 */
	public class SDKReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			String s = intent.getAction();
			if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
//				text.setText("key 验证出错! 请在 AndroidManifest.xml 文件中检查 key 设置");
			}
			else if (s.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
				ToastUtils.showToast("网络出错");
			}
		}
	}

	private void initMap(Context context) {
		mapView = (MapView) findViewById(R.id.map);
		mapView.showScaleControl(true);// 显示缩放比例；
		mapView.showZoomControls(false);// 显示缩放按钮；
		mBaiduMap = mapView.getMap();
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		mBaiduMap.setOnMapLoadedCallback(this);
		mBaiduMap.setOnMapStatusChangeListener(this);

		// 开启定位图层  
		mBaiduMap.setMyLocationEnabled(true);
		String str_lat = SPUtils.getSharePrefStr(SPConstants.SHARED_PREFS_FILE_NAME_LOCATION, SPConstants.SHARED_PREFS_KEY_LAT);
		String str_lng = SPUtils.getSharePrefStr(SPConstants.SHARED_PREFS_FILE_NAME_LOCATION, SPConstants.SHARED_PREFS_KEY_LNG);
		if (!UIUtils.isEmpty(str_lat) && !UIUtils.isEmpty(str_lng)) {
			mLocation = new LatLng(Double.parseDouble(str_lat), Double.parseDouble(str_lng));
			mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(mLocation, 17), 500);
		}
		mLocClient = CCApplication.getApplication().location_client;
		if (mLocClient.isStarted()) {
			CCApplication.getApplication().stopLocation();
		}
		mLocClient.registerLocationListener(this);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(3000);// 3秒定位一次
		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
		option.setIsNeedAddress(false);// 开启反地理编码（显示地址信息）
		mLocClient.setLocOption(option);
		mLocClient.start();

		findViewById(R.id.iv_reset_location).setOnClickListener(this);
	}

	private boolean isFirstLocation = true;
	private long lastLocationTime;

	@Override
	public void onReceiveLocation(BDLocation location) {
		// map view 销毁后不在处理新接收的位置
		if (location == null || mapView == null) {
			return;
		}
		mLocation = new LatLng(location.getLatitude(), location.getLongitude());
		if (isFirstLocation) {
			// 此处设置开发者获取到的方向信息，顺时针0-360
			MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius()).direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();

			mBaiduMap.setMyLocationData(locData);
			mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(mLocation, 17), 500);
//		BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.icon_search_normal);
			mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(LocationMode.FOLLOWING, true, null));
		}
		isFirstLocation = false;

		if (System.currentTimeMillis() - lastLocationTime > 1000 * 60) {
			lastLocationTime = System.currentTimeMillis();
			CCApplication.getApplication().postLocation(location);
		}
	}

	public void autoScale(LatLng l1, LatLng l2) {
		if (mBaiduMap == null || l1 == null || l2 == null) {
			return;
		}
		LatLngBounds bounds = new LatLngBounds.Builder().include(l1).include(l2).build();
//		LatLng northeast = new LatLng(l1.latitude > l2.latitude ? l1.latitude : l2.latitude, l1.longitude > l2.longitude ? l1.longitude
//				: l2.longitude);
//		LatLng southwest = new LatLng(l1.latitude < l2.latitude ? l1.latitude : l2.latitude, l1.longitude < l2.longitude ? l1.longitude
//				: l2.longitude);
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLngBounds(bounds);
		mBaiduMap.animateMapStatus(u);
		BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.icon_car);
		OverlayOptions option = new MarkerOptions().position(l1).icon(bitmap);
		mBaiduMap.addOverlay(option);
		bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.icon_people);
		option = new MarkerOptions().position(l2).icon(bitmap);
		mBaiduMap.addOverlay(option);
	}

	public void clear() {
		if (mBaiduMap != null) {
			mBaiduMap.clear();
		}
	}

	public void disableCenterIcon() {
		findViewById(R.id.iv_map_center).setVisibility(View.GONE);
	}

	@Override
	public void onMapLoaded() {
		view_controller = (ZoomControlView) findViewById(R.id.view_controller);
		view_controller.setVisibility(View.VISIBLE);
		view_controller.setMap(mBaiduMap);
	}

	@Override
	public void onMapStatusChange(MapStatus arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMapStatusChangeFinish(MapStatus arg0) {
		if (view_controller != null) {
			view_controller.refreshZoomButtonStatus();
		}
	}

	@Override
	public void onMapStatusChangeStart(MapStatus arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.iv_reset_location:
				if (mLocation != null) {
					mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(mLocation, 17), 500);
				}
				break;
		}
	}

	public void onPause() {
		mLocClient.stop();
		mapView.onPause();
	}

	public void onResume() {
		mLocClient.start();
		mapView.onResume();
	}

	public void onDestroy() {
		mLocClient.unRegisterLocationListener(this);
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mapView.onDestroy();
		mapView = null;
		context.unregisterReceiver(mReceiver);

		CCApplication.getApplication().initLocationClient();
	}

}
