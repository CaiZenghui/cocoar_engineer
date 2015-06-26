package com.csd.android.viewloader;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.csd.android.R;
import com.csd.android.activity.TaskDetailActivity;
import com.csd.android.model.IdCity;
import com.csd.android.model.IdentifyLicense;
import com.csd.android.net.CCHttpEngine;
import com.csd.android.net.HttpCallBack;
import com.csd.android.net.NetConstants;
import com.csd.android.net.ResponseBean;
import com.csd.android.utils.IDUtils;
import com.csd.android.utils.ToastUtils;
import com.csd.android.utils.UIUtils;
import com.csd.android.widget.CCWaitingDialog;
import com.csd.android.widget.DatePickerPop;
import com.csd.android.widget.IdAddressPickerPop;
import com.csd.android.widget.PickerPop;
import com.csd.android.widget.PickerPop.OnConfirmListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class IdentityCardViewLoader {
	private TaskDetailActivity context;
	private View identity_card_verify_view;
	private ScrollView vg_content;
	private ArrayList<IdCity> id_address_entity;

	public IdentityCardViewLoader(TaskDetailActivity context, ScrollView vg_content) {
		this.context = context;
		this.vg_content = vg_content;
	}

	public void load() {
		IdentifyLicense entity = context.task_detail_entity.getLicense().getIdentifyLicense();
		initView();
		loadImage(entity);
		initUiData(entity);
	}

	private void initView() {
		if (identity_card_verify_view == null) {
			identity_card_verify_view = LayoutInflater.from(context).inflate(R.layout.layout_identity_card_check, null);

			identity_card_verify_view.findViewById(R.id.btn_verify_identity_card).setOnClickListener(new VerifyIdInfoClickListener());
			identity_card_verify_view.findViewById(R.id.vg_date_out_line).setOnClickListener(new DateOutLineClickListener());
			identity_card_verify_view.findViewById(R.id.vg_address).setOnClickListener(new AddressClickListener());
			identity_card_verify_view.findViewById(R.id.vg_success).setOnClickListener(new PassClickListener());
			identity_card_verify_view.findViewById(R.id.btn_save).setOnClickListener(new SaveClickListener());
			identity_card_verify_view.findViewById(R.id.vg_fail).setOnClickListener(new RejectClickListener());

			if (!UIUtils.isEmpty(context.task_detail_entity.getLicense().getIdentifyLicense().getFailedNote())) {
				TextView tv_reject_reason = (TextView) identity_card_verify_view.findViewById(R.id.tv_reject_reason);
				tv_reject_reason.setVisibility(View.VISIBLE);
				tv_reject_reason.setText(context.task_detail_entity.getLicense().getIdentifyLicense().getFailedNote());
			}

			try {
				Type type = new TypeToken<ResponseBean<ArrayList<IdCity>>>() {
				}.getType();
				Gson gson = new Gson();
				ResponseBean responseBean = gson.fromJson(UIUtils.getFromAssets("id_address_list"), type);
				id_address_entity = (ArrayList<IdCity>) responseBean.getData();
			}
			catch (Exception e) {
				e.printStackTrace();
			}

		}
		vg_content.addView(identity_card_verify_view);
	}

	private void initUiData(IdentifyLicense entity) {
		((EditText) identity_card_verify_view.findViewById(R.id.et_name)).setText(entity.getUsername());
		((EditText) identity_card_verify_view.findViewById(R.id.et_identity_card_num)).setText(entity.getCardNumber());
		((TextView) identity_card_verify_view.findViewById(R.id.tv_address)).setText(entity.getAddress());
		((TextView) identity_card_verify_view.findViewById(R.id.tv_date_out_line)).setText(entity.getDeadLine());

		if ("3".equals(entity.getIdentityNumberStatus())) {// 身份证已通过审核；
			((Button) identity_card_verify_view.findViewById(R.id.btn_verify_identity_card)).setText("修改身份信息");
			identity_card_verify_view.findViewById(R.id.et_identity_card_num).setEnabled(false);
			identity_card_verify_view.findViewById(R.id.et_name).setEnabled(false);

		}
		else {
			((Button) identity_card_verify_view.findViewById(R.id.btn_verify_identity_card)).setText("验证身份信息");
			identity_card_verify_view.findViewById(R.id.et_identity_card_num).setEnabled(true);
			identity_card_verify_view.findViewById(R.id.et_name).setEnabled(true);
		}
	}

	private void loadImage(IdentifyLicense entity) {
		if (!UIUtils.isEmpty(entity.getIdentifyImage())) {
			Glide.with(context).load(Uri.parse(entity.getIdentifyImage())).placeholder(R.mipmap.icon_pic_loading)
					.error(R.mipmap.icon_pic_load_fail).listener(new ImageLoaderListener(context, Uri.parse(entity.getIdentifyImage())))
					.into((ImageView) identity_card_verify_view.findViewById(R.id.iv_identity_card_face));
			((TextView) identity_card_verify_view.findViewById(R.id.tv_face_2)).setText("上传成功，点击图片可重新上传");
		}
		else {
			Glide.with(context).load(R.mipmap.identity_card_face).listener(new ImageLoaderListener(context, null))
					.into((ImageView) identity_card_verify_view.findViewById(R.id.iv_identity_card_face));
			((TextView) identity_card_verify_view.findViewById(R.id.tv_face_2)).setText("点击上传");
		}

		if (!UIUtils.isEmpty(entity.getIdentifyBackImage())) {
			Glide.with(context).load(Uri.parse(entity.getIdentifyBackImage())).placeholder(R.mipmap.icon_pic_loading)
					.error(R.mipmap.icon_pic_load_fail).listener(new ImageLoaderListener(context, Uri.parse(entity.getIdentifyBackImage())))
					.into((ImageView) identity_card_verify_view.findViewById(R.id.iv_identity_card_back));
			((TextView) identity_card_verify_view.findViewById(R.id.tv_back_2)).setText("上传成功，点击图片可重新上传");
		}
		else {
			Glide.with(context).load(R.mipmap.identity_card_back).listener(new ImageLoaderListener(context, null))
					.into((ImageView) identity_card_verify_view.findViewById(R.id.iv_identity_card_back));
			((TextView) identity_card_verify_view.findViewById(R.id.tv_back_2)).setText("点击上传");
		}

	}

	class AddressClickListener implements OnClickListener {

		private IdAddressPickerPop picker;

		@Override
		public void onClick(View v) {
			if (picker == null) {
				picker = new IdAddressPickerPop(context, id_address_entity);
				picker.setOnConfirmListener(new IdAddressPickerPop.OnConfirmListener() {
					public void onConfirmClick(String... results) {
						((TextView) identity_card_verify_view.findViewById(R.id.tv_address)).setText(results[0] + " " + results[1]);
						context.task_detail_entity.getLicense().getIdentifyLicense().setProvince(results[0]);
						context.task_detail_entity.getLicense().getIdentifyLicense().setCity(results[1]);
					}
				});
			}
			picker.show();
		}
	}

	class DateOutLineClickListener implements OnClickListener {

		private DatePickerPop picker;

		@Override
		public void onClick(View v) {
			if (picker == null) {
				picker = new DatePickerPop(context);
				picker.setOnConfirmListener(new DatePickerPop.OnConfirmListener() {
					public void onConfirmClick(String... results) {
						((TextView) identity_card_verify_view.findViewById(R.id.tv_date_out_line)).setText(results[0] + "-" + results[1] + "-"
								+ results[2]);
						context.task_detail_entity.getLicense().getIdentifyLicense().setDeadLine(results[0] + "-" + results[1] + "-" + results[2]);
					}
				});
			}
			picker.show();
		}
	}

	class PassClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			IdentifyLicense entity = context.task_detail_entity.getLicense().getIdentifyLicense();
			if (UIUtils.isEmpty(entity.getIdentifyImage())) {
				ToastUtils.showToast("未上传身份证正面照片");
				return;
			}
			if (UIUtils.isEmpty(entity.getIdentifyBackImage())) {
				ToastUtils.showToast("未上传身份证背面照片");
				return;
			}
			if (!"3".equals(entity.getIdentityNumberStatus())) {
				ToastUtils.showToast("未完成身份信息验证");
				return;
			}

			if (UIUtils.isEmpty(((TextView) identity_card_verify_view.findViewById(R.id.tv_date_out_line)).getText().toString().trim())) {
				ToastUtils.showToast("请选择有效期");
				return;
			}

			String date_str = ((TextView) identity_card_verify_view.findViewById(R.id.tv_date_out_line)).getText().toString().trim();
			if (!UIUtils.checkDateValidity(date_str)) {
				ToastUtils.showToast("身份证已过有效期");
				return;
			}

			if (UIUtils.isEmpty(((TextView) identity_card_verify_view.findViewById(R.id.tv_address)).getText().toString().trim())) {
				ToastUtils.showToast("请选择地址");
				return;
			}

			requestPass();

		}
	}

	class SaveClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			requestSave();
		}
	}

	class RejectClickListener implements OnClickListener {
		private String[] strs = { "非二代身份证/图片不完整", "身份证上传模糊", "身份证正面未上传", "身份证反面未上传", "身份证上传不完整", "身份验证失败", "身份证已过有效期" };
		private PickerPop pop;

		public void onClick(View v) {
			if (pop == null) {
				pop = new PickerPop(context, strs);
				pop.setOnConfirmListener(new OnConfirmListener() {
					@Override
					public void onConfirmClick(int... results) {
						identity_card_verify_view.findViewById(R.id.tv_reject_reason).setVisibility(View.VISIBLE);
						((TextView) identity_card_verify_view.findViewById(R.id.tv_reject_reason)).setText(strs[results[0]]);
						UIUtils.scrollToBottome(vg_content);
						context.task_detail_entity.getLicense().getIdentifyLicense().setRejectCode((100 + 1 + results[0]) + "");
						requestReject();
					}
				});
			}
			pop.show();
		}

	}

	class VerifyIdInfoClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if ("3".equals(context.task_detail_entity.getLicense().getIdentifyLicense().getIdentityNumberStatus())) {// 身份信息已通过验证；
				((Button) identity_card_verify_view.findViewById(R.id.btn_verify_identity_card)).setText("验证身份信息");
				context.task_detail_entity.getLicense().getIdentifyLicense().setIdentityNumberStatus("0");
				identity_card_verify_view.findViewById(R.id.et_name).setEnabled(true);
				identity_card_verify_view.findViewById(R.id.et_identity_card_num).setEnabled(true);
				return;
			}
			if (!IDUtils.validateCard(((EditText) identity_card_verify_view.findViewById(R.id.et_identity_card_num)).getText().toString().trim())) {
				ToastUtils.showToast("身份证号码无效");
				return;
			}

			HashMap<String, String> hashMap = new HashMap<String, String>();
			hashMap.put("username", ((EditText) identity_card_verify_view.findViewById(R.id.et_name)).getText().toString().trim());
			hashMap.put("card_id", ((EditText) identity_card_verify_view.findViewById(R.id.et_identity_card_num)).getText().toString().trim());
			hashMap.put("user_id", context.task_detail_entity.getOwner().getUser_id());
			context.TAG_NO_FIRST_REQUEST = "verify_identity_card";
			CCWaitingDialog.show(context);
			new CCHttpEngine(context, NetConstants.NET_ID_VERIFY_IDENTITY_CARD_INFO, hashMap, context.TAG_NO_FIRST_REQUEST, new HttpCallBack() {

				@Override
				public void onSuccess(ResponseBean responseBean) {
					CCWaitingDialog.dismiss(context);
					if (responseBean.getCode() == 0) {
						ToastUtils.showToast("身份信息验证通过");
						context.task_detail_entity.getLicense().getIdentifyLicense().setIdentityNumberStatus("3");
						((Button) identity_card_verify_view.findViewById(R.id.btn_verify_identity_card)).setText("修改身份信息");
						identity_card_verify_view.findViewById(R.id.et_name).setEnabled(false);
						identity_card_verify_view.findViewById(R.id.et_identity_card_num).setEnabled(false);
					}
					else {
						ToastUtils.showToast(responseBean.getMessage());
					}

				}

				@Override
				public void onNetUnavailable(String net_unAvailabel) {
					CCWaitingDialog.dismiss(context);
					ToastUtils.showToast(net_unAvailabel);
				}

				@Override
				public void onFailure(IOException e) {
					CCWaitingDialog.dismiss(context);
					ToastUtils.showToast(e.getMessage());
				}
			}).executeTask();

		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	}

	public void requestReject() {
		context.TAG_NO_FIRST_REQUEST = "tag_request_reject";
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("car_id", context.task_detail_entity.getCarId());
		hashMap.put("status", context.task_detail_entity.getLicense().getIdentifyLicense().getRejectCode());
		new CCHttpEngine(context, NetConstants.NET_ID_IDENTITY_CARD_INFO_REJECT, hashMap, context.TAG_NO_FIRST_REQUEST, new HttpCallBack() {
			public void onSuccess(ResponseBean responseBean) {
				if (responseBean.getCode() == 0) {
					ToastUtils.showToast("操作成功");
					context.task_detail_entity.getLicense().getIdentifyLicense().setStatus("1");
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

	public void requestSave() {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		if (!UIUtils.isEmpty(((EditText) identity_card_verify_view.findViewById(R.id.et_name)).getText().toString().trim())) {
			hashMap.put("fill_name", ((EditText) identity_card_verify_view.findViewById(R.id.et_name)).getText().toString().trim());
		}
		if (!UIUtils.isEmpty(((EditText) identity_card_verify_view.findViewById(R.id.et_identity_card_num)).getText().toString().trim())) {
			hashMap.put("card_id", ((EditText) identity_card_verify_view.findViewById(R.id.et_identity_card_num)).getText().toString().trim());
		}
		if (!UIUtils.isEmpty(context.task_detail_entity.getLicense().getIdentifyLicense().getDeadLine())) {
			hashMap.put("certificate_end_time", context.task_detail_entity.getLicense().getIdentifyLicense().getDeadLine());
		}
		if (!UIUtils.isEmpty(context.task_detail_entity.getLicense().getIdentifyLicense().getProvince())) {
			hashMap.put("city", context.task_detail_entity.getLicense().getIdentifyLicense().getProvince());
		}
		if (!UIUtils.isEmpty(context.task_detail_entity.getLicense().getIdentifyLicense().getCity())) {
			hashMap.put("area", context.task_detail_entity.getLicense().getIdentifyLicense().getCity());
		}
		if (hashMap.size() == 0) {
			ToastUtils.showToast("暂无信息需要保存");
			return;
		}
		context.TAG_NO_FIRST_REQUEST = "tag_request_save";
		hashMap.put("car_id", context.task_detail_entity.getCarId());
		new CCHttpEngine(context, NetConstants.NET_ID_IDENTITY_CARD_INFO_PASS_SAVE, hashMap, context.TAG_NO_FIRST_REQUEST, new HttpCallBack() {
			public void onSuccess(ResponseBean responseBean) {
				if (responseBean.getCode() == 0) {
					ToastUtils.showToast("保存成功");
					identity_card_verify_view.findViewById(R.id.tv_reject_reason).setVisibility(View.GONE);
					context.task_detail_entity.getLicense().getIdentifyLicense()
							.setUsername(((EditText) identity_card_verify_view.findViewById(R.id.et_name)).getText().toString().trim());
					context.task_detail_entity
							.getLicense()
							.getIdentifyLicense()
							.setCardNumber(((EditText) identity_card_verify_view.findViewById(R.id.et_identity_card_num)).getText().toString().trim());
					if ("2".equals(context.task_detail_entity.getLicense().getIdentifyLicense().getStatus())
							|| "1".equals(context.task_detail_entity.getLicense().getIdentifyLicense().getStatus())) {
						context.task_detail_entity.getLicense().getIdentifyLicense().setStatus("0");
					}
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

	public void requestPass() {
		context.TAG_NO_FIRST_REQUEST = "tag_request_pass";
		final IdentifyLicense entity = context.task_detail_entity.getLicense().getIdentifyLicense();
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("car_id", context.task_detail_entity.getCarId());
		hashMap.put("fill_name", ((EditText) identity_card_verify_view.findViewById(R.id.et_name)).getText().toString().trim());
		hashMap.put("card_id", ((EditText) identity_card_verify_view.findViewById(R.id.et_identity_card_num)).getText().toString().trim());
		hashMap.put("certificate_end_time", entity.getDeadLine());
		hashMap.put("city", entity.getProvince());
		hashMap.put("area", entity.getCity());
		hashMap.put("audit", "1");
		new CCHttpEngine(context, NetConstants.NET_ID_IDENTITY_CARD_INFO_PASS_SAVE, hashMap, context.TAG_NO_FIRST_REQUEST, new HttpCallBack() {
			public void onSuccess(ResponseBean responseBean) {
				if (responseBean.getCode() == 0) {
					ToastUtils.showToast("审核通过");
					identity_card_verify_view.findViewById(R.id.tv_reject_reason).setVisibility(View.GONE);
					entity.setUsername(((EditText) identity_card_verify_view.findViewById(R.id.et_name)).getText().toString().trim());
					entity.setCardNumber(((EditText) identity_card_verify_view.findViewById(R.id.et_identity_card_num)).getText().toString().trim());
					context.task_detail_entity.getLicense().getIdentifyLicense().setStatus("2");
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

	public void onUploadPicResult(String uri) {
		if (PhotoViewClickListener.CAR_PHOTO_VIEW_ID == R.id.iv_identity_card_face) {// 身份证正面；
			context.task_detail_entity.getLicense().getIdentifyLicense().setIdentifyImage(uri);
			((TextView) identity_card_verify_view.findViewById(R.id.tv_face_2)).setText("上传成功，点击图片可重新上传");
		}
		else if (PhotoViewClickListener.CAR_PHOTO_VIEW_ID == R.id.iv_identity_card_back) {
			context.task_detail_entity.getLicense().getIdentifyLicense().setIdentifyBackImage(uri);
			((TextView) identity_card_verify_view.findViewById(R.id.tv_back_2)).setText("上传成功，点击图片可重新上传");
		}
		context.task_detail_entity.getLicense().getIdentifyLicense().setStatus("0");
		context.initCardsCheckStatus();

	}

}
