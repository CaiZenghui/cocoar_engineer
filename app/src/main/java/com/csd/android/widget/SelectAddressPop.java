package com.csd.android.widget;

import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.csd.android.R;
import com.csd.android.activity.SelectAddressActivity;

public class SelectAddressPop extends PopupWindow implements OnClickListener {

	private SelectAddressActivity context;
	private View view_bg;
	private View contentView;
	private ListView lv;
	
	

	public SelectAddressPop(SelectAddressActivity context) {
		this.context = context;
		contentView = LayoutInflater.from(context).inflate(R.layout.pop_select_address, null);
		setContentView(contentView);
		setHeight(LayoutParams.MATCH_PARENT);
		setWidth(LayoutParams.MATCH_PARENT);
		setOutsideTouchable(true);
		setFocusable(true);
		setBackgroundDrawable(new BitmapDrawable());

		initView();

	}

	private void initView() {
		lv = (ListView) contentView.findViewById(R.id.lv);
		view_bg = contentView.findViewById(R.id.view_bg);
		
		MyAdapter adapter = new MyAdapter();
		lv.setAdapter(adapter);

	}

	public void show() {
		view_bg.startAnimation(AnimationUtils.loadAnimation(context, R.anim.pop_fade_in));
		super.showAsDropDown(context.findViewById(R.id.vg_title), 0, 0);
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
				SelectAddressPop.super.dismiss();
			}
		});
		view_bg.startAnimation(fade_out_anim);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return null;
		}

	}

}
