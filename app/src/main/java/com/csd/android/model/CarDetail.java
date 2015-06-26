package com.csd.android.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class CarDetail implements Serializable {
	private String carPlateNo; //车牌号
	private String changeSpeedType;//0:手动档,1自动挡
	private String carType;//车类型
	private String carTypeId;
	private String carTime;
	private String capacity;
	private String carAge;
	//车颜色 '1' => '黑色','2' => '白色','3' => '金色','4' => '蓝色',
	//'5' => '银色', '6' => '紫色','7' => '红色','8' => '黄色', '9' => '绿色','10' => '棕色',
	private String color;
	private String discharge;
	private String carName;
	
	private int mp3;
	@SerializedName("avigraph")
	private int gps;
	@SerializedName("record")
	private int recorder;
	private int boySeat;
	@SerializedName("fridge")
	private int bluetooth;

	public int getMp3() {
		return mp3;
	}

	public void setMp3(int mp3) {
		this.mp3 = mp3;
	}

	public int getGps() {
		return gps;
	}

	public void setGps(int gps) {
		this.gps = gps;
	}

	public int getRecorder() {
		return recorder;
	}

	public void setRecorder(int recorder) {
		this.recorder = recorder;
	}

	public int getBoySeat() {
		return boySeat;
	}

	public void setBoySeat(int boySeat) {
		this.boySeat = boySeat;
	}

	public int getBluetooth() {
		return bluetooth;
	}

	public void setBluetooth(int bluetooth) {
		this.bluetooth = bluetooth;
	}

	public String getCarName() {
		return carName;
	}

	public void setCarName(String carName) {
		this.carName = carName;
	}

	public String getVehicleColor() {
		switch (Integer.parseInt(color)) {
			case 1:
				return "黑色";
			case 2:
				return "白色";
			case 3:
				return "金色";
			case 4:
				return "蓝色";
			case 5:
				return "银色";
			case 6:
				return "紫色";
			case 7:
				return "红色";
			case 8:
				return "黄色";
			case 9:
				return "绿色";
			case 10:
				return "棕色";
		}
		return null;
	}

	public String getSpeedBox() {
		return "0".equals(changeSpeedType) ? "手动档" : "自动档";
	}

	public String getCarPlateNo() {
		return carPlateNo;
	}

	public void setCarPlateNo(String carPlateNo) {
		this.carPlateNo = carPlateNo;
	}

	public String getChangeSpeedType() {
		return changeSpeedType;
	}

	public void setChangeSpeedType(String changeSpeedType) {
		this.changeSpeedType = changeSpeedType;
	}

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	public String getCarTypeId() {
		return carTypeId;
	}

	public void setCarTypeId(String carTypeId) {
		this.carTypeId = carTypeId;
	}

	public String getCarTime() {
		return carTime;
	}

	public void setCarTime(String carTime) {
		this.carTime = carTime;
	}

	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}

	public String getCarAge() {
		return carAge;
	}

	public void setCarAge(String carAge) {
		this.carAge = carAge;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getDischarge() {
		return discharge;
	}

	public void setDischarge(String discharge) {
		this.discharge = discharge;
	}

}
