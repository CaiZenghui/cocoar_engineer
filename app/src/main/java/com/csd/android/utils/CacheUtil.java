package com.csd.android.utils;

import android.content.Context;

import com.csd.android.CCApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class CacheUtil {
	public static Serializable readCacheObject(String fileName) {
		fileName = chageName2Hashcode(fileName);
		if (!isExistDataCache(fileName))
			return null;
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = CCApplication.getApplication().openFileInput(fileName);
			ois = new ObjectInputStream(fis);
			return (Serializable) ois.readObject();
		}
		catch (FileNotFoundException e) {
		}
		catch (Exception e) {
			e.printStackTrace();
			if (e instanceof InvalidClassException) {
				File data = CCApplication.getApplication().getFileStreamPath(fileName);
				data.delete();
			}
		}
		finally {
			try {
				ois.close();
			}
			catch (Exception e) {
			}
			try {
				fis.close();
			}
			catch (Exception e) {
			}
		}
		return null;
	}

	public static boolean saveCacheObject(Serializable object, String fileName) {
		fileName = chageName2Hashcode(fileName);
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = CCApplication.getApplication().openFileOutput(fileName, Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(object);
			oos.flush();
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			try {
				oos.close();
			}
			catch (Exception e) {
			}
			try {
				fos.close();
			}
			catch (Exception e) {
			}
		}
	}

	/**
	 * 判断缓存是否存在
	 * 
	 * @param fileName
	 * @return true 存在
	 */
	private static boolean isExistDataCache(String fileName) {
		boolean exist = false;
		File data = CCApplication.getApplication().getFileStreamPath(fileName);
		if (data.exists())
			exist = true;
		return exist;
	}

	/**
	 * 
	 * @param fileName
	 * @return 以唯一标识的hashcode命名
	 */
	private static String chageName2Hashcode(String fileName) {
		return "cache" + fileName.hashCode();
	}
}
