package com.xtel.ivipbusiness.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.presenter.RegisterPresenter;
import com.xtel.ivipbusiness.view.activity.inf.IRegisterView;
import com.xtel.nipservicesdk.CallbackManager;
import com.xtel.nipservicesdk.callback.CallbackLisenerRegister;
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
        initView();
    }

    private void initView() {
        edt_username = (EditText) findViewById(R.id.register_edt_username);
        edt_pass = (EditText) findViewById(R.id.register_edt_pass);
        edt_re_pass = (EditText) findViewById(R.id.register_edt_re_pass);

        Button button = (Button) findViewById(R.id.register_btn_register);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.register_btn_register) {
            presenter.registerAccount(edt_username.getText().toString(), edt_pass.getText().toString(), edt_re_pass.getText().toString());
        }
    }

    @Override
    public void onValidateError(String error) {
        showShortToast(error);
    }

    @Override
    public void onRegisterAccount(String phone, String password) {
        showProgressBar(false, false, null, getString(R.string.doing_register));

        callbackManager.registerNipService(phone, password, "", true, new CallbackLisenerRegister() {
            @Override
            public void onSuccess(final RESP_Register register) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressBar();
                        debug(JsonHelper.toJson(register));
                        showShortToast(getString(R.string.success_register));
                    }
                }, 500);
            }

            @Override
            public void onError(final Error error) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressBar();
                        debug(JsonHelper.toJson(error));
                        showShortToast(JsonParse.getCodeMessage(error.getCode(), getString(R.string.error_register_phone)));
                    }
                }, 500);
            }
        });
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        startActivityAndFinish(LoginActivity.class);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        callbackManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}