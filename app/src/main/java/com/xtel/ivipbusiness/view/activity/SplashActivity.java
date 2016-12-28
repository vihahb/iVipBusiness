package com.xtel.ivipbusiness.view.activity;

import android.os.Handler;
import android.os.Bundle;

import com.xtel.ivipbusiness.R;

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
