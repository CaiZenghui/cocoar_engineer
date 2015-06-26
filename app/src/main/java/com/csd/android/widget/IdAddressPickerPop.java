package com.csd.android.widget;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.csd.android.R;
import com.csd.android.model.IdCity;
import com.csd.android.widget.NumberPicker.OnValueChangeListener;

import java.util.ArrayList;

public class IdAddressPickerPop extends PopupWindow implements OnClickListener {

	private Activity context;
	private ArrayList<IdCity> list;

	private View view_bg;
	private View contentView;
	private String[] citys;
	private String[] area;
	private String[][] data = { citys, area };
	private String[][] areas;

	public IdAddressPickerPop(Activity context, ArrayList<IdCity> city_list) {
		this.context = context;
		this.list = city_list;

		contentView = LayoutInflater.from(context).inflate(R.layout.layout_pop_picker, null);
		setContentView(contentView);
		setOutsideTouchable(true);
		setFocusable(true);
		setBackgroundDrawable(new BitmapDrawable());
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);

		initData();

		initView();
	}

	private void initData() {
		citys = new String[list.size()];
		areas = new String[list.size()][];
		for (int i = 0; i < list.size(); i++) {
			citys[i] = list.get(i).getCityName();
			areas[i] = new String[list.get(i).getAreaInfo().size()];
			for (int j = 0; j < list.get(i).getAreaInfo().size(); j++) {
				areas[i][j] = list.get(i).getAreaInfo().get(j).getAreaName();
			}
		}
	}

	private void initView() {
		vg_picker_content = (LinearLayout) contentView.findViewById(R.id.vg_picker_content);
		data[0] = citys;
		data[1] = areas[0];
		for (int i = 0; i < data.length; i++) {
			NumberPicker picker = (NumberPicker) LayoutInflater.from(context).inflate(R.layout.layout_item_pop_picker, vg_picker_content, false);
			picker.setId(i);
			picker.setMinValue(0);
			picker.setMaxValue(data[i].length - 1);
			picker.setDisplayedValues(data[i]);
			vg_picker_content.addView(picker);
			if (i == 0) {
				picker.setOnValueChangedListener(new OnValueChangeListener() {
					public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
						NumberPicker old_area_picker = (NumberPicker) vg_picker_content.findViewById(1);
						NumberPicker new_area_picker = (NumberPicker) LayoutInflater.from(context).inflate(R.layout.layout_item_pop_picker,
								vg_picker_content, false);
						new_area_picker.setId(1);
						data[1] = areas[newVal];
						new_area_picker.setMinValue(0);
						new_area_picker.setMaxValue(data[1].length - 1);
						new_area_picker.setDisplayedValues(data[1]);
						vg_picker_content.removeView(old_area_picker);
						vg_picker_content.addView(new_area_picker);
					}
				});

			}
		}

		contentView.findViewById(R.id.tv_cancel).setOnClickListener(this);
		contentView.findViewById(R.id.tv_confirm).setOnClickListener(this);

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
				IdAddressPickerPop.super.dismiss();
			}
		});
		view_bg.startAnimation(fade_out_anim);
		contentView.findViewById(R.id.vg_content).startAnimation(AnimationUtils.loadAnimation(context, R.anim.translate_bottom_out));

	}

	public interface OnConfirmListener {
		void onConfirmClick(String... results);
	}

	private OnConfirmListener onConfirmListener;

	private LinearLayout vg_picker_content;

	public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
		this.onConfirmListener = onConfirmListener;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tv_confirm:
				String[] results = new String[data.length];
				for (int i = 0; i < data.length; i++) {
					results[i] = data[i][((NumberPicker) vg_picker_content.findViewById(i)).getValue()];
				}
				onConfirmListener.onConfirmClick(results);
				break;
		}

		dismiss();
	}

}
