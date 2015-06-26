package com.csd.android;

public class ReleaseConstants {
	public static final int MODE_DEVELOP = 0;
	public static final int MODE_BETA = 1;
	public static final int MODE_RELEASE = 2;

	// 修改Mode 值指定打包版本运行环境；
	public static final int MODE = MODE_DEVELOP;
	
	public static final boolean IS_RELEASE_MODE = (MODE == MODE_RELEASE) ? true : false;
	
	private final String BAIDU_KEY="rEAF8Kmd1YjAc8Qg7DHiMZYY";
	private final String BAIDU_TEST_KEY="Z1X0Mzu1VLY3zn3di4Tt32gS";

}
