package com.csd.android.viewloader;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.csd.android.CCApplication;
import com.csd.android.R;
import com.csd.android.activity.SelectCarBrandActivity;
import com.csd.android.activity.TaskDetailActivity;
import com.csd.android.model.CarDetail;
import com.csd.android.model.CarPrice;
import com.csd.android.model.DriverLicense;
import com.csd.android.model.PetrolConsume;
import com.csd.android.model.TaskDetail;
import com.csd.android.net.CCHttpEngine;
import com.csd.android.net.HttpCallBack;
import com.csd.android.net.NetConstants;
import com.csd.android.net.ResponseBean;
import com.csd.android.utils.ToastUtils;
import com.csd.android.utils.UIUtils;
import com.csd.android.widget.DatePickerPop;
import com.csd.android.widget.PickerPop;
import com.csd.android.widget.PickerPop.OnConfirmListener;

import java.io.IOException;
import java.util.HashMap;

public class DrivingLicenseCardViewLoader {
	private TaskDetailActivity context;
	private View driving_license_card_verify_view;
	private ScrollView vg_content;
	private String car_brand_name;
	private String car_type_name;

	public DrivingLicenseCardViewLoader(TaskDetailActivity context, ScrollView vg_content) {
		this.context = context;
		this.vg_content = vg_content;
	}

	public void load() {
		initView();
		loadImage();
		initUiData();
	}

	private void initView() {
		if (driving_license_card_verify_view == null) {
			driving_license_card_verify_view = LayoutInflater.from(context).inflate(R.layout.layout_driving_license_check, null);

			driving_license_card_verify_view.findViewById(R.id.vg_car_brand).setOnClickListener(new CarBrandClickListener());
			driving_license_card_verify_view.findViewById(R.id.layout_car_plate).setOnClickListener(new CarPlateNumClickListener());
			driving_license_card_verify_view.findViewById(R.id.vg_hold_property).setOnClickListener(new HoldPropertyClickListener());
			driving_license_card_verify_view.findViewById(R.id.vg_use_property).setOnClickListener(new UsePropertyClickListener());
			driving_license_card_verify_view.findViewById(R.id.vg_capacity).setOnClickListener(new CapacityClickListener());
			driving_license_card_verify_view.findViewById(R.id.vg_nianjian_youxiaoqi).setOnClickListener(new DatePickerClickListener());
			driving_license_card_verify_view.findViewById(R.id.vg_registration_date).setOnClickListener(new DatePickerClickListener());

			driving_license_card_verify_view.findViewById(R.id.vg_success).setOnClickListener(new PassClickListener());
			driving_license_card_verify_view.findViewById(R.id.btn_save).setOnClickListener(new SaveClickListener());
			driving_license_card_verify_view.findViewById(R.id.vg_fail).setOnClickListener(new RejectClickListener());

			if (!UIUtils.isEmpty(context.task_detail_entity.getLicense().getDriverLicense().getFailedNote())) {
				TextView tv_reject_reason = (TextView) driving_license_card_verify_view.findViewById(R.id.tv_reject_reason);
				tv_reject_reason.setVisibility(View.VISIBLE);
				tv_reject_reason.setText(context.task_detail_entity.getLicense().getDriverLicense().getFailedNote());
			}
		}
		vg_content.addView(driving_license_card_verify_view);
	}

