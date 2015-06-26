package com.csd.android.viewloader;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnLongClickListener;

import com.csd.android.activity.TaskDetailActivity;
import com.csd.android.io.CacheHelper;
import com.csd.android.utils.UIUtils;
import com.csd.android.widget.ItemPop;
import com.csd.android.widget.ItemPop.OnClickItemListener;

public class PhotoViewLongClickListener implements OnLongClickListener {
	private TaskDetailActivity context;
	private String[] strs = { "拍照", "从相册中选择" };
	private ItemPop pop;

	public PhotoViewLongClickListener(TaskDetailActivity context) {
		this.context = context;
	}

	@Override
	public boolean onLongClick(View v) {

		PhotoViewClickListener.CAR_PHOTO_VIEW_ID = v.getId();
		if (pop == null) {
			pop = new ItemPop(context, strs, true);
			pop.setOnClickItemListener(new OnClickItemListener() {
				public void onClickItem(int index) {
					if (index == 99) {//取消；
						return;
					}
					else if (index == 0) {
						Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
						PhotoViewClickListener.photoPath = CacheHelper.getPhotoPath();
						intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(PhotoViewClickListener.photoPath)));
						context.startActivityForResult(intent, TaskDetailActivity.REQUEST_CODE_TAKE_PHOTO);
					}
					else if (index == 1) {
						Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
						intent.setType("image/*");
						intent.putExtra("crop", "true");
//						intent.putExtra("aspectX", 3);
//						intent.putExtra("aspectY", 2);
						intent.putExtra("outputX", context.getWindow().getDecorView().getWidth() / 2);
						intent.putExtra("outputY", UIUtils.dp2px(120));
						intent.putExtra("noFaceDetection", true);
						PhotoViewClickListener.photoPath = CacheHelper.getPhotoPath();
						intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(PhotoViewClickListener.photoPath)));
						context.startActivityForResult(intent, TaskDetailActivity.REQUEST_CODE_PICK_PHOTO);
					}
				}
			});
		}
		pop.show();

		return true;
	}
}
