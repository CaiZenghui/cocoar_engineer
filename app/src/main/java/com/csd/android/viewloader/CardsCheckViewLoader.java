package com.csd.android.viewloader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;
import com.csd.android.R;
import com.csd.android.activity.TaskDetailActivity;
import com.csd.android.io.CacheHelper;
import com.csd.android.model.PicPostResult;
import com.csd.android.net.CCHttpEngine;
import com.csd.android.net.HttpCallBack;
import com.csd.android.net.NetConstants;
import com.csd.android.net.ResponseBean;
import com.csd.android.utils.LogUtils;
import com.csd.android.utils.ToastUtils;
import com.csd.android.utils.UIUtils;
import com.csd.android.widget.CCWaitingDialog;
import com.csd.android.widget.TabsHorizontalScrollView;
import com.csd.android.widget.TabsHorizontalScrollView.ItemClickListener;

public class CardsCheckViewLoader {
	private TaskDetailActivity context;
	private View cardsCheckView;

	private IdentityCardViewLoader identityCardViewLoader;
	private DrivingLicenseCardViewLoader drivingLicenseCardViewLoader;
	private TogetherCardsCheckViewLoader togetherCardsCheckViewLoader;
	private CarPhotoViewLoader carPhotoViewLoader;
	private InsurancePolicyCheckViewLoader insurancePolicyCheckViewLoader;
	private int current_view = 1;

	public CardsCheckViewLoader(TaskDetailActivity context) {
		this.context = context;
	}

	public void load() {
		if (cardsCheckView == null) {
			cardsCheckView = LayoutInflater.from(context).inflate(R.layout.layout_id_check, null);
			TabsHorizontalScrollView tabs = (TabsHorizontalScrollView) cardsCheckView.findViewById(R.id.tabs);

			final ScrollView vg_content = (ScrollView) cardsCheckView.findViewById(R.id.content);

			if (identityCardViewLoader == null) {
				identityCardViewLoader = new IdentityCardViewLoader(context, vg_content);
			}
			identityCardViewLoader.load();

			tabs.setItemClickListener(new ItemClickListener() {

				@Override
				public void click_identity_card_view() {
					removeChildView(vg_content);
					identityCardViewLoader.load();
					current_view = 1;
				}

				@Override
				public void click_driving_license_view() {
					removeChildView(vg_content);
					if (drivingLicenseCardViewLoader == null) {
						drivingLicenseCardViewLoader = new DrivingLicenseCardViewLoader(context, vg_content);
					}
					drivingLicenseCardViewLoader.load();
					current_view = 2;
				}

				@Override
				public void click_insurance_policy_view() {
					removeChildView(vg_content);
					if (insurancePolicyCheckViewLoader == null) {
						insurancePolicyCheckViewLoader = new InsurancePolicyCheckViewLoader(context, vg_content);
					}

					insurancePolicyCheckViewLoader.load();

					current_view = 4;
				}

				@Override
				public void click_cards_check_view() {
					removeChildView(vg_content);
					if (togetherCardsCheckViewLoader == null) {
						togetherCardsCheckViewLoader = new TogetherCardsCheckViewLoader(context, vg_content);
					}
					togetherCardsCheckViewLoader.load();
					current_view = 3;
				}

				@Override
				public void click_car_photo_view() {
					removeChildView(vg_content);
					if (carPhotoViewLoader == null) {
						carPhotoViewLoader = new CarPhotoViewLoader(context, vg_content);
					}
					carPhotoViewLoader.load();
					current_view = 5;
				}
			});
		}
		context.vg_root.addView(cardsCheckView, 1);

	}

