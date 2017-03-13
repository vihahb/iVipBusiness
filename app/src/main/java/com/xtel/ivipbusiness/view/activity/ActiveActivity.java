package com.xtel.ivipbusiness.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.presenter.ActivePresenter;
import com.xtel.ivipbusiness.view.activity.inf.IActiveView;
import com.xtel.nipservicesdk.CallbackManager;
import com.xtel.nipservicesdk.callback.CallbackListenerActive;
import com.xtel.nipservicesdk.callback.CallbackListenerReactive;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Reactive;
import com.xtel.nipservicesdk.utils.JsonParse;

public class ActiveActivity extends BasicActivity implements IActiveView {
    private ActivePresenter presenter;
    private EditText edt_username;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active);
        callbackManager = CallbackManager.create(this);

        presenter = new ActivePresenter(this);
//        initToolbar(R.id.active_toolbar, null);
        initView();
    }

    /*
    * Khởi tạo view và set sự kiện lắng nghe cho view trong layout
    * */
    private void initView() {
        edt_username = findEditText(R.id.active_edt_username);

        Button btn_active = findButton(R.id.active_btn_active);
        btn_active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reActiveAccount();
            }
        });
    }

    /*
    * Lấy lại mã active của tài khoản để kích hoạt tài khoản (Mã sẽ tự được lưu tự động)
    * */
    private void reActiveAccount() {
        callbackManager.reactiveNipAccount(edt_username.getText().toString(), true, new CallbackListenerReactive() {
            @Override
            public void onSuccess(RESP_Reactive reactive) {
//                activeAccount(auth_id);
                presenter.validatePhoneToActive(edt_username.getText().toString());
            }

            @Override
            public void onError(Error error) {
                closeProgressBar();
                showShortToast(JsonParse.getCodeMessage(error.getCode(), getString(R.string.have_error)));
            }
        });
    }

    /*
    * Bắt đầu kích hoạt tài khoản
    * */
    private void activeAccount(String auth_id) {
        debug("active now");
        callbackManager.activeNipAccount(auth_id, getString(R.string.type_phone), new CallbackListenerActive() {
            @Override
            public void onSuccess() {
                closeProgressBar();
                showShortToast(getString(R.string.success_active));
                finish();
            }

            @Override
            public void onError(Error error) {
                closeProgressBar();
                showShortToast(getString(R.string.error_active_account));
            }
        });
    }

    /*
    * Hiển thị thông báo khi có lỗi
    * */
    @Override
    public void onValidateError(String error) {
        showShortToast(error);
    }

    /*
    * Sự kiện validate số điện thoại bằng account kit thành công
    * */
    @Override
    public void onValidatePhoneToActiveSuccess(String auth_id) {
        showProgressBar(false, false, null, getString(R.string.doing_active));
        activeAccount(auth_id);
    }

    /*
    * Thông báo khi không có internet
    * */
    @Override
    public void onNoInternet() {
        showShortToast(getString(R.string.error_no_internet));
    }

    /*
    * Chuyển sang activity mới và hủy activity hiện tại
    * */
    @Override
    public void startActivityAndFinish(Class clazz) {
        super.startActivityAndFinish(clazz);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        presenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
        callbackManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}