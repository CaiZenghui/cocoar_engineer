package com.csd.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.csd.android.R;

public class TabsHorizontalScrollView extends HorizontalScrollView implements OnClickListener {

	public TabsHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);

		initView();
	}

	private void initView() {
		LinearLayout vg = new LinearLayout(getContext());
		vg.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		addView(vg);

		View identity_card_view = LayoutInflater.from(getContext()).inflate(R.layout.layout_id_check_tab, null, false);
		identity_card_view.setId(1);
		identity_card_view.setOnClickListener(this);
		identity_card_view.findViewById(R.id.indicator).setVisibility(View.VISIBLE);
		((TextView) identity_card_view.findViewById(R.id.tv_num)).setText("1");
		((TextView) identity_card_view.findViewById(R.id.tv_content)).setText("身份证");
		((TextView) identity_card_view.findViewById(R.id.tv_status)).setText("未上传");
		vg.addView(identity_card_view);

		View driving_license_view = LayoutInflater.from(getContext()).inflate(R.layout.layout_id_check_tab, null, false);
		driving_license_view.setId(2);
		driving_license_view.setOnClickListener(this);
		((TextView) driving_license_view.findViewById(R.id.tv_num)).setText("2");
		((TextView) driving_license_view.findViewById(R.id.tv_content)).setText("行驶证");
		((TextView) driving_license_view.findViewById(R.id.tv_status)).setText("已上传");
		vg.addView(driving_license_view);

		View cards_check_view = LayoutInflater.from(getContext()).inflate(R.layout.layout_id_check_tab, null, false);
		cards_check_view.setId(3);
		cards_check_view.setOnClickListener(this);
		((TextView) cards_check_view.findViewById(R.id.tv_num)).setText("3");
		((TextView) cards_check_view.findViewById(R.id.tv_content)).setText("两证并审");
		((TextView) cards_check_view.findViewById(R.id.tv_status)).setText("已通过");
		vg.addView(cards_check_view);

		View insurance_policy_view = LayoutInflater.from(getContext()).inflate(R.layout.layout_id_check_tab, null, false);
		insurance_policy_view.setId(4);
		insurance_policy_view.setOnClickListener(this);
		((TextView) insurance_policy_view.findViewById(R.id.tv_num)).setText("4");
		((TextView) insurance_policy_view.findViewById(R.id.tv_content)).setText("保单");
		((TextView) insurance_policy_view.findViewById(R.id.tv_status)).setText("不通过");
		vg.addView(insurance_policy_view);

		View car_photo_view = LayoutInflater.from(getContext()).inflate(R.layout.layout_id_check_tab, null, false);
		car_photo_view.setId(5);
		car_photo_view.setOnClickListener(this);
		((TextView) car_photo_view.findViewById(R.id.tv_num)).setText("5");
		((TextView) car_photo_view.findViewById(R.id.tv_content)).setText("车辆照片");
		((TextView) car_photo_view.findViewById(R.id.tv_status)).setText("不通过");
		vg.addView(car_photo_view);
	}

	private int current_item = 1;
	private ItemClickListener itemClickListener;

	public interface ItemClickListener {
		void click_identity_card_view();

		void click_driving_license_view();

		void click_cards_check_view();

		void click_insurance_policy_view();

		void click_car_photo_view();
	}

	public void setItemClickListener(ItemClickListener itemClickListener) {
		this.itemClickListener = itemClickListener;
	}

	@Override
	public void onClick(View v) {
		if (current_item == v.getId()) {
			return;
		}
		switch (v.getId()) {
			case 1:
				initIndicator(0);
				itemClickListener.click_identity_card_view();
				break;

			case 2:
				initIndicator(1);
				itemClickListener.click_driving_license_view();
				break;
			case 3:
				initIndicator(2);
				itemClickListener.click_cards_check_view();
				break;
			case 4:
				initIndicator(3);
				itemClickListener.click_insurance_policy_view();
				break;
			case 5:
				initIndicator(4);
				itemClickListener.click_car_photo_view();
				break;
		}
		current_item = v.getId();
	}

	private void initIndicator(int index) {
		View[] views = { findViewById(1), findViewById(2), findViewById(3), findViewById(4), findViewById(5) };
		for (int i = 0; i < views.length; i++) {
			if (i == index) {
				views[i].findViewById(R.id.indicator).setVisibility(View.VISIBLE);
			}
			else {
				views[i].findViewById(R.id.indicator).setVisibility(View.INVISIBLE);
			}
		}

	}
}
