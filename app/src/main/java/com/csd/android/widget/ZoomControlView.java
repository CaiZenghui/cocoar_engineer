package com.csd.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.csd.android.R;
import com.csd.android.utils.LogUtils;

public class ZoomControlView extends RelativeLayout implements OnClickListener {
	private Button mButtonZoomin;
	private Button mButtonZoomout;
	private BaiduMap mMap;

	public ZoomControlView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ZoomControlView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.zoom_controls_layout, null);
		mButtonZoomin = (Button) view.findViewById(R.id.zoomin);
		mButtonZoomout = (Button) view.findViewById(R.id.zoomout);
		mButtonZoomin.setOnClickListener(this);
		mButtonZoomout.setOnClickListener(this);
		addView(view);
	}

	@Override
	public void onClick(View v) {
		if (mMap == null) {
			throw new NullPointerException("you can call setMapView(MapView mapView) at first");
		}
		int level = (int) (mMap.getMapStatus().zoom + 0.5);
		switch (v.getId()) {
			case R.id.zoomin: {
				level++;
				mMap.animateMapStatus(MapStatusUpdateFactory.zoomTo(level));
				break;
			}
			case R.id.zoomout: {
				level--;
				mMap.animateMapStatus(MapStatusUpdateFactory.zoomTo(level));
				break;
			}
		}
		postDelayed(new Runnable() {
			@Override
			public void run() {
				refreshZoomButtonStatus();
			}
		}, 500);
	}

	public void setMap(BaiduMap map) {
		this.mMap = map;
	}

	public void refreshZoomButtonStatus() {
		if (mMap == null) {
			throw new NullPointerException("you can call setMapView(MapView mapView) at first");
		}
		float maxZoomLevel = mMap.getMaxZoomLevel();
		float minZoomLevel = mMap.getMinZoomLevel();
		float level = mMap.getMapStatus().zoom;
		if (level > minZoomLevel && level < maxZoomLevel) {
			if (!mButtonZoomout.isEnabled()) {
				mButtonZoomout.setEnabled(true);
			}
			if (!mButtonZoomin.isEnabled()) {
				mButtonZoomin.setEnabled(true);
			}
		}
		else if (level == minZoomLevel) {
			mButtonZoomout.setEnabled(false);
		}
		else if (level == maxZoomLevel) {
			mButtonZoomin.setEnabled(false);
		}
	}

}
