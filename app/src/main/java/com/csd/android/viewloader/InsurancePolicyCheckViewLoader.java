package com.csd.android.viewloader;

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
import com.csd.android.activity.TaskDetailActivity;
import com.csd.android.model.InsurancePolicyInfo;
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

public class InsurancePolicyCheckViewLoader {
	private TaskDetailActivity context;
	private View insurance_policy_check_view;
	private ScrollView vg_content;

	public InsurancePolicyCheckViewLoader(TaskDetailActivity context, ScrollView vg_content) {
		this.context = context;
		this.vg_content = vg_content;
	}

	public void load() {
		final InsurancePolicyInfo entity = context.task_detail_entity.getLicense().getPolicyInfo();
		initView(entity);
		initUiData(entity);
		loadImage(entity);
	}

	private void initView(final InsurancePolicyInfo entity) {
		if (insurance_policy_check_view == null) {
			insurance_policy_check_view = LayoutInflater.from(context).inflate(R.layout.layout_insurance_policy_check, null);
			insurance_policy_check_view.findViewById(R.id.vg_date_out_line).setOnClickListener(new DeadLineClickListener());
			insurance_policy_check_view.findViewById(R.id.vg_company_name).setOnClickListener(new CompanyClickListener());
			insurance_policy_check_view.findViewById(R.id.vg_pass).setOnClickListener(new PassClickListener());
			insurance_policy_check_view.findViewById(R.id.btn_save).setOnClickListener(new SaveClickListener());
			insurance_policy_check_view.findViewById(R.id.vg_reject).setOnClickListener(new RejectClickListener());

			if (!UIUtils.isEmpty(context.task_detail_entity.getLicense().getPolicyInfo().getFailedNote())) {
				TextView tv_reject_reason = (TextView) insurance_policy_check_view.findViewById(R.id.tv_reject_reason);
				tv_reject_reason.setVisibility(View.VISIBLE);
				tv_reject_reason.setText(context.task_detail_entity.getLicense().getPolicyInfo().getFailedNote());
			}
		}
		vg_content.addView(insurance_policy_check_view);
	}

	private void loadImage(InsurancePolicyInfo entity) {
		if (!UIUtils.isEmpty(entity.getInsuranceImage())) {
			Glide.with(context).load(Uri.parse(entity.getInsuranceImage())).placeholder(R.mipmap.icon_pic_loading)
					.error(R.mipmap.icon_pic_load_fail).listener(new ImageLoaderListener(context, Uri.parse(entity.getInsuranceImage())))
					.into((ImageView) insurance_policy_check_view.findViewById(R.id.iv_insurance));
		}
		else {
			Glide.with(context).load(R.mipmap.icon_insurance).listener(new ImageLoaderListener(context, null))
					.into((ImageView) insurance_policy_check_view.findViewById(R.id.iv_insurance));
		}
	}

	private void initUiData(InsurancePolicyInfo entity) {
		((EditText) insurance_policy_check_view.findViewById(R.id.et_insurance_num)).setText(entity.getInsuranceNumber());
		((EditText) insurance_policy_check_view.findViewById(R.id.et_insurance_name)).setText(entity.getInsuranceName());
		((TextView) insurance_policy_check_view.findViewById(R.id.et_car_identify_num)).setText(context.task_detail_entity.getLicense()
				.getDriverLicense().getCode());
		((TextView) insurance_policy_check_view.findViewById(R.id.tv_dead_line)).setText(entity.getInsuranceEndtime());
		((TextView) insurance_policy_check_view.findViewById(R.id.tv_company_name)).setText(entity.getInsuranceCompany());
		((EditText) insurance_policy_check_view.findViewById(R.id.et_service_num)).setText(entity.getInsurancePhone());
	}

	class DeadLineClickListener implements OnClickListener {
		private DatePickerPop pickerPop;

		public void onClick(View v) {
			if (pickerPop == null) {
				pickerPop = new DatePickerPop(context);
				pickerPop.setOnConfirmListener(new DatePickerPop.OnConfirmListener() {
					public void onConfirmClick(String... results) {
						((TextView) insurance_policy_check_view.findViewById(R.id.tv_dead_line)).setText(results[0] + "-" + results[1] + "-"
								+ results[2]);
						context.task_detail_entity.getLicense().getPolicyInfo().setInsuranceEndtime(results[0] + "-" + results[1] + "-" + results[2]);
					}
				});
			}
			pickerPop.show();
		}

	}

	class CompanyClickListener implements OnClickListener {
		private String[] strs = CCApplication.getApplication().getResources().getStringArray(R.array.insuranceCompanys);
		private PickerPop pop;

