package com.csd.android.net;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import com.csd.android.model.BoxTestResult;
import com.csd.android.model.CarBrand;
import com.csd.android.model.CarPrice;
import com.csd.android.model.PetrolConsume;
import com.csd.android.model.PicPostResult;
import com.csd.android.model.TaskCategoryIndex;
import com.csd.android.model.TaskDetail;
import com.csd.android.model.TaskListWrapper;
import com.csd.android.model.User;
import com.csd.android.utils.LogUtils;
import com.csd.android.utils.NetUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class CCHttpEngine implements Callback {
	private static final Handler handler = new Handler();
	private static final String MSG_NET_UNAVAILABLE = "无网络连接";
	private int task_id;
	private HashMap<String, String> params;
	private String tag;
	private HttpCallBack httpCallBack;
	private ResponseBean responseBean;
	private Context context;

	public CCHttpEngine(int task_id, HashMap<String, String> hashMap, String tag, HttpCallBack callBack) {
		this.task_id = task_id;
		this.params = hashMap;
		this.tag = tag;
		this.httpCallBack = callBack;
	}

	public CCHttpEngine(Context context, int task_id, HashMap<String, String> hashMap, String tag, HttpCallBack callBack) {
		this.task_id = task_id;
		this.params = hashMap;
		this.tag = tag;
		this.httpCallBack = callBack;
		this.context = context;
	}

	public void cancel(String tag) {
		CCHttpClient.cancel(tag);
	}

	public CCHttpEngine executeTask() {
		if (!NetUtil.isNetAvaliable()) {
			httpCallBack.onNetUnavailable(MSG_NET_UNAVAILABLE);
			return this;
		}
		switch (task_id) {
			case NetConstants.NET_ID_LOGIN:
				CCHttpClient.post(URLConstants.URL_LOGIN, params, tag, this);
				break;

			case NetConstants.NET_ID_GET_INSTALL_TASK_LIST:
				CCHttpClient.post(URLConstants.URL_GET_INSTALL_TASK_LIST, params, tag, this);
				break;

			case NetConstants.NET_ID_GET_CHE_LIANG_SHANGJIA_XINXI:
				CCHttpClient.post(URLConstants.URL_GET_CHE_LIANG_SHANGJIA_XINXI_VIEW_INFO, params, tag, this);
				break;

			case NetConstants.NET_ID_GET_CAR_BRAND_LIST:
				CCHttpClient.post(URLConstants.URL_GET_CAR_BRAND_LIST, params, tag, this);
				break;

			case NetConstants.NET_ID_GET_PETROL_CONSUME:
				CCHttpClient.post(URLConstants.URL_GET_CAR_CONSUME, params, tag, this);
				break;
			case NetConstants.NET_ID_SAVE_CHELIANG_SHANGJIA_VIEW_INFO:
				CCHttpClient.post(URLConstants.URL_SAVE_CHELIANG_SHANGJIA_VIEW_INFO, params, tag, this);
				break;
			case NetConstants.NET_ID_VERIFY_IDENTITY_CARD_INFO:
				CCHttpClient.post(URLConstants.URL_VERIFY_IDENTITY_CARD_INFO, params, tag, this);
				break;
			case NetConstants.NET_ID_POST_PHOTO:
				CCHttpClient.postFile(URLConstants.URL_POST_PHOTO, params, tag, this);
				break;
			case NetConstants.NET_ID_IDENTITY_CARD_INFO_PASS_SAVE:
				CCHttpClient.post(URLConstants.URL_REQUEST_IDENTITY_CARD_INFO_PASS_SAVE, params, tag, this);
				break;
			case NetConstants.NET_ID_IDENTITY_CARD_INFO_REJECT:
				CCHttpClient.post(URLConstants.URL_REQUEST_IDENTITY_CARD_INFO_REJECT, params, tag, this);
				break;
			case NetConstants.NET_ID_DRIVING_LICENSE_INFO_PASS_SAVE:
				CCHttpClient.post(URLConstants.URL_REQUEST_DRIVING_LICENSE_INFO_PASS_SAVE, params, tag, this);
				break;
			case NetConstants.NET_ID_DRIVING_LICENSE_INFO_REJECT:
				CCHttpClient.post(URLConstants.URL_REQUEST_DRIVING_LICENSE_INFO_REJECT, params, tag, this);
				break;
			case NetConstants.NET_ID_TOGETHER_CARDS_CHECK_REJECT:
				CCHttpClient.post(URLConstants.URL_REQUEST_TOGETHER_CARDS_CHECK_REJECT, params, tag, this);
				break;
			case NetConstants.NET_ID_TOGETHER_CARDS_CHECK_PASS:
				CCHttpClient.post(URLConstants.URL_REQUEST_TOGETHER_CARDS_CHECK_PASS, params, tag, this);
				break;
			case NetConstants.NET_ID_INSURANCE_CHECK_PASS_SAVE:
				CCHttpClient.post(URLConstants.URL_REQUEST_INSURANCE_CHECK_PASS, params, tag, this);
				break;
			case NetConstants.NET_ID_INSURANCE_CHECK_REJECT:
				CCHttpClient.post(URLConstants.URL_REQUEST_INSURANCE_CHECK_REJECT, params, tag, this);
				break;
			case NetConstants.NET_ID_CAR_PHOTO_PASS:
				CCHttpClient.post(URLConstants.URL_REQUEST_CAR_PHOTO_PASS, params, tag, this);
				break;
			case NetConstants.NET_ID_TASK_CATEGORY_DES:
				CCHttpClient.post(URLConstants.URL_GET_TASK_CATEGORY_DES, params, tag, this);
				break;
			case NetConstants.NET_ID_TASK_SUBMIT:
				CCHttpClient.post(URLConstants.URL_GET_TASK_SUBMIT, params, tag, this);
				break;
			case NetConstants.NET_ID_TASK_HASREAD:
				CCHttpClient.post(URLConstants.URL_GET_TASK_SET_READ, params, tag, this);
				break;
			case NetConstants.NET_ID_BOX_CHECK:
				CCHttpClient.post(URLConstants.URL_GET_BOX_CHECK, params, tag, this);
				break;
			case NetConstants.NET_ID_BOX_TEST:
				CCHttpClient.post(URLConstants.URL_GET_BOX_TEST, params, tag, this);
				break;
			case NetConstants.NET_ID_BOX_BIND:
				CCHttpClient.post(URLConstants.URL_GET_BOX_BIND, params, tag, this);
				break;
			case NetConstants.NET_ID_GETCAR_PRICE:
				CCHttpClient.post(URLConstants.URL_GET_CAR_PRICE, params, tag, this);
				break;
			case NetConstants.NET_ID_POST_LOCATION:
				CCHttpClient.post(URLConstants.URL_POST_LOCATION, params, tag, this);
				break;
		}
		return this;
	}

	@Override
	public void onFailure(final Request request, final IOException e) {
		if (context != null && context instanceof Activity && ((Activity) context).isFinishing()) {
			return;
		}
		if (!"Canceled".equals(e.getMessage())) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					httpCallBack.onFailure(e);
				}
			});
		}
	}

	@Override
	public void onResponse(Response response) throws IOException {
		if (!response.isSuccessful()) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					httpCallBack.onFailure(new IOException("服务器响应失败"));
				}
			});
			return;
		}
		Gson gson = new Gson();
		Type type = null;
		try {
			switch (task_id) {
				case NetConstants.NET_ID_LOGIN:
					type = new TypeToken<ResponseBean<User>>() {
					}.getType();
					responseBean = gson.fromJson(response.body().charStream(), type);
					break;

				case NetConstants.NET_ID_GET_INSTALL_TASK_LIST:
					type = new TypeToken<ResponseBean<TaskListWrapper>>() {
					}.getType();
					responseBean = gson.fromJson(response.body().charStream(), type);
					break;

				case NetConstants.NET_ID_GET_CHE_LIANG_SHANGJIA_XINXI:
					type = new TypeToken<ResponseBean<TaskDetail>>() {
					}.getType();
					responseBean = gson.fromJson(response.body().charStream(), type);
					break;

				case NetConstants.NET_ID_GET_CAR_BRAND_LIST:
					type = new TypeToken<ResponseBean<ArrayList<CarBrand>>>() {
					}.getType();
					responseBean = gson.fromJson(response.body().charStream(), type);
					break;
				case NetConstants.NET_ID_GET_PETROL_CONSUME:
					type = new TypeToken<ResponseBean<PetrolConsume>>() {
					}.getType();
					responseBean = gson.fromJson(response.body().charStream(), type);
					break;
				case NetConstants.NET_ID_SAVE_CHELIANG_SHANGJIA_VIEW_INFO:
				case NetConstants.NET_ID_VERIFY_IDENTITY_CARD_INFO:
				case NetConstants.NET_ID_IDENTITY_CARD_INFO_PASS_SAVE:
				case NetConstants.NET_ID_IDENTITY_CARD_INFO_REJECT:
				case NetConstants.NET_ID_DRIVING_LICENSE_INFO_PASS_SAVE:
				case NetConstants.NET_ID_DRIVING_LICENSE_INFO_REJECT:
				case NetConstants.NET_ID_TOGETHER_CARDS_CHECK_REJECT:
				case NetConstants.NET_ID_TOGETHER_CARDS_CHECK_PASS:
				case NetConstants.NET_ID_INSURANCE_CHECK_PASS_SAVE:
				case NetConstants.NET_ID_INSURANCE_CHECK_REJECT:
				case NetConstants.NET_ID_CAR_PHOTO_PASS:
				case NetConstants.NET_ID_TASK_SUBMIT:
				case NetConstants.NET_ID_TASK_HASREAD:
				case NetConstants.NET_ID_BOX_CHECK:
				case NetConstants.NET_ID_BOX_BIND:
					type = new TypeToken<ResponseBean>() {
					}.getType();
					responseBean = gson.fromJson(response.body().charStream(), type);
					break;
				case NetConstants.NET_ID_POST_PHOTO:
					type = new TypeToken<ResponseBean<PicPostResult>>() {
					}.getType();
					responseBean = gson.fromJson(response.body().charStream(), type);
					break;
				case NetConstants.NET_ID_TASK_CATEGORY_DES:
					type = new TypeToken<ResponseBean<ArrayList<TaskCategoryIndex>>>() {
					}.getType();
					responseBean = gson.fromJson(response.body().charStream(), type);
					break;
				case NetConstants.NET_ID_BOX_TEST:
					type = new TypeToken<ResponseBean<BoxTestResult>>() {
					}.getType();
					responseBean = gson.fromJson(response.body().charStream(), type);
					break;
				case NetConstants.NET_ID_GETCAR_PRICE:
					type = new TypeToken<ResponseBean<CarPrice>>() {
					}.getType();
					responseBean = gson.fromJson(response.body().charStream(), type);
					break;
			}
		}
		catch (Exception e) {
			LogUtils.d("HttpEngine", "exception:" + e.getMessage());
		}

		if (responseBean != null) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					httpCallBack.onSuccess(responseBean);
				}
			});
		}
		else {
			if (context != null && context instanceof Activity && ((Activity) context).isFinishing()) {
				return;
			}
			handler.post(new Runnable() {
				@Override
				public void run() {
					httpCallBack.onFailure(new IOException("数据错误"));
				}
			});
		}

	}

}
