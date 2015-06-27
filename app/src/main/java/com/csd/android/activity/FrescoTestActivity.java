package com.csd.android.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import com.csd.android.R;
import com.facebook.drawee.view.DraweeView;

public class FrescoTestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fresco_test);

        DraweeView draweeView =(DraweeView)findViewById(R.id.iv_fresco_test);
        Uri uri = Uri.parse("http://f.hiphotos.baidu.com/image/pic/item/574e9258d109b3de095ede66cebf6c81810a4cde.jpg");
        draweeView.setImageURI(uri);
    }


}
