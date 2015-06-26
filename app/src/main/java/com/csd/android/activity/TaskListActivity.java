package com.csd.android.activity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.csd.android.R;
import com.csd.android.model.TaskListEntity;
import com.csd.android.model.TaskListWrapper;
import com.csd.android.net.CCHttpEngine;
import com.csd.android.net.HttpCallBack;
import com.csd.android.net.NetConstants;
import com.csd.android.net.ResponseBean;
import com.csd.android.utils.ToastUtils;
import com.csd.android.utils.UIUtils;
import com.csd.android.widget.CCWaitingDialog;
import com.csd.android.widget.InstallTaskTypePop;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class TaskListActivity extends Activity implements OnClickListener, OnRefreshListener2 {
	private static String TAG = "TaskInstallActivity";
	public static final String INTENT_EXTRA_LIST_TYPE = "intent_extra_list_type";

	public TextView tv_task_category;
	public TextView tv_task_sort;
	private PullToRefreshListView lv;
	private CCHttpEngine engine;

	public InstallTaskListAdapter adapter;
	private InstallTaskTypePop pop_type;
	private InstallTaskTypePop pop_status;
	private int pageSize = 10;
	public int list_type = 2;
	public int list_sort = 0;
	//1 明天派单 2全部待安装 3已完成 4 未完成 5未成功;
	public static final int TYPE_TODAY = 0;
	public static final int TYPE_TOMORROW = 1;
	public static final int TYPE_ALL = 2;
	public static final int TYPE_FINISH = 3;
	public static final int TYPE_UNFINISH = 4;
	public static final int TYPE_UNSUCCESS = 5;
	public static final int TYPE_SORT_NORMAL = 0;
	public static final int TYPE_SORT_ABNORMAL = 1;

	public ArrayList<TaskListEntity> list = new ArrayList<TaskListEntity>();
	public ArrayList<TaskListEntity> list_all_sort_normal = new ArrayList<TaskListEntity>();
	public ArrayList<TaskListEntity> list_tomorrow_sort_normal = new ArrayList<TaskListEntity>();
	public ArrayList<TaskListEntity> list_today_sort_normal = new ArrayList<TaskListEntity>();
	public ArrayList<TaskListEntity> list_finish_sort_normal = new ArrayList<TaskListEntity>();
	public ArrayList<TaskListEntity> list_unfinish_sort_normal = new ArrayList<TaskListEntity>();
	public ArrayList<TaskListEntity> list_unsuccess_sort_normal = new ArrayList<TaskListEntity>();
	public ArrayList<TaskListEntity> list_all_sort_abnormal = new ArrayList<TaskListEntity>();
	public ArrayList<TaskListEntity> list_tomorrow_sort_abnormal = new ArrayList<TaskListEntity>();
	public ArrayList<TaskListEntity> list_today_sort_abnormal = new ArrayList<TaskListEntity>();
	public ArrayList<TaskListEntity> list_finish_sort_abnormal = new ArrayList<TaskListEntity>();
	public ArrayList<TaskListEntity> list_unfinish_sort_abnormal = new ArrayList<TaskListEntity>();
	public ArrayList<TaskListEntity> list_unsuccess_sort_abnormal = new ArrayList<TaskListEntity>();
	private int page_num_list_all_sort_normal = 1;
	private int page_num_list_tomorrow_sort_normal = 1;
	private int page_num_list_today_sort_normal = 1;
	private int page_num_list_finish_sort_normal = 1;
	private int page_num_list_unfinish_sort_normal = 1;
	private int page_num_list_unsuccess_sort_normal = 1;
	private int page_num_list_all_sort_abnormal = 1;
	private int page_num_list_tomorrow_sort_abnormal = 1;
	private int page_num_list_today_sort_abnormal = 1;
	private int page_num_list_finish_sort_abnormal = 1;
	private int page_num_list_unfinish_sort_abnormal = 1;
	private int page_num_list_unsuccess_sort_abnormal = 1;

	public TaskListWrapper list_wrapper;

	private EditText et_search_txt;
	private TextView tv_unread_count;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_install);

		initView();

		initData();
	}

	private void initData() {
		if (getIntent().hasExtra(INTENT_EXTRA_LIST_TYPE)) {
			list_type = getIntent().getIntExtra(INTENT_EXTRA_LIST_TYPE, TYPE_ALL);
		}
		TAG = "TaskInstallActivity";
		onPullDownToRefresh(lv);
	}

	private void initView() {
		tv_unread_count = (TextView) findViewById(R.id.tv_unread_count);

		findViewById(R.id.vg_task_category).setOnClickListener(this);
		findViewById(R.id.vg_task_sort).setOnClickListener(this);
		findViewById(R.id.tv_cancel).setOnClickListener(this);
		findViewById(R.id.iv_delete).setOnClickListener(this);

		tv_task_category = (TextView) findViewById(R.id.tv_task_category);
		tv_task_sort = (TextView) findViewById(R.id.tv_task_sort);

		et_search_txt = (EditText) findViewById(R.id.et_search_txt);
		et_search_txt.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void afterTextChanged(Editable s) {
				onPullDownToRefresh(lv);
			}
		});

		findViewById(R.id.iv_search).setOnClickListener(this);
		lv = (PullToRefreshListView) findViewById(R.id.lv);
		lv.setEmptyView(LayoutInflater.from(this).inflate(R.layout.layout_list_empty, null));
		lv.setOnRefreshListener(this);
		lv.getRefreshableView()
				.setDividerHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics()));
		adapter = new InstallTaskListAdapter();
		lv.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.vg_task_category:
				if (pop_type == null) {
					pop_type = new InstallTaskTypePop(this, InstallTaskTypePop.POP_TASK_CATEGORY);
				}
				pop_type.show();
				break;

			case R.id.vg_task_sort:
				if (pop_status == null) {
					pop_status = new InstallTaskTypePop(this, InstallTaskTypePop.POP_TASK_SORT);
				}
				pop_status.show();
				break;

			case R.id.iv_search:
				findViewById(R.id.vg_title_normal).setVisibility(View.GONE);
				findViewById(R.id.vg_title_search).setVisibility(View.VISIBLE);
				break;

			case R.id.tv_cancel:
				findViewById(R.id.vg_title_normal).setVisibility(View.VISIBLE);
				findViewById(R.id.vg_title_search).setVisibility(View.GONE);
				if (!UIUtils.isEmpty(et_search_txt.getText().toString())) {
					et_search_txt.setText("");
				}
				break;

			case R.id.iv_delete:
				if (!UIUtils.isEmpty(et_search_txt.getText().toString())) {
					et_search_txt.setText("");
				}
				break;

		}

	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase refreshView) {
		if (list_type == TYPE_ALL) {
			if (list_sort == TYPE_SORT_NORMAL) {
				page_num_list_all_sort_normal = 1;
			}
			else {
				page_num_list_all_sort_abnormal = 1;
			}
		}
		else if (list_type == TYPE_FINISH) {
			if (list_sort == TYPE_SORT_NORMAL) {
				page_num_list_finish_sort_normal = 1;
			}
			else {
				page_num_list_finish_sort_abnormal = 1;
			}
		}
		else if (list_type == TYPE_UNFINISH) {
			if (list_sort == TYPE_SORT_NORMAL) {
				page_num_list_unfinish_sort_normal = 1;
			}
			else {
				page_num_list_unfinish_sort_abnormal = 1;
			}
		}
		else if (list_type == TYPE_UNSUCCESS) {
			if (list_sort == TYPE_SORT_NORMAL) {
				page_num_list_unsuccess_sort_normal = 1;
			}
			else {
				page_num_list_unsuccess_sort_abnormal = 1;
			}
		}
		else if (list_type == TYPE_TOMORROW) {
			if (list_sort == TYPE_SORT_NORMAL) {
				page_num_list_tomorrow_sort_normal = 1;
			}
			else {
				page_num_list_tomorrow_sort_abnormal = 1;
			}
		}
		else if (list_type == TYPE_TODAY) {
			if (list_sort == TYPE_SORT_NORMAL) {
				page_num_list_today_sort_normal = 1;
			}
			else {
				page_num_list_today_sort_abnormal = 1;
			}
		}
		getFixTaskList(list_type, list_sort);

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase refreshView) {
		getFixTaskList(list_type, list_sort);
	}

	public void getFixTaskList(final int type, final int sort) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("assign_status", type + "");
		hashMap.put("sort", sort + "");
		if (UIUtils.isEmpty(et_search_txt.getText().toString().trim())) {
			CCWaitingDialog.show(this);
		}
		else {
			hashMap.put("keyword", et_search_txt.getText().toString().trim());
		}
		if (type == TYPE_TODAY) {
			hashMap.put("page", page_num_list_today_sort_normal + "");
		}
		else if (type == TYPE_TOMORROW) {
			hashMap.put("page", page_num_list_tomorrow_sort_normal + "");
		}
		else if (type == TYPE_ALL) {
			hashMap.put("page", page_num_list_all_sort_normal + "");
		}
		else if (type == TYPE_FINISH) {
			hashMap.put("page", page_num_list_finish_sort_normal + "");
		}
		else if (type == TYPE_UNFINISH) {
			hashMap.put("page", page_num_list_unfinish_sort_normal + "");
		}
		else if (type == TYPE_UNSUCCESS) {
			hashMap.put("page", page_num_list_unsuccess_sort_normal + "");
		}
		hashMap.put("pagesize", pageSize + "");
		engine = new CCHttpEngine(this, NetConstants.NET_ID_GET_INSTALL_TASK_LIST, hashMap, TAG, new HttpCallBack() {
			@Override
			public void onSuccess(ResponseBean responseBean) {
				lv.onRefreshComplete();
				CCWaitingDialog.dismiss(TaskListActivity.this);

				if (responseBean.getCode() == 0) {
					list_type = type;
					list_sort = sort;

					list_wrapper = (TaskListWrapper) responseBean.getData();
					ArrayList<TaskListEntity> temp_list = list_wrapper.getList();
					if (!list_wrapper.isHasMore()) {
						lv.setMode(Mode.PULL_FROM_START);
					}
					else {
						lv.setMode(Mode.BOTH);
					}

					if (sort == TYPE_SORT_NORMAL) {
						tv_task_sort.setText("时间降序");
					}
					else {
						tv_task_sort.setText("时间升序");
					}

					if (type == TYPE_ALL) {
						tv_task_category.setText(getResources().getString(R.string.type_all, list_wrapper.getTaskCategoryCount().getCategory_all()));
						if (sort == TYPE_SORT_NORMAL) {
							if (page_num_list_all_sort_normal == 1) {
								list_all_sort_normal.clear();
							}
							page_num_list_all_sort_normal++;
							list_all_sort_normal.addAll(temp_list);
							list = list_all_sort_normal;
						}
						else {
							if (page_num_list_all_sort_abnormal == 1) {
								list_all_sort_abnormal.clear();
							}
							page_num_list_all_sort_abnormal++;
							list_all_sort_abnormal.addAll(temp_list);
							list = list_all_sort_abnormal;
						}

					}
					else if (type == TYPE_TOMORROW) {
						tv_task_category.setText(getResources().getString(R.string.type_tomorrow,
								list_wrapper.getTaskCategoryCount().getCategory_tomorrow()));
						if (sort == TYPE_SORT_NORMAL) {
							if (page_num_list_tomorrow_sort_normal == 1) {
								list_tomorrow_sort_normal.clear();
							}
							page_num_list_tomorrow_sort_normal++;
							list_tomorrow_sort_normal.addAll(temp_list);
							list = list_tomorrow_sort_normal;
						}
						else {
							if (page_num_list_tomorrow_sort_abnormal == 1) {
								list_tomorrow_sort_abnormal.clear();
							}
							page_num_list_tomorrow_sort_abnormal++;
							list_tomorrow_sort_abnormal.addAll(temp_list);
							list = list_tomorrow_sort_abnormal;
						}

					}
					else if (type == TYPE_TODAY) {
						tv_task_category.setText(getResources().getString(R.string.type_today,
								list_wrapper.getTaskCategoryCount().getCategory_today()));
						if (sort == TYPE_SORT_NORMAL) {
							if (page_num_list_today_sort_normal == 1) {
								list_today_sort_normal.clear();
							}
							page_num_list_today_sort_normal++;
							list_today_sort_normal.addAll(temp_list);
							list = list_today_sort_normal;
						}
						else {
							if (page_num_list_today_sort_abnormal == 1) {
								list_today_sort_abnormal.clear();
							}
							page_num_list_today_sort_abnormal++;
							list_today_sort_abnormal.addAll(temp_list);
							list = list_today_sort_abnormal;
						}

					}
					else if (type == TYPE_FINISH) {
						tv_task_category.setText(getResources().getString(R.string.type_finish,
								list_wrapper.getTaskCategoryCount().getCategory_finish()));
						if (sort == TYPE_SORT_NORMAL) {
							if (page_num_list_finish_sort_normal == 1) {
								list_finish_sort_normal.clear();
							}
							page_num_list_finish_sort_normal++;
							list_finish_sort_normal.addAll(temp_list);
							list = list_finish_sort_normal;
						}
						else {
							if (page_num_list_finish_sort_abnormal == 1) {
								list_finish_sort_normal.clear();
							}
							page_num_list_finish_sort_abnormal++;
							list_finish_sort_abnormal.addAll(temp_list);
							list = list_finish_sort_abnormal;
						}

					}
					else if (type == TYPE_UNFINISH) {
						tv_task_category.setText(getResources().getString(R.string.type_unfinish,
								list_wrapper.getTaskCategoryCount().getCategory_unfinish()));
						if (sort == TYPE_SORT_NORMAL) {
							if (page_num_list_unfinish_sort_normal == 1) {
								list_unfinish_sort_normal.clear();
							}
							page_num_list_unfinish_sort_normal++;
							list_unfinish_sort_normal.addAll(temp_list);
							list = list_unfinish_sort_normal;
						}
						else {
							if (page_num_list_unfinish_sort_abnormal == 1) {
								list_unfinish_sort_abnormal.clear();
							}
							page_num_list_unfinish_sort_abnormal++;
							list_unfinish_sort_abnormal.addAll(temp_list);
							list = list_unfinish_sort_abnormal;
						}

					}
					else if (type == TYPE_UNSUCCESS) {
						tv_task_category.setText(getResources().getString(R.string.type_unsuccess,
								list_wrapper.getTaskCategoryCount().getCategory_unsuccess()));
						if (sort == TYPE_SORT_NORMAL) {
							if (page_num_list_unsuccess_sort_normal == 1) {
								list_unsuccess_sort_normal.clear();
							}
							page_num_list_unsuccess_sort_normal++;
							list_unsuccess_sort_normal.addAll(temp_list);
							list = list_unsuccess_sort_normal;
						}
						else {
							if (page_num_list_unsuccess_sort_abnormal == 1) {
								list_unsuccess_sort_abnormal.clear();
							}
							page_num_list_unsuccess_sort_abnormal++;
							list_unsuccess_sort_abnormal.addAll(temp_list);
							list = list_unsuccess_sort_abnormal;
						}

					}
					notifyDataSetChanged();
				}
				else {
					ToastUtils.showToast(responseBean.getMessage());
					finish();
				}

			}

			@Override
			public void onNetUnavailable(String net_unAvailabel) {
				CCWaitingDialog.dismiss(TaskListActivity.this);
				lv.onRefreshComplete();
				ToastUtils.showToast(net_unAvailabel);
				finish();
			}

			@Override
			public void onFailure(IOException e) {
				lv.onRefreshComplete();
				CCWaitingDialog.dismiss(TaskListActivity.this);
				ToastUtils.showToast("error:" + e.getMessage());
				finish();
			}
		}).executeTask();

	}

	public class InstallTaskListAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(TaskListActivity.this).inflate(R.layout.adapter_install_task, null);
				holder.iv_call_phone = (ImageView) convertView.findViewById(R.id.iv_call_phone);
				holder.tv_phone_num = (TextView) convertView.findViewById(R.id.tv_phone_num);
				holder.tv_nick_name = (TextView) convertView.findViewById(R.id.tv_nick_name);
				holder.tv_car_install_time = (TextView) convertView.findViewById(R.id.tv_car_install_time);
				holder.tv_car_brand_task_status = (TextView) convertView.findViewById(R.id.tv_car_brand_task_status);
				holder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
				holder.tv_task_type_engineer_name = (TextView) convertView.findViewById(R.id.tv_task_type_engineer_name);
				holder.tv_use_car_time = (TextView) convertView.findViewById(R.id.tv_use_car_time);
				holder.tv_cards_status = (TextView) convertView.findViewById(R.id.tv_cards_status);
				holder.tv_car_info_status = (TextView) convertView.findViewById(R.id.tv_car_info_status);
				holder.tv_box_bind_status = (TextView) convertView.findViewById(R.id.tv_box_bind_status);
				holder.vg_task_emergency_status = (ViewGroup) convertView.findViewById(R.id.vg_task_emergency_status);
				holder.vg_root = (ViewGroup) convertView.findViewById(R.id.vg_root);
				holder.btn_submit = (Button) convertView.findViewById(R.id.btn_submit);
				holder.view_read_tip = (View) convertView.findViewById(R.id.view_read);

				convertView.setTag(holder);
			}
			else {
				holder = (ViewHolder) convertView.getTag();
			}
			final TaskListEntity task = list.get(position);
			holder.iv_call_phone.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + task.getPhone()));
					startActivity(intent);
				}
			});

			holder.vg_root.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					onItemClick(position);
					if (task.getIsRead() == 0) {
						handleRedCircleTip(task);
					}
				}
			});

			holder.btn_submit.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					submitTask(task);
				}
			});
			holder.tv_phone_num.setText(task.getPhone());
			holder.tv_nick_name.setText(task.getNickName());
			holder.tv_address.setText("地址：" + task.getAddress());
