package com.xtel.ivipbusiness.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.presenter.RegisterPresenter;
import com.xtel.ivipbusiness.view.activity.inf.IRegisterView;
import com.xtel.nipservicesdk.CallbackManager;
import com.xtel.nipservicesdk.callback.CallbackLisenerRegister;
import com.xtel.nipservicesdk.callback.CallbackListenerActive;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Register;
import com.xtel.nipservicesdk.utils.JsonHelper;
import com.xtel.nipservicesdk.utils.JsonParse;

public class RegisterActivity extends BasicActivity implements View.OnClickListener, IRegisterView {
    private RegisterPresenter presenter;
    private EditText edt_username, edt_pass, edt_re_pass;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        callbackManager = CallbackManager.create(this);
        presenter = new RegisterPresenter(this);

//        initToolbar(R.id.register_toolbar, null);
        initView();
    }

    /*
    * Khởi tạo các view trong layout
    * */
    private void initView() {
        edt_username = findEditText(R.id.register_edt_username);
        edt_pass = findEditText(R.id.register_edt_pass);
        edt_re_pass = findEditText(R.id.register_edt_re_pass);

        Button button = findButton(R.id.register_btn_register);
        Button btn_exists = findButton(R.id.register_btn_exists);
        button.setOnClickListener(this);
        btn_exists.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.register_btn_register) {
            presenter.registerAccount(edt_username.getText().toString(), edt_pass.getText().toString(), edt_re_pass.getText().toString());
        } else if (id == R.id.register_btn_exists)
            finish();
    }

    /*
    * Thông báo lỗi khi check input data
    * */
    @Override
    public void onValidateError(String error) {
        showShortToast(error);
    }

    /*
    * Đăng ký tài khoản với server
    * */
    @Override
    public void onRegisterAccount(final String phone, String password) {
        showProgressBar(false, false, null, getString(R.string.doing_register));

        callbackManager.registerNipService(phone, password, "", true, new CallbackLisenerRegister() {
            @Override
            public void onSuccess(final RESP_Register register) {
                closeProgressBar();
//                showShortToast(getString(R.string.success_register));
                presenter.startValidatePhone(phone);
            }

            @Override
            public void onError(final Error error) {
                closeProgressBar();
                showShortToast(JsonParse.getCodeMessage(error.getCode(), getString(R.string.error_register_phone)));
            }
        });
    }

    /*
    * Đăng ký tài khoản thành công
    * */
    @Override
    public void onRegisterAccountSuccess() {
        showShortToast(getString(R.string.success_register));
        finish();
    }

    /*
    * Validate số điện thoại bằng account kit thành công
    * */
    @Override
    public void onValidatePhoneToActiveSuccess(String auth_id) {
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
    * Thông báo không có internet
    * */
    @Override
    public void onNoInternet() {
        showShortToast(getString(R.string.error_no_internet));
    }

    /*
    * Gọi activity mới và lắng nghe kết quả trả về
    * */
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

    /*
    * Lắng nghe sự kiện khi hỏi quyền
    * */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        callbackManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
    }
}