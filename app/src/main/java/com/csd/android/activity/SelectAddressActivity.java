package com.csd.android.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapLoadedCallback;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.csd.android.R;
import com.csd.android.adapter.SelectAddressAdapter;
import com.csd.android.model.Address;
import com.csd.android.utils.LogUtils;
import com.csd.android.utils.ToastUtils;
import com.csd.android.utils.UIUtils;
import com.csd.android.widget.ZoomControlView;

public class SelectAddressActivity extends Activity implements BDLocationListener, OnMapLoadedCallback, OnGetGeoCoderResultListener,
		OnMapStatusChangeListener, OnClickListener, OnItemClickListener, OnGetPoiSearchResultListener, TextWatcher, OnTouchListener {

	public static final String CITY_NAME = "city_name";
	private MapView mapView;
	private BaiduMap mBaiduMap;
	private LocationClient mLocClient;
	private int POP_ID = 1;
	private ArrayList<PoiInfo> list = new ArrayList<PoiInfo>();

	private View pop;
	private ListView lv;
	private SelectAddressAdapter adapter;

	private SDKReceiver mReceiver;
	private EditText et_search;
	private PoiSearch mPoiSearch;
	private GeoCoder geoCoder;
	private Address address;
	private EditText et_keyword;
	private boolean needReverse = true;
	private ZoomControlView view_controller;
	private LatLng mLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		registReceiver();

		//在使用SDK各组件之前初始化context信息，传入ApplicationContext  
		//注意该方法要再setContentView方法之前实现  
		SDKInitializer.initialize(getApplicationContext());
		initSearchEngine();

		setContentView(R.layout.activity_select_address);

		initView();

		address = new Address();

	}

	private void initSearchEngine() {
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(this);
	}

	private void initView() {
		initMapView();

		findViewById(R.id.iv_delete).setOnClickListener(this);
		findViewById(R.id.tv_confirm).setOnClickListener(this);
		findViewById(R.id.iv_reset_location).setOnClickListener(this);

		et_search = (EditText) findViewById(R.id.et_search);
		et_search.setOnTouchListener(this);

	}

	private void registReceiver() {
		// 注册 SDK 广播监听者
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
		mReceiver = new SDKReceiver();
		registerReceiver(mReceiver, iFilter);
	}

	private void initMapView() {
		mapView = (MapView) findViewById(R.id.map);
		mapView.showScaleControl(true);// 显示缩放比例；
		mapView.showZoomControls(false);// 显示缩放按钮；
		mBaiduMap = mapView.getMap();
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

		// 开启定位图层  
		mBaiduMap.setMyLocationEnabled(true);
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(this);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(5000);// 1秒定位一次
		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
		option.setIsNeedAddress(true);// 开启反地理编码（显示地址信息）
		mLocClient.setLocOption(option);
		mLocClient.start();

		mBaiduMap.setOnMapStatusChangeListener(this);
		mBaiduMap.setOnMapLoadedCallback(this);

		geoCoder = GeoCoder.newInstance();
		geoCoder.setOnGetGeoCodeResultListener(this);
	}

	@Override
	public void onReceiveLocation(BDLocation location) {
		// map view 销毁后不在处理新接收的位置
		if (location == null || mapView == null) {
			return;
		}
		mLocClient.stop();
		mLocation = new LatLng(location.getLatitude(), location.getLongitude());
		if (findViewById(POP_ID) != null) {
			return;
		}
		// 此处设置开发者获取到的方向信息，顺时针0-360
		MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius()).direction(100).latitude(location.getLatitude())
				.longitude(location.getLongitude()).build();

		mBaiduMap.setMyLocationData(locData);
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(mLocation);
		mBaiduMap.animateMapStatus(u);
