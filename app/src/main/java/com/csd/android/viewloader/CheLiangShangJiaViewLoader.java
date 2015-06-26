package com.csd.android.viewloader;

import java.io.IOException;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;

import com.csd.android.CCApplication;
import com.csd.android.R;
import com.csd.android.activity.SelectAddressActivity;
import com.csd.android.activity.SelectCarBrandActivity;
import com.csd.android.activity.TaskDetailActivity;
import com.csd.android.model.Address;
import com.csd.android.model.CarBrand.CarType;
import com.csd.android.model.CarPrice;
import com.csd.android.model.PetrolConsume;
import com.csd.android.model.TaskDetail;
import com.csd.android.net.CCHttpEngine;
import com.csd.android.net.HttpCallBack;
import com.csd.android.net.NetConstants;
import com.csd.android.net.ResponseBean;
import com.csd.android.utils.ToastUtils;
import com.csd.android.utils.UIUtils;
import com.csd.android.widget.CCWaitingDialog;
import com.csd.android.widget.ItemPop;
import com.csd.android.widget.ItemPop.OnClickItemListener;
import com.csd.android.widget.PickerPop;
import com.csd.android.widget.PickerPop.OnConfirmListener;

public class CheLiangShangJiaViewLoader {

	private TaskDetailActivity context;
	private View cheLiangShangJiaView;
	private String car_brand_name;
	private String car_type_name;

	public CheLiangShangJiaViewLoader(TaskDetailActivity context) {
		this.context = context;
	}

	public void load() {
		final TaskDetail entity = context.task_detail_entity;
		initView(entity);
		initUiData(entity);
	}

	private void initUiData(TaskDetail entity) {
		((TextView) cheLiangShangJiaView.findViewById(R.id.tv_car_owner_name)).setText(entity.getLicense().getDriverLicense().getOwnerName());
		((TextView) cheLiangShangJiaView.findViewById(R.id.tv_phone_num)).setText(entity.getOwner().getLoginname());
		((TextView) cheLiangShangJiaView.findViewById(R.id.tv_car_brand)).setText(entity.getCarDetail().getCarName());
		((TextView) cheLiangShangJiaView.findViewById(R.id.tv_take_car_type)).setText(entity.getTakeCarTypeName());

		((TextView) cheLiangShangJiaView.findViewById(R.id.tv_speed_box)).setText(entity.getCarDetail().getSpeedBox());
		((TextView) cheLiangShangJiaView.findViewById(R.id.tv_petrol_type)).setText(entity.getGas().getPetrolType());

		((TextView) cheLiangShangJiaView.findViewById(R.id.tv_car_color)).setText(entity.getCarDetail().getVehicleColor());

		((TextView) cheLiangShangJiaView.findViewById(R.id.tv_fuel_consumption)).setText(entity.getGas().getGasConsume() + "L/百公里");
		((TextView) cheLiangShangJiaView.findViewById(R.id.tv_price)).setText(entity.getRentMoney().getDay() + "元/日");

		((TextView) cheLiangShangJiaView.findViewById(R.id.tv_location)).setText(entity.getLocation().getAddress());

		if (!UIUtils.isEmpty(entity.getCarDetail().getCarPlateNo())) {
			String num = entity.getCarDetail().getCarPlateNo();
			((TextView) cheLiangShangJiaView.findViewById(R.id.tv_car_plate1)).setText(num.charAt(0) + "");
			((TextView) cheLiangShangJiaView.findViewById(R.id.tv_car_plate2)).setText(num.charAt(1) + "");
			((TextView) cheLiangShangJiaView.findViewById(R.id.tv_car_plate3)).setText(num.substring(2, num.length()));
		}

		((CheckBox) cheLiangShangJiaView.findViewById(R.id.cb_mp3)).setChecked(entity.getCarDetail().getMp3() == 0 ? false : true);
		((CheckBox) cheLiangShangJiaView.findViewById(R.id.cb_gps)).setChecked(entity.getCarDetail().getGps() == 0 ? false : true);
		((CheckBox) cheLiangShangJiaView.findViewById(R.id.cb_recorder)).setChecked(entity.getCarDetail().getRecorder() == 0 ? false : true);
		((CheckBox) cheLiangShangJiaView.findViewById(R.id.cb_boySeat)).setChecked(entity.getCarDetail().getBoySeat() == 0 ? false : true);
		((CheckBox) cheLiangShangJiaView.findViewById(R.id.cb_bluetooth)).setChecked(entity.getCarDetail().getBluetooth() == 0 ? false : true);
		getCarPrice(entity);
	}

