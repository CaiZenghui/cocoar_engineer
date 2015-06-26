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

public class PickerPop extends PopupWindow implements OnClickListener {

	private Activity context;

	private View view_bg;
	private View contentView;

	private String[][] strs;

	public PickerPop(Activity context, String[]... strs) {
		this.context = context;
		this.strs = strs;

		contentView = LayoutInflater.from(context).inflate(R.layout.layout_pop_picker, null);
		setContentView(contentView);
		setOutsideTouchable(true);
		setFocusable(true);
		setBackgroundDrawable(new BitmapDrawable());
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);

		initView();
	}

	private void initView() {
		vg_picker_content = (LinearLayout) contentView.findViewById(R.id.vg_picker_content);
		for (int i = 0; i < strs.length; i++) {
			NumberPicker picker = (NumberPicker) LayoutInflater.from(context).inflate(R.layout.layout_item_pop_picker, vg_picker_content, false);
			picker.setId(i);
			picker.setMinValue(0);
			picker.setMaxValue(strs[i].length - 1);
			picker.setDisplayedValues(strs[i]);
			vg_picker_content.addView(picker);
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
				PickerPop.super.dismiss();
			}
		});
		view_bg.startAnimation(fade_out_anim);
		contentView.findViewById(R.id.vg_content).startAnimation(AnimationUtils.loadAnimation(context, R.anim.translate_bottom_out));

	}

	public interface OnConfirmListener {
		void onConfirmClick(int... strs);
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
				int[] results = new int[strs.length];
				for (int i = 0; i < strs.length; i++) {
					results[i] = ((NumberPicker) vg_picker_content.findViewById(i)).getValue();
				}
				onConfirmListener.onConfirmClick(results);
				break;
		}

		dismiss();
	}

}
