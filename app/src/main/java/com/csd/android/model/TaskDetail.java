package com.csd.android.model;

import java.io.Serializable;
import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.csd.android.R;
import com.csd.android.model.Box.BoxDetail;
import com.csd.android.utils.UIUtils;
import com.csd.android.widget.BoxBindItemView;
import com.google.gson.annotations.SerializedName;

public class TaskDetail implements Serializable {

	private String carId;//车辆编号
	private String step;//0:待审核 1:审核不通过2:审核通过 3:已预约 4:已上门未完成 5.安装事项6:交易信息待提交7:待上架8:已上架9:已下
	private CarDetail carDetail;
	private Location location;
	private Gas gas;
	private Owner owner;
	private RentMoney rentMoney;
	private License license;
	@SerializedName("getCarTypeShow")
	private String takeCarTypeName;
	private ArrayList<Box> box;

	private int cheliang_xinxi_info_status;//0:待处理；1:完成处理；标记是否显示红点；
	private int cards_check_status;
	private int bind_box_status;
	private String taskType;//1安装，2加装，3检修，4回收;

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public ArrayList<Box> getBox() {
		return box;
	}

	public void setBox(ArrayList<Box> box) {
		this.box = box;
	}

	public int getCheLiang_XinXi_Info_Status() {
		if (UIUtils.isEmpty(getCarDetail().getCarPlateNo()) || UIUtils.isEmpty(getCarDetail().getCarTypeId())
				|| UIUtils.isEmpty(getCarDetail().getChangeSpeedType()) || UIUtils.isEmpty(getCarDetail().getColor())
				|| UIUtils.isEmpty(getLocation().getAddress()) || UIUtils.isEmpty(getGas().getGasType())) {

			return 0;
		}
		return 1;
	}

	public int getCards_Check_Status() {
		if ("2".equals(getLicense().getIdentifyLicense().getStatus()) && "3".equals(getLicense().getDriverLicense().getStatus())
				&& "3".equals(getLicense().getOtherLicense().getStatus()) && "3".equals(getLicense().getPolicyInfo().getStatus())
				&& "3".equals(getLicense().getCarImage().getStatus())) {
			return 1;

		}
		return 0;
	}

	public int getBind_Box_Status() {
		ArrayList<Box> box = getBox();
		boolean hasFinish = true;
		outer: for (int i = 0; i < box.size(); i++) {
			if (box.get(i).getNumber() != 0) {
				for (int j = 0; j < box.get(i).getList().size(); j++) {
					BoxDetail boxDetail = box.get(i).getList().get(j);
					if (!boxDetail.isHasFinish()&&boxDetail.getBoxStatus()==2) {
						hasFinish=false;
						break outer;
					}
				}
			}
			if (box.get(i).getNeed_install() != 0) {
				for (int j = 0; j < box.get(i).getNeed_install(); j++) {
					if (box.get(i).need_install_list.size()==0){
						hasFinish=false;
						break outer;
					}
					BoxDetail boxDetail = box.get(i).need_install_list.get(j);
					if (!boxDetail.isHasFinish()) {
						hasFinish=false;
						break outer;
					}
				}

			}
		}
		if (hasFinish){
			return 1;
		}
		return 0;
	}

	public String getTakeCarTypeName() {
		return takeCarTypeName;
	}

	public void setTakeCarTypeName(String takeCarTypeName) {
		this.takeCarTypeName = takeCarTypeName;
	}

	public License getLicense() {
		return license;
	}

	public void setLicense(License license) {
		this.license = license;
	}

	public RentMoney getRentMoney() {
		return rentMoney;
	}

	public void setRentMoney(RentMoney rentMoney) {
		this.rentMoney = rentMoney;
	}

	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}

	public CarDetail getCarDetail() {
		return carDetail;
	}

	public void setCarDetail(CarDetail carDetail) {
		this.carDetail = carDetail;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Gas getGas() {
		return gas;
	}

	public void setGas(Gas gas) {
		this.gas = gas;
	}
}
