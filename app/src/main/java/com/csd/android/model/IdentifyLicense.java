package com.csd.android.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class IdentifyLicense implements Serializable {

	private String identifyImage;
	private String identifyBackImage;
	private String username;
	private String cardNumber;
	private String address;
	private String status;//身份证图片验证（非审核）状态 -1:身份证未上传,0:待审核,1:未通过,2:通过
	private String identityNumberStatus;// 身份信息是否验证通过；默认：-1；待验证：0；验证中：1；未通过：2；通过：3；
	@SerializedName("city")
	private String province = "北京";
	@SerializedName("area")
	private String city = "中南海";
	@SerializedName("certificateEndTime")
	private String deadLine = "2050-12-12";
	private String rejectCode;
	private String failedNote;

	public String getFailedNote() {
		return failedNote;
	}

	public void setFailedNote(String failedNote) {
		this.failedNote = failedNote;
	}

	public String getRejectCode() {
		return rejectCode;
	}

	public void setRejectCode(String rejectCode) {
		this.rejectCode = rejectCode;
	}

	public String getDeadLine() {
		return deadLine;
	}

	public void setDeadLine(String deadLine) {
		this.deadLine = deadLine;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getIdentityNumberStatus() {
		return identityNumberStatus;
	}

	public void setIdentityNumberStatus(String identityInfoStatus) {
		this.identityNumberStatus = identityInfoStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String identifyStatus) {
		this.status = identifyStatus;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIdentifyImage() {
		return identifyImage;
	}

	public void setIdentifyImage(String identifyImage) {
		this.identifyImage = identifyImage;
	}

	public String getIdentifyBackImage() {
		return identifyBackImage;
	}

	public void setIdentifyBackImage(String identifyBackImage) {
		this.identifyBackImage = identifyBackImage;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

}
