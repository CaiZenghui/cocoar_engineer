package com.csd.android.activity;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.csd.android.R;
import com.csd.android.model.TaskCategoryIndex;
import com.csd.android.net.CCHttpEngine;
import com.csd.android.net.HttpCallBack;
import com.csd.android.net.NetConstants;
import com.csd.android.net.ResponseBean;
import com.csd.android.utils.ToastUtils;

public class TaskCategoryActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_category);
		initView();
	}

	@Override
	protected void onResume() {
		getTaskDes();
		super.onResume();
	}

	private void getTaskDes() {
		new CCHttpEngine(this, NetConstants.NET_ID_TASK_CATEGORY_DES, null, null, new HttpCallBack() {

			@Override
			public void onSuccess(ResponseBean responseBean) {
				if (responseBean.getCode() == 0) {
					ArrayList<TaskCategoryIndex> list = (ArrayList<TaskCategoryIndex>) responseBean.getData();
					String task_install_unread = list.get(0).getUnread();
					if (!"0".equals(task_install_unread)) {
						findViewById(R.id.tv_num_task_install).setVisibility(View.VISIBLE);
						((TextView) findViewById(R.id.tv_num_task_install)).setText(task_install_unread);
					}

					String str_install = String.format(getResources().getString(R.string.task_install_des), list.get(0).getCurrent(), list.get(0)
							.getAll());
					SpannableString ss = new SpannableString(str_install);
					ss.setSpan(new ForegroundColorSpan(0xff92a84e), str_install.indexOf(list.get(0).getCurrent()),
							str_install.indexOf(list.get(0).getCurrent()) + list.get(0).getCurrent().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					ss.setSpan(new AbsoluteSizeSpan(34, false), str_install.indexOf(list.get(0).getCurrent()),
							str_install.indexOf(list.get(0).getCurrent()) + list.get(0).getCurrent().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					ss.setSpan(new ForegroundColorSpan(0xff92a84e), str_install.lastIndexOf(list.get(0).getAll()),
							str_install.lastIndexOf(list.get(0).getAll()) + list.get(0).getAll().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					ss.setSpan(new AbsoluteSizeSpan(34, false), str_install.lastIndexOf(list.get(0).getAll()),
							str_install.lastIndexOf(list.get(0).getAll()) + list.get(0).getAll().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					((TextView) findViewById(R.id.tv_task_install_detail)).setText(ss);

					String task_finish_unread = list.get(1).getUnread();
					if (!"0".equals(task_finish_unread)) {
						findViewById(R.id.tv_num_task_finish).setVisibility(View.VISIBLE);
						((TextView) findViewById(R.id.tv_num_task_finish)).setText(task_finish_unread);
					}

					String str_finish = String.format(getResources().getString(R.string.task_finish_des), list.get(1).getCurrent(), list.get(1)
							.getAll());
					ss = new SpannableString(str_finish);
					ss.setSpan(new ForegroundColorSpan(0xff92a84e), str_finish.indexOf(list.get(1).getCurrent()),
							str_finish.indexOf(list.get(1).getCurrent()) + list.get(1).getCurrent().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					ss.setSpan(new AbsoluteSizeSpan(34, false), str_finish.indexOf(list.get(1).getCurrent()),
							str_finish.indexOf(list.get(1).getCurrent()) + list.get(1).getCurrent().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					ss.setSpan(new ForegroundColorSpan(0xff92a84e), str_finish.lastIndexOf(list.get(1).getAll()),
							str_finish.lastIndexOf(list.get(1).getAll()) + list.get(1).getAll().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					ss.setSpan(new AbsoluteSizeSpan(34, false), str_finish.lastIndexOf(list.get(1).getAll()),
							str_finish.lastIndexOf(list.get(1).getAll()) + list.get(1).getAll().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					((TextView) findViewById(R.id.tv_task_finish_detail)).setText(ss);

					String task_unfinish_unread = list.get(2).getUnread();
					if (!"0".equals(task_unfinish_unread)) {
						findViewById(R.id.tv_num_task_unfinish).setVisibility(View.VISIBLE);
						((TextView) findViewById(R.id.tv_num_task_unfinish)).setText(task_unfinish_unread);
					}
					String str_unfinish = String.format(getResources().getString(R.string.task_unfinish_des), list.get(2).getCurrent(), list.get(2)
							.getAll());

					ss = new SpannableString(str_unfinish);
					ss.setSpan(new ForegroundColorSpan(0xff92a84e), str_unfinish.indexOf(list.get(2).getCurrent()),
							str_unfinish.indexOf(list.get(2).getCurrent()) + list.get(2).getCurrent().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					ss.setSpan(new AbsoluteSizeSpan(34, false), str_unfinish.indexOf(list.get(2).getCurrent()),
							str_unfinish.indexOf(list.get(2).getCurrent()) + list.get(2).getCurrent().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					ss.setSpan(new ForegroundColorSpan(0xff92a84e), str_unfinish.lastIndexOf(list.get(2).getAll()),
							str_unfinish.lastIndexOf(list.get(2).getAll()) + list.get(2).getAll().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					ss.setSpan(new AbsoluteSizeSpan(34, false), str_unfinish.lastIndexOf(list.get(2).getAll()),
							str_unfinish.lastIndexOf(list.get(2).getAll()) + list.get(2).getAll().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

					((TextView) findViewById(R.id.tv_task_unfinish_detail)).setText(ss);

					String task_unsuccess_unread = list.get(3).getUnread();
					if (!"0".equals(task_unsuccess_unread)) {
						findViewById(R.id.tv_num_task_unsuccess).setVisibility(View.VISIBLE);
						((TextView) findViewById(R.id.tv_num_task_unsuccess)).setText(task_unsuccess_unread);
					}
					String str_unsuccess = String.format(getResources().getString(R.string.task_unsuccess_des), list.get(3).getCurrent(), list.get(3)
							.getAll());
					ss = new SpannableString(str_unsuccess);
					ss.setSpan(new ForegroundColorSpan(0xff92a84e), str_unsuccess.indexOf(list.get(3).getCurrent()),
							str_unsuccess.indexOf(list.get(3).getCurrent()) + list.get(3).getCurrent().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					ss.setSpan(new AbsoluteSizeSpan(34, false), str_unsuccess.indexOf(list.get(3).getCurrent()),
							str_unsuccess.indexOf(list.get(3).getCurrent()) + list.get(3).getCurrent().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					ss.setSpan(new ForegroundColorSpan(0xff92a84e), str_unsuccess.lastIndexOf(list.get(3).getAll()),
							str_unsuccess.lastIndexOf(list.get(3).getAll()) + list.get(3).getAll().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					ss.setSpan(new AbsoluteSizeSpan(34, false), str_unsuccess.lastIndexOf(list.get(3).getAll()),
							str_unsuccess.lastIndexOf(list.get(3).getAll()) + list.get(3).getAll().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					((TextView) findViewById(R.id.tv_task_unsuccess_detail)).setText(ss);
				}
				else {
					ToastUtils.showToast(responseBean.getMessage());
				}

			}

			@Override
			public void onNetUnavailable(String net_unAvailabel) {
				ToastUtils.showToast(R.string.net_unavailable);
			}

			@Override
			public void onFailure(IOException e) {
				ToastUtils.showToast(R.string.net_fail);
			}
		}).executeTask();
	}

	private void initView() {
		findViewById(R.id.vg_task_finish).setOnClickListener(this);
		findViewById(R.id.vg_task_unfinish).setOnClickListener(this);
		findViewById(R.id.vg_task_install).setOnClickListener(this);
		findViewById(R.id.vg_task_record).setOnClickListener(this);
		findViewById(R.id.vg_task_unsuccess).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, TaskListActivity.class);
		switch (v.getId()) {
			case R.id.vg_task_install:
				intent.putExtra(TaskListActivity.INTENT_EXTRA_LIST_TYPE, TaskListActivity.TYPE_TODAY);
				startActivity(intent);
				break;

			case R.id.vg_task_finish:
				intent.putExtra(TaskListActivity.INTENT_EXTRA_LIST_TYPE, TaskListActivity.TYPE_FINISH);
				startActivity(intent);
				break;
			case R.id.vg_task_unfinish:
				intent.putExtra(TaskListActivity.INTENT_EXTRA_LIST_TYPE, TaskListActivity.TYPE_UNFINISH);
				startActivity(intent);
				break;
			case R.id.vg_task_unsuccess:
				intent.putExtra(TaskListActivity.INTENT_EXTRA_LIST_TYPE, TaskListActivity.TYPE_UNSUCCESS);
				startActivity(intent);
				break;
			case R.id.vg_task_record:
				intent = new Intent(this, MapTestActivity.class);
				startActivity(intent);
				break;
		}

	}
}
