package com.xtel.ivipbusiness.view.activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.Gallery;
import com.xtel.ivipbusiness.model.entity.RESP_Gallery;
import com.xtel.ivipbusiness.view.adapter.ShowImageAdapter;
import com.xtel.nipservicesdk.utils.JsonHelper;
import com.xtel.sdk.callback.DialogListener;
import com.xtel.sdk.commons.Constants;

import java.util.ArrayList;

public class ShowImageActivity extends BasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        getData();
        initListener();
    }

    protected void getData() {
        String jsonObject = null;

        try {
            jsonObject = getIntent().getStringExtra(Constants.MODEL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            RESP_Gallery resp_gallery = JsonHelper.getObject(jsonObject, RESP_Gallery.class);
            initViewPager(resp_gallery);
        } else
            onGetDataError();
    }

    protected void initListener() {
        Button button = findButton(R.id.show_image_btn_close);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected void initViewPager(RESP_Gallery resp_gallery) {
        ViewPager viewPager = (ViewPager) findViewById(R.id.show_image_viewpager);
        ShowImageAdapter parkingDetailAdapter = new ShowImageAdapter(getSupportFragmentManager(), resp_gallery.getData());
        viewPager.setAdapter(parkingDetailAdapter);

        if (resp_gallery.getPosition() != null)
            viewPager.setCurrentItem(resp_gallery.getPosition());
    }

    protected void onGetDataError() {
        showMaterialDialog(false, false, null, getString(R.string.error_try_again), null, getString(R.string.back), new DialogListener() {
            @Override
            public void negativeClicked() {
                closeDialog();
                finish();
            }

            @Override
            public void positiveClicked() {
                closeDialog();
                finish();
            }
        });
    }
}