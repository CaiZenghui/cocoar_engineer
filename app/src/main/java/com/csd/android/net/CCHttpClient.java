package com.csd.android.net;

import android.text.TextUtils;

import com.csd.android.CCApplication;
import com.csd.android.UserManager;
import com.csd.android.utils.UIUtils;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

public class CCHttpClient {
	private static OkHttpClient client = new OkHttpClient();
	private static int HTTP_GET = 0;
	private static int HTTP_POST = 1;

	private static Builder form_urlencoded_builder = new Builder().header("User-Agent", "android:" + UIUtils.getVersionName()).addHeader(
			"Content-Type", "application/x-www-form-urlencoded");

	private static Builder post_file_builder = new Builder().header("User-Agent", "android:" + UIUtils.getVersionName());

	static {
		client.setConnectTimeout(25, TimeUnit.SECONDS);
		client.setWriteTimeout(25, TimeUnit.SECONDS);
		client.setReadTimeout(25, TimeUnit.SECONDS);
		client.setCache(new Cache(CCApplication.getApplication().getCacheDir(), 20 * 1024 * 1024));
	}

	public static void cancel(String tag) {
		client.cancel(tag);
	}

	/**
	 * @param url
	 * @param params 如{"key1=value1","key2=value2"}
	 * @param tag
	 * @param callBack
	 */
	public static void get(String url, String[] params, String tag, Callback callBack) {
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				try {
					if (i == 0) {
						url += "?";
					}
					url += (URLEncoder.encode(params[i].split("=")[0], "UTF-8") + "=" + URLEncoder.encode(params[i].split("=")[1], "UTF-8"));
					url += params[i];
					if (i != params.length - 1) {
						url += "&";
					}
				}
				catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		sendRequest(url, tag, callBack, HTTP_GET, null);
	}

	public static void get(String url, HashMap<String, String> params, String tag, Callback callBack) {
		StringBuilder builder = new StringBuilder(url);
		if (params != null) {
			Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
			builder.append("?");
			while (iterator.hasNext()) {
				Entry<String, String> entry = iterator.next();
				try {
					builder.append(URLEncoder.encode(entry.getKey(), "UTF-8")).append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8"))
							.append("&");
				}
				catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			builder.deleteCharAt(builder.length() - 1);
		}
		sendRequest(builder.toString(), tag, callBack, HTTP_GET, null);
	}

	/**
	 * @param url
	 * @param params 如{"key1=value1","key2=value2"}
	 * @param tag
	 * @param callBack
	 */
	public static void post(String url, String[] params, String tag, Callback callBack) {
		FormEncodingBuilder builder = new FormEncodingBuilder();
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				builder.add(params[i].split("=")[0], params[i].split("=")[1]);
			}
		}
		RequestBody formBody = null;
		try {
			formBody = builder.build();
		}
		catch (Exception e) {
		}
		sendRequest(url, tag, callBack, HTTP_POST, formBody);
	}

	public static void post(String url, HashMap<String, String> params, String tag, Callback callBack) {
		if (null != UserManager.getUser() && !UIUtils.isEmpty(UserManager.getUser().getToken())) {
			if (params == null) {
				params = new HashMap<String, String>();
			}
			params.put("ACCESS_TOKEN", UserManager.getUser().getToken());
		}
		FormEncodingBuilder builder = new FormEncodingBuilder();
		if (params != null) {
			Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, String> entry = iterator.next();
				builder.add(entry.getKey(), entry.getValue());
			}
		}
		RequestBody formBody = null;
		try {
			formBody = builder.build();
		}
		catch (Exception e) {
		}
		sendRequest(url, tag, callBack, HTTP_POST, formBody);
	}

	private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("text/x-markdown; charset=utf-8");

	public static void postFile(String url, HashMap<String, String> params, String tag, Callback callBack) {
		if (null != UserManager.getUser() && !UIUtils.isEmpty(UserManager.getUser().getToken())) {
			if (params == null) {
				params = new HashMap<String, String>();
			}
			params.put("ACCESS_TOKEN", UserManager.getUser().getToken());
		}
		String file_path = params.remove("file");
		FormEncodingBuilder builder = new FormEncodingBuilder();
		if (params != null) {
			Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, String> entry = iterator.next();
				builder.add(entry.getKey(), entry.getValue());
			}
		}
		RequestBody formBody = null;
		try {
			formBody = builder.build();
		}
		catch (Exception e) {
		}
		RequestBody requestBody = new MultipartBuilder()
				.type(MultipartBuilder.FORM)
				.addPart(Headers.of("Content-Disposition", "form-data; name=\"form\""), formBody)
				.addPart(Headers.of("Content-Disposition", "form-data; name=\"image_data\""),
						RequestBody.create(MEDIA_TYPE_PNG, new File(URI.create(file_path)))).build();

		sendFileRequest(url, tag, callBack, HTTP_POST, requestBody);
	}

	private static void sendRequest(String url, String tag, final Callback callBack, int http_method, RequestBody body) {
		if (null != UserManager.getUser() && !UIUtils.isEmpty(UserManager.getUser().getToken())) {
			form_urlencoded_builder.removeHeader("Token");
			form_urlencoded_builder.addHeader("Token", UserManager.getUser().getToken());
		}
		if (!TextUtils.isEmpty(tag)) {
			form_urlencoded_builder.tag(tag);
		}
		if (http_method == HTTP_GET) {
			form_urlencoded_builder.get();
		}
		else if (http_method == HTTP_POST) {
			form_urlencoded_builder.post(body);
		}
		
		form_urlencoded_builder.url(url);

		client.newCall(form_urlencoded_builder.build()).enqueue(callBack);

	}

	private static void sendFileRequest(String url, String tag, final Callback callBack, int http_method, RequestBody body) {
		if (null != UserManager.getUser() && !UIUtils.isEmpty(UserManager.getUser().getToken())) {
			post_file_builder.removeHeader("Token");
			post_file_builder.addHeader("Token", UserManager.getUser().getToken());
		}
		if (!TextUtils.isEmpty(tag)) {
			post_file_builder.tag(tag);
		}
		post_file_builder.url(url).post(body);

		client.newCall(post_file_builder.build()).enqueue(callBack);

	}
}
