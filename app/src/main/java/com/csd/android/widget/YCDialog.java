package com.csd.android.widget;
import com.csd.android.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.csd.android.utils.UIUtils;

public class YCDialog extends Dialog {

	@Override
	public void show() {
		super.show();
		LayoutParams params = getWindow().getAttributes();
		params.width = LayoutParams.MATCH_PARENT;
		params.height = LayoutParams.MATCH_PARENT;
		getWindow().setAttributes(params);
	}

	private static Dialog dialog;

	public static void show(Context context, String msg, String leftName, String rightName, View.OnClickListener leftListener,
			View.OnClickListener rightListener, boolean canCancelable) {
		Builder builder = new Builder(context);
		builder.setLeftButton(leftName, leftListener);
		if (rightName != null && rightListener != null) {
			builder.setRightButton(rightName, rightListener);
		}
		builder.setMessage(msg);
		builder.setCancelable(canCancelable);
		dialog = builder.create();
		if (context instanceof Activity) {
			if (((Activity) context).isFinishing()) {
				return;
			}

		}
		dialog.show();
	}

	public static void close() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	public static void show(Context context, String msg, String leftName, String rightName, View.OnClickListener leftListener,
			View.OnClickListener rightListener) {
		show(context, msg, leftName, rightName, leftListener, rightListener, true);
	}

	public static void show(Context context, String msg, String name) {
		if (UIUtils.isEmpty(name)) {
			name = context.getResources().getString(R.string.ok);
		}
		show(context, msg, name, null, null, null);
	}

	public static void show(Context context, String msg, String name, View.OnClickListener listener) {
		show(context, msg, name, null, listener, null);
	}

	public static void show(Context context, String msg, String name, View.OnClickListener listener, boolean canCancleable) {
		show(context, msg, name, null, listener, null, canCancleable);
	}

	private YCDialog(Context context, int theme) {
		super(context, theme);
	}

	private YCDialog(Context context) {
		super(context);
	}

	public static class Builder implements View.OnClickListener {

		private Context context;
		private CharSequence message;
		private CharSequence left_button_text;
		private CharSequence right_button_text;
		private boolean cancelable = true;

		private View.OnClickListener leftButtonClickListener, rightButtonClickListener;

		public Builder(Context context) {
			this.context = context;
		}

		public Builder setMessage(CharSequence message) {
			this.message = message;
			return this;
		}

		public Builder setMessage(int message) {
			this.message = (String) context.getText(message);
			return this;
		}

		public Builder setCancelable(boolean cancelable) {
			this.cancelable = cancelable;
			return this;
		}

		public Builder setLeftButton(int res_left_button_text_resId, View.OnClickListener listener) {
			return setLeftButton(context.getText(res_left_button_text_resId), listener);
		}

		public Builder setLeftButton(CharSequence left_button_text, View.OnClickListener listener) {
			this.left_button_text = left_button_text;
			this.leftButtonClickListener = listener;
			return this;
		}

		public Builder setRightButton(int right_button_text_resId, View.OnClickListener listener) {
			return setRightButton(context.getText(right_button_text_resId), listener);
		}

		public Builder setRightButton(CharSequence right_button_text, View.OnClickListener listener) {
			this.right_button_text = right_button_text;
			this.rightButtonClickListener = listener;
			return this;
		}

		private YCDialog create() {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final YCDialog dialog = new YCDialog(context, R.style.CustomDialogTheme);
			View layout = inflater.inflate(R.layout.layout_yc_dialog, null);
			dialog.setContentView(layout);

			TextView tv_message = ((TextView) layout.findViewById(R.id.tv_message));
			Button btn_left = (Button) layout.findViewById(R.id.btn_left);
			Button btn_right = (Button) layout.findViewById(R.id.btn_right);
			View vg_right_button = layout.findViewById(R.id.vg_right_button);

			if (!TextUtils.isEmpty(left_button_text)) {
				btn_left.setText(left_button_text);
				btn_left.setVisibility(View.VISIBLE);
				btn_left.setOnClickListener(this);
			}
			else {
				btn_left.setVisibility(View.GONE);
			}

			if (!TextUtils.isEmpty(right_button_text)) {
				btn_right.setText(right_button_text);
				btn_right.setVisibility(View.VISIBLE);
				btn_right.setOnClickListener(this);
			}
			else {
				vg_right_button.setVisibility(View.GONE);
			}

			if (!TextUtils.isEmpty(message)) {
				tv_message.setText(message);
			}
			else {
				tv_message.setVisibility(View.GONE);
			}
			dialog.setContentView(layout);
			dialog.setCancelable(cancelable);
			return dialog;
		}

		public YCDialog show() {
			YCDialog dialog = create();
			dialog.show();
			return dialog;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.btn_left:
					if (leftButtonClickListener != null) {
						leftButtonClickListener.onClick(v);
					}
					close();
					break;

				case R.id.btn_right:
					if (rightButtonClickListener != null) {
						rightButtonClickListener.onClick(v);
					}
					close();
					break;
			}

		}
	}
}