	private void getCarPrice(TaskDetail entity) {
		if (!UIUtils.isEmpty(entity.getCarDetail().getCarTypeId())) {
			HashMap<String, String> hashMap = new HashMap<String, String>();
			hashMap.put("carTypeId", entity.getCarDetail().getCarTypeId());
			new CCHttpEngine(context, NetConstants.NET_ID_GETCAR_PRICE, hashMap, null, new HttpCallBack() {
				public void onSuccess(ResponseBean responseBean) {
					if (responseBean.getCode() == 0) {
						CarPrice price = (CarPrice) responseBean.getData();
						((TextView) cheLiangShangJiaView.findViewById(R.id.tv_car_price)).setText(price.getPrice());
					}
				}

				@Override
				public void onNetUnavailable(String net_unAvailabel) {

				}

				@Override
				public void onFailure(IOException e) {

				}
			}).executeTask();
		}

	}
	
	private void getPetrolDayPrice(String car_type_id) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("car_type_id", car_type_id);
		new CCHttpEngine(context, NetConstants.NET_ID_GET_PETROL_CONSUME, hashMap, null, new HttpCallBack() {

			@Override
			public void onSuccess(ResponseBean responseBean) {
				if (responseBean.getCode() == 0) {
					PetrolConsume entity = (PetrolConsume) responseBean.getData();
					context.task_detail_entity.getRentMoney().setDay(entity.getRentDay());
					context.task_detail_entity.getGas().setGasConsume(entity.getPetrolConsume());
					((TextView) cheLiangShangJiaView.findViewById(R.id.tv_fuel_consumption)).setText(entity.getPetrolConsume() + "L/百公里");
					((TextView) cheLiangShangJiaView.findViewById(R.id.tv_price)).setText(entity.getRentDay() + "元/日");
				}
			}

			@Override
			public void onNetUnavailable(String net_unAvailabel) {
			}

			@Override
			public void onFailure(IOException e) {
			}
		}).executeTask();

	}

	private void initView(final TaskDetail entity) {
		if (cheLiangShangJiaView == null) {
			cheLiangShangJiaView = LayoutInflater.from(context).inflate(R.layout.layout_cheliang_shangjia, null);

			cheLiangShangJiaView.findViewById(R.id.btn_save).setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					saveCheLiangShangJiaViewInfo();
				}
			});

			cheLiangShangJiaView.findViewById(R.id.vg_location).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, SelectAddressActivity.class);
					intent.putExtra(SelectAddressActivity.CITY_NAME, entity.getOwner().getCityName());
					context.startActivityForResult(intent, TaskDetailActivity.REQUEST_CODE_SELECT_ADDRESS);
				}
			});

			cheLiangShangJiaView.findViewById(R.id.vg_car_brand).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, SelectCarBrandActivity.class);
					context.startActivityForResult(intent, TaskDetailActivity.REQUEST_CODE_SELECT_CAR_BRAND);
				}
			});

			cheLiangShangJiaView.findViewById(R.id.vg_car_color).setOnClickListener(new OnClickListener() {
				private PickerPop pop;
				private String[] colors = CCApplication.getApplication().getResources().getStringArray(R.array.vehicleColor);

				public void onClick(View v) {
					if (pop == null) {
						pop = new PickerPop(context, colors);
						pop.setOnConfirmListener(new OnConfirmListener() {
							@Override
							public void onConfirmClick(int... results) {
								((TextView) cheLiangShangJiaView.findViewById(R.id.tv_car_color)).setText(colors[results[0]]);
								context.task_detail_entity.getCarDetail().setColor((results[0] + 1) + "");
							}
						});
					}
					pop.show();
				}
			});

			cheLiangShangJiaView.findViewById(R.id.vg_petrol_type).setOnClickListener(new OnClickListener() {
				private ItemPop pop;
				private String strs[] = CCApplication.getApplication().getResources().getStringArray(R.array.petrol_type);

				public void onClick(View v) {
					if (pop == null) {
						pop = new ItemPop(context, strs, true);
						pop.setOnClickItemListener(new OnClickItemListener() {
							public void onClickItem(int index) {
								if (index == 99) {//取消；
									return;
								}
								((TextView) cheLiangShangJiaView.findViewById(R.id.tv_petrol_type)).setText(strs[index]);
								context.task_detail_entity.getGas().setGasType(index + "");
							}
						});
					}
					pop.show();
				}

			});

			cheLiangShangJiaView.findViewById(R.id.vg_speed_box).setOnClickListener(new OnClickListener() {
				private ItemPop speedBoxPop;
				private String strs[] = CCApplication.getApplication().getResources().getStringArray(R.array.speed_box);

				public void onClick(View v) {
					if (speedBoxPop == null) {
						speedBoxPop = new ItemPop(context, strs, true);
						speedBoxPop.setOnClickItemListener(new OnClickItemListener() {
							public void onClickItem(int index) {
								if (index == 99) {//取消；
									return;
								}
								((TextView) cheLiangShangJiaView.findViewById(R.id.tv_speed_box)).setText(strs[index]);
								context.task_detail_entity.getCarDetail().setChangeSpeedType(index + "");
							}
						});
					}
					speedBoxPop.show();
				}
			});

			cheLiangShangJiaView.findViewById(R.id.layout_car_plate).setOnClickListener(new OnClickListener() {
				private PickerPop carPlatePop;
				private String[] provices = CCApplication.getApplication().getResources().getStringArray(R.array.provinces);
				private String[] letters = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
						"V", "W", "X", "Y", "Z" };

				public void onClick(View v) {
					if (carPlatePop == null) {
						carPlatePop = new PickerPop(context, provices, letters);
						carPlatePop.setOnConfirmListener(new OnConfirmListener() {
							@Override
							public void onConfirmClick(int... results) {
								((TextView) cheLiangShangJiaView.findViewById(R.id.tv_car_plate1)).setText(provices[results[0]]);
								((TextView) cheLiangShangJiaView.findViewById(R.id.tv_car_plate2)).setText(letters[results[1]]);
							}
						});
					}
					carPlatePop.show();
				}
			});

		}

		context.vg_root.addView(cheLiangShangJiaView, 1);
	}

	public void saveCheLiangShangJiaViewInfo() {
		context.TAG_NO_FIRST_REQUEST = "tag_save_cheliang_shangjia_view_info";
		final HashMap<String, String> hashMap = new HashMap<String, String>();

		if (!UIUtils.isEmpty(((TextView) cheLiangShangJiaView.findViewById(R.id.tv_car_plate1)).getText().toString().trim())
				&& !UIUtils.isEmpty(((TextView) cheLiangShangJiaView.findViewById(R.id.tv_car_plate2)).getText().toString().trim())
				&& !UIUtils.isEmpty(((TextView) cheLiangShangJiaView.findViewById(R.id.tv_car_plate3)).getText().toString().trim())) {
			hashMap.put("car_number", ((TextView) cheLiangShangJiaView.findViewById(R.id.tv_car_plate1)).getText().toString().trim()
					+ ((TextView) cheLiangShangJiaView.findViewById(R.id.tv_car_plate2)).getText().toString().trim()
					+ ((TextView) cheLiangShangJiaView.findViewById(R.id.tv_car_plate3)).getText().toString().trim());
		}
		else {
			ToastUtils.showToast("请完善车牌号码");
			return;
		}

		if (!UIUtils.isEmpty(((TextView) cheLiangShangJiaView.findViewById(R.id.tv_car_brand)).getText().toString().trim())) {
			hashMap.put("car_type_id", context.task_detail_entity.getCarDetail().getCarTypeId());

		}
		else {
			ToastUtils.showToast("请选择品牌型号");
			return;
		}

		if (!UIUtils.isEmpty(((TextView) cheLiangShangJiaView.findViewById(R.id.tv_speed_box)).getText().toString().trim())) {
			hashMap.put("change_speed_type", context.task_detail_entity.getCarDetail().getChangeSpeedType());
		}
		else {
			ToastUtils.showToast("请选择变速箱类型");
			return;
		}

		if (!UIUtils.isEmpty(((TextView) cheLiangShangJiaView.findViewById(R.id.tv_car_color)).getText().toString().trim())) {
			hashMap.put("color", context.task_detail_entity.getCarDetail().getColor());
		}
		else {
			ToastUtils.showToast("请选择车辆颜色");
			return;
		}

		if (!UIUtils.isEmpty(((TextView) cheLiangShangJiaView.findViewById(R.id.tv_location)).getText().toString().trim())) {
			hashMap.put("address", context.task_detail_entity.getLocation().getAddress());
			hashMap.put("lng", context.task_detail_entity.getLocation().getPoint().getLng());
			hashMap.put("lat", context.task_detail_entity.getLocation().getPoint().getLat());
		}
		else {
			ToastUtils.showToast("请选择取车地址");
			return;
		}

		if (!UIUtils.isEmpty(((TextView) cheLiangShangJiaView.findViewById(R.id.tv_petrol_type)).getText().toString().trim())) {
			hashMap.put("petrol_type", context.task_detail_entity.getGas().getGasType());
		}
		else {
			ToastUtils.showToast("请选择汽油型号");
			return;
		}

		hashMap.put("mp3", (((CheckBox) cheLiangShangJiaView.findViewById(R.id.cb_mp3)).isChecked() ? 1 : 0) + "");
		hashMap.put("avigraph", (((CheckBox) cheLiangShangJiaView.findViewById(R.id.cb_gps)).isChecked() ? 1 : 0) + "");
		hashMap.put("record", (((CheckBox) cheLiangShangJiaView.findViewById(R.id.cb_recorder)).isChecked() ? 1 : 0) + "");
		hashMap.put("boy_seat", (((CheckBox) cheLiangShangJiaView.findViewById(R.id.cb_boySeat)).isChecked() ? 1 : 0) + "");
		hashMap.put("fridge", (((CheckBox) cheLiangShangJiaView.findViewById(R.id.cb_bluetooth)).isChecked() ? 1 : 0) + "");

		hashMap.put("login_name", context.task_detail_entity.getOwner().getLoginname());
		hashMap.put("car_id", context.task_detail_entity.getCarId());
		hashMap.put("save", "1");
		CCWaitingDialog.show(context);
		new CCHttpEngine(context, NetConstants.NET_ID_SAVE_CHELIANG_SHANGJIA_VIEW_INFO, hashMap, context.TAG_NO_FIRST_REQUEST, new HttpCallBack() {

			@Override
			public void onSuccess(ResponseBean responseBean) {
				CCWaitingDialog.dismiss(context);
				if (responseBean.getCode() == 0) {
					ToastUtils.showToast("保存成功");
					context.initRedCircle();
					context.findViewById(R.id.view_red_circle2).setVisibility(View.GONE);
					context.task_detail_entity.getCarDetail().setCarPlateNo(hashMap.get("car_number"));
					context.task_detail_entity.getCarDetail().setMp3(((CheckBox) cheLiangShangJiaView.findViewById(R.id.cb_mp3)).isChecked() ? 1 : 0);
					context.task_detail_entity.getCarDetail().setGps(((CheckBox) cheLiangShangJiaView.findViewById(R.id.cb_gps)).isChecked() ? 1 : 0);
					context.task_detail_entity.getCarDetail().setBluetooth(
							((CheckBox) cheLiangShangJiaView.findViewById(R.id.cb_bluetooth)).isChecked() ? 1 : 0);
					context.task_detail_entity.getCarDetail().setBoySeat(
							((CheckBox) cheLiangShangJiaView.findViewById(R.id.cb_boySeat)).isChecked() ? 1 : 0);
					context.task_detail_entity.getCarDetail().setRecorder(
							((CheckBox) cheLiangShangJiaView.findViewById(R.id.cb_recorder)).isChecked() ? 1 : 0);

					if (!UIUtils.isEmpty(car_brand_name) && !UIUtils.isEmpty(car_type_name)) {
						context.task_detail_entity.getCarDetail().setCarName(car_brand_name + car_type_name);
					}

				}
				else {
					ToastUtils.showToast(responseBean.getMessage());
				}
			}

			@Override
			public void onNetUnavailable(String net_unAvailabel) {
				CCWaitingDialog.dismiss(context);
				ToastUtils.showToast(net_unAvailabel);
			}

			@Override
			public void onFailure(IOException e) {
				CCWaitingDialog.dismiss(context);
				ToastUtils.showToast(e.getMessage());
			}
		}).executeTask();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == TaskDetailActivity.REQUEST_CODE_SELECT_CAR_BRAND && resultCode == Activity.RESULT_OK) {
			String car_type_id = data.getStringExtra(TaskDetailActivity.INTENT_EXTRA_NAME_CAR_TYPE_ID);
			car_brand_name = data.getStringExtra(TaskDetailActivity.INTENT_EXTRA_NAME_CAR_BRAND_NAME);
			car_type_name = data.getStringExtra(TaskDetailActivity.INTENT_EXTRA_NAME_CAR_TYPE_NAME);
			context.task_detail_entity.getCarDetail().setCarTypeId(car_type_id);
			context.task_detail_entity.getCarDetail().setCarName(car_brand_name + car_type_name);
			((TextView) cheLiangShangJiaView.findViewById(R.id.tv_car_brand)).setText(car_brand_name + car_type_name);

			getCarPrice(context.task_detail_entity);

			getPetrolDayPrice(car_type_id);
		}
		else if (requestCode == TaskDetailActivity.REQUEST_CODE_SELECT_ADDRESS && resultCode == Activity.RESULT_OK) {
			Address address = (Address) data.getSerializableExtra(TaskDetailActivity.INTENT_EXTRA_NAME_SELECT_ADDRESS);
			((TextView) cheLiangShangJiaView.findViewById(R.id.tv_location)).setText(address.name == null ? address.address : address.name);
			context.task_detail_entity.getLocation().setAddress(address.name == null ? address.address : address.name);
			context.task_detail_entity.getLocation().getPoint().setLng(address.location[0] + "");
			context.task_detail_entity.getLocation().getPoint().setLat(address.location[1] + "");

		}
	}

}
