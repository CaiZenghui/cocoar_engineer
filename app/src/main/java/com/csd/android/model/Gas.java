package com.csd.android.model;

import java.io.Serializable;

public class Gas implements Serializable {
	private String gasType;// 汽油型号0:90号汽油1:92号汽油2:95号汽油3:柴油
	private String gasConsume;//每百里耗数(L/百里)

	public String getPetrolType() {
		if ("0".equals(gasType)) {
			return "90号汽油";
		}
		else if ("1".equals(gasType)) {
			return "92号汽油";
		}
		else if ("2".equals(gasType)) {
			return "95号汽油";
		}
		else if ("3".equals(gasType)) {
			return "柴油";
		}
		else {
			return null;
		}
	}

	public String getGasType() {
		return gasType;
	}

	public void setGasType(String gasType) {
		this.gasType = gasType;
	}

	public String getGasConsume() {
		return gasConsume;
	}

	public void setGasConsume(String gasConsume) {
		this.gasConsume = gasConsume;
	}

}