	private void initUiData() {
		CarDetail cardetail_entity = context.task_detail_entity.getCarDetail();
		DriverLicense driverLicense_entity = context.task_detail_entity.getLicense().getDriverLicense();
		((TextView) driving_license_card_verify_view.findViewById(R.id.tv_car_brand)).setText(cardetail_entity.getCarName());
		if (!UIUtils.isEmpty(cardetail_entity.getCarPlateNo())) {
			String num = cardetail_entity.getCarPlateNo();
			((TextView) driving_license_card_verify_view.findViewById(R.id.tv_car_plate1)).setText(num.charAt(0) + "");
			((TextView) driving_license_card_verify_view.findViewById(R.id.tv_car_plate2)).setText(num.charAt(1) + "");
			((TextView) driving_license_card_verify_view.findViewById(R.id.tv_car_plate3)).setText(num.substring(2, num.length()));
		}
		((EditText) driving_license_card_verify_view.findViewById(R.id.et_name)).setText(driverLicense_entity.getOwnerName());
		((TextView) driving_license_card_verify_view.findViewById(R.id.tv_hold_property)).setText(driverLicense_entity.getOwnerTypeName());
		((TextView) driving_license_card_verify_view.findViewById(R.id.tv_use_property)).setText(driverLicense_entity.getUseTypeName());
		((TextView) driving_license_card_verify_view.findViewById(R.id.tv_capacity)).setText(driverLicense_entity.getCapacity());
		((EditText) driving_license_card_verify_view.findViewById(R.id.et_che_liang_shibie_daihao)).setText(driverLicense_entity.getCode());
		((EditText) driving_license_card_verify_view.findViewById(R.id.et_engine_num)).setText(driverLicense_entity.getEngerNumber());
		((TextView) driving_license_card_verify_view.findViewById(R.id.tv_registration_date)).setText(driverLicense_entity.getRegisteDate());
		((TextView) driving_license_card_verify_view.findViewById(R.id.tv_nianjian_youxiaoqi)).setText(driverLicense_entity.getLicenseEndTime());

		getCarPrice(context.task_detail_entity);
	}

	private void loadImage() {
		final DriverLicense entity = context.task_detail_entity.getLicense().getDriverLicense();
		if (!UIUtils.isEmpty(entity.getLicenseImage())) {
			Glide.with(context).load(Uri.parse(entity.getLicenseImage())).placeholder(R.mipmap.icon_pic_loading)
					.error(R.mipmap.icon_pic_load_fail).listener(new ImageLoaderListener(context, Uri.parse(entity.getLicenseImage())))
					.into((ImageView) driving_license_card_verify_view.findViewById(R.id.iv_driving_license_page));
			((TextView) driving_license_card_verify_view.findViewById(R.id.tv_face_2)).setText("上传成功，点击图片可重新上传");
		}
		else {
			Glide.with(context).load(R.mipmap.driving_license_page).listener(new ImageLoaderListener(context, null))
					.into((ImageView) driving_license_card_verify_view.findViewById(R.id.iv_driving_license_page));
			((TextView) driving_license_card_verify_view.findViewById(R.id.tv_face_2)).setText("点击上传");
		}

		if (!UIUtils.isEmpty(entity.getLicenseBackImage())) {
			Glide.with(context).load(Uri.parse(entity.getLicenseBackImage())).placeholder(R.mipmap.icon_pic_loading)
					.error(R.mipmap.icon_pic_load_fail).listener(new ImageLoaderListener(context, Uri.parse(entity.getLicenseBackImage())))
					.into((ImageView) driving_license_card_verify_view.findViewById(R.id.iv_driving_license_vice_page));
			((TextView) driving_license_card_verify_view.findViewById(R.id.tv_back_2)).setText("上传成功，点击图片可重新上传");
		}
		else {
			Glide.with(context).load(R.mipmap.driving_license_vice_page).listener(new ImageLoaderListener(context, null))
					.into((ImageView) driving_license_card_verify_view.findViewById(R.id.iv_driving_license_vice_page));
			((TextView) driving_license_card_verify_view.findViewById(R.id.tv_back_2)).setText("点击上传");
		}
	}

	class CarBrandClickListener implements OnClickListener {
		public void onClick(View v) {
			Intent intent = new Intent(context, SelectCarBrandActivity.class);
			context.startActivityForResult(intent, TaskDetailActivity.REQUEST_CODE_SELECT_CAR_BRAND);
		}
	}

