package com.csd.android.viewloader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.csd.android.R;
import com.csd.android.activity.BoxCheckIntroductionActivity;
import com.csd.android.activity.TaskDetailActivity;
import com.csd.android.model.Box;
import com.csd.android.model.Box.BoxDetail;
import com.csd.android.model.BoxTestResult;
import com.csd.android.model.TaskDetail;
import com.csd.android.net.CCHttpEngine;
import com.csd.android.net.HttpCallBack;
import com.csd.android.net.NetConstants;
import com.csd.android.net.ResponseBean;
import com.csd.android.utils.ToastUtils;
import com.csd.android.utils.UIUtils;
import com.csd.android.widget.BoxBindItemView;
import com.csd.android.widget.CCWaitingDialog;
import com.csd.android.widget.MapWrapper;

public class BoxBindViewLoader {

	public TaskDetailActivity context;
	private View box_bind_view_loader;
	public MapWrapper map_wraper;
	private int current_select_item = -1;
	private LinearLayout vg_item_box_bind;
	private BoxDetail selected_boxDetail;
	public TextView tv_test_result;

	public BoxBindViewLoader(TaskDetailActivity context) {
		this.context = context;
	}

	public void load() {
		final TaskDetail entity = context.task_detail_entity;
		initView(entity);
		initUiData(entity);
	}

	private void initUiData(TaskDetail entity) {

	}