		@Override
		public void onClick(View v) {
			if (pop == null) {
				pop = new PickerPop(context, strs);
				pop.setOnConfirmListener(new OnConfirmListener() {
					@Override
					public void onConfirmClick(int... index) {
						InsurancePolicyInfo entity = context.task_detail_entity.getLicense().getPolicyInfo();
						if (index[0] == strs.length - 1) {
							((TextView) insurance_policy_check_view.findViewById(R.id.tv_company_name)).setText(strs[index[0]]);
							insurance_policy_check_view.findViewById(R.id.et_other_company_name).setVisibility(View.VISIBLE);
							insurance_policy_check_view.findViewById(R.id.indicator_other_company_name).setVisibility(View.VISIBLE);
							((EditText) insurance_policy_check_view.findViewById(R.id.et_service_num)).setText(null);
							entity.setInsuranceCompany(null);
							entity.setInsurancePhone(null);
						}
						else {
							insurance_policy_check_view.findViewById(R.id.et_other_company_name).setVisibility(View.GONE);
							insurance_policy_check_view.findViewById(R.id.indicator_other_company_name).setVisibility(View.GONE);
							((TextView) insurance_policy_check_view.findViewById(R.id.tv_company_name)).setText(strs[index[0]]);
							((EditText) insurance_policy_check_view.findViewById(R.id.et_service_num)).setText(CCApplication.getApplication()
									.getResources().getStringArray(R.array.insurancephones)[index[0]]);
							entity.setInsuranceCompany(strs[index[0]]);
							entity.setInsurancePhone(CCApplication.getApplication().getResources().getStringArray(R.array.insurancephones)[index[0]]);
						}

					}
				});
			}
			pop.show();
		}
	}

	class PassClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			InsurancePolicyInfo entity = context.task_detail_entity.getLicense().getPolicyInfo();
			if (UIUtils.isEmpty(entity.getInsuranceImage())) {
				ToastUtils.showToast("未上传保单照片");
				return;
			}

			if (UIUtils.isEmpty(((EditText) insurance_policy_check_view.findViewById(R.id.et_insurance_num)).getText().toString().trim())) {
				ToastUtils.showToast("请输入保险单号");
				return;
			}

			if (UIUtils.isEmpty(((EditText) insurance_policy_check_view.findViewById(R.id.et_insurance_name)).getText().toString().trim())) {
				ToastUtils.showToast("请输入被保人姓名");
				return;
			}

			if (UIUtils.isEmpty(entity.getInsuranceEndtime())) {
				ToastUtils.showToast("请选择保单有效期");
				return;
			}

			if (!UIUtils.checkDateValidity(entity.getInsuranceEndtime())) {
				ToastUtils.showToast("保单已过有效期");
				return;
			}

			if (UIUtils.isEmpty(entity.getInsuranceCompany())
					&& UIUtils.isEmpty(((EditText) insurance_policy_check_view.findViewById(R.id.et_other_company_name)).getText().toString().trim())) {
				ToastUtils.showToast("请完善保险公司名称");
				return;
			}

