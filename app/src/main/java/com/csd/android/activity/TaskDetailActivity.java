package com.csd.android.activity;

import java.io.IOException;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.csd.android.R;
import com.csd.android.model.TaskDetail;
import com.csd.android.net.CCHttpClient;
import com.csd.android.net.CCHttpEngine;
import com.csd.android.net.HttpCallBack;
import com.csd.android.net.NetConstants;
import com.csd.android.net.ResponseBean;
import com.csd.android.utils.ToastUtils;
import com.csd.android.viewloader.ViewLoaderForTaskDetail;
import com.csd.android.widget.BoxBindPop;
import com.csd.android.widget.CCWaitingDialog;
import com.csd.android.widget.TabsHorizontalScrollView;

public class TaskDetailActivity extends Activity implements OnClickListener {
	private static final String TAG = "TaskDetailActivity";

	public static final String INTENT_EXTRA_NAME_CAR_ID = "VehicleId";
	public static final String INTENT_EXTRA_NAME_TASK_TYPE = "task_type";
	public static final String INTENT_EXTRA_NAME_LIST_ENTITY = "ListEntity";
	public static final String INTENT_EXTRA_NAME_CAR_TYPE_ID = "car_type_id";
	public static final String INTENT_EXTRA_NAME_CAR_BRAND_NAME = "car_brand_name";
	public static final String INTENT_EXTRA_NAME_CAR_TYPE_NAME = "car_type_name";
	public static final String INTENT_EXTRA_NAME_SELECT_ADDRESS = "select_address";
	public static final String INTENT_EXTRA_NAME_ASSIGN_ID = "assign_id";

	public static final int REQUEST_CODE_TAKE_PHOTO = 1;
	public static final int REQUEST_CODE_PICK_PHOTO = 2;
	public static final int REQUEST_CODE_SELECT_CAR_BRAND = 3;
	public static final int REQUEST_CODE_SELECT_ADDRESS = 4;
	public static final int REQUEST_CODE_SCAN_BOX_ID = 5;

