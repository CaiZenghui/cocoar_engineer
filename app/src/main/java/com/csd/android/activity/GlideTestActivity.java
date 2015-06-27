package com.csd.android.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.csd.android.R;

public class GlideTestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide_test);

        ImageView draweeView = (ImageView) findViewById(R.id.iv_fresco_test);
        Uri uri = Uri.parse("http://f.hiphotos.baidu.com/image/pic/item/574e9258d109b3de095ede66cebf6c81810a4cde.jpg");
        Glide.with(this).load(uri).into(draweeView);
    }


}
