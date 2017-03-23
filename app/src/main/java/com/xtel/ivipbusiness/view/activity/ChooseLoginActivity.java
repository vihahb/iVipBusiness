package com.xtel.ivipbusiness.view.activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.annotations.Expose;
import com.xtel.ivipbusiness.R;
import com.xtel.nipservicesdk.LoginManager;
import com.xtel.sdk.fcm.MyFirebaseMessagingService;

public class ChooseLoginActivity extends BasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        checkLoged();
        testThoi();
    }

    protected void testThoi() {
        try {
            Bundle bundle = getIntent().getExtras();

            for (String key : bundle.keySet()) {
                Object value = bundle.get(key);
                Log.d("testThoi", String.format("%s %s (%s)", key, value.toString(), value.getClass().getName()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    * Kiểm tra xem người dùng đã đăng nhập vào app chưa
    * Nếu đăng nhập rồi thì chuyển sang màn hình trang chủ
    * Nếu chưa đăng nhập thì tiếp tục khởi tạo các view
    * */
    protected void checkLoged() {
        if (!LoginManager.getCurrentAuthenticationId().isEmpty())
            startActivityAndFinish(HomeActivity.class);
        else
            initView();
    }

    /*
    * Khởi tạo các view trong layout
    * Lắng nghe sự kiện khi view được click
    * */
    protected void initView() {
        Button button = findButton(R.id.choose_btn_phone_number);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityAndFinish(LoginActivity.class);
            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finishAffinity();
    }
}