//		BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.icon_search_normal);
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(LocationMode.FOLLOWING, true, null));
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

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mapView.onDestroy();
		mapView = null;
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.iv_delete:
				et_search.setText(null);
				address.address = null;
				address.name = null;
				break;

			case R.id.tv_confirm:
				if (UIUtils.isEmpty(address.address)) {
					ToastUtils.showToast("请选择地址");
					return;
				}
				Intent intent = new Intent();
				intent.putExtra(TaskDetailActivity.INTENT_EXTRA_NAME_SELECT_ADDRESS, address);
				setResult(Activity.RESULT_OK, intent);
				finish();
				break;
			case R.id.iv_reset_location:
				if (mLocation != null) {
					mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(mLocation, 17), 500);
				}

				break;
		}
	}

	@Override
	public void onGetPoiDetailResult(PoiDetailResult result) {
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			address.address = result.getAddress();
			address.name = result.getName();
			address.location[0] = result.getLocation().longitude;
			address.location[1] = result.getLocation().latitude;
			et_search.setText(UIUtils.isEmpty(address.name) ? address.address : address.name);
		}

	}

	@Override
	public void onGetPoiResult(PoiResult result) {
		LogUtils.d("Map", "onGetPoiResult()");
		if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
			mBaiduMap.clear();
			return;
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
			mBaiduMap.clear();
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			mBaiduMap.clear();
			PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
			mBaiduMap.setOnMarkerClickListener(overlay);
			overlay.setData(result);
			overlay.addToMap();
			overlay.zoomToSpan();

			List<PoiInfo> allPoi = result.getAllPoi();
			list.clear();
			list.addAll(allPoi);
			adapter.notifyDataSetChanged();
			return;
		}
	}

	@Override
	public void onBackPressed() {
		if (findViewById(POP_ID) != null) {
			((ViewGroup) findViewById(R.id.vg_root)).removeView(pop);
			return;
		}
		super.onBackPressed();
	}

	private class MyPoiOverlay extends PoiOverlay {

		public MyPoiOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public boolean onPoiClick(int index) {
			super.onPoiClick(index);
			PoiInfo poi = getPoiResult().getAllPoi().get(index);
			address.address = poi.address;
			address.name = poi.name;
			address.location[0] = poi.location.longitude;
			address.location[1] = poi.location.latitude;
			et_search.setText(UIUtils.isEmpty(address.name) ? address.address : address.name);
//			mPoiSearch.searchPoiDetail((new PoiDetailSearchOption()).poiUid(poi.uid));
			return true;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		UIUtils.hideSoftInput(et_search, this);
		PoiInfo poiInfo = list.get(position);
		address.name = poiInfo.name;
		address.address = poiInfo.address;
		address.location[0] = poiInfo.location.longitude;
		address.location[1] = poiInfo.location.latitude;
		et_search.setText(UIUtils.isEmpty(address.name) ? address.address : address.name);

		((ViewGroup) findViewById(R.id.vg_root)).removeView(pop);
		mBaiduMap.clear();
		needReverse = false;

		LatLng ll = new LatLng(address.location[1], address.location[0]);
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 17);
		mBaiduMap.animateMapStatus(u, 1000);
	}

	@Override
	public void onMapStatusChange(MapStatus status) {

	}

	@Override
	public void onMapStatusChangeFinish(MapStatus status) {
		if (needReverse) {
			et_search.setText(null);
			LatLng target = status.target;
			geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(target));
		}
		needReverse = true;
		if (view_controller != null) {
			view_controller.refreshZoomButtonStatus();
		}
	}

	@Override
	public void onMapStatusChangeStart(MapStatus status) {

	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult arg0) {

	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(this, "抱歉，未能找到结果", Toast.LENGTH_LONG).show();
			return;
		}
		address.address = result.getAddress();
		address.name = null;
		address.location[0] = result.getLocation().longitude;
		address.location[1] = result.getLocation().latitude;
		et_search.setText(UIUtils.isEmpty(address.name) ? address.address : address.name);
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (s.length() < 2) {
			return;
		}
		mPoiSearch.searchInCity(new PoiCitySearchOption().city(getIntent().getStringExtra(CITY_NAME)).keyword(s.toString().trim()).pageNum(0)
				.pageCapacity(10));
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void afterTextChanged(Editable s) {
	}

	public boolean onTouch(View v, MotionEvent event) {
		if (findViewById(POP_ID) == null) {
			if (pop == null) {
				pop = LayoutInflater.from(SelectAddressActivity.this).inflate(R.layout.pop_select_address, null);
				pop.setId(POP_ID);
				pop.setAnimation(AnimationUtils.loadAnimation(SelectAddressActivity.this, R.anim.pop_fade_in));
				lv = (ListView) pop.findViewById(R.id.lv);
				et_keyword = (EditText) pop.findViewById(R.id.et_keyword_search);
				et_keyword.addTextChangedListener(SelectAddressActivity.this);
				pop.findViewById(R.id.iv_delete).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (!UIUtils.isEmpty(et_keyword.getText().toString())) {
							et_keyword.setText("");
							list.clear();
							adapter.notifyDataSetChanged();
						}
					}
				});
				adapter = new SelectAddressAdapter(SelectAddressActivity.this, list);
				lv.setAdapter(adapter);
				lv.setOnItemClickListener(SelectAddressActivity.this);
			}
			((ViewGroup) findViewById(R.id.vg_root)).addView(pop);
			et_keyword.requestFocus();
			UIUtils.showSoftInput(SelectAddressActivity.this);
		}
		return true;
	}

	@Override
	public void onMapLoaded() {
		view_controller = (ZoomControlView) findViewById(R.id.view_controller);
		view_controller.setVisibility(View.VISIBLE);
		view_controller.setMap(mBaiduMap);
	}

}