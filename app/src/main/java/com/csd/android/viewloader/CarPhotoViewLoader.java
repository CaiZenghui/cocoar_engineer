package com.csd.android.viewloader;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;
import com.csd.android.R;
import com.csd.android.activity.TaskDetailActivity;
import com.csd.android.model.CarImages;
import com.csd.android.net.CCHttpEngine;
import com.csd.android.net.HttpCallBack;
import com.csd.android.net.NetConstants;
import com.csd.android.net.ResponseBean;
import com.csd.android.utils.ToastUtils;
import com.csd.android.utils.UIUtils;

import java.io.IOException;
import java.util.HashMap;

public class CarPhotoViewLoader {
	private TaskDetailActivity context;
	private View car_photos_check_view;
	private ScrollView vg_content;

	public CarPhotoViewLoader(TaskDetailActivity context, ScrollView vg_content) {
		this.context = context;
		this.vg_content = vg_content;
	}

	public void load() {
		final CarImages entity = context.task_detail_entity.getLicense().getCarImage();

		if (car_photos_check_view == null) {
			car_photos_check_view = LayoutInflater.from(context).inflate(R.layout.layout_car_photos_check, null);
			car_photos_check_view.findViewById(R.id.btn_pass).setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					requestPass();
				}
			});
		}

		if (!UIUtils.isEmpty(entity.getImgPos1())) {
			Glide.with(context).load(Uri.parse(entity.getImgPos1())).placeholder(R.mipmap.icon_pic_loading).error(R.mipmap.icon_pic_load_fail)
					.listener(new ImageLoaderListener(context, Uri.parse(entity.getImgPos1())))
					.into((ImageView) car_photos_check_view.findViewById(R.id.iv_car_left_front));
		}
		else {
			Glide.with(context).load(R.mipmap.icon_left_front).listener(new ImageLoaderListener(context, null))
					.into((ImageView) car_photos_check_view.findViewById(R.id.iv_car_left_front));
		}

		if (!UIUtils.isEmpty(entity.getImgPos2())) {
			Glide.with(context).load(Uri.parse(entity.getImgPos2())).placeholder(R.mipmap.icon_pic_loading).error(R.mipmap.icon_pic_load_fail)
					.listener(new ImageLoaderListener(context, Uri.parse(entity.getImgPos2())))
					.into((ImageView) car_photos_check_view.findViewById(R.id.iv_car_right_front));
		}
		else {
			Glide.with(context).load(R.mipmap.icon_right_front).listener(new ImageLoaderListener(context, null))
					.into((ImageView) car_photos_check_view.findViewById(R.id.iv_car_right_front));
		}

		if (!UIUtils.isEmpty(entity.getImgPos3())) {
			Glide.with(context).load(Uri.parse(entity.getImgPos3())).placeholder(R.mipmap.icon_pic_loading).error(R.mipmap.icon_pic_load_fail)
					.listener(new ImageLoaderListener(context, Uri.parse(entity.getImgPos3())))
					.into((ImageView) car_photos_check_view.findViewById(R.id.iv_car_left_back));
		}
		else {
			Glide.with(context).load(R.mipmap.icon_left_back).listener(new ImageLoaderListener(context, null))
					.into((ImageView) car_photos_check_view.findViewById(R.id.iv_car_left_back));
		}

		if (!UIUtils.isEmpty(entity.getImgPos4())) {
			Glide.with(context).load(Uri.parse(entity.getImgPos4())).placeholder(R.mipmap.icon_pic_loading).error(R.mipmap.icon_pic_load_fail)
					.listener(new ImageLoaderListener(context, Uri.parse(entity.getImgPos4())))
					.into((ImageView) car_photos_check_view.findViewById(R.id.iv_car_right_back));
		}
		else {
			Glide.with(context).load(R.mipmap.icon_right_back).listener(new ImageLoaderListener(context, null))
					.into((ImageView) car_photos_check_view.findViewById(R.id.iv_car_right_back));
		}

		if (!UIUtils.isEmpty(entity.getImgPos5())) {
			Glide.with(context).load(Uri.parse(entity.getImgPos5())).placeholder(R.mipmap.icon_pic_loading).error(R.mipmap.icon_pic_load_fail)
					.listener(new ImageLoaderListener(context, Uri.parse(entity.getImgPos5())))
					.into((ImageView) car_photos_check_view.findViewById(R.id.iv_car_driver));
		}
		else {
			Glide.with(context).load(R.mipmap.icon_driver_space).listener(new ImageLoaderListener(context, null))
					.into((ImageView) car_photos_check_view.findViewById(R.id.iv_car_driver));
		}

		if (!UIUtils.isEmpty(entity.getImgPos6())) {
			Glide.with(context).load(Uri.parse(entity.getImgPos6())).placeholder(R.mipmap.icon_pic_loading).error(R.mipmap.icon_pic_load_fail)
					.listener(new ImageLoaderListener(context, Uri.parse(entity.getImgPos6())))
					.into((ImageView) car_photos_check_view.findViewById(R.id.iv_car_control));
		}
		else {
			Glide.with(context).load(R.mipmap.icon_controller).listener(new ImageLoaderListener(context, null))
					.into((ImageView) car_photos_check_view.findViewById(R.id.iv_car_control));
		}

		if (!UIUtils.isEmpty(entity.getImgPos7())) {
			Glide.with(context).load(Uri.parse(entity.getImgPos7())).placeholder(R.mipmap.icon_pic_loading).error(R.mipmap.icon_pic_load_fail)
					.listener(new ImageLoaderListener(context, Uri.parse(entity.getImgPos7())))
					.into((ImageView) car_photos_check_view.findViewById(R.id.iv_car_front_row));
		}
		else {
			Glide.with(context).load(R.mipmap.icon_front_row).listener(new ImageLoaderListener(context, null))
					.into((ImageView) car_photos_check_view.findViewById(R.id.iv_car_front_row));
		}

		if (!UIUtils.isEmpty(entity.getImgPos8())) {
			Glide.with(context).load(Uri.parse(entity.getImgPos8())).placeholder(R.mipmap.icon_pic_loading).error(R.mipmap.icon_pic_load_fail)
					.listener(new ImageLoaderListener(context, Uri.parse(entity.getImgPos8())))
					.into((ImageView) car_photos_check_view.findViewById(R.id.iv_car_back_row));
		}
		else {
			Glide.with(context).load(R.mipmap.icon_back_row).listener(new ImageLoaderListener(context, null))
					.into((ImageView) car_photos_check_view.findViewById(R.id.iv_car_back_row));
		}

		vg_content.addView(car_photos_check_view);
	}

	protected void requestPass() {
		if (UIUtils.isEmpty(context.task_detail_entity.getLicense().getCarImage().getImgPos1())
				|| UIUtils.isEmpty(context.task_detail_entity.getLicense().getCarImage().getImgPos2())
				|| UIUtils.isEmpty(context.task_detail_entity.getLicense().getCarImage().getImgPos3())
				|| UIUtils.isEmpty(context.task_detail_entity.getLicense().getCarImage().getImgPos4())
				|| UIUtils.isEmpty(context.task_detail_entity.getLicense().getCarImage().getImgPos5())
				|| UIUtils.isEmpty(context.task_detail_entity.getLicense().getCarImage().getImgPos6())
				|| UIUtils.isEmpty(context.task_detail_entity.getLicense().getCarImage().getImgPos7())
				|| UIUtils.isEmpty(context.task_detail_entity.getLicense().getCarImage().getImgPos8())) {
			ToastUtils.showToast("图片上传不完整");
			return;
		}

		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("car_id", context.task_detail_entity.getCarId());
		context.TAG_NO_FIRST_REQUEST = "tag_photo_pass";
		new CCHttpEngine(context, NetConstants.NET_ID_CAR_PHOTO_PASS, hashMap, context.TAG_NO_FIRST_REQUEST, new HttpCallBack() {
			@Override
			public void onSuccess(ResponseBean responseBean) {
				if (responseBean.getCode() == 0) {
					ToastUtils.showToast("操作成功");
					context.task_detail_entity.getLicense().getCarImage().setStatus("3");
					context.initCardsCheckStatus();
					context.initRedCircle();
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

	public void onActivityResult(int requestCode, int resultCode, Intent data) {

	}

	public void onUploadPicResult(String uri) {
		if (PhotoViewClickListener.CAR_PHOTO_VIEW_ID == R.id.iv_car_left_front) {
			context.task_detail_entity.getLicense().getCarImage().setImgPos1(uri);
		}
		else if (PhotoViewClickListener.CAR_PHOTO_VIEW_ID == R.id.iv_car_right_front) {
			context.task_detail_entity.getLicense().getCarImage().setImgPos2(uri);
		}
		else if (PhotoViewClickListener.CAR_PHOTO_VIEW_ID == R.id.iv_car_left_back) {
			context.task_detail_entity.getLicense().getCarImage().setImgPos3(uri);
		}
		else if (PhotoViewClickListener.CAR_PHOTO_VIEW_ID == R.id.iv_car_right_back) {
			context.task_detail_entity.getLicense().getCarImage().setImgPos4(uri);
		}
		else if (PhotoViewClickListener.CAR_PHOTO_VIEW_ID == R.id.iv_car_driver) {
			context.task_detail_entity.getLicense().getCarImage().setImgPos5(uri);
		}
		else if (PhotoViewClickListener.CAR_PHOTO_VIEW_ID == R.id.iv_car_control) {
			context.task_detail_entity.getLicense().getCarImage().setImgPos6(uri);
		}
		else if (PhotoViewClickListener.CAR_PHOTO_VIEW_ID == R.id.iv_car_front_row) {
			context.task_detail_entity.getLicense().getCarImage().setImgPos7(uri);
		}
		else if (PhotoViewClickListener.CAR_PHOTO_VIEW_ID == R.id.iv_car_back_row) {
			context.task_detail_entity.getLicense().getCarImage().setImgPos8(uri);
		}

		context.task_detail_entity.getLicense().getCarImage().setStatus("0");
		context.initCardsCheckStatus();

	}

}
