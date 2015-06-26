package com.csd.android.widget;

import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.csd.android.R;
import com.csd.android.activity.TaskListActivity;
import com.csd.android.model.TaskListWrapper.TaskCategoryCount;
import com.csd.android.utils.UIUtils;

public class InstallTaskTypePop extends PopupWindow implements OnClickListener, OnDismissListener {

	private TaskListActivity context;

	private String[] category;
	private String[] sort = { "时间降序", "时间升序" };

	public static int POP_TASK_CATEGORY = 0;
	public static int POP_TASK_SORT = 1;

	private int pop_id;

	public InstallTaskTypePop(TaskListActivity context, int whichPop) {
		this.context = context;
		pop_id = whichPop;

		TaskCategoryCount entity = context.list_wrapper.getTaskCategoryCount();
		category = new String[] { context.getResources().getString(R.string.type_today, entity.getCategory_today()),
				context.getResources().getString(R.string.type_tomorrow, entity.getCategory_tomorrow()),
				context.getResources().getString(R.string.type_all, entity.getCategory_all()),
				context.getResources().getString(R.string.type_finish, entity.getCategory_finish()),
				context.getResources().getString(R.string.type_unfinish, entity.getCategory_unfinish()),
				context.getResources().getString(R.string.type_unsuccess, entity.getCategory_unsuccess()) };

		View contentView = initView();
		setContentView(contentView);
//		setAnimationStyle(R.style.PopupAnimation);
		setOutsideTouchable(true);
		setFocusable(true);
		setBackgroundDrawable(new BitmapDrawable());
		setWidth(context.getWindow().getDecorView().getWidth() / 2);
		setHeight(LayoutParams.WRAP_CONTENT);
		setOnDismissListener(this);
	}

	private View initView() {
		LinearLayout vg = new LinearLayout(context);
		vg.setOrientation(LinearLayout.VERTICAL);
		String[] strs;
		if (pop_id == POP_TASK_CATEGORY) {
			strs = category;
		}
		else {
			strs = sort;
		}

		for (int i = 0; i < strs.length; i++) {
			TextView tv = new TextView(context);
			tv.setId(i);
			tv.setOnClickListener(this);
			tv.setText(strs[i]);
			tv.setGravity(Gravity.CENTER);
			tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
			tv.setTextColor(context.getResources().getColor(R.color.cor_text_black));
			tv.setPadding(0, UIUtils.dp2px(10), 0, UIUtils.dp2px(10));
			if (pop_id == POP_TASK_CATEGORY) {
				if (i == context.list_type) {
					tv.setTag(1);
				}
				else {
					tv.setTag(0);
				}
			}
			else {
				if (i == context.list_sort) {
					tv.setTag(1);
				}
				else {
					tv.setTag(0);
				}
			}
			LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			vg.addView(tv, params1);
			if (i != strs.length - 1) {
				View divider = new View(context);
				divider.setTag(0);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				params.height = UIUtils.dp2px(1);
				divider.setBackgroundResource(R.color.cor_list_divider);
				vg.addView(divider, params);
			}
		}
		return vg;
	}

	public void show() {
		initSelectedItem();
		if (pop_id == POP_TASK_CATEGORY) {
			showAsDropDown(context.findViewById(R.id.vg_title), 0, 0);
		}
		else {
			showAsDropDown(context.findViewById(R.id.vg_title), context.getWindow().getDecorView().getWidth() / 2, 0);
		}
		WindowManager.LayoutParams params = context.getWindow().getAttributes();
		params.alpha = 0.5f;
		context.getWindow().setAttributes(params);
	}

