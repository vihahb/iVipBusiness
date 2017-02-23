package com.xtel.ivipbusiness.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.presenter.SendFcmPresenter;
import com.xtel.ivipbusiness.view.activity.inf.ISendFcmView;
import com.xtel.ivipbusiness.view.adapter.TypeSaleAdapter;
import com.xtel.sdk.callback.DialogListener;

public class SendFcmActivity extends BasicActivity implements ISendFcmView {
    private SendFcmPresenter presenter;

    private EditText edt_begin_time;
    private Spinner sp_gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_fcm);

        presenter = new SendFcmPresenter(this);
        initToolbar(R.id.send_fcm_toolbar, null);
        initView();
        initGender();
        initListener();
        presenter.getData();
    }

//    Khởi tạo các widget
    private void initView() {
        edt_begin_time = findEditText(R.id.send_fcm_edt_begin_time);
    }

    //    Khởi tạo spinner để chọn giới tính
    private void initGender() {
        String[] arrayList = getResources().getStringArray(R.array.gender);
        sp_gender = findSpinner(R.id.send_fcm_sp_gender);
        TypeSaleAdapter typeAdapter = new TypeSaleAdapter(this, R.drawable.ic_action_gender, arrayList);
        sp_gender.setAdapter(typeAdapter);
    }

//    Khởi tạo sự kiệm khi click vào widget
    private void initListener() {

    }










    @Override 
    public void onGetDataError() {
        showMaterialDialog(false, false, null, getString(R.string.error_try_again), null, getString(R.string.back), new DialogListener() {
            @Override
            public void onClicked(Object object) {
                closeDialog();
                finish();
            }

            @Override
            public void onCancel() {
                closeDialog();
                finish();
            }
        });
    }

    @Override
    public Activity getActivity() {
        return this;
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}