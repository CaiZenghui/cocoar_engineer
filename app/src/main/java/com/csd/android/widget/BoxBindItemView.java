package com.csd.android.widget;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.csd.android.R;
import com.csd.android.activity.TaskDetailActivity;
import com.csd.android.model.Box.BoxDetail;
import com.csd.android.utils.UIUtils;
import com.csd.android.viewloader.BoxBindViewLoader;

import java.util.Arrays;
import java.util.List;

public class BoxBindItemView extends FrameLayout implements OnClickListener {
	private TaskDetailActivity context;
	private BoxDetail boxDetail;
	private BoxBindViewLoader loader;
	private TextView tv_2;

	public BoxBindItemView(BoxBindViewLoader boxBindViewLoader, BoxDetail boxDetail) {
		super(boxBindViewLoader.context);
		this.loader = boxBindViewLoader;
		this.context = (TaskDetailActivity) boxBindViewLoader.context;
		this.boxDetail = boxDetail;

		initView();
	}

	private void initView() {
		LayoutInflater.from(getContext()).inflate(R.layout.item_bind_box, this);
		TextView tv_1 = (TextView) findViewById(R.id.tv_1);
		tv_2 = (TextView) findViewById(R.id.tv_2);
		if (boxDetail.getBoxStatus() == 2) {
			tv_1.setTextColor(0xffff0000);
			tv_2.setTextColor(0xffff0000);
		}
		else {
			tv_1.setTextColor(0xff393939);
			tv_2.setTextColor(0xff393939);
		}

		tv_1.setTranslationX(-UIUtils.dp2px(17));
		findViewById(R.id.iv_status).setVisibility(View.INVISIBLE);

		tv_1.setText(boxDetail.getName());
		if (!UIUtils.isEmpty(boxDetail.getPosition())) {

			if ("19".equals(boxDetail.getPosition()) || "29".equals(boxDetail.getPosition()) || "39".equals(boxDetail.getPosition())) {
				tv_2.setText(boxDetail.getOther_position());
			}
			else {
				List<String> positions_str = Arrays.asList(context.getResources().getStringArray(R.array.box_position_str));
				List<String> positions_int = Arrays.asList(context.getResources().getStringArray(R.array.box_position_int));
				int index_in = positions_int.indexOf(boxDetail.getPosition());
				tv_2.setText(positions_str.get(index_in));
			}

		}
		else {
			tv_2.setText("请选择安装位置");
		}

		findViewById(R.id.vg_tv).setBackgroundColor(0xffffffff);

		tv_1.setOnClickListener(this);
		tv_2.setOnClickListener(this);

		if (boxDetail.getBoxStatus() == 0 || boxDetail.getBoxStatus() == 1) {
			findViewById(R.id.iv_status).setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tv_2:
				loader.initBoxItem(boxDetail);
				context.pop = new BoxBindPop(context, this, boxDetail);
				context.pop.show();
				break;

			case R.id.tv_1:
				loader.initBoxItem(boxDetail);
				break;
		}

		loader.map_wraper.clear();
		loader.tv_test_result.setText("");
	}

	public void update() {
		if (!UIUtils.isEmpty(boxDetail.getPosition())) {
			if (boxDetail.getPosition().equals("19") || boxDetail.getPosition().equals("29") || boxDetail.getPosition().equals("39")) {
				tv_2.setText(boxDetail.getOther_position());
				return;
			}
			List<String> positions_str = Arrays.asList(context.getResources().getStringArray(R.array.box_position_str));
			List<String> positions_int = Arrays.asList(context.getResources().getStringArray(R.array.box_position_int));
			int index_in = positions_int.indexOf(boxDetail.getPosition());
			tv_2.setText(positions_str.get(index_in));
		}
		else {
			tv_2.setText("请选择安装位置");
		}
	}
}
