package com.csd.android.viewloader;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.csd.android.R;
import com.csd.android.activity.TaskDetailActivity;
import com.csd.android.model.TogetherCheckInfo;
import com.csd.android.net.CCHttpEngine;
import com.csd.android.net.HttpCallBack;
import com.csd.android.net.NetConstants;
import com.csd.android.net.ResponseBean;
import com.csd.android.utils.ToastUtils;
import com.csd.android.utils.UIUtils;

import java.io.IOException;
import java.util.HashMap;

public class TogetherCardsCheckViewLoader {
	private TaskDetailActivity context;
	private View together_cards_check_view;
	private ScrollView vg_content;

	public TogetherCardsCheckViewLoader(TaskDetailActivity context, ScrollView vg_content) {
		this.context = context;
		this.vg_content = vg_content;
	}

	public void load() {
		initView();
		loadImage();
		initUiData();
	}

	private void initView() {
		if (together_cards_check_view == null) {
			together_cards_check_view = LayoutInflater.from(context).inflate(R.layout.layout_cards_check, null);
			together_cards_check_view.findViewById(R.id.btn_upload_other_cards).setOnClickListener(new UploadOtherPhotoClickListener());
			together_cards_check_view.findViewById(R.id.vg_pass).setOnClickListener(new PassClickListener());
			together_cards_check_view.findViewById(R.id.vg_reject).setOnClickListener(new RejectClickListener());
		}
		vg_content.addView(together_cards_check_view);
	}

	private void initUiData() {
		((TextView) together_cards_check_view.findViewById(R.id.tv_identity_card_name)).setText("身份证姓名："
				+ context.task_detail_entity.getLicense().getIdentifyLicense().getUsername());
		((TextView) together_cards_check_view.findViewById(R.id.tv_driving_license_name)).setText("行驶证姓名："
				+ context.task_detail_entity.getLicense().getDriverLicense().getOwnerName());
	}

	private void loadImage() {
		TogetherCheckInfo together_license_entity = context.task_detail_entity.getLicense().getOtherLicense();
		if (!UIUtils.isEmpty(together_license_entity.getImage_uri1())) {
			Glide.with(context).load(Uri.parse(together_license_entity.getImage_uri1())).placeholder(R.mipmap.icon_pic_loading)
					.error(R.mipmap.icon_pic_load_fail)
					.listener(new ImageLoaderListener(context, Uri.parse(together_license_entity.getImage_uri1())))
					.into((ImageView) together_cards_check_view.findViewById(R.id.iv_together_1));
		}
		else {
			Glide.with(context).load(R.mipmap.identity_card_face).listener(new ImageLoaderListener(context, null))
					.into((ImageView) together_cards_check_view.findViewById(R.id.iv_together_1));
		}

		if (!UIUtils.isEmpty(together_license_entity.getImage_uri2())) {
			Glide.with(context).load(Uri.parse(together_license_entity.getImage_uri2())).placeholder(R.mipmap.icon_pic_loading)
					.error(R.mipmap.icon_pic_load_fail)
					.listener(new ImageLoaderListener(context, Uri.parse(together_license_entity.getImage_uri2())))
					.into((ImageView) together_cards_check_view.findViewById(R.id.iv_together_2));
		}
		else {
			Glide.with(context).load(R.mipmap.identity_card_face).listener(new ImageLoaderListener(context, null))
					.into((ImageView) together_cards_check_view.findViewById(R.id.iv_together_2));
		}

		if (!UIUtils.isEmpty(together_license_entity.getImage_uri3())) {
			Glide.with(context).load(Uri.parse(together_license_entity.getImage_uri3())).placeholder(R.mipmap.icon_pic_loading)
					.error(R.mipmap.icon_pic_load_fail)
					.listener(new ImageLoaderListener(context, Uri.parse(together_license_entity.getImage_uri3())))
					.into((ImageView) together_cards_check_view.findViewById(R.id.iv_together_3));
		}
		else {
			Glide.with(context).load(R.mipmap.identity_card_face).listener(new ImageLoaderListener(context, null))
					.into((ImageView) together_cards_check_view.findViewById(R.id.iv_together_3));
		}

		if (!UIUtils.isEmpty(together_license_entity.getImage_uri4())) {
			Glide.with(context).load(Uri.parse(together_license_entity.getImage_uri4())).placeholder(R.mipmap.icon_pic_loading)
					.error(R.mipmap.icon_pic_load_fail)
					.listener(new ImageLoaderListener(context, Uri.parse(together_license_entity.getImage_uri4())))
					.into((ImageView) together_cards_check_view.findViewById(R.id.iv_together_4));
		}
		else {
			Glide.with(context).load(R.mipmap.identity_card_face).listener(new ImageLoaderListener(context, null))
					.into((ImageView) together_cards_check_view.findViewById(R.id.iv_together_4));
		}

	}