	private View indicator1;
	private View indicator2;
	private View indicator3;
	private int current_view = 0;
	private ViewLoaderForTaskDetail view_loader = new ViewLoaderForTaskDetail(this);
	private String car_id;
	public LinearLayout vg_root;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_task_detail);

		car_id = getIntent().getStringExtra(INTENT_EXTRA_NAME_CAR_ID);
		task_type = getIntent().getStringExtra(INTENT_EXTRA_NAME_TASK_TYPE);
		assign_id = getIntent().getStringExtra(INTENT_EXTRA_NAME_ASSIGN_ID);
		initView();

		getCheLiangShangJiaInfo();
	}

	private void initView() {
		vg_root = (LinearLayout) findViewById(R.id.vg_root);
		indicator1 = findViewById(R.id.indicator1);
		indicator2 = findViewById(R.id.indicator2);
		indicator3 = findViewById(R.id.indicator3);

		findViewById(R.id.vg_title1).setOnClickListener(this);
		findViewById(R.id.vg_title2).setOnClickListener(this);
		findViewById(R.id.vg_title3).setOnClickListener(this);
	}

	private void showView(int i) {
		if (i == current_view) {
			return;
		}
		initIndicator(i);
		if (vg_root.getChildCount() > 1) {
			vg_root.removeViewAt(1);
		}
		if (i == 1) {
			view_loader.loadCardsCheckView();
			initCardsCheckStatus();
			if (current_view == 3) {
				mapOnPause();
			}
		}
		else if (i == 2) {
			view_loader.loadCheLiangShangJiaView();
			if (current_view == 3) {
				mapOnPause();
			}
		}
		else {
			view_loader.loadBoxBindView();
			mapOnResume();
		}

		current_view = i;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.vg_title1:
				showView(1);
				break;
			case R.id.vg_title2:
				showView(2);
				break;
			case R.id.vg_title3:
				showView(3);
				break;
		}

	}

	private void initIndicator(int i) {
		if (i == 1) {
			indicator1.setVisibility(View.VISIBLE);
			indicator2.setVisibility(View.INVISIBLE);
			indicator3.setVisibility(View.INVISIBLE);
		}
		else if (i == 2) {
			indicator2.setVisibility(View.VISIBLE);
			indicator3.setVisibility(View.INVISIBLE);
			indicator1.setVisibility(View.INVISIBLE);
		}
		else {
			indicator3.setVisibility(View.VISIBLE);
			indicator2.setVisibility(View.INVISIBLE);
			indicator1.setVisibility(View.INVISIBLE);
		}
	}

	public TaskDetail task_detail_entity;

	private void getCheLiangShangJiaInfo() {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("car_id", car_id);
		hashMap.put("task_type", task_type);
		hashMap.put("assignId", assign_id);
		hashMap.put("ACCESS_TOKEN", com.csd.android.UserManager.getUser().getToken());
		CCWaitingDialog.show(this);
		new CCHttpEngine(this, NetConstants.NET_ID_GET_CHE_LIANG_SHANGJIA_XINXI, hashMap, TAG, new HttpCallBack() {
			@Override
			public void onSuccess(ResponseBean responseBean) {
				CCWaitingDialog.dismiss(TaskDetailActivity.this);
				if (responseBean.getCode() == 0) {
					task_detail_entity = (TaskDetail) responseBean.getData();
					task_detail_entity.setTaskType(task_type);
					initRedCircle();
					showView(1);
				}
				else {
					ToastUtils.showToast(responseBean.getMessage());
					finish();
				}
			}

			@Override
			public void onNetUnavailable(String net_unAvailabel) {
				CCWaitingDialog.dismiss(TaskDetailActivity.this);
				ToastUtils.showToast(net_unAvailabel);
				finish();

			}

			@Override
			public void onFailure(IOException e) {
				CCWaitingDialog.dismiss(TaskDetailActivity.this);
				ToastUtils.showToast("error:" + e.getMessage());
				finish();

			}
		}).executeTask();

	}

	public void initCardsCheckStatus() {
		TabsHorizontalScrollView tabs = (TabsHorizontalScrollView) findViewById(R.id.tabs);
		String str = null;
		for (int i = 1; i < 6; i++) {
			if (i == 1) {
				str = task_detail_entity.getLicense().getIdentifyLicense().getStatus();
			}
			else if (i == 2) {
				str = task_detail_entity.getLicense().getDriverLicense().getStatus();
			}
			else if (i == 3) {
				if ("1".equals(task_detail_entity.getLicense().getIdentifyLicense().getStatus())
						|| "2".equals(task_detail_entity.getLicense().getDriverLicense().getStatus())) {
					str = "2";
				}
				else {
					str = task_detail_entity.getLicense().getOtherLicense().getStatus();
				}
			}
			else if (i == 4) {
				str = task_detail_entity.getLicense().getPolicyInfo().getStatus();
			}
			else if (i == 5) {
				str = task_detail_entity.getLicense().getCarImage().getStatus();
			}
			initTabsStatus(tabs, str, i);
		}

	}

	private void initTabsStatus(TabsHorizontalScrollView tabs, String str, int i) {
		if (i == 1) {
			if ("2".equals(str)) {
				tabs.findViewById(i).setBackgroundColor(0xff35ce7f);
				((TextView) tabs.findViewById(i).findViewById(R.id.tv_status)).setText("已通过");
			}
			else if ("-1".equals(str)) {
				tabs.findViewById(i).setBackgroundColor(0xffdedede);
				((TextView) tabs.findViewById(i).findViewById(R.id.tv_status)).setText("未上传");
			}
			else if ("0".equals(str)) {
				tabs.findViewById(i).setBackgroundColor(0xffffa340);
				((TextView) tabs.findViewById(i).findViewById(R.id.tv_status)).setText("待审核");
			}
			else if ("1".equals(str)) {
				tabs.findViewById(i).setBackgroundColor(0xfff7774a);
				((TextView) tabs.findViewById(i).findViewById(R.id.tv_status)).setText("不通过");
			}
		}
		else {
			if ("3".equals(str)) {
				tabs.findViewById(i).setBackgroundColor(0xff35ce7f);
				((TextView) tabs.findViewById(i).findViewById(R.id.tv_status)).setText("已通过");
			}
			else if ("-1".equals(str)) {
				tabs.findViewById(i).setBackgroundColor(0xffdedede);
				((TextView) tabs.findViewById(i).findViewById(R.id.tv_status)).setText("未上传");
			}
			else if ("0".equals(str) || "1".equals(str)) {
				tabs.findViewById(i).setBackgroundColor(0xffffa340);
				((TextView) tabs.findViewById(i).findViewById(R.id.tv_status)).setText("待审核");
			}
			else if ("2".equals(str)) {
				tabs.findViewById(i).setBackgroundColor(0xfff7774a);
				((TextView) tabs.findViewById(i).findViewById(R.id.tv_status)).setText("不通过");
			}
		}

	}

	public void initRedCircle() {
		if (task_detail_entity.getCheLiang_XinXi_Info_Status() == 0) {
			findViewById(R.id.view_red_circle2).setVisibility(View.VISIBLE);
		}
		else {
			findViewById(R.id.view_red_circle2).setVisibility(View.GONE);
		}

		if (task_detail_entity.getCards_Check_Status() == 0) {
			findViewById(R.id.view_red_circle1).setVisibility(View.VISIBLE);
		}
		else {
			findViewById(R.id.view_red_circle1).setVisibility(View.GONE);
		}

		if (task_detail_entity.getBind_Box_Status() == 0) {
			findViewById(R.id.view_red_circle3).setVisibility(View.VISIBLE);
		}
		else {
			findViewById(R.id.view_red_circle3).setVisibility(View.GONE);
		}
	}

	@Override
	public void onBackPressed() {
		if (CCWaitingDialog.isShowing(this) && null != TAG_NO_FIRST_REQUEST) {
			CCWaitingDialog.dismiss(this);
			CCHttpClient.cancel(TAG_NO_FIRST_REQUEST);
			return;
		}
		if (CCWaitingDialog.isShowing(this)) {
//			CCWaitingDialog.dismiss(this);
			CCHttpClient.cancel(TAG);
		}
		super.onBackPressed();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		view_loader.onActivityResult(current_view, requestCode, resultCode, data);

	}

	public String TAG_NO_FIRST_REQUEST;

	public BoxBindPop pop;

	private String task_type;

	private String assign_id;

	@Override
	protected void onPause() {
		mapOnPause();
		super.onPause();
	}

	private void mapOnPause() {
		if (view_loader != null && view_loader.boxBindViewLoader != null) {
			view_loader.boxBindViewLoader.map_wraper.onPause();
		}
	}

	@Override
	protected void onResume() {
		mapOnResume();
		super.onResume();
	}

	private void mapOnResume() {
		if (view_loader != null && view_loader.boxBindViewLoader != null) {
			view_loader.boxBindViewLoader.map_wraper.onResume();
		}
	}

	@Override
	protected void onDestroy() {
		mapOnDestroy();
		super.onDestroy();
	}

	private void mapOnDestroy() {
		if (view_loader != null && view_loader.boxBindViewLoader != null) {
			view_loader.boxBindViewLoader.map_wraper.onDestroy();
		}
	}

}
