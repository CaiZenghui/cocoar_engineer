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
import com.csd.android.widget.NumberPicker.OnValueChangeListener;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class DatePickerPop extends PopupWindow implements OnClickListener {

	private Activity context;

	private View view_bg;
	private View contentView;
	private static String[] years = new String[50];
	private static String[] months = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" };
	private static String[] days_31 = new String[31];
	private static String[] days_30 = new String[30];
	private static String[] days_28 = new String[28];
	private static String[] days_29 = new String[29];
	private static String[][] data = { years, months, days_31 };
	private static ArrayList<Integer> m1 = new ArrayList<Integer>();

	static {
		for (int i = 0; i < 50; i++) {
			years[i] = (2001 + i) + "";
		}

		for (int i = 0; i < 31; i++) {
			days_31[i] = (i + 1) + "";
		}
		for (int i = 0; i < 30; i++) {
			days_30[i] = (i + 1) + "";
		}
		for (int i = 0; i < 28; i++) {
			days_28[i] = (i + 1) + "";
		}
		for (int i = 0; i < 29; i++) {
			days_29[i] = (i + 1) + "";
		}

		m1.add(1);
		m1.add(3);
		m1.add(5);
		m1.add(7);
		m1.add(8);
		m1.add(10);
		m1.add(12);
	}

	public DatePickerPop(Activity context) {
		this.context = context;

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
		data[2] = days_31;
		for (int i = 0; i < 3; i++) {
			NumberPicker picker = (NumberPicker) LayoutInflater.from(context).inflate(R.layout.layout_item_pop_picker, vg_picker_content, false);
			picker.setId(i);
			picker.setMinValue(0);
			picker.setMaxValue(data[i].length - 1);
			picker.setDisplayedValues(data[i]);
			vg_picker_content.addView(picker);
			if (i == 0) {
				picker.setOnValueChangedListener(new OnValueChangeListener() {
					public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
						NumberPicker month_picker = (NumberPicker) vg_picker_content.findViewById(1);
						if (month_picker.getValue() != 1) {
							return;
						}

						NumberPicker old_day_picker = (NumberPicker) vg_picker_content.findViewById(2);
						NumberPicker new_day_picker = (NumberPicker) LayoutInflater.from(context).inflate(R.layout.layout_item_pop_picker,
								vg_picker_content, false);
						new_day_picker.setId(2);
						int current_value = old_day_picker.getValue() + 1;
						GregorianCalendar cal = new GregorianCalendar();
						if (cal.isLeapYear(Integer.parseInt(years[((NumberPicker) vg_picker_content.findViewById(0)).getValue()]))) {
							data[2] = days_29;
							new_day_picker.setMinValue(0);
							new_day_picker.setMaxValue(data[2].length - 1);
							new_day_picker.setDisplayedValues(data[2]);
							if (current_value > 29) {
								current_value = 29;
							}

						}
						else {
							data[2] = days_28;
							new_day_picker.setMinValue(0);
							new_day_picker.setMaxValue(data[2].length - 1);
							new_day_picker.setDisplayedValues(data[2]);
							if (current_value > 28) {
								current_value = 28;
							}
						}

						new_day_picker.setValue(current_value - 1);
						vg_picker_content.removeView(old_day_picker);
						vg_picker_content.addView(new_day_picker);
					}
				});

			}
			else if (i == 1) {
				picker.setOnValueChangedListener(new OnValueChangeListener() {
					public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
						oldVal++;
						newVal++;
						if (newVal == 2 || (m1.contains(oldVal) && !m1.contains(newVal)) || (!m1.contains(oldVal) && m1.contains(newVal))) {
							NumberPicker old_day_picker = (NumberPicker) vg_picker_content.findViewById(2);
							NumberPicker new_day_picker = (NumberPicker) LayoutInflater.from(context).inflate(R.layout.layout_item_pop_picker,
									vg_picker_content, false);
							new_day_picker.setId(2);
							int current_value = old_day_picker.getValue() + 1;
							if (newVal == 2) {
								GregorianCalendar cal = new GregorianCalendar();

								if (cal.isLeapYear(Integer.parseInt(years[((NumberPicker) vg_picker_content.findViewById(0)).getValue()]))) {
									data[2] = days_29;
									new_day_picker.setMinValue(0);
									new_day_picker.setMaxValue(data[2].length - 1);
									new_day_picker.setDisplayedValues(data[2]);
									if (current_value > 29) {
										current_value = 29;
									}

								}
								else {
									data[2] = days_28;
									new_day_picker.setMinValue(0);
									new_day_picker.setMaxValue(data[2].length - 1);
									new_day_picker.setDisplayedValues(data[2]);
									if (current_value > 28) {
										current_value = 28;
									}
								}
							}
							else if (m1.contains(newVal)) {
								data[2] = days_31;
								new_day_picker.setMinValue(0);
								new_day_picker.setMaxValue(data[2].length - 1);
								new_day_picker.setDisplayedValues(data[2]);
							}
							else {
								data[2] = days_30;
								new_day_picker.setMinValue(0);
								new_day_picker.setMaxValue(data[2].length - 1);
								new_day_picker.setDisplayedValues(data[2]);
								if (current_value > 30) {
									current_value = 30;
								}
							}
							new_day_picker.setValue(current_value - 1);
							vg_picker_content.removeView(old_day_picker);
							vg_picker_content.addView(new_day_picker);

						}
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
				DatePickerPop.super.dismiss();
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
