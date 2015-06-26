package com.csd.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.csd.android.R;
import com.csd.android.UserManager;
import com.csd.android.constants.SPConstants;
import com.csd.android.utils.SPUtils;
import com.csd.android.utils.ToastUtils;
import com.csd.android.utils.UIUtils;
import com.csd.android.widget.YCDialog;

public class MainActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_role);
		initView();
	}

	private void initView() {
		findViewById(R.id.iv_task_install).setOnClickListener(this);
		findViewById(R.id.iv_task_sale).setOnClickListener(this);
		findViewById(R.id.iv_logout).setOnClickListener(this);
		findViewById(R.id.iv_logout).setVisibility(View.VISIBLE);

		TextView tv_welcome_tip = (TextView) findViewById(R.id.tv_welcome_tip);
		tv_welcome_tip.setText("欢迎登陆cocar信息采集系统，" + UserManager.getUser().getUserName() + "！");
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
			case R.id.iv_task_install:
				intent = new Intent(this, TaskCategoryActivity.class);
				startActivity(intent);
				break;

			case R.id.iv_task_sale:
				ToastUtils.showToast(UIUtils.getChannel());
//				intent = new Intent(this, MapTestActivity.class);
//				startActivity(intent);
				break;

			case R.id.iv_logout:
				YCDialog.show(this, "确定要登出当前账号吗？", "取消", "确定", null, new OnClickListener() {
					public void onClick(View v) {
						SPUtils.clearPreferences(SPConstants.SHARED_PREFS_FILE_NAME_USER);
						startActivity(new Intent(MainActivity.this, LoginActivity.class));
						UserManager.setUser(null);
						finish();
					}
				});
				break;
		}

	}

	private long firstTimeClick = 0;

	@Override
	public void onBackPressed() {
		if (System.currentTimeMillis() - firstTimeClick > 2000) {
			ToastUtils.showToast("再按一次退出");
			firstTimeClick = System.currentTimeMillis();
		}
		else {
			finish();
		}
	}
}
