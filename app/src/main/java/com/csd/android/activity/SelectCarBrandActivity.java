package com.csd.android.activity;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.csd.android.R;
import com.csd.android.adapter.CarBrandAdapter;
import com.csd.android.model.CarBrand;
import com.csd.android.net.CCHttpEngine;
import com.csd.android.net.HttpCallBack;
import com.csd.android.net.NetConstants;
import com.csd.android.net.ResponseBean;
import com.csd.android.utils.ToastUtils;
import com.csd.android.widget.CCWaitingDialog;
import com.csd.android.widget.SideBar;
import com.csd.android.widget.SideBar.Mode;

public class SelectCarBrandActivity extends Activity implements OnClickListener {

	private EditText et_search_box;
	private ListView lv;
	private SideBar sideBar;
	private TextView textView;
	private CarBrandAdapter adapter;
	public ArrayList<CarBrand> list = new ArrayList<CarBrand>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_car_brand);

		initView();

		getCarBrandList();

	}

	private void initView() {
		et_search_box = (EditText) findViewById(R.id.et_search_box);

		lv = (ListView) findViewById(R.id.lv);
		sideBar = (SideBar) findViewById(R.id.sidebar);
		textView = (TextView) findViewById(R.id.tv_slidebar_tip);
		sideBar.setTextView(textView);

		adapter = new CarBrandAdapter(this, list);

		lv.setAdapter(adapter);

		sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
			public void onTouchingLetterChanged(String s) {
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position < 0) {
					return;
				}
				lv.setSelection(position + lv.getHeaderViewsCount());
			}
		});

		et_search_box.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				adapter.getFilter().filter(s);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

	}

	private void getCarBrandList() {
		CCWaitingDialog.show(this);
		new CCHttpEngine(this, NetConstants.NET_ID_GET_CAR_BRAND_LIST, null, null, new HttpCallBack() {

			@Override
			public void onSuccess(ResponseBean responseBean) {
				CCWaitingDialog.dismiss(SelectCarBrandActivity.this);
				if (responseBean.getCode() == 0) {
					ArrayList<CarBrand> list_car_brand = (ArrayList<CarBrand>) responseBean.getData();
					list.clear();
					list.addAll(list_car_brand);
					adapter.notifyDataSetChanged();

				}
				else {
					ToastUtils.showToast(responseBean.getMessage());
				}
			}

			@Override
			public void onNetUnavailable(String net_unAvailabel) {
				CCWaitingDialog.dismiss(SelectCarBrandActivity.this);
				ToastUtils.showToast(net_unAvailabel);
			}

			@Override
			public void onFailure(IOException e) {
				CCWaitingDialog.dismiss(SelectCarBrandActivity.this);
				ToastUtils.showToast(e.getMessage());
			}
		}).executeTask();

	}

	@Override
	public void onClick(View v) {

	}
}