//			holder.tv_task_type.setText("任务类型：" + task.get);
			String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(Long.parseLong(task.getAssignDate()) * 1000));
			holder.tv_car_install_time.setText("安装时间：" + time);
			task.setTaskBrand_TaskStatus(holder.tv_car_brand_task_status);
			task.setTaskType_Adm(holder.tv_task_type_engineer_name);
			task.setTaskProcessStatus(holder.btn_submit, holder.tv_use_car_time, holder.vg_task_emergency_status, holder.tv_cards_status,
					holder.tv_car_info_status, holder.tv_box_bind_status);
			if (task.getIsRead() == 0) {
				holder.view_read_tip.setVisibility(View.VISIBLE);
			}
			else {
				holder.view_read_tip.setVisibility(View.GONE);
			}

			return convertView;
		}

	}

	protected void handleRedCircleTip(final TaskListEntity task) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("id", task.getId() + "");
		new CCHttpEngine(this, NetConstants.NET_ID_TASK_HASREAD, hashMap, null, new HttpCallBack() {
			@Override
			public void onSuccess(ResponseBean responseBean) {
				if (responseBean.getCode() == 0) {
					task.setIsRead(1);
					notifyDataSetChanged();
				}
			}

			@Override
			public void onNetUnavailable(String net_unAvailabel) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFailure(IOException e) {
				// TODO Auto-generated method stub

			}
		}).executeTask();

	}

	static class ViewHolder {
		public ImageView iv_call_phone;
		public TextView tv_phone_num;
		public TextView tv_nick_name;
		public TextView tv_car_install_time;
		public TextView tv_car_brand_task_status;
		public TextView tv_address;
		public TextView tv_task_type_engineer_name;
		public TextView tv_use_car_time;
		public TextView tv_cards_status;
		public TextView tv_car_info_status;
		public TextView tv_box_bind_status;
		public ViewGroup vg_task_emergency_status;
		public ViewGroup vg_root;
		public Button btn_submit;
		public View view_read_tip;
	}

	private void submitTask(final TaskListEntity task) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("id", task.getId() + "");
		TAG = "submit";
		CCWaitingDialog.show(this);
		new CCHttpEngine(this, NetConstants.NET_ID_TASK_SUBMIT, hashMap, TAG, new HttpCallBack() {
			@Override
			public void onSuccess(ResponseBean responseBean) {
				CCWaitingDialog.dismiss(TaskListActivity.this);
				if (responseBean.getCode() == 0) {
					ToastUtils.showToast("提交成功");
					removeTask(task);
				}
				else {
					ToastUtils.showToast(responseBean.getMessage());
				}
			}

			@Override
			public void onNetUnavailable(String net_unAvailabel) {
				ToastUtils.showToast(R.string.net_unavailable);
				CCWaitingDialog.dismiss(TaskListActivity.this);
			}

			@Override
			public void onFailure(IOException e) {
				ToastUtils.showToast(R.string.net_fail);
				CCWaitingDialog.dismiss(TaskListActivity.this);
			}
		}).executeTask();

	}

	protected void removeTask(TaskListEntity task) {
		list.remove(task);
		notifyDataSetChanged();
	}

	public void notifyDataSetChanged() {
		adapter.notifyDataSetChanged();
		int count = 0;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getIsRead() == 0) {
				count++;
			}
		}
		if (count == 0) {
			tv_unread_count.setVisibility(View.GONE);
		}
		else {
			tv_unread_count.setVisibility(View.VISIBLE);
			tv_unread_count.setText(count + "");
		}
	}

	public void onItemClick(int position) {
		TaskListEntity task = list.get(position);

		if (!task.isClickEnable()) {
			return;
		}

		Intent intent = new Intent(this, TaskDetailActivity.class);
		intent.putExtra(TaskDetailActivity.INTENT_EXTRA_NAME_CAR_ID, task.getCarId());
		intent.putExtra(TaskDetailActivity.INTENT_EXTRA_NAME_TASK_TYPE, task.getTaskType());
		intent.putExtra(TaskDetailActivity.INTENT_EXTRA_NAME_ASSIGN_ID, task.getAssignId());
		startActivity(intent);
	}

	@Override
	public void onBackPressed() {
		if (CCWaitingDialog.isShowing(this)) {
			CCWaitingDialog.dismiss(this);
			engine.cancel(TAG);
		}
		super.onBackPressed();
	}

}
