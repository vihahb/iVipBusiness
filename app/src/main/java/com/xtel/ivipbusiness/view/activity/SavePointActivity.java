package com.xtel.ivipbusiness.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.xtel.ivipbusiness.R;

public class SavePointActivity extends BasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_point);

        initToolbar(R.id.save_point_toolbar, null);
    }


}