package com.csd.android.model;

import java.io.Serializable;

public class License implements Serializable {
	private IdentifyLicense identifyLicense;
	private DriverLicense driverLicense;
	private CarImages carImage;
	private InsurancePolicyInfo policyInfo;
	private TogetherCheckInfo otherLicense;

	public TogetherCheckInfo getOtherLicense() {
		return otherLicense;
	}

	public void setOtherLicense(TogetherCheckInfo otherLicense) {
		this.otherLicense = otherLicense;
	}

	public InsurancePolicyInfo getPolicyInfo() {
		return policyInfo;
	}

	public void setPolicyInfo(InsurancePolicyInfo policyInfo) {
		this.policyInfo = policyInfo;
	}

	public CarImages getCarImage() {
		return carImage;
	}

	public void setCarImage(CarImages carImage) {
		this.carImage = carImage;
	}

	public IdentifyLicense getIdentifyLicense() {
		return identifyLicense;
	}

	public void setIdentifyLicense(IdentifyLicense identifyLicense) {
		this.identifyLicense = identifyLicense;
	}

	public DriverLicense getDriverLicense() {
		return driverLicense;
	}

	public void setDriverLicense(DriverLicense driverLicense) {
		this.driverLicense = driverLicense;
	}

}