	private void initView(final TaskDetail entity) {
		if (box_bind_view_loader == null) {

			box_bind_view_loader = LayoutInflater.from(context).inflate(R.layout.layout_box_bind, null);
			if (context.task_detail_entity.getTaskType().equals("3")) {//检修；
				box_bind_view_loader.findViewById(R.id.btn_change).setEnabled(true);
			}
			else {
				box_bind_view_loader.findViewById(R.id.btn_change).setEnabled(false);
			}
			box_bind_view_loader.findViewById(R.id.btn_bind).setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					bindBox(0);
				}
			});
			box_bind_view_loader.findViewById(R.id.btn_change).setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					bindBox(1);
				}
			});
			box_bind_view_loader.findViewById(R.id.btn_test_box).setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (selected_boxDetail.getBox_type() == 1) {//cocar1;
						testBox();
					}
					else {
						ToastUtils.showToast("盒子测试成功");
						box_bind_view_loader.findViewById(R.id.btn_bind).setEnabled(true);
					}

				}
			});

			box_bind_view_loader.findViewById(R.id.iv_box_check_introduction).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, BoxCheckIntroductionActivity.class);
					context.startActivity(intent);

				}
			});

			tv_test_result = (TextView) box_bind_view_loader.findViewById(R.id.tv_test_result);
			map_wraper = (MapWrapper) box_bind_view_loader.findViewById(R.id.map_wrapper);
			map_wraper.disableCenterIcon();

			vg_item_box_bind = (LinearLayout) box_bind_view_loader.findViewById(R.id.vg_item_box_bind);
			ArrayList<Box> box = context.task_detail_entity.getBox();

			for (int i = 0; i < box.size(); i++) {
				if (box.get(i).getNumber() != 0) {
					for (int j = 0; j < box.get(i).getList().size(); j++) {
						BoxDetail boxDetail = box.get(i).getList().get(j);
						boxDetail.setName(box.get(i).getName());
						boxDetail.setBox_type(box.get(i).getBox_type());
						boxDetail.setPositions(box.get(i).getPosition());
						boxDetail.setOld_gps_id(boxDetail.getGps_id());
						boxDetail.setIndex(vg_item_box_bind.getChildCount());
						BoxBindItemView itemView = new BoxBindItemView(this, boxDetail);
						vg_item_box_bind.addView(itemView);
						if (vg_item_box_bind.getChildCount() == 1) {
							initBoxItem(boxDetail);
						}
						View view = new View(context);
						view.setBackgroundResource(R.color.cor_list_divider);
						LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, UIUtils.dp2px(1));
						vg_item_box_bind.addView(view, param);
					}
				}
				if (box.get(i).getNeed_install() != 0) {
					for (int j = 0; j < box.get(i).getNeed_install(); j++) {
						BoxDetail boxDetail = box.get(i).new BoxDetail();
						box.get(i).need_install_list.add(boxDetail);
						boxDetail.setName(box.get(i).getName());
						boxDetail.setBox_type(box.get(i).getBox_type());
						boxDetail.setPositions(box.get(i).getPosition());
						boxDetail.setOld_gps_id(boxDetail.getGps_id());
						boxDetail.setIndex(vg_item_box_bind.getChildCount());
						BoxBindItemView itemView = new BoxBindItemView(this, boxDetail);
						vg_item_box_bind.addView(itemView);
						if (vg_item_box_bind.getChildCount() == 1) {
							initBoxItem(boxDetail);
						}
						View view = new View(context);
						view.setBackgroundResource(R.color.cor_list_divider);
						LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, UIUtils.dp2px(1));
						vg_item_box_bind.addView(view, param);
					}

				}
			}
		}

		context.vg_root.addView(box_bind_view_loader, 1);
	}

	protected void bindBox(final int isChange) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		if (UIUtils.isEmpty(selected_boxDetail.getGps_id()) || UIUtils.isEmpty(selected_boxDetail.getPosition())) {
			ToastUtils.showToast("盒子信息不完整");
			return;
		}
		hashMap.put("car_id", context.task_detail_entity.getCarId());
		hashMap.put("gps_id", selected_boxDetail.getGps_id());
		hashMap.put("box_type", selected_boxDetail.getBox_type() + "");
		hashMap.put("position_type", selected_boxDetail.getPosition());
		if ("19".equals(selected_boxDetail.getOther_position()) || "29".equals(selected_boxDetail.getOther_position())
				|| "39".equals(selected_boxDetail.getOther_position())) {
			hashMap.put("position_desc", selected_boxDetail.getOther_position());
		}
		hashMap.put("change", isChange + "");
		if (!UIUtils.isEmpty(selected_boxDetail.getOld_gps_id())) {
			hashMap.put("old_gps_id", selected_boxDetail.getOld_gps_id());
		}
		context.TAG_NO_FIRST_REQUEST = "bind_change_box";
		CCWaitingDialog.show(context);
		new CCHttpEngine(context, NetConstants.NET_ID_BOX_BIND, hashMap, context.TAG_NO_FIRST_REQUEST, new HttpCallBack() {
			public void onSuccess(ResponseBean responseBean) {
				CCWaitingDialog.dismiss(context);
				if (responseBean.getCode() == 0) {
					ToastUtils.showToast("绑定成功");
					selected_boxDetail.setHasFinish(true);
					context.initRedCircle();
					updateBoxItem(isChange);
				}
				else {
					ToastUtils.showToast(responseBean.getMessage());
				}
			}

			@Override
			public void onNetUnavailable(String net_unAvailabel) {
				CCWaitingDialog.dismiss(context);
				ToastUtils.showToast(R.string.net_unavailable);
			}

			@Override
			public void onFailure(IOException e) {
				CCWaitingDialog.dismiss(context);
				ToastUtils.showToast(R.string.net_fail);
			}
		}).executeTask();
	}

	protected void testBox() {
		if (UIUtils.isEmpty(selected_boxDetail.getGps_id())) {
			ToastUtils.showToast("请优先完善盒子信息");
			return;
		}
		if (map_wraper.getMyLocation() == null || map_wraper.getMyLocation().latitude == 0 || map_wraper.getMyLocation().longitude == 0) {
			ToastUtils.showToast("未获取手机定位信息，请稍后再试");
			return;
		}
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("gps_id", selected_boxDetail.getGps_id());
		hashMap.put("lng", map_wraper.getMyLocation().longitude + "");
		hashMap.put("lat", map_wraper.getMyLocation().latitude + "");
		hashMap.put("box_type", selected_boxDetail.getBox_type() + "");
		CCWaitingDialog.show(context);
		context.TAG_NO_FIRST_REQUEST = "text_box";
		new CCHttpEngine(context, NetConstants.NET_ID_BOX_TEST, hashMap, context.TAG_NO_FIRST_REQUEST, new HttpCallBack() {
			public void onSuccess(ResponseBean responseBean) {
				CCWaitingDialog.dismiss(context);
				if (responseBean.getCode() == 0) {
					ToastUtils.showToast("盒子测试成功");
					BoxTestResult entity = (BoxTestResult) responseBean.getData();
					((TextView) box_bind_view_loader.findViewById(R.id.tv_test_result)).setText("电量:" + entity.getBattery() + "%定位时间:\n"
							+ entity.getSendtime());
					LatLng latLng = new LatLng(Double.parseDouble(entity.getLat()), Double.parseDouble(entity.getLng()));
					map_wraper.autoScale(latLng, map_wraper.getMyLocation());
					box_bind_view_loader.findViewById(R.id.btn_bind).setEnabled(true);
				}
				else {
					ToastUtils.showToast(responseBean.getMessage());
				}
			}

			@Override
			public void onNetUnavailable(String net_unAvailabel) {
				CCWaitingDialog.dismiss(context);
				ToastUtils.showToast(R.string.net_unavailable);
			}

			@Override
			public void onFailure(IOException e) {
				CCWaitingDialog.dismiss(context);
				ToastUtils.showToast(R.string.net_fail);
			}
		}).executeTask();

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == TaskDetailActivity.REQUEST_CODE_SCAN_BOX_ID && resultCode == Activity.RESULT_OK) {
			if (context.pop != null && context.pop.isShowing()) {
				context.pop.onActivityResult(data);
			}
		}

	}

	public void updateBoxItem(int isChange) {
		if (isChange == 0) {
			vg_item_box_bind.getChildAt(current_select_item).findViewById(R.id.iv_status).setVisibility(View.VISIBLE);
		}
		else if (isChange == 1) {
			((TextView) vg_item_box_bind.getChildAt(current_select_item).findViewById(R.id.tv_1)).setTextColor(0xff393939);
			((TextView) vg_item_box_bind.getChildAt(current_select_item).findViewById(R.id.tv_2)).setTextColor(0xff393939);
		}
	}

	public void initBoxItem(BoxDetail boxDetail) {
		if (current_select_item == boxDetail.getIndex()) {
			return;
		}
		if (current_select_item == -1) {
			current_select_item = 0;
		}
		vg_item_box_bind.getChildAt(current_select_item).findViewById(R.id.vg_tv).setBackgroundColor(0xffffffff);
		current_select_item = boxDetail.getIndex();
		vg_item_box_bind.getChildAt(current_select_item).findViewById(R.id.vg_tv).setBackgroundColor(0xffffa340);
		selected_boxDetail = boxDetail;
	}

}
