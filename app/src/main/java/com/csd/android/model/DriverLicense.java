package com.csd.android.model;

import java.io.Serializable;

public class DriverLicense implements Serializable {
	private String licenseImage;
	private String licenseBackImage;
	private String ownerName;
	private String carNumber;
	private String useType;//0:非营运 1:营运
	private String code;//车辆识别代码
	private String engerNumber;
	private String registeDate;
	private String age;
	private String carType;
	private String capacity;
	private String status;
	private String licenseEndTime;
	private String ownerType;//0:个人,1:公司2,其他
	private String rejectCode;
	private String failedNote;

	public String getFailedNote() {
		return failedNote;
	}

	public void setFailedNote(String failedNote) {
		this.failedNote = failedNote;
	}

	public String getOwnerTypeName() {
		String type = null;
		try {
			switch (Integer.parseInt(ownerType)) {
				case 0:
					type = "个人";
					break;
				case 1:
					type = "租赁公司";
					break;
				case 2:
					type = "其他公司";
					break;

			}
		}
		catch (Exception e) {
			// TODO: handle exception
		}

		return type;
	}

	public String getUseTypeName() {
		String type = null;
		try {
			switch (Integer.parseInt(useType)) {
				case 0:
					type = "非营运";
					break;
				case 1:
					type = "营运";
					break;
			}
		}
		catch (Exception e) {
		}

		return type;
	}

	public String getRejectCode() {
		return rejectCode;
	}

	public void setRejectCode(String rejectCode) {
		this.rejectCode = rejectCode;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public String getLicenseEndTime() {
		return licenseEndTime;
	}

	public void setLicenseEndTime(String licenseEndTime) {
		this.licenseEndTime = licenseEndTime;
	}

	public String getLicenseImage() {
		return licenseImage;
	}

	public void setLicenseImage(String licenseImage) {
		this.licenseImage = licenseImage;
	}

	public String getLicenseBackImage() {
		return licenseBackImage;
	}

	public void setLicenseBackImage(String licenseBackImage) {
		this.licenseBackImage = licenseBackImage;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getCarNumber() {
		return carNumber;
	}

	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}

	public String getUseType() {
		return useType;
	}

	public void setUseType(String useType) {
		this.useType = useType;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getEngerNumber() {
		return engerNumber;
	}

	public void setEngerNumber(String engerNumber) {
		this.engerNumber = engerNumber;
	}

	public String getRegisteDate() {
		return registeDate;
	}

	public void setRegisteDate(String registeDate) {
		this.registeDate = registeDate;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
