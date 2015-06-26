package com.csd.android.model;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.csd.android.R;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TaskListEntity implements Serializable {
	private int id;
	private String carId;
	@SerializedName("loginName")
	private String phone;
	private String nickName;
	private String carNumber;
	@SerializedName("startTime")
	private String assignDate;
	private String adminName;
	@SerializedName("assignStatus")
	private String taskStatus;//派工单状态：1待派单，2已派单，3安装中，4未完成,5已完成，6未成功，7已成功，-1已取消（必须）
	@SerializedName("installAddress")
	private String address;
	private String taskType;// 任务类型：1安装，2加装，3检修，4回收
	private int licenseStatus;//证件状态 0未上传 1通过 2 待审核
	private int carStatus;//车辆信息1已保存 0未保存
	private int carboxStatus;//盒子绑定状态 1已绑定 0 未绑定
	private int emergencyStatus;//是否紧急派工单 1是 0否
	private int isRead;//是否已读 0：未读 1：已读
	@SerializedName("getCarStartTime")
	private String useCarTime;
	private String assignId;

	public String getAssignId() {
		return assignId;
	}

	public void setAssignId(String assignId) {
		this.assignId = assignId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getEmergencyStatus() {
		return emergencyStatus;
	}

	public void setEmergencyStatus(int emergencyStatus) {
		this.emergencyStatus = emergencyStatus;
	}

	public int getIsRead() {
		return isRead;
	}

	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}

	public String getUseCarTime() {
		return useCarTime;
	}

	public void setUseCarTime(String useCarTime) {
		this.useCarTime = useCarTime;
	}

	private String getTaskTypeName() {
		String type = null;
		switch (Integer.parseInt(taskType)) {
			case 1:
				type = "安装";
				break;
			case 2:
				type = "加装";
				break;
			case 3:
				type = "检修";
				break;
			case 4:
				type = "回收";
				break;

		}
		return type;
	}

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getCarNumber() {
		return carNumber;
	}

	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}

	public String getAssignDate() {
		return assignDate;
	}

	public void setAssignDate(String assignDate) {
		this.assignDate = assignDate;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public int getLicenseStatus() {
		return licenseStatus;
	}

	public void setLicenseStatus(int licenseStatus) {
		this.licenseStatus = licenseStatus;
	}

	public int getCarStatus() {
		return carStatus;
	}

	public void setCarStatus(int carStatus) {
		this.carStatus = carStatus;
	}

	public int getCarboxStatus() {
		return carboxStatus;
	}

	public void setCarboxStatus(int carboxStatus) {
		this.carboxStatus = carboxStatus;
	}

	private String getTaskStatusName() {
		//1待派单，2已派单，3安装中，4未完成,5已完成，6未成功，7已成功，-1已取消（必须）
		String status = null;
		switch (Integer.parseInt(taskStatus)) {
			case -1:
				status = "已取消";
				break;
			case 1:
				status = "待派单";
				break;
			case 2:
				status = "已派单";
				break;
			case 3:
				status = "安装中";
				break;

			case 4:
				status = "未完成";
				break;

			case 5:
				status = "已完成";
				break;

			case 6:
				status = "未成功";
				break;

			case 7:
				status = "已成功";
				break;
		}

		return status;

	}

	/**
	 * 当“派工单”状态为“已成功”或者派工单任务类型为“回收”类型时，不允许其在进入详细页面编辑
	 * @return
	 */
	public boolean isClickEnable() {
		if (Integer.parseInt(taskStatus) == 7) {//任务类型：1安装，2加装，3检修，4回收
			return false;
		}
		return true;
	}

	public void setTaskBrand_TaskStatus(TextView tv_car_brand_task_status) {
		String target = "车牌：" + getCarNumber() + "      " + getTaskStatusName();
		SpannableString ss = new SpannableString(target);
		ss.setSpan(new ForegroundColorSpan(0xff00b9ff), target.indexOf(getTaskStatusName()), target.indexOf(getTaskStatusName())
				+ getTaskStatusName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv_car_brand_task_status.setText(ss);

	}

	public void setTaskType_Adm(TextView tv_task_type_engineer_name) {
		String target = "任务类型：" + getTaskTypeName() + "    " + "派单人：" + getAdminName();
		SpannableString ss = new SpannableString(target);
		ss.setSpan(new ForegroundColorSpan(0xff00b9ff), target.indexOf("任务类型：" + getTaskTypeName()), target.indexOf("任务类型：" + getTaskTypeName())
				+ ("任务类型：" + getTaskTypeName()).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv_task_type_engineer_name.setText(ss);
	}

//	private String licenseStatus;//证件状态 0未上传 1通过 2 待审核
//	private String carStatus;//车辆信息1已保存 0未保存
//	private String carboxStatus;//盒子绑定状态 1已绑定 0 未绑定
//	private int emergencyStatus;//是否紧急派工单 1是 0否
	public void setTaskProcessStatus(Button btn_submit, TextView tv_use_car_time, ViewGroup vg_task_emergency_status, TextView tv_cards_status,
			TextView tv_car_info_status, TextView tv_box_bind_status) {
		if (Integer.parseInt(taskStatus) == 5) {
			btn_submit.setVisibility(View.GONE);
		}
		else {
			btn_submit.setVisibility(View.VISIBLE);
		}
		if (licenseStatus == 1 && carStatus == 1 && carboxStatus == 1) {
			btn_submit.setEnabled(true);
		}
		else {
			btn_submit.setEnabled(false);
		}

		if (emergencyStatus == 0) {
			tv_use_car_time.setVisibility(View.GONE);
			vg_task_emergency_status.setBackgroundResource(R.drawable.vg_bg_black_stroke_gray_content);
		}
		else {
			tv_use_car_time.setVisibility(View.VISIBLE);
			tv_use_car_time.setText("取车时间：" + useCarTime);
			vg_task_emergency_status.setBackgroundResource(R.drawable.vg_bg_black_stroke_pink_content);
		}
		if (licenseStatus == 0) {
			tv_cards_status.setBackgroundResource(R.mipmap.bg_task_status_4);
		}
		else if (licenseStatus == 1) {
			tv_cards_status.setBackgroundResource(R.mipmap.bg_task_status_3);
		}
		else if (licenseStatus == 2) {
			tv_cards_status.setBackgroundResource(R.mipmap.bg_task_status_0);
		}

		if (carStatus == 0) {
			tv_car_info_status.setBackgroundResource(R.mipmap.bg_task_status_4);
		}
		else {
			tv_car_info_status.setBackgroundResource(R.mipmap.bg_task_status_3);
		}

		if (carboxStatus == 1) {
			tv_box_bind_status.setBackgroundResource(R.mipmap.bg_task_status_3);
		}
		else {
			tv_box_bind_status.setBackgroundResource(R.mipmap.bg_task_status_4);
		}

	}

}
