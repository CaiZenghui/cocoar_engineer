package com.csd.android.widget;

import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

import com.csd.android.R;
import com.csd.android.activity.TaskDetailActivity;

public class ItemPop extends PopupWindow implements OnClickListener {

	private TaskDetailActivity context;
	private View view_bg;
	private View contentView;
	private boolean addCancel = false;

	private String[] strs;

	public ItemPop(TaskDetailActivity context, String[] strs, boolean addCancel) {
		this.context = context;
		this.strs = strs;
		this.addCancel = addCancel;

		contentView = LayoutInflater.from(context).inflate(R.layout.layout_item_pop, null);
		setContentView(contentView);
		setHeight(LayoutParams.WRAP_CONTENT);
		setWidth(LayoutParams.MATCH_PARENT);
		setOutsideTouchable(true);
		setFocusable(true);
		setBackgroundDrawable(new BitmapDrawable());

		initView();

	}

	private void initView() {
		LinearLayout vg_content = (LinearLayout) contentView.findViewById(R.id.vg_content);

		for (int i = 0; i < strs.length; i++) {
			Button view = (Button) LayoutInflater.from(context).inflate(R.layout.layout_item_pop_button, vg_content, false);
			vg_content.addView(view);
			view.setId(i);
			view.setText(strs[i]);
			view.setOnClickListener(this);
		}

		if (addCancel) {
			Button view = (Button) LayoutInflater.from(context).inflate(R.layout.layout_item_pop_button, vg_content, false);
			vg_content.addView(view);
			view.setId(99);
			view.setText("取消");
			view.setOnClickListener(this);
		}
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
				ItemPop.super.dismiss();
			}
		});
		view_bg.startAnimation(fade_out_anim);
		contentView.findViewById(R.id.vg_content).startAnimation(AnimationUtils.loadAnimation(context, R.anim.translate_bottom_out));

	}

	public interface OnClickItemListener {
		void onClickItem(int index);
	}

	private OnClickItemListener onClickItemListener;

	public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
		this.onClickItemListener = onClickItemListener;
	}

	@Override
	public void onClick(View v) {
		onClickItemListener.onClickItem(v.getId());
		dismiss();
	}

}
