package com.csd.android.model;

import java.io.Serializable;

public class PetrolConsume implements Serializable {
	private String petrolConsume;
	private String rentDay;

	public String getPetrolConsume() {
		return petrolConsume;
	}

	public void setPetrolConsume(String petrolConsume) {
		this.petrolConsume = petrolConsume;
	}

	public String getRentDay() {
		return rentDay;
	}

	public void setRentDay(String rentDay) {
		this.rentDay = rentDay;
	}
}