	protected void removeChildView(ScrollView vg_content) {
		if (vg_content.getChildCount() > 0) {
			vg_content.removeViewAt(0);
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == TaskDetailActivity.REQUEST_CODE_TAKE_PHOTO) {
			if (resultCode == Activity.RESULT_OK) {
				LogUtils.d("ChoosePic", "pic path=" + Uri.fromFile(new File(PhotoViewClickListener.photoPath)).toString());

				Intent intent = new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(Uri.fromFile(new File(PhotoViewClickListener.photoPath)), "image/*");
				PhotoViewClickListener.photoPath = CacheHelper.getPhotoPath();
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(PhotoViewClickListener.photoPath)));
				intent.putExtra("crop", "true");
//				intent.putExtra("aspectX", 3);
//				intent.putExtra("aspectY", 2);
				intent.putExtra("outputX", context.getWindow().getDecorView().getWidth() / 2);
				intent.putExtra("outputY", UIUtils.dp2px(120));
				intent.putExtra("noFaceDetection", false);

				context.startActivityForResult(intent, TaskDetailActivity.REQUEST_CODE_PICK_PHOTO);
			}

		}
		else if (requestCode == TaskDetailActivity.REQUEST_CODE_PICK_PHOTO && resultCode == Activity.RESULT_OK) {
			uploadPic();
		}
		else if (current_view == 1) {
			identityCardViewLoader.onActivityResult(requestCode, resultCode, data);
		}
		else if (current_view == 2) {
			drivingLicenseCardViewLoader.onActivityResult(requestCode, resultCode, data);
		}
		else if (current_view == 3) {
			togetherCardsCheckViewLoader.onActivityResult(requestCode, resultCode, data);
		}
		else if (current_view == 4) {
			insurancePolicyCheckViewLoader.onActivityResult(requestCode, resultCode, data);
		}
		else if (current_view == 5) {
			carPhotoViewLoader.onActivityResult(requestCode, resultCode, data);
		}

	}

	private void uploadPic() {

		LogUtils.d("ChoosePic", "pic path=" + Uri.fromFile(new File(PhotoViewClickListener.photoPath)).toString());

		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("file", Uri.fromFile(new File(PhotoViewClickListener.photoPath)).toString());

		hashMap.put("car_id", context.task_detail_entity.getCarId());
		if (PhotoViewClickListener.CAR_PHOTO_VIEW_ID == R.id.iv_identity_card_face) {// 身份证正面；
			hashMap.put("type", "1");
			hashMap.put("back", "0");
		}
		else if (PhotoViewClickListener.CAR_PHOTO_VIEW_ID == R.id.iv_identity_card_back) {
			hashMap.put("type", "1");
			hashMap.put("back", "1");
		}
		else if (PhotoViewClickListener.CAR_PHOTO_VIEW_ID == R.id.iv_car_left_front) {
			hashMap.put("type", "4");
			hashMap.put("pos", "1");
		}
		else if (PhotoViewClickListener.CAR_PHOTO_VIEW_ID == R.id.iv_car_right_front) {
			hashMap.put("type", "4");
			hashMap.put("pos", "2");
		}
		else if (PhotoViewClickListener.CAR_PHOTO_VIEW_ID == R.id.iv_car_left_back) {
			hashMap.put("type", "4");
			hashMap.put("pos", "3");
		}
		else if (PhotoViewClickListener.CAR_PHOTO_VIEW_ID == R.id.iv_car_right_back) {
			hashMap.put("type", "4");
			hashMap.put("pos", "4");
		}
		else if (PhotoViewClickListener.CAR_PHOTO_VIEW_ID == R.id.iv_car_driver) {
			hashMap.put("type", "4");
			hashMap.put("pos", "5");
		}
		else if (PhotoViewClickListener.CAR_PHOTO_VIEW_ID == R.id.iv_car_control) {
			hashMap.put("type", "4");
			hashMap.put("pos", "6");
		}
		else if (PhotoViewClickListener.CAR_PHOTO_VIEW_ID == R.id.iv_car_front_row) {
			hashMap.put("type", "4");
			hashMap.put("pos", "7");
		}
		else if (PhotoViewClickListener.CAR_PHOTO_VIEW_ID == R.id.iv_car_back_row) {
			hashMap.put("type", "4");
			hashMap.put("pos", "8");
		}
		else if (PhotoViewClickListener.CAR_PHOTO_VIEW_ID == R.id.iv_driving_license_page) {
			hashMap.put("type", "2");
			hashMap.put("back", "0");
		}
		else if (PhotoViewClickListener.CAR_PHOTO_VIEW_ID == R.id.iv_driving_license_vice_page) {
			hashMap.put("type", "2");
			hashMap.put("back", "1");
		}
		else if (PhotoViewClickListener.CAR_PHOTO_VIEW_ID == R.id.iv_insurance) {
			hashMap.put("type", "3");
		}
		else if (PhotoViewClickListener.CAR_PHOTO_VIEW_ID == R.id.iv_together_1) {
			hashMap.put("type", "5");
			hashMap.put("car_type", "1");
		}
		else if (PhotoViewClickListener.CAR_PHOTO_VIEW_ID == R.id.iv_together_2) {
			hashMap.put("type", "5");
			hashMap.put("car_type", "2");
		}
		else if (PhotoViewClickListener.CAR_PHOTO_VIEW_ID == R.id.iv_together_3) {
			hashMap.put("type", "5");
			hashMap.put("car_type", "3");
		}
		else if (PhotoViewClickListener.CAR_PHOTO_VIEW_ID == R.id.iv_together_4) {
			hashMap.put("type", "5");
			hashMap.put("car_type", "4");
		}

		context.TAG_NO_FIRST_REQUEST = "save_photo";
		CCWaitingDialog.show(context);
		new CCHttpEngine(context, NetConstants.NET_ID_POST_PHOTO, hashMap, context.TAG_NO_FIRST_REQUEST, new HttpCallBack() {

			@Override
			public void onSuccess(ResponseBean responseBean) {
				CCWaitingDialog.dismiss(context);
				if (responseBean.getCode() == 0) {
					ToastUtils.showToast("图片上传成功");
					Glide.with(context).load(new File(PhotoViewClickListener.photoPath))
							.into((ImageView) context.findViewById(PhotoViewClickListener.CAR_PHOTO_VIEW_ID));
					context.findViewById(PhotoViewClickListener.CAR_PHOTO_VIEW_ID).setTag(R.id.tag_glide, "success");
					context.findViewById(PhotoViewClickListener.CAR_PHOTO_VIEW_ID).setOnLongClickListener(null);
					String uri = ((PicPostResult) responseBean.getData()).getPic();

					if (current_view == 1) {
						identityCardViewLoader.onUploadPicResult(uri);
					}
					else if (current_view == 2) {
						drivingLicenseCardViewLoader.onUploadPicResult(uri);
					}
					else if (current_view == 3) {
						togetherCardsCheckViewLoader.onUploadPicResult(uri);
					}
					else if (current_view == 4) {
						insurancePolicyCheckViewLoader.onUploadPicResult(uri);
					}
					else if (current_view == 5) {
						carPhotoViewLoader.onUploadPicResult(uri);
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
}