	class DatePickerClickListener implements OnClickListener {
		private DatePickerPop pickerPop;

		@Override
		public void onClick(final View v) {
			if (pickerPop == null) {
				pickerPop = new DatePickerPop(context);
				pickerPop.setOnConfirmListener(new DatePickerPop.OnConfirmListener() {
					public void onConfirmClick(String... results) {
						switch (v.getId()) {
							case R.id.vg_nianjian_youxiaoqi:
								((TextView) driving_license_card_verify_view.findViewById(R.id.tv_nianjian_youxiaoqi)).setText(results[0] + "-"
										+ results[1] + "-" + results[2]);
								context.task_detail_entity.getLicense().getDriverLicense()
										.setLicenseEndTime(results[0] + "-" + results[1] + "-" + results[2]);
								break;

							case R.id.vg_registration_date:
								((TextView) driving_license_card_verify_view.findViewById(R.id.tv_registration_date)).setText(results[0] + "-"
										+ results[1] + "-" + results[2]);
								context.task_detail_entity.getLicense().getDriverLicense()
										.setRegisteDate(results[0] + "-" + results[1] + "-" + results[2]);
								break;
						}
					}
				});
			}
			pickerPop.show();
		}
	}

	class CapacityClickListener implements OnClickListener {
		private PickerPop pop;
		private String[] strs = { "2", "3", "4", "5", "6", "7" };

		public void onClick(View v) {
			if (pop == null) {
				pop = new PickerPop(context, strs);
				pop.setOnConfirmListener(new OnConfirmListener() {
					@Override
					public void onConfirmClick(int... results) {
						((TextView) driving_license_card_verify_view.findViewById(R.id.tv_capacity)).setText(strs[results[0]]);
						context.task_detail_entity.getLicense().getDriverLicense().setCapacity(strs[results[0]]);
					}
				});
			}
			pop.show();
		}
	}

	class UsePropertyClickListener implements OnClickListener {
		private PickerPop pop;
		private String[] strs = { "非营运", "营运" };

		public void onClick(View v) {
			if (pop == null) {
				pop = new PickerPop(context, strs);
				pop.setOnConfirmListener(new OnConfirmListener() {
					@Override
					public void onConfirmClick(int... results) {
						((TextView) driving_license_card_verify_view.findViewById(R.id.tv_use_property)).setText(strs[results[0]]);
						context.task_detail_entity.getLicense().getDriverLicense().setUseType(results[0] + "");
					}
				});
			}
			pop.show();
		}
	}

	class HoldPropertyClickListener implements OnClickListener {
		private PickerPop pop;
		private String[] strs = { "个人", "租赁公司", "其他公司" };

		public void onClick(View v) {
			if (pop == null) {
				pop = new PickerPop(context, strs);
				pop.setOnConfirmListener(new OnConfirmListener() {
					@Override
					public void onConfirmClick(int... results) {
						((TextView) driving_license_card_verify_view.findViewById(R.id.tv_hold_property)).setText(strs[results[0]]);
						context.task_detail_entity.getLicense().getDriverLicense().setOwnerType(results[0] + "");
					}
				});
			}
			pop.show();
		}
	}

	class CarPlateNumClickListener implements OnClickListener {
		private PickerPop carPlatePop;
		private String[] provices = CCApplication.getApplication().getResources().getStringArray(R.array.provinces);
		private String[] letters = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
				"W", "X", "Y", "Z" };

