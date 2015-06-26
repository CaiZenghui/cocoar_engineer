package com.csd.android.model;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class Box implements Serializable {
	private String name;
	private int number;
	private int need_install;
	private ArrayList<String> position;
	private int box_type;//盒子类型 1cocar1 2 cocar2 3cocar3
	private ArrayList<BoxDetail> list;
	public ArrayList<BoxDetail> need_install_list = new ArrayList<BoxDetail>();

	public int getNeed_install() {
		return need_install;
	}

	public void setNeed_install(int need_install) {
		this.need_install = need_install;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public ArrayList<String> getPosition() {
		return position;
	}

	public void setPosition(ArrayList<String> position) {
		this.position = position;
	}

	public int getBox_type() {
		return box_type;
	}

	public void setBox_type(int box_type) {
		this.box_type = box_type;
	}

	public ArrayList<BoxDetail> getList() {
		return list;
	}

	public void setList(ArrayList<BoxDetail> list) {
		this.list = list;
	}

	public class BoxDetail {
		private String old_gps_id;
		private String gps_id;
		private String note;
		private String position;
		@SerializedName("position_desc")
		private String other_position;
		private String name;
		private int box_type;
		private ArrayList<String> positions;
		private int index = -1;
		private boolean hasFinish = false;// 标记该盒子对应的处理操作是否完成；
		private boolean valid;
		private int boxStatus=-1;//0初始:新装正常 1正常：检修正常 2：异常;

		public int getBoxStatus() {
			return boxStatus;
		}

		public void setBoxStatus(int boxStatus) {
			this.boxStatus = boxStatus;
		}

		public boolean isValid() {
			return valid;
		}

		public void setValid(boolean valid) {
			this.valid = valid;
		}

		public boolean isHasFinish() {
			return hasFinish;
		}

		public void setHasFinish(boolean hasFinish) {
			this.hasFinish = hasFinish;
		}

		public String getOld_gps_id() {
			return old_gps_id;
		}

		public void setOld_gps_id(String old_gps_id) {
			this.old_gps_id = old_gps_id;
		}

		public String getOther_position() {
			return other_position;
		}

		public void setOther_position(String other_position) {
			this.other_position = other_position;
		}

		public int getBox_type() {
			return box_type;
		}

		public void setBox_type(int box_type) {
			this.box_type = box_type;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public ArrayList<String> getPositions() {
			return positions;
		}

		public void setPositions(ArrayList<String> positions) {
			this.positions = positions;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getGps_id() {
			return gps_id;
		}

		public void setGps_id(String gps_id) {
			this.gps_id = gps_id;
		}

		public String getNote() {
			return note;
		}

		public void setNote(String note) {
			this.note = note;
		}

		public String getPosition() {
			return position;
		}

		public void setPosition(String position) {
			this.position = position;
		}

	}

}
