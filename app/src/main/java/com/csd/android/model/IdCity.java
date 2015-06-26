package com.csd.android.model;

import java.io.Serializable;
import java.util.ArrayList;

public class IdCity implements Serializable {
	private int cityId;
	private String cityName;
	private ArrayList<Area> areaInfo;

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public ArrayList<Area> getAreaInfo() {
		return areaInfo;
	}

	public void setAreaInfo(ArrayList<Area> areaInfo) {
		this.areaInfo = areaInfo;
	}

	public class Area implements Serializable {
		private int areaId;
		private String areaName;

		public int getAreaId() {
			return areaId;
		}

		public void setAreaId(int areaId) {
			this.areaId = areaId;
		}

		public String getAreaName() {
			return areaName;
		}

		public void setAreaName(String areaName) {
			this.areaName = areaName;
		}

	}
}
