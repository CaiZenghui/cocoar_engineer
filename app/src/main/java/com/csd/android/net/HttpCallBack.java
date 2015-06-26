package com.csd.android.net;

import java.io.IOException;

public interface HttpCallBack {
	/**
	 * 手机无网络连接；
	 * @param net_unAvailabel
	 */
	void onNetUnavailable(String net_unAvailabel);

	/**
	 * 目前仅处理 http code 在[200..300)区间；
	 * @param responseBean
	 */
	void onSuccess(ResponseBean responseBean);

	void onFailure(IOException e);
}
