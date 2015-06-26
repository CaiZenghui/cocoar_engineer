package com.csd.android.utils;

import android.text.TextUtils;
import android.widget.Toast;

import com.csd.android.CCApplication;

/**
 * 对toast的简单封装
 * @author caizenghui
 *
 */
public class ToastUtils {

	private static Toast mToast;

	public static void showToast(CharSequence text) {
		if (TextUtils.isEmpty(text)) {
			return;
		}
		if (mToast == null) {
			mToast = Toast.makeText(CCApplication.getApplication(), text, Toast.LENGTH_SHORT);
		}
		else {
			mToast.setText(text);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}
		mToast.show();
	}

	public static final void showToast(int resId) {
		showToast(CCApplication.getApplication().getText(resId));
	}

	public static void cancelToast() {
		if (mToast != null) {
			mToast.cancel();
		}
	}
}
