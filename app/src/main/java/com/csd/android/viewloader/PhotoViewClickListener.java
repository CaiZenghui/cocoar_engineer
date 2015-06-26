package com.csd.android.viewloader;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.csd.android.R;
import com.csd.android.activity.TaskDetailActivity;
import com.csd.android.io.CacheHelper;
import com.csd.android.utils.UIUtils;
import com.csd.android.widget.ItemPop;
import com.csd.android.widget.ItemPop.OnClickItemListener;

import java.io.File;

public class PhotoViewClickListener implements OnClickListener {
	private TaskDetailActivity context;
	private Uri uri;
	private String[] strs = { "拍照", "从相册中选择" };
	private ItemPop pop;

	public static String photoPath;
	public static int CAR_PHOTO_VIEW_ID;

	public PhotoViewClickListener(TaskDetailActivity context, Uri model) {
		this.context = context;
		this.uri = model;
	}

	@Override
	public void onClick(final View v) {
		if ("fail".equals((String) v.getTag(R.id.tag_glide))) {
			Glide.with(context).load(uri).placeholder(R.mipmap.icon_pic_loading).error(R.mipmap.icon_pic_load_fail)
					.listener(new ImageLoaderListener(context, uri)).into((ImageView) v);
		}
		else if ("success".equals((String) v.getTag(R.id.tag_glide))) {
			CAR_PHOTO_VIEW_ID = v.getId();
			if (pop == null) {
				pop = new ItemPop(context, strs, true);
				pop.setOnClickItemListener(new OnClickItemListener() {
					public void onClickItem(int index) {
						if (index == 99) {//取消；
							return;
						}
						else if (index == 0) {
							Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
							photoPath = CacheHelper.getPhotoPath();
							intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(photoPath)));
							context.startActivityForResult(intent, TaskDetailActivity.REQUEST_CODE_TAKE_PHOTO);
						}
						else if (index == 1) {
							Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
							intent.setType("image/*");
							intent.putExtra("crop", "true");
//							intent.putExtra("aspectX", 3);
//							intent.putExtra("aspectY", 2);
							intent.putExtra("outputX", context.getWindow().getDecorView().getWidth() / 2);
							intent.putExtra("outputY", UIUtils.dp2px(120));
							intent.putExtra("noFaceDetection", true);
							photoPath = CacheHelper.getPhotoPath();
							intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(photoPath)));
							context.startActivityForResult(intent, TaskDetailActivity.REQUEST_CODE_PICK_PHOTO);
						}
					}
				});
			}
			pop.show();
		}
	}
}
