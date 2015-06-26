package com.csd.android.model;

import java.io.Serializable;
import java.util.ArrayList;

public class CarBrand implements Serializable {
	private String brandId;
	private String brandName;
	private String letter;
	private ArrayList<CarType> carType;

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getLetter() {
		return letter;
	}

	public void setLetter(String letter) {
		this.letter = letter;
	}

	public ArrayList<CarType> getCarType() {
		return carType;
	}

	public void setCarType(ArrayList<CarType> carType) {
		this.carType = carType;
	}

	public class CarType {
		private String typeId;
		private String typeName;

		public String getTypeId() {
			return typeId;
		}

		public void setTypeId(String typeId) {
			this.typeId = typeId;
		}

		public String getTypeName() {
			return typeName;
		}

		public void setTypeName(String typeName) {
			this.typeName = typeName;
		}

	}

}
