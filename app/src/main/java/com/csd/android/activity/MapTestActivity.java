package com.csd.android.activity;

import android.app.Activity;
import android.os.Bundle;

import com.csd.android.R;
import com.csd.android.widget.MapWrapper;

public class MapTestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_test);
		map_wraper = (MapWrapper) findViewById(R.id.map_wrapper);
	}

	private MapWrapper map_wraper;

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		map_wraper.mLocClient.stop();
		// 关闭定位图层
		map_wraper.mBaiduMap.setMyLocationEnabled(false);
		map_wraper.mapView.onDestroy();
		map_wraper.mapView = null;
		unregisterReceiver(map_wraper.mReceiver);
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		map_wraper.mapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		map_wraper.mapView.onPause();
	}
}