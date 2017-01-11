package com.xtel.ivipbusiness.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xtel.ivipbusiness.R;

public class ChooseLoginActivity extends BasicActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        initView();
    }

    private void initView() {
        Button button = (Button) findViewById(R.id.choose_btn_phone_number);
        button.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finishAffinity();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.choose_btn_phone_number) {
            startActivityAndFinish(LoginActivity.class);
        }
    }
}
