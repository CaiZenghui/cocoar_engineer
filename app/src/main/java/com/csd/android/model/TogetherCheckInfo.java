package com.csd.android.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class TogetherCheckInfo implements Serializable {
	@SerializedName("authImage")
	private String image_uri1;// 营业执照；
	@SerializedName("bussinessImage")
	private String image_uri2;
	@SerializedName("ortherOneImage")
	private String image_uri3;
	@SerializedName("ortherTwoImage")
	private String image_uri4;

	private String status;// 两证并审状态；

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getImage_uri1() {
		return image_uri1;
	}

	public void setImage_uri1(String image_uri1) {
		this.image_uri1 = image_uri1;
	}

	public String getImage_uri2() {
		return image_uri2;
	}

	public void setImage_uri2(String image_uri2) {
		this.image_uri2 = image_uri2;
	}

	public String getImage_uri3() {
		return image_uri3;
	}

	public void setImage_uri3(String image_uri3) {
		this.image_uri3 = image_uri3;
	}

	public String getImage_uri4() {
		return image_uri4;
	}

	public void setImage_uri4(String image_uri4) {
		this.image_uri4 = image_uri4;
	}
}
