package com.xtel.ivipbusiness.view.activity;

import android.os.Bundle;
import android.os.Handler;

import com.xtel.ivipbusiness.R;

/**
 * Created by Lê Công Long Vũ on 12/2/2016
 */

public class SplashActivity extends BasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        if (SharedPreferencesUtils.getInstance().getStringValue(Constants.FCM_TOKEN, null) != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivityAndFinish(HomeActivity.class);
                }
            }, 500);
//        } else {
//            FirebaseInstanceId.getInstance().getToken();
//        }
    }
}