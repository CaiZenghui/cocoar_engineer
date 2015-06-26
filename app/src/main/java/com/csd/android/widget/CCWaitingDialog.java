package com.csd.android.widget;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.csd.android.R;

public class CCWaitingDialog {
	public static int CCWAITINGDIALOG_ID = 999;

	public static void show(Activity context) {
		if (context.findViewById(CCWAITINGDIALOG_ID) != null) {
			return;
		}
		View content = LayoutInflater.from(context).inflate(R.layout.layout_ccwaiting_dialog, null);
		content.setId(CCWAITINGDIALOG_ID);
		content.setFocusable(true);
		content.setClickable(true);
		content.setFocusableInTouchMode(true);
		((FrameLayout) context.getWindow().getDecorView()).addView(content);

		ImageView imageView = (ImageView) content.findViewById(R.id.loadingImageView);
		AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
		animationDrawable.setOneShot(false);
		animationDrawable.start();

		WindowManager.LayoutParams params = context.getWindow().getAttributes();
		params.alpha = 0.5f;
		context.getWindow().setAttributes(params);
	}

	public static void dismiss(Activity context) {
		if (isShowing(context)) {
			((FrameLayout) context.getWindow().getDecorView()).removeView(context.findViewById(CCWAITINGDIALOG_ID));
			WindowManager.LayoutParams params = context.getWindow().getAttributes();
			params.alpha = 1.0f;
			context.getWindow().setAttributes(params);
		}
	}

	public static boolean isShowing(Activity context) {
		return context.findViewById(CCWAITINGDIALOG_ID) == null ? false : true;
	}
}
