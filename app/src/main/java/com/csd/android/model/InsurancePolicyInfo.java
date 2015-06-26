package com.csd.android.model;

import java.io.Serializable;

public class InsurancePolicyInfo implements Serializable {
	private String insuranceImage;
	private String insuranceNumber;
	private String insuranceName;
	private String insuranceStarttime;
	private String insuranceEndtime;
	private String insuranceCompany;
	private String insurancePhone;
	private String status;
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

	public String getInsuranceImage() {
		return insuranceImage;
	}

	public void setInsuranceImage(String insuranceImage) {
		this.insuranceImage = insuranceImage;
	}

	public String getInsuranceNumber() {
		return insuranceNumber;
	}

	public void setInsuranceNumber(String insuranceNumber) {
		this.insuranceNumber = insuranceNumber;
	}

	public String getInsuranceName() {
		return insuranceName;
	}

	public void setInsuranceName(String insuranceName) {
		this.insuranceName = insuranceName;
	}

	public String getInsuranceStarttime() {
		return insuranceStarttime;
	}

	public void setInsuranceStarttime(String insuranceStarttime) {
		this.insuranceStarttime = insuranceStarttime;
	}

	public String getInsuranceEndtime() {
		return insuranceEndtime;
	}

	public void setInsuranceEndtime(String insuranceEndtime) {
		this.insuranceEndtime = insuranceEndtime;
	}

	public String getInsuranceCompany() {
		return insuranceCompany;
	}

	public void setInsuranceCompany(String insuranceCompany) {
		this.insuranceCompany = insuranceCompany;
	}

	public String getInsurancePhone() {
		return insurancePhone;
	}

	public void setInsurancePhone(String insurancePhone) {
		this.insurancePhone = insurancePhone;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
