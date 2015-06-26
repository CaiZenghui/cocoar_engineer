package com.csd.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.csd.android.CacheFileNameConfig;
import com.csd.android.R;
import com.csd.android.UserManager;
import com.csd.android.constants.SPConstants;
import com.csd.android.model.User;
import com.csd.android.net.CCHttpEngine;
import com.csd.android.net.HttpCallBack;
import com.csd.android.net.NetConstants;
import com.csd.android.net.ResponseBean;
import com.csd.android.utils.CacheUtil;
import com.csd.android.utils.SPUtils;
import com.csd.android.utils.ToastUtils;
import com.csd.android.utils.UIUtils;
import com.csd.android.widget.CCWaitingDialog;

import java.io.IOException;
import java.util.HashMap;

public class LoginActivity extends Activity implements OnClickListener {
    private static final String TAG = "LoginActivity";

    private EditText et_user_pwd;
    private EditText et_user_name;
    private String username;
    private String password;

    private View tv_input_error_tip;
    private CCHttpEngine engine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

        readCache();
    }

    private void readCache() {
        String last_login_id = SPUtils.getSharePrefStr(SPConstants.SHARED_PREFS_FILE_NAME_USER, SPConstants.SHARED_PREFS_KEY_USER_ID);
        if (!UIUtils.isEmpty(last_login_id)) {
            User user = (User) CacheUtil.readCacheObject(String.format(CacheFileNameConfig.CACHE_USER_INFO, last_login_id));
            if (user != null && !UIUtils.isEmpty(user.getLoginName()) && !UIUtils.isEmpty(user.getLoginPwd())) {
                et_user_name.setText(user.getLoginName());
                et_user_pwd.setText(user.getLoginPwd());
                et_user_name.setSelection(et_user_name.length());
                et_user_pwd.setSelection(et_user_pwd.length());
                login();
            }
        }

    }

    private void initView() {
        et_user_name = (EditText) findViewById(R.id.et_user_name);
        et_user_pwd = (EditText) findViewById(R.id.et_user_pwd);
        tv_input_error_tip = findViewById(R.id.tv_input_error_tip);
        findViewById(R.id.btn_login).setOnClickListener(this);

        String user_name = SPUtils.getSharePrefStr(SPConstants.SHARED_PREFS_FILE_NAME_USER, SPConstants.SHARED_PREFS_KEY_USER_NAME);
        if (!UIUtils.isEmpty(user_name)) {
            et_user_name.setText(user_name);
            et_user_name.setSelection(et_user_name.length());
        }
    }

    private void login() {
        tv_input_error_tip.setVisibility(View.GONE);
        username = et_user_name.getText().toString().trim();
        password = et_user_pwd.getText().toString().trim();
        if (!UIUtils.isEmpty(username) && !UIUtils.isEmpty(password)) {
            CCWaitingDialog.show(this);
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("username", username);
            hashMap.put("password", password);
            engine = new CCHttpEngine(this, NetConstants.NET_ID_LOGIN, hashMap, TAG, new HttpCallBack() {
                @Override
                public void onSuccess(ResponseBean responseBean) {
                    CCWaitingDialog.dismiss(LoginActivity.this);
                    if (responseBean.getCode() == 0) {
                        User user = (User) responseBean.getData();
                        user.setLoginName(username);
                        user.setLoginPwd(password);
                        UserManager.setUser(user);
                        CacheUtil.saveCacheObject(user, String.format(CacheFileNameConfig.CACHE_USER_INFO, user.getUserId()));
                        SPUtils.putSharePref(SPConstants.SHARED_PREFS_FILE_NAME_USER, SPConstants.SHARED_PREFS_KEY_USER_NAME, username);
                        SPUtils.putSharePref(SPConstants.SHARED_PREFS_FILE_NAME_USER, SPConstants.SHARED_PREFS_KEY_USER_ID, user.getUserId());

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtils.showToast(responseBean.getMessage());
                    }
                }

                @Override
                public void onNetUnavailable(String net_unAvailabel) {
                    CCWaitingDialog.dismiss(LoginActivity.this);
                    ToastUtils.showToast(net_unAvailabel);
                }

                @Override
                public void onFailure(IOException e) {
                    CCWaitingDialog.dismiss(LoginActivity.this);
                    ToastUtils.showToast("error:" + e.getMessage());
                }
            }).executeTask();
        } else {
            ToastUtils.showToast("用户名或密码不能为空");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                login();
                break;
        }

    }

    @Override
    public void onBackPressed() {
        if (CCWaitingDialog.isShowing(this)) {
            CCWaitingDialog.dismiss(this);
            engine.cancel(TAG);
            return;
        }
        super.onBackPressed();
    }

}