		public void onClick(View v) {
			if (carPlatePop == null) {
				carPlatePop = new PickerPop(context, provices, letters);
				carPlatePop.setOnConfirmListener(new OnConfirmListener() {
					@Override
					public void onConfirmClick(int... results) {
						((TextView) driving_license_card_verify_view.findViewById(R.id.tv_car_plate1)).setText(provices[results[0]]);
						((TextView) driving_license_card_verify_view.findViewById(R.id.tv_car_plate2)).setText(letters[results[1]]);
					}
				});
			}
			carPlatePop.show();
		}
	}

	class PassClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			CarDetail cardetail_entity = context.task_detail_entity.getCarDetail();
			DriverLicense driverLicense_entity = context.task_detail_entity.getLicense().getDriverLicense();
			if (UIUtils.isEmpty(driverLicense_entity.getLicenseImage())) {
				ToastUtils.showToast("未上传行驶证正面照片");
				return;
			}
			if (UIUtils.isEmpty(driverLicense_entity.getLicenseBackImage())) {
				ToastUtils.showToast("未上传行驶证副页照片");
				return;
			}

			if (UIUtils.isEmpty(cardetail_entity.getCarTypeId())) {
				ToastUtils.showToast("未选择品牌型号");
				return;
			}

			if (UIUtils.isEmpty(((TextView) driving_license_card_verify_view.findViewById(R.id.tv_car_plate1)).getText().toString().trim())
					|| UIUtils.isEmpty(((TextView) driving_license_card_verify_view.findViewById(R.id.tv_car_plate2)).getText().toString().trim())
					|| UIUtils.isEmpty(((TextView) driving_license_card_verify_view.findViewById(R.id.tv_car_plate3)).getText().toString().trim())) {
				ToastUtils.showToast("请检查车牌号码");
				return;
			}

			if (UIUtils.isEmpty(((EditText) driving_license_card_verify_view.findViewById(R.id.et_name)).getText().toString().trim())) {
				ToastUtils.showToast("请输入所有人姓名");
				return;
			}
			if (UIUtils.isEmpty(driverLicense_entity.getOwnerType())) {// 所有人性质；
				ToastUtils.showToast("请选择所有性质");
				return;
			}

			if (UIUtils.isEmpty(driverLicense_entity.getUseType())) {
				ToastUtils.showToast("请选择使用性质");
				return;
			}

			if (UIUtils.isEmpty(driverLicense_entity.getCapacity())) {
				ToastUtils.showToast("请选择可载人数");
				return;
			}

			if (UIUtils.isEmpty(((EditText) (driving_license_card_verify_view.findViewById(R.id.et_che_liang_shibie_daihao))).getText().toString()
					.trim())) {
				ToastUtils.showToast("请输入车辆识别号");
				return;
			}

			if (UIUtils.isEmpty(((EditText) (driving_license_card_verify_view.findViewById(R.id.et_engine_num))).getText().toString().trim())) {
				ToastUtils.showToast("请输入发动机号");
				return;
			}

			if (UIUtils.isEmpty(driverLicense_entity.getRegisteDate())) {
				ToastUtils.showToast("请选择注册日期");
				return;
			}

			if (UIUtils.isEmpty(driverLicense_entity.getLicenseEndTime())) {
				ToastUtils.showToast("请选择年检有效期");
				return;
			}

			String registeDate = driverLicense_entity.getRegisteDate();
			if (UIUtils.checkDateValidity(registeDate)) {
				ToastUtils.showToast("注册日期无效");
				return;
			}

			String outlineDate = driverLicense_entity.getLicenseEndTime();
			if (!UIUtils.checkDateValidity(outlineDate)) {
				ToastUtils.showToast("车检已过有效期");
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
		private String[] strs = { "非行驶证图片/图片不完整", "行驶证图片上传模糊", "行驶证正本图片未上传", "行驶证副本图片未上传", "行驶证照片拍摄不完整", "车辆超标", "可载人数超标(限7座)", "年检已过有效期",
				"车辆信息不符合平台规则", "其它" };
		private PickerPop pop;

		public void onClick(View v) {
			if (pop == null) {
				pop = new PickerPop(context, strs);
				pop.setOnConfirmListener(new OnConfirmListener() {
					@Override
					public void onConfirmClick(int... results) {
						driving_license_card_verify_view.findViewById(R.id.tv_reject_reason).setVisibility(View.VISIBLE);
						((TextView) driving_license_card_verify_view.findViewById(R.id.tv_reject_reason)).setText(strs[results[0]]);
						UIUtils.scrollToBottome(vg_content);
						context.task_detail_entity.getLicense().getDriverLicense().setRejectCode((300 + 1 + results[0]) + "");
						requestReject();
					}
				});
			}
			pop.show();
		}

	}

	public void requestReject() {
		context.TAG_NO_FIRST_REQUEST = "tag_request_reject";
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("car_id", context.task_detail_entity.getCarId());
		hashMap.put("status", context.task_detail_entity.getLicense().getDriverLicense().getRejectCode());
		new CCHttpEngine(context, NetConstants.NET_ID_DRIVING_LICENSE_INFO_REJECT, hashMap, context.TAG_NO_FIRST_REQUEST, new HttpCallBack() {
			public void onSuccess(ResponseBean responseBean) {
				if (responseBean.getCode() == 0) {
					ToastUtils.showToast("操作成功");
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

	public void requestSave() {
		final CarDetail cardetail_entity = context.task_detail_entity.getCarDetail();
		final DriverLicense driverLicense_entity = context.task_detail_entity.getLicense().getDriverLicense();
		final HashMap<String, String> hashMap = new HashMap<String, String>();
		if (!UIUtils.isEmpty(cardetail_entity.getCarTypeId())) {
			hashMap.put("car_type_id", cardetail_entity.getCarTypeId());
		}

		if (!UIUtils.isEmpty(((TextView) driving_license_card_verify_view.findViewById(R.id.tv_car_plate1)).getText().toString().trim())
				&& !UIUtils.isEmpty(((TextView) driving_license_card_verify_view.findViewById(R.id.tv_car_plate2)).getText().toString().trim())
				&& !UIUtils.isEmpty(((TextView) driving_license_card_verify_view.findViewById(R.id.tv_car_plate3)).getText().toString().trim())) {
			hashMap.put("car_number", ((TextView) driving_license_card_verify_view.findViewById(R.id.tv_car_plate1)).getText().toString().trim()
					+ ((TextView) driving_license_card_verify_view.findViewById(R.id.tv_car_plate2)).getText().toString().trim()
					+ ((TextView) driving_license_card_verify_view.findViewById(R.id.tv_car_plate3)).getText().toString().trim());
		}

		if (!UIUtils.isEmpty(((EditText) driving_license_card_verify_view.findViewById(R.id.et_name)).getText().toString().trim())) {
			hashMap.put("c_license_username", ((EditText) driving_license_card_verify_view.findViewById(R.id.et_name)).getText().toString().trim());
		}
		if (!UIUtils.isEmpty(driverLicense_entity.getOwnerType())) {// 所有人性质；
			hashMap.put("car_owner_type", driverLicense_entity.getOwnerType());
		}

		if (!UIUtils.isEmpty(driverLicense_entity.getUseType())) {
			hashMap.put("car_type", driverLicense_entity.getUseType());
		}

		if (!UIUtils.isEmpty(driverLicense_entity.getCapacity())) {
			hashMap.put("capacity", driverLicense_entity.getCapacity());
		}

		if (!UIUtils.isEmpty(((EditText) (driving_license_card_verify_view.findViewById(R.id.et_che_liang_shibie_daihao))).getText().toString()
				.trim())) {
			hashMap.put("code", ((EditText) (driving_license_card_verify_view.findViewById(R.id.et_che_liang_shibie_daihao))).getText().toString()
					.trim());
		}

		if (!UIUtils.isEmpty(((EditText) (driving_license_card_verify_view.findViewById(R.id.et_engine_num))).getText().toString().trim())) {
			hashMap.put("engine_number", ((EditText) (driving_license_card_verify_view.findViewById(R.id.et_engine_num))).getText().toString().trim());
		}

		if (!UIUtils.isEmpty(driverLicense_entity.getRegisteDate())) {
			hashMap.put("license_register_date", driverLicense_entity.getRegisteDate());
		}

		if (!UIUtils.isEmpty(driverLicense_entity.getLicenseEndTime())) {
			hashMap.put("license_end_time", driverLicense_entity.getLicenseEndTime());
		}

		if (hashMap.size() == 0) {
			ToastUtils.showToast("暂无信息需要保存");
			return;
		}
		context.TAG_NO_FIRST_REQUEST = "tag_request_save";
		hashMap.put("car_id", context.task_detail_entity.getCarId());

		new CCHttpEngine(context, NetConstants.NET_ID_DRIVING_LICENSE_INFO_PASS_SAVE, hashMap, context.TAG_NO_FIRST_REQUEST, new HttpCallBack() {

			@Override
			public void onSuccess(ResponseBean responseBean) {
				if (responseBean.getCode() == 0) {
					ToastUtils.showToast("保存成功");
					driving_license_card_verify_view.findViewById(R.id.tv_reject_reason).setVisibility(View.GONE);
					cardetail_entity.setCarPlateNo(hashMap.get("car_number"));
					driverLicense_entity.setCode(hashMap.get("code"));
					driverLicense_entity.setEngerNumber(hashMap.get("engine_number"));
					driverLicense_entity.setOwnerName(hashMap.get("c_license_username"));
					if ("3".equals(context.task_detail_entity.getLicense().getDriverLicense().getStatus())
							|| "2".equals(context.task_detail_entity.getLicense().getDriverLicense().getStatus())) {
						context.task_detail_entity.getLicense().getDriverLicense().setStatus("0");
					}

					if (!UIUtils.isEmpty(car_brand_name) && !UIUtils.isEmpty(car_type_name)) {
						context.task_detail_entity.getCarDetail().setCarName(car_brand_name + car_type_name);
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
		final DriverLicense entity = context.task_detail_entity.getLicense().getDriverLicense();
		final HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("car_id", context.task_detail_entity.getCarId());
		hashMap.put("car_number", ((TextView) driving_license_card_verify_view.findViewById(R.id.tv_car_plate1)).getText().toString().trim()
				+ ((TextView) driving_license_card_verify_view.findViewById(R.id.tv_car_plate2)).getText().toString().trim()
				+ ((TextView) driving_license_card_verify_view.findViewById(R.id.tv_car_plate3)).getText().toString().trim());
		hashMap.put("code", ((EditText) (driving_license_card_verify_view.findViewById(R.id.et_che_liang_shibie_daihao))).getText().toString().trim());
		hashMap.put("capacity", entity.getCapacity());
		hashMap.put("engine_number", ((EditText) (driving_license_card_verify_view.findViewById(R.id.et_engine_num))).getText().toString().trim());
		hashMap.put("license_register_date", entity.getRegisteDate());
		hashMap.put("car_type", entity.getUseType());
		hashMap.put("car_owner_type", entity.getOwnerType());
		hashMap.put("car_type_id", context.task_detail_entity.getCarDetail().getCarTypeId());
		hashMap.put("license_end_time", entity.getLicenseEndTime());
		hashMap.put("c_license_username", ((EditText) driving_license_card_verify_view.findViewById(R.id.et_name)).getText().toString().trim());
		hashMap.put("audit", "1");
		new CCHttpEngine(context, NetConstants.NET_ID_DRIVING_LICENSE_INFO_PASS_SAVE, hashMap, context.TAG_NO_FIRST_REQUEST, new HttpCallBack() {
			public void onSuccess(ResponseBean responseBean) {
				if (responseBean.getCode() == 0) {
					ToastUtils.showToast("审核通过");
					driving_license_card_verify_view.findViewById(R.id.tv_reject_reason).setVisibility(View.GONE);
					context.task_detail_entity.getCarDetail().setCarPlateNo(hashMap.get("car_number"));
					entity.setCode(hashMap.get("code"));
					entity.setEngerNumber(hashMap.get("engine_number"));
					entity.setOwnerName(hashMap.get("c_license_username"));
					context.task_detail_entity.getLicense().getDriverLicense().setStatus("3");

					if (!UIUtils.isEmpty(car_brand_name) && !UIUtils.isEmpty(car_type_name)) {
						context.task_detail_entity.getCarDetail().setCarName(car_brand_name + car_type_name);
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

	private void getCarPrice(TaskDetail entity) {
		if (!UIUtils.isEmpty(entity.getCarDetail().getCarTypeId())) {
			HashMap<String, String> hashMap = new HashMap<String, String>();
			hashMap.put("carTypeId", entity.getCarDetail().getCarTypeId());
			new CCHttpEngine(context, NetConstants.NET_ID_GETCAR_PRICE, hashMap, null, new HttpCallBack() {
				public void onSuccess(ResponseBean responseBean) {
					if (responseBean.getCode() == 0) {
						CarPrice price = (CarPrice) responseBean.getData();
						((TextView) driving_license_card_verify_view.findViewById(R.id.tv_car_price)).setText(price.getPrice());
					}
				}

				@Override
				public void onNetUnavailable(String net_unAvailabel) {

				}

				@Override
				public void onFailure(IOException e) {

				}
			}).executeTask();
		}

	}

	private void getPetrolDayPrice(String car_type_id) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("car_type_id", car_type_id);
		new CCHttpEngine(context, NetConstants.NET_ID_GET_PETROL_CONSUME, hashMap, null, new HttpCallBack() {

			@Override
			public void onSuccess(ResponseBean responseBean) {
				if (responseBean.getCode() == 0) {
					PetrolConsume entity = (PetrolConsume) responseBean.getData();
					context.task_detail_entity.getRentMoney().setDay(entity.getRentDay());
					context.task_detail_entity.getGas().setGasConsume(entity.getPetrolConsume());
				}
			}

			@Override
			public void onNetUnavailable(String net_unAvailabel) {
			}

			@Override
			public void onFailure(IOException e) {
			}
		}).executeTask();

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == TaskDetailActivity.REQUEST_CODE_SELECT_CAR_BRAND && resultCode == Activity.RESULT_OK) {
			String car_type_id = data.getStringExtra(TaskDetailActivity.INTENT_EXTRA_NAME_CAR_TYPE_ID);
			car_brand_name = data.getStringExtra(TaskDetailActivity.INTENT_EXTRA_NAME_CAR_BRAND_NAME);
			car_type_name = data.getStringExtra(TaskDetailActivity.INTENT_EXTRA_NAME_CAR_TYPE_NAME);
			context.task_detail_entity.getCarDetail().setCarTypeId(car_type_id);
			context.task_detail_entity.getCarDetail().setCarName(car_brand_name + car_type_name);
			getCarPrice(context.task_detail_entity);
			getPetrolDayPrice(car_type_id);
			((TextView) driving_license_card_verify_view.findViewById(R.id.tv_car_brand)).setText(car_brand_name + car_type_name);
		}
	}

	public void onUploadPicResult(String uri) {
		if (PhotoViewClickListener.CAR_PHOTO_VIEW_ID == R.id.iv_driving_license_page) {
			context.task_detail_entity.getLicense().getDriverLicense().setLicenseImage(uri);
		}
		else if (PhotoViewClickListener.CAR_PHOTO_VIEW_ID == R.id.iv_driving_license_vice_page) {
			context.task_detail_entity.getLicense().getDriverLicense().setLicenseBackImage(uri);
		}

		context.task_detail_entity.getLicense().getDriverLicense().setStatus("0");
		context.initCardsCheckStatus();

	}

}