	@Override
	public void onClick(View v) {
		int last_selected = (Integer) v.getTag();
		if (last_selected == 1) {
			dismiss();
			return;
		}

		ViewGroup parent = (ViewGroup) v.getParent();
		for (int i = 0; i < parent.getChildCount(); i++) {
			parent.getChildAt(i).setTag(0);
		}
		v.setTag(1);
		switch (v.getId()) {
			case 0:
				if (pop_id == POP_TASK_CATEGORY) {
					if (context.list_sort == TaskListActivity.TYPE_SORT_NORMAL && context.list_today_sort_normal.size() != 0) {
						context.list = context.list_today_sort_normal;
						context.notifyDataSetChanged();
						context.list_type = TaskListActivity.TYPE_TODAY;
						context.tv_task_category.setText(context.getResources().getString(R.string.type_today,
								context.list_wrapper.getTaskCategoryCount().getCategory_today()));
					}
					else if (context.list_sort == TaskListActivity.TYPE_SORT_ABNORMAL && context.list_today_sort_abnormal.size() != 0) {
						context.list = context.list_today_sort_abnormal;
						context.notifyDataSetChanged();
						context.list_type = TaskListActivity.TYPE_TODAY;
						context.tv_task_category.setText(context.getResources().getString(R.string.type_today,
								context.list_wrapper.getTaskCategoryCount().getCategory_today()));
					}
					else {
						context.getFixTaskList(TaskListActivity.TYPE_TODAY, context.list_sort);
					}
				}
				else {
					if (context.list_type == TaskListActivity.TYPE_TODAY && context.list_today_sort_normal.size() != 0) {
						context.list = context.list_today_sort_normal;
						context.list_sort = TaskListActivity.TYPE_SORT_NORMAL;
						context.notifyDataSetChanged();
					}
					else if (context.list_type == TaskListActivity.TYPE_TOMORROW && context.list_tomorrow_sort_normal.size() != 0) {
						context.list = context.list_tomorrow_sort_normal;
						context.list_sort = TaskListActivity.TYPE_SORT_NORMAL;
						context.notifyDataSetChanged();
					}
					else if (context.list_type == TaskListActivity.TYPE_ALL && context.list_all_sort_normal.size() != 0) {
						context.list = context.list_all_sort_normal;
						context.list_sort = TaskListActivity.TYPE_SORT_NORMAL;
						context.notifyDataSetChanged();
					}
					else if (context.list_type == TaskListActivity.TYPE_FINISH && context.list_finish_sort_normal.size() != 0) {
						context.list = context.list_finish_sort_normal;
						context.list_sort = TaskListActivity.TYPE_SORT_NORMAL;
						context.notifyDataSetChanged();
					}
					else if (context.list_type == TaskListActivity.TYPE_UNFINISH && context.list_unfinish_sort_normal.size() != 0) {
						context.list = context.list_unfinish_sort_normal;
						context.list_sort = TaskListActivity.TYPE_SORT_NORMAL;
						context.notifyDataSetChanged();
					}
					else if (context.list_type == TaskListActivity.TYPE_UNSUCCESS && context.list_unsuccess_sort_normal.size() != 0) {
						context.list = context.list_unsuccess_sort_normal;
						context.list_sort = TaskListActivity.TYPE_SORT_NORMAL;
						context.notifyDataSetChanged();
					}
					else {
						context.getFixTaskList(context.list_type, TaskListActivity.TYPE_SORT_NORMAL);
					}
				}
				break;
			case 1:
				if (pop_id == POP_TASK_CATEGORY) {
					if (context.list_sort == TaskListActivity.TYPE_SORT_NORMAL && context.list_tomorrow_sort_normal.size() != 0) {
						context.list = context.list_tomorrow_sort_normal;
						context.notifyDataSetChanged();
						context.list_type = TaskListActivity.TYPE_TOMORROW;
						context.tv_task_category.setText(context.getResources().getString(R.string.type_tomorrow,
								context.list_wrapper.getTaskCategoryCount().getCategory_tomorrow()));
					}
					else if (context.list_sort == TaskListActivity.TYPE_SORT_ABNORMAL && context.list_tomorrow_sort_abnormal.size() != 0) {
						context.list = context.list_tomorrow_sort_abnormal;
						context.notifyDataSetChanged();
						context.list_type = TaskListActivity.TYPE_TOMORROW;
						context.tv_task_category.setText(context.getResources().getString(R.string.type_tomorrow,
								context.list_wrapper.getTaskCategoryCount().getCategory_tomorrow()));
					}
					else {
						context.getFixTaskList(TaskListActivity.TYPE_TOMORROW, context.list_sort);
					}
				}
				else {
					if (context.list_type == TaskListActivity.TYPE_TODAY && context.list_today_sort_abnormal.size() != 0) {
						context.list = context.list_today_sort_abnormal;
						context.list_sort = TaskListActivity.TYPE_SORT_ABNORMAL;
						context.notifyDataSetChanged();
					}
					else if (context.list_type == TaskListActivity.TYPE_TOMORROW && context.list_tomorrow_sort_abnormal.size() != 0) {
						context.list = context.list_tomorrow_sort_abnormal;
						context.list_sort = TaskListActivity.TYPE_SORT_ABNORMAL;
						context.notifyDataSetChanged();
					}
					else if (context.list_type == TaskListActivity.TYPE_ALL && context.list_all_sort_abnormal.size() != 0) {
						context.list = context.list_all_sort_abnormal;
						context.list_sort = TaskListActivity.TYPE_SORT_ABNORMAL;
						context.notifyDataSetChanged();
					}
					else if (context.list_type == TaskListActivity.TYPE_FINISH && context.list_finish_sort_abnormal.size() != 0) {
						context.list = context.list_finish_sort_abnormal;
						context.list_sort = TaskListActivity.TYPE_SORT_ABNORMAL;
						context.notifyDataSetChanged();
					}
					else if (context.list_type == TaskListActivity.TYPE_UNFINISH && context.list_unfinish_sort_abnormal.size() != 0) {
						context.list = context.list_unfinish_sort_abnormal;
						context.list_sort = TaskListActivity.TYPE_SORT_ABNORMAL;
						context.notifyDataSetChanged();
					}
					else if (context.list_type == TaskListActivity.TYPE_UNSUCCESS && context.list_unsuccess_sort_abnormal.size() != 0) {
						context.list = context.list_unsuccess_sort_abnormal;
						context.list_sort = TaskListActivity.TYPE_SORT_ABNORMAL;
						context.notifyDataSetChanged();
					}
					else {
						context.getFixTaskList(context.list_type, TaskListActivity.TYPE_SORT_ABNORMAL);
					}
				}
				break;
			case 2:
				if (context.list_sort == TaskListActivity.TYPE_SORT_NORMAL && context.list_all_sort_normal.size() != 0) {
					context.list = context.list_all_sort_normal;
					context.notifyDataSetChanged();
					context.list_type = TaskListActivity.TYPE_ALL;
					context.tv_task_category.setText(context.getResources().getString(R.string.type_all,
							context.list_wrapper.getTaskCategoryCount().getCategory_all()));
				}
				else if (context.list_sort == TaskListActivity.TYPE_SORT_ABNORMAL && context.list_all_sort_abnormal.size() != 0) {
					context.list = context.list_all_sort_abnormal;
					context.notifyDataSetChanged();
					context.list_type = TaskListActivity.TYPE_ALL;
					context.tv_task_category.setText(context.getResources().getString(R.string.type_all,
							context.list_wrapper.getTaskCategoryCount().getCategory_all()));
				}
				else {
					context.getFixTaskList(TaskListActivity.TYPE_ALL, context.list_sort);
				}
				break;
			case 3:
				if (context.list_sort == TaskListActivity.TYPE_SORT_NORMAL && context.list_finish_sort_normal.size() != 0) {
					context.list = context.list_finish_sort_normal;
					context.notifyDataSetChanged();
					context.list_type = TaskListActivity.TYPE_FINISH;
					context.tv_task_category.setText(context.getResources().getString(R.string.type_finish,
							context.list_wrapper.getTaskCategoryCount().getCategory_finish()));
				}
				else if (context.list_sort == TaskListActivity.TYPE_SORT_ABNORMAL && context.list_finish_sort_abnormal.size() != 0) {
					context.list = context.list_finish_sort_abnormal;
					context.notifyDataSetChanged();
					context.list_type = TaskListActivity.TYPE_FINISH;
					context.tv_task_category.setText(context.getResources().getString(R.string.type_finish,
							context.list_wrapper.getTaskCategoryCount().getCategory_finish()));
				}
				else {
					context.getFixTaskList(TaskListActivity.TYPE_FINISH, context.list_sort);
				}
				break;
			case 4:
				if (context.list_sort == TaskListActivity.TYPE_SORT_NORMAL && context.list_unfinish_sort_normal.size() != 0) {
					context.list = context.list_unfinish_sort_normal;
					context.notifyDataSetChanged();
					context.list_type = TaskListActivity.TYPE_UNFINISH;
					context.tv_task_category.setText(context.getResources().getString(R.string.type_unfinish,
							context.list_wrapper.getTaskCategoryCount().getCategory_unfinish()));
				}
				else if (context.list_sort == TaskListActivity.TYPE_SORT_ABNORMAL && context.list_unfinish_sort_abnormal.size() != 0) {
					context.list = context.list_unfinish_sort_abnormal;
					context.notifyDataSetChanged();
					context.list_type = TaskListActivity.TYPE_UNFINISH;
					context.tv_task_category.setText(context.getResources().getString(R.string.type_unfinish,
							context.list_wrapper.getTaskCategoryCount().getCategory_unfinish()));
				}
				else {
					context.getFixTaskList(TaskListActivity.TYPE_UNFINISH, context.list_sort);
				}
				break;
			case 5:
				if (context.list_sort == TaskListActivity.TYPE_SORT_NORMAL && context.list_unsuccess_sort_normal.size() != 0) {
					context.list = context.list_unsuccess_sort_normal;
					context.notifyDataSetChanged();
					context.list_type = TaskListActivity.TYPE_UNSUCCESS;
					context.tv_task_category.setText(context.getResources().getString(R.string.type_unsuccess,
							context.list_wrapper.getTaskCategoryCount().getCategory_unsuccess()));
				}
				else if (context.list_sort == TaskListActivity.TYPE_SORT_ABNORMAL && context.list_unsuccess_sort_abnormal.size() != 0) {
					context.list = context.list_unsuccess_sort_abnormal;
					context.notifyDataSetChanged();
					context.list_type = TaskListActivity.TYPE_UNSUCCESS;
					context.tv_task_category.setText(context.getResources().getString(R.string.type_unsuccess,
							context.list_wrapper.getTaskCategoryCount().getCategory_unsuccess()));
				}
				else {
					context.getFixTaskList(TaskListActivity.TYPE_UNSUCCESS, context.list_sort);
				}
				break;

		}
		dismiss();
	}

	private void initSelectedItem() {
		LinearLayout vg = (LinearLayout) getContentView();
		for (int i = 0; i < vg.getChildCount(); i++) {
			View child = vg.getChildAt(i);
			if ((Integer) child.getTag() == 1) {
				child.setBackgroundResource(R.color.cor_dark_gray);
			}
			else if (child instanceof TextView) {
				child.setBackgroundResource(R.color.white);
			}
		}
	}

	@Override
	public void onDismiss() {
		WindowManager.LayoutParams params = context.getWindow().getAttributes();
		params.alpha = 1.0f;
		context.getWindow().setAttributes(params);
	}

}
