package com.xtel.basicsample.view.activity;

import android.os.Bundle;
import android.os.Handler;

import com.xtel.basicsample.R;

/**
 * Created by Lê Công Long Vũ on 12/2/2016
 */

public class SplashActivity extends BasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivityAndFinish(HomeActivity.class);
            }
        }, 500);
    }
}
