package com.csd.android.widget;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.csd.android.R;
import com.csd.android.activity.TaskDetailActivity;
import com.csd.android.model.Box.BoxDetail;
import com.csd.android.net.CCHttpEngine;
import com.csd.android.net.HttpCallBack;
import com.csd.android.net.NetConstants;
import com.csd.android.net.ResponseBean;
import com.csd.android.utils.ToastUtils;
import com.csd.android.utils.UIUtils;
import com.csd.android.widget.NumberPicker.OnValueChangeListener;
import com.zbar.lib.CaptureActivity;

public class BoxBindPop extends PopupWindow implements OnClickListener {

	private TaskDetailActivity context;

	private View view_bg;
	private View contentView;
	private String[] positions_int;
	private String[] positions_str;
	private List<String> positions_all_int;
	private List<String> positions_all_str;
	private BoxDetail boxDetail;
	private BoxBindItemView boxBindItemView;

	public BoxBindPop(TaskDetailActivity context, BoxBindItemView boxBindItemView, BoxDetail boxDetail) {
		this.boxBindItemView = boxBindItemView;
		this.context = context;
		this.boxDetail = boxDetail;
		positions_int = new String[boxDetail.getPositions().size()];
		positions_str = new String[boxDetail.getPositions().size()];
		boxDetail.getPositions().toArray(positions_int);

		positions_all_str = Arrays.asList(context.getResources().getStringArray(R.array.box_position_str));
		positions_all_int = Arrays.asList(context.getResources().getStringArray(R.array.box_position_int));
		for (int i = 0; i < positions_int.length; i++) {
			int index = positions_all_int.indexOf(positions_int[i]);
			positions_str[i] = positions_all_str.get(index);
		}

		contentView = LayoutInflater.from(context).inflate(R.layout.pop_box_bind, null);
		setContentView(contentView);
		setOutsideTouchable(true);
		setFocusable(true);
		setBackgroundDrawable(new BitmapDrawable());
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);
		setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		initView();
	}

	private void initView() {
		contentView.findViewById(R.id.btn_scan).setOnClickListener(this);
		contentView.findViewById(R.id.btn_check).setOnClickListener(this);
		contentView.findViewById(R.id.btn_save).setOnClickListener(this);
		contentView.findViewById(R.id.btn_cancel).setOnClickListener(this);
		contentView.findViewById(R.id.btn_save).setEnabled(false);
		et_box_id = (EditText) contentView.findViewById(R.id.et_box_id);

		et_box_id.setText(boxDetail.getGps_id());
		((TextView) contentView.findViewById(R.id.tv_box_problem)).setText(boxDetail.getNote());

		vg_picker_content = (LinearLayout) contentView.findViewById(R.id.vg_picker_content);

		picker = (NumberPicker) LayoutInflater.from(context).inflate(R.layout.layout_item_pop_picker, vg_picker_content, false);
		picker.setId(0);
		picker.setMinValue(0);
		picker.setMaxValue(positions_str.length - 1);
		picker.setDisplayedValues(positions_str);
		picker.setOnValueChangedListener(new OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				if (newVal == positions_str.length - 1) {
					contentView.findViewById(R.id.et_other_position).setVisibility(View.VISIBLE);
				}
				else {
					contentView.findViewById(R.id.et_other_position).setVisibility(View.INVISIBLE);
				}
			}
		});
		if (!UIUtils.isEmpty(boxDetail.getPosition())&& !"-1".equals(boxDetail.getPosition())) {
			List<String> list = Arrays.asList(positions_int);
			int index = list.indexOf(boxDetail.getPosition());
			picker.setValue(index);
		}

		if (positions_int[positions_int.length - 1].equals(boxDetail.getPosition())) {
			contentView.findViewById(R.id.et_other_position).setVisibility(View.VISIBLE);
			((EditText) contentView.findViewById(R.id.et_other_position)).setText(boxDetail.getOther_position());
		}
		vg_picker_content.addView(picker);

		view_bg = contentView.findViewById(R.id.view_bg);
	}

	public void show() {
		view_bg.startAnimation(AnimationUtils.loadAnimation(context, R.anim.pop_fade_in));
		contentView.findViewById(R.id.vg_content).startAnimation(AnimationUtils.loadAnimation(context, R.anim.translate_bottom_in));
		super.showAtLocation(context.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
	}

	@Override
	public void dismiss() {
		Animation fade_out_anim = AnimationUtils.loadAnimation(context, R.anim.pop_fade_out);
		fade_out_anim.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				BoxBindPop.super.dismiss();
			}
		});
		view_bg.startAnimation(fade_out_anim);
		contentView.findViewById(R.id.vg_content).startAnimation(AnimationUtils.loadAnimation(context, R.anim.translate_bottom_out));

	}

	public interface OnConfirmListener {
		void onConfirmClick(String... results);
	}

	private LinearLayout vg_picker_content;

	private EditText et_box_id;

	private NumberPicker picker;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_save:
				save();
				break;
			case R.id.btn_cancel:
				dismiss();
				break;
			case R.id.btn_check:
				checkBox();
				break;
			case R.id.btn_scan:
				Intent intent = new Intent(context, CaptureActivity.class);
				context.startActivityForResult(intent, TaskDetailActivity.REQUEST_CODE_SCAN_BOX_ID);
				break;
		}

	}

	private void save() {
		if (UIUtils.isEmpty(et_box_id.getText().toString().trim())) {
			ToastUtils.showToast("盒子编号不能为空");
			return;
		}
		boolean hasChanged = false;
		if (!et_box_id.getText().toString().trim().equals(boxDetail.getGps_id())) {
			hasChanged = true;
			boxDetail.setGps_id(et_box_id.getText().toString().trim());
		}
		if (picker != null) {
			int value = picker.getValue();
			if (!positions_int[value].equals(boxDetail.getPosition())) {
				hasChanged = true;
				boxDetail.setPosition(positions_int[value]);
			}
			if (value == positions_int.length - 1) {
				String et_position = ((EditText) contentView.findViewById(R.id.et_other_position)).getText().toString().trim();
				if (UIUtils.isEmpty(et_position)) {
					ToastUtils.showToast("请输入盒子位置");
					return;
				}
				if (!et_position.equals(boxDetail.getOther_position())) {
					boxDetail.setOther_position(et_position);
					hasChanged = true;
				}
			}
			boxBindItemView.update();
		}
		if (hasChanged) {
			boxDetail.setHasFinish(false);
			context.initRedCircle();
		}
		dismiss();
	}

	private void checkBox() {
		if (UIUtils.isEmpty(et_box_id.getText().toString().trim())) {
			ToastUtils.showToast("盒子编号不能为空");
			return;
		}
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("gps_id", et_box_id.getText().toString().trim());
		hashMap.put("car_id", context.task_detail_entity.getCarId());
		context.TAG_NO_FIRST_REQUEST = "box_check";
		((TextView) contentView.findViewById(R.id.tv_box_problem)).setText("");
		CCWaitingDialog.show(context);
		new CCHttpEngine(context, NetConstants.NET_ID_BOX_CHECK, hashMap, context.TAG_NO_FIRST_REQUEST, new HttpCallBack() {
			@Override
			public void onSuccess(ResponseBean responseBean) {
				CCWaitingDialog.dismiss(context);
				if (responseBean.getCode() == 0) {
					ToastUtils.showToast("检验成功");
					contentView.findViewById(R.id.btn_save).setEnabled(true);
					boxDetail.setNote("校验成功");
					((TextView) contentView.findViewById(R.id.tv_box_problem)).setText(boxDetail.getNote());
				}
				else {
					ToastUtils.showToast(responseBean.getMessage());
					boxDetail.setNote(responseBean.getMessage());
					((TextView) contentView.findViewById(R.id.tv_box_problem)).setText(boxDetail.getNote());
				}
			}

			@Override
			public void onNetUnavailable(String net_unAvailabel) {
				ToastUtils.showToast(R.string.net_unavailable);
				CCWaitingDialog.dismiss(context);
			}

			@Override
			public void onFailure(IOException e) {
				ToastUtils.showToast(R.string.net_fail);
				CCWaitingDialog.dismiss(context);
			}
		}).executeTask();

	}

	public void onActivityResult(Intent data) {
		et_box_id.setText(data.getStringExtra("result"));
	}

}
