package com.xtel.ivipbusiness.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.accountkit.AccountKit;
import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.presenter.LoginPresenter;
import com.xtel.ivipbusiness.view.activity.inf.ILoginView;
import com.xtel.nipservicesdk.CallbackManager;
import com.xtel.nipservicesdk.callback.CallbacListener;
import com.xtel.nipservicesdk.callback.CallbackListenerReactive;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Login;
import com.xtel.nipservicesdk.model.entity.RESP_Reactive;
import com.xtel.nipservicesdk.utils.JsonHelper;
import com.xtel.nipservicesdk.utils.JsonParse;
import com.xtel.sdk.callback.DialogListener;

/**
 * Created by Lê Công Long Vũ on 12/2/2016
 */

public class LoginActivity extends BasicActivity implements View.OnClickListener, ILoginView {
    private LoginPresenter presenter;
    private CallbackManager callbackManager;
    private EditText edt_username, edt_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AccountKit.initialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        callbackManager = CallbackManager.create(this);

        presenter = new LoginPresenter(this);
        initView();
    }

    private void initView() {
        edt_username = (EditText) findViewById(R.id.login_edt_username);
        edt_password = (EditText) findViewById(R.id.login_edt_pass);

        Button btn_login = (Button) findViewById(R.id.login_btn_login);
        Button btn_register = (Button) findViewById(R.id.login_btn_register);

        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }

    private void askActive(final String phone, final String password) {
        showMaterialDialog(true, true, null, getString(R.string.ask_active_now), getString(R.string.cancel), getString(R.string.ok), new DialogListener() {
            @Override
            public void onClicked(Object object) {
                closeDialog();
                reActiveAccount(phone, password);
            }

            @Override
            public void onCancel() {
                closeDialog();
            }
        });
    }

    private void reActiveAccount(String phone, String password) {
        callbackManager.reactiveNipAccount(phone, true, new CallbackListenerReactive() {
            @Override
            public void onSuccess(RESP_Reactive reactive) {

            }

            @Override
            public void onError(Error error) {
                showShortToast(JsonParse.getCodeMessage(error.getCode(), getString(R.string.error)));
            }
        });
    }

    private void activeAccount(String phone) {

    }

    @Override
    public void onValidateError(String error) {
        showShortToast(error);
    }

    @Override
    public void loginAccount(final String phone, final String password) {
        debug("login");
        callbackManager.LoginNipAcc(phone, password, new CallbacListener() {
            @Override
            public void onSuccess(RESP_Login success) {
                debug(JsonHelper.toJson(success));
                startActivity(HomeActivity.class);
            }

            @Override
            public void onError(Error error) {
                debug(JsonHelper.toJson(error));
                if (error.getCode() == 112) {
                    askActive(phone, password);
                } else {
                    showShortToast(JsonParse.getCodeMessage(error.getCode(), getString(R.string.error)));
                }
            }
        });
    }

    @Override
    public void startActivityAndFinish(Class clazz) {
        super.startActivityAndFinish(clazz);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.login_btn_login) {
            presenter.loginAccount(edt_username.getText().toString(), edt_password.getText().toString());
        } else if (id == R.id.login_btn_register) {
            presenter.registerAccount();
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finishAffinity();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        presenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
        callbackManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        presenter.onActivityResult(requestCode, resultCode, data);
    }
}