package com.csd.android.io;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import com.csd.android.CCApplication;
import com.csd.android.utils.UIUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CacheHelper {
	private static final String TAG = CacheHelper.class.getSimpleName();
	private static final Context CONTEXT = CCApplication.sContext;

	public static final String URI_SCHEME_FILE = "file";
	public static final String URI_SCHEME_CONTENT_PROVIDER = "content";

	public static final String UIL_URI_PREFIX_WEB = "http://";
	public static final String UIL_URI_PREFIX_FILE = "file://";
	// public static final String UIL_URI_PREFIX_CONTENT = "content://";
	public static final String UIL_URI_PREFIX_ASSETS = "assets://";
	public static final String UIL_URI_PREFIX_DRAWABLE = "drawable://";

	private static final String JPG_PREFIX = "IMG_";
	private static final String JPG_SUFFIX = ".jpg";
	// private static final String PNG_SUFFIX = ".png";

	private static final String UIL_DIR_NAME = "UIL";
	private static final String ALBUM_DIR_NAME = "COCAR";

	/* Checks if external storage is available for read and write */
	private static boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	/* Checks if external storage is available to at least read */
	private static boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}

	private static void checkDir(File dir) {
		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				Log.e(TAG, "Directory not created");
			}
			else
				Log.v(TAG, "Directory created");
		}
	}

	public static void removeCache(String fn) {
		removeCache(new File(fn));
	}

	public static void removeCache(File file) {
		if (file.isFile() && file.exists())
			file.delete();
	}

	public static File getUILDir() {
		// Get the directory for the user's public pictures directory.
		if (!isExternalStorageWritable()) {
			return null;
		}

		File file = new File(CONTEXT.getExternalFilesDir(null), UIL_DIR_NAME);
		checkDir(file);
		return file;
	}

	private static File getImageDir() {
		if (!isExternalStorageWritable()) {
			return null;
		}
		File file = CONTEXT.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		checkDir(file);
		return file;
	}

	private static File getAlumbDir() {
		if (!isExternalStorageWritable()) {
			return null;
		}
		File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), ALBUM_DIR_NAME);
		checkDir(file);
		return file;
	}

	private static File getTempDir() {
		if (!isExternalStorageWritable()) {
			return null;
		}
		File file = CONTEXT.getExternalCacheDir();
		checkDir(file);
		return file;
	}

	private static String getFileName() {

		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = JPG_PREFIX + timeStamp + "_";

		return imageFileName;
	}

	public static String getPhotoPath() {
		try {
			File file = File.createTempFile(getFileName(), JPG_SUFFIX, getAlumbDir());
			return file.getAbsolutePath();
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getImagePath() {
		try {
			File file = File.createTempFile(getFileName(), JPG_SUFFIX, getImageDir());
			return file.getAbsolutePath();
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getTempPath() {
		try {
			File file = File.createTempFile(getFileName(), JPG_SUFFIX, getTempDir());
			return file.getAbsolutePath();
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getBase64FromFile(String path) {
		if (UIUtils.isEmpty(path))
			return "";

		String base64 = "";
		try {
			FileInputStream fis = new FileInputStream(path);
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			base64 = Base64.encodeToString(buffer, Base64.DEFAULT);

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return base64;
	}

	public static void saveJpg(Bitmap bitmap, String path) {
		saveJpg(bitmap, path, 100);
	}

	public static void saveJpg(Bitmap bitmap, String path, int quality) {
		if ((quality < 0) || (quality > 100))
			quality = 100;
		File file = new File(path);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			OutputStream os = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, quality, os);
			os.flush();
			os.close();
		}
		catch (Exception e) {
		}
	}

	public static void galleryAddPic(Context context, String path) {
		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File f = new File(path);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		context.sendBroadcast(mediaScanIntent);
	}

	public static String getUriFromPath(String path) {
		Uri uri = Uri.fromFile(new File(path));
		return uri.toString();
	}

	public static String getUriFromPath2(String path) {
		return UIL_URI_PREFIX_FILE + path;
	}

	public static String getPathFromUri(String uri) {
		if (UIUtils.isEmpty(uri))
			return null;

		return getPath(Uri.parse(uri));
	}

	private static String getPath(Uri uri) {
		String scheme = uri.getScheme();
		if (scheme.equalsIgnoreCase(URI_SCHEME_FILE)) {
			return uri.getPath();

		}
		else if (scheme.equalsIgnoreCase(URI_SCHEME_CONTENT_PROVIDER)) {
			return getRealPathFromURI(uri);
		}
		return null;
	}

	private static String getRealPathFromURI(Uri contentUri) {
		Cursor cursor = null;
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			cursor = CONTEXT.getContentResolver().query(contentUri, proj, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		}
		finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	public static boolean isWebUri(String uri) {
		if (uri == null)
			return true;

		if (uri.startsWith(UIL_URI_PREFIX_WEB))
			return true;
		return false;
	}

	@SuppressLint("NewApi")
	public static String getPath2(final Uri uri) {
		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(CCApplication.sContext, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}

			}
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

				return getDataColumn(contentUri, null, null);
			}
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				}
				else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				}
				else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(contentUri, selection, selectionArgs);
			}
		}
		else if ("content".equalsIgnoreCase(uri.getScheme())) {
			return getDataColumn(uri, null, null);
		}
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	public static String getDataColumn(Uri uri, String selection, String[] selectionArgs) {

		Context context = CCApplication.sContext;

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		}
		finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	private static File getDebugStringDirectory() {
		if (!isExternalStorageWritable()) {
			return null;
		}
		File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "COCARDebug");
		checkDir(file);
		return file;
	}

	private static File createDebugFile() throws IOException {
		// Create an image file name

		String imageFileName = "form";
		File dir = getDebugStringDirectory();
		File file = File.createTempFile(imageFileName, ".txt", dir);
		return file;
	}

	public static String assignDebugFileName() {
		String path = getDebugStringDirectory() + File.separator + "form.txt";
		return path;
	}

	public static void saveDebug(String s) {
		try {
			File f = new File(assignDebugFileName());
			FileWriter fw = new FileWriter(f);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(s, 0, s.length());
			bw.close();

		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}
}
