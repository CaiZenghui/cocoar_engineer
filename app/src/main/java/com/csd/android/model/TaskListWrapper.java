package com.csd.android.model;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class TaskListWrapper implements Serializable {
	private int count;
	private boolean hasMore;
	private int page;
	private ArrayList<TaskListEntity> list;

	@SerializedName("collectCount")
	private TaskCategoryCount taskCategoryCount;

	public TaskCategoryCount getTaskCategoryCount() {
		return taskCategoryCount;
	}

	public void setTaskCategoryCount(TaskCategoryCount taskCategoryCount) {
		this.taskCategoryCount = taskCategoryCount;
	}

	public class TaskCategoryCount {
		@SerializedName("tomorrow")
		private int category_tomorrow;
		@SerializedName("today")
		private int category_today;
		@SerializedName("need")
		private int category_all;
		@SerializedName("success")
		private int category_finish;
		@SerializedName("uncomplete")
		private int category_unfinish;
		@SerializedName("unsuccess")
		private int category_unsuccess;

		public int getCategory_tomorrow() {
			return category_tomorrow;
		}

		public void setCategory_tomorrow(int category_tomorrow) {
			this.category_tomorrow = category_tomorrow;
		}

		public int getCategory_today() {
			return category_today;
		}

		public void setCategory_today(int category_today) {
			this.category_today = category_today;
		}

		public int getCategory_all() {
			return category_all;
		}

		public void setCategory_all(int category_all) {
			this.category_all = category_all;
		}

		public int getCategory_finish() {
			return category_finish;
		}

		public void setCategory_finish(int category_finish) {
			this.category_finish = category_finish;
		}

		public int getCategory_unfinish() {
			return category_unfinish;
		}

		public void setCategory_unfinish(int category_unfinish) {
			this.category_unfinish = category_unfinish;
		}

		public int getCategory_unsuccess() {
			return category_unsuccess;
		}

		public void setCategory_unsuccess(int category_unsuccess) {
			this.category_unsuccess = category_unsuccess;
		}

	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public boolean isHasMore() {
		return hasMore;
	}

	public void setHasMore(boolean hasMore) {
		this.hasMore = hasMore;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public ArrayList<TaskListEntity> getList() {
		return list;
	}

	public void setList(ArrayList<TaskListEntity> list) {
		this.list = list;
	}

}
