package com.csd.android.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ScrollView;

import com.csd.android.CCApplication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UIUtils {

    /**
     * 获取渠道名
     */
    public static String getChannel() {
        ApplicationInfo appInfo;
        try {
            appInfo = CCApplication.getApplication().getPackageManager().getApplicationInfo(CCApplication.getApplication().getPackageName(), PackageManager.GET_META_DATA);
            String msg = appInfo.metaData.getString("UMENG_CHANNEL");
            return msg;
        } catch (NameNotFoundException e) {
            return null;
        }
    }


    public static String getVersionName() {
        PackageInfo info;
        try {
            info = CCApplication.getApplication().getPackageManager().getPackageInfo(CCApplication.getApplication().getPackageName(), 0);
            return info.versionName;
        } catch (NameNotFoundException e) {
        }
        return null;
    }

    /**
     * @param input
     * @return
     */
    public static boolean isEmpty(String input) {
        if (TextUtils.isEmpty(input) || TextUtils.isEmpty(input.trim())) {
            return true;
        }

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    public static int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, CCApplication.getApplication().getResources().getDisplayMetrics());
    }

    public static void hideSoftInput(EditText view, Context context) {
        InputMethodManager inputMeMana = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMeMana.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void showSoftInput(Context context) {
        InputMethodManager inputMeMana = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMeMana.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static boolean checkDateValidity(String date_str) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            Date date = sdf.parse(date_str);
            if (date.before(new Date())) {
                return false;
            } else {
                return true;
            }
        } catch (ParseException e) {
            return false;
        }

    }

    public static void scrollToBottome(final ScrollView vg_content) {
        vg_content.postDelayed(new Runnable() {
            public void run() {
                vg_content.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }, 300);
    }

    public static String getFromAssets(String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(CCApplication.getApplication().getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            return null;
        }
    }

}