			if (UIUtils.isEmpty(((EditText) insurance_policy_check_view.findViewById(R.id.et_service_num)).getText().toString().trim())) {
				ToastUtils.showToast("请输入保险公司服务电话");
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
		private String[] strs = { "非保单图片", "保单不清楚", "保单已过有效期", "保单和车辆不匹配", "其它" };
		private PickerPop pop;

		public void onClick(View v) {
			if (pop == null) {
				pop = new PickerPop(context, strs);
				pop.setOnConfirmListener(new OnConfirmListener() {
					@Override
					public void onConfirmClick(int... results) {
						insurance_policy_check_view.findViewById(R.id.tv_reject_reason).setVisibility(View.VISIBLE);
						((TextView) insurance_policy_check_view.findViewById(R.id.tv_reject_reason)).setText(strs[results[0]]);
						UIUtils.scrollToBottome(vg_content);
						context.task_detail_entity.getLicense().getPolicyInfo().setRejectCode((500 + 1 + results[0]) + "");
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
		hashMap.put("status", context.task_detail_entity.getLicense().getPolicyInfo().getRejectCode());
		new CCHttpEngine(context, NetConstants.NET_ID_INSURANCE_CHECK_REJECT, hashMap, context.TAG_NO_FIRST_REQUEST, new HttpCallBack() {
			public void onSuccess(ResponseBean responseBean) {
				if (responseBean.getCode() == 0) {
					ToastUtils.showToast("操作成功");
					context.task_detail_entity.getLicense().getPolicyInfo().setStatus("2");
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
		final InsurancePolicyInfo entity = context.task_detail_entity.getLicense().getPolicyInfo();
		final HashMap<String, String> hashMap = new HashMap<String, String>();
		if (!UIUtils.isEmpty(((EditText) insurance_policy_check_view.findViewById(R.id.et_insurance_num)).getText().toString().trim())) {
			hashMap.put("number", ((EditText) insurance_policy_check_view.findViewById(R.id.et_insurance_num)).getText().toString().trim());
		}
		if (!UIUtils.isEmpty(((EditText) insurance_policy_check_view.findViewById(R.id.et_insurance_name)).getText().toString().trim())) {
			hashMap.put("username", ((EditText) insurance_policy_check_view.findViewById(R.id.et_insurance_name)).getText().toString().trim());
		}
		if (!UIUtils.isEmpty(entity.getInsuranceEndtime())) {
			hashMap.put("end_time", entity.getInsuranceEndtime());
		}

		if (!UIUtils.isEmpty(entity.getInsuranceCompany())
				|| !UIUtils.isEmpty(((EditText) insurance_policy_check_view.findViewById(R.id.et_other_company_name)).getText().toString().trim())) {
			hashMap.put(
					"company",
					entity.getInsuranceCompany() != null ? entity.getInsuranceCompany() : ((EditText) insurance_policy_check_view
							.findViewById(R.id.et_other_company_name)).getText().toString().trim());
		}

		if (!UIUtils.isEmpty(((EditText) insurance_policy_check_view.findViewById(R.id.et_service_num)).getText().toString().trim())) {
			hashMap.put("phone", ((EditText) insurance_policy_check_view.findViewById(R.id.et_service_num)).getText().toString().trim());
		}
		if (hashMap.size() == 0) {
			ToastUtils.showToast("暂无信息需要保存");
			return;
		}
		context.TAG_NO_FIRST_REQUEST = "tag_request_save";
		hashMap.put("car_id", context.task_detail_entity.getCarId());

		new CCHttpEngine(context, NetConstants.NET_ID_INSURANCE_CHECK_PASS_SAVE, hashMap, context.TAG_NO_FIRST_REQUEST, new HttpCallBack() {
			public void onSuccess(ResponseBean responseBean) {
				if (responseBean.getCode() == 0) {
					ToastUtils.showToast("保存成功");
					insurance_policy_check_view.findViewById(R.id.tv_reject_reason).setVisibility(View.GONE);
					entity.setInsuranceCompany(hashMap.get("company"));
					entity.setInsuranceName(hashMap.get("username"));
					entity.setInsurancePhone(hashMap.get("phone"));
					entity.setInsuranceNumber(hashMap.get("number"));
					if ("2".equals(context.task_detail_entity.getLicense().getPolicyInfo().getStatus())
							|| "3".equals(context.task_detail_entity.getLicense().getPolicyInfo().getStatus())) {
						context.task_detail_entity.getLicense().getPolicyInfo().setStatus("0");
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
		final InsurancePolicyInfo entity = context.task_detail_entity.getLicense().getPolicyInfo();
		final HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("car_id", context.task_detail_entity.getCarId());
		hashMap.put(
				"company",
				entity.getInsuranceCompany() != null ? entity.getInsuranceCompany() : ((EditText) insurance_policy_check_view
						.findViewById(R.id.et_other_company_name)).getText().toString().trim());
		hashMap.put("end_time", entity.getInsuranceEndtime());
		hashMap.put("username", ((EditText) insurance_policy_check_view.findViewById(R.id.et_insurance_name)).getText().toString().trim());
		hashMap.put("number", ((EditText) insurance_policy_check_view.findViewById(R.id.et_insurance_num)).getText().toString().trim());
		hashMap.put("phone", ((EditText) insurance_policy_check_view.findViewById(R.id.et_service_num)).getText().toString().trim());
		hashMap.put("audit", "1");
		new CCHttpEngine(context, NetConstants.NET_ID_INSURANCE_CHECK_PASS_SAVE, hashMap, context.TAG_NO_FIRST_REQUEST, new HttpCallBack() {
			public void onSuccess(ResponseBean responseBean) {
				if (responseBean.getCode() == 0) {
					ToastUtils.showToast("审核通过");
					insurance_policy_check_view.findViewById(R.id.tv_reject_reason).setVisibility(View.GONE);
					entity.setInsuranceCompany(hashMap.get("company"));
					entity.setInsuranceName(hashMap.get("username"));
					entity.setInsurancePhone(hashMap.get("phone"));
					entity.setInsuranceNumber(hashMap.get("number"));
					context.task_detail_entity.getLicense().getPolicyInfo().setStatus("3");
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
		if (PhotoViewClickListener.CAR_PHOTO_VIEW_ID == R.id.iv_insurance) {
			context.task_detail_entity.getLicense().getPolicyInfo().setInsuranceImage(uri);
		}
		context.task_detail_entity.getLicense().getPolicyInfo().setStatus("0");
		context.initCardsCheckStatus();
	}

}
