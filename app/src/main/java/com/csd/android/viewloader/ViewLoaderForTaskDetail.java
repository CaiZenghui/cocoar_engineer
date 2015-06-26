package com.csd.android.viewloader;

import android.content.Intent;

import com.csd.android.activity.TaskDetailActivity;

public class ViewLoaderForTaskDetail {
	private TaskDetailActivity context;

	private CheLiangShangJiaViewLoader cheLiangShangJiaViewLoader;

	private CardsCheckViewLoader cardsCheckViewLoader;

	public BoxBindViewLoader boxBindViewLoader;

	public ViewLoaderForTaskDetail(TaskDetailActivity taskDetailActivity) {
		this.context = taskDetailActivity;
	}

	public void loadCheLiangShangJiaView() {
		if (cheLiangShangJiaViewLoader == null) {
			cheLiangShangJiaViewLoader = new CheLiangShangJiaViewLoader(context);
		}
		cheLiangShangJiaViewLoader.load();
	}

	public void loadCardsCheckView() {
		if (cardsCheckViewLoader == null) {
			cardsCheckViewLoader = new CardsCheckViewLoader(context);
		}
		cardsCheckViewLoader.load();
	}

	public void onActivityResult(int currentPage, int requestCode, int resultCode, Intent data) {
		if (currentPage == 1) {
			cardsCheckViewLoader.onActivityResult(requestCode, resultCode, data);
		}
		else if (currentPage == 2) {
			cheLiangShangJiaViewLoader.onActivityResult(requestCode, resultCode, data);
		}else if(currentPage == 3){
			boxBindViewLoader.onActivityResult(requestCode, resultCode, data);
		}
	}

	public void loadBoxBindView() {
		if (boxBindViewLoader == null) {
			boxBindViewLoader = new BoxBindViewLoader(context);
		}
		boxBindViewLoader.load();
	}

}
