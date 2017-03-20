package com.xtel.ivipbusiness.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xtel.ivipbusiness.R;
import com.xtel.nipservicesdk.LoginManager;

public class ChooseLoginActivity extends BasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        checkLoged();
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