	class UploadOtherPhotoClickListener implements OnClickListener {
		public void onClick(View v) {
			int visibility = together_cards_check_view.findViewById(R.id.vg_other_cards).getVisibility();
			if (visibility == View.GONE) {
				together_cards_check_view.findViewById(R.id.vg_other_cards).setVisibility(View.VISIBLE);
			}
			else if (visibility == View.VISIBLE) {
				together_cards_check_view.findViewById(R.id.vg_other_cards).setVisibility(View.GONE);
			}
		}
	}

	class PassClickListener implements OnClickListener {
		public void onClick(View v) {
			if (UIUtils.isEmpty(context.task_detail_entity.getLicense().getIdentifyLicense().getUsername())) {
				ToastUtils.showToast("身份证信息不完整");
				return;
			}
			if (UIUtils.isEmpty(context.task_detail_entity.getLicense().getDriverLicense().getOwnerName())) {
				ToastUtils.showToast("行驶证证信息不完整");
				return;
			}
			if (!context.task_detail_entity.getLicense().getIdentifyLicense().getUsername()
					.equals(context.task_detail_entity.getLicense().getDriverLicense().getOwnerName())) {
				ToastUtils.showToast("身份证信息与行驶证信息不符合");
				return;
			}

			requestPass();
		}
	}

	class RejectClickListener implements OnClickListener {
		public void onClick(View v) {
			requestReject();
		}

	}

	public void requestPass() {
		context.TAG_NO_FIRST_REQUEST = "tag_request_pass";
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("car_id", context.task_detail_entity.getCarId());
		new CCHttpEngine(context, NetConstants.NET_ID_TOGETHER_CARDS_CHECK_PASS, hashMap, context.TAG_NO_FIRST_REQUEST, new HttpCallBack() {

			@Override
			public void onSuccess(ResponseBean responseBean) {
				if (responseBean.getCode() == 0) {
					ToastUtils.showToast("操作成功");
					context.task_detail_entity.getLicense().getOtherLicense().setStatus("3");
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

	public void requestReject() {
		context.TAG_NO_FIRST_REQUEST = "tag_request_reject";
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("car_id", context.task_detail_entity.getCarId());
		new CCHttpEngine(context, NetConstants.NET_ID_TOGETHER_CARDS_CHECK_REJECT, hashMap, context.TAG_NO_FIRST_REQUEST, new HttpCallBack() {

			@Override
			public void onSuccess(ResponseBean responseBean) {
				if (responseBean.getCode() == 0) {
					ToastUtils.showToast("操作成功");
					context.task_detail_entity.getLicense().getOtherLicense().setStatus("2");
					context.task_detail_entity.getLicense().getDriverLicense().setStatus("2");
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
		if (PhotoViewClickListener.CAR_PHOTO_VIEW_ID == R.id.iv_together_1) {
			context.task_detail_entity.getLicense().getOtherLicense().setImage_uri1(uri);
		}
		else if (PhotoViewClickListener.CAR_PHOTO_VIEW_ID == R.id.iv_together_2) {
			context.task_detail_entity.getLicense().getOtherLicense().setImage_uri2(uri);
		}
		if (PhotoViewClickListener.CAR_PHOTO_VIEW_ID == R.id.iv_together_3) {
			context.task_detail_entity.getLicense().getOtherLicense().setImage_uri3(uri);
		}
		if (PhotoViewClickListener.CAR_PHOTO_VIEW_ID == R.id.iv_together_4) {
			context.task_detail_entity.getLicense().getOtherLicense().setImage_uri4(uri);
		}

		context.task_detail_entity.getLicense().getOtherLicense().setStatus("0");
		context.initCardsCheckStatus();

	}

}
